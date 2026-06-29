package com.example.inventory.config;

import com.example.inventory.entity.Inventory;
import com.example.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryDataLoader implements CommandLineRunner {

    private final InventoryRepository repository;

    @Override
    public void run(String... args) {

        createInventory("Laptop", 100);
        createInventory("Mobile", 150);
        createInventory("Headphones", 200);
        createInventory("Keyboard", 75);
        createInventory("Mouse", 120);

        System.out.println("Inventory initialized successfully.");
    }

    private void createInventory(String product, Integer stock) {

        if (!repository.existsById(product)) {

            repository.save(
                    Inventory.builder()
                            .product(product)
                            .stock(stock)
                            .build()
            );

            System.out.println("Inserted : " + product);
        }
    }
}