package com.dbs.database.crm.entities.ratingbillinginvoice;

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
@Table(name = "M_RBI_RATING_CALC_USAGE")
public class M_RBI_RATING_CALC_USAGE extends DefaultBaseEntities implements Serializable {
    @Id
    @Column(name ="ID_CALCULATION_USAGE")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_RBI_RATING_CALC_USAGE_SEQ")
    @SequenceGenerator(sequenceName = "M_RBI_RATING_CALC_USAGE_SEQ",allocationSize = 1, name = "M_RBI_RATING_CALC_USAGE_SEQ")
    private Integer idCalcUsage;
    @Column(name ="RATING_CODE")
    private String ratingCode;
    @Column(name ="TYPE")
    private String type;
    @Column(name ="UOM")
    private String uom;
    @Column(name ="USAGE")
    private Integer usage;
    @Column(name ="CONV_USAGE_M3")
    private Integer convUsageM3;
    @Column(name ="CONV_USAGE_MMBTU")
    private Integer convUsageMmbtu;
    @Column(name ="DISCOUNT_USAGE")
    private Integer discountUsage;
    @Column(name ="DISCOUNT_USAGE_M3")
    private Integer discountUsageM3;
    @Column(name ="DISCOUNT_USAGE_MMBTU")
    private Integer discountUsageMmbtu;
    @Column(name ="TOTAL_USAGE")
    private Integer totalUsage;
    @Column(name ="CONV_TOTAL_USAGE_M3")
    private Integer convTotalUsageM3;
    @Column(name ="CONV_TOTAL_USAGE_MMBTU")
    private Integer convTotalUsageMmbtu;
    @Column(name ="PRICE_CODE")
    private String priceCode;
    @Column(name ="CURRENCY")
    private String currency;
    @Column(name ="AMOUNT")
    private BigDecimal amount;
    @Column(name ="AMOUNT_EQV_USD")
    private BigDecimal amountEqvUsd;
    @Column(name ="AMOUNT_EQV_IDR")
    private BigDecimal amountEqvIdr;
    @Column(name ="DISCOUNT_AMOUNT")
    private BigDecimal discountAmount;
    @Column(name ="DISCOUNT_AMOUNT_EQV_USD")
    private BigDecimal discountAmountEqvUsd;
    @Column(name ="DISCOUNT_AMOUNT_EQV_IDR")
    private BigDecimal discountAmountEqvIdr;
    @Column(name ="TOTAL_AMOUNT")
    private BigDecimal totalAmount;
    @Column(name ="TOTAL_AMOUNT_EQV_USD")
    private BigDecimal totalAmountEqvUsd;
    @Column(name ="TOTAL_AMOUNT_EQV_IDR")
    private BigDecimal totalAmountEqvIdr;
    @Column(name ="CALCULATION_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date calculationDate;
    @Column(name ="REMARK")
    private String remark;
    @Column(name ="PRICE")
    private BigDecimal price;
}
