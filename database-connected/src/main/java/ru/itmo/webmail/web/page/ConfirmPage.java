package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.exception.RepositoryException;
import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ConfirmPage extends Page {
    private void action(HttpServletRequest request, Map<String, Object> view) {
        try {
            getUserService().tryToConfirm(request.getParameter("secret"));
        } catch (RepositoryException e) {
            throw new RedirectException("/index", "confirmationFailed");
        }
        throw new RedirectException("/index", "confirmationAccepted");
    }
}
