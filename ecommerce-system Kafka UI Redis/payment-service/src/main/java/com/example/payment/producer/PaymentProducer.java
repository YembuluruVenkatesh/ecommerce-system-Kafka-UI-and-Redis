package com.example.payment.producer;

import com.example.common.dto.PaymentCompletedEvent;
import com.example.common.dto.PaymentFailedEvent;
import com.example.payment.consumer.PaymentConsumer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentProducer {

    private static final Logger log = LoggerFactory.getLogger(PaymentProducer.class);

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public void publishSuccess(
            PaymentCompletedEvent event) {

        kafkaTemplate.send(
                "payment-completed",
                event.getOrderId(),
                event
        );

        log.info(
                "PaymentCompletedEvent Published"
        );
    }

    public void publishFailure(
            PaymentFailedEvent event) {

        kafkaTemplate.send(
                "payment-failed",
                event.getOrderId(),
                event
        );

        log.info(
                "PaymentFailedEvent Published"
        );
    }
}