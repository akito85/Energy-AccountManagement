package com.dbs.module.account.detail.serviceagreement.dto.helper;

import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class PricingRuleDTO implements Serializable {
    private String name;
    private Integer pricingRuleId;
}
