package com.dbs.module.account.detail.serviceagreement.dto.helper;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class ValidationCreateSaDTO implements Serializable {
    private Integer saId;
    private Boolean isMain;
    private Integer accountId;
    private Date startDate;
    private Date endDate;
    private Integer productId;
    private String saType;
    private String saReferenceNumber;
}
