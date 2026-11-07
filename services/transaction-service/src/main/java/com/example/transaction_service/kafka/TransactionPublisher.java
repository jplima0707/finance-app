package com.example.transaction_service.kafka;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.transaction_service.domain.models.Transaction;
import com.example.transaction_service.services.OutboxService;
import com.jplima0707.common.EventMetadata;
import com.jplima0707.common.KafkaTopics;
import com.jplima0707.common.events.TransactionCreatedEvent;
import com.jplima0707.common.events.TransactionRequestedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Writes transaction events to the transactional outbox. Must be called inside the
 * same DB transaction that persists the transaction; OutboxRelay delivers them to Kafka.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionPublisher {

    private static final String AGGREGATE_TYPE = "TRANSACTION";

    private final OutboxService outboxService;

    public void publishTransactionRequested(Transaction transaction, String traceId) {
        UUID eventId = UUID.randomUUID();
        TransactionRequestedEvent event = new TransactionRequestedEvent(
            eventId,
            transaction.getTransactionId(),
            transaction.getSourceAccountId(),
            transaction.getDestinationAccountId(),
            transaction.getAmount(),
            Instant.now(),
            new EventMetadata(
                "TRANSACTION_REQUESTED",
                1,
                Instant.now(),
                "transaction-service",
                traceId,
                transaction.getIdempotencyKey()
            )
        );

        outboxService.append(
            eventId,
            AGGREGATE_TYPE,
            transaction.getTransactionId(),
            "TRANSACTION_REQUESTED",
            KafkaTopics.TRANSACTION_REQUESTED,
            transaction.getSourceAccountId().toString(),
            event
        );

        log.info("Transaction requested event added to outbox for transaction ID: {}", transaction.getTransactionId());
    }

    public void publishTransactionCreated(Transaction transaction, String traceId) {
        UUID eventId = UUID.randomUUID();
        TransactionCreatedEvent event = new TransactionCreatedEvent(
            eventId,
            transaction.getTransactionId(),
            transaction.getSourceAccountId(),
            transaction.getDestinationAccountId(),
            transaction.getAmount(),
            Instant.now(),
            new EventMetadata(
                "TRANSACTION_CREATED",
                1,
                Instant.now(),
                "transaction-service",
                traceId,
                transaction.getIdempotencyKey()
            )
        );

        outboxService.append(
            eventId,
            AGGREGATE_TYPE,
            transaction.getTransactionId(),
            "TRANSACTION_CREATED",
            KafkaTopics.TRANSACTION_CREATED,
            transaction.getSourceAccountId().toString(),
            event
        );

        log.info("Transaction created event added to outbox for transaction ID: {}", transaction.getTransactionId());
    }
}
