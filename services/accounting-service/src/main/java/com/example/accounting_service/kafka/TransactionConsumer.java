package com.example.accounting_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.accounting_service.domain.dtos.types.TransactionValidationResult;
import com.example.accounting_service.services.TransactionConsumerService;
import com.jplima0707.common.EventSerializer;
import com.jplima0707.common.KafkaTopics;
import com.jplima0707.common.events.TransactionRequestedEvent;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionConsumer {
    private final TransactionConsumerService transactionConsumerService;
    private final TransactionPublisher transactionPublisher;

    @KafkaListener(
        topics = KafkaTopics.TRANSACTION_REQUESTED,
        groupId = "accounting-service"
    )
    @Transactional
    public void consumeTransactionRequested(String message) {
        log.info("Received transaction request event: {}", message);

        TransactionRequestedEvent event = EventSerializer.fromJson(message, TransactionRequestedEvent.class);

        if (event == null) {
            log.error("Failed to deserialize transaction request event: {}", message);
            return;
        }

        TransactionValidationResult isTransactionValid = transactionConsumerService.isTransactionValid(event);
        if (!isTransactionValid.isValid()) {
            log.warn("Transaction {} is invalid. Reason: {}", event.transactionId(), isTransactionValid.getReason());
            transactionPublisher.publishRejected(event, isTransactionValid.getReason());
            return;
        }

        transactionConsumerService.debitSourceAccount(event.sourceAccountId(), event.amount());
        transactionConsumerService.creditDestinationAccount(event.destinationAccountId(), event.amount());

        transactionPublisher.publishAccepted(event);
    }
}
