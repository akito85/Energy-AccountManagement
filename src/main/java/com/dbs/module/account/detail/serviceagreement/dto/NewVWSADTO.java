package com.dbs.module.account.detail.serviceagreement.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class NewVWSADTO implements Serializable{
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
    private Date gasInPlanDate;
    private String idrFullPriceCode;
    private String usdFullPriceCode;
    private String idrUom;
    private String idrUomId;
    private Integer idrValue;
    private String usdUom;
    private String usdUomId;
    private Integer usdValue;
    private String priceCode;
    private Integer idMPricing;
    private String productName;
    private String productType;
    private Integer productTypeId;
    private String serviceType;
    private Integer serviceTypeId;
    private String productClass;
    private Integer productClassId;
    private String productVersion;
    private String description;
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
    private String pricingRule;
    private String status;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName();
        }
    }
}

