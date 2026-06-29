package com.example.orders.service;

import com.example.orders.dto.OrderCreatedEvent;
import com.example.orders.dto.OutboxEvent;
import com.example.orders.entity.Order;
import com.example.orders.producer.OrderProducer;
import com.example.orders.repository.OrderRepository;
import com.example.orders.repository.OutboxRepository;
import com.example.orders.scheduler.OutboxPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository repository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper mapper;

    @Transactional
    public Order create(OrderCreatedEvent event)
            throws Exception {

        Order order = Order.builder()
                .orderId(event.getOrderId())
                .product(event.getProduct())
                .quantity(event.getQuantity())
                .status("CREATED")
                .build();

        repository.save(order);

        OutboxEvent outbox =
                OutboxEvent.builder()
                        .eventType(
                                "ORDER_CREATED")
                        .payload(
                                mapper
                                        .writeValueAsString(
                                                event))
                        .published(false)
                        .createdAt(
                                LocalDateTime.now())
                        .build();

        outboxRepository.save(outbox);
        log.info(
                "Outbox payload: " +
                        mapper.writeValueAsString(event)
        );
        return order;
    }
}