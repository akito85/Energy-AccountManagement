package com.dbs.database.crm.entities.accountmanagement;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "M_AM_LATECHARGE_RULE")
public class M_AM_LATECHARGE_RULE extends BaseEntities implements Serializable {
    
    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_AM_LATECHARGE_RULE_SEQ")
    @SequenceGenerator(sequenceName = "M_AM_LATECHARGE_RULE_SEQ", allocationSize = 1, name = "M_AM_LATECHARGE_RULE_SEQ")
    private Integer id;

    @Column(name = "M_AM_LATECHARGE_ID")
    private Integer latechargeId;

    @Column(name = "DOCUMENT_NUMBER")
    private String documentNumber;

    @Column(name = "MAX_AMOUNT")
    private Double maxAmount;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IS_CONDITION")
    private String isCondition;

    @Column(name = "APPHIER_ID")
    private Integer apphierId;
    @Column(name = "APPROVAL_STATUS")
    private String approvalStatus;
    @Column(name = "JSON_DATA")
    private String jsonData;

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
