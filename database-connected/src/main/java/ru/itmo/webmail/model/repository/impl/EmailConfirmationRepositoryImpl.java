package ru.itmo.webmail.model.repository.impl;

import org.apache.commons.lang3.RandomStringUtils;
import ru.itmo.webmail.model.database.DatabaseUtils;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.RepositoryException;
import ru.itmo.webmail.model.repository.EmailConfirmationRepository;

import javax.sql.DataSource;
import java.sql.*;

public class EmailConfirmationRepositoryImpl implements EmailConfirmationRepository {
    private static final DataSource DATA_SOURCE = DatabaseUtils.getDataSource();

    @Override
    public void save(long userId) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO EmailConfirmation (userId, secret, creationTime) VALUES (?, ?, CURRENT_TIMESTAMP )",
                    Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, userId);
                statement.setString(2, RandomStringUtils.random(50, true, true));
                if (statement.executeUpdate() != 1) {
                    throw new RepositoryException("Can't save EmailConfirmation.");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't save EmailConfirmation.", e);
        }
    }

    @Override
    public long findUserIdBySecret(String secret) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM EmailConfirmation WHERE secret=?")) {
                statement.setString(1, secret);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        Long userId = toUserId(statement.getMetaData(), resultSet);
                        if (userId != null) {
                            return userId;
                        } else {
                            throw new RepositoryException("Can't find user by EmailConfirmation.");
                        }
                    } else {
                        throw new RepositoryException("Can't find EmailConfirmation by secret.");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find EmailConfirmation by secret.", e);
        }
    }

    private Long toUserId(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        Long userId = null;
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String columnName = metaData.getColumnName(i);
            if ("userId".equalsIgnoreCase(columnName)) {
                userId = resultSet.getLong(i);
            }
        }
        return userId;
    }
}
