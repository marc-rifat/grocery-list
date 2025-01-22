package com.example.springboot.grocerylist.service;

import com.example.springboot.grocerylist.dao.GroceryRepository;
import com.example.springboot.grocerylist.entity.Grocery;
import com.example.springboot.grocerylist.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroceryServiceImplTest {

    @Mock
    private GroceryRepository groceryRepository;

    @InjectMocks
    private GroceryServiceImpl groceryService;

    private User testUser;
    private Grocery grocery1;
    private Grocery grocery2;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "password", false);
        
        grocery1 = new Grocery("Milk", "1 gallon", "Whole milk");
        grocery1.setId(1);
        grocery1.setUser(testUser);

        grocery2 = new Grocery("Bread", "2 loaves", "Whole wheat");
        grocery2.setId(2);
        grocery2.setUser(testUser);
    }

    @Test
    void findAllByUser_ShouldReturnAllGroceriesForUser() {
        // Arrange
        List<Grocery> expectedGroceries = Arrays.asList(grocery1, grocery2);
        when(groceryRepository.findAllByUser(testUser)).thenReturn(expectedGroceries);

        // Act
        List<Grocery> actualGroceries = groceryService.findAllByUser(testUser);

        // Assert
        assertThat(actualGroceries).isEqualTo(expectedGroceries);
        verify(groceryRepository).findAllByUser(testUser);
    }

    @Test
    void findAllByUser_WithPagination_ShouldReturnPagedGroceriesForUser() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Grocery> groceries = Arrays.asList(grocery1, grocery2);
        Page<Grocery> expectedPage = new PageImpl<>(groceries, pageable, groceries.size());
        when(groceryRepository.findAllByUser(testUser, pageable)).thenReturn(expectedPage);

        // Act
        Page<Grocery> actualPage = groceryService.findAllByUser(testUser, pageable);

        // Assert
        assertThat(actualPage).isEqualTo(expectedPage);
        verify(groceryRepository).findAllByUser(testUser, pageable);
    }

    @Test
    void findByIdAndUser_ShouldReturnGroceryForUser() {
        // Arrange
        when(groceryRepository.findByIdAndUser(1, testUser)).thenReturn(grocery1);

        // Act
        Grocery foundGrocery = groceryService.findByIdAndUser(1, testUser);

        // Assert
        assertThat(foundGrocery).isEqualTo(grocery1);
        verify(groceryRepository).findByIdAndUser(1, testUser);
    }

    @Test
    void save_ShouldSaveGrocery() {
        // Arrange
        when(groceryRepository.save(any(Grocery.class))).thenReturn(grocery1);

        // Act
        groceryService.save(grocery1);

        // Assert
        verify(groceryRepository).save(grocery1);
    }

    @Test
    void deleteById_ShouldDeleteGrocery() {
        // Arrange
        doNothing().when(groceryRepository).deleteById(1);

        // Act
        groceryService.deleteById(1);

        // Assert
        verify(groceryRepository).deleteById(1);
    }

    @Test
    void deleteAllByUser_ShouldDeleteAllGroceriesForUser() {
        // Arrange
        doNothing().when(groceryRepository).deleteAllByUser(testUser);

        // Act
        groceryService.deleteAllByUser(testUser);

        // Assert
        verify(groceryRepository).deleteAllByUser(testUser);
    }

    @Test
    void findAll_ShouldReturnAllGroceries() {
        // Arrange
        List<Grocery> expectedGroceries = Arrays.asList(grocery1, grocery2);
        when(groceryRepository.findAll()).thenReturn(expectedGroceries);

        // Act
        List<Grocery> actualGroceries = groceryService.findAll();

        // Assert
        assertThat(actualGroceries).isEqualTo(expectedGroceries);
        verify(groceryRepository).findAll();
    }
} 