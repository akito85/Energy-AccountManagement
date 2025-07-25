package com.dbs.database.crm.entities.product;


import lombok.Data;

import javax.persistence.*;


import java.util.Date;

@Entity
@Data
@Table(name = "T_EXT_TER_HISTORY")
public class T_EXT_TER_HISTORY {

    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "T_EXT_TER_HISTORY_SEQ")
    @SequenceGenerator(sequenceName = "T_EXT_TER_HISTORY_SEQ",allocationSize = 1, name = "T_EXT_TER_HISTORY_SEQ")
    private Integer id;

    @Column(name = "REF_ID")
    private Integer refId;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "TYPE")
    private String type;
    
    @Column(name = "ACTION_BY")
    private String actionBy;
    
    @Column(name = "ACTION_DATE")
    private Date actionDate;
    
    @Column(name = "END_DATE_BEFORE")
    private Date endDateBefore;
    
    @Column(name = "END_DATE_AFTER")
    private Date endDateAfter;
    
    @Column
    private String status;
    
    @Column(name = "TAPP_ID")
    private Integer tappId;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    
}
