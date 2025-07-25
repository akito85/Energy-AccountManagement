package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "LOG_MONITORING_SESSION")
public class LOG_MONITORING_SESSION implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_MONITORING_SESSION_SEQ")
    @SequenceGenerator(sequenceName = "LOG_MONITORING_SESSION_SEQ", allocationSize = 1, name = "LOG_MONITORING_SESSION_SEQ")
    @Column(name = "LOG_ID_MONITORING", nullable = false, updatable = false)
    private Integer idMonitoring;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "LAST_ACCESS_MENU")
    private String lastAccessMenu;

    @Column(name = "LAST_ACCESS")
    private Date lastAccess;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @Column(name = "USER_ID")
    private Integer userId;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + idMonitoring;
        }
    }
}
