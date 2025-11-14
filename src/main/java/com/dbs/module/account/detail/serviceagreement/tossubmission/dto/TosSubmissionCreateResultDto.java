package com.dbs.module.account.detail.serviceagreement.tossubmission.dto;

import com.dbs.database.crm.entities.accountmanagement.T_AM_TOS_SUBMISSION_DTL;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("java:S1068") 
public class TosSubmissionCreateResultDto implements Serializable {
    private static final long serialVersionUID = 6728850939414836859L;

    private Integer id;
    private Integer saTosId;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    private Date endDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    private Date appliedDate;

    private List<T_AM_TOS_SUBMISSION_DTL> tosSubmissionDtls;
}
