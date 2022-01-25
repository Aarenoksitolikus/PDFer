package ru.itis.javalab.pdfer_producer.security.token;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.itis.javalab.pdfer_producer.security.jwt.JwtUtils;

@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var tokenAuthentication = (TokenAuthentication) authentication;
        DecodedJWT jwt;
        try {
            jwt = jwtUtils.validateJwtToken(authentication.getName());

        } catch (JWTVerificationException e) {
            throw new BadCredentialsException("Bad token");
        }
        tokenAuthentication.setAuthenticated(true);
        tokenAuthentication.setAuthority(jwt.getClaim("role").asString());
        return tokenAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TokenAuthentication.class.equals(authentication);
    }
}
