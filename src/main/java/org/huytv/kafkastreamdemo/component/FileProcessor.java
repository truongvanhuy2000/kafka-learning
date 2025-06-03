package org.huytv.kafkastreamdemo.component;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.huytv.kafkastreamdemo.model.SavedFileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import static org.huytv.kafkastreamdemo.configuration.KafkaConfiguration.STRING_SERDE;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FileProcessor {
    private final NewTopic fileTopic;
    private final NewTopic fileCountTopic;

    private static final Serde<SavedFileDTO> FILE_DTO_SERDE = Serdes.serdeFrom(
        new JsonSerializer<>(),
        new JsonDeserializer<>(SavedFileDTO.class)
    );

    @Bean
    KStream<String, SavedFileDTO> FileCountProcessor(@Qualifier("myKStreamBuilder") StreamsBuilder streamsBuilder) {
        KStream<String, SavedFileDTO> messageStream = streamsBuilder
            .stream(fileTopic.name(), Consumed.with(STRING_SERDE, FILE_DTO_SERDE))
            .filter((key, value) -> value.getSize() > 500_000);
        KTable<Integer, Long> wordCountForUsers = messageStream
            .map((key, value) -> new KeyValue<>(value.getUserID(), value.getId()))
            .groupByKey(Grouped.with(Serdes.Integer(), Serdes.String()))
            .count();
        wordCountForUsers.toStream().to(fileCountTopic.name(), Produced.with(Serdes.Integer(), Serdes.Long()));
        return messageStream;
    }
}
