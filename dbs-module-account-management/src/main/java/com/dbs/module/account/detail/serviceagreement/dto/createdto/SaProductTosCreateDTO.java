package com.dbs.module.account.detail.serviceagreement.dto.createdto;

import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class SaProductTosCreateDTO {
    private Integer tosId;
    private String tosName;
    private String description;
    private List<SaTosDetailDTO> tosDetail;
}
