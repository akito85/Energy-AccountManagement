package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for updating a billing item
 */
@Data
public class BillingItemUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Billing item ID cannot be null")
    private Integer billingItemId;

    private String itemName;

    private String itemDescription;

    private Integer itemType;

    private BigDecimal amount;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal taxAmount;

    private BigDecimal discountAmount;

    private BigDecimal netAmount;
}
