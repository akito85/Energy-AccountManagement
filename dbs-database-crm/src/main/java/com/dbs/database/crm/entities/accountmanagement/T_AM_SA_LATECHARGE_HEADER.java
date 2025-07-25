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
@Table(name = "T_AM_SA_LATECHARGE_HEADER")
public class T_AM_SA_LATECHARGE_HEADER extends BaseEntities implements Serializable {

    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "T_AM_SA_LATECHARGE_HEADER_SEQ")
    @SequenceGenerator(sequenceName = "T_AM_SA_LATECHARGE_HEADER_SEQ", allocationSize = 1, name = "T_AM_SA_LATECHARGE_HEADER_SEQ")
    private Integer id;
    
    @Column(name = "T_AM_SA_ID")
    private Integer saId;

    @Column(name = "LATE_CHARGES_ID")
    private Integer lateChargesId;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "IS_ACTIVE")
    private String isActive;

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
