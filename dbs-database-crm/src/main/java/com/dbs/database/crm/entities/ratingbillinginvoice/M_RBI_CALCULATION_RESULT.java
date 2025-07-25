package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "M_RBI_CALCULATION_RESULT")
public class M_RBI_CALCULATION_RESULT extends BaseEntities implements Serializable {
    @Id
    @Column(name ="ID_RESULT")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_RBI_CALCULATIONRESULT_SEQ")
    @SequenceGenerator(sequenceName = "M_RBI_CALCULATIONRESULT_SEQ",allocationSize = 1, name = "M_RBI_CALCULATIONRESULT_SEQ")
    private Integer resultId;

    @Column(name = "CALCULATION_CODE")
    private String calCode;

    @Column(name = "CUSTOMER_NUMBER")
    private String custNumb;

    @Column(name = "CUSTOMER_NAME")
    private String custName;

    @Column(name = "ACCOUNT_NUMBER")
    private String accNumb;

    @Column(name = "ACCOUNT_GROUP_TYPE")
    private Integer accGroupType;

    @Column(name = "SERVICE_TYPE")
    private Integer serviceType;

    @Column(name = "SA_NUMBER")
    private String saNumb;

    @Column(name = "SOR")
    private Integer  sor;

    @Column(name = "COST_CENTER")
    private Integer costCenter;

    @Column(name = "ACCOUNT_SEGMENT")
    private Integer accSegment;

    @Column(name = "METER_READING_CODE")
    private Integer meterReadingCode;

    @Column(name = "BILLING_CYCLE")
    private Integer billingCycle;

    @Column(name = "BILLING_PERIOD")
    private Integer billingPeriod;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "IS_TRY")
    private String isTry;

    @Column(name = "CALCULATE_AT")
    private Date calculateAt;

    @Column(name = "CALCULATION_TYPE")
    private Integer calType;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "ACTION")
    private String action;

    @Column(name = "CC_ID")
    private Integer ccId;
}
