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
@Table(name = "M_PREMISE")
public class M_PREMISE extends BaseEntities implements Serializable {
    
    @Id
    @Column(name = "ID",nullable = false, updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_PREMISE_SEQ")
    @SequenceGenerator(sequenceName = "M_PREMISE_SEQ", allocationSize = 1, name = "M_PREMISE_SEQ")
    private Integer id;

    @Column(name = "ACCOUNT_ID")
    private Integer accountId;

    @Column(name = "ADDRESS_ID")
    private Integer addressId;

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
