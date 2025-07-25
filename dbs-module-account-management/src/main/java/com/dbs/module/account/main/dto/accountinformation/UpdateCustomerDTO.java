package com.dbs.module.account.main.dto.accountinformation;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@SuppressWarnings("java:S1068") 
public class UpdateCustomerDTO {
    private Integer customerId;

    @NotNull(message = "Customer type not be null")
    private Integer customerType;

    @NotNull(message = "Identification type not be null")
    private Integer identificationType;

    @NotEmpty(message = "Customer identification number not be empty")
    private String customerIdentificationNumber;

    private String firstName;

    private String middleName;
    private String lastName;
    private String customerName;
    private String foundedBirthDate;
    private String foundedBirthPlace;
    private Integer sex;
    private Integer maritalStatus;
    private String searchKey;
    private String description;
}
