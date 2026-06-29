package com.example.notification.consumer;

import com.example.common.dto.OrderCreatedEvent;
import com.example.notification.entity.ProcessedEvent;
import com.example.notification.repository.ProcessedEventRepository;
import com.example.notification.service.EmailService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final ObjectMapper mapper;
    private final EmailService emailService;
    private final ProcessedEventRepository repository;
    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

    @KafkaListener(
            topics = "orders",
            groupId = "notification-group"
    )
    //@Transactional
    public void consume(OrderCreatedEvent event) {

        try {
            Thread.sleep(1000);
            log.info("STEP 1: Event received");
            if(event!=null)
                log.info(String.valueOf(event));
            log.info("STEP 2: Checking duplicate");
            if (repository.existsById(event.getEventId())) {
                log.info("Duplicate event ignored");
                return;
            }
            log.info("STEP 3: Send Email");
            emailService.sendEmail(event);
            repository.save(new ProcessedEvent(event.getEventId(),LocalDateTime.now()));
            log.info("Notification processed: " + event.getOrderId());

            /*log.info("Received Event: "+event);
            throw new RuntimeException("Testing DLT");*/
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}