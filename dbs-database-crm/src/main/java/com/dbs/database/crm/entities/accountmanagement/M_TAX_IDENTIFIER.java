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
@Table(name = "M_TAX_IDENTIFIER")
public class M_TAX_IDENTIFIER extends BaseEntities implements Serializable {
    
    @Id
    @Column(name = "ID", nullable=false, updatable=false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_TAX_IDENTIFIER_SEQ")
    @SequenceGenerator(sequenceName = "M_TAX_IDENTIFIER_SEQ", allocationSize = 1, name = "M_TAX_IDENTIFIER_SEQ")
    private Integer taxIdentifierId;
    
    @Column(name = "ACCOUNT_ID")
    private Integer accountId;
    
    @Column(name = "TAX_IDENTIFIER_TYPE")
    private Integer taxIdentifierType;
    
    @Column(name = "TAX_IDENTIFIER_NUMBER")
    private String taxIdentifierNumber;
    
    @Column(name = "TAX_IDENTIFIER_NAME")
    private String taxIdentifierName;
    
    @Column(name = "TAX_IDENTIFIER_ADDRESS")
    private Integer taxIdentifierAddress;
    
    @Column(name = "START_DATE")
    private Date startDate;
    
    @Column(name = "END_DATE")
    private Date endDate;
    
    @Column(name = "DESCRIPTION")
    private String description;
}
