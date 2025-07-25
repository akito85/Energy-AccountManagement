package com.dbs.database.crm.entities.mastermanagement;

import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Data
@Entity
@Table(name = "R_PRICING_CRITERIA")
public class R_PRICING_CRITERIA {

	@Id
	@Column(name = "ID", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_PRICING_CRITERIA_SEQ")
	@SequenceGenerator(sequenceName = "R_PRICING_CRITERIA_SEQ", allocationSize = 1, name = "R_PRICING_CRITERIA_SEQ")
	private Integer id;
	@Column(name = "ID_PRICING")
	private Integer idPricing;
	@Column(name = "CRITERIA")
	private Integer criteria;
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "UPDATED_DATE")
	private Date updatedDate;
	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CRITERIA", referencedColumnName = "GLB_TYPE_VAL_ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
	private R_GLOBAL_TYPE_VALUE criteriaValue;

	@Transient
//	@JsonIgnore
	public String getCriteriaName() {
		if (criteriaValue != null)
			return getCriteriaValue().getName();
		else
			return null;
	}

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

/*
 * id number [PK] id_pricing number criteria varchar2 created_date date
 * created_by varchar2 updated_date date updated_by varchar2
 */
