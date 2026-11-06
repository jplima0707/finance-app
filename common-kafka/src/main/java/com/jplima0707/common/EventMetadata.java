package com.jplima0707.common;

import java.time.Instant;

public record EventMetadata(
    String eventType,
    int version,
    Instant publishedAt,
    String producer,
    String traceId,
    String idempotencyKey
) {

    public EventMetadata(String eventType, int version, Instant publishedAt, String producer) {
        this(eventType, version, publishedAt, producer, null, null);
    }
}
