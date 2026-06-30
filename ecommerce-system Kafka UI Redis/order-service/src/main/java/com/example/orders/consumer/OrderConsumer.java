package com.example.orders.consumer;

import com.example.common.dto.OrderCancelledEvent;
import com.example.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderConsumer {

    private final OrderService orderService;

    @KafkaListener(
            topics = "order-cancelled",
            groupId = "order-group"
    )
    public void consume(OrderCancelledEvent event) {

        log.info("======================================");
        log.info("OrderCancelledEvent Received");
        log.info("Order Id : {}", event.getOrderId());
        log.info("Reason   : {}", event.getReason());
        log.info("======================================");

        orderService.cancelOrder(
                event.getOrderId()
        );
    }
}