package com.example.transaction_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.transaction_service.domain.models.OutboxEvent;

public interface IOutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

    // SKIP LOCKED lets several instances relay in parallel without picking the same rows.
    @Query(
        value = """
            SELECT * FROM outbox_events
            WHERE status = 'PENDING'
            ORDER BY id
            LIMIT :limit
            FOR UPDATE SKIP LOCKED
            """,
        nativeQuery = true
    )
    List<OutboxEvent> lockPendingBatch(@Param("limit") int limit);
}
