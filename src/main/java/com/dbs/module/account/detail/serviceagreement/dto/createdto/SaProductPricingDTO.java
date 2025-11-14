package com.dbs.module.account.detail.serviceagreement.dto.createdto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@SuppressWarnings("java:S1068") 
public class SaProductPricingDTO implements Serializable {
    private Integer priceCodeId;
    private Integer priceRuleId;
    private List<Integer> priceAdjustment;
    private List<SaPrcRuleDTO> priceRuleTiering;
}
