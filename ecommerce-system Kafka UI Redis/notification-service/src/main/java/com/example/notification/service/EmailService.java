package com.example.notification.service;

import com.example.common.dto.OrderCreatedEvent;
import com.example.notification.consumer.NotificationConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    public void sendEmail(OrderCreatedEvent event) {

        log.info(
                "EMAIL SENT for order: "
                        + event.getOrderId()
        );
    }
}