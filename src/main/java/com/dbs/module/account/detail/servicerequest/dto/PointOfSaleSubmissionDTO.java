package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for POS submission including products and installment
 */
@Data
public class PointOfSaleSubmissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String posNumber;
    private Integer posType;
    private String posLocation;

    // Products with promo eligibility
    private List<ProductSelectionDTO> products;

    // Installment from billing
    private InstallmentSelectionDTO installment;

    // Additional POS data
    private String salesPersonName;
    private String notes;
}
