package com.dbs.database.crm.entities.accountmanagement;


import com.dbs.common.base.utils.BooleanToYNStringConverter;
import java.util.Date;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "M_CRITERIA")
public class M_CRITERIA {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_CRITERIA_SEQ")
    @SequenceGenerator(sequenceName = "M_CRITERIA_SEQ",allocationSize = 1, name = "M_CRITERIA_SEQ")
    private Integer id;

    @Column(name = "MODUL_TYPE")
    private String modulType;

    @Column(name = "REFERENCE_ID")
    private Integer referenceId;

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

    @Column(name = "CUSTOMER_SEGMENT")
    private Integer customerSegment;
    
    @Column(name = "ACCOUNT_GROUP")
    private Integer accountGroup;
    
    @Column(name = "ACCOUNT_CLASS")
    private Integer accountClass;
    
    @Column(name = "ACCOUNT_CATEGORY")
    private Integer accountCategory;
    
    @Column(name = "PRODUCT")
    private Integer product;
    
    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "ALL_CRITERIA")
    private Boolean allCriteria;
    
    @Column(name="CREATED_BY")
    private String createdBy;

    @Column(name="UPDATED_BY")
    private String updatedBy;

    @Column(name="CREATED_DATE")
    private Date createdDate;

    @Column(name="UPDATED_DATE")
    private Date updatedDate;
}
