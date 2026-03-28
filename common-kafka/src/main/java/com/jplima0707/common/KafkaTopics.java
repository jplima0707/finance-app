package com.jplima0707.common;

public final class KafkaTopics {
    public static final String TRANSACTION_CREATED = "transactions.created";
    public static final String TRANSACTION_REJECTED = "transactions.rejected"; 
    public static final String TRANSACTION_REQUESTED = "transactions.requested";
    public static final String TRANSACTION_ACCEPTED = "transactions.accepted";

    private KafkaTopics() {
        // Prevent instantiation
    }
}
