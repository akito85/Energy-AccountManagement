package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_LOGIN_BACKGROUND")
public class VW_LOGIN_BACKGROUND extends BaseEntities implements Serializable {
    @Id
    @Column(name = "LOGIN_BACKGROUND_ID")
    private Integer loginBackgroundId;

    @Column(name = "BACKGROUND_NAME")
    private String backgroundName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "START_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;

    @Column(name = "END_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;

    @Column(name = "IMAGE")
    private String image;

}
