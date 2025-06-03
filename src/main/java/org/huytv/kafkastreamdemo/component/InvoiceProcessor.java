package org.huytv.kafkastreamdemo.component;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.huytv.kafkastreamdemo.model.InvoiceDTO;
import org.huytv.kafkastreamdemo.model.MaskedInvoiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import static org.huytv.kafkastreamdemo.configuration.KafkaConfiguration.STRING_SERDE;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvoiceProcessor {
    private final NewTopic shipmentTopic;
    private final NewTopic invoiceTopic;
    private final NewTopic loyaltyTopic;
    private final NewTopic hadoopTopic;

    private static final Serde<InvoiceDTO> INVOICE_DTO_SERDE = Serdes.serdeFrom(
        new JsonSerializer<>(),
        new JsonDeserializer<>(InvoiceDTO.class)
    );

    private static final Serde<MaskedInvoiceDTO> MASKED_INVOICE_DTO_SERDE = Serdes.serdeFrom(
        new JsonSerializer<>(),
        new JsonDeserializer<>(MaskedInvoiceDTO.class)
    );

    @Bean
    public KStream<String, InvoiceDTO> shipmentProcessor(@Qualifier("myKStreamBuilder") StreamsBuilder streamsBuilder) {
        KStream<String, InvoiceDTO> messageStream = streamsBuilder
            .stream(invoiceTopic.name(), Consumed.with(STRING_SERDE, INVOICE_DTO_SERDE))
            .filter((key, value) -> value.getDeliveryType().equalsIgnoreCase("HOME-DELIVERY"));
        messageStream.to(shipmentTopic.name(), Produced.with(Serdes.String(), INVOICE_DTO_SERDE));
        return messageStream;
    }

    @Bean
    public KStream<String, String> loyaltyProcessor(@Qualifier("myKStreamBuilder") StreamsBuilder streamsBuilder) {
        StoreBuilder<KeyValueStore<String, Double>> kvStoreBuilder = Stores.keyValueStoreBuilder(
            Stores.inMemoryKeyValueStore("reward-store"),
            Serdes.String(),
            Serdes.Double()
        );
        streamsBuilder.addStateStore(kvStoreBuilder);

        KStream<String, String> messageStream = streamsBuilder
            .stream(invoiceTopic.name(), Consumed.with(STRING_SERDE, INVOICE_DTO_SERDE))
            .filter((key, value) -> value.getDeliveryType().equalsIgnoreCase("PRIME"))
                .mapValues(InvoiceDTO::getInvoiceNumber);
        messageStream.to(loyaltyTopic.name(), Produced.with(Serdes.String(), Serdes.String()));
        return messageStream;
    }

    @Bean
    public KStream<String, MaskedInvoiceDTO> hadoopProcessor(@Qualifier("myKStreamBuilder") StreamsBuilder streamsBuilder) {
        KStream<String, MaskedInvoiceDTO> messageStream = streamsBuilder
            .stream(invoiceTopic.name(), Consumed.with(STRING_SERDE, INVOICE_DTO_SERDE))
            .mapValues((value) -> MaskedInvoiceDTO.builder()
                .invoiceNumber(value.getInvoiceNumber())
                .createdTime(value.getCreatedTime())
                .posId(value.getStoreID())
                .storeId(value.getStoreID())
                .build());
        messageStream.to(hadoopTopic.name(), Produced.with(Serdes.String(), MASKED_INVOICE_DTO_SERDE));
        return messageStream;
    }
}
