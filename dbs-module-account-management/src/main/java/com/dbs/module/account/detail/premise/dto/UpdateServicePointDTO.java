package com.dbs.module.account.detail.premise.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068") 
public class UpdateServicePointDTO implements Serializable {
    @NotNull(message = "Service Point Id cannot be null!")
    private Integer servicePointId;

    private String description;
    private String remarks;
}
