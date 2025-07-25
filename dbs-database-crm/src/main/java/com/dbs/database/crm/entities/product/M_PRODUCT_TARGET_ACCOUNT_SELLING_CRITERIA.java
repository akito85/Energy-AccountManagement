package com.dbs.database.crm.entities.product;

import lombok.Data;

import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

@Entity
@Data
@Table(name = "M_PRODUCT_TARGET_ACCOUNT_SELLING_CRITERIA")
public class M_PRODUCT_TARGET_ACCOUNT_SELLING_CRITERIA {

	@Id
	@Column(name = "ID", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_CRITERIA_PRODUCT_TAS_SEQ")
	@SequenceGenerator(sequenceName = "M_CRITERIA_PRODUCT_TAS_SEQ", allocationSize = 1, name = "M_CRITERIA_PRODUCT_TAS_SEQ")
	private Integer id;

	@Column(name = "CRITERIA")
	private String criteria;

	@Column(name = "ID_TARGET_ACCOUNT_SELLING")
	private Integer idTargetAccountSelling;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "UPDATED_DATE")
	private Date updatedDate;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "IS_DELETED")
	@Convert(converter = BooleanToYNStringConverter.class)
	private Boolean isDeleted;

	@ManyToOne
	@JoinColumn(name = "ID_TARGET_ACCOUNT_SELLING", referencedColumnName = "ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private M_PRODUCT_TARGET_ACCOUNT_SELLING mProductTargetAccountSelling;

	@ManyToOne
	@JoinColumn(name = "CRITERIA", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private R_GLOBAL_TYPE_VALUE criteriaValue;

	@Transient
	public String getCriteriaName() {
		if (criteriaValue != null)
			return getCriteriaValue().getName();
		else
			return null;
	}
}
