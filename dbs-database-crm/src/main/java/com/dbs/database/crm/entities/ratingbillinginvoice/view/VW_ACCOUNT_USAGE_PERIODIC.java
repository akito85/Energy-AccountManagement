package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_ACCOUNT_USAGE_PERIODIC")
public class VW_ACCOUNT_USAGE_PERIODIC {
    @Id
    @Column(name = "SA_ID")
    private Integer id;

    @Column(name = "BILLING_CYCLE_ID")
    private Integer billCycId;

    @Column(name = "ACCOUNT_NUMBER")
    private String accNumb;

    @Column(name = "MAX_USAGE")
    private Integer maxUsage;

    @Column(name = "TOTAL_EST")
    private Integer totalEst;

    @Column(name = "MIN_USAGE")
    private Integer minUsage;

    @Column(name = "BILLING_PERIOD_MINUS")
    private String billingPeriodMinus;

    @Column(name = "BILLING_PERIOD")
    private Integer billPeriod;

    @Column(name = "ROW_NUMBER")
    private Integer rowNum;
}
