package com.epam.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableKafkaStreams
public class SignalHandlingApplication {
    public static void main(String[] args) {
        SpringApplication.run(SignalHandlingApplication.class, args);
    }
}