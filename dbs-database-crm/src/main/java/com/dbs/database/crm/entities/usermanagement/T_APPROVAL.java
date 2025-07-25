package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "T_APPROVAL")
public class T_APPROVAL extends BaseEntities implements Serializable {
    @Id
    @Column(name="T_APP_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "T_APPROVAL_SEQ")
    @SequenceGenerator(sequenceName = "T_APPROVAL_SEQ", allocationSize = 1, name = "T_APPROVAL_SEQ")
    private Integer tAppId;

    @Column(name="ID_TRANS", length = 100, unique = true)
    private String idTrans;

    @Column(name = "APPROVAL_TYPE")
    private String approvalType;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column (name = "CATEGORY")
    private String category;

    @Column(name="ENTITY_ID")
    private Integer entityId;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "T_APP_ID",referencedColumnName = "T_APP_ID")
    private List<T_APPROVAL_DTL> tApprovalDtl;

    @Column (name = "SUBMITTER_ID")
    private String submitterId;

    @Column (name = "SUBMITTER_POSITION_ID")
    private Integer submitterPositionId;

    @Column (name = "APPROVAL_NAME")
    private String approvalName;

    @Column (name = "TRIGGER_JSON")
    private String triggerJson;

    @Column(name="APPHIER_ID")
    private Integer appHierId;
    
    @Column(name="APPHIER_DTL_ID")
    private Integer appHierDtlId;
    
    @Column(name="APPROVAL_LEVEL")
    private Integer approvalLevel;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + tAppId;
        }
    }
}
