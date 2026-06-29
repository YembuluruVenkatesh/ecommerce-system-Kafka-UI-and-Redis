package com.example.inventory.consumer;

import com.example.common.dto.*;
import com.example.inventory.entity.ProcessedEvent;
import com.example.inventory.producer.InventoryProducer;
import com.example.inventory.repository.ProcessedEventRepository;
import com.example.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InventoryConsumer {

    private final InventoryService inventoryService;
    private final ProcessedEventRepository repository;
    private final InventoryProducer producer;
    private static final Logger log = LoggerFactory.getLogger(InventoryConsumer.class);
    @KafkaListener(
            topics = "reserve-inventory",
            groupId = "inventory-group"
    )
    @Transactional
    public void consume(
            ReserveInventoryCommand command) throws InterruptedException {

        try {
            Thread.sleep(1000);
            log.info("STEP 1: Command received");
            if(command!=null)
                log.info(String.valueOf(command));

            String eventId =
                    command.getSagaId();

            log.info(
                    "STEP 2: Checking duplicate ProcessedEventRepository eventId: "+eventId);

            if (repository.existsById(eventId)) {

                log.info(
                        "Duplicate command ignored");

                return;
            }

            log.info(
                    "STEP 3: Calling reduceStock");

            inventoryService.reduceStock(command);

            log.info(
                    "STEP 4: Publishing InventoryReservedEvent");

            producer.publishReserved(
                    new InventoryReservedEvent(
                            command.getSagaId(),
                            command.getOrderId(),
                            command.getProduct(),
                            command.getQuantity()
                    )
            );

            log.info(
                    "STEP 5: Saving ProcessedEvent");

            repository.save(
                    new ProcessedEvent(
                            eventId,
                            LocalDateTime.now()
                    )
            );

            log.info(
                    "STEP 6: SUCCESS");

        } catch (Exception e) {

            System.err.println(
                    "ERROR OCCURRED");

            producer.publishFailed(
                    new InventoryFailedEvent(
                            command.getSagaId(),
                            command.getOrderId(),
                            e.getMessage()
                    )
            );

            e.printStackTrace();

            throw new RuntimeException(e);
        }
    }
    @KafkaListener(
            topics = "release-inventory",
            groupId = "inventory-group"
    )
    @Transactional
    public void releaseInventory(
            ReleaseInventoryCommand command) {

        log.info("======================================");
        log.info("ReleaseInventoryCommand received");
        log.info("Order : {}", command.getOrderId());
        log.info("======================================");

        inventoryService.restoreStock(command);

        producer.publishReleased(
                new InventoryReleasedEvent(
                        command.getSagaId(),
                        command.getOrderId(),
                        command.getProduct(),
                        command.getQuantity()
                )
        );

        log.info("Inventory Released Successfully");
    }
}