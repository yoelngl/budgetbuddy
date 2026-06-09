package com.budgetbuddy.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.budgetbuddy.exception.AuthenticationException;
import com.budgetbuddy.exception.DuplicateUserException;
import com.budgetbuddy.exception.ResourceNotFoundException;
import com.budgetbuddy.model.User;
import com.budgetbuddy.repository.UserRepository;
import com.budgetbuddy.util.PasswordUtil;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String username, String email, String password) {
        if (username == null || username.isBlank()) {
            throw new InvalidInputException("Username tidak boleh kosong");
        }
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUserException("Username '" + username + "' sudah digunakan");
        }
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateUserException("Email '" + email + "' sudah terdaftar");
        }
        User user = new User(username, email, PasswordUtil.hash(password));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Username atau password salah"));
        if (!PasswordUtil.verify(password, user.getPassword())) {
            throw new AuthenticationException("Username atau password salah");
        }
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    // Inner static helper for input validation (demonstrates static inner class)
    private static class InvalidInputException extends com.budgetbuddy.exception.InvalidOperationException {
        InvalidInputException(String msg) { super(msg); }
    }
}
