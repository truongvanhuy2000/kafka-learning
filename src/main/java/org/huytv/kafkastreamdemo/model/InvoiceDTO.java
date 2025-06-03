package org.huytv.kafkastreamdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private String invoiceNumber;
    private String createdTime;
    private String storeID;
    private String posID;
    private String customerType;
    private String paymentMethod;
    private String deliveryType;
    private String city;
    private String state;
    private String pinCode;
    private String itemCode;
    private String itemDescription;
    private double itemPrice;
    private int itemQty;
    private double totalValue;
}
