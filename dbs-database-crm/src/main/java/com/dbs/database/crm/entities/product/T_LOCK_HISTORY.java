package com.dbs.database.crm.entities.product;


import lombok.Data;

import javax.persistence.*;


import java.util.Date;

@Entity
@Data
@Table(name = "T_LOCK_HISTORY")
public class T_LOCK_HISTORY {

    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "T_LOCK_HISTORY_SEQ")
    @SequenceGenerator(sequenceName = "T_LOCK_HISTORY_SEQ",allocationSize = 1, name = "T_LOCK_HISTORY_SEQ")
    private Integer id;

    @Column(name = "REF_ID")
    private Integer refId;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "LOCK_TYPE")
    private String lockType;
    
    @Column(name = "LOCK_BY")
    private String lockBy;

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
