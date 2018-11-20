package ru.itmo.webmail.web.page;

import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class MyArticlesPage extends Page {
    @Override
    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);
        if (getUser() == null) {
            throw new RedirectException("/index", "unauthorized");
        }
    }

    public void action(HttpServletRequest request, Map<String, Object> view) {
        view.put("myArticles", getArticleService().findByUserId(getUser().getId()));
    }

    private void changePublicity(HttpServletRequest request, Map<String, Object> view) {
        String value = request.getParameter("value");
        String articleIdParameter = request.getParameter("articleId");
        articleIdParameter = articleIdParameter.trim().replaceAll("\n| ", "");
        long articleId = Long.parseLong(articleIdParameter);
        if ("Show".equals(value)) {
            getArticleService().setHidden(articleId, false);
        } else {
            getArticleService().setHidden(articleId, true);
        }
    }
}
