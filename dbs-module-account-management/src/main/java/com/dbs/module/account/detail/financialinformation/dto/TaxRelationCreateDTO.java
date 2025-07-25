package com.dbs.module.account.detail.financialinformation.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class TaxRelationCreateDTO implements Serializable {
     private Integer accountId;
     private Integer relatedAccountId;
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
