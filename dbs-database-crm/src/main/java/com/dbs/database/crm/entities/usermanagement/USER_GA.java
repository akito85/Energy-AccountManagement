package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "T_USER_GROUPACCESS")
public class USER_GA extends BaseEntities implements Serializable {
    @Id
    @Column(name = "USER_GA_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "T_USER_GROUPACCESS_SEQ")
    @SequenceGenerator(sequenceName = "T_USER_GROUPACCESS_SEQ", allocationSize = 1, name = "T_USER_GROUPACCESS_SEQ")
    private Integer userGaId;

    @Column(name = "USER_ID", length = 10)
    private Integer userId;

    @Column(name = "GA_ID")
    private Integer gaId;

    @Column
    private Date startDate;

    @Column
    private Date endDate;
}
