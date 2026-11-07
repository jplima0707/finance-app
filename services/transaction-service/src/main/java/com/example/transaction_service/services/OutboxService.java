package com.example.transaction_service.services;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction_service.domain.models.OutboxEvent;
import com.example.transaction_service.repositories.IOutboxEventRepository;
import com.jplima0707.common.EventSerializer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final IOutboxEventRepository outboxEventRepository;

    // MANDATORY: the outbox row must be written in the same DB transaction as the aggregate change.
    @Transactional(propagation = Propagation.MANDATORY)
    public void append(UUID eventId, String aggregateType, UUID aggregateId,
                       String eventType, String topic, String messageKey, Object event) {
        outboxEventRepository.save(
            OutboxEvent.pending(
                eventId,
                aggregateType,
                aggregateId,
                eventType,
                topic,
                messageKey,
                EventSerializer.toJson(event)
            )
        );
    }
}
