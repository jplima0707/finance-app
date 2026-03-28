package com.example.accounting_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransactionConsumer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    
    
}
