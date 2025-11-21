package com.dbs.module.account.master.gassource.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class ActiveInactiveDTO {

    private Integer id;

    private Integer gasSourceId;

    private String remark;

    private Integer gasSourceDetailId;

    private String endDate;
}
