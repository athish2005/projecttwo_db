package com.example.projectthree.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.projectthree.entity.User;
import com.example.projectthree.repository.UserRepository;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // ✅ Fetch the user from DB by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));

        // ✅ Return a Spring Security UserDetails object
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),                                   // username (email)
                user.getPassword(),                                // encoded password
                Collections.singleton(() -> "ROLE_" + user.getRole().name()) // authority
        );
    }
}
