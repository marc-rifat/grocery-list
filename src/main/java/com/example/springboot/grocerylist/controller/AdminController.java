package com.example.springboot.grocerylist.controller;

import com.example.springboot.grocerylist.entity.User;
import com.example.springboot.grocerylist.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listUsers(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !currentUser.isAdmin()) {
            return "redirect:/groceries/list";
        }
        
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @DeleteMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, 
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !currentUser.isAdmin()) {
            return "redirect:/groceries/list";
        }

        try {
            User userToDelete = userService.findById(id);
            if (userToDelete.isAdmin()) {
                redirectAttributes.addFlashAttribute("error", "Cannot delete admin user");
            } else {
                userService.deleteById(id);
                redirectAttributes.addFlashAttribute("success", 
                    String.format("User '%s' deleted successfully", userToDelete.getUsername()));
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting user: " + e.getMessage());
        }
        
        return "redirect:/admin/users";
    }
} 