package com.dbs.database.crm.entities.summary;

import com.dbs.common.base.entities.DefaultBaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_SUMMARY_AGREEMENT")
public class VW_SUMMARY_AGREEMENT extends DefaultBaseEntities {
    @Id
    @Column(name = "SEQUENCE")
    private Integer sequence;
    @Column(name = "COST_CENTER_ID")
    private Integer costCenterId;
    @Column(name = "CUSTOMER_NUMBER")
    private String customerNumber;
    @Column(name = "CUSTOMER_NAME")
    private String customerName;
    @Column(name = "SOR")
    private String sor;
    @Column(name = "COST_CENTER")
    private String costCenter;
    @Column(name = "ACCOUNT_NAME")
    private String accountName;
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;
    @Column(name = "ACCOUNT_ID_SA")
    private Integer accountIdSa;
    @Column(name = "PRICE_CODE")
    private String priceCode;
    @Column(name = "PRICING_RULE")
    private String pricingRule;
    @Column(name = "PRICE_ADJUSTMENT_IDR")
    private String priceAdjustmentIdr;
    @Column(name = "PRICE_ADJUSTMENT_USD")
    private String priceAdjustmentUsd;
    @Column(name = "SA_NUMBER")
    private String saNumber;
    @Column(name = "SA_REFERENCE_NUMBER")
    private String saReferenceNumber;
    @Column(name = "SERVICE_TYPE")
    private String serviceType;
    @Column(name = "SA_TYPE")
    private String saType;
    @Column(name = "PJBG_TYPE")
    private String pjbgType;
    @Column(name = "SA_DATE")
    private Date saDate;
    @Column(name = "START_DATE_SA")
    private Date startDateSa;
    @Column(name = "END_DATE_SA")
    private Date endDateSa;
    @Column(name = "COMITMENT_DATE")
    private Date comitmentDate;
    @Column(name = "BILLING_CYCLE")
    private String billingCycle;
    @Column(name = "TERMS_OF_PAYMENT_NAME")
    private String termsOfPaymentName;
    @Column(name = "INVOICE_TEMPLATE")
    private String invoiceTemplate;
    @Column(name = "GAS_IN_PLAN_DATE")
    private Date gasInPlanDate;
    @Column(name = "ALREADY_GAS_IN")
    private String alreadyGasIn;
    @Column(name = "PRODUCT")
    private String product;
    @Column(name = "PRODUCT_TYPE")
    private String productType;
    @Column(name = "PRODUCT_CLASS")
    private String productClass;
    @Column(name = "PRODUCT_VERSION_ID")
    private Integer productVersionId;
    @Column(name = "CREATE_FROM")
    private String createFrom;
    @Column(name = "IS_MAIN")
    private String isMain;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CALCULATION_TYPE")
    private String calculationType;
    @Column(name = "PAYMENT_TYPE")
    private String paymentType;
    @Column(name = "CHARGING_METHOD")
    private String chargingMethod;
    @Column(name = "MIN")
    private Integer min;
    @Column(name = "MAX")
    private Integer max;
    @Column(name = "UOM")
    private String uom;
    @Column(name = "PRESSURE")
    private String pressure;
    @Column(name = "CONTRACT_PRESSURE")
    private String contractPressure;
    @Column(name = "TIME_UNIT")
    private String timeUnit;
}
