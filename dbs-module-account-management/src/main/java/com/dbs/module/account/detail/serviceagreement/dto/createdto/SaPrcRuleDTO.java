package com.dbs.module.account.detail.serviceagreement.dto.createdto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class SaPrcRuleDTO implements Serializable {
    private Integer line;
    private Double min;
    private Double max;
    private Boolean isUnlimited;
    private Integer priceId;
    private List<Integer> priceAdjustment;
}
