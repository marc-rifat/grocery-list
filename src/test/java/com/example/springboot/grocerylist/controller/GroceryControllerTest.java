package com.example.springboot.grocerylist.controller;

import com.example.springboot.grocerylist.entity.Grocery;
import com.example.springboot.grocerylist.service.GroceryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GroceryControllerTest {

    @Mock
    private GroceryService groceryService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private GroceryController groceryController;

    private Grocery testGrocery;
    private List<Grocery> groceryList;
    private Page<Grocery> groceryPage;

    @BeforeEach
    void setUp() {
        testGrocery = new Grocery();
        testGrocery.setId(1);
        testGrocery.setProductName("Test Product");
        testGrocery.setProductQuantity("2 units");

        groceryList = Arrays.asList(testGrocery);
        groceryPage = new PageImpl<>(groceryList);
    }

    @Test
    void listGroceryList_ShouldReturnPaginatedList() {
        when(groceryService.findAll(any(Pageable.class))).thenReturn(groceryPage);

        String viewName = groceryController.listGroceryList(model, 0);

        assertEquals("groceries/list-groceries", viewName);
        verify(model).addAttribute("groceries", groceryList);
        verify(model).addAttribute("currentPage", 0);
        verify(model).addAttribute("totalPages", groceryPage.getTotalPages());
        verify(model).addAttribute("totalItems", groceryPage.getTotalElements());
    }

    @Test
    void showFormForAdd_ShouldReturnFormView() {
        String viewName = groceryController.showFormForAdd(model);

        assertEquals("groceries/grocery-form", viewName);
        verify(model).addAttribute(eq("grocery"), any(Grocery.class));
    }

    @Test
    void showFormForUpdate_ShouldReturnFormWithExistingGrocery() {
        when(groceryService.findById(1)).thenReturn(testGrocery);

        String viewName = groceryController.showFormForUpdate(1, model);

        assertEquals("groceries/grocery-form", viewName);
        verify(model).addAttribute("grocery", testGrocery);
    }

    @Test
    void saveGrocery_WhenNewGrocery_ShouldSaveAndShowSuccessMessage() {
        Grocery newGrocery = new Grocery();
        newGrocery.setId(0); // New grocery has id 0
        newGrocery.setProductName("New Product");
        newGrocery.setProductQuantity("1 unit");

        String viewName = groceryController.saveGrocery(newGrocery, redirectAttributes);

        assertEquals("redirect:/groceries/list", viewName);
        verify(groceryService).save(newGrocery);
        verify(redirectAttributes).addFlashAttribute("success",
                String.format("Added new item: %s (%s)", newGrocery.getProductName(), newGrocery.getProductQuantity()));
    }

    @Test
    void saveGrocery_WhenUpdatingGrocery_ShouldUpdateAndShowSuccessMessage() {
        String viewName = groceryController.saveGrocery(testGrocery, redirectAttributes);

        assertEquals("redirect:/groceries/list", viewName);
        verify(groceryService).save(testGrocery);
        verify(redirectAttributes).addFlashAttribute("success",
                String.format("Updated item: %s (%s)", testGrocery.getProductName(), testGrocery.getProductQuantity()));
    }

    @Test
    void createRandomGrocery_ShouldCreateAndRedirect() {
        String viewName = groceryController.createRandomGrocery(redirectAttributes);

        assertEquals("redirect:/groceries/list", viewName);
        verify(groceryService).save(any(Grocery.class));
        verify(redirectAttributes).addFlashAttribute(eq("success"), anyString());
    }

    @Test
    void deleteGrocery_WhenGroceryExists_ShouldDeleteAndShowSuccess() {
        when(groceryService.findById(1)).thenReturn(testGrocery);

        String viewName = groceryController.deleteGrocery(1, redirectAttributes);

        assertEquals("redirect:/groceries/list", viewName);
        verify(groceryService).deleteById(1);
        verify(redirectAttributes).addFlashAttribute("success", "Item deleted successfully");
    }

    @Test
    void deleteGrocery_WhenGroceryNotFound_ShouldShowError() {
        when(groceryService.findById(1)).thenReturn(null);

        String viewName = groceryController.deleteGrocery(1, redirectAttributes);

        assertEquals("redirect:/groceries/list", viewName);
        verify(groceryService, never()).deleteById(anyInt());
        verify(redirectAttributes).addFlashAttribute("error", "Nothing to delete - Item not found");
    }

    @Test
    void deleteAllGroceries_WhenListNotEmpty_ShouldDeleteAllAndShowSuccess() {
        when(groceryService.findAll()).thenReturn(groceryList);

        String viewName = groceryController.deleteAllGroceries(redirectAttributes);

        assertEquals("redirect:/groceries/list", viewName);
        verify(groceryService).deleteAll();
        verify(redirectAttributes).addFlashAttribute("success", "All items deleted successfully");
    }

    @Test
    void deleteAllGroceries_WhenListEmpty_ShouldShowError() {
        when(groceryService.findAll()).thenReturn(Collections.emptyList());

        String viewName = groceryController.deleteAllGroceries(redirectAttributes);

        assertEquals("redirect:/groceries/list", viewName);
        verify(groceryService, never()).deleteAll();
        verify(redirectAttributes).addFlashAttribute("error", "Nothing to delete - List is already empty");
    }
}