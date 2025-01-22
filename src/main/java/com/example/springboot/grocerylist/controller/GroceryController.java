package com.example.springboot.grocerylist.controller;

import com.example.springboot.grocerylist.entity.Grocery;
import com.example.springboot.grocerylist.service.GroceryService;
import com.example.springboot.grocerylist.util.Randomizer;
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

    @GetMapping("/list")
    public String listGroceryList(Model theModel,
                                @RequestParam(defaultValue = "0") int page) {
        // Create Pageable instance with 10 items per page
        Pageable pageable = PageRequest.of(page, 10);
        
        // Get paginated groceries from database
        Page<Grocery> groceryPage = groceryService.findAll(pageable);
        
        // Add pagination attributes to the model
        theModel.addAttribute("groceries", groceryPage.getContent());
        theModel.addAttribute("currentPage", page);
        theModel.addAttribute("totalPages", groceryPage.getTotalPages());
        theModel.addAttribute("totalItems", groceryPage.getTotalElements());
        
        return "groceries/list-groceries";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {
        Grocery grocery = new Grocery();
        theModel.addAttribute("grocery", grocery);
        return "groceries/grocery-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("groceryId") int theId, Model theModel) {
        //get the grocery from the service
        Grocery grocery = groceryService.findById(theId);
        theModel.addAttribute("grocery", grocery);
        //set grocery in the model to prepopulate the form
        return "groceries/grocery-form";
    }

    @PostMapping("/save")
    public String saveGrocery(@ModelAttribute("grocery") Grocery theGrocery, RedirectAttributes redirectAttributes) {
        boolean isUpdate = theGrocery.getId() != 0; // Check if this is an update operation
        
        // Save the grocery
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
    public String createRandomGrocery(RedirectAttributes redirectAttributes) {
        Grocery grocery = new Randomizer().makeRandomGrocery();
        groceryService.save(grocery);
        redirectAttributes.addFlashAttribute("success", 
            String.format("Added random item: %s (%s)", grocery.getProductName(), grocery.getProductQuantity()));
        return "redirect:/groceries/list";
    }

    @DeleteMapping("/delete")
    public String deleteGrocery(@RequestParam("groceryId") int theId, 
                              RedirectAttributes redirectAttributes) {
        Grocery grocery = groceryService.findById(theId);
        if (grocery == null) {
            redirectAttributes.addFlashAttribute("error", "Nothing to delete - Item not found");
        } else {
            groceryService.deleteById(theId);
            redirectAttributes.addFlashAttribute("success", "Item deleted successfully");
        }
        return "redirect:/groceries/list";
    }

    @DeleteMapping("/deleteAll")
    public String deleteAllGroceries(RedirectAttributes redirectAttributes) {
        List<Grocery> groceries = groceryService.findAll();
        if (groceries.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Nothing to delete - List is already empty");
        } else {
            groceryService.deleteAll();
            redirectAttributes.addFlashAttribute("success", "All items deleted successfully");
        }
        return "redirect:/groceries/list";
    }
}
