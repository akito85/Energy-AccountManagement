package com.dbs.database.crm.entities.mastermanagement;

import com.dbs.common.base.utils.CustomddMMMyyyyDeserializer;
import com.dbs.common.base.utils.CustomddMMMyyyySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.codehaus.jackson.map.ObjectMapper;

import lombok.Data;

@Entity
@Data
@Table(name = "R_PRICING_DETAIL")
public class R_PRICING_DETAIL {
	@Id
        @Column(name = "ID",nullable = false,updatable = false)
        @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "R_PRICING_DETAIL_SEQ")
        @SequenceGenerator(sequenceName = "R_PRICING_DETAIL_SEQ",allocationSize = 1, name = "R_PRICING_DETAIL_SEQ")
	private Integer id;
	@Column(name = "ID_PRICING_NUMBER")
	private Integer idPricing;
	@Column(name = "CURRENCY")
	private String currency;
	@Column(name = "VALUE")
	private BigDecimal value;
	@Column(name = "UOM")
	private String uom;
        @JsonSerialize(using = CustomddMMMyyyySerializer.class)
        @JsonDeserialize(using = CustomddMMMyyyyDeserializer.class)
	@Column(name = "START_DATE")
	private Date startDate;
        @JsonSerialize(using = CustomddMMMyyyySerializer.class)
        @JsonDeserialize(using = CustomddMMMyyyyDeserializer.class)
	@Column(name = "END_DATE")
	private Date endDate;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "UPDATED_DATE")
	private Date updatedDate;
	@Column(name = "UPDATED_BY")
	private String updatedBy;
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
//		  id number [PK]
//		  id_pricing number
//		  currency varchar2 //IDR USD 
//		  value double 
//		  UOM varchar2 //M3 MBTU
//		  start_date date
//		  end_date date
//		  description varchar2
//		  created_date date
//		  created_by varchar2
//		  updated_date date
//		  updated_by varchar2