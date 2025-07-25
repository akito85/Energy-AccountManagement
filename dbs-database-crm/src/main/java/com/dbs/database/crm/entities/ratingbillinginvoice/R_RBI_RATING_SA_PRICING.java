package com.dbs.database.crm.entities.ratingbillinginvoice;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "R_RBI_RATING_SA_PRICING")
public class R_RBI_RATING_SA_PRICING implements Serializable {
    @Id
    @Column(name="RATING_SA_PRICING_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_RBI_RATING_SA_PRICING_SEQ")
    @SequenceGenerator(sequenceName = "R_RBI_RATING_SA_PRICING_SEQ", allocationSize = 1, name = "R_RBI_RATING_SA_PRICING_SEQ")
    private Integer ratingSaPricingId;

    @Column(name = "RATING_SA_ID")
    private Integer ratingSaId;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "VALUE")
    private Double value;

    @Column(name = "UOM")
    private String uom;
    @Column(name = "ID_M_PRICING")
    private Integer idPricing;
}
