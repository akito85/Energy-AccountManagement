package com.dbs.module.account.master.meterreadingcodes.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class ActiveInactiveDTO {
    
    private Integer meterReadingCodeId;
    
    private String endDate;
    
    private String remark;
}
