package com.budgetbuddy.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.budgetbuddy.model.User;
import com.budgetbuddy.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody Map<String, String> body) {
        return userService.register(
                body.get("username"),
                body.get("email"),
                body.get("password")
        );
    }

    @PostMapping("/login")
    public User login(@RequestBody Map<String, String> body, HttpSession session) {
        User user = userService.login(body.get("username"), body.get("password"));
        session.setAttribute("userId",   user.getId());
        session.setAttribute("username", user.getUsername());
        return user;
    }

    @PostMapping("/logout")
    public Map<String, String> logout(HttpSession session) {
        session.invalidate();
        return Map.of("message", "Berhasil keluar");
    }

    @GetMapping("/me")
    public User me(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return userService.getUserById(userId);
    }
}
