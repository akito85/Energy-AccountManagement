package com.dbs.database.crm.entities.product;

import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNT;
import com.dbs.database.crm.entities.accountmanagement.M_PRICING_RULE;
import com.dbs.database.crm.entities.usermanagement.M_COSTCENTER;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Data
@Table(name = "R_PRICING_RULE_CRITERIA_DATA")
public class R_PRICING_RULE_CRITERIA_DATA extends DefaultBaseEntities implements Serializable {

	@Id
	@Column(name = "ID", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_PRICING_RULE_CRITERIA_DATA_SEQ")
	@SequenceGenerator(sequenceName = "R_PRICING_RULE_CRITERIA_DATA_SEQ", allocationSize = 1, name = "R_PRICING_RULE_CRITERIA_DATA_SEQ")
	private Integer id;

	@Column(name = "CRITERIA_VALUE")
	private String criteriaValue;

	@Column(name = "CUSTOMER")
	private Integer customer;

	@Column(name = "BUDGET")
	private Integer budget;

	@Column(name = "SUB_DISTRICT")
	private Integer subDistrict;

	@Column(name = "DISTRICT")
	private Integer district;

	@Column(name = "CITY")
	private Integer city;

	@Column(name = "PROVINCE")
	private Integer province;

	@Column(name = "AREA")
	private Integer area;

	@Column(name = "SOR")
	private Integer sor;

	@Column(name = "INDUSTRIAL_SECTOR")
	private Integer industrialSector;

	@Column(name = "PRODUCT")
	private Integer product;

	@Column(name = "GSIZES")
	private Integer gsizes;

	@Column(name = "CUSTOMER_SEGMENT")
	private Integer customerSegment;

	@Column(name = "ACCOUNT_GROUP")
	private Integer accountGroup;

	@Column(name = "SERVICE_TYPE")
	private Integer serviceType;

	@Column(name = "ACCOUNT_CATEGORY")
	private Integer accountCategory;

	@Convert(converter = BooleanToYNStringConverter.class)
	@Column(name = "ALL_CRITERIA")
	private Boolean allCriteria;

	@Column(name = "ID_PRICING_RULE")
	private Integer idPricingRule;

	@Column(name = "START_DATE")
	private Date startDate;

	@Column(name = "END_DATE")
	private Date endDate;

	@ManyToOne
	@JoinColumn(name = "BUDGET", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private R_GLOBAL_TYPE_VALUE budgetName;

	@ManyToOne
	@JoinColumn(name = "SUB_DISTRICT", referencedColumnName = "LOCATION_ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private M_LOCATION subDistrictName;

	@ManyToOne
	@JoinColumn(name = "DISTRICT", referencedColumnName = "LOCATION_ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private M_LOCATION districtName;

	@ManyToOne
	@JoinColumn(name = "CITY", referencedColumnName = "LOCATION_ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private M_LOCATION cityName;

	@ManyToOne
	@JoinColumn(name = "PROVINCE", referencedColumnName = "LOCATION_ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private M_LOCATION provinceName;

	@ManyToOne
	@JoinColumn(name = "AREA", referencedColumnName = "CC_ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private M_COSTCENTER areaName;

	@ManyToOne
	@JoinColumn(name = "SOR", referencedColumnName = "CC_ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private M_COSTCENTER sorName;

	@ManyToOne
	@JoinColumn(name = "INDUSTRIAL_SECTOR", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private R_GLOBAL_TYPE_VALUE industrialSectorName;

	@ManyToOne
	@JoinColumn(name = "PRODUCT", referencedColumnName = "ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private M_PRODUCT productName;

	@ManyToOne
	@JoinColumn(name = "GSIZES", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private R_GLOBAL_TYPE_VALUE gSizesName;

	@ManyToOne
	@JoinColumn(name = "CUSTOMER_SEGMENT", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private R_GLOBAL_TYPE_VALUE customerSegmentName;

	@ManyToOne
	@JoinColumn(name = "ACCOUNT_GROUP", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private R_GLOBAL_TYPE_VALUE accountGroupName;

	@ManyToOne
	@JoinColumn(name = "ACCOUNT_CATEGORY", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private R_GLOBAL_TYPE_VALUE accountCategoryName;

//	@ManyToOne
//	@JoinColumn(name = "ID_PRICING_RULE", updatable = false, insertable = false)
//	@JsonIgnore
//	@NotFound(action = NotFoundAction.IGNORE)
//	private M_PRICING_RULE mPricingRule;

	@ManyToOne
	@JoinColumn(name = "CUSTOMER", referencedColumnName = "ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private M_ACCOUNT customerValue;

	@Transient
//		@JsonIgnore
	public String getCustomerName() {
		if (customerValue != null)
			return getCustomerValue().getAccountName();
		else
			return null;
	}

	@ManyToOne
	@JoinColumn(name = "SERVICE_TYPE", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private R_GLOBAL_TYPE_VALUE serviceTypeValue;

	@Transient
//	@JsonIgnore
	public String getServiceTypeName() {
		if (serviceTypeValue != null)
			return getServiceTypeValue().getName();
		else
			return null;
	}

}
