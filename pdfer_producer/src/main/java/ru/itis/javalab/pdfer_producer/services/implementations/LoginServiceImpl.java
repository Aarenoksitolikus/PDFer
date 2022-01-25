package ru.itis.javalab.pdfer_producer.services.implementations;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.javalab.pdfer_producer.dto.EmailPasswordDto;
import ru.itis.javalab.pdfer_producer.dto.TokenDto;
import ru.itis.javalab.pdfer_producer.redis.services.RedisUsersService;
import ru.itis.javalab.pdfer_producer.repositories.UsersRepository;
import ru.itis.javalab.pdfer_producer.security.jwt.JwtUtils;
import ru.itis.javalab.pdfer_producer.services.templates.LoginService;

import java.util.function.Supplier;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RedisUsersService redisUsersService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @SneakyThrows
    @Override
    public TokenDto login(EmailPasswordDto emailPassword) {
        var user = usersRepository.findByEmail(emailPassword.getEmail())
                .orElseThrow((Supplier<Throwable>) () -> new UsernameNotFoundException("User not found"));

        if (passwordEncoder.matches(emailPassword.getPassword(), user.getHashPassword())) {
            String accessToken = jwtUtils.generateAccessToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(user);
            redisUsersService.addTokenToUser(user, accessToken, refreshToken);
            return TokenDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }
}
