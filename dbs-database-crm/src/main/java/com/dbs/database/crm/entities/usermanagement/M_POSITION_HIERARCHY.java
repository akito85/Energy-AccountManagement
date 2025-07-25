package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "M_POSITION_HIERARCHY")
public class M_POSITION_HIERARCHY extends BaseEntities implements Serializable {
    @Id
    @Column(name="HIER_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_POSITION_HIERARCHY_SEQ")
    @SequenceGenerator(sequenceName = "M_POSITION_HIERARCHY_SEQ", allocationSize = 1, name = "M_POSITION_HIERARCHY_SEQ")
    private Integer hierId;

//    @NotBlank(message = "name cannot be null or empty")
    @Column(name="NAME", length = 250, unique = true)
    private String name;

    @Column(name="DESCRIPTION", length = 250)
    private String description;

    @Column(name="START_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;

    @Column(name = "END_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;

    @Column(name="ENTITY_ID", length = 250)
    private Integer entityId;

    @Column(name="IS_DELETED")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isDeleted;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "HIER_ID",referencedColumnName = "HIER_ID")
    private List<R_POSITION_HIERARCHY> rPositionHierarchies;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + hierId;
        }
    }
}
