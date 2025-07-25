package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "M_RBI_BATCH_USAGE")
public class M_RBI_BATCH_USAGE extends BaseEntities implements Serializable {
    @Id
    @Column(name = "BATCH_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "B_USAGE_SEQ")
    @SequenceGenerator(sequenceName = "B_USAGE_SEQ", allocationSize = 1, name = "B_USAGE_SEQ")
    private Integer batchId;
    @Column(name = "TOTAL_USAGE")
    private Integer totalUsage;
	@Column(name = "UPLOAD_TYPE")
    private Integer uploadType;
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