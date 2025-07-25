package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "T_EMP_ASSIGNMENT")
public class T_EMP_ASSIGNMENT extends BaseEntities implements Serializable {
    @Id
    @Column(name = "EMP_ASSIGNMENT_ID", nullable = false, updatable = false, length = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "T_EMP_ASSIGNMENT_SEQ")
    @SequenceGenerator(sequenceName = "T_EMP_ASSIGNMENT_SEQ", allocationSize = 1, name = "T_EMP_ASSIGNMENT_SEQ")
    private Integer empAssignmentId;

    @Column(name = "EMPLOYEE_CODE")
    private String employeeCode;

    @Column(name = "EMP_NUMBER")
    private String empNumber;

    @Column(name = "JOB_ID", length = 100)
    private Integer jobId;

    @Column(name = "POSITION_ID", length = 100)
    private Integer positionId;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Convert(converter = BooleanToYNStringConverter.class)
    @Column(name = "IS_MAIN")
    private Boolean isMain;

    public T_EMP_ASSIGNMENT() {
        super();
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + empAssignmentId;
        }
    }
}

