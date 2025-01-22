package com.example.springboot.grocerylist.controller;

import com.example.springboot.grocerylist.entity.Grocery;
import com.example.springboot.grocerylist.entity.User;
import com.example.springboot.grocerylist.service.GroceryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GroceryControllerTest {

    @Mock
    private GroceryService groceryService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private GroceryController groceryController;

    private User testUser;
    private Grocery testGrocery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        
        // Setup test grocery
        testGrocery = new Grocery();
        testGrocery.setId(1);
        testGrocery.setProductName("Test Product");
        testGrocery.setProductQuantity("1 unit");
        testGrocery.setUser(testUser);
    }

    @Test
    void listGroceryList_WhenNotAuthenticated_ShouldRedirectToLogin() {
        when(session.getAttribute("user")).thenReturn(null);
        
        String result = groceryController.listGroceryList(model, 0, session);
        
        assertEquals("redirect:/login", result);
        verifyNoInteractions(groceryService);
    }

    @Test
    void listGroceryList_WhenAuthenticated_ShouldReturnListView() {
        when(session.getAttribute("user")).thenReturn(testUser);
        
        List<Grocery> groceries = new ArrayList<>();
        groceries.add(testGrocery);
        Page<Grocery> groceryPage = new PageImpl<>(groceries);
        
        when(groceryService.findAllByUser(any(User.class), any(Pageable.class))).thenReturn(groceryPage);
        
        String result = groceryController.listGroceryList(model, 0, session);
        
        assertEquals("groceries/list-groceries", result);
        verify(model).addAttribute("groceries", groceries);
        verify(model).addAttribute("currentPage", 0);
        verify(model).addAttribute("totalPages", groceryPage.getTotalPages());
        verify(model).addAttribute("totalItems", groceryPage.getTotalElements());
    }

    @Test
    void saveGrocery_WhenNewGrocery_ShouldSaveAndRedirect() {
        when(session.getAttribute("user")).thenReturn(testUser);
        
        Grocery newGrocery = new Grocery();
        newGrocery.setId(0);
        newGrocery.setProductName("New Product");
        newGrocery.setProductQuantity("2 units");
        
        String result = groceryController.saveGrocery(newGrocery, redirectAttributes, session);
        
        assertEquals("redirect:/groceries/list", result);
        verify(groceryService).save(any(Grocery.class));
        verify(redirectAttributes).addFlashAttribute(
            eq("success"), 
            eq(String.format("Added new item: %s (%s)", newGrocery.getProductName(), newGrocery.getProductQuantity()))
        );
    }

    @Test
    void saveGrocery_WhenUpdatingGrocery_ShouldUpdateAndRedirect() {
        when(session.getAttribute("user")).thenReturn(testUser);
        when(groceryService.findByIdAndUser(anyInt(), any(User.class))).thenReturn(testGrocery);
        
        String result = groceryController.saveGrocery(testGrocery, redirectAttributes, session);
        
        assertEquals("redirect:/groceries/list", result);
        verify(groceryService).save(any(Grocery.class));
        verify(redirectAttributes).addFlashAttribute(
            eq("success"), 
            eq(String.format("Updated item: %s (%s)", testGrocery.getProductName(), testGrocery.getProductQuantity()))
        );
    }

    @Test
    void deleteGrocery_WhenGroceryExists_ShouldDeleteAndRedirect() {
        when(session.getAttribute("user")).thenReturn(testUser);
        when(groceryService.findByIdAndUser(anyInt(), any(User.class))).thenReturn(testGrocery);
        
        String result = groceryController.deleteGrocery(testGrocery.getId(), redirectAttributes, session);
        
        assertEquals("redirect:/groceries/list", result);
        verify(groceryService).deleteById(testGrocery.getId());
        verify(redirectAttributes).addFlashAttribute("success", "Item deleted successfully");
    }

    @Test
    void deleteGrocery_WhenGroceryNotFound_ShouldRedirectWithError() {
        when(session.getAttribute("user")).thenReturn(testUser);
        when(groceryService.findByIdAndUser(anyInt(), any(User.class))).thenReturn(null);
        
        String result = groceryController.deleteGrocery(testGrocery.getId(), redirectAttributes, session);
        
        assertEquals("redirect:/groceries/list", result);
        verify(groceryService, never()).deleteById(anyInt());
        verify(redirectAttributes).addFlashAttribute("error", "Nothing to delete - Item not found");
    }

    @Test
    void deleteAllGroceries_WhenGroceriesExist_ShouldDeleteAllAndRedirect() {
        when(session.getAttribute("user")).thenReturn(testUser);
        List<Grocery> groceries = List.of(testGrocery);
        when(groceryService.findAllByUser(any(User.class))).thenReturn(groceries);
        
        String result = groceryController.deleteAllGroceries(redirectAttributes, session);
        
        assertEquals("redirect:/groceries/list", result);
        verify(groceryService).deleteAllByUser(any(User.class));
        verify(redirectAttributes).addFlashAttribute("success", "All items deleted successfully");
    }

    @Test
    void deleteAllGroceries_WhenNoGroceries_ShouldRedirectWithError() {
        when(session.getAttribute("user")).thenReturn(testUser);
        when(groceryService.findAllByUser(any(User.class))).thenReturn(new ArrayList<>());
        
        String result = groceryController.deleteAllGroceries(redirectAttributes, session);
        
        assertEquals("redirect:/groceries/list", result);
        verify(groceryService, never()).deleteAllByUser(any(User.class));
        verify(redirectAttributes).addFlashAttribute("error", "Nothing to delete - List is already empty");
    }

    @Test
    void showFormForAdd_WhenAuthenticated_ShouldShowForm() {
        when(session.getAttribute("user")).thenReturn(testUser);
        
        String result = groceryController.showFormForAdd(model, session);
        
        assertEquals("groceries/grocery-form", result);
        verify(model).addAttribute(eq("grocery"), any(Grocery.class));
    }

    @Test
    void showFormForAdd_WhenNotAuthenticated_ShouldRedirectToLogin() {
        when(session.getAttribute("user")).thenReturn(null);
        
        String result = groceryController.showFormForAdd(model, session);
        
        assertEquals("redirect:/login", result);
        verifyNoInteractions(model);
    }

    @Test
    void showFormForUpdate_WhenAuthenticatedAndGroceryExists_ShouldShowForm() {
        when(session.getAttribute("user")).thenReturn(testUser);
        when(groceryService.findByIdAndUser(anyInt(), any(User.class))).thenReturn(testGrocery);
        
        String result = groceryController.showFormForUpdate(testGrocery.getId(), model, session);
        
        assertEquals("groceries/grocery-form", result);
        verify(model).addAttribute("grocery", testGrocery);
    }

    @Test
    void showFormForUpdate_WhenGroceryNotFound_ShouldRedirectToList() {
        when(session.getAttribute("user")).thenReturn(testUser);
        when(groceryService.findByIdAndUser(anyInt(), any(User.class))).thenReturn(null);
        
        String result = groceryController.showFormForUpdate(testGrocery.getId(), model, session);
        
        assertEquals("redirect:/groceries/list", result);
        verifyNoInteractions(model);
    }
} 