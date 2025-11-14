package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for product selection in POS
 */
@Data
public class ProductSelectionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    // Promo eligibility
    private Boolean hasPromo = false;
    private Integer promoId;
    private String promoName;
    private BigDecimal promoDiscount;
}
