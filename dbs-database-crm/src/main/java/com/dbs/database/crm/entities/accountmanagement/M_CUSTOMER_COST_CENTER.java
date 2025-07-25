package com.dbs.database.crm.entities.accountmanagement;

import java.util.Date;
import lombok.Data;
import javax.persistence.*;

import com.dbs.common.base.entities.BaseEntities;

@Entity
@Data
@Table(name = "M_CUSTOMER_COST_CENTER")
public class M_CUSTOMER_COST_CENTER extends BaseEntities {

    @Id
    @Column(name = "ID", nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_CUSTOMER_COST_CENTER_SEQ")
    @SequenceGenerator(sequenceName = "M_CUSTOMER_COST_CENTER_SEQ",allocationSize = 1, name = "M_CUSTOMER_COST_CENTER_SEQ")
    private Integer customerCostCenterId;

    @Column(name = "CUSTOMER_ID")
    private Integer customerId;

    @Column(name = "COST_CENTER_ID")
    private Integer costCenterId;
    
    @Column(name = "M_CUSTOMER_ENTITY_ID")
    private Integer mCustomerEntityId;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "OWNER")
    private String owner;

    @Column(name = "DESCRIPTION")
    private String description;
}
