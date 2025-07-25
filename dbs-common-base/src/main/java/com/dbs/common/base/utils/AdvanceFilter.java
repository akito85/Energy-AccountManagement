package com.dbs.common.base.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.Serializable;

@Data
public class AdvanceFilter implements Serializable {
    private static final long serialVersionUID = -8875250350831528005L;
    private String column;
    private String condition;
    private String operator;
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
