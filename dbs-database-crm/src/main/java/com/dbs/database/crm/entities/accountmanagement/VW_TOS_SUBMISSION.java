package com.dbs.database.crm.entities.accountmanagement;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_TOS_SUBMISSION")
public class VW_TOS_SUBMISSION implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "SA_ID")
    private Integer saId;

    @Column(name = "SA_NUMBER")
    private String saNumber;

    @Column(name = "TOS_NAME")
    private String tosName;

    @Column(name = "TOS_NAME_ID")
    private Integer tosNameId;

    @Column(name = "APPLIED_DATE")
    private Date appliedDate;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

}
