package com.dbs.module.account.detail.serviceagreement.dto.createdto;

import com.dbs.database.crm.entities.product.R_PRODUCT_TOS;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class SaProductTosDTO implements Serializable {
    private Integer tosId;
    private String tosName;
    private String description;
    private List<R_PRODUCT_TOS> tosDetail;
}
