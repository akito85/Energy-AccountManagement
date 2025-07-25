package com.dbs.database.crm.entities.product;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import javax.persistence.*;


import java.util.Date;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "M_PRODUCT_CALCULATION_RULE")
public class M_PRODUCT_CALCULATION_RULE {

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_PRODUCT_CALCULATION_RULE_SEQ")
    @SequenceGenerator(sequenceName = "M_PRODUCT_CALCULATION_RULE_SEQ", allocationSize = 1, name = "M_PRODUCT_CALCULATION_RULE_SEQ")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NAME_ID")
    private Integer nameId;

    @Column(name = "VALUE")
    private BigDecimal value;

    @Column(name = "UOM")
    private String uom;

    @Column(name = "PRODUCT_VERSION_ID")
    private Integer productVersionId;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "DESCRIPTION")
    private String description;

    @Convert(converter = BooleanToYNStringConverter.class)
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @Column(name = "CALCULATION_TYPE")
    private Integer calculationType;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_VERSION_ID", referencedColumnName = "ID", updatable = false, insertable = false)
    @JsonIgnore
    private M_PRODUCT_VERSION mProductVersion;

//    @ManyToOne
//    @JoinColumn(name = "UOM", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
//    @JsonIgnore
//    @NotFound(action = NotFoundAction.IGNORE)
//    private R_GLOBAL_TYPE_VALUE uomValue;
//
//    @Transient
//    //@JsonIgnore
//    public String getUomName() {
//        if (uomValue != null) {
//            return getUomValue().getName();
//        } else {
//            return null;
//        }
//    }

}
