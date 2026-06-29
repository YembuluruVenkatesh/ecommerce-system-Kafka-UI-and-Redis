package com.example.payment.consumer;

import com.example.common.dto.PaymentCompletedEvent;
import com.example.common.dto.ProcessPaymentCommand;
import com.example.payment.producer.PaymentProducer;
import com.example.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentConsumer {
    private static final Logger log = LoggerFactory.getLogger(PaymentConsumer.class);

    private final PaymentService paymentService;
    private final PaymentProducer producer;

    @KafkaListener(
            topics = "process-payment",
            groupId = "payment-group"
    )
    public void consume(
            ProcessPaymentCommand command) throws InterruptedException {
        Thread.sleep(10000);
        log.info(
                "ProcessPaymentCommand received"
        );

        paymentService.processPayment(command);

        producer.publishSuccess(
                new PaymentCompletedEvent(
                        command.getSagaId(),
                        command.getOrderId(),
                        command.getAmount()
                )
        );
    }
}