package com.example.springboot.grocerylist.dao;

import com.example.springboot.grocerylist.entity.Grocery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroceryRepository extends JpaRepository<Grocery, Integer> {
    List<Grocery> findAllByOrderByIdDesc();
}
