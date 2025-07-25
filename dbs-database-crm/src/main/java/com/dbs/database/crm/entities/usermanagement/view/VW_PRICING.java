package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "VW_PRICING")
public class VW_PRICING {
    @Id
    @Column(name = "KEY")
    private Integer key;

    @Column(name="PRICING_RULE_DETAIL_ID")
    private Integer pricingRuleDetailId;

    @Column(name="VALUE")
    private Double value;

    @Column(name="UOM")
    private String uom;

    @Column(name="CURRENCY")
    private String currency;
}
