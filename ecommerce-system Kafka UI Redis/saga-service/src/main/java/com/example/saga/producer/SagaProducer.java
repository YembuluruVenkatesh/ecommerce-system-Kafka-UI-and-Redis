package com.example.saga.producer;

import com.example.common.dto.ProcessPaymentCommand;
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
}