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
            initializeUser();
        };
    }

    private void initializeAdminUser() {
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            User adminUser = new User(adminUsername, adminPassword, true);
            userRepository.save(adminUser);
            log.info("Created admin user");
        }
    }

    private void initializeUser() {
        if (userRepository.findByUsername(defaultUsername).isEmpty()) {
            User user = new User(defaultUsername, defaultPassword, false);
            userRepository.save(user);
            log.info("Created user");
        }
    }

    @Bean
    public void openBrowser() {
        Runnable openBrowser = () -> {
            try {
                System.setProperty("java.awt.headless", "false");
                Desktop.getDesktop().browse(new URI(browserUrl));
                log.info("Your default browser opened");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(openBrowser).start();
    }
}
