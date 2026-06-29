package com.example.inventory.service;

import com.example.common.dto.ReserveInventoryCommand;
import com.example.inventory.entity.Inventory;
import com.example.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository repository;
    private final InventoryCacheService cacheService;

    /**
     * Get Inventory
     *
     * Flow:
     * Redis
     *   ↓
     * Cache Hit -> Return
     *
     * Cache Miss
     *   ↓
     * PostgreSQL
     *   ↓
     * Save into Redis
     *   ↓
     * Return
     */
    public Inventory getInventory(String product) {

        return cacheService.getInventory(product);
    }

    /**
     * Reduce Stock
     *
     * Flow:
     * Redis/DB
     *   ↓
     * Validate Stock
     *   ↓
     * Update PostgreSQL
     *   ↓
     * Update Redis
     */
    public Inventory reduceStock(ReserveInventoryCommand command) {

        log.info("========================================");
        log.info("ReserveInventoryCommand received");
        log.info("Saga Id  : {}", command.getSagaId());
        log.info("Order Id : {}", command.getOrderId());
        log.info("Product  : {}", command.getProduct());
        log.info("Quantity : {}", command.getQuantity());
        log.info("========================================");

        // Fetch inventory using Cache Aside Pattern
        Inventory inventory =
                cacheService.getInventory(command.getProduct());

        log.info("Current Stock : {}", inventory.getStock());

        if (inventory.getStock() < command.getQuantity()) {

            throw new RuntimeException(
                    "Insufficient stock for product : "
                            + command.getProduct());
        }

        // Reduce Stock
        inventory.setStock(
                inventory.getStock() - command.getQuantity());

        // Save latest stock into PostgreSQL
        Inventory updatedInventory =
                repository.save(inventory);

        log.info("========================================");
        log.info("Inventory Updated in PostgreSQL");
        log.info("Product : {}", updatedInventory.getProduct());
        log.info("Stock   : {}", updatedInventory.getStock());
        log.info("========================================");

        // Update Redis
        cacheService.updateInventory(updatedInventory);

        log.info("Inventory Updated Successfully");

        return updatedInventory;
    }

    /**
     * Save Inventory
     */
    public Inventory saveInventory(Inventory inventory) {

        Inventory savedInventory =
                repository.save(inventory);

        cacheService.updateInventory(savedInventory);

        log.info("Inventory Saved Successfully");

        return savedInventory;
    }

    /**
     * Remove Inventory from Redis
     */
    public void evictInventory(String product) {

        cacheService.evictInventory(product);
    }

    /**
     * Clear Inventory Cache
     */
    public void clearInventoryCache() {

        cacheService.clearCache();
    }
}