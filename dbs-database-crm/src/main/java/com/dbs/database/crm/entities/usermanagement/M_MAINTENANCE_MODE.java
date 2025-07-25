package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "M_MAINTENANCE_MODE")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class M_MAINTENANCE_MODE extends BaseEntities implements Serializable {

    @Id
    @Column(name = "MAINTENANCE_MODE_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_MAINTENANCE_MODE_SEQ")
    @SequenceGenerator(sequenceName = "M_MAINTENANCE_MODE_SEQ", allocationSize = 1, name = "M_MAINTENANCE_MODE_SEQ")
    private Integer maintenanceModeId;

    @Column(name = "REMARK_ON", length = 255)
    private String remarkOn;
    
    @Column(name = "REMARK_OFF", length = 255)
    private String remarkOff;

    @Column
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date startDate;

    @Column
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date endDate;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + maintenanceModeId;
        }
    }
}
