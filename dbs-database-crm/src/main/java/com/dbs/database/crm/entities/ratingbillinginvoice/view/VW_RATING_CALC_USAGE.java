package com.dbs.database.crm.entities.ratingbillinginvoice.view;

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
@Table(name = "VW_RATING_CALC_USAGE")
public class VW_RATING_CALC_USAGE extends DefaultBaseEntities implements Serializable {
    @Id
    @Column(name ="ID_CALCULATION_USAGE")
    private Integer idCalcUsage;
    @Column(name ="RATING_CODE")
    private String ratingCode;
    @Column(name ="TYPE")
    private String type;
    @Column(name ="UOM")
    private String uom;
    @Column(name ="USAGE")
    private String usage;
    @Column(name ="CONV_USAGE_M3")
    private String convUsageM3;
    @Column(name ="CONV_USAGE_MMBTU")
    private String convUsageMmbtu;
    @Column(name ="DISCOUNT_USAGE")
    private String discountUsage;
    @Column(name ="DISCOUNT_USAGE_M3")
    private String discountUsageM3;
    @Column(name ="DISCOUNT_USAGE_MMBTU")
    private String discountUsageMmbtu;
    @Column(name ="TOTAL_USAGE")
    private String totalUsage;
    @Column(name ="CONV_TOTAL_USAGE_M3")
    private String convTotalUsageM3;
    @Column(name ="CONV_TOTAL_USAGE_MMBTU")
    private String convTotalUsageMmbtu;
    @Column(name ="PRICE_CODE")
    private String priceCode;
    @Column(name ="CURRENCY")
    private String currency;
    @Column(name ="AMOUNT")
    private String amount;
    @Column(name ="AMOUNT_EQV_USD")
    private String amountEqvUsd;
    @Column(name ="AMOUNT_EQV_IDR")
    private String amountEqvIdr;
    @Column(name ="DISCOUNT_AMOUNT")
    private String discountAmount;
    @Column(name ="DISCOUNT_AMOUNT_EQV_USD")
    private String discountAmountEqvUsd;
    @Column(name ="DISCOUNT_AMOUNT_EQV_IDR")
    private String discountAmountEqvIdr;
    @Column(name ="TOTAL_AMOUNT")
    private String totalAmount;
    @Column(name ="TOTAL_AMOUNT_EQV_USD")
    private String totalAmountEqvUsd;
    @Column(name ="TOTAL_AMOUNT_EQV_IDR")
    private String totalAmountEqvIdr;
    @Column(name ="CALCULATION_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date calculationDate;
    @Column(name ="REMARK")
    private String remark;
    @Column(name ="PRICE")
    private String price;
}
