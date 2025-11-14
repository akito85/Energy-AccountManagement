package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for creating a new billing item
 */
@Data
public class BillingItemCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Installment ID cannot be null")
    private Integer installmentId;

    @NotEmpty(message = "Item name cannot be empty")
    private String itemName;

    private String itemDescription;

    @NotNull(message = "Item type cannot be null")
    private Integer itemType;

    @NotNull(message = "Amount cannot be null")
    private BigDecimal amount;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal taxAmount;

    private BigDecimal discountAmount;

    private BigDecimal netAmount;
}
