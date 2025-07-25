package com.dbs.database.crm.entities.product;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;

import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the VW_APPROVAL_HISTORY database table.
 * 
 */
@Entity
@Data
public class VW_APPROVAL_HISTORY implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="EVENT_DATE")
	private Timestamp eventDate;

	@Column(name="EVENT_TYPE")
	private String eventType;

	@Column(name="FULL_NAME")
	private String fullName;

	@Column(name="ID_TRANS")
	private Integer idTrans;

	@Id
	private Integer ids;


        @Column
	private String position;

        @Column
	private String status;


	@Column(name="SUBMIT_DATE")
	private Date submitDate;

	@Column(name="T_APP_ID")
	private Integer tAppId;
	
	@Column(name="APPROVAL_NAME")
	private String approvalName;

        @Column
	private String category;
	
        @Column
	private String description;

}