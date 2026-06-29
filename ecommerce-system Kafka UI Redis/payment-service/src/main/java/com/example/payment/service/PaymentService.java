package com.example.payment.service;

import com.example.common.dto.ProcessPaymentCommand;
import com.example.payment.entity.Payment;
import com.example.payment.producer.PaymentProducer;
import com.example.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository repository;

    public void processPayment(
            ProcessPaymentCommand command) {

        Payment payment =
                Payment.builder()
                        .orderId(command.getOrderId())
                        .amount(command.getAmount())
                        .status("COMPLETED")
                        .build();

        repository.save(payment);

        log.info(
                "Payment processed successfully"
        );
    }
}