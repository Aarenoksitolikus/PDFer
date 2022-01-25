package ru.itis.javalab.pdfer_producer.redis.repositories;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import ru.itis.javalab.pdfer_producer.redis.entities.RedisUser;

public interface RedisUsersRepository extends KeyValueRepository<RedisUser, String> {
    RedisUser findByUserId(Long userId);
}
