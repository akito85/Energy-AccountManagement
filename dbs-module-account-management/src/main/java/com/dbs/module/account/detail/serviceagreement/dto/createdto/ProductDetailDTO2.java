package com.dbs.module.account.detail.serviceagreement.dto.createdto;

import com.dbs.module.account.detail.serviceagreement.dto.helper.CalcRuleProductDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.ViewTosProductDTO;
import com.dbs.module.account.detail.serviceagreement.dto.ProductDetailViewDto;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
@Data
@SuppressWarnings("java:S1068")
public class ProductDetailDTO2 {
    private String productName;
    private String description;
    private String productType;
    private String serviceType;
    private String productClass;
    private List<ProductDetailViewDto> productDetail;
    private ProductDetailPricingDTO productPricing;
    private List<CalcRuleProductDTO> productCalcRule;
    private List<ViewTosProductDTO> productTos;
    private LinkedHashMap<String, Object> lateCharge;
}
