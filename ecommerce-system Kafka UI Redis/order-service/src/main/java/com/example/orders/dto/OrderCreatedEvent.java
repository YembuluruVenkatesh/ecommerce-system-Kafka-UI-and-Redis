package com.example.orders.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {

    private String eventId;

    private String orderId;

    private String product;

    private Integer quantity;

    private String status;

    private LocalDateTime createdAt;
}