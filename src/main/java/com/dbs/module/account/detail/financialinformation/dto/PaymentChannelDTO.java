package com.dbs.module.account.detail.financialinformation.dto;

import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class PaymentChannelDTO implements Serializable {
    private Integer paymentChannelType;
    private Boolean virtualAccount;
}
