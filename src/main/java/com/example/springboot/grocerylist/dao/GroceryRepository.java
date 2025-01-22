package com.example.springboot.grocerylist.dao;

import com.example.springboot.grocerylist.entity.Grocery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroceryRepository extends JpaRepository<Grocery, Integer> {
    List<Grocery> findAllByOrderByIdDesc();
}
