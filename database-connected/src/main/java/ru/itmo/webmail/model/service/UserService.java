package ru.itmo.webmail.model.service;

import com.google.common.hash.Hashing;
import ru.itmo.webmail.model.domain.Event;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.repository.UserRepository;
import ru.itmo.webmail.model.repository.impl.UserRepositoryImpl;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static ru.itmo.webmail.web.page.Page.USER_ID_SESSION_KEY;

@SuppressWarnings("UnstableApiUsage")
public class UserService {
    private static final String USER_PASSWORD_SALT = "dc3475f2b301851b";

    private UserRepository userRepository = new UserRepositoryImpl();
    private EventService eventService = new EventService();

    public void validateRegistration(User user, String password) throws ValidationException {
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
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidationException("Email is required");
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
    }

    public void register(User user, String password) {
        String passwordSha = getPasswordSha(password);
        userRepository.save(user, passwordSha);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void validateEnter(String loginOrEmail, String password) throws ValidationException {
        if (loginOrEmail == null || loginOrEmail.isEmpty()) {
            throw new ValidationException("Login or email is required");
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
        User user = userRepository.findByLoginOrEmailAndPasswordSha(loginOrEmail, getPasswordSha(password));
        if (user == null) {
            throw new ValidationException("Invalid login or password");
        }
        if (!user.isConfirmed()) {
            throw new ValidationException("Please confirm your email");
        }
    }

    private String getPasswordSha(String password) {
        return Hashing.sha256().hashString(USER_PASSWORD_SALT + password,
                StandardCharsets.UTF_8).toString();
    }

    public User authorize(HttpServletRequest request, String loginOrEmail, String password) {
        User user = userRepository.findByLoginOrEmailAndPasswordSha(loginOrEmail, getPasswordSha(password));
        request.getSession(true).setAttribute(USER_ID_SESSION_KEY, user.getId());
        eventService.addEvent(new Event(user, Event.Type.ENTER));
        return user;
    }

    public void unauthorize(HttpServletRequest request, User user) {
        eventService.addEvent(new Event(user, Event.Type.LOGOUT));
        request.getSession().removeAttribute(USER_ID_SESSION_KEY);
    }

    public void confirm(long userId) {
        userRepository.confirm(userId);
    }

    public User find(long userId) {
        return userRepository.find(userId);
    }
    public User findByLogin(String login) {return userRepository.findByLogin(login);}
}
