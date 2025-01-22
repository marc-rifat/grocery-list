package com.example.springboot.grocerylist.service;

import com.example.springboot.grocerylist.entity.User;
import com.example.springboot.grocerylist.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password))
                .orElse(null);
    }

    public User register(String username, String password) throws Exception {
        // Check if username already exists
        if (userRepository.findByUsername(username).isPresent()) {
            throw new Exception("Username already exists");
        }

        // Create new user
        User newUser = new User(username, password, false);
        return userRepository.save(newUser);
    }

    public boolean isUsernameAvailable(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }
} 