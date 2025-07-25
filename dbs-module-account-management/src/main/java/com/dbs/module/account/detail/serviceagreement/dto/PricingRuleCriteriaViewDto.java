package com.dbs.module.account.detail.serviceagreement.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class PricingRuleCriteriaViewDto implements Serializable {
	/**
	 * 
	 */
	private Integer pricingRuleCriteriaId;
	private String ccId;
	private String createdBy;
	private Date createdDate;

	private Integer criteria;

	private Integer entityId;

	private String isDeleted;

	private Integer pricingRuleId;

	private String updatedBy;

	private Date updatedDate;
	
	private Integer flag;
	
	private String criteriaName;

	@Override
	public String toString() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(this);
		} catch (Exception e) {
			e.printStackTrace();
			return getClass().getName();
		}
	}
}
