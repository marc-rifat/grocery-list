package com.example.springboot.grocerylist.service;

import com.example.springboot.grocerylist.entity.Grocery;
import com.example.springboot.grocerylist.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroceryService {

    List<Grocery> findAll();

    List<Grocery> findAllByUser(User user);

    Page<Grocery> findAllByUser(User user, Pageable pageable);

    Grocery findByIdAndUser(int theId, User user);

    void save(Grocery grocery);

    void deleteById(int theId);

    void deleteAllByUser(User user);
}
