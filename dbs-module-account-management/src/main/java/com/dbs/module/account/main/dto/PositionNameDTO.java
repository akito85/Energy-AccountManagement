package com.dbs.module.account.main.dto;

import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class PositionNameDTO implements Serializable {
    private String customerManagement;
    private String cc;
    private String sor;
}
