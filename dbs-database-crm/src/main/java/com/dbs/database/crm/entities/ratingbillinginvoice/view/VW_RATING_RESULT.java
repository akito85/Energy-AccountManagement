package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "VW_RATING_RESULT")
public class VW_RATING_RESULT {
    @Id
    @Column(name = "ID")
    private Integer id;
    @Column(name = "RATING_CODE")
    private String ratingCode;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "VALUE")
    private String value;
    @Column(name = "UOM")
    private String uom;
    @Column(name = "TOTAL_AMOUNT")
    private String totalAmount;
    @Column(name = "CURRENCY")
    private String currency;
    @Column(name = "PRICING")
    private String pricing;
}
