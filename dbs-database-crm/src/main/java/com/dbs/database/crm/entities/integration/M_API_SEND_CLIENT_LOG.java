package com.dbs.database.crm.entities.integration;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@Table(name = "M_API_SEND_CLIENT_LOG")
public class M_API_SEND_CLIENT_LOG extends BaseEntities implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2",strategy = "uuid2")
    @Column(name = "ID", nullable = false, updatable = false)
    private String id;
    
    @Column(name = "PAYLOAD", length = 4000)
    private String payload;
    
    @Column(name = "SEND_TO")
    private String sendTo;
    
    @Column(name = "RETRY")
    private Integer retry;

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
