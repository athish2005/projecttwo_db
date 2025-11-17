package com.example.projectthree.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.projectthree.entity.User;
import com.example.projectthree.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Register new user (hash password before saving)
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Optional: Assign default role if not provided
      if (user.getRole() == null || user.getRole().toString().trim().isEmpty()) {
       user.setRole(User.Role.USER);
     }

        return userRepository.save(user);
    }

    // ✅ Get user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✅ Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ SAFE version: Get user by email (returns null if not found)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // ✅ (Optional) Strict version: Throws if user missing — for admin logic
    public User getUserOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
