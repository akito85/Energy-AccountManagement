package com.dbs.database.crm.entities.accountmanagement;


import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "R_TOS_CRITERIA_DATA")
public class R_TOS_CRITERIA_DATA extends DefaultBaseEntities{

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_CRITERIA_SEQ")
    @SequenceGenerator(sequenceName = "M_CRITERIA_SEQ",allocationSize = 1, name = "M_CRITERIA_SEQ")
    private Integer id;

    @Column(name = "id_tos")
    private Integer idTos;
    
    @Column(name = "gsizes")
    private Integer gsizes;
          
    @Column(name = "customer")
    private Integer customer;

    @Column(name = "BUDGET")
    private Integer budget;
//
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
    
    @Column(name = "ACCOUNT_CATEGORY")
    private Integer accountCategory;
    
    @Column(name = "SERVICE_TYPE")
    private Integer serviceType;
    
    @Column(name = "PRODUCT")
    private Integer product;
    
    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "ALL_CRITERIA")
    private Boolean allCriteria;

    @Column(name="START_DATE")
    private Date startDate;

    @Column(name="END_DATE")
    private Date endDate;
}
