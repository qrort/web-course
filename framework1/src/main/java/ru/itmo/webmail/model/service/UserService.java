package ru.itmo.webmail.model.service;

import com.google.common.hash.Hashing;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.repository.UserRepository;
import ru.itmo.webmail.model.repository.impl.UserRepositoryImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class UserService {
    private static final String USER_PASSWORD_SALT = "dc3475f2b301851b";

    private UserRepository userRepository = new UserRepositoryImpl();

    public void validateEntrance(String login, String password) throws ValidationException {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new ValidationException("No such user");
        }
        String passwordSha1 = Hashing.sha256().hashString(USER_PASSWORD_SALT + password,
                StandardCharsets.UTF_8).toString();
        if (!user.getPasswordSha1().equals(passwordSha1)) {
            throw new ValidationException("Incorrect password");
        }
    }

    public void validateRegistration(User user, String password, String passwordConfirmation) throws ValidationException {
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new ValidationException("Login is required");
        }
        if (!user.getLogin().matches("[a-z]+")) {
            throw new ValidationException("Login can contain only lowercase Latin letters");
        }
        if (user.getLogin().length() > 8) {
            throw new ValidationException("Login can't be longer than 8");
        }
        if (userRepository.findByLogin(user.getLogin()) != null) {
            throw new ValidationException("Login is already in use");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ValidationException("Email is already in use");
        }
        if (!user.getEmail().matches(".*@.*")) {
            throw new ValidationException("Email is not correct");
        }
        if (password == null || password.isEmpty()) {
            throw new ValidationException("Password is required");
        }
        if (password.length() < 4) {
            throw new ValidationException("Password can't be shorter than 4");
        }
        if (password.length() > 32) {
            throw new ValidationException("Password can't be longer than 32");
        }
        if (!password.equals(passwordConfirmation)) {
            throw new ValidationException("Passwords don't match");
        }
    }

    public void register(User user, String password) {
        user.setPasswordSha1(Hashing.sha256().hashString(USER_PASSWORD_SALT + password,
                StandardCharsets.UTF_8).toString());
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
    public long findCount() {return userRepository.findCount(); }
    public User findByLogin(String login) {return userRepository.findByLogin(login);}
}
