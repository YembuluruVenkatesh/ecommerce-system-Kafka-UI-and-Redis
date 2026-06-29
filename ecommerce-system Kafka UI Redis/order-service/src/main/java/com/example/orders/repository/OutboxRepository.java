package com.example.orders.repository;

import com.example.orders.dto.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxRepository
        extends JpaRepository<OutboxEvent, Long> {

    List<OutboxEvent>
    findByPublishedFalse();
}