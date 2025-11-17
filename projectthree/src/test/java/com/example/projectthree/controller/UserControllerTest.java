package com.example.projectthree.controller;


import com.example.projectthree.entity.User;
import com.example.projectthree.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1L)
                .name("Alice")
                .email("alice@example.com")
                .role(User.Role.USER)
                .build();
    }

    // ✅ Get all users
    @Test
    void testGetAllUsers() {
        when(userService.getAllUsers()).thenReturn(List.of(user));

        List<User> result = userController.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getName());
        verify(userService, times(1)).getAllUsers();
    }

    // ✅ Get user by ID
    @Test
    void testGetUserById() {
        when(userService.getUserById(1L)).thenReturn(user);

        User result = userController.getUserById(1L);

        assertNotNull(result);
        assertEquals("Alice", result.getName());
        verify(userService, times(1)).getUserById(1L);
    }

    // ✅ Get user by email
    @Test
    void testGetUserByEmail() {
        when(userService.getUserByEmail("alice@example.com")).thenReturn(user);

        User result = userController.getUserByEmail("alice@example.com");

        assertNotNull(result);
        assertEquals("Alice", result.getName());
        verify(userService, times(1)).getUserByEmail("alice@example.com");
    }
}
