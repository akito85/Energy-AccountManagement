package com.dbs.common.library.utils.dto;

import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class UtilsContactDetailsCreateRequestDTO implements Serializable {

    private Integer contactDetailId;
    
    private Integer contactId;
    private Integer type;
    private Integer inputType;
    private Integer prefix1;
    private Integer prefix2;
    private String value;
    private String sufix;
}
