package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_CALC_HISTORY")
public class VW_CALC_HISTORY {
    @Id
    @Column(name = "ID_RESULT")
    private Integer resultId;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ACCOUNT_GROUP_TYPE")
    private String accGroupType;

    @Column(name = "ACCOUNT_GROUP_TYPE_VAL")
    private String accGroupTypeVal;

    @Column(name = "ACCOUNT_NUMBER")
    private String accNumb;

    @Column(name = "ACCOUNT_NAME")
    private String accName;

    @Column(name = "ACCOUNT_SEGMENT")
    private String accSegment;

    @Column(name = "ACCOUNT_SEGMENT_VAL")
    private String accSegmentVal;

    @Column(name = "BILLING_CYCLE")
    private Integer billingCycId;

    @Column(name = "BILLING_CYCLE_VALUE")
    private String billingCycVal;

    @Column(name = "BILLING_PERIOD")
    private Integer billingPeriodId;

    @Column(name = "BILLING_PERIOD_VALUE")
    private String billingPeriodVal;

    @Column(name = "CALCULATION_CODE")
    private String calCode;

    @Column(name = "CALCULATION_TYPE")
    private Integer calType;

    @Column(name = "CALCULATION_TYPE_VALUE")
    private String calTypeVal;

    @Column(name = "CALCULATE_AT")
    private String calculateAt;

    @Column(name = "COST_CENTER")
    private String costCenter;

    @Column(name = "COST_CENTER_VAL")
    private String costCenterVal;

    @Column(name = "CUSTOMER_NUMBER")
    private String custNumb;

    @Column(name = "CUSTOMER_NAME")
    private String custName;

    @Column(name = "SA_NUMBER")
    private String saNumb;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "IS_TRY")
    private Boolean isTry;

    @Column(name = "METER_READING_CODE")
    private String meterReadingCode;

    @Column(name = "METER_READING_CODE_VAL")
    private String meterReadingCodeVal;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "SERVICE_TYPE")
    private String serviceType;

    @Column(name = "SERVICE_TYPE_VAL")
    private String serviceTypeVal;

    @Column(name = "SOR")
    private String sor;

    @Column(name = "SOR_VAL")
    private String sorVal;

    @Column(name = "ACTION")
    private String action;

    @Column(name = "CC_ID")
    private Integer ccId;
}
