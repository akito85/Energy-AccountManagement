package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "VW_MASTER_LATE_CHARGE")
public class VW_MASTER_LATE_CHARGE extends BaseEntities implements Serializable {
    @Id
    @Column(name="LATE_CHARGE_ID")
    private Integer lateChargeId;
    @Column(name="NAME")
    private String name;
    @Column(name="DESCRIPTION")
    private String description;
    @Column(name="CURRENCY")
    private String currency;
    @Column(name="LATE_CHARGE_RULE_ID")
    private Integer lateChargeRuleId;
    @Column(name="MAX_AMOUNT")
    private String maxAmount;
    @Column(name="MAX_AMOUNT_REAL")
    private Double maxAmountReal;
    @Column(name="FORMULA")
    private String formula;
    @Column(name="CRITERIA")
    private String criteria;


}
