package com.dbs.database.crm.entities.product;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.dbs.database.crm.entities.accountmanagement.M_PRICING_RULE;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import java.util.Date;

/**
 * The persistent class for the R_PRICING_RULE_CRITERIA database table.
 * 
 */
@Entity
@Data
//@NamedQuery(name="R_PRICING_RULE_CRITERIA.findAll", query="SELECT r FROM R_PRICING_RULE_CRITERIA r")
public class R_PRICING_RULE_CRITERIA implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "R_PRICING_RULE_CRITERIA_SEQ", allocationSize = 1, sequenceName = "R_PRICING_RULE_CRITERIA_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_PRICING_RULE_CRITERIA_SEQ")
	@Column(name = "PRICING_RULE_CRITERIA_ID")
	private Integer pricingRuleCriteriaId;

	@Column(name = "CC_ID")
	private String ccId;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "CRITERIA")
	private Integer criteria;

	@Column(name = "ENTITY_ID")
	private Integer entityId;

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@Column(name = "PRICING_RULE_ID")
	private Integer pricingRuleId;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_DATE")
	private Date updatedDate;

//	@ManyToOne
//	@JoinColumn(name = "PRICING_RULE_ID", updatable = false, insertable = false)
//	@JsonIgnore
//	@NotFound(action = NotFoundAction.IGNORE)
//	private M_PRICING_RULE mPricingRule;

	@ManyToOne
	@JoinColumn(name = "CRITERIA", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private R_GLOBAL_TYPE_VALUE criteriaValue;

	@Transient
//	@JsonIgnore
	public String getCriteriaName() {
		if (criteriaValue != null)
			return getCriteriaValue().getName();
		else
			return null;
	}

}