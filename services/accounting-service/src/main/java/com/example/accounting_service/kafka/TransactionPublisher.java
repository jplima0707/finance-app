package com.example.accounting_service.kafka;

import java.time.Instant;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.jplima0707.common.EventMetadata;
import com.jplima0707.common.EventSerializer;
import com.jplima0707.common.KafkaTopics;
import com.jplima0707.common.events.TransactionAccepted;
import com.jplima0707.common.events.TransactionRejectedEvent;
import com.jplima0707.common.events.TransactionRequestedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    
    protected void publishRejected(TransactionRequestedEvent event, String reason) {
        TransactionRejectedEvent rejectedEvent = new TransactionRejectedEvent(
                UUID.randomUUID(),
                event.transactionId(),
                event.sourceAccountId(),
                event.destinationAccountId(),
                Instant.now(),
                reason,
                new EventMetadata(
                        "TRANSACTION_REJECTED",
                        1,
                        Instant.now(),
                        "accounting-service"
                )
        );

        kafkaTemplate.send(
                KafkaTopics.TRANSACTION_REJECTED,
                event.sourceAccountId().toString(),
                EventSerializer.toJson(rejectedEvent)
        );

        log.warn("Published rejected event for transaction {}. Reason: {}", event.transactionId(), reason);
    }

    protected void publishAccepted(TransactionRequestedEvent event) {
        TransactionAccepted acceptedEvent = new TransactionAccepted(
                UUID.randomUUID(),
                event.transactionId(),
                event.sourceAccountId(),
                event.destinationAccountId(),
                event.amount(),
                Instant.now(),
                new EventMetadata(
                        "TRANSACTION_ACCEPTED",
                        1,
                        Instant.now(),
                        "accounting-service"
                )
        );

        kafkaTemplate.send(
                KafkaTopics.TRANSACTION_ACCEPTED,
                event.sourceAccountId().toString(),
                EventSerializer.toJson(acceptedEvent)
        );

        log.info("Published accepted event for transaction {}", event.transactionId());
    }
}
