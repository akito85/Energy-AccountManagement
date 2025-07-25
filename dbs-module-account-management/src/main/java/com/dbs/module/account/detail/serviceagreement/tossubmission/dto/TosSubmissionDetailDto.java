package com.dbs.module.account.detail.serviceagreement.tossubmission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("java:S1068") 
public class TosSubmissionDetailDto implements Serializable {
    private static final long serialVersionUID = 5139911849255583623L;

    private Integer id;
    private Integer tosSubmissionId;
    private Integer attribute;
    private Integer value;
    private Integer unit;
    private Integer fromItem;
    private String status;
    private Date createdDate;
    private String createdBy;
    private Date updatedDate;
    private String updatedBy;
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
