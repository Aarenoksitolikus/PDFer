package ru.itis.javalab.pdfer_producer.security.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itis.javalab.pdfer_producer.security.jwt.JwtUtils;
import ru.itis.javalab.pdfer_producer.services.templates.JwtBlacklistService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JwtBlacklistService blacklistService;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String token = request.getHeader("X-TOKEN");

        if (token != null) {
            if (blacklistService.exists(token) || isExpired(token)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            TokenAuthentication tokenAuthentication = new TokenAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(tokenAuthentication);
        }
        filterChain.doFilter(request, response);
    }

    private boolean isExpired(String tokenValue) {
        return jwtUtils.validateJwtToken(tokenValue).getExpiresAt().before(new Date());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().equals("/refresh");
    }
}
