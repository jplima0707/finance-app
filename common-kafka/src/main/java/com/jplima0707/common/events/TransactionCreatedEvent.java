package com.jplima0707.common.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.jplima0707.common.EventMetadata;

public record TransactionCreatedEvent(
    UUID eventId,
    UUID transactionId,
    UUID sourceAccountId,
    UUID destinationAccountId,
    BigDecimal amount,
    Instant ocurredAt,
    EventMetadata eventMetadata
) {
    
}
