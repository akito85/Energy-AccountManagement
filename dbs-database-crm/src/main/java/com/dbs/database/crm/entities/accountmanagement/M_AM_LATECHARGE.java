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
@Table(name = "M_AM_LATECHARGE")
public class M_AM_LATECHARGE extends BaseEntities implements Serializable {
    
    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_AM_LATECHARGE_SEQ")
    @SequenceGenerator(sequenceName = "M_AM_LATECHARGE_SEQ", allocationSize = 1, name = "M_AM_LATECHARGE_SEQ")
    private Integer id;

    @Column(name = "LATE_CHARGE_NAME")
    private String lateChargeName;

    @Column(name = "CURRENCY")
    private Integer currency;

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
