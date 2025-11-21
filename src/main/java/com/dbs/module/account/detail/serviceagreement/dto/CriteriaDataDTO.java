package com.dbs.module.account.detail.serviceagreement.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class CriteriaDataDTO implements Serializable {
    
    private Integer accountId;
    private Integer serviceType;
    private Integer productVersionId;
    private List<Integer> priceCode;
    
}

