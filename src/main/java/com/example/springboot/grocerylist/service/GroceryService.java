package com.example.springboot.grocerylist.service;

import com.example.springboot.grocerylist.entity.Grocery;

import java.util.List;

public interface GroceryService {

    List<Grocery> findAll();

    Grocery findById(int theId);

    void save(Grocery grocery);

    void deleteById(int theId);

    void deleteAll();
}
