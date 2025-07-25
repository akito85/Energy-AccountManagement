package com.dbs.common.library.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalDetailGroupDto implements Serializable {

    private static final long serialVersionUID = 3021827762940138267L;

    private Integer apphierId;
    private String position;
    private String approvalLevel;
    private List<EmployeeDetailDto> employeeDetail;
}
