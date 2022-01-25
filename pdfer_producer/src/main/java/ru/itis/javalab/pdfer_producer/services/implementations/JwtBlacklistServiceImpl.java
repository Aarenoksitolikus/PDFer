package ru.itis.javalab.pdfer_producer.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.javalab.pdfer_producer.repositories.BlacklistRepository;
import ru.itis.javalab.pdfer_producer.services.templates.JwtBlacklistService;

@Service
public class JwtBlacklistServiceImpl implements JwtBlacklistService {

    @Autowired
    private BlacklistRepository blacklistRepository;

    @Override
    public void add(String token) {
        blacklistRepository.save(token);
    }

    @Override
    public boolean exists(String token) {
        return blacklistRepository.exists(token);
    }
}
