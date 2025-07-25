package com.dbs.module.account.detail.serviceagreement.dto;

import com.dbs.module.account.detail.serviceagreement.dto.createdto.SaInfoDTO;
import com.dbs.module.account.detail.serviceagreement.dto.createdto.SaDetailDTO;
import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068") 
public class SaCreateDTO implements Serializable {

    // Boolean Validation
    private Boolean isDraft;
    // Data
    private SaInfoDTO saInfo;
    private SaDetailDTO saDetail;
    private Integer appHierId;
}
