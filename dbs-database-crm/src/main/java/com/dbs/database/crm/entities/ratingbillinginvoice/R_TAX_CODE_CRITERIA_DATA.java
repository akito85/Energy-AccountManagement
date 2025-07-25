package com.dbs.database.crm.entities.ratingbillinginvoice;


import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNT;
import com.dbs.database.crm.entities.product.M_LOCATION;
import com.dbs.database.crm.entities.product.M_PRODUCT;
import com.dbs.database.crm.entities.usermanagement.M_COSTCENTER;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

import javax.persistence.*;
import org.codehaus.jackson.map.ObjectMapper;
import java.util.Date;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Setter
@Getter
@Table(name = "R_TAX_CODE_CRITERIA_DATA")
public class R_TAX_CODE_CRITERIA_DATA extends DefaultBaseEntities implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "R_TAX_CODE_CRITERIA_DATA_SEQ")
    @SequenceGenerator(sequenceName = "R_TAX_CODE_CRITERIA_DATA_SEQ",allocationSize = 1, name = "R_TAX_CODE_CRITERIA_DATA_SEQ")
    private Integer id;
    
    @Column(name = "TAX_CODE_ID")
    private Integer taxCodeId;

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
    
    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "ALL_CRITERIA")
    private Boolean allCriteria;

    @Column(name="START_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;

    @Column(name="END_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;
    
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
    @JoinColumn(name = "ACCOUNT_GROUP", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE accountGroupName;

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
    @JoinColumn(name = "COST_CENTER", referencedColumnName = "CC_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private M_COSTCENTER costCenterName;

    @ManyToOne
    @JoinColumn(name = "GSIZES", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE gSizesName;
    
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.taxCodeId);
        hash = 89 * hash + Objects.hashCode(this.customer);
        hash = 89 * hash + Objects.hashCode(this.budget);
        hash = 89 * hash + Objects.hashCode(this.subDistrict);
        hash = 89 * hash + Objects.hashCode(this.district);
        hash = 89 * hash + Objects.hashCode(this.city);
        hash = 89 * hash + Objects.hashCode(this.province);
        hash = 89 * hash + Objects.hashCode(this.area);
        hash = 89 * hash + Objects.hashCode(this.sor);
        hash = 89 * hash + Objects.hashCode(this.industrialSector);
        hash = 89 * hash + Objects.hashCode(this.product);
        hash = 89 * hash + Objects.hashCode(this.gsizes);
        hash = 89 * hash + Objects.hashCode(this.customerSegment);
        hash = 89 * hash + Objects.hashCode(this.accountGroup);
        hash = 89 * hash + Objects.hashCode(this.serviceType);
        hash = 89 * hash + Objects.hashCode(this.accountCategory);
        hash = 89 * hash + Objects.hashCode(this.allCriteria);
        hash = 89 * hash + Objects.hashCode(this.startDate);
        hash = 89 * hash + Objects.hashCode(this.endDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final R_TAX_CODE_CRITERIA_DATA other = (R_TAX_CODE_CRITERIA_DATA) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.taxCodeId, other.taxCodeId)) {
            return false;
        }
        if (!Objects.equals(this.customer, other.customer)) {
            return false;
        }
        if (!Objects.equals(this.budget, other.budget)) {
            return false;
        }
        if (!Objects.equals(this.subDistrict, other.subDistrict)) {
            return false;
        }
        if (!Objects.equals(this.district, other.district)) {
            return false;
        }
        if (!Objects.equals(this.city, other.city)) {
            return false;
        }
        if (!Objects.equals(this.province, other.province)) {
            return false;
        }
        if (!Objects.equals(this.area, other.area)) {
            return false;
        }
        if (!Objects.equals(this.sor, other.sor)) {
            return false;
        }
        if (!Objects.equals(this.industrialSector, other.industrialSector)) {
            return false;
        }
        if (!Objects.equals(this.product, other.product)) {
            return false;
        }
        if (!Objects.equals(this.gsizes, other.gsizes)) {
            return false;
        }
        if (!Objects.equals(this.customerSegment, other.customerSegment)) {
            return false;
        }
        if (!Objects.equals(this.accountGroup, other.accountGroup)) {
            return false;
        }
        if (!Objects.equals(this.serviceType, other.serviceType)) {
            return false;
        }
        if (!Objects.equals(this.accountCategory, other.accountCategory)) {
            return false;
        }
        if (!Objects.equals(this.allCriteria, other.allCriteria)) {
            return false;
        }
        if (!Objects.equals(this.startDate, other.startDate)) {
            return false;
        }
        long lEndDate = 0;
        long lotherEndDate = 0;
        if (this.endDate != null) {
            lEndDate = this.endDate.getTime();
        }
        if (other.endDate != null) {
            lotherEndDate = other.endDate.getTime();
        }
        if (!Objects.equals(lEndDate, lotherEndDate)) {
            return false;
        }
        return true;
    }
    
    
}
