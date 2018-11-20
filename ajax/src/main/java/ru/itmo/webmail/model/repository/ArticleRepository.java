package ru.itmo.webmail.model.repository;

import ru.itmo.webmail.model.domain.Article;

import java.util.List;

public interface ArticleRepository {
    void save(Article article);
    List<Article> findAll();
    List<Article> findByUserId(long userId);
    void setHidden(long articleId, boolean value);
}
