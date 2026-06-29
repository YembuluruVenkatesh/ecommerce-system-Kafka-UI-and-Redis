package com.example.orders.scheduler;

import com.example.common.dto.OrderCreatedEvent;
import com.example.orders.dto.OutboxEvent;
import com.example.orders.producer.OrderProducer;
import com.example.orders.repository.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);
    private final OutboxRepository repository;
    private final OrderProducer producer;
    private final ObjectMapper mapper;

    @Scheduled(fixedDelay = 5000)
    public void publish() throws Exception {

        List<OutboxEvent> events = repository.findByPublishedFalse();

        log.info("events : " + events);

        for (OutboxEvent e : events) {

            OrderCreatedEvent event =
                    mapper.readValue(
                            e.getPayload(),
                            OrderCreatedEvent.class
                    );

            log.info("Publishing payload: " + event);
            producer.publish(event);
            e.setPublished(true);
            repository.save(e);
            log.info("Published: " + e.getId());
        }
    }
}