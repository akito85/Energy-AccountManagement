package com.dbs.database.crm.entities.accountmanagement;

import java.io.Serializable;
import java.util.Date;

import com.dbs.common.base.entities.BaseEntities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "M_INSURANCE_CLAIM_HISTORY")
public class M_INSURANCE_CLAIM_HISTORY extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ID", nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_INSURANCE_CLAIM_HISTORY_SEQ")
    @SequenceGenerator(sequenceName = "M_INSURANCE_CLAIM_HISTORY_SEQ",allocationSize = 1, name = "M_INSURANCE_CLAIM_HISTORY_SEQ")
    private Integer id;

    @Column(name = "INSURANCE_ID")
    private Integer insuranceId;

    @Column(name = "DATE_OF_INCIDENT")
    private Date dateOfIncident;

    @Column(name = "CLAIM_INSURANCE_DATE")
    private Date claimInsuranceDate;

    @Column(name = "CAUSE_OF_INCIDENT")
    private String causeofIncident;

    @Column(name = "COVERAGE_TYPE")
    private String coverageType;

    @Column(name = "COST_OF_COVERAGE")
    private Double costOfCoverage;

    @Column(name = "CLAIM_PAYMENT_DATE")
    private Date claimPaymentDate;

    @Column(name = "DESCRIPTION")
    private String description;
}
