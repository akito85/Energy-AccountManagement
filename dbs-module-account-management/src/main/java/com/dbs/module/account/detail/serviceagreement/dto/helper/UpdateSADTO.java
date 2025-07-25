package com.dbs.module.account.detail.serviceagreement.dto.helper;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class UpdateSADTO {
    private Boolean isDraft;
    private Integer saId;
    private String description;
    private String endDate;
    private Integer appHierId;
}
