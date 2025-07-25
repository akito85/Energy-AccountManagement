package com.dbs.module.account.detail.serviceagreement.dto.helper;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class PricingRuleDetailDTO implements Serializable {
    private String createdBy;
    private Date createdDate;
    private String currency;
    private String description;
    private String isDeleted;
    private Integer lineNumber;
    private Integer max;
    private BigDecimal min;
    private String priceCode;
    private Integer priceCodeId;
    private Integer pricingRuleDetailId;
    private Integer pricingRuleId;
    private String uom;
    private Boolean isUnlim;
    private String updatedBy;
    private Date updatedDate;
    private String value;
    private PriceCodeDetailAdjustmentDTO adjustment;
}
