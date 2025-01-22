package com.example.springboot.grocerylist.util;

import com.example.springboot.grocerylist.entity.Grocery;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomizerTest {

    @Test
    void makeRandomGrocery_ShouldCreateGroceryWithAllFieldsPopulated() {
        // Arrange
        Randomizer randomizer = new Randomizer();

        // Act
        Grocery grocery = randomizer.makeRandomGrocery();

        // Assert
        assertNotNull(grocery, "Grocery should not be null");
        assertNotNull(grocery.getProductName(), "Product name should not be null");
        assertNotNull(grocery.getProductQuantity(), "Product quantity should not be null");
        assertNotNull(grocery.getNote(), "Note should not be null");
        assertFalse(grocery.getProductName().isEmpty(), "Product name should not be empty");
        assertFalse(grocery.getProductQuantity().isEmpty(), "Product quantity should not be empty");
        assertFalse(grocery.getNote().isEmpty(), "Note should not be empty");
    }

    @Test
    void makeRandomGrocery_ShouldCreateDifferentGroceriesOnMultipleCalls() {
        // Arrange
        Randomizer randomizer = new Randomizer();

        // Act
        Grocery grocery1 = randomizer.makeRandomGrocery();
        Grocery grocery2 = randomizer.makeRandomGrocery();

        // Assert
        assertNotNull(grocery1);
        assertNotNull(grocery2);

    }

    @Test
    void makeRandomGrocery_QuantityShouldBeNumericBetweenOneAndTen() {
        // Arrange
        Randomizer randomizer = new Randomizer();

        // Act
        Grocery grocery = randomizer.makeRandomGrocery();

        // Assert
        int quantity = Integer.parseInt(grocery.getProductQuantity());
        assertTrue(quantity >= 1 && quantity <= 10,
                "Quantity should be between 1 and 10");
    }
}