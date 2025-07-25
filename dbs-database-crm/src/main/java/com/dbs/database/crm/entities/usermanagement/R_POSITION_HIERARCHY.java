package com.dbs.database.crm.entities.usermanagement;


import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Data
@Table(name = "R_POSITION_HIERARCHY")
public class R_POSITION_HIERARCHY extends BaseEntities implements Serializable {
    @Id
    @Column(name = "R_HIER_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_POSITION_HIERARCHY_SEQ")
    @SequenceGenerator(sequenceName = "R_POSITION_HIERARCHY_SEQ", allocationSize = 1, name = "R_POSITION_HIERARCHY_SEQ")
    private Integer rHierId;

//    @NotNull(message = "hierId cannot be null")
    @Column(name = "HIER_ID")
    private Integer hierId;

    @Column(name = "PARENT_ID")
    private Integer parentId;

//    @NotNull(message = "positionId cannot be null")
    @Column(name = "POSITION_ID")
    private Integer positionId;

    @Column(name = "DESCRIPTION", length = 250)
    private String description;

    public R_POSITION_HIERARCHY(Integer hierId, Integer parentId, Integer positionId, String description) {
        this.hierId = hierId;
        this.parentId = parentId;
        this.positionId = positionId;
        this.description = description;
    }

    public R_POSITION_HIERARCHY() {
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + rHierId;
        }
    }

}
