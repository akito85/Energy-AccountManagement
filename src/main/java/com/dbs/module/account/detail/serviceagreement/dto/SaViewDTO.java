package com.dbs.module.account.detail.serviceagreement.dto;

import com.dbs.module.account.detail.serviceagreement.dto.helper.SaObjectMapperDTO;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068") 
public class SaViewDTO implements Serializable {

    private Integer id;
    private Integer accountId;
    private String saNumber;
    private String saReference;
    private SaObjectMapperDTO serviceType;
    private SaObjectMapperDTO saType;
    private SaObjectMapperDTO pjbgType;
    private Date saDate;
    private Date startDate;
    private Date endDate;
    private Date commitmentDate;
    private SaObjectMapperDTO billingCycle;
    private SaObjectMapperDTO termOfPayment;
    private SaObjectMapperDTO invoiceTemplate;
    private Date gasInPlanDate;
    private SaObjectMapperDTO lateCharge;
    private String status;
    private String approvalStatus;
    private String isMain;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;

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
