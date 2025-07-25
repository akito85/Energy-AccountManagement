package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "VW_RATING_SA_PRICING_RULE")
public class VW_RATING_SA_PRICING_RULE {
    @Id
    private Integer ratingSaPricingRuleId;

    @Column(name = "RATING_SA_ID")
    private Integer ratingSaId;

    @Column(name = "LINE_NUMBER")
    private Integer lineNumber;

    @Column(name = "MIN")
    private String min;

    @Column(name = "MAX")
    private String max;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_UNLIM")
    private Boolean isUnlim;

    @Column(name = "ID_M_PRICING")
    private Integer idPricing;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "UOM")
    private String uom;

    @Column(name = "PRICE_CODE")
    private String priceCode;

    @Column(name = "PRICING_ADJUSTMENT")
    private String pricingAdjustment;
}
