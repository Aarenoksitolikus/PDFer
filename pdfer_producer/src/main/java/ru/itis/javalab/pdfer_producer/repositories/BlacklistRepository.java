package ru.itis.javalab.pdfer_producer.repositories;

public interface BlacklistRepository {
    void save(String token);
    boolean exists(String token);
}
