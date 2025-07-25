package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import lombok.*;
import lombok.experimental.Accessors;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "M_GENERAL_TEMPLATE")
@NoArgsConstructor
public class M_GENERAL_TEMPLATE extends DefaultBaseEntities implements Serializable {
    @Id
    @Column(name = "TEMPLATE_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_GENERAL_TEMPLATE_SEQ")
    @SequenceGenerator(sequenceName = "M_GENERAL_TEMPLATE_SEQ", allocationSize = 1, name = "M_GENERAL_TEMPLATE_SEQ")
    private Integer templateId;
    @Column(name = "TEMPLATE_NAME")
    private String templateName;
    @Column(name = "PATH")
    private String path;
    @Column(name = "START_DATE")
    private Date startDate;
    @Column(name = "END_DATE")
    private Date endDate;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;
    @Column(name = "APPROVAL_HIERARCHY")
    private Integer approvalHierarchy;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name = "TRIGGER_JSON")
    private String triggerJson;
    @Column(name = "FILE_TYPE")
    private String fileType;
    @Column(name = "FILE_NAME")
    private String fileName;
    @Column(name = "FILE_SIZE")
    private long fileSize;
    @Column(name = "CC_ID")
    private Integer ccId;

    @ManyToOne
    @JoinColumn(name = "TEMPLATE_TYPE")
    private R_GLOBAL_TYPE_VALUE templateType;
}
