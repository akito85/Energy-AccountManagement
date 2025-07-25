package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "GA_HISTORY")
public class GA_HISTORY extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GA_HISTORY_SEQ")
    @SequenceGenerator(sequenceName = "GA_HISTORY_SEQ", allocationSize = 1, name = "GA_HISTORY_SEQ")
    private Integer id;

    @Column(name = "USER_GA_ID")
    private Integer userGaId;

    @Column(name = "USER_ID", length = 10)
    private Integer userId;

    @Column(name = "GA_ID")
    private Integer gaId;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;
}
