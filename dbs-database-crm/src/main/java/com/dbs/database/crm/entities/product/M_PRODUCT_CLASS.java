package com.dbs.database.crm.entities.product;

import com.dbs.common.base.entities.DefaultBaseEntities;
import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;


/**
 * The persistent class for the M_PRODUCT_CLASS database table.
 * 
 */
@Entity
@Data
@Table(name = "M_PRODUCT_CLASS")
public class M_PRODUCT_CLASS extends DefaultBaseEntities implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="M_PRODUCT_CLASS_SEQ", sequenceName="M_PRODUCT_CLASS_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_PRODUCT_CLASS_SEQ" )
	@Column(name="PRODUCT_CLASS_ID")
	private Integer productClassId;

        @Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="STATUS")
	private String status;

	@Column(name="IS_DELETED")
	private String isDeleted;

        @Column(name="NAME")
	private String name;
        
        @Column(name="CCID")
	private Integer ccId;
	
	@Column(name="ENTITY_ID")
	private Integer entityId;

}