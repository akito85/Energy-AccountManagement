package com.dbs.common.library.utils.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ApproveRejectRequestDto implements Serializable {

    private static final long serialVersionUID = 2599369927093092236L;

    @NotNull(message = "id can't be null")
    private Integer id;

    @NotBlank(message = "remark can't be empty")
    private String remark;

    @NotNull(message = "approvalId can't be null")
    private Integer approvalId;

    @NotBlank(message = "action can't be empty")
    private String action;
}
