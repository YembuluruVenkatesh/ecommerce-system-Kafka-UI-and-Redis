package com.example.saga.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "saga_state")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SagaState {

    @Id
    private String sagaId;

    private String orderId;

    private String status;
}