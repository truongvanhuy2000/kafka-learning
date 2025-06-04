package org.huytv.kafkastreamdemo;

import org.huytv.kafkastreamdemo.model.InvoiceDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.function.Consumer;

@EnableScheduling
@SpringBootApplication
public class KafkaStreamDemoApplication {

	@Bean
	public Consumer<InvoiceDTO> shipmentProcess() {
		return messageStream -> {
			System.out.println("Processing shipment for invoice: " + messageStream.getInvoiceNumber());
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(KafkaStreamDemoApplication.class, args);
	}

}
