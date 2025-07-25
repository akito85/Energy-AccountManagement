package com.dbs.module.account.main.dto;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.Convert;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@SuppressWarnings("java:S1068")
public class AccountDTO {
    
    private Integer accountId;

    private Integer customerId;

    private String registrationNumber;

    private String accountNumber;

    @NotEmpty(message="Account Name cannot be empty!")
    private String accountName;

    @NotNull(message="Account Segment cannot be null!")
    private Integer accountSegment;

    @NotNull(message="Account Group Type cannot be null!")
    private Integer accountGroupType;

    @NotNull(message="Category cannot be null!")
    private Integer accountCategory;

    @NotNull(message="Classification Type cannot be null!")
    private Integer accountRuleId;

    @NotNull(message="Account Type cannot be null!")
    private Integer accountType;

    private Integer paymentChannel;

    private String description;

    @NotNull(message="SOR cannot be null!")
    private Integer sor;

    @NotNull(message="Cost Center cannot be null!")
    private Integer costCenter;

    @NotNull(message="Meter Reading Code cannot be null!")
    private Integer meterReadingCode; // Kode Buku (001, 002, dll)

    private Integer budgetYear;

    private Integer budget;

    private Integer teritory;

    private String taxIdentificationNumber;

    private String primaryTaxIdentificationFlag;

    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean corporateFlag;

    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isBadDebt;

    private String syncFlag;

    private String referenceCustomerId;

    private Integer entityId;

    private Integer industrialSector;

    private Integer priority;

    @NotNull(message="Account Group cannot be null!")
    private Integer accountGroup;

    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean exceptionFlag;

    @NotNull(message="Customer Management cannot be null!")
    private Integer customerManagement;
}
