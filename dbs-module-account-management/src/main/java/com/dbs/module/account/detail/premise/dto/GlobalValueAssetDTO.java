package com.dbs.module.account.detail.premise.dto;

import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class GlobalValueAssetDTO implements Serializable {
    private Integer id;
    private String value;
    private String name;
}
