package com.epam.learn.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class VehicleDistanceLoggingService {

    @KafkaListener(topics = "${spring.kafka.consumer.distance-topic-name}", groupId = "vehicle-distance-logger")
    public void logVehicleDistance(ConsumerRecord<String, BigDecimal> receivedRecord) {
        log.info("Vehicle: \"{}\" traveled distance: {}", receivedRecord.key(), receivedRecord.value());
    }
}
