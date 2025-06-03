package org.huytv.kafkastreamdemo.component;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
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
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FileProducer {
    private final KafkaTemplate<String, SavedFileDTO> savedFileKafkaTemplate;
    private static final Random random = new Random();
    private final NewTopic fileTopic;

    @Scheduled(fixedRate = 5000, timeUnit = TimeUnit.MILLISECONDS)
    public void produce() {
        UUID uuid = UUID.randomUUID();
        savedFileKafkaTemplate.send(fileTopic.name(), uuid.toString(),
            SavedFileDTO.builder()
                .id(uuid.toString())
                .size(random.nextInt(1_000_000))
                .updatedDate(randomDateTime())
                .fileName("file-" + uuid + ".txt")
                .userID(random.nextInt(10))
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
