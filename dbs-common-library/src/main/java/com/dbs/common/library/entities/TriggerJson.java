package com.dbs.common.library.entities;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@SuppressWarnings("java:S1068")
public class TriggerJson implements Serializable {
    private String type;
    private String table;
    private String identifier;
    private String value;
    private List<TriggerJsonAction> action;
}
