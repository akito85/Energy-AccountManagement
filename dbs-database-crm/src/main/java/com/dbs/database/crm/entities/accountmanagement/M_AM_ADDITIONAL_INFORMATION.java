package com.dbs.database.crm.entities.accountmanagement;


import javax.persistence.*;

import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

@Entity
@Data
@Table(name = "M_AM_ADDITIONAL_INFORMATION")
public class M_AM_ADDITIONAL_INFORMATION extends DefaultBaseEntities {
    
    @Id
    @Column(name = "ID", nullable=false, updatable=false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_AM_ADDITIONAL_INFORMATION_SEQ")
    @SequenceGenerator(sequenceName = "M_AM_ADDITIONAL_INFORMATION_SEQ", allocationSize = 1, name = "M_AM_ADDITIONAL_INFORMATION_SEQ")
    private Integer id;
    
    @Column(name = "ACCOUNT_ID")
    private Integer accountId;
    
    @Column(name = "INFORMATION_TYPE")
    private Integer informationType;
    
    @Column(name = "VALUE1")
    private String value1;

    @Column(name = "VALUE2")
    private Integer value2;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;
    
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
