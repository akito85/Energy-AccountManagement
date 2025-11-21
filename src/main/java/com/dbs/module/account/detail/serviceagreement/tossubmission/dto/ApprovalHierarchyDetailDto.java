package com.dbs.module.account.detail.serviceagreement.tossubmission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("java:S1068")
public class ApprovalHierarchyDetailDto {

    private Integer apphierId;
    private String position;
    private String approvalLevel;
    private List<HashMap<String, Object>> employeeDetail;
}
