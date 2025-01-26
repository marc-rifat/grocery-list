package com.example.springboot.grocerylist.service;

import com.example.springboot.grocerylist.dao.UserRepository;
import com.example.springboot.grocerylist.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "testpass";
    private static final String ENCODED_PASSWORD = "encodedpass";

    @BeforeEach
    void setUp() {
        testUser = new User(1L, TEST_USERNAME, ENCODED_PASSWORD, false);
    }

    @Test
    void authenticate_WhenCredentialsValid_ShouldReturnUser() {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(TEST_PASSWORD, ENCODED_PASSWORD)).thenReturn(true);

        User result = userService.authenticate(TEST_USERNAME, TEST_PASSWORD);

        assertNotNull(result);
        assertEquals(TEST_USERNAME, result.getUsername());
        verify(passwordEncoder).matches(TEST_PASSWORD, ENCODED_PASSWORD);
    }

    @Test
    void authenticate_WhenCredentialsInvalid_ShouldReturnNull() {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(TEST_PASSWORD, ENCODED_PASSWORD)).thenReturn(false);

        User result = userService.authenticate(TEST_USERNAME, TEST_PASSWORD);

        assertNull(result);
        verify(passwordEncoder).matches(TEST_PASSWORD, ENCODED_PASSWORD);
    }

    @Test
    void authenticate_WhenUserNotFound_ShouldReturnNull() {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        User result = userService.authenticate(TEST_USERNAME, TEST_PASSWORD);

        assertNull(result);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void register_WhenUsernameAvailable_ShouldCreateUser() throws Exception {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.register(TEST_USERNAME, TEST_PASSWORD);

        assertNotNull(result);
        assertEquals(TEST_USERNAME, result.getUsername());
        assertEquals(ENCODED_PASSWORD, result.getPassword());
        verify(passwordEncoder).encode(TEST_PASSWORD);
    }

    @Test
    void register_WhenUsernameExists_ShouldThrowException() {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(testUser));

        Exception exception = assertThrows(Exception.class, () -> userService.register(TEST_USERNAME, TEST_PASSWORD));

        assertEquals("Username already exists", exception.getMessage());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void isUsernameAvailable_WhenUsernameNotTaken_ShouldReturnTrue() {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        boolean result = userService.isUsernameAvailable(TEST_USERNAME);

        assertTrue(result);
    }

    @Test
    void isUsernameAvailable_WhenUsernameTaken_ShouldReturnFalse() {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(testUser));

        boolean result = userService.isUsernameAvailable(TEST_USERNAME);

        assertFalse(result);
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        List<User> expectedUsers = Arrays.asList(
                testUser,
                new User(2L, "user2", "pass2", false));
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> result = userService.findAll();

        assertEquals(expectedUsers.size(), result.size());
        assertEquals(expectedUsers, result);
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(testUser, result);
    }

    @Test
    void findById_WhenUserNotExists_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> userService.findById(1L));

        assertEquals("User not found with id: 1", exception.getMessage());
    }
}