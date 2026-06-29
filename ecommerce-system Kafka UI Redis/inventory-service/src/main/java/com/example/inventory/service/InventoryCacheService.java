package com.example.inventory.service;

import com.example.inventory.entity.Inventory;
import com.example.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryCacheService {

    private static final String CACHE_PREFIX = "inventory::";
    private static final Duration CACHE_TTL = Duration.ofMinutes(10);

    private final RedisTemplate<String, Inventory> redisTemplate;
    private final InventoryRepository repository;

    /**
     * Cache Aside Pattern
     *
     * 1. Check Redis
     * 2. If MISS -> PostgreSQL
     * 3. Save into Redis
     * 4. Return Inventory
     */
    public Inventory getInventory(String product) {

        String key = CACHE_PREFIX + product;

        log.info("========================================");
        log.info("Checking Redis Cache for Product : {}", product);

        Inventory inventory = redisTemplate.opsForValue().get(key);

        if (inventory != null) {

            log.info("CACHE HIT");
            log.info("Returning '{}' from Redis", product);
            log.info("========================================");

            return inventory;
        }

        log.info("CACHE MISS");
        log.info("Loading '{}' from PostgreSQL", product);

        inventory = repository.findById(product)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found : " + product));

        redisTemplate.opsForValue()
                .set(key, inventory, CACHE_TTL);

        log.info("Saved '{}' into Redis", product);
        log.info("========================================");

        return inventory;
    }

    /**
     * Update Redis after successful DB update.
     */
    public void updateInventory(Inventory inventory) {

        String key = CACHE_PREFIX + inventory.getProduct();

        redisTemplate.opsForValue()
                .set(key, inventory, CACHE_TTL);

        log.info("========================================");
        log.info("Redis Cache Updated");
        log.info("Product : {}", inventory.getProduct());
        log.info("Stock   : {}", inventory.getStock());
        log.info("========================================");
    }

    /**
     * Remove one product from Redis.
     */
    public void evictInventory(String product) {

        String key = CACHE_PREFIX + product;

        redisTemplate.delete(key);

        log.info("========================================");
        log.info("Redis Cache Evicted");
        log.info("Product : {}", product);
        log.info("========================================");
    }

    /**
     * Clear all inventory cache.
     */
    public void clearCache() {

        var keys = redisTemplate.keys(CACHE_PREFIX + "*");

        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }

        log.info("========================================");
        log.info("Inventory Redis Cache Cleared");
        log.info("========================================");
    }
}