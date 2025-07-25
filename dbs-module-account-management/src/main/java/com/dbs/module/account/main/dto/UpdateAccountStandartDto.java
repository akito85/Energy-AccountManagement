package com.dbs.module.account.main.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class UpdateAccountStandartDto {
    private String accountNumber;
    private Integer accountGroup;
    private Integer accountGroupType;
    private String accountName;
    private String accountRegistrationNumber;
    private Integer accountType;
    private Integer budget;
    private Integer budgetYear;
    private Integer category;
    private Integer classificationType;
    private Boolean corporateCustomer;
    private Integer costCenter;
    private String description;
    private Integer industrialSector;
    private String meterReadingCodes;
    private Integer priority;
    private Boolean ratingAndBillingException;
    private Integer segment;
    private Integer sor;
    private Integer teritory;
}
