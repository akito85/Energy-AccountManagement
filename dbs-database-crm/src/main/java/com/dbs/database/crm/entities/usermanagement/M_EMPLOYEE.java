package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@Table(name = "M_EMPLOYEE")
public class M_EMPLOYEE extends BaseEntities implements Serializable {
    @Id
    @Column(name = "EMPLOYEE_ID", nullable = false, updatable = false, length = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_EMPLOYEE_SEQ")
    @SequenceGenerator(sequenceName = "M_EMPLOYEE_SEQ", allocationSize = 1, name = "M_EMPLOYEE_SEQ")
    private Integer employeeId;

    @Column(name = "EMPLOYEE_CODE")
    private String employeeCode;

    @Column(name="EMP_NUMBER")
    private String empNumber;

    @Column(name="FIRST_NAME", length = 100)
    private String firstName;

    @Column(name="LAST_NAME", length = 100)
    private String lastName;

    @Column(name = "EMAIL", length = 50)
    private String email;

    @Column(name = "PHONE", length = 50)
    private String phone;

    @Column(name = "EMP_TYPE")
    private String empType;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "START_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;

    @Column(name = "END_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;

    @Column(name = "FULL_NAME")
    private String fullName;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_CODE", referencedColumnName = "EMPLOYEE_CODE")
    private Set<T_EMP_ASSIGNMENT> tEmpAssignment;

    public M_EMPLOYEE() {
        super();
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + employeeId;
        }
    }
}

