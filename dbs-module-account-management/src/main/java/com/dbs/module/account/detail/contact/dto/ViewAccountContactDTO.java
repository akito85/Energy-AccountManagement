package com.dbs.module.account.detail.contact.dto;

import com.dbs.module.account.master.contact.dto.ViewContactDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class ViewAccountContactDTO implements Serializable {
    
    private Integer accountContactId;
    private Integer accountId;
    private Integer contactAddressId;
    private String contactAddress;
    private String additionalNote;
    private String description;
    private Boolean primaryFlag;
    private String primaryFlagValue;
    private String status;
    private Date createdDate;
    private String createdBy;
    private Date updateDate;
    private String updateBy;
    private ViewContactDTO contact;






}
