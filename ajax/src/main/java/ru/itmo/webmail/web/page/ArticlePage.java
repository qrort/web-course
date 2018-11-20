package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.Article;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ArticlePage extends Page {
    private Map <String, Object> create(HttpServletRequest request, Map<String, Object> view) {
        Article article = new Article();
        article.setTitle(request.getParameter("title"));
        article.setText(request.getParameter("text"));
        article.setUserId(getUser().getId());
        try {
            getArticleService().validateArticle(article);
        } catch (ValidationException e) {
            view.put("success", false);
            view.put("error", e.getMessage());
            return view;
        }
        getArticleService().create(article);
        view.put("success", true);
        view.put("error", "wut");
        return view;
    }

    @Override
    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);
        if (getUser() == null) {
            throw new RedirectException("/index", "unauthorized");
        }
    }

    void action(HttpServletRequest request, Map<String, Object> view) {
        //No operations.
    };
}
