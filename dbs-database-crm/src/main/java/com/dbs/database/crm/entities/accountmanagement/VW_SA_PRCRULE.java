package com.dbs.database.crm.entities.accountmanagement;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "VW_SA_PRCRULE")
public class VW_SA_PRCRULE implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "SA_ID")
    private Integer saId;

    @Column(name = "SA_NUMBER")
    private String saNumber;

    @Column(name = "LINE_NUMBER")
    private Integer lineNumber;

    @Column(name = "MAX")
    private Float max;

    @Column(name = "MIN")
    private Float min;

    @Column(name = "IS_UNLIM")
    private String isUnlim;

    @Column(name = "ID_M_PRICING")
    private Integer idMPricing;

    @Column(name = "PRICE_CODE")
    private String priceCode;

    @Column(name = "VALUE")
    private Integer value;

    @Column(name = "UOM")
    private String uom;

    @Column(name = "UOM_ID")
    private Integer uomId;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "CURRENCY_ID")
    private Integer currencyId;

    @Column(name = "ADJUSTMENT_VALUE")
    private Integer adjustmentValue;

    @Column(name = "ADJUSTMENT_TYPE")
    private String adjustmentType;

    @Column(name = "FINAL_ADJUSTMENT_VALUE")
    private Integer finalAdjustmentValue;
}
