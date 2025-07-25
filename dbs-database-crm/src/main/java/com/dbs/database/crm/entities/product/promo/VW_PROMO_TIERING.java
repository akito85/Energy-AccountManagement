package com.dbs.database.crm.entities.product.promo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "VW_PROMO_TIERING")
public class VW_PROMO_TIERING implements Serializable {
    @Id
    @Column(name = "TIERING_ID",nullable = false,updatable = false)
    private Integer tieringId;

    @Column(name = "PRICING_RULE_ID")
    private Integer pricingRuleId;

    @Column(name = "TIERING")
    private String tiering;

}
