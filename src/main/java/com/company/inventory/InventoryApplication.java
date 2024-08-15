package com.company.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class InventoryApplication {

    private static final Logger logger = LoggerFactory.getLogger(InventoryApplication.class);

    /**
     * The main method serves as the entry point for the Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
        logger.info("Inventory Application has started successfully.");
    }
}
