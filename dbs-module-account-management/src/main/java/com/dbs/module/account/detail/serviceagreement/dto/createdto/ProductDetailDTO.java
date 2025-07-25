package com.dbs.module.account.detail.serviceagreement.dto.createdto;

import com.dbs.module.account.detail.serviceagreement.dto.helper.ProductDetailListDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class ProductDetailDTO implements Serializable {
    private String productName;
    private String description;
    private String productType;
    private String serviceType;
    private String productClass;
    private List<ProductDetailListDTO> productDetail;
    private ProductDetailPricingDTO productPricing;
    private List<CalculationRuleViewDto> productCalcRule;
    private List<SaProductTosDTO> productTos;
}
