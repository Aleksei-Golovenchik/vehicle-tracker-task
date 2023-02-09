package com.epam.learn.serde;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public class BigDecimalSerde implements Serde<BigDecimal>, Serializer<BigDecimal>, Deserializer<BigDecimal> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public BigDecimal deserialize(String s, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            return  OBJECT_MAPPER.readValue(bytes, BigDecimal.class);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public byte[] serialize(String topic, BigDecimal val) {
        if (val == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsBytes(val);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing JSON message", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}

    @Override
    public Serializer<BigDecimal> serializer() {
        return this;
    }

    @Override
    public Deserializer<BigDecimal> deserializer() {
        return this;
    }
}