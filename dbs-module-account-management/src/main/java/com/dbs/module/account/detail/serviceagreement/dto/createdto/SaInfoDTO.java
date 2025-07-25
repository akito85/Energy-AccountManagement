package com.dbs.module.account.detail.serviceagreement.dto.createdto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class SaInfoDTO implements Serializable {

    private Integer accountId;
    private Boolean isMain;
    private Integer serviceType;
    private String saNumber;
    private Integer saType;
    private Integer pjbgType;
    private Date saDate;
    private Date startDate;
    private Date endDate;
    private Integer billingCycle;
    private Integer termOfPayment;
    private Integer invoiceTemplate;
    private Boolean alreadyGasIn;
    private Date gasInPlanDate;
    private Date commitmentDate;
    private String description;
    private String saReferenceNumber;

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
