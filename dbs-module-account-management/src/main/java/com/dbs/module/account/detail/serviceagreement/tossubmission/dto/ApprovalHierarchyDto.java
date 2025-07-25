package com.dbs.module.account.detail.serviceagreement.tossubmission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("java:S1068")
public class ApprovalHierarchyDto implements Serializable {
    private static final long serialVersionUID = 7190633873656014896L;

    private String appHierCode;
    private String approvalName;
    private String approvalType;
    private String desc;
    private Integer appHierId;
    private Integer entityId;
}
