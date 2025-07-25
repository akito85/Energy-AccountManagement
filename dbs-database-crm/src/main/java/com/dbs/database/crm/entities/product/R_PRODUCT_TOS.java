package com.dbs.database.crm.entities.product;


import lombok.Data;

import javax.persistence.*;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Table(name = "R_PRODUCT_TOS")
public class R_PRODUCT_TOS {

    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "R_PRODUCT_TOS_SEQ")
    @SequenceGenerator(sequenceName = "R_PRODUCT_TOS_SEQ",allocationSize = 1, name = "R_PRODUCT_TOS_SEQ")
    private Integer id;

    @Column(name = "ID_M_PRODUCT_TOS")
    private Integer idMProductTos;

    @Column(name = "ATTRIBUTE")
    private String attribute;
    
    @Column
    private BigDecimal value;

    @Column(name = "UNIT")
    private String unit;

    @Column(name = "FROM_ITEM")
    private String fromItem;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;
    
    @Column(name = "IS_DELETED")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isDeleted;
    
    @ManyToOne
	@JoinColumn(name = "ID_M_PRODUCT_TOS", referencedColumnName = "ID", updatable = false, insertable = false)
	@JsonIgnore
	private M_PRODUCT_TERM_OF_SERVICE mProductTos;
    
    @ManyToOne
	@JoinColumn(name = "ATTRIBUTE", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
	@JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
	private R_GLOBAL_TYPE_VALUE attributeValue;

	@Transient
//	@JsonIgnore
	public String getAttributeName() {
		if (attributeValue != null)
			return getAttributeValue().getName();
		else
			return null;
	}

    @ManyToOne
    @JoinColumn(name = "UNIT", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE uomValue;

    @Transient
//	@JsonIgnore
    public String getUnitName() {
        if (uomValue != null)
            return getUomValue().getName();
        else
            return null;
    }
}
