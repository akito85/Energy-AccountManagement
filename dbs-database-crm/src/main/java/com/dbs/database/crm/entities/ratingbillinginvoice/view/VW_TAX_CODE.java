package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import com.dbs.common.base.entities.DefaultBaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_TAX_CODE")
public class VW_TAX_CODE extends DefaultBaseEntities implements Serializable {
    
    @Id
    @Column(name="TAX_CODE_ID")
    private Integer taxCodeId;
    @Column(name = "TAX_CODE", unique = true, length = 10)
    private String taxCode;
    @Column(name = "TAX_CODE_NAME")
    private String taxCodeName;
    @Column(name = "TAX_RATE")
    private String taxRate;
    @Column(name = "CATEGORY")
    private Integer category;
    @Column(name = "CATEGORY_NAME")
    private String categoryName;
    @Column(name = "GL_ACCOUNT")
    private Integer glAccount;   //LOV
    @Column(name = "START_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;
    @Column(name = "END_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;
    @Column(name = "DESCRIPTION", length = 255)
    private String description;
    @Column(name = "APP_HIER_ID")
    private Integer appHierId;
    @Column(name = "STATUS_APPROVAL", length = 20)
    private String statusApproval;
    @Column(name = "STATUS", length = 20)
    private String status;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name = "CC_ID")
    private Integer ccId;
    @Column(name = "CRITERIAS")
    private String criterias;
    @Column(name = "CRITERIAS_DOWNLOAD")
    private String criteriasDownload;
    
}
