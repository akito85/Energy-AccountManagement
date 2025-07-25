package com.dbs.database.crm.entities.product;


import lombok.Data;

import javax.persistence.*;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "M_PRODUCT_TARGET_ACCOUNT_SELLING")
public class M_PRODUCT_TARGET_ACCOUNT_SELLING {

    @Id
    @Column(name = "ID",updatable = false,nullable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_PRODUCT_TARGET_ACCOUNT_SELLING_HEADER_SEQ")
    @SequenceGenerator(sequenceName = "M_PRODUCT_TARGET_ACCOUNT_SELLING_HEADER_SEQ",allocationSize = 1, name = "M_PRODUCT_TARGET_ACCOUNT_SELLING_HEADER_SEQ")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PRODUCT_VERSION_ID")
    private Integer productVersionId;

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
    
    @OneToMany(mappedBy = "mProductTargetAccountSelling",fetch = FetchType.LAZY)
	@JsonIgnore
	@JsonProperty(value="mProductTargetAccountSellingCriteria")
	private List<M_PRODUCT_TARGET_ACCOUNT_SELLING_CRITERIA> mProductTargetAccountSellingCriteria;
	
	@OneToMany(mappedBy = "mProductTargetAccountSelling",fetch = FetchType.LAZY)
	@JsonIgnore
	@JsonProperty(value="rCriteriaDataProductTas")
	private List<R_CRITERIA_DATA_PRODUCT_TAS> rCriteriaDataProductTas;
	
	@OneToOne
	@JoinColumn(name = "PRODUCT_VERSION_ID", referencedColumnName = "ID", updatable = false, insertable = false)
	@JsonIgnore
	private M_PRODUCT_VERSION mProductVersion;
	
	

}
