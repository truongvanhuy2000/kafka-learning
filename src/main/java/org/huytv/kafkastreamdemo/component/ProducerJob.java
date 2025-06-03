package org.huytv.kafkastreamdemo.component;

import lombok.RequiredArgsConstructor;
import org.huytv.kafkastreamdemo.model.SavedFileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ProducerJob {
    private final KafkaTemplate<String, SavedFileDTO> kafkaTemplate;
    private String TOPIC = "test-topic";

    @Scheduled(fixedRate = 500, timeUnit = TimeUnit.MILLISECONDS)
    public void produce() {
        UUID uuid = UUID.randomUUID();
        kafkaTemplate.send(TOPIC, uuid.toString(),
            SavedFileDTO.builder()
                .id(uuid.toString())
                .size(new Random().nextInt(1_000_000))
                .updatedDate(randomDateTime())
                .fileName("file-" + uuid + ".txt")
                .userID(new Random().nextInt(10))
                .build()
        );
    }

    private LocalDateTime randomDateTime() {
        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.now();
        long startEpoch = start.toEpochSecond(ZoneOffset.UTC);
        long endEpoch = end.toEpochSecond(ZoneOffset.UTC);
        long randomEpoch = ThreadLocalRandom.current().nextLong(startEpoch, endEpoch);
        return LocalDateTime.ofEpochSecond(randomEpoch, 0, ZoneOffset.UTC);
    }
}
