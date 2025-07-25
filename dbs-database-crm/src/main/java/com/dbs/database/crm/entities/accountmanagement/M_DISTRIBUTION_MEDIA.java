package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import java.util.Date;
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
@Table(name = "M_DISTRIBUTION_MEDIA")
public class M_DISTRIBUTION_MEDIA extends BaseEntities {
    
    @Id
    @Column(name = "ID", nullable=false, updatable=false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="M_DISTRIBUTION_MEDIA_SEQ")
    @SequenceGenerator(sequenceName = "M_DISTRIBUTION_MEDIA_SEQ", allocationSize = 1, name = "M_DISTRIBUTION_MEDIA_SEQ")
    private Integer distributionMediaId;
    
    @Column(name = "ACCOUNT_ID")
    private Integer accountId;
    
    @Column(name = "PRODUCT_VERSION_ID")
    private Integer productVersionId;

    @Column(name = "PRODUCT_ID")
    private Integer productId;
    
    @Column(name = "START_DATE")
    private Date startDate;
    
    @Column(name = "END_DATE")
    private Date endDate;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + distributionMediaId;
        }
    }
}
