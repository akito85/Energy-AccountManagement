package com.dbs.module.account.detail.distributionmedia.dto;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class DismeViewDTO extends BaseEntities implements Serializable {
    
    private Integer distributionMediaId;
    private Integer accountId;
    private Integer media;
    private Date startDate;
    private Date endDate;
    private String source;
    private String status;
    
    public DismeViewDTO(){
        super();
    }
}
