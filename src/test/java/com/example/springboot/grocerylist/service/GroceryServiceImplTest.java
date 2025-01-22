package com.example.springboot.grocerylist.service;

import com.example.springboot.grocerylist.dao.GroceryRepository;
import com.example.springboot.grocerylist.entity.Grocery;
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
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GroceryServiceImplTest {

    @Mock
    private GroceryRepository groceryRepository;

    @InjectMocks
    private GroceryServiceImpl groceryService;

    private Grocery testGrocery;
    private List<Grocery> groceryList;

    @BeforeEach
    void setUp() {
        testGrocery = new Grocery();
        testGrocery.setId(1);
        testGrocery.setProductName("Test Product");
        testGrocery.setProductQuantity("2");
        testGrocery.setNote("Test Note");

        groceryList = Arrays.asList(testGrocery);
    }

    @Test
    void findAll_ShouldReturnAllGroceries() {
        when(groceryRepository.findAll()).thenReturn(groceryList);

        List<Grocery> result = groceryService.findAll();

        assertEquals(groceryList, result);
        verify(groceryRepository).findAll();
    }

    @Test
    void findAll_WithPageable_ShouldReturnPagedGroceries() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Grocery> groceryPage = new PageImpl<>(groceryList);
        when(groceryRepository.findAll(pageable)).thenReturn(groceryPage);

        Page<Grocery> result = groceryService.findAll(pageable);

        assertEquals(groceryPage, result);
        verify(groceryRepository).findAll(pageable);
    }

    @Test
    void findById_WhenGroceryExists_ShouldReturnGrocery() {
        when(groceryRepository.findById(1)).thenReturn(Optional.of(testGrocery));

        Grocery result = groceryService.findById(1);

        assertEquals(testGrocery, result);
        verify(groceryRepository).findById(1);
    }

    @Test
    void findById_WhenGroceryDoesNotExist_ShouldThrowException() {
        when(groceryRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> groceryService.findById(1));
        assertEquals("Did not find grocery id - 1", exception.getMessage());
        verify(groceryRepository).findById(1);
    }

    @Test
    void save_ShouldSaveGrocery() {
        groceryService.save(testGrocery);

        verify(groceryRepository).save(testGrocery);
    }

    @Test
    void deleteById_ShouldDeleteGrocery() {
        groceryService.deleteById(1);

        verify(groceryRepository).deleteById(1);
    }

    @Test
    void deleteAll_ShouldDeleteAllGroceries() {
        groceryService.deleteAll();

        verify(groceryRepository).deleteAll();
    }
} 