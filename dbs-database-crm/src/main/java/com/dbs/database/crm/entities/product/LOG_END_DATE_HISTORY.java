package com.dbs.database.crm.entities.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "LOG_END_DATE_HISTORY")
public class LOG_END_DATE_HISTORY implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "T_PRICING_END_DATE_HISTORY_SEQ")
	@SequenceGenerator(sequenceName = "T_PRICING_END_DATE_HISTORY_SEQ", allocationSize = 1, name = "T_PRICING_END_DATE_HISTORY_SEQ")
	private Integer id;

	@Column(name = "REFERENCE_ID")
	private Integer refId;

	@Column(name = "ACTION_BY")
	private String actionBy;
	
        @Column(name = "TYPE")
	private String type;

	@JsonFormat(pattern = "dd MMM yyyy")
	@Column(name = "END_DATE_BEFORE")
	private Date endDateBefore;
	
	@JsonFormat(pattern = "dd MMM yyyy")
	@Column(name = "END_DATE_AFTER")
	private Date endDateAfter;

	@Column(name = "ACTION_DATE")
	private Date actionDate;

        @Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "UPDATED_DATE")
	private Date updatedDate;

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
