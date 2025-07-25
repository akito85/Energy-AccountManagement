package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

@Entity
@Data
@Table(name = "M_MULTI_DESTINATION_ATTACHMENT")
public class M_MULTI_DESTINATION_ATTACHMENT extends BaseEntities implements Serializable {
    
    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_MULTI_DESTINATION_ATTACHMENT_SEQ")
    @SequenceGenerator(sequenceName = "M_MULTI_DESTINATION_ATTACHMENT_SEQ", allocationSize = 1, name = "M_MULTI_DESTINATION_ATTACHMENT_SEQ")
    private Integer multiDestinationAttachmentId;
    
    @Column(name = "MULTI_DESTINATION_ID")
    private Integer multiDestinationId;
    
    @Column(name = "ATTACHMENT_ID")
    private Integer attachmentId;
    
    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + multiDestinationAttachmentId;
        }
    }
}
