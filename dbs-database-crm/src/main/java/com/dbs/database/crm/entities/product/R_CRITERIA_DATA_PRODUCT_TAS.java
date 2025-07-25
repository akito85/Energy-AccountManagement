package com.dbs.database.crm.entities.product;

import com.dbs.common.base.entities.DefaultBaseEntities;
import java.io.Serializable;
import javax.persistence.*;

import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNT;
import com.dbs.database.crm.entities.usermanagement.M_COSTCENTER;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import java.util.Date;

/**
 * The persistent class for the R_CRITERIA_DATA_PRODUCT_TAS database table.
 *
 */
@Entity
@Data
@Table(name = "R_CRITERIA_DATA_PRODUCT_TAS")
public class R_CRITERIA_DATA_PRODUCT_TAS extends DefaultBaseEntities implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "R_CRITERIA_DATA_PRODUCT_TAS_SEQ", sequenceName = "R_CRITERIA_DATA_PRODUCT_TAS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_CRITERIA_DATA_PRODUCT_TAS_SEQ")
    private Integer id;

    @Column(name = "DESCRIPTION")
    private String description;

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

    @Column(name = "AREA")
    private Integer area;

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

    @Column(name = "ACCOUNT_GROUP")
    private Integer accountGroup;

    @Column(name = "SERVICE_TYPE")
    private Integer serviceType;

    @Column(name = "ACCOUNT_CATEGORY")
    private Integer accountCategory;

    @Convert(converter = BooleanToYNStringConverter.class)
    @Column(name = "ALL_CRITERIA")
    private Boolean allCriteria;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "ID_PRODUCT_TAS")
    private Integer idProductTas;

    @Column(name = "IS_DELETED")
    @Convert(converter = BooleanToYNStringConverter.class)
    private Boolean isDeleted;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @ManyToOne
    @JoinColumn(name = "ID_PRODUCT_TAS", referencedColumnName = "ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private M_PRODUCT_TARGET_ACCOUNT_SELLING mProductTargetAccountSelling;

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
    @JoinColumn(name = "PRODUCT", referencedColumnName = "ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private M_PRODUCT productName;

    @ManyToOne
    @JoinColumn(name = "GSIZES", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE gSizesName;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_SEGMENT", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE customerSegmentName;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_GROUP", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE accountGroupName;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_CATEGORY", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE accountCategoryName;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER", referencedColumnName = "ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private M_ACCOUNT customerValue;

    @Transient
    public String getCustomerName() {
        if (customerValue != null) {
            return getCustomerValue().getAccountName();
        } else {
            return null;
        }
    }

    @ManyToOne
    @JoinColumn(name = "SERVICE_TYPE", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE serviceTypeValue;

    @Transient
//	@JsonIgnore
    public String getServiceTypeName() {
        if (serviceTypeValue != null) {
            return getServiceTypeValue().getName();
        } else {
            return null;
        }
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
