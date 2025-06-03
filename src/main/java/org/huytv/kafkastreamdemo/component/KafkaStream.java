package org.huytv.kafkastreamdemo.component;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.huytv.kafkastreamdemo.model.SavedFileDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static org.huytv.kafkastreamdemo.configuration.KafkaConfiguration.FILE_DTO_SERDE;
import static org.huytv.kafkastreamdemo.configuration.KafkaConfiguration.STRING_SERDE;

@Configuration
public class KafkaStream {

    @Bean
    KStream<String, SavedFileDTO> FileCountProcessor(@Qualifier("myKStreamBuilder") StreamsBuilder streamsBuilder) {
        KStream<String, SavedFileDTO> messageStream = streamsBuilder
            .stream("test-topic", Consumed.with(STRING_SERDE, FILE_DTO_SERDE))
            .peek((key, value) -> System.out.println("key: " + key + " value: " + value))
            .filter((key, value) -> value.getSize() > 100_000);
        KTable<Integer, Long> wordCountForUsers = messageStream
            .map((key, value) -> new KeyValue<>(value.getUserID(), value.getId()))
            .groupByKey(Grouped.with(Serdes.Integer(), Serdes.String()))
            .count();
        wordCountForUsers.suppress(Suppressed.untilTimeLimit(Duration.ofSeconds(30), Suppressed.BufferConfig.maxBytes(1_000_000)))
            .toStream()
            .peek((key, value) -> System.out.println("UserID: " + key + ", Count: " + value))
            .to("streams-filecount-output", Produced.with(Serdes.Integer(), Serdes.Long()));
        return messageStream;
    }
}
