package com.dbs.module.account.detail.serviceagreement.dto.helper;

import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class DdlMapperDTO implements Serializable {

    private Integer id;
    private String name;
    private String value;
}
