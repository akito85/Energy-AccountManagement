package com.dbs.module.account.master.taximplication.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class HistoryLogTaxImplicationDto {
    private Integer id;
    private String createdDate;
    private String createdBy;
    private String updatedBy;
    private String updatedDate;
}
