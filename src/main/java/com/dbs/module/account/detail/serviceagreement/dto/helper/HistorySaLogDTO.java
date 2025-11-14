package com.dbs.module.account.detail.serviceagreement.dto.helper;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class HistorySaLogDTO implements Serializable {
    private Integer saId;
    private Date createdDate;
    private String createdBy;
    private Date updateDate;
    private String updatedBy;
    private String approvalStatus;
    private String description;
}
