package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.BaseEntities;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "M_RBI_CALCULATION_JOB")
public class M_RBI_CALCULATION_JOB extends BaseEntities implements Serializable {
    @Id
    @Column(name ="ID_CALCULATION_JOB")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_RBI_CALCULATIONJOB_SEQ")
    @SequenceGenerator(sequenceName = "M_RBI_CALCULATIONJOB_SEQ",allocationSize = 1, name = "M_RBI_CALCULATIONJOB_SEQ")
    private Integer calJobId;

    @Column(name = "CALCULATION_CODE")
    private String calCode;

    @Column(name = "TOTAL_CUSTOMER")
    private Integer totalCustomer;

    @Column(name = "TOTAL_SUCCEED")
    private Integer totalSucceed;

    @Column(name = "TOTAL_PROGRESS")
    private Integer totalProgress;

    @Column(name = "TOTAL_FAILED")
    private Integer totalFailed;

    @Column(name = "BILLING_PERIOD")
    private Integer billingPeriod;

    @Column(name = "BILLING_CYCLE")
    private Integer billingCycle;

    @Column(name = "SERVICE_TYPE")
    private Integer serviceType;

    @Column(name = "SOR")
    private Integer sor;

    @Column(name = "SCHEDULE_TYPE")
    private Integer scheduleType;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CALCULATION_TYPE")
    private Integer calculationType;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "PREFIX_CODE")
    private String prefixCode;

    @Column(name = "IS_SHOW")
    private String isShow;

    @Column(name = "CC_ID")
    private Integer ccId;

    @JsonProperty("rRbiCalculationCostCenter")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CALCULATION_CODE",referencedColumnName = "CALCULATION_CODE")
    private List<R_RBI_CALCULATION_COST_CENTER>rRbiCalculationCostCenter;

    @JsonProperty("rRbiCalculationAccountGroupType")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CALCULATION_CODE",referencedColumnName = "CALCULATION_CODE")
    private List<R_RBI_CALCULATION_ACCOUNT_GROUP_TYPE>rRbiCalculationAccountGroupType;

    @JsonProperty("rRbiCalculationAccountSegment")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CALCULATION_CODE",referencedColumnName = "CALCULATION_CODE")
    private List<R_RBI_CALCULATION_ACCOUNT_SEGMENT>rRbiCalculationAccountSegment;

    @JsonProperty("rRbiCalculationMeterReadingCode")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CALCULATION_CODE",referencedColumnName = "CALCULATION_CODE")
    private List<R_RBI_CALCULATION_METER_READING_CODE>rRbiCalculationMeterReadingCode;

    @JsonProperty("rRbiCalculationSpecificCustomer")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CALCULATION_CODE",referencedColumnName = "CALCULATION_CODE")
    private List<R_RBI_CALC_SPECIFIC_CUSTOMER>rRbiCalculationSpecificCustomer;

}
