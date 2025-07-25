package com.dbs.module.account.detail.serviceagreement.tossubmission.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class CreateTosSubmissionRequest implements Serializable {
    private Integer id;
    private Integer saTosId;
    private Integer saId;
    private String description;
    private String startDate;
    private String endDate;
    private String appliedDate;
    private List<TosSubmissionDetailDto> tosSubmissionDetailDtoList;
    private List<AttachmentDto> mAttachments;
    private Integer appHierId;
    private int flag;// 1 = SAVE DRAFT, 2 = SAVE & SUBMIT
}
