package org.huytv.kafkastreamdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaskedInvoiceDTO {
    private String invoiceNumber;
    private String createdTime;
    private String storeId;
    private String posId;
}
