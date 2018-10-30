package ru.itmo.webmail.web.page;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class IndexPage extends BasePage {
    public void before(HttpServletRequest request, Map <String, Object> view) {
        super.before(request, view);
    }
    public void after(HttpServletRequest request, Map <String, Object> view) {
        super.after(request, view);
    }
    private void action() {
        // No operations.
    }

    private void registrationDone(Map<String, Object> view) {
        view.put("message", "You have been registered");
    }
    private void loggedIn(Map<String, Object> view) { view.put("message", "You have logged in"); }
    private void loggedOut(Map<String, Object> view) { view.put("message", "You have logged out"); }

}
