package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNT;
import com.dbs.database.crm.entities.product.M_LOCATION;
import com.dbs.database.crm.entities.product.M_PRODUCT;
import com.dbs.database.crm.entities.usermanagement.M_COSTCENTER;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "M_RBI_BILLING_BUCKET_CRITERIA_DATA")
public class M_RBI_BILLING_BUCKET_CRITERIA_DATA extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_RBI_BILLING_BUCKET_CRITERIA_DATA_SEQ")
    @SequenceGenerator(sequenceName = "M_RBI_BILLING_BUCKET_CRITERIA_DATA_SEQ", allocationSize = 1, name = "M_RBI_BILLING_BUCKET_CRITERIA_DATA_SEQ")
    private Integer id;
    @Column(name = "BILLING_BUCKET_CODE")
    private String billingBucketCode;
    @Column(name = "START_DATE")
    private Date startDate;
    @Column(name = "END_DATE")
    private Date endDate;
    @Column(name = "CUSTOMER")
    private Integer customer;
    @Column(name = "BUDGET")
    private Integer budget;
    @Column(name = "SUB_DISTRICT")
    private Integer subDistrict;
    @Column(name = "DISTRICT")
    private Integer district;
    @Column(name = "CITY")
    private Integer city;
    @Column(name = "PROVINCE")
    private Integer province;
    @Column(name = "SOR")
    private Integer sor;
    @Column(name = "INDUSTRIAL_SECTOR")
    private Integer industrialSector;
    @Column(name = "PRODUCT")
    private Integer product;
    @Column(name = "GSIZES")
    private Integer gsizes;
    @Column(name = "CUSTOMER_SEGMENT")
    private Integer customerSegment;
    @Column(name = "ACCOUNT_GROUP_TYPE")
    private Integer accountGroupType;
    @Column(name = "ACCOUNT_CLASS")
    private Integer accountClass;
    @Column(name = "ACCOUNT_CATEGORY")
    private Integer accountCategory;
    @Column(name = "SERVICE_TYPE")
    private Integer serviceType;
    @Column(name = "AREA")
    private Integer area;
    @Column(name = "all_criteria")
    private Character allCriteria;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER", referencedColumnName = "ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private M_ACCOUNT customerValue;

    @Transient
    public String getCustomerName() {
        if (customerValue != null)
            return getCustomerValue().getAccountName();
        else
            return null;
    }

    @ManyToOne
    @JoinColumn(name = "BUDGET", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE budgetName;

    @ManyToOne
    @JoinColumn(name = "SUB_DISTRICT", referencedColumnName = "LOCATION_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private M_LOCATION subDistrictName;

    @ManyToOne
    @JoinColumn(name = "DISTRICT", referencedColumnName = "LOCATION_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private M_LOCATION districtName;

    @ManyToOne
    @JoinColumn(name = "CITY", referencedColumnName = "LOCATION_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private M_LOCATION cityName;

    @ManyToOne
    @JoinColumn(name = "PROVINCE", referencedColumnName = "LOCATION_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private M_LOCATION provinceName;

    @ManyToOne
    @JoinColumn(name = "AREA", referencedColumnName = "CC_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private M_COSTCENTER areaName;

    @ManyToOne
    @JoinColumn(name = "SOR", referencedColumnName = "CC_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private M_COSTCENTER sorName;

    @ManyToOne
    @JoinColumn(name = "INDUSTRIAL_SECTOR", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE industrialSectorName;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_SEGMENT", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE customerSegmentName;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_GROUP_TYPE", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE accountGroupTypeName;

    @ManyToOne
    @JoinColumn(name = "SERVICE_TYPE", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE serviceTypeValue;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_CATEGORY", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE accountCategoryName;

    @ManyToOne
    @JoinColumn(name = "PRODUCT", referencedColumnName = "ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private M_PRODUCT productName;

    @ManyToOne
    @JoinColumn(name = "GSIZES", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE gSizesName;
}
