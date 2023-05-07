package com.example.springboot.grocerylist.util;

import com.example.springboot.grocerylist.dao.GroceryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.net.URI;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(GroceryRepository groceryRepository) {

        return args -> {
            groceryRepository.save(new Randomizer().makeRandomGrocery());
            groceryRepository.save(new Randomizer().makeRandomGrocery());
            groceryRepository.save(new Randomizer().makeRandomGrocery());
            groceryRepository.findAll().forEach(grocery -> log.info("Preloaded " + grocery));
        };
    }

    @Bean
    public void openBrowser() {
        Runnable openBrowser = () -> {
            try {
                System.setProperty("java.awt.headless", "false");
                Desktop.getDesktop().browse(new URI("http://localhost:8080/groceries/list"));
                log.info("Your default browser opened");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(openBrowser).start();
    }
}
