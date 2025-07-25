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
@Table(name = "M_AM_TAXIMPLICATION_RULE_OVR_CONDITION")
public class M_AM_TAXIMPLICATION_RULE_OVR_CONDITION extends BaseEntities implements Serializable {
    
    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_AM_TAXIMPLICATION_RULE_OVR_CONDITION_SEQ")
    @SequenceGenerator(sequenceName = "M_AM_TAXIMPLICATION_RULE_OVR_CONDITION_SEQ", allocationSize = 1, name = "M_AM_TAXIMPLICATION_RULE_OVR_CONDITION_SEQ")
    private Integer id;

    @Column(name = "M_AM_TAXIMPLICATION_RULE_OVR_ID")
    private Integer taximplicationRuleOvrId;

    @Column(name = "NAME")
    private Integer name;

    @Column(name = "NAME_VALUE")
    private String nameValue;

    @Column(name = "OPERATOR")
    private Integer operator;

    @Column(name = "OPERATOR_VALUE")
    private String operatorValue;

    @Column(name = "DATA_TYPE")
    private Integer dataType;

    @Column(name = "VALUE")
    private String value;

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
