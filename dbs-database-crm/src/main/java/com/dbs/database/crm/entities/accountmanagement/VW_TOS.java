package com.dbs.database.crm.entities.accountmanagement;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;

import java.util.Date;


/**
 * The persistent class for the VW_TOS database table.
 * 
 */
@Entity
@Table(name = "VW_TOS")
@Data
public class VW_TOS implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
        @Column(name="ID")
	private Integer id;
        
        @Column(name="NAME")
	private String name;

        @Column(name="DESCRIPTION")
	private String description;

	@Column(name="CREATED_DATE")
	private Date createdDate;

	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Column(name="UPDATED_DATE")
	private Date updatedDate;
	
        @Column(name="STATUS")
	private String status;
        
        @Column(name="ATTRIBUTES")
	private String attributes;
        
        @Column(name="CRITERIAS")
	private String criterias;
        
        @Column(name="ENTITY_ID")
	private Integer entityId;
	@Column(name = "CCID")
	private Integer ccId;

}