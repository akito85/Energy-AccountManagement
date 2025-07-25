package com.dbs.database.crm.entities.product;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name="VW_PRODUCT")
public class VW_PRODUCT implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRODUCT_DESCRIPTION")
    private String productDescription;

    @Column(name = "PRODUCT_TYPE")
    private Integer productType;
    
    @Column(name = "PRODUCT_TYPE_NAME")
    private String productTypeName;

    @Column(name = "SERVICE_TYPE")
    private Integer serviceType;
    
    @Column(name = "SERVICE_TYPE_NAME")
    private String serviceTypeName;

    @Column(name = "PRODUCT_CLASS")
    private Integer productClass;
    
    @Column(name = "PRODUCT_CLASS_NAME")
    private String productClassName;

    @Column(name = "PRICING")
    private String pricing;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "APPROVAL_STATUS")
    private String approvalStatus;

    @Column(name = "LOCK_STATUS")
    private String lockStatus;

    @Column(name = "LOCKED_BY")
    private String lockedBy;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "MAKER_POSITION")
    private String makerPosition;

    @Column(name = "LATEST_VERSION")
    private Integer lastVersion;

    @Column(name = "START_DATE")
    @JsonFormat(pattern = "dd MMM yyyy")
    private Date startDate;

    @Column(name = "END_DATE")
    @JsonFormat(pattern = "dd MMM yyyy")
    private Date endDate;
    
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "CCID")
    private Integer ccId;

}
