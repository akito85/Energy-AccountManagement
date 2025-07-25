package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "R_RBI_RATING_SA_PRICING_RULE")
public class R_RBI_RATING_SA_PRICING_RULE implements Serializable {
    @Id
    @Column(name="RATING_SA_PRICING_RULE_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_RBI_RATING_SA_PRICING_RULE_SEQ")
    @SequenceGenerator(sequenceName = "R_RBI_RATING_SA_PRICING_RULE_SEQ", allocationSize = 1, name = "R_RBI_RATING_SA_PRICING_RULE_SEQ")
    private Integer ratingSaPricingRuleId;

    @Column(name = "RATING_SA_ID")
    private Integer ratingSaId;

    @Column(name = "LINE_NUMBER")
    private Integer lineNumber;

    @Column(name = "MIN")
    private Integer min;

    @Column(name = "MAX")
    private Integer max;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_UNLIM")
    private Boolean isUnlim;

    @Column(name = "ID_M_PRICING")
    private Integer idPricing;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "VALUE")
    private Double value;

    @Column(name = "UOM")
    private String uom;
}
