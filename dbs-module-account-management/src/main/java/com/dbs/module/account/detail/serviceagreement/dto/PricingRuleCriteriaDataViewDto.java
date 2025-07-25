package com.dbs.module.account.detail.serviceagreement.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.Date;
import java.util.HashMap;

@Data
@SuppressWarnings("java:S1068")
public class PricingRuleCriteriaDataViewDto {
	/**
	 * 
	 */
	private Integer id;

	private String criteriaValue;

	private HashMap<String,Object> customer;
	private HashMap<String,Object> budget;
	private HashMap<String,Object> subDistrict;
	private HashMap<String,Object> district;
	private HashMap<String,Object> city;

	private HashMap<String,Object> province;
	private HashMap<String,Object> area;
	private HashMap<String,Object> sor;
	private HashMap<String,Object> industrialSector;
	private HashMap<String,Object> product;
	private HashMap<String,Object> gsizes;
	private HashMap<String,Object> customerSegment;
	private HashMap<String,Object> accountGroup;

	private HashMap<String,Object> serviceType;
	private HashMap<String,Object> accountCategory;

	private HashMap<String,Object> allCriteria;
	private Integer idPricingRule;
	private Integer flag;
	private Date createdDate;
	private Date updatedDate;
	private String createdBy;
	private String updatedBy;

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
