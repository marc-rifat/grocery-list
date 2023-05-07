package com.example.springboot.grocerylist.controller;

import com.example.springboot.grocerylist.entity.Grocery;
import com.example.springboot.grocerylist.service.GroceryService;
import com.example.springboot.grocerylist.util.Randomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/groceries")
public class GroceryController {
    @Autowired
    private GroceryService groceryService;

    @GetMapping("/list")
    public String listGroceryList(Model theModel) {
        //get all groceries from database
        List<Grocery> groceryList = groceryService.findAll();
        // add to the spring model
        theModel.addAttribute("groceries", groceryList);
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
    public String saveGrocery(@ModelAttribute("grocery") Grocery theGrocery) {
        //save the grocery
        groceryService.save(theGrocery);
        //use a redirect to prevent duplicate submissions
        return "redirect:/groceries/list";
    }

    @GetMapping("/createRandomGrocery")
    public String createRandomGrocery() {
        groceryService.save(new Randomizer().makeRandomGrocery());
        //use a redirect to prevent duplicate submissions
        return "redirect:/groceries/list";
    }

    @GetMapping("/delete")
    public String deleteGrocery(@RequestParam("groceryId") int theId) {
        //delete grocery
        groceryService.deleteById(theId);
        return "redirect:/groceries/list";
    }

    @GetMapping("/deleteAll")
    public String deleteAllGroceries() {
        //delete all groceries
        groceryService.deleteAll();
        return "redirect:/groceries/list";
    }
}
