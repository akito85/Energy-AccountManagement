package com.dbs.module.account.detail.serviceagreement.dto.helper;

import com.dbs.common.base.utils.BigDecimal2JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@SuppressWarnings("java:S1068")
public class PriceCodeDetailAdjustmentDTO implements Serializable {
    private Integer priceAdjustmentDetailId;
    private String adjustmentName;
    private String adjustmentType;

    @JsonDeserialize(using = BigDecimal2JsonDeserializer.class)
    private BigDecimal adjustmentValue;

    @JsonDeserialize(using = BigDecimal2JsonDeserializer.class)
    private BigDecimal finalAdjustmentValue;

    private String adjustmentText;
}
