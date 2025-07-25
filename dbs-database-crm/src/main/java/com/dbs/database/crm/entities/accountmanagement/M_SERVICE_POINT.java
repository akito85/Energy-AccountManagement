package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

@Entity
@Data
@Table(name = "M_SERVICE_POINT")
public class M_SERVICE_POINT extends BaseEntities {
    
    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_SERVICE_POINT_SEQ")
    @SequenceGenerator(sequenceName = "M_SERVICE_POINT_SEQ", allocationSize = 1, name = "M_SERVICE_POINT_SEQ")
    private Integer id;
    
    @Column(name = "ACCOUNT_ADDRESS_ID")
    private Integer accountAddressId;
    
    @Column(name = "SERVICE_POINT_NAME")
    private Integer servicePointName;
    
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
