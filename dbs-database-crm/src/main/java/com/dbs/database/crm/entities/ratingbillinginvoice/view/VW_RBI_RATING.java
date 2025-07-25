package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "VW_RBI_RATING")
public class VW_RBI_RATING extends DefaultBaseEntities implements Serializable {
    @Id
    @Column(name ="RATING_CODE")
    private String ratingCode;
    @Column(name ="CALCULATION_CODE")
    private String calculationCode;
    @Column(name ="CUSTOMER_NUMBER")
    private String customerNumber;
    @Column(name ="CUSTOMER_NAME")
    private String customerName;
    @Column(name ="ACCOUNT_NUMBER")
    private String accountNumber;
    @Column(name ="ACCOUNT_NAME")
    private String accountName;
    @Column(name ="ACCOUNT_GROUP_TYPE")
    private String accGroupType;
    @Column(name ="SERVICE_TYPE")
    private String serviceType;
    @Column(name ="SOR")
    private String sor;
    @Column(name ="COST_CENTER")
    private String costCenter;
    @Column(name = "COST_CENTER_ID")
    private Integer ccId;
    @Column(name ="ACCOUNT_SEGMENT")
    private String accountSegment;
    @Column(name ="METER_READING_CODE")
    private String mReadingCode;
    @Column(name ="BILLING_CYCLE")
    private String billingCycle;
    @Column(name ="BILLING_PERIOD")
    private String billingPeriod;
//    @Column(name = "BILLING_PERIOD_ID")
//    private Integer billingPeriodId;
    @Column(name ="SA_NUMBER")
    private String saNumber;
    @Column(name ="PRODUCT")
    private String product;
    @Column(name ="UOM")
    private String uom;
    @Column(name ="MIN_CONTRACT")
    private String minContract;
    @Column(name ="MIN_CONTRACT_REAL")
    private Double minContractReal;
    @Column(name ="MAX_CONTRACT")
    private String maxContract;
    @Column(name ="MAX_CONTRACT_REAL")
    private Double maxContractReal;
    @Column(name ="TIME_UNIT_CONTRACT")
    private String timeUnitContract;
    @Column(name ="USAGE")
    private String usage;
    @Column(name ="USAGE_REAL")
    private Double usageReal;
    @Column(name ="CONV_USAGE_M3")
    private String convUsageM3;
    @Column(name ="CONV_USAGE_M3_REAL")
    private Double convUsageM3Real;
    @Column(name ="CONV_USAGE_MMBTU")
    private String convUsageMmbtu;
    @Column(name ="CONV_USAGE_MMBTU_REAL")
    private Double convUsageMmbtuReal;
    @Column(name ="DISCOUNT_USAGE")
    private String discountUsage;
    @Column(name ="TOTAL_USAGE")
    private String totalUsage;
    @Column(name ="TOTAL_USAGE_REAL")
    private Double totalUsageReal;
    @Column(name ="CONV_TOTAL_USAGE_M3")
    private String convTotalUsageM3;
    @Column(name ="CONV_TOTAL_USAGE_M3_REAL")
    private Double convTotalUsageM3Real;
    @Column(name ="CONV_TOTAL_USAGE_MMBTU")
    private String convTotalUsageMmbtu;
    @Column(name ="CONV_TOTAL_USAGE_MMBTU_REAL")
    private Double convTotalUsageMmbtuReal;
    @Column(name ="MINIMUM_USAGE")
    private String minimumUsage;
    @Column(name ="MINIMUM_USAGE_REAL")
    private Double minimumUsageReal;
    @Column(name ="CONV_MINIMUM_USAGE_M3")
    private String convMinimumUsageM3;
    @Column(name ="CONV_MINIMUM_USAGE_M3_REAL")
    private String convMinimumUsageM3Real   ;
    @Column(name ="CONV_MINIMUM_USAGE_MMBTU")
    private String convMinimumUsageMmbtu;
    @Column(name ="NORMAL_USAGE")
    private String normalUsage;
    @Column(name ="NORMAL_USAGE_REAL")
    private Double normalUsageReal;
    @Column(name ="CONV_NORMAL_USAGE_M3")
    private String convNormalUsageM3;
    @Column(name ="CONV_NORMAL_USAGE_M3_REAL")
    private Double convNormalUsageM3Real;
    @Column(name ="CONV_NORMAL_USAGE_MMBTU")
    private String convNormalUsageMmbtu;
    @Column(name ="CONV_NORMAL_USAGE_MMBTU_REAL")
    private Double convNormalUsageMmbtuReal;
    @Column(name ="OUP")
    private String oup;
    @Column(name ="OUP_REAL")
    private Integer oupReal;
    @Column(name ="CONV_OUP_M3")
    private String convOupM3;
    @Column(name ="CONV_OUP_M3_REAL")
    private String convOupM3Real;
    @Column(name ="CONV_OUP_MMBTU")
    private String convOupMmbtu;
    @Column(name ="CONV_OUP_MMBTU_REAL")
    private String convOupMmbtuReal;
    @Column(name ="CALCULATED_USAGE")
    private String calculatedUsage;
    @Column(name ="CALCULATED_USAGE_REAL")
    private Double calculatedUsageReal;
    @Column(name ="CONV_CALCULATED_USAGE_M3")
    private String convCalculatedUsageM3;
    @Column(name ="CONV_CALCULATED_USAGE_M3_REAL")
    private Double convCalculatedUsageM3Real;
    @Column(name ="CONV_CALCULATED_USAGE_MMBTU")
    private String convCalculatedUsageMmbtu;
    @Column(name ="CONV_CALCULATED_USAGE_MMBTU_REAL")
    private String convCalculatedUsageMmbtuReal;
    @Column(name ="CURRENCY")
    private String currency;
    
    @Column(name ="RATE")
    private String rate;
    @Column(name ="RATE_REAL")
    private Double rateReal;
    
    @Column(name ="RATE_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date rateDate;
    
    @Column(name ="TOTAL_AMOUNT_MIN_USD")
    private String totalAmountMinUsd;
    @Column(name ="TOTAL_AMOUNT_MIN_USD_REAL")
    private Double totalAmountMinUsdReal;
    
    @Column(name ="TOTAL_AMOUNT_MIN_IDR")
    private String totalAmountMinIdr;
    @Column(name ="TOTAL_AMOUNT_MIN_IDR_REAL")
    private Double totalAmountMinIdrReal;
    
    @Column(name ="TOTAL_AMOUNT_MIN_EQV_USD")
    private String totalAmountMinEqvUsd;
    @Column(name ="TOTAL_AMOUNT_MIN_EQV_USD_REAL")
    private Double totalAmountMinEqvUsdReal;
    
    @Column(name ="TOTAL_AMOUNT_MIN_EQV_IDR")
    private String totalAmountMinEqvIdr;
    @Column(name ="TOTAL_AMOUNT_MIN_EQV_IDR_REAL")
    private Double totalAmountMinEqvIdrReal;
    
    @Column(name ="TOTAL_AMOUNT_NORMAL_USD")
    private String totalAmountNormalUsd;
    @Column(name ="TOTAL_AMOUNT_NORMAL_USD_REAL")
    private Double totalAmountNormalUsdReal;
    
    @Column(name ="TOTAL_AMOUNT_NORMAL_IDR")
    private String totalAmountNormalIdr;
     @Column(name ="TOTAL_AMOUNT_NORMAL_IDR_REAL")
    private Double totalAmountNormalIdrReal;
    
    @Column(name ="TOTAL_AMOUNT_NORMAL_EQV_USD")
    private String totalAmountNormalEqvUsd;
    @Column(name ="TOTAL_AMOUNT_NORMAL_EQV_USD_REAL")
    private Double totalAmountNormalEqvUsdReal;
    
    @Column(name ="TOTAL_AMOUNT_NORMAL_EQV_IDR")
    private String totalAmountNormalEqvIdr;
    @Column(name ="TOTAL_AMOUNT_NORMAL_EQV_IDR_REAL")
    private Double totalAmountNormalEqvIdrReal;
    
    @Column(name ="TOTAL_AMOUNT_OUP_USD")
    private String totalAmountOupUsd;
    @Column(name ="TOTAL_AMOUNT_OUP_USD_REAL")
    private Double totalAmountOupUsdReal;
    
    
    @Column(name ="TOTAL_AMOUNT_OUP_IDR")
    private String totalAmountOupIdr;
     @Column(name ="TOTAL_AMOUNT_OUP_IDR_REAL")
    private Double totalAmountOupIdrReal;
    
    @Column(name ="TOTAL_AMOUNT_OUP_EQV_USD")
    private String totalAmountOupEqvUsd;
    @Column(name ="TOTAL_AMOUNT_OUP_EQV_USD_REAL")
    private Double totalAmountOupEqvUsdReal;
    
    @Column(name ="TOTAL_AMOUNT_OUP_EQV_IDR")
    private String totalAmountOupEqvIdr;
    @Column(name ="TOTAL_AMOUNT_OUP_EQV_IDR_REAL")
    private Double totalAmountOupEqvIdrReal;
    
    @Column(name ="AMOUNT_USD")
    private String amountUsd;
    @Column(name ="AMOUNT_USD_REAL")
    private Double amountUsdReal;
    
    @Column(name ="AMOUNT_IDR")
    private String amountIdr;
    @Column(name ="AMOUNT_IDR_REAL")
    private Double amountIdrReal;
    
    @Column(name ="AMOUNT_EQV_USD")
    private String amountEqvUsd;
    @Column(name ="AMOUNT_EQV_USD_REAL")
    private Double amountEqvUsdReal;
    
    @Column(name ="AMOUNT_EQV_IDR")
    private String amountEqvIdr;
    @Column(name ="AMOUNT_EQV_IDR_REAL")
    private Double amountEqvIdrReal;
    
    @Column(name ="DISCOUNT_AMOUNT")
    private String discountAmount;
    @Column(name ="DISCOUNT_AMOUNT_REAL")
    private Double discountAmountReal;
    
    @Column(name ="TOTAL_AMOUNT_USD") 
    private String totalAmountUsd;
    @Column(name ="TOTAL_AMOUNT_USD_REAL")
    private Double totalAmountUsdReal;
    
    @Column(name ="TOTAL_AMOUNT_IDR")
    private String totalAmountIdr;
    @Column(name ="TOTAL_AMOUNT_IDR_REAL")
    private Double totalAmountIdrReal;
    
    @Column(name ="TOTAL_AMOUNT_EQV_USD")
    private String totalAmountEqvUsd;
    @Column(name ="TOTAL_AMOUNT_EQV_USD_REAL")
    private Double totalAmountEqvUsdReal;
    
    @Column(name ="TOTAL_AMOUNT_EQV_IDR")
    private String totalAmountEqvIdr;
    @Column(name ="TOTAL_AMOUNT_EQV_IDR_REAL")
    private Double totalAmountEqvIdrReal;
    
    @Column(name ="TRANSACTION_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date transactionDate;
    @Column(name ="ACCOUNTING_DATE")
//    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private String accountingDate;
    @Column(name = "READING_DATE")
    private Date readingDate;
}
