package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;

import javax.persistence.*;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

@Entity
@Data
@Table(name = "R_AM_GAS_UTILS_DTL")
public class R_AM_GAS_UTILS_DTL extends BaseEntities {
    
    @Id
    @Column(name = "ID", nullable=false, updatable=false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="R_AM_GAS_UTILS_DTL_SEQ")
    @SequenceGenerator(sequenceName = "R_AM_GAS_UTILS_DTL_SEQ", allocationSize = 1, name = "R_AM_GAS_UTILS_DTL_SEQ")
    private Integer id;
    
    @Column(name = "GAS_UTILS_ID")
    private Integer gasUtilsId;
    
    @Column(name = "NAME")
    private Integer name;
    
    @Column(name = "PERCENTAGE")
    private Double percentage;
    
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
