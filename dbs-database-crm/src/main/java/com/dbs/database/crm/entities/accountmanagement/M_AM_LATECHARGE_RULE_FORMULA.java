package com.dbs.database.crm.entities.accountmanagement;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.codehaus.jackson.map.ObjectMapper;

import com.dbs.common.base.entities.BaseEntities;

import lombok.Data;

@Entity
@Data
@Table(name = "M_AM_LATECHARGE_RULE_FORMULA")
public class M_AM_LATECHARGE_RULE_FORMULA extends BaseEntities implements Serializable {
    
    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_AM_LATECHARGE_RULE_FORMULA_SEQ")
    @SequenceGenerator(sequenceName = "M_AM_LATECHARGE_RULE_FORMULA_SEQ", allocationSize = 1, name = "M_AM_LATECHARGE_RULE_FORMULA_SEQ")
    private Integer id;

    @Column(name = "M_AM_LATECHARGE_RULE_ID")
    private Integer latechargeRuleId;

    @Column(name = "OPERATION")
    private Integer operation;

    @Column(name = "OPERATION_VALUE")
    private String operationValue;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "VARIABLE_NAME")
    private Integer variableName;

    @Column(name = "VARIABEL_NAME_VALUE")
    private String variabelNameValue;

    @Column(name = "CONSTANT_NAME")
    private String constantName;

    @Column(name = "VALUE")
    private Float value;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + id;
        }
    }
}
