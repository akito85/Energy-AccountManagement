package com.dbs.module.account.detail.distributionmedia.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class DismeInactiveRequestDTO {
    
    @NotNull(message = "Distribution Media not be null")
    private Integer distributionMediaId;
    
    @NotEmpty(message = "End date not be empty")
    private String endDate;
    
    @NotEmpty(message = "Remark not be empty")
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
