package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.Message;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.RepositoryException;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.service.MessageService;
import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TalksPage extends Page {
    private class MessageOut {
        String sourceUser;
        String targetUser;
        String text;
    }
    private MessageService messageService = new MessageService();

    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);
       if (getUser() == null) {
           throw new RedirectException("/index", "unauthorized");
       }
    }

    private void send(HttpServletRequest request, Map <String, Object> view) {
        Message message = new Message();
        message.setSourceUserId(getUser().getId());
        message.setText(request.getParameter("text"));
        String targetUserLogin = request.getParameter("target_user_login");
        User targetUser = getUserService().findByLogin(targetUserLogin);
        if (targetUser != null) {
            message.setTargetUserId(targetUser.getId());
            try {
                messageService.validateMessage(message);
                messageService.send(message);
            } catch (ValidationException e) {
                view.put("target_user_login", targetUserLogin);
                view.put("text", message.getText());
                view.put("error", e.getMessage());
                return;
            }
        } else {
            view.put("target_user_login", targetUserLogin);
            view.put("text", message.getText());
            view.put("error", "Incorrect recipient");
            return;
        }
    }

    private void action(HttpServletRequest request, Map<String, Object> view) {
        List <Message> messages = messageService.findAll(getUser().getId());
        for (Message message : messages) {
            User sourceUser = getUserService().find(message.getSourceUserId());
            message.setSourceUserLogin(sourceUser.getLogin());
            User targerUser = getUserService().find(message.getTargetUserId());
            message.setTargetUserLogin(targerUser.getLogin());
        }
        view.put("messages", messages);
    }

    public void after(HttpServletRequest request, Map<String, Object> view) {
        action(request, view);
    }
}
