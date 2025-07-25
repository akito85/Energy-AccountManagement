package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "M_APPROVAL_HIERARCHY")
public class M_APPROVAL_HIERARCHY extends BaseEntities implements Serializable {
    @Id
    @Column(name = "APPHIER_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_APPROVAL_HIERARCHY_SEQ")
    @SequenceGenerator(sequenceName = "M_APPROVAL_HIERARCHY_SEQ", allocationSize = 1, name = "M_APPROVAL_HIERARCHY_SEQ")
    private Integer appHierId;

    @Column(name = "APPROVAL_TYPE")
    private String approvalType;

//    @Column(name = "APPROVAL_CODE", length = 100, unique = true)
//    private String approvalCode;

    @Column(name = "APPROVAL_NAME")
    private String approvalName;

    @Column(name = "DESCRIPTION")
    private String desc;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "IS_DELETED")
    @Convert(converter = BooleanToYNStringConverter.class)
    private Boolean isDeleted;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "APPHIER_ID", referencedColumnName = "APPHIER_ID")
    private List<M_APPROVAL_HIERARCHY_DTL> mApprovalHierarchyDtl;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + appHierId;
        }
    }
}
