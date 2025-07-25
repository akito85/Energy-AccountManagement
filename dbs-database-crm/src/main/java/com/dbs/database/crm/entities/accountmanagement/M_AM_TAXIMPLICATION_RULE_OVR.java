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
@Table(name = "M_AM_TAXIMPLICATION_RULE_OVR")
public class M_AM_TAXIMPLICATION_RULE_OVR extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_AM_TAXIMPLICATION_RULE_OVR_SEQ")
    @SequenceGenerator(sequenceName = "M_AM_TAXIMPLICATION_RULE_OVR_SEQ", allocationSize = 1, name = "M_AM_TAXIMPLICATION_RULE_OVR_SEQ")
    private Integer id;

    @Column(name = "M_AM_TAXIMPLICATION_RULE_ID")
    private Integer taximplicationRuleId;

    @Column(name = "IMPLICATION_TYPE_OVR")
    private Integer implicationTypeOvr;

    @Column(name = "IMPLICATION_TYPE_OVR_VALUE")
    private String implicationTypeOvrValue;

    @Column(name = "TRANS_CODE_OVR")
    private Integer transCodeOvr;

    @Column(name = "DESCRIPTION")
    private String description;

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
