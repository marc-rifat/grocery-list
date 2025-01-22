package com.example.springboot.grocerylist.controller;

import com.example.springboot.grocerylist.entity.Grocery;
import com.example.springboot.grocerylist.entity.User;
import com.example.springboot.grocerylist.service.GroceryService;
import com.example.springboot.grocerylist.util.Randomizer;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/groceries")
public class GroceryController {
    @Autowired
    private GroceryService groceryService;

    private User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    private boolean isAuthenticated(HttpSession session) {
        return getCurrentUser(session) != null;
    }

    @GetMapping("/list")
    public String listGroceryList(Model theModel,
                                @RequestParam(defaultValue = "0") int page,
                                HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        User currentUser = getCurrentUser(session);
        Pageable pageable = PageRequest.of(page, 10);
        
        // Get paginated groceries for current user
        Page<Grocery> groceryPage = groceryService.findAllByUser(currentUser, pageable);
        
        // Add pagination attributes to the model
        theModel.addAttribute("groceries", groceryPage.getContent());
        theModel.addAttribute("currentPage", page);
        theModel.addAttribute("totalPages", groceryPage.getTotalPages());
        theModel.addAttribute("totalItems", groceryPage.getTotalElements());
        
        return "groceries/list-groceries";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        Grocery grocery = new Grocery();
        grocery.setUser(getCurrentUser(session));
        theModel.addAttribute("grocery", grocery);
        return "groceries/grocery-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("groceryId") int theId, 
                                  Model theModel,
                                  HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        User currentUser = getCurrentUser(session);
        Grocery grocery = groceryService.findByIdAndUser(theId, currentUser);
        
        if (grocery == null) {
            return "redirect:/groceries/list";
        }
        
        theModel.addAttribute("grocery", grocery);
        return "groceries/grocery-form";
    }

    @PostMapping("/save")
    public String saveGrocery(@ModelAttribute("grocery") Grocery theGrocery, 
                            RedirectAttributes redirectAttributes,
                            HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        User currentUser = getCurrentUser(session);
        theGrocery.setUser(currentUser);
        
        boolean isUpdate = theGrocery.getId() != 0;
        
        // For updates, verify the grocery belongs to current user
        if (isUpdate) {
            Grocery existingGrocery = groceryService.findByIdAndUser(theGrocery.getId(), currentUser);
            if (existingGrocery == null) {
                return "redirect:/groceries/list";
            }
        }
        
        groceryService.save(theGrocery);
        
        // Add appropriate flash message
        if (isUpdate) {
            redirectAttributes.addFlashAttribute("success", 
                String.format("Updated item: %s (%s)", theGrocery.getProductName(), theGrocery.getProductQuantity()));
        } else {
            redirectAttributes.addFlashAttribute("success", 
                String.format("Added new item: %s (%s)", theGrocery.getProductName(), theGrocery.getProductQuantity()));
        }
        
        return "redirect:/groceries/list";
    }

    @GetMapping("/createRandomGrocery")
    public String createRandomGrocery(RedirectAttributes redirectAttributes,
                                    HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        Grocery grocery = new Randomizer().makeRandomGrocery();
        grocery.setUser(getCurrentUser(session));
        groceryService.save(grocery);
        redirectAttributes.addFlashAttribute("success", 
            String.format("Added random item: %s (%s)", grocery.getProductName(), grocery.getProductQuantity()));
        return "redirect:/groceries/list";
    }

    @DeleteMapping("/delete")
    public String deleteGrocery(@RequestParam("groceryId") int theId, 
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        User currentUser = getCurrentUser(session);
        Grocery grocery = groceryService.findByIdAndUser(theId, currentUser);
        
        if (grocery == null) {
            redirectAttributes.addFlashAttribute("error", "Nothing to delete - Item not found");
        } else {
            groceryService.deleteById(theId);
            redirectAttributes.addFlashAttribute("success", "Item deleted successfully");
        }
        return "redirect:/groceries/list";
    }

    @DeleteMapping("/deleteAll")
    public String deleteAllGroceries(RedirectAttributes redirectAttributes,
                                   HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/login";
        }
        
        User currentUser = getCurrentUser(session);
        List<Grocery> groceries = groceryService.findAllByUser(currentUser);
        
        if (groceries.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Nothing to delete - List is already empty");
        } else {
            groceryService.deleteAllByUser(currentUser);
            redirectAttributes.addFlashAttribute("success", "All items deleted successfully");
        }
        return "redirect:/groceries/list";
    }
}
