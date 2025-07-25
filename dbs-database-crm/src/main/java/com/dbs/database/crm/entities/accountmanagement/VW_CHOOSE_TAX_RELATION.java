package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "VW_CHOOSE_TAX_RELATION")
public class VW_CHOOSE_TAX_RELATION extends BaseEntities implements Serializable {
    
    @Id
    @Column(name = "CUSTOMER_ID")
    private Integer customerId;
    
    @Column(name = "CUSTOMER_NUMBER")
    private String customerNumber;
    
    @Column(name = "CUSTOMER_NAME")
    private String customerName;
    
    @Column(name = "ACCOUNT_ID")
    private Integer accountId;
    
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;
    
    @Column(name = "ACCOUNT_NAME")
    private String accountName;
    
    @Column(name = "TAX_IDENTIFIER_TYPE")
    private Integer taxIdentifierType;
    
    @Column(name = "TAX_IDENTIFIER_TYPE_VALUE")
    private String taxIdentifierTypeValue;
    
    @Column(name = "TAX_IDENTIFIER_NUMBER")
    private String taxIdentifierNumber;
    
    @Column(name = "TAX_IDENTIFIER_NAME")
    private String taxIdentifierName;
    
    @Column(name = "TAX_IDENTIFIER_ADDRESS")
    private Integer taxIdentifierAddress;
    
    @Column(name = "TAX_IDENTIFIER_ADDRESS_VALUE")
    private String taxIdentifierAddressValue;
    
}
