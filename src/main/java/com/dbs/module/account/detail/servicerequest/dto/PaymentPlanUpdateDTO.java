package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * DTO for updating a payment plan
 */
@Data
public class PaymentPlanUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Payment plan ID cannot be null")
    private Integer paymentPlanId;

    private String planName;

    private String description;

    private String termsAndConditions;

    private Boolean autoDebit;

    private Integer paymentMethod;
}
