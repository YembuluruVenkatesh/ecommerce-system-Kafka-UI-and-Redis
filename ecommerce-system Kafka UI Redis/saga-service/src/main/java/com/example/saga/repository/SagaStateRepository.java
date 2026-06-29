package com.example.saga.repository;

import com.example.saga.entity.SagaState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SagaStateRepository
        extends JpaRepository<SagaState,String> {
}