package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Entity
@Data
@Table(name = "VW_TAX_RELATION")
public class VW_TAX_RELATION extends BaseEntities implements Serializable{
    
    @Id
    @Column(name = "TAX_RELATION_ID")
    private Integer taxRelationId;
            
    @Column(name = "START_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date startDate;
    
    @Column(name = "END_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date endDate;
            
    @Column(name = "ACCOUNT_ID")
    private Integer accountId;
    
    @Column(name = "RELATED_ACCOUNT_ID")
    private Integer relatedAccountId;
       
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;
    
    @Column(name = "ACCOUNT_NAME")
    private String accountName;
            
    @Column(name = "TAX_IDENTIFIER_TYPE")
    private String taxIdentifierType;
    
    @Column(name = "TAX_IDENTIFIER_NUMBER")
    private String taxIdentifierNumber;

    @Column(name = "TAX_IDENTIFIER_ADDRESS_VALUE")
    private String taxIdentifierAddressValue;

    @Column(name = "DESCRIPTION")
    private String description;
    
}
