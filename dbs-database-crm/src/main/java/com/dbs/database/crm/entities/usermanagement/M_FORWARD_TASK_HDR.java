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
@Table(name = "M_FORWARD_TASK_HDR")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class M_FORWARD_TASK_HDR extends DefaultBaseEntities implements Serializable {

    @Id
    @Column(name = "FORWARD_TASK_HDR_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_FORWARD_TASK_HDR_SEQ")
    @SequenceGenerator(sequenceName = "M_FORWARD_TASK_HDR_SEQ", allocationSize = 1, name = "M_FORWARD_TASK_HDR_SEQ")
    private Integer forwardTaskHdrId;

    @Column(name = "REMARK", length = 255)
    private String remark;
    
    @Column(name = "EMPLOYEE_CODE", length = 30)
    private String employeeCode;


    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + forwardTaskHdrId;
        }
    }
}
