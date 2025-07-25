package com.dbs.database.crm.entities.accountmanagement;

import java.util.Date;

import javax.persistence.*;


import lombok.Data;

@Data
@Entity
@Table(name = "R_CRITERIA_ADJUSTMENT_PRICING")
public class R_CRITERIA_ADJUSTMENT_PRICING {
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "R_CRITERIA_ADJUSTMENT_PRICING_SEQ")
	@SequenceGenerator(sequenceName = "R_CRITERIA_ADJUSTMENT_PRICING_SEQ",allocationSize = 1, name = "R_CRITERIA_ADJUSTMENT_PRICING_SEQ")
	private Integer id;
	@Column(name="CRITERIA")
	private Integer criteria;
	@Column(name="ID_PRICING_ADJUSTMENT")
	private Integer idPricingAdjustment;
	@Column(name="CREATE_DATE")
	private Date createdDate;
	@Column(name="CREATE_BY")
	private String createdBy;
	@Column(name="UPDATED_DATE")
	private Date updatedDate;
	@Column(name="UPDATED_BY")
	private String updatedBy;
}
