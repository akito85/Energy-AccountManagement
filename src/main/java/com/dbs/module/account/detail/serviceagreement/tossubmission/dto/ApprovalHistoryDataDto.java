package com.dbs.module.account.detail.serviceagreement.tossubmission.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class ApprovalHistoryDataDto implements Serializable {
    private Integer id;
    private String status;
    private String name;
    private String hierarchy;
    private String role;
    private Date taskDate;
    private Date actionDate;
    private String description;

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
