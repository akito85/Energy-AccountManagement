package com.dbs.common.library.entities;

import lombok.Data;

import java.io.Serializable;


@Data
@SuppressWarnings("java:S1068")
public class TriggerJsonAction implements Serializable {
    private String columnName;
    private String value;
}
