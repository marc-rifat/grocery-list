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

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @InjectMocks
    private LoginController loginController;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
    }

    @Test
    void showLoginForm_ShouldReturnLoginView() {
        assertEquals("login", loginController.showLoginForm());
    }

    @Test
    void login_WhenCredentialsValid_ShouldRedirectToGroceryList() {
        when(userService.authenticate("testuser", "password123")).thenReturn(testUser);

        String viewName = loginController.login("testuser", "password123", session, model);

        assertEquals("redirect:/groceries/list", viewName);
        verify(session).setAttribute("user", testUser);
        verify(model, never()).addAttribute(eq("error"), anyString());
    }

    @Test
    void login_WhenCredentialsInvalid_ShouldShowError() {
        when(userService.authenticate("testuser", "wrongpass")).thenReturn(null);

        String viewName = loginController.login("testuser", "wrongpass", session, model);

        assertEquals("login", viewName);
        verify(session, never()).setAttribute(anyString(), any());
        verify(model).addAttribute("error", "Invalid username or password");
    }

    @Test
    void showRegistrationForm_ShouldReturnRegisterView() {
        assertEquals("register", loginController.showRegistrationForm());
    }

    @Test
    void register_WhenPasswordsDontMatch_ShouldShowError() throws Exception {
        String viewName = loginController.register("newuser", "pass123", "pass456", model);

        assertEquals("register", viewName);
        verify(model).addAttribute("error", "Passwords do not match");
        verify(userService, never()).register(anyString(), anyString());
    }

    @Test
    void register_WhenSuccessful_ShouldRedirectToLogin() throws Exception {
        when(userService.register("newuser", "pass123")).thenReturn(testUser);

        String viewName = loginController.register("newuser", "pass123", "pass123", model);

        assertEquals("redirect:/login?registered=true", viewName);
        verify(userService).register("newuser", "pass123");
        verify(model, never()).addAttribute(eq("error"), anyString());
    }

    @Test
    void register_WhenServiceThrowsException_ShouldShowError() throws Exception {
        String errorMessage = "Username already exists";
        when(userService.register("newuser", "pass123")).thenThrow(new RuntimeException(errorMessage));

        String viewName = loginController.register("newuser", "pass123", "pass123", model);

        assertEquals("register", viewName);
        verify(model).addAttribute("error", errorMessage);
    }

    @Test
    void logout_ShouldInvalidateSessionAndRedirectToLogin() {
        String viewName = loginController.logout(session);

        assertEquals("redirect:/login", viewName);
        verify(session).invalidate();
    }
} 