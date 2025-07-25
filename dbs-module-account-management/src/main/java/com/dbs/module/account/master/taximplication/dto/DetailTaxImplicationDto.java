package com.dbs.module.account.master.taximplication.dto;

import com.dbs.module.account.master.latecharge.dto.CriteriaListDTO;
import com.dbs.module.account.master.latecharge.dto.GlobalTypeDTO;
import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class DetailTaxImplicationDto {
    private Integer taxImplicationId;
    private String name;
    private String description;
    private String status;
    private GlobalTypeDTO category;
    private GlobalTypeDTO serviceType;
    private List<CriteriaListDTO> criteria;
    private HistoryLogTaxImplicationDto historyLog;
    private List<CriteriaDataTaxImpliDTO> taxImplicationCriterias;
    private Boolean isRuleActive;
    private List<AUDIT_TRAIL> activeInactiveLog;
}
