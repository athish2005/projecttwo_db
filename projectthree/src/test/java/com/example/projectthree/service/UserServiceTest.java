package com.example.projectthree.service;


import com.example.projectthree.entity.User;
import com.example.projectthree.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1L)
                .name("Alice")
                .email("alice@example.com")
                .password("plainPassword")
                .role(User.Role.USER)
                .build();
    }

    // ✅ Test register user (password encoding + default role)
    @Test
    void testRegisterUser_Success() {
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.registerUser(user);

        assertNotNull(result);
        assertEquals(User.Role.USER, result.getRole());
        verify(passwordEncoder, times(1)).encode("plainPassword");
        verify(userRepository, times(1)).save(user);
    }

    // ✅ Test register user assigns default role when null
    @Test
    void testRegisterUser_AssignDefaultRole() {
        user.setRole(null);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.registerUser(user);

        assertEquals(User.Role.USER, result.getRole());
        verify(userRepository, times(1)).save(user);
    }

    // ✅ Test get user by ID success
    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertEquals("alice@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    // ❌ Test get user by ID not found
    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.getUserById(1L)
        );

        assertEquals("User not found", ex.getMessage());
    }

    // ✅ Test get all users
    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    // ✅ Test get user by email success
    @Test
    void testGetUserByEmail_Success() {
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(user));

        User result = userService.getUserByEmail("alice@example.com");

        assertNotNull(result);
        assertEquals("Alice", result.getName());
        verify(userRepository, times(1)).findByEmail("alice@example.com");
    }

    // ✅ Test get user by email when not found (should return null)
    @Test
    void testGetUserByEmail_NotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        User result = userService.getUserByEmail("unknown@example.com");

        assertNull(result);
    }

    // ✅ Test strict getUserOrThrow success
    @Test
    void testGetUserOrThrow_Success() {
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(user));

        User result = userService.getUserOrThrow("alice@example.com");

        assertEquals("Alice", result.getName());
    }

    // ❌ Test strict getUserOrThrow not found
    @Test
    void testGetUserOrThrow_NotFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.getUserOrThrow("missing@example.com")
        );

        assertEquals("User not found", ex.getMessage());
    }
}
