package com.dbs.database.crm.entities.payment;

import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNT;
import com.dbs.database.crm.entities.product.M_LOCATION;
import com.dbs.database.crm.entities.product.M_PRODUCT;
import com.dbs.database.crm.entities.usermanagement.M_COSTCENTER;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "R_PAY_ACCOUNT_BANK_CRITERIA_DATA")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class R_PAY_ACCOUNT_BANK_CRITERIA_DATA extends DefaultBaseEntities implements Serializable {

    private static final long serialVersionUID = -9038084069918044373L;

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_PAY_ACCOUNT_BANK_CRITERIA_DATA_SEQ")
    @SequenceGenerator(sequenceName = "R_PAY_ACCOUNT_BANK_CRITERIA_DATA_SEQ", allocationSize = 1, name = "R_PAY_ACCOUNT_BANK_CRITERIA_DATA_SEQ")
    private Long id;

    @Column(name = "ACCOUNT_INFORMATION_ID")
    private Long accountInformationId;

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

    @Column(name = "COST_CENTER")
    private Integer costCenter;

    @Column(name = "SOR")
    private Integer sor;

    @Column(name = "AREA")
    private Integer area;

    @Column(name = "INDUSTRIAL_SECTOR")
    private Integer industrialSector;

    @Column(name = "PRODUCT")
    private Integer product;

    @Column(name = "G_SIZES")
    private Integer gSizes;

    @Column(name = "CUSTOMER_SEGMENT")
    private Integer customerSegment;

    @Column(name = "ACCOUNT_GROUP")
    private Integer accountGroup;

    @Column(name = "ACCOUNT_CLASS")
    private Integer accountClass;

    @Column(name = "ACCOUNT_CATEGORY")
    private Integer accountCategory;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "ALL_CRITERIA")
    private Boolean allCriteria;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER", referencedColumnName = "ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private M_ACCOUNT customerValue;

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
    @JoinColumn(name = "ACCOUNT_GROUP", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE accountGroupName;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_CLASS", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
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
    @JoinColumn(name = "COST_CENTER", referencedColumnName = "CC_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private M_COSTCENTER costCenterName;

    @ManyToOne
    @JoinColumn(name = "G_SIZES", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE gSizesName;
}
