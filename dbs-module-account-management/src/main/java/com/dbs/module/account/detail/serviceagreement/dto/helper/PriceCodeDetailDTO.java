package com.dbs.module.account.detail.serviceagreement.dto.helper;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@SuppressWarnings("java:S1068")
public class PriceCodeDetailDTO implements Serializable {
    private String currencyId;
    private String currency;
    private String description;
    private Integer id;
    private Integer idPricing;
    private String uom;
    private String uomName;
    private BigDecimal value;
    private PriceCodeDetailAdjustmentDTO adjustment;
}
