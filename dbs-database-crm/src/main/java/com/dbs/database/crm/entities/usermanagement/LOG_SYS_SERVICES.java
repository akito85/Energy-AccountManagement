package com.dbs.database.crm.entities.usermanagement;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "LOG_SYS_SERVICES")
public class LOG_SYS_SERVICES implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_SYS_SERVICES_SEQ")
    @SequenceGenerator(sequenceName = "LOG_SYS_SERVICES_SEQ", allocationSize = 1, name = "LOG_SYS_SERVICES_SEQ")
    @Column(name = "ID", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "VURL", length = 512, nullable = false)
    private String vurl;

    @Column(name = "VMETHOD", length = 10, nullable = false)
    private String vmethod;

    @Column(name = "VIPADDRESS", length = 30, nullable = false)
    private String vipaddress;

    @Column(name = "VAGENT", length = 255, nullable = false)
    private String vagent;

    @Column(name = "IEXECTIME", nullable = false)
    private long iexectime;

    @Column(name = "VUSERNAME", length = 50, nullable = false)
    private String vusername;

    @Column(name = "VORIGIN", length = 255)
    private String vorigin;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + id;
        }
    }
}
