package com.dbs.module.account.detail.premise.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class ServicePointCreateDTO implements Serializable {

    @NotNull(message = "Premise Address cannot be null!")
    private Integer accountAddressId;

    @NotNull(message = "Service Point Name cannot be null!")
    private Integer servicePointName;

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
