package ru.itis.javalab.pdfer_producer.services.templates;

public interface JwtBlacklistService {
    void add(String token);

    boolean exists(String token);
}
