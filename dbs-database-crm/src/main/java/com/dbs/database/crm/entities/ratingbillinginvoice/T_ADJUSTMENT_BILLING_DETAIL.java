package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "T_ADJUSTMENT_BILLING_DETAIL")
public class T_ADJUSTMENT_BILLING_DETAIL implements Serializable {
	@Id
	@SequenceGenerator(name = "T_ADJUSTMENT_BILLING_DETAIL_SEQ", sequenceName = "T_ADJUSTMENT_BILLING_DETAIL_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "T_ADJUSTMENT_BILLING_DETAIL_SEQ")
	@Column(name = "ADJUSTMENT_DETAIL_ID")
	private Integer id;

	@Column(name = "ADJUSTMENT_ID")
	private Integer adjustmentId;

	@Column(name = "ITEM")
	private String item;

	@Column(name = "ITEM_CODE")
	private String itemCode;

	@Column(name = "QUANTITY")
	private Integer quantity;

	@Column(name = "PRICE")
	private Double price;

	@Column(name = "UOM")
	private String uom;

	@Column(name = "CURRENCY")
	private String currency;

	@Column(name = "AMOUNT")
	private Double amount;

	@Column(name = "TYPE")
	private Integer type;

	@Column(name = "ADJUSTMENT_AMOUNT")
	private Double adjustmentAmount;

	@Column(name = "TOTAL_AMOUNT")
	private Double totalAmount;

	@Column(name = "TOTAL_AMOUNT_EQV_IDR")
	private Double totalAmountEqvIdr;

	@Column(name = "TOTAL_AMOUNT_EQV_USD")
	private Double totalAmountEqvUsd;

	@Column(name = "REMARK")
	private String remark;

	@Column(name = "CREATED_BY")
	private String createdBy;

	//	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "UPDATED_DATE")
	private Date updatedDate;

	@Column(name = "IS_DELETED")
	@Convert(converter= BooleanToYNStringConverter.class)
	private Boolean isDeleted;

	@Column(name = "ENTITY_ID")
	private Integer entityId;

	@ManyToOne
	@JoinColumn(name = "ADJUSTMENT_ID", referencedColumnName = "ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private T_ADJUSTMENT_BILLING tAdjustmentBilling;

}
