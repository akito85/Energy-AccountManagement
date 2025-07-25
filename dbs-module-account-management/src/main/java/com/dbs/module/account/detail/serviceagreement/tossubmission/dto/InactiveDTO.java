package com.dbs.module.account.detail.serviceagreement.tossubmission.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class InactiveDTO implements Serializable {
    private Integer tosSubmissionId;
    private Integer appHierId;
    private String remark;
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
