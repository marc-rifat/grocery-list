package com.example.springboot.grocerylist.service;

import com.example.springboot.grocerylist.entity.User;
import com.example.springboot.grocerylist.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(null);
    }

    public User register(String username, String password) throws Exception {
        // Check if username already exists
        if (userRepository.findByUsername(username).isPresent()) {
            throw new Exception("Username already exists");
        }

        // Create new user with encrypted password
        String encodedPassword = passwordEncoder.encode(password);
        User newUser = new User(username, encodedPassword, false);
        return userRepository.save(newUser);
    }

    public boolean isUsernameAvailable(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}