package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_LATE_CHARGE_RULE_FORMULA")
public class VW_LATE_CHARGE_RULE_FORMULA extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "CONSTANT_NAME")
    private String constantName;

    @Column(name = "LATECHARGE_RULE_ID")
    private Integer latechargeRuleId;

    @Column(name = "OPERATION")
    private Integer operation;

    @Column(name = "OPERATION_VALUE")
    private String operationValue;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "VARIABLE_NAME")
    private Integer variableName;

    @Column(name = "VARIABLE_NAME_VALUE")
    private String variableNameValue;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "VALUE_REAL")
    private Double valueReal;

}
