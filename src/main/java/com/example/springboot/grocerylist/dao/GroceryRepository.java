package com.example.springboot.grocerylist.dao;

import com.example.springboot.grocerylist.entity.Grocery;
import com.example.springboot.grocerylist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroceryRepository extends JpaRepository<Grocery, Integer> {
    Page<Grocery> findAllByUser(User user, Pageable pageable);
    List<Grocery> findAllByUser(User user);
    Grocery findByIdAndUser(int id, User user);
    void deleteAllByUser(User user);
}
