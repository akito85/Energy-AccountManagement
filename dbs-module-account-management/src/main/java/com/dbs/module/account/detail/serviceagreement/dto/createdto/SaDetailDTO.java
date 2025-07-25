package com.dbs.module.account.detail.serviceagreement.dto.createdto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class SaDetailDTO implements Serializable {
    private String isCustom;
    private Integer productVersionId;
    private List<ListSaDetailDTO> productDetail;
    private SaProductPricingDTO productPricing;
    private List<ListSaDetailDTO> productCalcRule;
    private List<SaProductTosCreateDTO> productTOS;
    private Integer lateChargeIDR;
    private Integer lateChargeUSD;
    private Integer taxImplicationPPN;
    private Integer taxImplicationPPh;
}
