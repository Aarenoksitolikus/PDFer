package ru.itis.javalab.pdfer_producer.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.itis.javalab.pdfer_producer.entities.User;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtils {

    @Value("${rest.jwt.access-expiration-ms}")
    private int accessExpirationTime;

    @Autowired
    private Algorithm algorithm;

    public String generateAccessToken(User user) {
        return JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("username", user.getUsername())
                .withClaim("state", user.getState().toString())
                .withClaim("role", user.getRole().toString())
                .withExpiresAt(new Date(new Date().getTime() + accessExpirationTime))
                .sign(algorithm);
    }

    public String generateRefreshToken(User user) {
        return UUID.randomUUID().toString();
    }

    public DecodedJWT validateJwtToken(String token) {
        return JWT.require(algorithm).build().verify(token);
    }
}
