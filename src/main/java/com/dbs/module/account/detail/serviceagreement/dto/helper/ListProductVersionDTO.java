package com.dbs.module.account.detail.serviceagreement.dto.helper;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class ListProductVersionDTO implements Serializable {
    private Integer accountId;
    private Integer productId;
    private Date saDate;
}
