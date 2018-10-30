package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public abstract class BasePage {
    public void before(HttpServletRequest request, Map<String, Object> view) {
        UserService userService = new UserService();
        view.put("userCount", userService.findCount());
    }
    public void after(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            view.put("userLogin", user.getLogin());
        }
    }
}
