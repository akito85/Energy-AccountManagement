package com.dbs.database.crm.entities.usermanagement;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "LOG_RESET_PASSWORD")
public class LOG_RESET_PASSWORD implements Serializable {
    @Id
    @Column(name = "LOG_ID", nullable = false, updatable = false, unique = true)
    private String logId;

    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "START_DATE")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "END_DATE")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "LINK")
    private String link;

    @Column(name = "ACCESS_DATE")
    private String accessDate;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "REMARK")
    private String remark;
    
    @Column(name = "TYPE_RESET", length = 20)
    private String typeReset;

    public boolean isLinkExpired() {
        if (this.endDate == null) return false;

        long currentTime = System.currentTimeMillis();
        long lastChangedTime = this.endDate.getTime();

        return currentTime > lastChangedTime;
    }
}
