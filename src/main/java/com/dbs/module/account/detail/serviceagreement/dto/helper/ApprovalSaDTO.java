package com.dbs.module.account.detail.serviceagreement.dto.helper;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class ApprovalSaDTO implements Serializable {

    private Integer saId;
    private String description;
    private Integer approvalId;
    private String action;

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
