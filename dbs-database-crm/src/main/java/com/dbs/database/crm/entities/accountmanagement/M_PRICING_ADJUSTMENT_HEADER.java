package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "M_PRICING_ADJUSTMENT_HEADER")
public class M_PRICING_ADJUSTMENT_HEADER extends BaseEntities implements Serializable {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_PRICING_ADJUSTMENT_HEADER_SEQ")
	@SequenceGenerator(sequenceName = "M_PRICING_ADJUSTMENT_HEADER_SEQ", allocationSize = 1, name = "M_PRICING_ADJUSTMENT_HEADER_SEQ")
	private Integer id;

	@Column(name = "ID_PRICING_ADJUSTMENT")
	private String pricingAdjustmentId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "M_PRICING_DETAIL_ID")
	private Integer mPricingDetailId;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "STATUS_APPROVAL")
	private String statusApproval;

	@Column(name = "ENTITY")
	private Integer entityId;

	@Column(name = "TRIGGER_JSON")
	private String triggerJson;

	@Column(name = "APPHIER_ID")
	private Integer appHierId;
	
	@Column(name = "CC_ID")
	private Integer ccId;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "ID_PRICING_ADJUSTMENT", referencedColumnName = "ID")
	@JsonProperty(value = "rcriteriaPricingAdjustments")
	private List<R_CRITERIA_ADJUSTMENT_PRICING> rCriteriaPricingAdjustments;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "ID_PRICING_ADJUSTMENT", referencedColumnName = "ID")
	@JsonProperty(value = "mpricingAdjustmentDetails")
	private List<R_PRICING_ADJUSTMENT_DETAIL> mPricingAdjustmentDetails;

	@Override
	public String toString() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(this);
		} catch (Exception e) {
			e.printStackTrace();
			return getClass().getName() + "#" + id;
		}
	}
}
