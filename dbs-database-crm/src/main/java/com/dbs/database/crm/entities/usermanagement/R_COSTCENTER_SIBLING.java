package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "R_COSTCENTER_SIBLING")
public class R_COSTCENTER_SIBLING extends BaseEntities implements Serializable {
    @Id
    @Column(name = "RSIBLING_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_COSTCENTER_SIBLING_SEQ")
    @SequenceGenerator(sequenceName = "R_COSTCENTER_SIBLING_SEQ", allocationSize = 1, name = "R_COSTCENTER_SIBLING_SEQ")
    private Integer rsiblingId;

    @Column(name = "RDAH_ID")
    private Integer rDahId;

    @Column(name = "SIBLING_ID")
    private Integer siblingId;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + rsiblingId;
        }
    }
}
