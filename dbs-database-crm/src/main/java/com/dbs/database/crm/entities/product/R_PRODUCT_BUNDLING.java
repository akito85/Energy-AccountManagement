package com.dbs.database.crm.entities.product;

import lombok.Data;

import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

@Entity
@Data
@Table(name = "R_PRODUCT_BUNDLING")
public class R_PRODUCT_BUNDLING {

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_PRODUCT_BUNDLING_SEQ")
    @SequenceGenerator(sequenceName = "R_PRODUCT_BUNDLING_SEQ", allocationSize = 1, name = "R_PRODUCT_BUNDLING_SEQ")
    private Integer id;

    @Column(name = "PRODUCT_ID")
    private Integer productId;

    @Column(name = "PRODUCT_VERSION_MAIN_ID")
    private Integer productVersionMainId;

    @Column(name = "DISCOUNT_TYPE")
    private String discountType;

    @Column(name = "DISCOUNT_AMOUNT")
    private Double discountAmount;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "IS_DELETED")
    @Convert(converter = BooleanToYNStringConverter.class)
    private Boolean isDeleted;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_VERSION_MAIN_ID", referencedColumnName = "ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private M_PRODUCT_VERSION mProductVersion;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private M_PRODUCT mProduct;

    @Transient
// 	@JsonIgnore
    public String getProductName() {
        if (mProduct != null) {
            return getMProduct().getProductName();
        } else {
            return null;
        }
    }

    @ManyToOne
    @JoinColumn(name = "DISCOUNT_TYPE", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE discountTypeValue;

    @Transient
// 	@JsonIgnore
    public String getDiscountTypeName() {
        if (discountTypeValue != null) {
            return getDiscountTypeValue().getName();
        } else {
            return null;
        }
    }
}
