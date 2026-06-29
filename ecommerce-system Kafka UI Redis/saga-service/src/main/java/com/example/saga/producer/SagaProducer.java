package com.example.saga.producer;

import com.example.common.dto.OrderCancelledEvent;
import com.example.common.dto.ProcessPaymentCommand;
import com.example.common.dto.ReleaseInventoryCommand;
import com.example.common.dto.ReserveInventoryCommand;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SagaProducer {

    private final KafkaTemplate<String,Object> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(SagaProducer.class);
    public void sendReserveInventory(
            ReserveInventoryCommand command) {

        kafkaTemplate.send(
                "reserve-inventory",
                command.getOrderId(),
                command
        );

        log.info("ReserveInventoryCommand Published");
    }

    public void sendProcessPayment(
            ProcessPaymentCommand command) {

        kafkaTemplate.send(
                "process-payment",
                command.getOrderId(),
                command
        );

        log.info(
                "ProcessPaymentCommand Published"
        );
    }

    public void publishReleaseInventory(
            ReleaseInventoryCommand command) {

        kafkaTemplate.send(
                "release-inventory",
                command
        );

        log.info(
                "ReleaseInventoryCommand published for Order {}",
                command.getOrderId()
        );
    }

    public void publishOrderCancelled(OrderCancelledEvent event) {
        kafkaTemplate.send(
                "order-cancelled",
                event
        );

        log.info("======================================");
        log.info("Publishing OrderCancelledEvent");
        log.info("Order : {}", event.getOrderId());
        log.info("======================================");
    }
}