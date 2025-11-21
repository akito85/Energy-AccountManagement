package com.dbs.module.account.detail.serviceagreement.dto;

import com.dbs.module.account.detail.serviceagreement.dto.helper.PriceCodeDetailAdjustmentDTO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@SuppressWarnings("java:S1068")
public class SaPriceRuleDTO implements Serializable {
    private Integer id;
    private Integer saId;
    private String saNumber;
    private Integer lineNumber;
    private Double max;
    private Double min;
    private String isUnlim;
    private Integer idMPricing;
    private String priceCode;
    private BigDecimal value;
    private String uom;
    private Integer uomId;
    private String currency;
    private Integer currencyId;
    private PriceCodeDetailAdjustmentDTO adjustment;
}
