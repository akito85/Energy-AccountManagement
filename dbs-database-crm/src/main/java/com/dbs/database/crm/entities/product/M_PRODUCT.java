package com.dbs.database.crm.entities.product;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.database.crm.entities.usermanagement.M_COSTCENTER;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "M_PRODUCT")
public class M_PRODUCT implements Serializable {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_PRODUCT_SEQ")
    @SequenceGenerator(sequenceName = "M_PRODUCT_SEQ", allocationSize = 1, name = "M_PRODUCT_SEQ")
    private Integer id;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRODUCT_DESCRIPTION")
    private String productDescription;

    @Column(name = "PRODUCT_TYPE")
    private Integer productType;

    @Column(name = "SERVICE_TYPE")
    private Integer serviceType;

    @Column(name = "PRODUCT_CLASS")
    private Integer productClass;

    @Convert(converter = BooleanToYNStringConverter.class)
    @Column(name = "LOCK_STATUS")
    private Boolean lockStatus;

    @Column(name = "CCID")
    private Integer ccId;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "LAST_VERSION")
    private Integer lastVersion;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "LOCKED_BY")
    private String lockedBy;

    @Column(name = "MAKER_POSITION")
    private String makerPosition;

    @Column(name = "PRODUCT_NUMBER", length = 10)
    private String productNumber;

    @Column(name = "APPROVAL_STATUS")
    private String approvalStatus;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_CLASS", referencedColumnName = "PRODUCT_CLASS_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private M_PRODUCT_CLASS mProductClass;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_TYPE", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE productTypeValue;

    @ManyToOne
    @JoinColumn(name = "SERVICE_TYPE", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE serviceTypeValue;

    @ManyToOne
    @JoinColumn(name = "CCID", referencedColumnName = "CC_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private M_COSTCENTER costCenterValue;

    @OneToMany(mappedBy = "mProduct", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<M_PRODUCT_VERSION> productVersion;

    @Transient
    @JsonIgnore
    public String getProductClassName() {
        if (mProductClass != null) {
            return getMProductClass().getName();
        } else {
            return null;
        }
    }

    @Transient
    @JsonIgnore
    public String getProductTypeName() {
        if (productTypeValue != null) {
            return getProductTypeValue().getName();
        } else {
            return null;
        }
    }

    @Transient
    @JsonIgnore
    public String getServiceTypeName() {
        if (serviceTypeValue != null) {
            return getServiceTypeValue().getName();
        } else {
            return null;
        }
    }

    @Transient
    @JsonIgnore
    public Integer getProductVersionId() {
        if (productVersion != null) {
            return getProductVersion().stream().max(Comparator.comparing(M_PRODUCT_VERSION::getVersion)).get().getId();
        } else {
            return null;
        }
    }

    @Transient
    @JsonIgnore
    @DateTimeFormat(pattern = "dd MMM yyyy")
    public Date getVersionStartDate() {
        if (productVersion != null) {
            return getProductVersion().stream().max(Comparator.comparing(M_PRODUCT_VERSION::getVersion)).get().getStartDate();
        } else {
            return null;
        }
    }

    @Transient
    @JsonIgnore
    @DateTimeFormat(pattern = "dd MMM yyyy")
    public Date getVersionEndDate() {
        if (productVersion != null) {
            return getProductVersion().stream().max(Comparator.comparing(M_PRODUCT_VERSION::getVersion)).get().getEndDate();
        } else {
            return null;
        }
    }

    @Transient
    @JsonIgnore
    public String getCcName() {
        if (costCenterValue != null) {
            return getCostCenterValue().getName();
        } else {
            return null;
        }
    }

}
