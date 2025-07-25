package com.dbs.database.crm.entities.usermanagement.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_T_USER_GROUPACCESS")
public class VW_T_USER_GROUPACCESS {
    @Id
    @Column(name = "USER_GA_ID")
    private Integer userGaId;

    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "GA_ID")
    private Integer gaId;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

}
