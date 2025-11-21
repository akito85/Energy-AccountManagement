package com.dbs.module.account.detail.distributionmedia.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class DismeUpdateRequestDTO {
    
    @NotNull(message = "Distribution Media not be null")
    private Integer distributionMediaId;
    
    @NotNull(message = "Account Id not be null")
    private Integer accountId;
    
    @NotNull(message = "productVersionId not be null")
    private Integer productVersionId;
    
    @NotEmpty(message = "Start date not be empty")
    private String startDate;
    
    @NotEmpty(message = "description not be empty")
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
