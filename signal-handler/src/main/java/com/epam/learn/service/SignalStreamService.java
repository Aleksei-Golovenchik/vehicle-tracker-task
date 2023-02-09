package com.epam.learn.service;

import com.epam.learn.dto.SignalDistanceEntry;
import com.epam.learn.dto.VehicleSignal;
import com.epam.learn.util.SignalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.internals.MaterializedInternal;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignalStreamService {

    private final StreamsBuilderFactoryBean factoryBean;

    @Autowired
    public void process(StreamsBuilder builder,
                        @Value("${spring.kafka.streams.publish-distance-topic:output}") String outputTopic,
                        @Value("${spring.kafka.streams.consume-signal-topic:input}") String inputTopic) {

        final Serde<VehicleSignal> signalSerde = new JsonSerde<>(VehicleSignal.class);
        final Serde<SignalDistanceEntry> signalDistanceEntrySerde = new JsonSerde<>(SignalDistanceEntry.class);
        final Serde<BigDecimal> bigDecimalSerde = new JsonSerde<>(BigDecimal.class);
        final Serde<String> stringSerde = Serdes.String();

        KStream<String, VehicleSignal> vehicleSignalStream = builder.stream(inputTopic, Consumed.with(stringSerde, signalSerde));


        KTable<String, BigDecimal> distanceKTable = vehicleSignalStream
                .groupBy((key, value) -> key, Grouped.with(stringSerde, signalSerde))
                .aggregate(
                        SignalDistanceEntry::new,
                        (key, value, aggregation) -> {
                            if (SignalUtil.isEmpty(aggregation)) {
                                SignalDistanceEntry.createWithZeroDistance(value);
                                return SignalDistanceEntry.createWithZeroDistance(value);
                            }
                            SignalUtil.evaluateDistanceAndUpdateLastCoordinates(aggregation, value);
                            log.info("Recorded new distance: {}", aggregation.getDistance());
                            return aggregation;
                        },
                        new MaterializedInternal<>(Materialized.<String, SignalDistanceEntry, KeyValueStore<Bytes, byte[]>>as("distanceAndSignal"))
                                .withKeySerde(stringSerde)
                                .withValueSerde(signalDistanceEntrySerde))
                .mapValues(
                        SignalDistanceEntry::getDistance,
                        new MaterializedInternal<>(Materialized.<String, BigDecimal, KeyValueStore<Bytes, byte[]>>as("distance"))
                        .withKeySerde(stringSerde)
                        .withValueSerde(bigDecimalSerde));

        distanceKTable.toStream().to(outputTopic, Produced.with(stringSerde, bigDecimalSerde));


    }
}
