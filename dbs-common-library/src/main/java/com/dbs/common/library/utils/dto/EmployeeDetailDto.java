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
public class EmployeeDetailDto implements Serializable {

    private static final long serialVersionUID = -6732638322065213731L;

    private Integer apphierId;
    private String employeeName;
    private Integer employeeId;
}
