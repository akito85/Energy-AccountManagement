package com.dbs.module.account.main.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class MCustomerDTO implements Serializable {
    private Integer customerId;

    private String customerNumber;

    @NotNull(message="Customer Type cannot be null!")
    private Integer customerType;

    private String firstName;

    private String middleName;

    private String lastName;

    @NotEmpty(message="Customer Name cannot be empty!")
    private String customerName;

    @NotEmpty(message="Customer Identification Number cannot be empty!")
    private String customerIdentificationNumber;

    @NotNull(message="Identification Type cannot be null!")
    private Integer identificationType;

    private Integer positionId;

    private Integer sex;

    private String foundedBirthPlace;

    private String foundedBirthDate;

    private Integer maritalStatus;

    private String searchKey;

    private String description;
    
    private String foundedBirthDate2;
}
