package ru.itmo.webmail.model.repository;

import ru.itmo.webmail.model.domain.Message;

import java.util.List;

public interface MessageRepository {
    void save(Message message);
    List<Message> findAll(long userId);
}
