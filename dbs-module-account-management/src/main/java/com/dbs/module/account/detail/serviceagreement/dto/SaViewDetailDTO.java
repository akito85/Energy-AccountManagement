package com.dbs.module.account.detail.serviceagreement.dto;

import com.dbs.database.crm.entities.accountmanagement.VW_SA_CALCRULE;
import com.dbs.database.crm.entities.accountmanagement.VW_SA_DETAIL;
import com.dbs.database.crm.entities.usermanagement.M_ATTACHMENT;
import com.dbs.module.account.detail.serviceagreement.dto.createdto.ProductDetailDTO2;
import com.dbs.module.account.detail.serviceagreement.dto.createdto.VersionListDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.HistorySaLogDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.SaViewTosDTO;
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.ApprovalHeaderViewDto;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Data
@SuppressWarnings("java:S1068") 
public class SaViewDetailDTO {

    private Boolean isApprover;
    private Integer tAppId;
    private Integer appHierId;
    private String approvalType;
    private ApprovalHeaderViewDto approvalDetail;
    private HistorySaLogDTO saHistory;
    private SaInfoDetailDTO saInfo;
    private List<VW_SA_DETAIL> saDetail;
    private List<SaPriceRuleDTO> saPricing;
    private List<VW_SA_CALCRULE> saCalcRule;
    private List<SaViewTosDTO> saTOS;
    private LinkedHashMap<String, Object> saLateCharge;
    private LinkedHashMap<String, Object> saTaxImplication;
    private Optional<List<M_ATTACHMENT>> attachment;
    private List<VersionListDTO> versionList;
    private ProductDetailDTO2 product;
}
