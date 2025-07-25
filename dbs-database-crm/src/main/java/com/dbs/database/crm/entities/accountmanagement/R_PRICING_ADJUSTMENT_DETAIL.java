package com.dbs.database.crm.entities.accountmanagement;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.common.base.utils.CustomddMMyyyyDeserializer;
import com.dbs.common.base.utils.CustomddMMyyyySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
@Entity
@Data
@Table(name="R_PRICING_ADJUSTMENT_DETAIL")
public class R_PRICING_ADJUSTMENT_DETAIL {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "R_CRITERIA_ADJUSTMENT_PRICING_SEQ")
	@SequenceGenerator(sequenceName = "R_CRITERIA_ADJUSTMENT_PRICING_SEQ",allocationSize = 1, name = "R_CRITERIA_ADJUSTMENT_PRICING_SEQ")
	private Integer id;
	@Column(name = "ADJUSTMENT_TYPE")
	private Integer adjustmentType;
	@Column(name = "ADJUSTMENT_VALUE", scale = 2)
	private BigDecimal adjustmentValue;
	@JsonSerialize(using = CustomddMMyyyySerializer.class)
	@JsonDeserialize(using = CustomddMMyyyyDeserializer.class)
	@Column(name = "START_DATE")
        @Temporal(javax.persistence.TemporalType.DATE)
	private Date startDate;
	@JsonSerialize(using = CustomddMMyyyySerializer.class)
	@JsonDeserialize(using = CustomddMMyyyyDeserializer.class)
	@Column(name = "END_DATE")
        @Temporal(javax.persistence.TemporalType.DATE)
	private Date endDate;
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "UPDATED_DATE")
	private Date updatedDate;
	@Column(name = "UPDATED_BY")
	private String updatedBy;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name="ID_PRICING_ADJUSTMENT")
	private Integer pricingAdjustmentId;
	//Criteria
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

	@Convert(converter= BooleanToYNStringConverter.class)
	@Column(name = "ALL_CRITERIA")
	private Boolean allCriteria;
}
