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
@Table(name = "M_METER_READING_CODES")
public class M_METER_READING_CODES extends BaseEntities {
    
    @Id
    @Column(name = "ID", nullable=false, updatable=false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="M_METER_READING_CODES_SEQ")
    @SequenceGenerator(sequenceName = "M_METER_READING_CODES_SEQ", allocationSize=1, name = "M_METER_READING_CODES_SEQ")
    private Integer meterReadingCodeId;
    
    @Column(name = "CODE", length=25)
    private String code;
    
    @Column(name = "COST_CENTER_ID")
    private Integer costCenterId;

    @Column(name = "COST_CENTER")
    private String costCenter;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    
    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + meterReadingCodeId;
        }
    }
}
