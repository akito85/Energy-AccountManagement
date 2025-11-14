package com.dbs.module.account.main.dto;

import lombok.Data;

import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class VwCustomerDetailDTO {
    private Integer customerId;
    private String customerNumber;
    private String customerType;
    private String firstName;
    private String middleName;
    private String lastName;
    private String customerName;
    private String personalIdentificationNumber;
    private String identificationType;
    private String sex;
    private Date dateOfBirth;
    private String placeOfBirth;
    private String maritalStatus;
    private String industrialSector;
    private String status;
    private String searchKey;
    private Integer position;
    private String description;
}
