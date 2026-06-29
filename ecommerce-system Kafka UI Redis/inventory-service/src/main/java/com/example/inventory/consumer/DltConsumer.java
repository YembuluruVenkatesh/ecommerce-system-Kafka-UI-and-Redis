package com.example.inventory.consumer;

import com.example.common.dto.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DltConsumer {
    private static final Logger log = LoggerFactory.getLogger(DltConsumer.class);


    @KafkaListener(
            topics = "orders-dlt",
            groupId = "inventory-dlt-group"
    )
    public void consume(OrderCreatedEvent event) {

        log.info(
                "DLT Event received: "
                        + event
        );
    }
}