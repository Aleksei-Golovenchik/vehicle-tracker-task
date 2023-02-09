package com.epam.learn.serde;

import com.epam.learn.dto.SignalDistanceEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;
import java.util.Map;

public class SignalDistanceEntrySerde implements Serde<SignalDistanceEntry>, Serializer<SignalDistanceEntry>, Deserializer<SignalDistanceEntry> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public SignalDistanceEntry deserialize(String s, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            return  OBJECT_MAPPER.readValue(bytes, SignalDistanceEntry.class);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public byte[] serialize(String topic, SignalDistanceEntry val) {
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
    public Serializer<SignalDistanceEntry> serializer() {
        return this;
    }

    @Override
    public Deserializer<SignalDistanceEntry> deserializer() {
        return this;
    }
}
