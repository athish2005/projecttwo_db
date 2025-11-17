package com.example.projectthree.controller;


import com.example.projectthree.entity.User;
import com.example.projectthree.entity.User.Role;
import com.example.projectthree.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setRole(Role.USER);
    }

    // ✅ Test successful registration
    @Test
    void testRegisterUser_Success() {
        when(userService.getUserByEmail(testUser.getEmail())).thenReturn(null);

        ResponseEntity<?> response = authController.registerUser(testUser);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("User registered successfully", response.getBody());
        verify(userService, times(1)).registerUser(testUser);
    }

    // ❌ Test registration when email already exists
    @Test
    void testRegisterUser_EmailAlreadyExists() {
        when(userService.getUserByEmail(testUser.getEmail())).thenReturn(testUser);

        ResponseEntity<?> response = authController.registerUser(testUser);

        assertEquals(409, response.getStatusCodeValue());
        assertEquals("Email already registered", response.getBody());
        verify(userService, never()).registerUser(any());
    }

    // ✅ Test successful login
    @Test
    void testLogin_Success() {
        User loginRequest = new User();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("plainPassword");

        when(userService.getUserByEmail("john@example.com")).thenReturn(testUser);
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(200, response.getStatusCodeValue());

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("success", body.get("status"));
        assertEquals("Login successful", body.get("message"));
        assertEquals("USER", body.get("role"));
    }

    // ❌ Test login when user not found
    @Test
    void testLogin_UserNotFound() {
        User loginRequest = new User();
        loginRequest.setEmail("unknown@example.com");
        loginRequest.setPassword("password");

        when(userService.getUserByEmail("unknown@example.com")).thenReturn(null);

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(404, response.getStatusCodeValue());

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("error", body.get("status"));
        assertEquals("User not found", body.get("message"));
    }

    // ❌ Test login with invalid password
    @Test
    void testLogin_InvalidPassword() {
        User loginRequest = new User();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("wrongPassword");

        when(userService.getUserByEmail("john@example.com")).thenReturn(testUser);
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(401, response.getStatusCodeValue());

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("error", body.get("status"));
        assertEquals("Invalid credentials", body.get("message"));
    }

    // ⚙️ Test login when exception occurs
    @Test
    void testLogin_InternalServerError() {
        User loginRequest = new User();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("password");

        when(userService.getUserByEmail("john@example.com")).thenThrow(new RuntimeException("DB error"));

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(500, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("error", body.get("status"));
        assertEquals("An error occurred during login", body.get("message"));
    }
}
