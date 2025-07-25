package com.dbs.module.account.master.latecharge.dto;

import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class CriteriaListDTO implements Serializable{
    private Integer id;
    private Integer value;
    private String label;
    private String name;
}