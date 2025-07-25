package com.dbs.database.crm.entities.product;


import lombok.Data;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "M_PRODUCT_VERSION")
public class M_PRODUCT_VERSION {

    @Id
    @Column(name = "ID",nullable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_PRODUCT_VERSION_SEQ")
    @SequenceGenerator(sequenceName = "M_PRODUCT_VERSION_SEQ",allocationSize = 1, name = "M_PRODUCT_VERSION_SEQ")
    private Integer id;

    @Column(name = "PRODUCT_ID")
    private Integer productId;

    @Column(name = "VERSION")
    private Integer version;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "RELEASE_DATE")
    private Date releaseDate;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "APPROVAL_STATUS")
    private String approvalStatus;
    
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "RELEASE_REMARK")
    private String releaseRemark;
    
    @Column(name = "APPHIER_ID")
    private Integer apphierId;
    
    
    @ManyToOne
    @JoinColumn(name="PRODUCT_ID", referencedColumnName = "ID",updatable = false,insertable = false)
    @JsonIgnore
    private M_PRODUCT mProduct;
    
    
//    @OneToMany(mappedBy = "mProductVersion",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	private List<M_PRODUCT_DETAIL> mProductDetail;
//	
//	@OneToMany(mappedBy = "mProductVersion",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	private List<M_PRODUCT_CALCULATION_RULE> mProductCalculationRule;
	
//	@OneToOne(mappedBy = "mProductVersion",fetch = FetchType.LAZY)
//	private M_PRODUCT_TARGET_ACCOUNT_SELLING mProductTargetAccountSelling;
//	
//	@OneToOne(mappedBy = "mProductVersion",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	private M_PRODUCT_PRICING mProductPricing;
//	
//	@OneToMany(mappedBy = "mProductVersion",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	private List<M_PRODUCT_TERM_OF_SERVICE> mProductTermOfService;
//	
//	@OneToMany(mappedBy = "mProductVersion",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	private List<R_ELIGIBILITY_PRODUCT> rEligibilityProduct;
//	
//	@OneToMany(mappedBy = "mProductVersion",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	private List<R_PRODUCT_BUNDLING> rProductBundling;
	
	
	
	
	
	
	
    
    
}
