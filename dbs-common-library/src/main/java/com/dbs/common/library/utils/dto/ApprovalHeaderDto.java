package com.dbs.common.library.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalHeaderDto implements Serializable {

    private static final long serialVersionUID = -61330408621734776L;

    private String approvalName;
    private String approvalType;
    private String desc;
    private Integer appHierId;
    private Integer entityId;
}
