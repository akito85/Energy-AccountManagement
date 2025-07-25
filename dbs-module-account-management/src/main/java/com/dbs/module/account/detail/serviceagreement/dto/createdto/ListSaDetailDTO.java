package com.dbs.module.account.detail.serviceagreement.dto.createdto;

import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class ListSaDetailDTO implements Serializable {
    private Integer name;
    private String unit;
    private Integer value;
    private String description;
}
