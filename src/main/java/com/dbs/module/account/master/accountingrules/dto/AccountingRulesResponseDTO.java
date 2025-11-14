package com.dbs.module.account.master.accountingrules.dto;

import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class AccountingRulesResponseDTO {

    private Integer masterAccountingRuleId;
    private String classificationTypeName;
    private String code;
    private String receivableAccount;
    private String revenueAccount;
    private String description;
    private Integer entityId;
    private String createdBy;
    private String updatedBy;
    private String status;
    private Date createdDate;
    private Date updatedDate;
    private List<AUDIT_TRAIL> activeInactiveLog;
}
