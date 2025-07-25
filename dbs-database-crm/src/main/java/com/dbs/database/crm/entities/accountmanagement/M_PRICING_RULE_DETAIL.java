package com.dbs.database.crm.entities.accountmanagement;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dbs.common.base.utils.BooleanToYNStringConverter;

import lombok.Data;

@Data
@Entity
//@NamedQueries({ @NamedQuery(name = "MPricingRuleDetail.findAll", query = "select o from MPricingRuleDetail o") })
@Table(name = "M_PRICING_RULE_DETAIL")
public class M_PRICING_RULE_DETAIL implements Serializable {
	private static final long serialVersionUID = -1399672423886886458L;
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "ENTITY_ID")
	private Integer entityId;
	
	@Column(name = "IS_DELETED")
	private String isDeleted;
	
        @Column
	private int linenumber;
	
        @Column
	private double max;
	
        @Column
	private double min;
	
	@Column(name = "PRICE_CODE_ID")
	private Integer priceCodeId;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_PRICING_RULE_DETAIL_SEQ")
	@SequenceGenerator(sequenceName = "M_PRICING_RULE_DETAIL_SEQ", allocationSize = 1, name = "M_PRICING_RULE_DETAIL_SEQ")
	@Column(name = "PRICING_RULE_DETAIL_ID", nullable = false)
	private Integer pricingRuleDetailId;
	
	@Column(name = "UPDATED_BY")
	private String updatedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_DATE")
	private Date updatedDate;
	
	@Column(name = "PRICING_RULE_ID")
	Integer pricingRuleId;
	
	@Column(name = "IS_UNLIM")
	@Convert(converter = BooleanToYNStringConverter.class)
	Boolean isUnlim;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "START_DATE")
	private Date startDate;

	@Column(name = "END_DATE")
	private Date endDate;


	@ManyToOne
	@JoinColumn(name = "PRICING_RULE_ID", updatable = false, insertable = false)
	private M_PRICING_RULE mPricingRule;

}
