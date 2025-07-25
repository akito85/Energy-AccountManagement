package com.dbs.module.account.master.latecharge.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class HistoryLogInformationDto {
    private Integer id;
    private String createdDate;
    private String createdBy;
    private String updatedDate;
    private String updatedBy;
}
