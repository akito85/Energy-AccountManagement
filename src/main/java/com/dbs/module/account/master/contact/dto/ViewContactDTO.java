package com.dbs.module.account.master.contact.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class ViewContactDTO implements Serializable {
    
    private Integer id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String contactName;
    private Integer jobId;
    private String jobName;
    private Integer positionId;
    private String positionName;
    private String description;
    private String status;
    private Date createdDate;
    private String createdBy;
    private Date updateDate;
    private String updateBy;
    private List<ViewContactDetailsDTO> contactDetail;
    
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
