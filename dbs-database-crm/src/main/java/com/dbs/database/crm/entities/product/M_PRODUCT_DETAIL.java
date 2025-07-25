package com.dbs.database.crm.entities.product;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "M_PRODUCT_DETAIL")
public class M_PRODUCT_DETAIL {

	@Id
	@Column(name = "ID", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_PRODUCT_DETAIL_SEQ")
	@SequenceGenerator(sequenceName = "M_PRODUCT_DETAIL_SEQ", allocationSize = 1, name = "M_PRODUCT_DETAIL_SEQ")
	private Integer id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "VALUE")
	private BigDecimal value;

	@Column(name = "UOM")
	private String uom;

	@Column(name = "PRODUCT_VERSION_ID")
	private Integer productVersionId;

	@Column(name = "NAME_ID")
	private Integer nameId;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "UPDATED_DATE")
	private Date updatedDate;

	@Column(name = "DESCRIPTION")
	private String description;

	@Convert(converter = BooleanToYNStringConverter.class)
	@Column(name = "IS_DELETED")
	private boolean isDeleted;

	@Column(name = "PAYMENT_TYPE")
	private Integer paymentType;

	@Column(name = "CHARGING_METHOD")
	private Integer chargingMethod;

	@ManyToOne
	@JoinColumn(name = "PRODUCT_VERSION_ID", referencedColumnName = "ID", updatable = false, insertable = false)
	@JsonIgnore
	private M_PRODUCT_VERSION mProductVersion;

	@ManyToOne
	@JoinColumn(name = "UOM", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
	@JsonIgnore
	private R_GLOBAL_TYPE_VALUE uomValue;

	@Transient
//	@JsonIgnore
	public String getUnitName() {
		if (uomValue != null)
			return getUomValue().getName();
		else
			return null;
	}
}
