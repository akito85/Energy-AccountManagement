package com.dbs.module.account.master.address.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class HistoryLogInformationResponseDto {
    private Integer id;
    private String createdDate;
    private String createdBy;
    private String updatedDate;
    private String updatedBy;
}
