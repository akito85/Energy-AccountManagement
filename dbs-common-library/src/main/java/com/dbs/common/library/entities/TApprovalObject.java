package com.dbs.common.library.entities;

import com.dbs.common.library.utils.ApprovalType;
import lombok.Data;

import java.io.Serializable;


@Data
@SuppressWarnings("java:S1068")
public class TApprovalObject implements Serializable {
    private String idTrans;
    private Integer appHierId;
    private ApprovalType approvalType;
    private String category;
    private String description;
    private String jsonString;
}
