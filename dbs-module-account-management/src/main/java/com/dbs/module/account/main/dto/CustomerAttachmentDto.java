package com.dbs.module.account.main.dto;

import lombok.Data;

import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class CustomerAttachmentDto {
    private Integer id;
    private String category;
    private String fileName;
    private String uploadBy;
    private Date uploadDate;
    private long fileSize;
}
