package com.example.inventory.producer;

import com.example.common.dto.InventoryFailedEvent;
import com.example.common.dto.InventoryReleasedEvent;
import com.example.common.dto.InventoryReservedEvent;
import com.example.inventory.consumer.InventoryConsumer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryProducer {

    private static final Logger log = LoggerFactory.getLogger(InventoryProducer.class);

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

    public void publishReleased(
            InventoryReleasedEvent event) {

        kafkaTemplate.send(
                "inventory-released",
                event
        );

        log.info(
                "InventoryReleasedEvent published for Order {}",
                event.getOrderId()
        );
    }


}