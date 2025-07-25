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
@Table(name = "M_AM_TAXIMPLICATION")
public class M_AM_TAXIMPLICATION extends BaseEntities implements Serializable {

    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_AM_TAXIMPLICATION_SEQ")
    @SequenceGenerator(sequenceName = "M_AM_TAXIMPLICATION_SEQ", allocationSize = 1, name = "M_AM_TAXIMPLICATION_SEQ")
    private Integer id;

    @Column(name = "TAX_IMPLICATION_NAME")
    private String taxImplicationName;

    @Column(name = "CATEGORY")
    private Integer category;

    @Column(name = "SERVICE_TYPE")
    private Integer serviceType;

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
