package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

@Entity
@Data
@Table(name = "M_MULTI_DESTINATION")
public class M_MULTI_DESTINATION extends BaseEntities implements Serializable{
    
    @Id
    @Column(name = "ID", nullable = false, updatable = false, length = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_MULTI_DESTINATION_SEQ")
    @SequenceGenerator(sequenceName = "M_MULTI_DESTINATION_SEQ", allocationSize = 1, name = "M_MULTI_DESTINATION_SEQ")
    private Integer multiDestinationId; // ID Muldes for SA column flagging
    
    @Column(name = "ACCOUNT_ID")
    private Integer accountId;
    
    @Column(name = "MULTI_DESTINATION_NAME", length = 100)
    private String multiDestinationName;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "START_DATE")
    private Date startDate;
    
    @Column(name = "END_DATE")
    private Date endDate;
    
    @Column(name = "STATUS", length = 25)
    private String status; // Draft,Waiting for Approval, Active, Inactive
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "MULTI_DESTINATION_ID", referencedColumnName = "ID")
    private List<M_MULTI_DESTINATION_DETAIL> amMultiDestinationDetail;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "MULTI_DESTINATION_ID", referencedColumnName = "ID")
    private List<M_MULTI_DESTINATION_ATTACHMENT> amMultiDestinationAttachment;
    
    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + multiDestinationId;
        }
    }
}
