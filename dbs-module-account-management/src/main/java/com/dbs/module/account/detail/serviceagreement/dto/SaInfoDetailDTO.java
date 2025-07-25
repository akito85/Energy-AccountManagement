package com.dbs.module.account.detail.serviceagreement.dto;

import com.dbs.module.account.detail.serviceagreement.dto.helper.PriceCodeDetailAdjustmentDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class SaInfoDetailDTO implements Serializable {
    private Integer id;
    private Integer accountId;
    private String accountNumber;
    private String saNumber;
    private String saReferenceNumber;
    private Integer saReferenceId;
    private Date saDate;
    private String saServiceType;
    private Integer saServiceTypeId;
    private String saType;
    private Integer saTypeId;
    private String pjbgType;
    private Integer pjbgTypeId;
    private Integer bcBeginCycle;
    private Integer bcEndCycle;
    private String billingCycle;
    private Integer billingCycleId;
    private Date startDate;
    private Date endDate;
    private Date comitmentDate;
    private Boolean alreadyGasIn;
    private Date gasInPlanDate;
    private String fullPriceCode;
    private String idrFullPriceCode;
    private PriceCodeDetailAdjustmentDTO idrAdjustment;
    private String usdFullPriceCode;
    private PriceCodeDetailAdjustmentDTO usdAdjustment;
    private String idrUom;
    private String idrUomId;
    private Integer idrValue;
    private String usdUom;
    private String usdUomId;
    private Integer usdValue;
    private String priceCode;
    private Integer idMPricing;
    private String isCustom;
    private String productName;
    private Integer productId;
    private String productType;
    private Integer productTypeId;
    private String serviceType;
    private Integer serviceTypeId;
    private String productClass;
    private Integer productClassId;
    private String productVersion;
    private String productDescription;
    private Integer productVersionId;
    private String invoiceTemplate;
    private Integer invoiceTemplateId;
    private String termsOfPaymentName;
    private Integer termOfPaymentId;
    private String idrLateCharge;
    private Integer idrLateChargeId;
    private String usdLateCharge;
    private Integer usdLateChargeId;
    private String ppnTaxImp;
    private Integer ppnTaxImpId;
    private String pphTaxImp;
    private Integer pphTaxImpId;
    private String isMain;
    private String isPricingRule;
    private String pricingRuleName;
    private Integer pricingRuleId;
    private Integer appHierId;
    private String status;
    private String description;

    private Date saMainStartDate;
    private Date saMainEndDate;
}
