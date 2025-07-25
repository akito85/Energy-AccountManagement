package com.dbs.module.account.detail.serviceagreement.dto.helper;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class SaViewTosDTO implements Serializable {

    private Integer tosId;
    private Integer saTosId;
    private String tosName;
    private String description;
    private List<ViewTosDtlDTO> tosDetail;
}
