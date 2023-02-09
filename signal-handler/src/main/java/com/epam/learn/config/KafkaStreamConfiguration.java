package com.epam.learn.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaStreamConfiguration {
    @Bean
    NewTopic counts() {
        return TopicBuilder.name("output").partitions(6).replicas(3).build();
    }
}
