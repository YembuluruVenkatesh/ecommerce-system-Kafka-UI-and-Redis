package com.example.orders.producer;

import com.example.common.dto.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderProducer {

    private static final Logger log = LoggerFactory.getLogger(OrderProducer.class);

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public void publish(
            OrderCreatedEvent event) {

        kafkaTemplate.send(
                "order-created",
                event.getOrderId(),
                event
        );

        log.info(
                "OrderCreatedEvent Published"
        );
    }
}