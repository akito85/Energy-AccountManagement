package com.dbs.common.library.utils.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class ApprovalHistoryApproverDto implements Serializable {
	private Integer id;
	private String status;
	private String name;
	private String role;

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
