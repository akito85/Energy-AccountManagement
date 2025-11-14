package com.dbs.module.account.detail.address.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class AccountAddressUpdateDTO {
    private Integer addressId; //accountAddressId
    private String description;
    private String descriptionAddress;
    private Boolean primaryFlag;
    private Boolean needValidation;
}
