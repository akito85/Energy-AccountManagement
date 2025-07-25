package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "VW_APPROVAL_PENDING")
public class VW_APPROVAL_PENDING extends BaseEntities implements Serializable {
    @Id
    @Column(name="T_APP_ID")
    private Integer tAppId;

    @Column(name="ID_TRANS", length = 100, unique = true)
    private String idTrans;

    @Column(name = "APPROVAL_TYPE")
    private String approvalType;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column
    private String category;

    @Column(name="ENTITY_ID")
    private Integer entityId;

    @Column
    private String submitterId;

    @Column
    private Integer submitterPositionId;

    @Column
    private String approvalName;

    @Column
    private String triggerJson;

    @Column(name="APPHIER_ID")
    private Integer appHierId;
    
    @Column(name="APPHIER_DTL_ID")
    private Integer appHierDtlId;
    
    @Column(name="APPROVAL_LEVEL")
    private Integer approvalLevel;
    
    @Column(name="POSITION_ID")
    private Integer positionId;
    
    @Column(name="POSITION_NAME")
    private String positionName;

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
