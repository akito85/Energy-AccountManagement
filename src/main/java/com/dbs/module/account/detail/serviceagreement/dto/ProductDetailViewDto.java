package com.dbs.module.account.detail.serviceagreement.dto;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.Convert;
import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class ProductDetailViewDto implements Serializable {
	private Integer id;
	private String name;
	private Double value;
	private String uom;
	private String unitName;
	private Integer productVersionId;
	private Integer nameId;
	private String createdBy;
	private Date createdDate;
	private String updatedBy;
	private Date updatedDate;
	private String description;
	private Integer flag;
	private Integer paymentType;
	private Integer chargingMethod;

	@Convert(converter = BooleanToYNStringConverter.class)
	private boolean isDeleted;

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
