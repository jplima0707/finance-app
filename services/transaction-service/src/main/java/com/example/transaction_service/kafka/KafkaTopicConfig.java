package com.example.transaction_service.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import com.jplima0707.common.KafkaTopics;

@Configuration
public class KafkaTopicConfig {

    @Bean
    NewTopic transactionRequestedTopic() {
        return TopicBuilder.name(KafkaTopics.TRANSACTION_REQUESTED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic transactionCreatedTopic() {
        return TopicBuilder.name(KafkaTopics.TRANSACTION_CREATED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic transactionAcceptedTopic() {
        return TopicBuilder.name(KafkaTopics.TRANSACTION_ACCEPTED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic transactionRejectedTopic() {
        return TopicBuilder.name(KafkaTopics.TRANSACTION_REJECTED)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
