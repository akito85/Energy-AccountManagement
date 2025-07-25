package com.dbs.database.crm.entities.accountmanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;

@Entity
@Data
@Table(name = "VW_AM_SRC_DIST_DTL")
public class VW_AM_SRC_DIST_DTL extends BaseEntities {
    
    @Id
    @Column(name = "ID", nullable=false, updatable=false)
    private Integer id;
    
    @Column(name = "SRC_DIST_ID")
    private Integer srcDistId;

    @Column(name = "COUNTRY_ID")
    private Integer countryId;
    
    @Column(name = "COUNTRY")
    private String country;
    
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
