package com.dbs.module.account.detail.serviceagreement.dto.helper;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class PriceCodeVersionDTO implements Serializable {

    private String createdBy;
    private Date createdDate;
    private Integer entityId;
    private Integer id;
    private List<PriceCodeDetailDTO> mPricingDetail;
    private String priceCode;
    private String priceDescription;
    private String status;
    private String updateBy;
    private Date updateDate;
}
