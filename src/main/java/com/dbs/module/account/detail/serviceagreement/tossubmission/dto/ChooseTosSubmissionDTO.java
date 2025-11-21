package com.dbs.module.account.detail.serviceagreement.tossubmission.dto;

import com.dbs.database.crm.entities.accountmanagement.VW_TOS_CATALOG_DTL;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class ChooseTosSubmissionDTO implements Serializable {
    private Integer saId;
    private Integer saTosId;
    private String saTosName;
    private String saTosDescription;
    private Integer saTosSubmissionId;
    private List<VW_TOS_CATALOG_DTL> saTosDetail;
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
