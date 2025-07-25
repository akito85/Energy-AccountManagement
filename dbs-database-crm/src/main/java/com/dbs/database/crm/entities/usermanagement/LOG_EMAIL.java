package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "LOG_EMAIL")
public class LOG_EMAIL extends BaseEntities implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_EMAIL_SEQ")
    @SequenceGenerator(sequenceName = "LOG_EMAIL_SEQ", allocationSize = 1, name = "LOG_EMAIL_SEQ")
    @Column(name = "ID", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "DATA_USER")
    private String dataUser;

    @Column(name = "SUBJECT")
    private String subject;
    
     @Column(name = "CTX_VAR")
    private String ctxVar;
    
    @Column(name = "TEMPLATE_EMAIL", length = 100)
    private String templateEmail;
    
    @Column(name = "LINK")
    private String link;
    
    @Column(name = "ENCRYP_DATA")
    private String encrypData;
    
    @Column(name = "TYPE_EMAIL")
    private String typeEmail;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
