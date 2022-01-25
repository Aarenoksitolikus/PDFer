package ru.itis.javalab.pdfer_producer.services.templates;

import ru.itis.javalab.pdfer_producer.dto.TokenDto;

public interface TokenService {
    TokenDto updateTokens(String accessToken, String refreshToken);
}
