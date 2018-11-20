package ru.itmo.webmail.model.repository.impl;

import ru.itmo.webmail.model.database.DatabaseUtils;
import ru.itmo.webmail.model.domain.Article;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.RepositoryException;
import ru.itmo.webmail.model.repository.ArticleRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleRepositoryImpl implements ArticleRepository {
    private static final DataSource DATA_SOURCE = DatabaseUtils.getDataSource();

    @Override
    public void save(Article article) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Article (userId, title, text, creationTime) VALUES (?, ?, ?, CURRENT_TIMESTAMP)",
                    Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, article.getUserId());
                statement.setString(2, article.getTitle());
                statement.setString(3, article.getText());
                if (statement.executeUpdate() == 1) {
                    ResultSet generatedIdResultSet = statement.getGeneratedKeys();
                    if (generatedIdResultSet.next()) {
                        article.setId(generatedIdResultSet.getLong(1));
                        article.setCreationTime(findCreationTime(article.getId()));
                    } else {
                        throw new RepositoryException("Can't find id of saved Article.");
                    }
                } else {
                    throw new RepositoryException("Can't save Article.");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't save User.", e);
        }
    }

    @Override
    public List<Article> findAll() {
        List<Article> articles = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM Article WHERE hidden=FALSE ORDER BY id DESC")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        articles.add(toArticle(statement.getMetaData(), resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find all users.", e);
        }
        return articles;
    }

    @Override
    public List<Article> findByUserId(long userId) {
        List<Article> articles = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM Article WHERE userId=? ORDER BY id DESC")) {
                statement.setLong(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        articles.add(toArticle(statement.getMetaData(), resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find all users.", e);
        }
        return articles;
    }

    @Override
    public void setHidden(long articleId, boolean x) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE Article SET hidden=? WHERE id=?")) {
                statement.setBoolean(1, x);
                statement.setLong(2, articleId);
                if (statement.executeUpdate() != 1) {
                    throw new RepositoryException("Can't update Article.hidden");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Article by id.");
        }
    }

    private Article toArticle(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        Article article = new Article();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String columnName = metaData.getColumnName(i);
            if ("id".equalsIgnoreCase(columnName)) {
                article.setId(resultSet.getLong(i));
            } else if ("userId".equalsIgnoreCase(columnName)) {
                article.setUserId(resultSet.getLong(i));
            } else if ("title".equalsIgnoreCase(columnName)) {
                article.setTitle(resultSet.getString(i));
            } else if ("text".equalsIgnoreCase(columnName)) {
                article.setText(resultSet.getString(i));
            } else if ("creationTime".equalsIgnoreCase(columnName)) {
                article.setCreationTime(resultSet.getTimestamp(i));
            } else if ("hidden".equalsIgnoreCase(columnName)) {
                article.setHidden(resultSet.getBoolean(i));
            } else {
                throw new RepositoryException("Unexpected column 'Article." + columnName + "'.");
            }
        }
        return article;
    }

    private Date findCreationTime(long articleId) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT creationTime FROM Article WHERE id=?")) {
                statement.setLong(1, articleId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getTimestamp(1);
                    }
                }
                throw new RepositoryException("Can't find Article.creationTime by id.");
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find Article.creationTime by id.", e);
        }
    }
}
