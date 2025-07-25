package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "T_DELEGATION")
public class T_DELEGATION extends BaseEntities implements Serializable {
    @Id
    @Column(name="DELEGATION_ID", nullable = false, updatable = false, length = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "T_DELEGATION_SEQ")
    @SequenceGenerator(sequenceName = "T_DELEGATION_SEQ", allocationSize = 1, name = "T_DELEGATION_SEQ")
    private Integer delegationId;

    @Column(name="REQUESTOR")
    private String requestor;

    @Column(name="DELEGATE_TO")
    private String delegateTo;

    @Column(name="REMARK")
    private String remark;

    @Column(name="REASON")
    private String reason;

    @Column(name="GA_REL_ID")
    private Integer gaId;

    @Column(name="POSITION_ID")
    private Integer positionId;

    @Column(name="ENTITY_ID")
    private Integer entityId;

    @Column(name="APPROVAL_DATE")
    private Date approvalDate;

    @Column(name="START_DATE")
    private Date startDate;

    @Column(name="END_DATE")
    private Date endDate;

    public T_DELEGATION() {
        super();
    }

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
