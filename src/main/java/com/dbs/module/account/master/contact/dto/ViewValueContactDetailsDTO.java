package com.dbs.module.account.master.contact.dto;

import com.dbs.module.account.utils.dtoLabelValue;
import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class ViewValueContactDetailsDTO implements Serializable {

    private dtoLabelValue prefix1;
    private dtoLabelValue prefix2;
    private String value;
    private String sufix;
}
