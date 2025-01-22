package com.example.springboot.grocerylist.util;

import com.example.springboot.grocerylist.dao.GroceryRepository;
import com.example.springboot.grocerylist.dao.UserRepository;
import com.example.springboot.grocerylist.entity.Grocery;
import com.example.springboot.grocerylist.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import java.awt.*;
import java.net.URI;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Autowired
    private UserRepository userRepository;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.default.username}")
    private String defaultUsername;

    @Value("${app.default.password}")
    private String defaultPassword;

    @Value("${app.browser.url}")
    private String browserUrl;

    @Bean
    CommandLineRunner initDatabase(GroceryRepository groceryRepository, UserRepository userRepository) {
        return args -> {
            // Create default admin user if it doesn't exist
            User adminUser = userRepository.findByUsername("admin")
                .orElseGet(() -> {
                    User newAdmin = new User("admin", "admin", true);
                    return userRepository.save(newAdmin);
                });

            // Create regular user if it doesn't exist
            User regularUser = userRepository.findByUsername("user")
                .orElseGet(() -> {
                    User newUser = new User("user", "user", false);
                    return userRepository.save(newUser);
                });

            // Only add sample groceries if the repository is empty
            if (groceryRepository.count() == 0) {
                // Admin's groceries
                Grocery milk = new Grocery("Milk", "1", "Whole milk");
                milk.setUser(adminUser);
                groceryRepository.save(milk);

                Grocery bread = new Grocery("Bread", "2", "Whole wheat");
                bread.setUser(adminUser);
                groceryRepository.save(bread);

                Grocery eggs = new Grocery("Eggs", "1", "Large, brown");
                eggs.setUser(adminUser);
                groceryRepository.save(eggs);

                // Regular user's groceries
                Grocery apples = new Grocery("Apples", "5", "Red delicious");
                apples.setUser(regularUser);
                groceryRepository.save(apples);

                Grocery bananas = new Grocery("Bananas", "1", "Bunch");
                bananas.setUser(regularUser);
                groceryRepository.save(bananas);

                log.info("Preloaded groceries for admin and regular user");
            }
        };
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> openBrowser() {
        return event -> {
            try {
                Thread.sleep(1000); // Small delay to ensure server is ready
                System.setProperty("java.awt.headless", "false");
                Desktop.getDesktop().browse(new URI(browserUrl));
                log.info("Opening browser to {}", browserUrl);
            } catch (Exception e) {
                log.error("Failed to open browser: {}", e.getMessage());
            }
        };
    }
}
