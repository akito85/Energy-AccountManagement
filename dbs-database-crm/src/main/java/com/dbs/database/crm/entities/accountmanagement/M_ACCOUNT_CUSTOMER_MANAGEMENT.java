package com.dbs.database.crm.entities.accountmanagement;

import lombok.Data;

import javax.persistence.*;

import com.dbs.common.base.entities.BaseEntities;

import java.util.Date;

@Entity
@Data
@Table(name = "M_ACCOUNT_CUSTOMER_MANAGEMENT")
public class M_ACCOUNT_CUSTOMER_MANAGEMENT extends BaseEntities {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_ACCOUNT_CUSTOMER_MANAGEMENT_SEQ")
    @SequenceGenerator(sequenceName = "M_ACCOUNT_CUSTOMER_MANAGEMENT_SEQ",allocationSize = 1, name = "M_ACCOUNT_CUSTOMER_MANAGEMENT_SEQ")
    private Integer id;
    
    @Column(name = "ACCOUNT_ID")
    private Integer accountId;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "POSITION_ID")
    private Integer positionId;

    @Column(name = "START_DATE")
    private Date startDate;
 
    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "OWNER")
    private String owner;
}
