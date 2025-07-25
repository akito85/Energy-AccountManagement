package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_RATE_ADJ")
public class VW_RATE_ADJ {
    @Id
    @Column(name ="ID")
    private Integer id;

    @Column(name ="BILLING_CODE")
    private String billingCode;

    @Column(name ="RATE")
    private Integer rate;

    @Column(name ="RATE_DATE")
    private Date rateDate;

    @Column(name ="RATE_TYPE")
    private String rateType;

    @Column(name ="FROM_CURRENCY")
    private Integer fromCurrency;

    @Column(name ="FROM_CURRENCY_VAL")
    private String fromCurrencyVal;

    @Column(name ="TO_CURRENCY")
    private Integer toCurrency;

    @Column(name ="TO_CURRENCY_VAL")
    private String toCurrencyVal;
}
