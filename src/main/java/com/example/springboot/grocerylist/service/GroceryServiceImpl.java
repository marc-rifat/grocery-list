package com.example.springboot.grocerylist.service;

import com.example.springboot.grocerylist.dao.GroceryRepository;
import com.example.springboot.grocerylist.entity.Grocery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroceryServiceImpl implements GroceryService {

    private final GroceryRepository groceryRepository;

    @Autowired
    public GroceryServiceImpl(GroceryRepository groceryRepository) {
        this.groceryRepository = groceryRepository;
    }

    @Override
    public List<Grocery> findAll() {
        return groceryRepository.findAllByOrderByIdDesc();
    }

    @Override
    public Grocery findById(int theId) {
        Optional<Grocery> result = groceryRepository.findById(theId);
        Grocery grocery = null;
        if (result.isPresent()) {
            grocery = result.get();
        } else {
            throw new RuntimeException("Did not find grocery id - " + theId);
        }
        return grocery;
    }

    @Override
    public void save(Grocery grocery) {
        groceryRepository.save(grocery);
    }

    @Override
    public void deleteById(int theId) {
        groceryRepository.deleteById(theId);
    }

    @Override
    public void deleteAll() {
        groceryRepository.deleteAll();
    }
}
