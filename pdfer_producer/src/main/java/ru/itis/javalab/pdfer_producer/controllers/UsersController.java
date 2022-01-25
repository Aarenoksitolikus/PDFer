package ru.itis.javalab.pdfer_producer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.javalab.pdfer_producer.dto.EmailPasswordDto;
import ru.itis.javalab.pdfer_producer.dto.TokenDto;
import ru.itis.javalab.pdfer_producer.dto.UserDto;
import ru.itis.javalab.pdfer_producer.services.templates.LoginService;
import ru.itis.javalab.pdfer_producer.services.templates.TokenService;
import ru.itis.javalab.pdfer_producer.services.templates.UsersService;

import java.util.List;

@RestController
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody EmailPasswordDto emailPassword) {
        return ResponseEntity.ok(loginService.login(emailPassword));
    }

    @PostMapping("/signup")
    public ResponseEntity<TokenDto> signUp(@RequestBody EmailPasswordDto emailPassword) throws Throwable {
        return ResponseEntity.ok(usersService.signUp(emailPassword));
    }

    @PostMapping("/users")
    public ResponseEntity<List<UserDto>> users(@RequestHeader("X-TOKEN") String token) {
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestHeader("X-TOKEN") String accessToken,
                                            @RequestHeader("REFRESH-TOKEN") String refreshToken){
        return ResponseEntity.ok(tokenService.updateTokens(accessToken, refreshToken));
    }

    @PostMapping("/users/{user-id}/block")
    public ResponseEntity<?> blockUser(@PathVariable("user-id") Long userId) {
        usersService.blockUser(userId);
        return ResponseEntity.ok().build();
    }
}
