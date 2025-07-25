package com.dbs.database.crm.entities.usermanagement;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "AUDIT_TRAIL")
public class AUDIT_TRAIL implements Serializable {
    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUDIT_TRAIL_SEQ")
    @SequenceGenerator(sequenceName = "AUDIT_TRAIL_SEQ", allocationSize = 1, name = "AUDIT_TRAIL_SEQ")
    private Integer id;

    @Column(name = "OPERATION")
    private String operation;

    @Column(name = "TABLE_NAME")
    private String tableName;

    @Column(name = "DATA_ID")
    private String dataId;

    @Column(name = "OLD_VALUE")
    private String oldValue;

    @Column(name = "NEW_VALUE")
    private String newValue;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "USER_LEVEL")
    private String userLevel;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_DATE")
    private Date createdDate;
}
