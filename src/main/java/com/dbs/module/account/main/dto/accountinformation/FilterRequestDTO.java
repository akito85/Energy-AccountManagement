package com.dbs.module.account.main.dto.accountinformation;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class FilterRequestDTO implements Serializable {
    private Integer column;
    private Integer condition;
    private Integer operator;
    private String value;

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
