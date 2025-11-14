package com.dbs.module.account.detail.financialinformation.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class WithholdingTaxCreateDTO implements Serializable {

    private Integer accountId;
    
    private String description;

    private String startDate;
    
    private boolean wapuFlag;
    
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
