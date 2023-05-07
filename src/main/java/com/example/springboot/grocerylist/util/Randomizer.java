package com.example.springboot.grocerylist.util;

import com.example.springboot.grocerylist.entity.Grocery;
import com.github.javafaker.Faker;


public class Randomizer {
    private final Grocery grocery = new Grocery();
    private final Faker fakeData = new Faker();

    public Grocery makeRandomGrocery() {
        grocery.setProductName(fakeData.food().ingredient());
        grocery.setProductQuantity(String.valueOf(fakeData.number().numberBetween(1, 10)));
        grocery.setNote(fakeData.food().dish());
        return grocery;
    }
}
