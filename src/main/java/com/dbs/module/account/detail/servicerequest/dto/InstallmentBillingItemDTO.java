package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for Installment Billing Item
 */
@Data
public class InstallmentBillingItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer billingItemId;
    private Integer installmentId;
    private String itemName;
    private String itemDescription;
    private Integer itemType;
    private String itemTypeName;
    private BigDecimal amount;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal netAmount;
}
