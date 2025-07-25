package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.entities.DefaultBaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_LIST_BATCH")
public class VW_LIST_BATCH extends BaseEntities {
    @Id
    @Column(name = "BATCH_ID")
    private Integer batchId;
    @Column(name = "TOTAL_USAGE")
    private Integer totalUsage;
    @Column(name = "UPLOAD_TYPE")
    private String uploadType;
    @Column(name = "UPLOAD_TYPE_ID")
    private Integer uploadTypeId;
    @Column(name = "UPLOAD_BY")
    private String uploadBy;
    @Column(name = "BEGIN_STAND")
    private Integer beginStand;
    @Column(name = "APPHIER_ID")
    private Integer apphierId;
    @Column(name = "UPLOAD_DATE")
    private Date uploadDate;
    @Column(name = "TOTAL_FAILED")
    private Integer totalFailed;
    @Column(name = "TOTAL_PROGRESS")
    private Integer totalProgress;
    @Column(name = "TOTAL_SUCCEED")
    private Integer totalSucceed;
    @Column(name = "CC_ID")
    private Integer ccId;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
}
