package ru.itis.javalab.pdfer_producer.services.templates;

import ru.itis.javalab.pdfer_producer.dto.EmailPasswordDto;
import ru.itis.javalab.pdfer_producer.dto.TokenDto;

public interface LoginService {
    TokenDto login(EmailPasswordDto emailPassword);
}
