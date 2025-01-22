package com.example.springboot.grocerylist.controller;

import com.example.springboot.grocerylist.entity.User;
import com.example.springboot.grocerylist.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private AdminController adminController;

    private User adminUser;
    private User regularUser;

    @BeforeEach
    void setUp() {
        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setUsername("admin");
        adminUser.setAdmin(true);

        regularUser = new User();
        regularUser.setId(2L);
        regularUser.setUsername("user");
        regularUser.setAdmin(false);
    }

    @Test
    void listUsers_WhenUserNotLoggedIn_RedirectsToGroceryList() {
        when(session.getAttribute("user")).thenReturn(null);

        String viewName = adminController.listUsers(model, session);

        assertEquals("redirect:/groceries/list", viewName);
        verify(model, never()).addAttribute(anyString(), any());
    }

    @Test
    void listUsers_WhenNonAdminUser_RedirectsToGroceryList() {
        when(session.getAttribute("user")).thenReturn(regularUser);

        String viewName = adminController.listUsers(model, session);

        assertEquals("redirect:/groceries/list", viewName);
        verify(model, never()).addAttribute(anyString(), any());
    }

    @Test
    void listUsers_WhenAdminUser_ShowsUsersList() {
        when(session.getAttribute("user")).thenReturn(adminUser);
        List<User> usersList = Arrays.asList(adminUser, regularUser);
        when(userService.findAll()).thenReturn(usersList);

        String viewName = adminController.listUsers(model, session);

        assertEquals("admin/users", viewName);
        verify(model).addAttribute("users", usersList);
    }

    @Test
    void deleteUser_WhenUserNotLoggedIn_RedirectsToGroceryList() {
        when(session.getAttribute("user")).thenReturn(null);

        String viewName = adminController.deleteUser(2L, session, redirectAttributes);

        assertEquals("redirect:/groceries/list", viewName);
        verify(userService, never()).deleteById(anyLong());
    }

    @Test
    void deleteUser_WhenNonAdminUser_RedirectsToGroceryList() {
        when(session.getAttribute("user")).thenReturn(regularUser);

        String viewName = adminController.deleteUser(2L, session, redirectAttributes);

        assertEquals("redirect:/groceries/list", viewName);
        verify(userService, never()).deleteById(anyLong());
    }

    @Test
    void deleteUser_WhenDeletingAdminUser_ShowsError() {
        when(session.getAttribute("user")).thenReturn(adminUser);
        when(userService.findById(1L)).thenReturn(adminUser);

        String viewName = adminController.deleteUser(1L, session, redirectAttributes);

        assertEquals("redirect:/admin/users", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "Cannot delete admin user");
        verify(userService, never()).deleteById(anyLong());
    }

    @Test
    void deleteUser_WhenDeletingRegularUser_DeletesSuccessfully() {
        when(session.getAttribute("user")).thenReturn(adminUser);
        when(userService.findById(2L)).thenReturn(regularUser);

        String viewName = adminController.deleteUser(2L, session, redirectAttributes);

        assertEquals("redirect:/admin/users", viewName);
        verify(redirectAttributes).addFlashAttribute("success", "User 'user' deleted successfully");
        verify(userService).deleteById(2L);
    }

    @Test
    void deleteUser_WhenServiceThrowsException_ShowsError() {
        when(session.getAttribute("user")).thenReturn(adminUser);
        when(userService.findById(2L)).thenThrow(new RuntimeException("Database error"));

        String viewName = adminController.deleteUser(2L, session, redirectAttributes);

        assertEquals("redirect:/admin/users", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "Error deleting user: Database error");
        verify(userService, never()).deleteById(anyLong());
    }
} 