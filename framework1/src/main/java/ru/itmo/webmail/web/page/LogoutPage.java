package ru.itmo.webmail.web.page;

import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class LogoutPage extends BasePage {
    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);
    }
    public void after(HttpServletRequest request, Map <String, Object> view) {
        super.after(request, view);
    }
    private void action(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        throw new RedirectException("/index", "loggedOut");
    }
}
