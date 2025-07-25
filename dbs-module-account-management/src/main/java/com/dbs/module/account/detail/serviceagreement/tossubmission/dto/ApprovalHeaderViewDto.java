package com.dbs.module.account.detail.serviceagreement.tossubmission.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class ApprovalHeaderViewDto implements Serializable {
    private Date requestedDate;
    private String requestedBy;
    private String approvalType;
    private String type;
    private String remarks;

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
