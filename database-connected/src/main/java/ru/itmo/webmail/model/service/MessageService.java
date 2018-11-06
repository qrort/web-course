package ru.itmo.webmail.model.service;

import ru.itmo.webmail.model.domain.Message;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.repository.MessageRepository;
import ru.itmo.webmail.model.repository.impl.MessageRepositoryImpl;

import java.util.List;

public class MessageService {
    private MessageRepository messageRepository = new MessageRepositoryImpl();

    public void send(Message message) {
        messageRepository.save(message);
    }

    public void validateMessage(Message message) throws ValidationException {
        if (message.getText().length() > 255) {
            throw new ValidationException("Message is too long");
        }
    }

    public List<Message> findAll(long userId) {
        return messageRepository.findAll(userId);
    }
}
