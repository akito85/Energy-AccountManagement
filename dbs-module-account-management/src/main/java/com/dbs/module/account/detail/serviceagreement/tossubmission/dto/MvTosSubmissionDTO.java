package com.dbs.module.account.detail.serviceagreement.tossubmission.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class MvTosSubmissionDTO implements Serializable {
    private Integer id;
    private Integer saId;
    private String saNumber;
    private String tosName;
    private Integer tosNameId;
    private String appliedDate;
    private String startDate;
    private String endDate;
    private String status;
    private String statusApproval;
    private String remark;
    private Date createdDate;
    private Date updatedDate;
    private String createdBy;
    private String updatedBy;

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
