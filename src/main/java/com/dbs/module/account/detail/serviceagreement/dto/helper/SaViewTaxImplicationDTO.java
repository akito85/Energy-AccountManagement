package com.dbs.module.account.detail.serviceagreement.dto.helper;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
@SuppressWarnings("java:S1068")
public class SaViewTaxImplicationDTO implements Serializable {
    private String createdBy;
    private String updatedBy;
    private String status;
    private Date createdDate;
    private Date updatedDate;
    private Integer id;
    private Integer saId;
    private Integer taxImplicationId;
    private String category;
    private String taxImplicationName;
    private String serviceType;
    private String implicationType;
    private Boolean gunggung;
    private Boolean vatInvoiceIssuance;
    private Integer transactionCode;
    private String description;
    private Date startDate;
    private Date endDate;
    private Boolean isActive;
}
