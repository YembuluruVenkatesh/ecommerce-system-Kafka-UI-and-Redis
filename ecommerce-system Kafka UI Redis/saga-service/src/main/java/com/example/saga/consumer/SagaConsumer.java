package com.example.saga.consumer;

import com.example.common.dto.*;
import com.example.saga.entity.SagaState;
import com.example.saga.producer.SagaProducer;
import com.example.saga.repository.SagaStateRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SagaConsumer {

    private static final Logger log = LoggerFactory.getLogger(SagaConsumer.class);

    private final SagaStateRepository repository;
    private final SagaProducer producer;

    @KafkaListener(
            topics = "order-created",
            groupId = "saga-group"
    )
    public void consume(OrderCreatedEvent event) throws InterruptedException {

        Thread.sleep(10000);
        log.info("Saga received OrderCreatedEvent");
        if(event!=null)
            log.info(String.valueOf(event));

        String sagaId = UUID.randomUUID().toString();

        repository.save(
                new SagaState(
                        sagaId,
                        event.getOrderId(),
                        "STARTED"
                )
        );

        producer.sendReserveInventory(
                new ReserveInventoryCommand(
                        sagaId,
                        event.getOrderId(),
                        event.getProduct(),
                        event.getQuantity()
                )
        );

        log.info(
                "Saga started: " + sagaId
        );
    }

    @KafkaListener(
            topics = "inventory-reserved",
            groupId = "saga-group"
    )
    public void inventoryReserved(
            InventoryReservedEvent event) throws InterruptedException {
        Thread.sleep(10000);
        log.info(
                "Saga received InventoryReservedEvent"
        );
        if(event!=null)
            log.info(String.valueOf(event));

        SagaState saga =
                repository.findById(
                        event.getSagaId()
                ).orElseThrow();

        saga.setStatus(
                "INVENTORY_RESERVED"
        );

        repository.save(saga);

        producer.sendProcessPayment(
                new ProcessPaymentCommand(
                        event.getSagaId(),
                        event.getOrderId(),
                        1000.0
                )
        );

        log.info(
                "Payment processing started"
        );
    }

    @KafkaListener(
            topics = "payment-completed",
            groupId = "saga-group"
    )
    public void paymentCompleted(
            PaymentCompletedEvent event) throws InterruptedException {
        Thread.sleep(10000);
        log.info(
                "PaymentCompletedEvent received"
        );

        SagaState saga =
                repository.findById(
                        event.getSagaId()
                ).orElseThrow();

        saga.setStatus(
                "COMPLETED"
        );

        repository.save(saga);

        log.info(
                "Saga COMPLETED"
        );
    }

}