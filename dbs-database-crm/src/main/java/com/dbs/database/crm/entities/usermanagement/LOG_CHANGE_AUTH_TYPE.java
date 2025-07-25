package com.dbs.database.crm.entities.usermanagement;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "LOG_CHANGE_AUTH_TYPE")
public class LOG_CHANGE_AUTH_TYPE implements Serializable {
    @Id
    @Column(name="LOG_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_CHANGE_AUTH_TYPE_SEQ")
    @SequenceGenerator(sequenceName = "LOG_CHANGE_AUTH_TYPE_SEQ", allocationSize = 1, name = "LOG_CHANGE_AUTH_TYPE_SEQ")
    private Integer logId;

    @Column(name="USER_ID", length = 100)
    private String userId;

    @Column(name="CREATED_DATE")
    private Date createdDate;

    @Column(name = "AUTH_FROM")
    private Integer authFrom;

    @Column(name = "AUTH_TO")
    private Integer authTo;

    @Column(name = "REMARK")
    private String remark;
}
