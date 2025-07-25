package com.dbs.database.crm.entities.accountmanagement;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "VW_CUSTOMER_CONTACT_DTL")
public class VW_CUSTOMER_CONTACT_DTL implements Serializable {
    
    @Column(name = "CUSTOMER_ID")
    private Integer customerId;
    
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;
    
    @Column(name = "ACCOUNT_NAME")
    private String accountName;
    
    @Id
    @Column(name = "CONTACT_ID")
    private Integer contactId;
    
    @Column(name = "TYPE")
    private Integer type;
    
    @Column(name = "INPUT_TYPE")
    private Integer inputType;
    
    @Column(name = "PREFIX_1")
    private Integer prefix1;
    
    @Column(name = "PREFIX_2")
    private Integer prefix2;
    
    @Column(name = "VALUE")
    private String value;
    
    @Column(name = "SUFIX")
    private String sufix;
    
    @Column(name = "FULL_VALUE")
    private String fullValue;
    
}
