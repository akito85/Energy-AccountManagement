package com.dbs.database.crm.entities.accountmanagement;

import java.io.Serializable;
import java.util.Date;

import com.dbs.common.base.entities.BaseEntities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "M_INSURANCE")
public class M_INSURANCE extends BaseEntities implements Serializable {

    @Id
    @Column(name = "ID", nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_INSURANCE_SEQ")
    @SequenceGenerator(sequenceName = "M_INSURANCE_SEQ",allocationSize = 1, name = "M_INSURANCE_SEQ")
    private Integer id;

    @Column(name = "ACCOUNT_ID")
    private Integer accountId;

    @Column(name = "INSURANCE_TYPE")
    private Integer insuranceType;

    @Column(name = "INSURANCE_NUMBER")
    private String insuranceNumber;

    @Column(name = "VENDOR")
    private Integer vendor;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "INSURANCE_PREMIUM")
    private Double insurancePremium;

    @Column(name = "COVERAGE")
    private Double coverage;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "DESCRIPTION")
    private String description;
}
