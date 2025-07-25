package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "VW_CUSTOMER_CONTACT")
public class VW_CUSTOMER_CONTACT implements Serializable {

    @Column(name = "CUSTOMER_ID")
    private Integer customerId;

    @Id
    @Column(name = "ACCOUNT_CONTACT_ID")
    private Integer accountContactId;
    
    @Column(name = "ACCOUNT_ID")
    private Integer accountId;
    
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;
    
    @Column(name = "ACCOUNT_NAME")
    private String accountName;
    
    @Column(name = "CUSTOMER_MANAGEMENT_ID")
    private Integer customerManagementId;
    
    @Column(name = "COST_CENTER_ID")
    private Integer costCenterId;
    
    @Column(name = "CONTACT_NAME")
    private String contactName;

    @Column(name = "CONTACT_ID")
    private Integer contactId;

    @Column(name = "CONTACT_ADDRESS_ID")
    private Integer contactAddressId;
    
    @Column(name = "CONTACT_ADDRESS")
    private String contactAddress;
    
    @Column(name = "FIRST_NAME")
    private String firstName;
    
    @Column(name = "MIDDLE_NAME")
    private String middleName;
    
    @Column(name = "LAST_NAME")
    private String lastName;
    
    @Column(name = "JOB_NAME")
    private String jobName;
    
    @Column(name = "JOB_ID")
    private String jobId;
    
    @Column(name = "POSITION_NAME")
    private String positionName;
    
    @Column(name = "POSITION_ID")
    private String positionId;
    
    @Column(name = "ADDITIONAL_NOTE")
    private String additionalNote;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "STATUS")
    private String status;
    
    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "PRIMARY_FLAG")
    private Boolean primaryFlag;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;
}
