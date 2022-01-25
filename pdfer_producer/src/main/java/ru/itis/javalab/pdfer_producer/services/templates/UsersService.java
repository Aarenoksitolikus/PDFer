package ru.itis.javalab.pdfer_producer.services.templates;

import ru.itis.javalab.pdfer_producer.dto.EmailPasswordDto;
import ru.itis.javalab.pdfer_producer.dto.TokenDto;
import ru.itis.javalab.pdfer_producer.dto.UserDto;
import ru.itis.javalab.pdfer_producer.entities.User;

import java.util.List;

public interface UsersService {
    List<UserDto> getAllUsers();
    void addUser(UserDto userDto);

    UserDto getUser(Long userId);
    boolean containsUser(String email, String password);
    User findByEmail(String email);
    void blockUser(Long userId);

    TokenDto signUp(EmailPasswordDto emailPassword) throws Throwable;
}
