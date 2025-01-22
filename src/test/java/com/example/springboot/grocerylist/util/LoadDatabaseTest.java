package com.example.springboot.grocerylist.util;

import com.example.springboot.grocerylist.dao.GroceryRepository;
import com.example.springboot.grocerylist.dao.UserRepository;
import com.example.springboot.grocerylist.entity.Grocery;
import com.example.springboot.grocerylist.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoadDatabaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroceryRepository groceryRepository;

    @InjectMocks
    private LoadDatabase loadDatabase;

    @BeforeEach
    void setUp() {
        // Set the required properties using ReflectionTestUtils
        ReflectionTestUtils.setField(loadDatabase, "adminUsername", "admin");
        ReflectionTestUtils.setField(loadDatabase, "adminPassword", "adminpass");
        ReflectionTestUtils.setField(loadDatabase, "defaultUsername", "user");
        ReflectionTestUtils.setField(loadDatabase, "defaultPassword", "userpass");
        ReflectionTestUtils.setField(loadDatabase, "browserUrl", "http://localhost:8080");
    }

    @Test
    void initDatabase_ShouldCreateSampleGroceriesAndUsers() throws Exception {
        // Arrange
        when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());

        // Act
        loadDatabase.initDatabase(groceryRepository).run();

        // Assert
        verify(groceryRepository, times(3)).save(any(Grocery.class));
        verify(userRepository).save(argThat(user -> 
            user.getUsername().equals("admin") && user.isAdmin()));
        verify(userRepository).save(argThat(user -> 
            user.getUsername().equals("user") && !user.isAdmin()));
    }

    @Test
    void initDatabase_WhenUsersExist_ShouldNotCreateDuplicates() throws Exception {
        // Arrange
        User existingAdmin = new User("admin", "adminpass", true);
        User existingUser = new User("user", "userpass", false);
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(existingAdmin));
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(existingUser));

        // Act
        loadDatabase.initDatabase(groceryRepository).run();

        // Assert
        verify(groceryRepository, times(3)).save(any(Grocery.class));
        verify(userRepository, never()).save(argThat(user -> 
            user.getUsername().equals("admin")));
        verify(userRepository, never()).save(argThat(user -> 
            user.getUsername().equals("user")));
    }

    @Test
    void initDatabase_ShouldCreateOnlyMissingUsers() throws Exception {
        // Arrange
        User existingAdmin = new User("admin", "adminpass", true);
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(existingAdmin));
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());

        // Act
        loadDatabase.initDatabase(groceryRepository).run();

        // Assert
        verify(groceryRepository, times(3)).save(any(Grocery.class));
        verify(userRepository, never()).save(argThat(user -> 
            user.getUsername().equals("admin")));
        verify(userRepository).save(argThat(user -> 
            user.getUsername().equals("user")));
    }

    @Test
    void openBrowser_ShouldNotThrowException() {
        // This test just verifies that the method doesn't throw an exception
        loadDatabase.openBrowser();
    }
} 