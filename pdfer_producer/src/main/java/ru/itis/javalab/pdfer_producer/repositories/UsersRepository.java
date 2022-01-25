package ru.itis.javalab.pdfer_producer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.javalab.pdfer_producer.entities.User;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndHashPassword(String email, String password);
    Optional<User> findByEmail(String email);
}
