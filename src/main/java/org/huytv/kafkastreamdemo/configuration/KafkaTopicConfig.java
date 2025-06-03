package org.huytv.kafkastreamdemo.configuration;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    private String bootstrapAddress = "localhost:9092,localhost:9093";

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic fileTopic() {
        return new NewTopic("file-topic", 3, (short) 1);
    }

    @Bean
    public NewTopic fileCountTopic() {
        return new NewTopic("streams-filecount-output", 3, (short) 1);
    }

    @Bean
    public NewTopic invoiceTopic() {
        return new NewTopic("invoice-topic", 3, (short) 1);
    }

    @Bean
    public NewTopic shipmentTopic() {
        return new NewTopic("SHIPMENT", 3, (short) 1);
    }

    @Bean
    public NewTopic loyaltyTopic() {
        return new NewTopic("LOYALTY", 3, (short) 1);
    }

    @Bean
    public NewTopic hadoopTopic() {
        return new NewTopic("HADOOP", 3, (short) 1);
    }
}
