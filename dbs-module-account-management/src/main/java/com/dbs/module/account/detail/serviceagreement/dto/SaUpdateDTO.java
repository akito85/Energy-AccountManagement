package com.dbs.module.account.detail.serviceagreement.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class SaUpdateDTO implements Serializable {
    // Data
    private Integer saId;
    private String description;
    private Date endDate;
    private Integer appHierId;
}
