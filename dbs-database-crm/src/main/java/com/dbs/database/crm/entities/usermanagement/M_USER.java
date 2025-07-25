package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import lombok.EqualsAndHashCode;
import org.codehaus.jackson.map.ObjectMapper;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(
        name = "M_USER",
        uniqueConstraints = {
                @UniqueConstraint(name = "m_user_username_unique_constraint", columnNames = "USERNAME")
        }
)
public class M_USER extends BaseEntities implements Serializable {
    @Id
    @Column(name = "USER_ID", nullable = false, updatable = false, length = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_USER_SEQ")
    @SequenceGenerator(sequenceName = "M_USER_SEQ", allocationSize = 1, name = "M_USER_SEQ")
    private Integer userId;

    @Column(name = "USER_CODE")
    private String userCode;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL", length = 50)
    private String email;

    @Column(name = "PHONE", length = 50)
    private String phone;

    @Column(name = "USER_TYPE")
    private String userType;

    @Column(name = "AUTH_TYPE")
    private String authType;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Convert(converter = BooleanToYNStringConverter.class)
    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

    @Column(name = "START_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;

    @Column(name = "END_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;

    @Column(name = "USER_LEVEL")
    private String userLevel;

    @Column(name = "EMPLOYEE_ID")
    private Integer employeeId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "EMPLOYEE_ID", insertable = false, updatable = false, nullable = true)
    private M_EMPLOYEE employee;
    
    @Column(name = "FAILED_LOGIN")
    private Integer failedLogin;
    
    @Column(name = "LOCKED_TIME")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lockedTime;
    
    @Column(name = "EXP_PASS")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date expPass;
    
    public M_USER() {
        super();
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + userId;
        }
    }
}

