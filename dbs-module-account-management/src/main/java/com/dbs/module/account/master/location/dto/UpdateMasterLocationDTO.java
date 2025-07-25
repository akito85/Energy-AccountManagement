package com.dbs.module.account.master.location.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class UpdateMasterLocationDTO implements Serializable {

    @NotNull(message = "Location Type cannot be null")
    private Integer locationType;

    @NotEmpty(message = "Location Code cannot be empty")
    private String locationCode;

    @NotEmpty(message = "Location Name cannot be empty")
    private String locationName;

    private Integer locationParentType;
    private Integer locationParent;
    private Integer locationReference;

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
