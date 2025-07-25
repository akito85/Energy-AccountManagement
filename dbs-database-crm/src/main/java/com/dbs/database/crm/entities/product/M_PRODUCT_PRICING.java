package com.dbs.database.crm.entities.product;


import lombok.Data;

import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.database.crm.entities.accountmanagement.M_PRICING;
import com.dbs.database.crm.entities.accountmanagement.M_PRICING_RULE;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

@Entity
@Data
@Table(name = "M_PRODUCT_PRICING")
public class M_PRODUCT_PRICING {

    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_PRODUCT_PRICING_SEQ")
    @SequenceGenerator(sequenceName = "M_PRODUCT_PRICING_SEQ",allocationSize = 1, name = "M_PRODUCT_PRICING_SEQ")
    private Integer id;

    @Column(name = "PRICE_CODE")
    private String priceCode;

    @Column(name = "PRICING_RULE_ID")
    private Integer pricingRuleId;

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

    @Column(name = "PRICE_CODE_ID")
    private Integer priceCodeId;
    
    @Column(name = "IS_DELETED")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isDeleted;
    
    @OneToOne
	@JoinColumn(name = "PRODUCT_VERSION_ID", referencedColumnName = "ID", updatable = false, insertable = false)
	@JsonIgnore
	private M_PRODUCT_VERSION mProductVersion;
    
    @ManyToOne
	@JoinColumn(name = "PRICING_RULE_ID", referencedColumnName = "PRICING_RULE_ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private M_PRICING_RULE pricingRuleValue;

	@Transient
//	@JsonIgnore
	public String getPricingRuleDescription() {
		if (pricingRuleValue != null)
			return getPricingRuleValue().getDescription();
		else
			return null;
	}
	
	@Transient
//	@JsonIgnore
	public String getPricingRuleName() {
		if (pricingRuleValue != null)
			return getPricingRuleValue().getName();
		else
			return null;
	}
	
	@ManyToOne
	@JoinColumn(name = "PRICE_CODE_ID", referencedColumnName = "ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private M_PRICING pricingValue;

	@Transient
//	@JsonIgnore
	public String getPricingDescription() {
		if (pricingValue != null)
			return getPricingValue().getPriceDescription();
		else
			return null;
	}

}
