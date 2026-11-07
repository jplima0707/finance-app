package com.jplima0707.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class EventSerializer {

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private EventSerializer() {}

    public static String toJson(Object event) {
        try {
            return mapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new EventSerializerException("Failed to serialize event", e);
        }
    }

    public static <T> T fromJson(String payload, Class<T> type) {
        try {
            return mapper.readValue(payload, type);
        } catch (Exception e) {
            throw new EventSerializerException("Failed to deserialize event", e);
        }
    }
}

