package com.dbs.module.account.detail.financialinformation.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class TaxIdentifierCreateDTO implements Serializable {
    
    private Integer accountId;

    @NotNull(message = "Tax identifier type cannot be null!")
    private Integer taxIdentifierType;

    @NotEmpty(message = "Tax identifier name cannot be empty!")
    private String taxIdentifierName;

    @NotEmpty(message = "Tax identifier number cannot be empty!")
    private String taxIdentifierNumber;

    private Integer taxIdentifierAddress;
    private String startDate;
    private String description;
    private String taxAddress;
    
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
