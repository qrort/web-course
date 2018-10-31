package ru.itmo.webmail.web.page;

import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class LogoutPage extends BasePage {
    private void action(HttpServletRequest request) {
        unauthorize(request);
        throw new RedirectException("/index", "loggedOut");
    }
}
