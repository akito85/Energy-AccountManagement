package com.dbs.module.account.main.dto;

import lombok.Data;


@Data
@SuppressWarnings("java:S1068")
public class CustomerExistRequestDTO {
    private Integer customerId;
    private String customerNumber;
    private Integer customerType;
    private Integer identificationType;
    private String identificationNumber;
    private String accountGroup;
}
