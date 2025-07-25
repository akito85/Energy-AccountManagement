package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "M_DATA_ACCESS_HIERARCHY")
public class M_DATA_ACCESS_HIERARCHY extends BaseEntities implements Serializable {
    @Id
    @Column(name = "DAH_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_DATA_ACCESS_HIERARCHY_SEQ")
    @SequenceGenerator(sequenceName = "M_DATA_ACCESS_HIERARCHY_SEQ", allocationSize = 1, name = "M_DATA_ACCESS_HIERARCHY_SEQ")
    private Integer dahId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "DAH_ID",referencedColumnName = "DAH_ID")
    private Set<R_DATA_ACCESS_HIERARCHY> rDataAccessHierarchy;

    public M_DATA_ACCESS_HIERARCHY() {
        super();
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + dahId;
        }
    }
}
