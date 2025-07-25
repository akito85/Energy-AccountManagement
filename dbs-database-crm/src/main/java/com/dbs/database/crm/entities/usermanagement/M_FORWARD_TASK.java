package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.DefaultBaseEntities;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "M_FORWARD_TASK")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class M_FORWARD_TASK extends DefaultBaseEntities implements Serializable {

    @Id
    @Column(name = "FORWARD_TASK_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_FORWARD_TASK_SEQ")
    @SequenceGenerator(sequenceName = "M_FORWARD_TASK_SEQ", allocationSize = 1, name = "M_FORWARD_TASK_SEQ")
    private Integer forwardTaskId;
    
    @Column(name = "FORWARD_TASK_HDR_ID")
    private Integer forwardTaskHdrId;

    @Column(name = "POSITION_ID_FROM")
    private Integer positionIdFrom;
    
    @Column(name = "POSITION_ID_TO")
    private Integer positionIdTo;
    
    @Column(name = "APPHIER_ID")
    private Integer appHierId;
    
    @Column(name = "T_APP_ID")
    private Integer tAppId;
    
    @Column(name = "APPHIER_DTL_ID")
    private Integer apphierDtlId;
    
    @Column(name = "EMPLOYEE_FORWARD", length = 30)
    private String employeeForward;


    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + forwardTaskId;
        }
    }
}
