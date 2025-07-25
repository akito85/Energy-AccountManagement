package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Data
@Table(name = "R_DATA_ACCESS_HIERARCHY")
public class R_DATA_ACCESS_HIERARCHY extends BaseEntities implements Serializable {
    @Id
    @Column(name = "RDAH_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_DATA_ACCESS_HIERARCHY_SEQ")
    @SequenceGenerator(sequenceName = "R_DATA_ACCESS_HIERARCHY_SEQ", allocationSize = 1, name = "R_DATA_ACCESS_HIERARCHY_SEQ")
    private Integer rDahId;

    @Column(name = "DAH_ID")
    private Integer dahId;

    @Column(name = "CC_ID")
    private Integer ccId;

    @Column(name = "PARENT_ID")
    private Integer parentId;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "RDAH_ID", referencedColumnName = "RDAH_ID")
    private Set<R_COSTCENTER_SIBLING> rCostCenterSibling;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + rDahId;
        }
    }
}
