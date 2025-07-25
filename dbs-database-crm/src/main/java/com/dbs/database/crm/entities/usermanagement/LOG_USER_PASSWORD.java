package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.DefaultBaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

import lombok.EqualsAndHashCode;
import org.codehaus.jackson.map.ObjectMapper;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(
        name = "LOG_USER_PASSWORD"
)
public class LOG_USER_PASSWORD extends DefaultBaseEntities implements Serializable {
    @Id
    @Column(name = "ID", nullable = false, updatable = false, length = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_USER_PASSWORD_SEQ")
    @SequenceGenerator(sequenceName = "LOG_USER_PASSWORD_SEQ", allocationSize = 1, name = "LOG_USER_PASSWORD_SEQ")
    private Integer id;

    @Column(name = "USER_ID")
    private Integer userId;

    @Column
    private String password;

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

