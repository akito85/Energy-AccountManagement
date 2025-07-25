package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "LOG_USER_LOGIN")
public class LOG_USER_LOGIN implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_USER_LOGIN_SEQ")
    @SequenceGenerator(sequenceName = "LOG_USER_LOGIN_SEQ", allocationSize = 1, name = "LOG_USER_LOGIN_SEQ")
    @Column(name = "LOG_USER_LOGIN_ID", nullable = false, updatable = false)
    private Integer logUserLoginId;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "REMOTER_ADDR")
    private String remoteAddr;
    
    @Column(name = "TOKEN", length = 4000)
    private String token;
    
    @Column(name = "SESSION_ID")
    private String sessionId;
    
    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_ACTIVE")
    private Boolean isActive;
    
    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_LOGIN")
    private Boolean isLogin;

    @Column(name = "DATE_ACCESS")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateAccess;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + logUserLoginId;
        }
    }
}
