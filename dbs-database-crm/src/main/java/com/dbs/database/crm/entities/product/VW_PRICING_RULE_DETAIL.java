package com.dbs.database.crm.entities.product;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.dbs.common.base.utils.BooleanToYNStringConverter;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the VW_PRICING_RULE_DETAIL database table.
 */
@Data
@Entity
@Table(
        name = "VW_PRICING_RULE_DETAIL"
)
public class VW_PRICING_RULE_DETAIL implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "LINENUMBER")
    private Integer linenumber;

    @Column(name = "MAX")
    private String max;

    @Column(name = "MIN")
    private BigDecimal min;

    @Column(name = "PRICE_CODE")
    private String priceCode;

    @Column(name = "PRICE_CODE_ID")
    private Integer priceCodeId;

    @Id
    @Column(name = "IDS")
    private Integer ids;

    @Column(name = "PRICING_RULE_DETAIL_ID")
    private Integer pricingRuleDetailId;

    @Column(name = "PRICING_RULE_ID")
    private Integer pricingRuleId;

    @Column(name = "UOM")
    private String uom;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "IS_DELETED")
    private String isDeleted;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "IS_UNLIM")
    @Convert(converter = BooleanToYNStringConverter.class)
    Boolean isUnlim;

    @Column(name = "DESCRIPTION")
    String description;

//	@ManyToOne
//	@JoinColumn(name = "PRICING_RULE_ID", updatable = false, insertable = false)
//	@JsonIgnore
//	private M_PRICING_RULE mPricingRule;

}