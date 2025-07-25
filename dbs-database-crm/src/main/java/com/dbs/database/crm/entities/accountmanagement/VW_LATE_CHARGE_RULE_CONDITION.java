package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "VW_LATE_CHARGE_RULE_CONDITION")
public class VW_LATE_CHARGE_RULE_CONDITION extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "LATECHARGE_RULE_ID")
    private Integer latechargeRuleId;

    @Column(name = "DATA_TYPE")
    private Integer dataType;

    @Column(name = "DATA_TYPE_VALUE")
    private String dataTypeValue;

    @Column(name = "NAME")
    private Integer name;

    @Column(name = "NAME_VALUE")
    private String nameValue;

    @Column(name = "OPERATOR")
    private Integer operator;

    @Column(name = "OPERATOR_VALUE")
    private String operatorValue;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "VALUE_REAL")
    private Double valueReal;
}
