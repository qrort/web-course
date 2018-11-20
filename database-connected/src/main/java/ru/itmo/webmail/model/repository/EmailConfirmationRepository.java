package ru.itmo.webmail.model.repository;

public interface EmailConfirmationRepository {
    void save(long userId);
    long findUserIdBySecret(String secret);
}
