package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;

import lombok.Data;

import java.util.Date;

import javax.persistence.*;

@Entity
@Data
@Table(name = "M_CUSTOMER_ENTITY")
public class M_CUSTOMER_ENTITY extends BaseEntities{

    @Id
    @Column(name = "ID", nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_CUSTOMER_ENTITY_SEQ")
    @SequenceGenerator(sequenceName = "M_CUSTOMER_ENTITY_SEQ",allocationSize = 1, name = "M_CUSTOMER_ENTITY_SEQ")
    private Integer customerEntityId;

    @Column(name = "CUSTOMER_ID")
    private Integer customerId;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

     @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "OWNER")
    private String owner;

    @Column(name = "DESCRIPTION")
    private String description;
}
