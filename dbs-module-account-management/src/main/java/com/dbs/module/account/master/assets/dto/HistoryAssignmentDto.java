package com.dbs.module.account.master.assets.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class HistoryAssignmentDto {
    private Integer id;
    private String customerNumber;
    private String customerName;
    private String accountNumber;
    private String accountName;
    private String premise;
    private String servicePoint;
    private String remark;
    private String installDate;
    private String uninstallDate;
}
