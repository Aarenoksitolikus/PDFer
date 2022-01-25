package ru.itis.javalab.pdfer_producer.redis.services;

import ru.itis.javalab.pdfer_producer.dto.TokenDto;
import ru.itis.javalab.pdfer_producer.entities.User;

public interface RedisUsersService {

    void addTokenToUser(User user, String token, String refresh);

    void addAllTokensToBlackList(User user);

    String getRefreshToken(String userId);

    TokenDto addUser(User user);
}