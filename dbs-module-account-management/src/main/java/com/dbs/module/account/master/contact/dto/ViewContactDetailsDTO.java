package com.dbs.module.account.master.contact.dto;

import com.dbs.module.account.utils.dtoLabelValue;
import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class ViewContactDetailsDTO implements Serializable {

    private Integer contactDetailId;
    private Integer contactId;
    private String fullValue;
    private String contactValue;
    private dtoLabelValue type;
    private dtoLabelValue inputType;
    private ViewValueContactDetailsDTO valueDetail;

}
