package com.dbs.module.account.master.latecharge.dto;

import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class ViewDetailLateChargeDto {
    private Integer lateChargeId;
    private String lateChargeName;
    private Object currency;
    private List<CriteriaListDTO> criteria;
    private String description;
    private String status;
    private HistoryLogInformationDto historyLogInformation;
    private List<CriteriaDataLatechargeDTO> lateChargeCriterias;
    private Boolean isRuleActive;
    private List<AUDIT_TRAIL> activeInactiveLog;

}
