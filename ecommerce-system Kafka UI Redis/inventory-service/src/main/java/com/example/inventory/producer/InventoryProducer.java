package com.example.inventory.producer;

import com.example.common.dto.InventoryFailedEvent;
import com.example.common.dto.InventoryReservedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryProducer {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public void publishReserved(
            InventoryReservedEvent event) throws InterruptedException {
        Thread.sleep(1000);
        kafkaTemplate.send(
                "inventory-reserved",
                event.getOrderId(),
                event
        );
    }

    public void publishFailed(
            InventoryFailedEvent event) throws InterruptedException {
        Thread.sleep(1000);
        kafkaTemplate.send(
                "inventory-failed",
                event.getOrderId(),
                event
        );
    }
}