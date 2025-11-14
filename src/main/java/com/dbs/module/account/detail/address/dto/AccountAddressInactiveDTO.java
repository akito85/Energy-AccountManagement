package com.dbs.module.account.detail.address.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

@Data
@SuppressWarnings("java:S1068")
public class AccountAddressInactiveDTO {
    
    private Integer id;
    
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
