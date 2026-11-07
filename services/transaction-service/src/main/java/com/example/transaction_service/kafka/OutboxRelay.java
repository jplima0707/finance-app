package com.example.transaction_service.kafka;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction_service.domain.models.OutboxEvent;
import com.example.transaction_service.repositories.IOutboxEventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxRelay {

    private final IOutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${outbox.relay.batch-size:50}")
    private int batchSize;

    @Value("${outbox.relay.max-attempts:10}")
    private int maxAttempts;

    @Value("${outbox.relay.send-timeout-ms:10000}")
    private long sendTimeoutMs;

    // At-least-once delivery: a crash after Kafka acks but before the commit re-sends the event.
    // Consumers deduplicate using eventId.
    @Scheduled(fixedDelayString = "${outbox.relay.fixed-delay-ms:1000}")
    @Transactional
    public void relayPendingEvents() {
        List<OutboxEvent> batch = outboxEventRepository.lockPendingBatch(batchSize);
        if (batch.isEmpty()) {
            return;
        }

        for (OutboxEvent event : batch) {
            try {
                kafkaTemplate.send(event.getTopic(), event.getMessageKey(), event.getPayload())
                        .get(sendTimeoutMs, TimeUnit.MILLISECONDS);
                event.markSent(Instant.now());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                event.markAttemptFailed("Interrupted while publishing", maxAttempts);
                break;
            } catch (Exception e) {
                event.markAttemptFailed(e.getMessage(), maxAttempts);
                log.warn("Failed to relay outbox event {} (attempt {}): {}",
                        event.getEventId(), event.getAttempts(), e.getMessage());
                // Stop at the first failure so later events of this batch do not overtake it.
                break;
            }
        }
    }
}
