package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "VW_RATING_SA_PRICING")
public class VW_RATING_SA_PRICING {
    @Id
    @Column(name="RATING_SA_PRICING_ID")
    private Integer ratingSaPricingId;

    @Column(name = "RATING_SA_ID")
    private Integer ratingSaId;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "UOM")
    private String uom;

    @Column(name = "PRICING")
    private String pricing;

    @Column(name = "FINAL_ADJUSTMENT_VALUE")
    private Double finalAdjustmentVal;

    @Column(name = "PRICING_ADJUSTMENT")
    private String pricingAdjustment;
}
