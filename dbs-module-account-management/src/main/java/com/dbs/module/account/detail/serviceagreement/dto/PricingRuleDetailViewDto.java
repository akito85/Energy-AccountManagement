package com.dbs.module.account.detail.serviceagreement.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class PricingRuleDetailViewDto implements Serializable {
	/**
	 * 
	 */

	private String createdBy;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createdDate;
	private Integer entityId;
	private String isDeleted;
	private int lineNumber;
	private String max;
	private double min;
	private String priceCode;
	private Integer priceCodeId;
	private Integer pricingRuleDetailId;
	private String updatedBy;
	private Date updatedDate;
	Integer pricingRuleId;
	private String currency;
	private String value;
	private String uom;
	private String description;

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
