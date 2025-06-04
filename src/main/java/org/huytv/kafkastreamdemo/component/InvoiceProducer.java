package org.huytv.kafkastreamdemo.component;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.huytv.kafkastreamdemo.model.InvoiceDTO;
import org.huytv.kafkastreamdemo.model.SavedFileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InvoiceProducer {
    private static final Random random = new Random();
    private final KafkaTemplate<String, InvoiceDTO> invoiceKafkaTemplate;
    private final NewTopic invoiceTopic;

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    public void produce() {
        UUID uuid = UUID.randomUUID();
        invoiceKafkaTemplate.send(invoiceTopic.name(), uuid.toString(), generateRandomInvoice(uuid.toString()));
    }

    private static InvoiceDTO generateRandomInvoice(String invoiceId) {
        InvoiceDTO invoice = new InvoiceDTO();
        invoice.setInvoiceNumber(invoiceId);
        invoice.setCreatedTime(String.valueOf(System.currentTimeMillis()));
        invoice.setStoreID("Store-" + random.nextInt(10));
        invoice.setPosID("POS-" + random.nextInt(100));
        invoice.setCustomerType(random.nextBoolean() ? "PRIME" : "REGULAR");
        invoice.setPaymentMethod(random.nextBoolean() ? "CARD" : "CASH");
        invoice.setDeliveryType(random.nextBoolean() ? "HOME-DELIVERY" : "PICK-UP");
        invoice.setCity("City" + random.nextInt(100));
        invoice.setState("State" + random.nextInt(50));
        invoice.setPinCode(String.valueOf(100000 + random.nextInt(899999)));
        invoice.setItemCode("Item" + random.nextInt(1000));
        invoice.setItemDescription("Item Description " + random.nextInt(100));
        invoice.setItemPrice(random.nextDouble() * 500);
        invoice.setItemQty(random.nextInt(5) + 1);
        invoice.setTotalValue(invoice.getItemPrice() * invoice.getItemQty());
        return invoice;
    }
}
