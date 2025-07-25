package com.dbs.module.account.detail.serviceagreement.tossubmission.dto;

import com.dbs.database.crm.entities.accountmanagement.VW_TOS_SUBMISSION_DTL;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("java:S1068") 
public class TosSubmissionDto implements Serializable {
    private static final long serialVersionUID = 4283990231542778032L;

    private Integer saTosSubmissionId;
    private Integer saTosId;
    private String saTosName;
    private String status;
    private String statusApproval;
    private String remark;

    private Date startDate;

    private Date endDate;

    private Date appliedDate;
    private Date createdDate;
    private Date updatedDate;
    private String createdBy;
    private String updatedBy;
    private ApprovalHeaderViewDto approvalDetail;
    private Integer appHierId;
    private Integer tAppId;
    private Boolean isApprover;
    private String approvalType;

    private List<VW_TOS_SUBMISSION_DTL> tosSubmissionDetail;
    private List<AttachmentDto> mAttachments;
    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName();
        }
    }
}
