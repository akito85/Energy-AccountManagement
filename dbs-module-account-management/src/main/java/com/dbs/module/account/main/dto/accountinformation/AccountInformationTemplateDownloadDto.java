package com.dbs.module.account.main.dto.accountinformation;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class AccountInformationTemplateDownloadDto {
    private int no;
    private String customerNumber;
    private String customerName;
    private String customerType;
    private String accountNumber;
    private String registrationNumber;
    private String accountName;
    private String sor;
    private String costCenter;
    private String meterReadingCode;
    private String accountSegment;
    private String accountGroupType;
    private String accountCategory;
    private String classificationType;
    private String accountType;
    private String industrialSector;
    private String budgetYear;
    private String budget;
    private String teritory;
    private String accountGroup;
    private String priority;
    private String isCorporate;
    private String isException;
    private String customerManagement;
    private String accountStatus;
    private String accountDescription;
}
