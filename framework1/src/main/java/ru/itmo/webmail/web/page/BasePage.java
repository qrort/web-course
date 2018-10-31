package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public abstract class BasePage {
    private static final String USER_SESSION_ATTRIBUTE_NAME = "user";

    public void before(HttpServletRequest request, Map<String, Object> view) {
        UserService userService = new UserService();
        view.put("userCount", userService.findCount());
    }

    public void after(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute(USER_SESSION_ATTRIBUTE_NAME);
        if (user != null) {
            view.put("userLogin", user.getLogin());
        }
    }

    public void authorize(HttpServletRequest request, User user) {
        request.getSession().setAttribute(USER_SESSION_ATTRIBUTE_NAME, user);
    }

    public void unauthorize(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_SESSION_ATTRIBUTE_NAME);
    }
}
