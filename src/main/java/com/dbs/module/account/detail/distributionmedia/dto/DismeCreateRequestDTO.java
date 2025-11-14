package com.dbs.module.account.detail.distributionmedia.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class DismeCreateRequestDTO implements Serializable {

    private Integer accountId;
    
    @NotNull(message = "Media not be null")
    private Integer productId;
    
    @NotEmpty(message = "Start date not be empty")
    private String startDate;
    
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
