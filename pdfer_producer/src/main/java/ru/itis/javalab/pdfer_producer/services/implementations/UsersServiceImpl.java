package ru.itis.javalab.pdfer_producer.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.javalab.pdfer_producer.dto.EmailPasswordDto;
import ru.itis.javalab.pdfer_producer.dto.TokenDto;
import ru.itis.javalab.pdfer_producer.dto.UserDto;
import ru.itis.javalab.pdfer_producer.entities.User;
import ru.itis.javalab.pdfer_producer.redis.services.RedisUsersService;
import ru.itis.javalab.pdfer_producer.repositories.UsersRepository;
import ru.itis.javalab.pdfer_producer.services.templates.UsersService;

import java.util.List;
import java.util.function.Supplier;

import static ru.itis.javalab.pdfer_producer.dto.UserDto.from;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisUsersService redisUsersService;

    @Override
    public List<UserDto> getAllUsers() {
        return from(usersRepository.findAll());
    }

    @Override
    public void addUser(UserDto userDto) {
        User newUser = User.builder()
                .username(userDto.getUsername())
                .hashPassword(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .role(User.Role.USER)
                .state(User.State.ACTIVE)
                .build();
        usersRepository.save(newUser);
    }

    @Override
    public UserDto getUser(Long userId) {
        return from(usersRepository.findById(userId).orElse(null));
    }

    @Override
    public boolean containsUser(String email, String password) {
        return usersRepository.findByEmailAndHashPassword(email, password).isPresent();
    }

    @Override
    public User findByEmail(String email) {
        return usersRepository.findByEmail(email).orElse(null);
    }

    @Override
    public void blockUser(Long userId) {
        User user = usersRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
        redisUsersService.addAllTokensToBlackList(user);
    }

    @Override
    public TokenDto signUp(EmailPasswordDto emailPassword) throws Throwable {
        var user = usersRepository.findByEmail(emailPassword.getEmail())
                .orElseThrow((Supplier<Throwable>) () -> new UsernameNotFoundException("User not found"));
        return redisUsersService.addUser(user);
    }
}
