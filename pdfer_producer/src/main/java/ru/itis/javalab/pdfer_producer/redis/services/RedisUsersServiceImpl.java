package ru.itis.javalab.pdfer_producer.redis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.javalab.pdfer_producer.dto.TokenDto;
import ru.itis.javalab.pdfer_producer.entities.User;
import ru.itis.javalab.pdfer_producer.redis.entities.RedisUser;
import ru.itis.javalab.pdfer_producer.redis.repositories.RedisUsersRepository;
import ru.itis.javalab.pdfer_producer.repositories.UsersRepository;
import ru.itis.javalab.pdfer_producer.security.jwt.JwtUtils;
import ru.itis.javalab.pdfer_producer.services.templates.JwtBlacklistService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RedisUsersServiceImpl implements RedisUsersService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JwtBlacklistService blacklistService;

    @Autowired
    private RedisUsersRepository redisUsersRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void addTokenToUser(User user, String token, String refresh) {
        String redisId = user.getRedisId();

        RedisUser redisUser;
        if (redisId != null) {
            redisUser = redisUsersRepository.findById(redisId).orElseThrow(IllegalArgumentException::new);
            if (redisUser.getTokens() == null) {
                redisUser.setTokens(new ArrayList<>());
            }
            redisUser.getTokens().add(token);
            redisUser.setRefreshToken(refresh);
        } else {
            redisUser = RedisUser.builder()
                    .userId(user.getId())
                    .tokens(Collections.singletonList(token))
                    .refreshToken(refresh)
                    .build();
        }
        redisUsersRepository.save(redisUser);
        user.setRedisId(redisUser.getId());
        usersRepository.save(user);
    }

    @Override
    public void addAllTokensToBlackList(User user) {
        if (user.getRedisId() != null) {
            RedisUser redisUser = redisUsersRepository.findById(user.getRedisId())
                    .orElseThrow(IllegalArgumentException::new);

            List<String> tokens = redisUser.getTokens();
            for (String token : tokens) {
                blacklistService.add(token);
            }
            redisUser.getTokens().clear();
            redisUsersRepository.save(redisUser);
        }
    }

    @Override
    public String getRefreshToken(String userId) {
        return redisUsersRepository.findById(userId).orElseThrow(IllegalArgumentException::new).getRefreshToken();
    }

    @Override
    public TokenDto addUser(User user) {
        var accessToken = jwtUtils.generateAccessToken(user);
        var redisUser = RedisUser.builder()
                .userId(user.getId())
                .refreshToken(jwtUtils.generateRefreshToken(user))
                .tokens(Collections.singletonList(accessToken))
                .build();
        redisUsersRepository.save(redisUser);
        user.setRedisId(redisUsersRepository.findByUserId(user.getId()).getId());
        usersRepository.save(user);
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(redisUser.getRefreshToken())
                .build();
    }
}
