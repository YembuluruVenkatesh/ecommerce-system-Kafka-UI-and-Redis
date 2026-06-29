package com.example.orders.controller;

import com.example.orders.dto.OrderCreatedEvent;
import com.example.orders.entity.Order;
import com.example.orders.service.OrderService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<Order> create(
            @RequestBody OrderCreatedEvent event)
            throws Exception {

        return ResponseEntity.ok(
                service.create(event));
    }
}