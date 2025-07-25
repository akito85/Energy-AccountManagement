package com.dbs.module.account.detail.premise.dto;

import lombok.Data;

import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class InactiveAssetAssignmentDTO {
    private Integer assetAssignmentId;
    private Date uninstallDate;
    private String remarks;
}
