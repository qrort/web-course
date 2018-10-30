package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.service.UserService;
import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class EnterPage extends BasePage {
    private UserService userService = new UserService();

    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);
    }
    public void after(HttpServletRequest request, Map <String, Object> view) {
        super.after(request, view);
    }
    private void enter(HttpServletRequest request, Map<String, Object> view) {
        try {
            userService.validateEntrance(request.getParameter("login"), request.getParameter("password"));
        } catch (ValidationException e) {
            view.put("login", request.getParameter("login"));
            view.put("error", e.getMessage());
            return;
        }
        request.getSession().setAttribute("user", userService.findByLogin(request.getParameter("login")));
        throw new RedirectException("/index", "loggedIn");
    }

    private void action() {
        // No operations.
    }
}
