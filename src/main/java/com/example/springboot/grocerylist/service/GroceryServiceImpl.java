package com.example.springboot.grocerylist.service;

import com.example.springboot.grocerylist.dao.GroceryRepository;
import com.example.springboot.grocerylist.entity.Grocery;
import com.example.springboot.grocerylist.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional  // Add class-level transaction management
public class GroceryServiceImpl implements GroceryService {

    private final GroceryRepository groceryRepository;

    @Autowired
    public GroceryServiceImpl(GroceryRepository groceryRepository) {
        this.groceryRepository = groceryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Grocery> findAll() {
        return groceryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Grocery> findAllByUser(User user) {
        return groceryRepository.findAllByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Grocery> findAllByUser(User user, Pageable pageable) {
        return groceryRepository.findAllByUser(user, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Grocery findByIdAndUser(int theId, User user) {
        return groceryRepository.findByIdAndUser(theId, user);
    }

    @Override
    @Transactional
    public void save(Grocery grocery) {
        groceryRepository.save(grocery);
    }

    @Override
    @Transactional
    public void deleteById(int theId) {
        groceryRepository.deleteById(theId);
    }

    @Override
    @Transactional
    public void deleteAllByUser(User user) {
        groceryRepository.deleteAllByUser(user);
    }
}
