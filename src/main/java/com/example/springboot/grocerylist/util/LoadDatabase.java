package com.example.springboot.grocerylist.util;

import com.example.springboot.grocerylist.dao.GroceryRepository;
import com.example.springboot.grocerylist.dao.UserRepository;
import com.example.springboot.grocerylist.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.net.URI;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Autowired
    private UserRepository userRepository;

    @Bean
    CommandLineRunner initDatabase(GroceryRepository groceryRepository) {
        return args -> {
            // Initialize sample groceries
            for (int i = 0; i < 3; i++) {
                groceryRepository.save(new Randomizer().makeRandomGrocery());
            }
            
            // Log all preloaded groceries
            groceryRepository.findAll()
                    .forEach(grocery -> log.info("Preloaded {}", grocery));

            // Initialize admin user
            initializeAdminUser();
        };
    }

    private void initializeAdminUser() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User adminUser = new User("admin", "admin", true);
            userRepository.save(adminUser);
            log.info("Created admin user");
        }
    }

    @Bean
    public void openBrowser() {
        Runnable openBrowser = () -> {
            try {
                System.setProperty("java.awt.headless", "false");
                Desktop.getDesktop().browse(new URI("http://localhost:9999/groceries/list"));
                log.info("Your default browser opened");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(openBrowser).start();
    }
}
