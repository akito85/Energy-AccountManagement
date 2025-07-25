package com.dbs.module.account.main.dto.createaccount;

import com.dbs.module.account.detail.financialinformation.dto.PaymentChannelDTO;
import com.dbs.module.account.detail.financialinformation.dto.TaxIdentifierCreateDTO;
import com.dbs.module.account.detail.financialinformation.dto.TaxRelationCreateDTO;
import com.dbs.module.account.detail.financialinformation.dto.WithholdingTaxCreateDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class FinancialInformationDTO implements Serializable {
    
    private PaymentChannelDTO paymentChannel;
    private TaxIdentifierCreateDTO taxIdentifier;
    private TaxRelationCreateDTO taxRelation;
    private WithholdingTaxCreateDTO wapu;
    private List<String> billingBucket;
    private List<Integer> taxImplication;
}
