package com.dbs.database.crm.entities.product;


import lombok.Data;

import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

@Entity
@Data
@Table(name = "R_ELIGIBILITY_PRODUCT")
public class R_ELIGIBILITY_PRODUCT {

    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "R_ELIGIBILITY_PRODUCT_SEQ")
    @SequenceGenerator(sequenceName = "R_ELIGIBILITY_PRODUCT_SEQ",allocationSize = 1, name = "R_ELIGIBILITY_PRODUCT_SEQ")
    private Integer id;

    @Column(name = "PRODUCT_ID")
    private Integer productId;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "PRODUCT_VERSION_MAIN_ID")
    private Integer productVersionMainId;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;
    
    @Column(name = "IS_DELETED")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isDeleted;
    
    @Column
    private String description;
    
    @ManyToOne
	@JoinColumn(name = "PRODUCT_VERSION_MAIN_ID", referencedColumnName = "ID", updatable = false, insertable = false)
	@JsonIgnore
	private M_PRODUCT_VERSION mProductVersion;
    
    @ManyToOne
	@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private M_PRODUCT mProduct;
    
    @Transient
//	@JsonIgnore
	public String getProductName() {
    	if(mProduct != null)
    		return getMProduct().getProductName();
    	else
    		return null;
	}
    
}
