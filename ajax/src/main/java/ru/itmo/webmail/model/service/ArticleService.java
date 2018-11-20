package ru.itmo.webmail.model.service;

import ru.itmo.webmail.model.domain.Article;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.repository.ArticleRepository;
import ru.itmo.webmail.model.repository.impl.ArticleRepositoryImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleService {
    private ArticleRepository articleRepository = new ArticleRepositoryImpl();
    private UserService userService = new UserService();

    public void validateArticle(Article article) throws ValidationException {
        if (article.getTitle() == null || article.getTitle().isEmpty()) {
            throw new ValidationException("Title is required");
        }
        if (article.getText() == null || article.getText().isEmpty()) {
            throw new ValidationException("Text is required");
        }
        if (article.getTitle().length() > 255) {
            throw new ValidationException("Title is too long");
        }
        if (article.getText().length() > 1400) {
            throw new ValidationException("Text is too ling");
        }
    }

    public void create(Article article) {
        articleRepository.save(article);
    }

    public void setHidden(long articleId, boolean value) {
        articleRepository.setHidden(articleId, value);
    }

    public List <ArticleView> findAll() {
        List <Article> articles = articleRepository.findAll();
        List <ArticleView> result = new ArrayList<>(articles.size());
        for (Article article : articles) {
            result.add(new ArticleView(
                    userService.find(article.getUserId()),
                    article.getTitle(),
                    article.getText(),
                    article.getCreationTime()
            ));
        }
        return result;
    }

    public List <Article> findByUserId(long userId) {
        return articleRepository.findByUserId(userId);
    }

    public static class ArticleView {
        private final User author;
        private final String title;
        private final String text;
        private final String creationTime;
        public ArticleView(User author, String title, String text, Date creationTime) {
            this.author = author;
            this.title = title;
            this.text = text;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            this.creationTime = dateFormat.format(creationTime);
        }
    }
}
