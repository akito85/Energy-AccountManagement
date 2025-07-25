package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_SA")
public class VW_SA extends DefaultBaseEntities implements Serializable {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ACCOUNT_ID")
    private Integer accountId;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "SA_NUMBER")
    private String saNumber;

    @Column(name = "SA_REFERENCE_NUMBER")
    private String saReferenceNumber;

    @Column(name = "SA_REFERENCE_ID")
    private Integer saReferenceId;

    @Column(name = "SA_DATE")
    private Date saDate;

    @Column(name = "SA_SERVICE_TYPE")
    private String saServiceType;

    @Column(name = "SA_SERVICE_TYPE_ID")
    private Integer saServiceTypeId;

    @Column(name = "SA_TYPE")
    private String saType;

    @Column(name = "SA_TYPE_ID")
    private Integer saTypeId;

    @Column(name = "PJBG_TYPE")
    private String pjbgType;

    @Column(name = "PJBG_TYPE_ID")
    private Integer pjbgTypeId;

    @Column(name = "BC_BEGIN_CYCLE")
    private Integer bcBeginCycle;

    @Column(name = "BC_END_CYCLE")
    private Integer bcEndCycle;

    @Column(name = "BILLING_CYCLE")
    private String billingCycle;

    @Column(name = "BILLING_CYCLE_ID")
    private Integer billingCycleId;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "COMITMENT_DATE")
    private Date comitmentDate;

    @Column(name = "ALREADY_GAS_IN")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean alreadyGasIn;

    @Column(name = "GAS_IN_PLAN_DATE")
    private Date gasInPlanDate;

    @Column(name = "FULL_PRICE_CODE")
    private String fullPriceCode;

    @Column(name = "IDR_FULL_PRICE_CODE")
    private String idrFullPriceCode;

    @Column(name = "USD_FULL_PRICE_CODE")
    private String usdFullPriceCode;

    @Column(name = "IDR_UOM")
    private String idrUom;

    @Column(name = "IDR_UOM_ID")
    private String idrUomId;

    @Column(name = "IDR_VALUE")
    private Integer idrValue;

    @Column(name = "USD_UOM")
    private String usdUom;

    @Column(name = "USD_UOM_ID")
    private String usdUomId;

    @Column(name = "USD_VALUE")
    private Integer usdValue;

    @Column(name = "PRICE_CODE")
    private String priceCode;

    @Column(name = "ID_M_PRICING")
    private Integer idMPricing;

    @Column(name = "IS_CUSTOM")
    private String isCustom;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRODUCT_ID")
    private Integer productId;

    @Column(name = "PRODUCT_TYPE")
    private String productType;

    @Column(name = "PRODUCT_TYPE_ID")
    private Integer productTypeId;

    @Column(name = "SERVICE_TYPE")
    private String serviceType;

    @Column(name = "SERVICE_TYPE_ID")
    private Integer serviceTypeId;

    @Column(name = "PRODUCT_CLASS")
    private String productClass;

    @Column(name = "PRODUCT_CLASS_ID")
    private Integer productClassId;

    @Column(name = "VERSION")
    private String productVersion;

    @Column(name = "PRODUCT_DESCRIPTION")
    private String productDescription;

    @Column(name = "PRODUCT_VERSION_ID")
    private Integer productVersionId;

    @Column(name = "INVOICE_TEMPLATE")
    private String invoiceTemplate;

    @Column(name = "INVOICE_TEMPLATE_ID")
    private Integer invoiceTemplateId;

    @Column(name = "TERMS_OF_PAYMENT_NAME")
    private String termsOfPaymentName;

    @Column(name = "TERM_OF_PAYMENT_ID")
    private Integer termOfPaymentId;

    @Column(name = "IDR_LATE_CHARGE")
    private String idrLateCharge;

    @Column(name = "IDR_LATE_CHARGE_ID")
    private Integer idrLateChargeId;

    @Column(name = "USD_LATE_CHARGE")
    private String usdLateCharge;

    @Column(name = "USD_LATE_CHARGE_ID")
    private Integer usdLateChargeId;

    @Column(name = "PPN_TAX_IMP")
    private String ppnTaxImp;

    @Column(name = "PPN_TAX_IMP_ID")
    private Integer ppnTaxImpId;

    @Column(name = "PPH_TAX_IMP")
    private String pphTaxImp;

    @Column(name = "PPH_TAX_IMP_ID")
    private Integer pphTaxImpId;

    @Column(name = "IS_MAIN")
    private String isMain;

    @Column(name = "IS_PRICING_RULE")
    private String isPricingRule;

    @Column(name = "PRICING_RULE_NAME")
    private String pricingRuleName;

    @Column(name = "PRICING_RULE_ID")
    private Integer pricingRuleId;

    @Column(name = "APPHIER_ID")
    private Integer appHierId;

    @Column(name = "APPROVAL_STATUS")
    private String approvalStatus;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ADJUSTMENT_IDR_ID")
    private Integer adjustmentIdrId;

    @Column(name = "ADJUSTMENT_IDR_VALUE")
    private Integer adjustmentIdrValue;

    @Column(name = "ADJUSTMENT_TYPE_IDR")
    private String adjustmentTypeIdr;

    @Column(name = "FINAL_ADJUSTMENT_IDR_VALUE")
    private Integer finalAdjustmentIdrValue;

    @Column(name = "ADJUSTMENT_IDR_FULL_PRICE_CODE")
    private String adjustmentIdrFullPriceCode;

    @Column(name = "ADJUSTMENT_USD_ID")
    private Integer adjustmentUsdId;

    @Column(name = "ADJUSTMENT_USD_VALUE")
    private Integer adjustmentUsdValue;

    @Column(name = "ADJUSTMENT_TYPE_USD")
    private String adjustmentTypeUsd;

    @Column(name = "FINAL_ADJUSTMENT_USD_VALUE")
    private Integer finalAdjustmentUsdValue;

    @Column(name = "ADJUSTMENT_USD_FULL_PRICE_CODE")
    private String adjustmentUsdFullPriceCode;

    @Column(name = "MAIN_START_DATE")
    private Date saMainStartDate;

    @Column(name = "MAIN_END_DATE")
    private Date saMainEndDate;

    @Column(name = "JSON_DATA")
    private String jsonData;

}
