package ru.itis.javalab.pdfer_producer.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.javalab.pdfer_producer.dto.TokenDto;
import ru.itis.javalab.pdfer_producer.redis.services.RedisUsersService;
import ru.itis.javalab.pdfer_producer.repositories.UsersRepository;
import ru.itis.javalab.pdfer_producer.security.jwt.JwtUtils;
import ru.itis.javalab.pdfer_producer.services.templates.JwtBlacklistService;
import ru.itis.javalab.pdfer_producer.services.templates.TokenService;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RedisUsersService redisUsersService;

    @Autowired
    private JwtBlacklistService jwtBlacklistService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public TokenDto updateTokens(String accessToken, String refreshToken) {
        var decodedJWT = jwtUtils.validateJwtToken(accessToken);
        var user = usersRepository.findById(Long.valueOf(decodedJWT.getSubject()))
                .orElse(null);

        if (redisUsersService.getRefreshToken(user.getRedisId())
                .equals(refreshToken)) {
            jwtBlacklistService.add(accessToken);
            String newAccessToken = jwtUtils.generateAccessToken(user);
            String newRefreshToken = jwtUtils.generateRefreshToken(user);
            redisUsersService.addTokenToUser(user, newAccessToken, newRefreshToken);
            return TokenDto.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        } else {
            throw new IllegalArgumentException("refresh token not found");
        }
    }
}
