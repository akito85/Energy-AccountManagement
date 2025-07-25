package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Table(name = "M_RBI_RATING")
public class M_RBI_RATING extends DefaultBaseEntities implements Serializable {
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
    @Column(name ="ACCOUNT_SEGMENT")
    private String accountSegment;
    @Column(name ="METER_READING_CODE")
    private String mReadingCode;
    @Column(name ="BILLING_CYCLE")
    private String billingCycle;
    @Column(name ="BILLING_PERIOD")
    @JsonFormat(pattern = "yyyy-MM", timezone = Constant.TIMEZONE)
    private Date billingPeriod;
    @Column(name ="SA_NUMBER")
    private String saNumber;
    @Column(name ="PRODUCT")
    private String product;
    @Column(name ="UOM")
    private String uom;
    @Column(name ="MIN_CONTRACT")
    private Integer minContract;
    @Column(name ="MAX_CONTRACT")
    private Integer maxContract;
    @Column(name ="TIME_UNIT_CONTRACT")
    private String timeUnitContract;
    @Column(name ="USAGE")
    private Integer usage;
    @Column(name ="CONV_USAGE_M3")
    private Integer convUsageM3;
    @Column(name ="CONV_USAGE_MMBTU")
    private Integer convUsageMmbtu;
    @Column(name ="DISCOUNT_USAGE")
    private String discountUsage;
    @Column(name ="TOTAL_USAGE")
    private Integer totalUsage;
    @Column(name ="CONV_TOTAL_USAGE_M3")
    private Integer convTotalUsageM3;
    @Column(name ="CONV_TOTAL_USAGE_MMBTU")
    private Integer convTotalUsageMmbtu;
    @Column(name ="MINIMUM_USAGE")
    private Integer minimumUsage;
    @Column(name ="CONV_MINIMUM_USAGE_M3")
    private Integer convMinimumUsageM3;
    @Column(name ="CONV_MINIMUM_USAGE_MMBTU")
    private Integer convMinimumUsageMmbtu;
    @Column(name ="NORMAL_USAGE")
    private Integer normalUsage;
    @Column(name ="CONV_NORMAL_USAGE_M3")
    private Integer convNormalUsageM3;
    @Column(name ="CONV_NORMAL_USAGE_MMBTU")
    private Integer convNormalUsageMmbtu;
    @Column(name ="OUP")
    private Integer oup;
    @Column(name ="CONV_OUP_M3")
    private Integer convOupM3;
    @Column(name ="CONV_OUP_MMBTU")
    private Integer convOupMmbtu;
    @Column(name ="CALCULATED_USAGE")
    private Integer calculatedUsage;
    @Column(name ="CONV_CALCULATED_USAGE_M3")
    private Integer convCalculatedUsageM3;
    @Column(name ="CONV_CALCULATED_USAGE_MMBTU")
    private Integer convCalculatedUsageMmbtu;
    @Column(name ="CURRENCY")
    private String currency;
    @Column(name ="RATE")
    private BigDecimal rate;
    @Column(name ="RATE_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date rateDate;
    @Column(name ="TOTAL_AMOUNT_MIN_USD")
    private BigDecimal totalAmountMinUsd;
    @Column(name ="TOTAL_AMOUNT_MIN_IDR")
    private BigDecimal totalAmountMinIdr;
    @Column(name ="TOTAL_AMOUNT_MIN_EQV_USD")
    private BigDecimal totalAmountMinEqvUsd;
    @Column(name ="TOTAL_AMOUNT_MIN_EQV_IDR")
    private BigDecimal totalAmountMinEqvIdr;
    @Column(name ="TOTAL_AMOUNT_NORMAL_USD")
    private BigDecimal totalAmountNormalUsd;
    @Column(name ="TOTAL_AMOUNT_NORMAL_IDR")
    private BigDecimal totalAmountNormalIdr;
    @Column(name ="TOTAL_AMOUNT_NORMAL_EQV_USD")
    private BigDecimal totalAmountNormalEqvUsd;
    @Column(name ="TOTAL_AMOUNT_NORMAL_EQV_IDR")
    private BigDecimal totalAmountNormalEqvIdr;
    @Column(name ="TOTAL_AMOUNT_OUP_USD")
    private BigDecimal totalAmountOupUsd;
    @Column(name ="TOTAL_AMOUNT_OUP_IDR")
    private BigDecimal totalAmountOupIdr;
    @Column(name ="TOTAL_AMOUNT_OUP_EQV_USD")
    private BigDecimal totalAmountOupEqvUsd;
    @Column(name ="TOTAL_AMOUNT_OUP_EQV_IDR")
    private BigDecimal totalAmountOupEqvIdr;
    @Column(name ="AMOUNT_USD")
    private BigDecimal amountUsd;
    @Column(name ="AMOUNT_IDR")
    private BigDecimal amountIdr;
    @Column(name ="AMOUNT_EQV_USD")
    private BigDecimal amountEqvUsd;
    @Column(name ="AMOUNT_EQV_IDR")
    private BigDecimal amountEqvIdr;
    @Column(name ="DISCOUNT_AMOUNT")
    private BigDecimal discountAmount;
    @Column(name ="TOTAL_AMOUNT_USD")
    private BigDecimal totalAmountUsd;
    @Column(name ="TOTAL_AMOUNT_IDR")
    private BigDecimal totalAmountIdr;
    @Column(name ="TOTAL_AMOUNT_EQV_USD")
    private BigDecimal totalAmountEqvUsd;
    @Column(name ="TOTAL_AMOUNT_EQV_IDR")
    private BigDecimal totalAmountEqvIdr;
    @Column(name ="TRANSACTION_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date transactionDate;
    @Column(name ="ACCOUNTING_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date accountingDate;
    @Column(name = "COST_CENTER_ID")
    private Integer ccId;
    @Column(name = "READING_DATE")
    private Date readingDate;
}
