package com.dbs.module.account.master.contact.dto;

import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class ContactDetailsCreateRequestDTO implements Serializable {
    
    private Integer contactId;
    private Integer type; // accountContact
    private Integer inputType; // accountContact
    private Integer typeId; // craeteAccount
    private Integer inputTypeId; // createAccount
    private Integer prefix1;
    private Integer prefix2;
    private String value;
    private String sufix;
}
