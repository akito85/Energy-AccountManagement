package com.dbs.database.crm.entities.accountmanagement;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "R_TOS_CRITERIA")
public class R_TOS_CRITERIA implements Serializable {

    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "R_TOS_CRITERIA_SEQ")
    @SequenceGenerator(sequenceName = "R_TOS_CRITERIA_SEQ",allocationSize = 1, name = "R_TOS_CRITERIA_SEQ")
    private Integer id;

    @Column(name = "CRITERIA_ID")
    private Integer criteriaId;

    @Column(name = "ID_TOS")
    private Integer idTos;

    @Column(name="CREATED_BY")
    private String createdBy;

    @Column(name="UPDATED_BY")
    private String updatedBy;

    @Column(name="CREATED_DATE")
    private Date createdDate;

    @Column(name="UPDATED_DATE")
    private Date updatedDate;


    

}
