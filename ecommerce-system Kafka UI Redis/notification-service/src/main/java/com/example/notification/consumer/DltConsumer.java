package com.example.notification.consumer;

import com.example.common.dto.OrderCreatedEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DltConsumer {

    private static final Logger log = LoggerFactory.getLogger(DltConsumer.class);

    private final ObjectMapper mapper;

    @KafkaListener(
            topics = "orders-dlt",
            groupId = "notification-dlt-group"
    )
    public void consume(OrderCreatedEvent event)
            throws Exception {

        log.info("===== DLT EVENT RECEIVED =====");
        if(event!=null)
            log.info(String.valueOf(event));
    }
}