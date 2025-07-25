package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.Temporal;

@Entity
@Data
@Table(name = "VW_CALC_JOB")
public class VW_CALC_JOB {
    @Id
    @Column(name = "ID_CALCULATION_JOB")
    private Integer calJobId;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name = "TYPE")
    private Integer type;
    @Column(name = "TYPE_VALUE")
    private String typeVal;
    @Column(name = "GENERATE_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date generateDate;
    @Column(name = "REMARK")
    private String remark;
    @Column(name = "BILLING_PERIOD")
    private Integer billingPeriod;
    @Column(name = "BILLING_PERIOD_VALUE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date billingPeriodVal;
    @Column(name = "BILLING_PERIOD_NAME")
    private String billingPeriodName;
    @Column(name = "SERVICE_TYPE")
    private Integer serviceType;
    @Column(name = "SERVICE_TYPE_VALUE")
    private String serviceTypeVal;
    @Column(name = "SOR")
    private Integer sor;
    @Column(name = "SOR_VALUE")
    private String sorVal;
    @Column(name = "COST_CENTER")
    private String costCenter;
    @Column(name = "METER_READING_CODE")
    private String meterReadingCode;
    @Column(name = "CUSTOMER_SEGMENT")
    private String customerSegment;
    @Column(name = "ACCOUNT_GROUP_TYPE")
    private String accGroupType;
    @Column(name = "SPECIFIC_CUSTOMER_ACCOUNT")
    private String custNumb;
    @Column(name = "SCHEDULE_TYPE")
    private Integer scheduleType;
    @Column(name = "SCHEDULE_TYPE_VALUE")
    private String scheduleTypeVal;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CALCULATION_CODE")
    private String calculationCode;
    @Column(name = "CUSTOMER")
    private Integer customer;
    @Column(name = "FAILED")
    private Integer failed;
    @Column(name = "PROGRESS")
    private Integer progress;
    @Column(name = "SUCCEED")
    private Integer succeed;
    @Column(name = "BILLING_CYCLE")
    private Integer billingCycle;
    @Column(name = "BILLING_CYCLE_VALUE")
    private String billingCycleVal;
    @Column(name = "IS_SHOW")
    private String isShow;
    @Column(name = "CC_ID")
    private Integer ccId;
    @Column(name = "CREATED_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdDate;
    @Column(name = "CREATED_BY")
    private String createdBy;

}
