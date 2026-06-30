package com.example.payment.consumer;

import com.example.common.dto.PaymentCompletedEvent;
import com.example.common.dto.PaymentFailedEvent;
import com.example.common.dto.ProcessPaymentCommand;
import com.example.payment.producer.PaymentProducer;
import com.example.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class PaymentConsumer {
    private static final Logger log = LoggerFactory.getLogger(PaymentConsumer.class);

    private final PaymentService paymentService;
    private final PaymentProducer producer;
    private final Random random = new Random();

    @KafkaListener(
            topics = "process-payment",
            groupId = "payment-group"
    )
    public void consume(ProcessPaymentCommand command)
            throws InterruptedException {

        Thread.sleep(1000);

        log.info("======================================");
        log.info("Payment Request Received");
        log.info("Saga Id  : {}", command.getSagaId());
        log.info("Order Id : {}", command.getOrderId());
        log.info("Amount   : {}", command.getAmount());
        log.info("======================================");

        boolean paymentSuccess = random.nextBoolean();
        //boolean paymentSuccess = false;

        if (paymentSuccess) {

            log.info("======================================");
            log.info("Payment Successful");
            log.info("======================================");

            producer.publishSuccess(

                    new PaymentCompletedEvent(

                            command.getSagaId(),
                            command.getOrderId(),
                            command.getAmount()
                    )
            );

        } else {

            log.info("======================================");
            log.info("Payment Failed");
            log.info("======================================");

            producer.publishFailure(

                    new PaymentFailedEvent(

                            command.getSagaId(),
                            command.getOrderId(),
                            "Card Declined",
                            command.getProduct(),
                            command.getQuantity()
                    )
            );
        }
    }
}