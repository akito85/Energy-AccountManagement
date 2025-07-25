package com.dbs.module.account.detail.serviceagreement.dto.createdto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import com.dbs.common.base.utils.BooleanToYNStringConverter;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Convert;

@Data
@SuppressWarnings("java:S1068")
public class CalculationRuleViewDto implements Serializable {
	private String name;
	private Integer id;
	private Double value;
	private String uom;
	private String uomName;
	private Integer productVersionId;
	private Integer nameId;
	private String createdBy;
	private Date createdDate;
	private String updatedBy;
	private Date updatedDate;
	private String description;
	private Integer calculationType;
	private Integer flag;

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
