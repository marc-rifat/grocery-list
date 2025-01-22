package com.example.springboot.grocerylist.service;

import com.example.springboot.grocerylist.entity.Grocery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroceryService {

    List<Grocery> findAll();

    Grocery findById(int theId);

    void save(Grocery grocery);

    void deleteById(int theId);

    void deleteAll();

    Page<Grocery> findAll(Pageable pageable);
}
