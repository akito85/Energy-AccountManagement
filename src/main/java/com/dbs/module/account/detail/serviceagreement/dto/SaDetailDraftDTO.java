package com.dbs.module.account.detail.serviceagreement.dto;

import com.dbs.database.crm.entities.usermanagement.M_ATTACHMENT;
import com.dbs.module.account.detail.serviceagreement.dto.helper.SaInfoDetailDraftDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Data
@SuppressWarnings("java:S1068")
public class SaDetailDraftDTO implements Serializable {

    private Integer saId;
    private SaInfoDetailDraftDTO saInfo;
    private Optional<List<M_ATTACHMENT>> attachment;
    private Integer appHierId;
}
