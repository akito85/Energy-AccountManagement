package com.dbs.module.account.detail.serviceagreement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class ProductCriteriaFilterDto implements Serializable {
	/**
	 * 
	 */

	private Integer customer;
	private Integer budget;
	private Integer subDistrict;
	private Integer district;
	private Integer city;
	private Integer province;
	private Integer area;
	private Integer sor;
	private Integer industrialSector;
	private Integer product;
	private Integer gsizes;
	private Integer customerSegment;
	private Integer accountGroup;
	private Integer accountClass;
	private Integer serviceType;
	private Integer accountCategory;

	@JsonFormat(shape = Shape.NUMBER)
	private Boolean allCriteria;

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
