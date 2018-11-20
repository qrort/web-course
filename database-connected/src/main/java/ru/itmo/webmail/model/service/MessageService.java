package ru.itmo.webmail.model.service;

import ru.itmo.webmail.model.domain.Message;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.repository.MessageRepository;
import ru.itmo.webmail.model.repository.impl.MessageRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class MessageService {
    private MessageRepository messageRepository = new MessageRepositoryImpl();
    private UserService userService = new UserService();

    public void send(Message message) {
        messageRepository.save(message);
    }

    public void validateMessage(User targetUser, String text) throws ValidationException {
        if (text == null || text.isEmpty()) {
            throw new ValidationException("Text is required");
        }
        if (text.length() > 255) {
            throw new ValidationException("Message is too long");
        }
        if (targetUser == null) {
            throw new ValidationException("Incorrect recipient");
        }
    }

    public List<MessageView> findAll(long userId) {
        List<Message> messages = messageRepository.findAll(userId);
        List<MessageView> result = new ArrayList<>(messages.size());
        for (Message message : messages) {
            result.add(new MessageView(
                    userService.find(message.getSourceUserId()),
                    userService.find(message.getTargetUserId()),
                    message.getText()
            ));
        }
        return result;
    }

    public static class MessageView {
        private final User sourceUser;
        private final User targetUser;
        private final String text;

        public MessageView(User sourceUser, User targetUser, String text) {
            this.sourceUser = sourceUser;
            this.targetUser = targetUser;
            this.text = text;
        }

        public User getSourceUser() {
            return sourceUser;
        }

        public User getTargetUser() {
            return targetUser;
        }

        public String getText() {
            return text;
        }
    }
}
