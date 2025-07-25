package com.dbs.module.account.master.meterreadingcodes.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@SuppressWarnings("java:S1068") 
public class UpdateRequestDTO {
    @NotNull(message = "Meter Reading Code Id cannot be null")
    private Integer meterReadingCodeId;

    private Integer costCenterId;

    @NotEmpty(message = "Code cannot be empty")
    private String code;
    
    private String description;
}
