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
@Table(name = "T_AM_SA_CALCRULE_DTL")
public class T_AM_SA_CALCRULE_DTL extends BaseEntities implements Serializable {

    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "T_AM_SA_CALCRULE_DTL_SEQ")
    @SequenceGenerator(sequenceName = "T_AM_SA_CALCRULE_DTL_SEQ", allocationSize = 1, name = "T_AM_SA_CALCRULE_DTL_SEQ")
    private Integer id;

    @Column(name = "T_AM_SA_CALCRULE_HEADER_ID")
    private Integer saCalcruleHeaderId;

    @Column(name = "NAME")
    private Integer name;

    @Column(name = "VALUE")
    private Integer value;

    @Column(name = "UNIT")
    private String unit;

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
