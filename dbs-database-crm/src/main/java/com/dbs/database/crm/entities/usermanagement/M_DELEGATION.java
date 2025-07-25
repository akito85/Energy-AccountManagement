package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "M_DELEGATION")
public class M_DELEGATION extends BaseEntities implements Serializable {
    @Id
    @Column(name="DELEGATION_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_DELEGATION_SEQ")
    @SequenceGenerator(sequenceName = "M_DELEGATION_SEQ", allocationSize = 1, name = "M_DELEGATION_SEQ")
    private Integer delegationId;

    @Column(name="POSITION_ID")
    private Integer positionId;

    @Column(name="EMPLOYEE_ID")
    private Integer employeeId;

    @Column(name="START_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;

    @Column(name = "END_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;

    @Column(name="REMARK", length = 255)
    private String remark;
    
    @Column(name="ENTITY_ID")
    private Integer entityId;

    @Column(name="STATUS_APPROVAL")
    private String statusApproval;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + delegationId;
        }
    }
}
