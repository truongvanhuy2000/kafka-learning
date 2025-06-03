package org.huytv.kafkastreamdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class KafkaStreamDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaStreamDemoApplication.class, args);
	}

}
