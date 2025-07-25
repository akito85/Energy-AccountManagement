package com.dbs.database.crm.entities.accountmanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;

@Entity
@Data
@Table(name = "VW_AM_GAS_UTILS_DTL")
public class VW_AM_GAS_UTILS_DTL extends BaseEntities {
    
    @Id
    @Column(name = "ID", nullable=false, updatable=false)
    private Integer id;
    
    @Column(name = "GAS_UTILS_ID")
    private Integer gasUtilsId;

    @Column(name = "NAME_ID")
    private Integer nameId;
    
    @Column(name = "NAME")
    private String name;
    
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
