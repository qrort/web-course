package ru.itmo.webmail.web.page;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class IndexPage extends Page {
    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    private void registrationDone(HttpServletRequest request, Map<String, Object> view) {
        view.put("message", "You have been registered. Please check email for verification link.");
    }
    private void confirmationFailed(HttpServletRequest request, Map<String, Object> view) {
        view.put("message", "Incorrect secret.");
    }
    private void confirmationAccepted(HttpServletRequest request, Map<String, Object> view) {
        view.put("message", "Your account has been verified.");
    }
    private void unauthorized(HttpServletRequest request, Map<String, Object> view) {
        view.put("message", "Please, enter the system.");
    }
}
