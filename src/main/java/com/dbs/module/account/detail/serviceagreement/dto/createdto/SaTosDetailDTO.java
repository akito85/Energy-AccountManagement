package com.dbs.module.account.detail.serviceagreement.dto.createdto;

import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class SaTosDetailDTO implements Serializable {
    private Integer attribute;
    private Integer unit;
    private Integer value;
    private Integer fromItem;
}
