package com.dbs.module.account.detail.serviceagreement.dto;

import com.dbs.module.account.detail.serviceagreement.dto.createdto.SaDetailDTO;
import com.dbs.module.account.detail.serviceagreement.dto.createdto.SaInfoDTO;
import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class SaUpdateDraftDTO implements Serializable {
    // Boolean Validation
    private Integer saId;
    private Boolean isSubmit;
    // Data
    private SaInfoDTO saInfo;
    private SaDetailDTO saDetail;
    private Integer appHierId;
}
