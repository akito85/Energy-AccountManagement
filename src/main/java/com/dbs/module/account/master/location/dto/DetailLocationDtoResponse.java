package com.dbs.module.account.master.location.dto;

import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import com.dbs.module.account.main.dto.NameValueDdlResponseDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class DetailLocationDtoResponse {
    private Integer id;
    private NameValueDdlResponseDto locationType;
    private NameValueDdlResponseDto locationParentType;
    private NameValueDdlResponseDto locationParent;
    private NameValueDdlResponseDto locationReference;

    private String locationCode;
    private String locationName;

    private String status;

    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;

    private List<AUDIT_TRAIL> activeInactiveLog;

}
