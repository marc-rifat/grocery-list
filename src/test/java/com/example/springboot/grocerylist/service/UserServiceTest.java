package com.example.springboot.grocerylist.service;

import com.example.springboot.grocerylist.dao.UserRepository;
import com.example.springboot.grocerylist.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "password123", false);
        testUser.setId(1L);
        userList = Arrays.asList(testUser);
    }

    @Test
    void authenticate_WhenCredentialsValid_ShouldReturnUser() {
        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(testUser));

        User result = userService.authenticate("testuser", "password123");

        assertEquals(testUser, result);
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void authenticate_WhenCredentialsInvalid_ShouldReturnNull() {
        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(testUser));

        User result = userService.authenticate("testuser", "wrongpassword");

        assertNull(result);
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void register_WhenUsernameAvailable_ShouldCreateUser() throws Exception {
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.register("newuser", "password123");

        assertNotNull(result);
        verify(userRepository).findByUsername("newuser");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_WhenUsernameExists_ShouldThrowException() {
        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(testUser));

        Exception exception = assertThrows(Exception.class,
                () -> userService.register("testuser", "password123"));
        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository).findByUsername("testuser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void isUsernameAvailable_WhenUsernameAvailable_ShouldReturnTrue() {
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());

        boolean result = userService.isUsernameAvailable("newuser");

        assertTrue(result);
        verify(userRepository).findByUsername("newuser");
    }

    @Test
    void isUsernameAvailable_WhenUsernameExists_ShouldReturnFalse() {
        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(testUser));

        boolean result = userService.isUsernameAvailable("testuser");

        assertFalse(result);
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.findAll();

        assertEquals(userList, result);
        verify(userRepository).findAll();
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User result = userService.findById(1L);

        assertEquals(testUser, result);
        verify(userRepository).findById(1L);
    }

    @Test
    void findById_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.findById(1L));
        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository).findById(1L);
    }

    @Test
    void deleteById_ShouldDeleteUser() {
        userService.deleteById(1L);

        verify(userRepository).deleteById(1L);
    }
} 