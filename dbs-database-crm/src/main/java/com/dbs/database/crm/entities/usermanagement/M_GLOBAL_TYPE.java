package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "M_GLOBAL_TYPE")
public class M_GLOBAL_TYPE extends BaseEntities implements Serializable {
    @Id
    @Column(name="GLB_TYPE_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_GLOBAL_TYPE_SEQ")
    @SequenceGenerator(sequenceName = "M_GLOBAL_TYPE_SEQ", allocationSize = 1, name = "M_GLOBAL_TYPE_SEQ")
    private Integer glbTypeId;

    @Column(name="GROUPNAME", length = 250, unique = true)
    private String groupName;

    @Column(name="DESCRIPTION", length = 250)
    private String desc;

    @Column(name="SORT_BY", length = 250)
    private String sortBy;

    @Column(name="ENTITY_ID", length = 250)
    private Integer entity;

    @Column(name="IS_DELETED")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isDeleted;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "GLB_TYPE_ID",referencedColumnName = "GLB_TYPE_ID")
    private Set<R_GLOBAL_TYPE_VALUE> rGlobalTypeValues;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + glbTypeId;
        }
    }

}
