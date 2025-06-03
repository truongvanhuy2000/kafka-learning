package org.huytv.kafkastreamdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private String invoiceNumber;
    private String customerCardNo;
    private double totalAmount;
    private double earnedLoyaltyPoints;
    private double totalLoyaltyPoints;
}
