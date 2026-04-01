package com.example.transaction_service.kafka;

import java.time.Instant;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.example.transaction_service.domain.models.Transaction;
import com.jplima0707.common.EventMetadata;
import com.jplima0707.common.EventSerializer;
import com.jplima0707.common.KafkaTopics;
import com.jplima0707.common.events.TransactionCreatedEvent;
import com.jplima0707.common.events.TransactionRequestedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    
    public void publishTransactionRequested(Transaction transaction) {
        log.info("Publishing transaction requested event for transaction ID: {}", transaction.getTransactionId());  
        TransactionRequestedEvent event = new TransactionRequestedEvent(
            UUID.randomUUID(),
            transaction.getTransactionId(),
            transaction.getSourceAccountId(),
            transaction.getDestinationAccountId(),
            transaction.getAmount(),
            Instant.now(),
            new EventMetadata(
                "TRANSACTION_REQUESTED",
                1,
                Instant.now(),
                "transaction-service"
            )
        );

        kafkaTemplate.send(
            KafkaTopics.TRANSACTION_REQUESTED,
            transaction.getSourceAccountId().toString(),
            EventSerializer.toJson(event)
        );
    }

    public void publishTransactionCreated(Transaction transaction) {
        log.info("Publishing transaction created event for transaction ID: {}", transaction.getTransactionId());
        TransactionCreatedEvent event = new TransactionCreatedEvent(
            UUID.randomUUID(),
            transaction.getTransactionId(),
            transaction.getSourceAccountId(),
            transaction.getDestinationAccountId(),
            transaction.getAmount(),    
            Instant.now(),
            new EventMetadata(
                "TRANSACTION_CREATED",
                1,
                Instant.now(),
                "transaction-service"
            )
        );

        kafkaTemplate.send(
            KafkaTopics.TRANSACTION_CREATED,
            transaction.getSourceAccountId().toString(),
            EventSerializer.toJson(event)
        );
    }


}
