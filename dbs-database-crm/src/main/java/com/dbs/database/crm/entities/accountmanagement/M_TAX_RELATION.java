package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "M_TAX_RELATION")
public class M_TAX_RELATION extends BaseEntities implements Serializable {
     
    @Id
    @Column(name = "ID", nullable=false, updatable=false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_TAX_RELATION_SEQ")
    @SequenceGenerator(sequenceName = "M_TAX_RELATION_SEQ", allocationSize = 1, name = "M_TAX_RELATION_SEQ")
    private Integer taxRelationId;
    
    @Column(name = "ACCOUNT_ID")
    private Integer accountId;
    
    @Column(name = "RELATED_ACCOUNT_ID")
    private Integer relatedAccountId;
    
    @Column(name = "START_DATE")
    private Date startDate;
    
    @Column(name = "END_DATE")
    private Date endDate;
    
    @Column(name = "DESCRIPTION")
    private String description;
}
