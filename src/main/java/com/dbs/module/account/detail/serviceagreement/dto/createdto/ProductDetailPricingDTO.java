package com.dbs.module.account.detail.serviceagreement.dto.createdto;

import com.dbs.module.account.detail.serviceagreement.dto.helper.PriceCodeVersionDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.PricingRuleDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.PricingRuleDetailDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProductDetailPricingDTO implements Serializable {
    List<PriceCodeVersionDTO> priceCodeList;
    List<PricingRuleDTO> priceRuleList;
    List<PricingRuleDetailDTO> priceRuleTiering;
}
