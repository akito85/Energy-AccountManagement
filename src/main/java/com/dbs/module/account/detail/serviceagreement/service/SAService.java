package com.dbs.module.account.detail.serviceagreement.service;

import com.dbs.module.account.detail.serviceagreement.dto.helper.ApprovalSaDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.ViewTosProductDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.InactiveSaDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.SaViewTosDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.SaInfoDetailDraftDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.PricingRuleDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.SaLateChargeDetailDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.PricingRuleDetailDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.SaObjectMapperDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.PriceCodeVersionDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.PriceCodeDetailAdjustmentDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.SaViewTaxImplicationDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.HistorySaLogDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.PriceCodeDetailDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.CalcRuleProductDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.ValidationCreateSaDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.ViewTosDtlDTO;
import com.dbs.module.account.detail.serviceagreement.dto.createdto.SaInfoDTO;
import com.dbs.module.account.detail.serviceagreement.dto.createdto.SaPrcRuleDTO;
import com.dbs.module.account.detail.serviceagreement.dto.createdto.SaProductTosCreateDTO;
import com.dbs.module.account.detail.serviceagreement.dto.createdto.ProductDetailDTO2;
import com.dbs.module.account.detail.serviceagreement.dto.createdto.ProductDetailPricingDTO;
import com.dbs.module.account.detail.serviceagreement.dto.createdto.ListSaDetailDTO;
import com.dbs.module.account.detail.serviceagreement.dto.createdto.SaTosDetailDTO;
import com.dbs.module.account.detail.serviceagreement.dto.createdto.VersionListDTO;
import com.dbs.module.account.detail.serviceagreement.dto.CriteriaDataDTO;
import com.dbs.module.account.detail.serviceagreement.dto.SaUpdateDraftDTO;
import com.dbs.module.account.detail.serviceagreement.dto.SaDetailDraftDTO;
import com.dbs.module.account.detail.serviceagreement.dto.SaPriceRuleDTO;
import com.dbs.module.account.detail.serviceagreement.dto.SaViewDetailDTO;
import com.dbs.module.account.detail.serviceagreement.dto.SaInfoDetailDTO;
import com.dbs.module.account.detail.serviceagreement.dto.SaCreateDTO;
import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.ApprovalServices;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.*;
import com.dbs.database.crm.entities.accountmanagement.*;
import com.dbs.database.crm.entities.mastermanagement.R_PRICING_DETAIL;
import com.dbs.database.crm.entities.product.*;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_TAX_CODE;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_TERMS_OF_PAYMENT;
import com.dbs.database.crm.entities.usermanagement.*;
import com.dbs.database.crm.entities.usermanagement.view.VW_HIER_FLOW;
import com.dbs.database.crm.repositories.accountmanagement.Account.*;
import com.dbs.database.crm.repositories.accountmanagement.*;
import com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement.*;
import com.dbs.database.crm.repositories.mastermanagement.RPricingAdjustmentDetailRepo;
import com.dbs.database.crm.repositories.mastermanagement.RPricingDetailRepo;
import com.dbs.database.crm.repositories.product.*;
import com.dbs.database.crm.repositories.rbi.MRbiTaxCodeRepo;
import com.dbs.database.crm.repositories.rbi.MRbiTermsOfPaymentRepo;
import com.dbs.database.crm.repositories.usermanagement.*;
import com.dbs.database.crm.repositories.usermanagement.view.VWHierFlowRepo;
import com.dbs.module.account.detail.address.service.AccountAddressService;
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.ApprovalHeaderViewDto;
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.ApprovalHierarchyDetailDto;
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.ApprovalHierarchyDto;
import com.dbs.module.account.detail.serviceagreement.dto.ProductCriteriaFilterDto;
import com.dbs.module.account.detail.serviceagreement.dto.ProductDetailViewDto;
import com.dbs.module.account.utils.ConstantAccount;
import com.dbs.module.account.utils.UtilsAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jlefebure.spring.boot.minio.MinioConfigurationProperties;
import com.unboundid.util.json.JSONException;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

import static com.dbs.common.base.utils.CommonHelper.removeSpace;
import com.dbs.module.account.detail.serviceagreement.dto.SaViewDTO;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@Slf4j
@RequiredArgsConstructor
public class SAService {

    private static final String ACTIVE = "ACTIVE";
    private static final String APP_CATEGORY = "SERVICE_AGREEMENT";
    private static final String PATH_FILE = "PATH_01";
    private static final String SUBMIT = "SUBMIT";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioConfigurationProperties configurationProperties;

    private final Logger logger = LoggerFactory.getLogger(AccountAddressService.class);

    @Autowired
    private final MAccountRepo mAccountRepo;

    @Autowired
    private final VWAccountCriteriaRepo vwAccountCriteriaRepo;

    @Autowired
    private MAmLateChargeRuleFormulaRepo latechargeRuleFormulaRepo;

    @Autowired
    private final VWPricingRuleDetailRepo vwPricingRuleDetailRepo;

    @Autowired
    private AuditTrailRepo auditTrailRepo;

    @Autowired
    private final MUserRepo mUserRepo;

    @Autowired
    private final RGlobalTypeValueRepo rGlobalTypeValueRepo;

    @Autowired
    private final TAmSARepo tAmSARepo;

    @Autowired
    private final MPricingAdjustmentHeaderRepo mPricingAdjustmentHeaderRepo;

    @Autowired
    private final RPricingAdjustmentDetailRepo rPricingAdjustmentDetailRepo;

    @Autowired
    private final MAccountAddressRepo mAccountAddressRepo;

    @Autowired
    private final MServicePointRepo mServicePointRepo;

    @Autowired
    private final MAccountGasSourceRepo mAccountGasSourceRepo;

    @Autowired
    private final TAmSALateChargeHeaderRepo tAmSALateChargeHeaderRepo;

    @Autowired
    private final TAmSATaxImpHeaderRepo tAmSATaxImpHeaderRepo;

    @Autowired
    private final TAmSAAttachmentRepo tAmSAAttachmentRepo;

    @Autowired
    private final VwSaRepo vwSaRepo;

    @Autowired
    private final MRbiTaxCodeRepo mRbiTaxCodeRepo;

    @Autowired
    private final VwSaDetailRepo mvSaDetailRepo;

    @Autowired
    private final VwSaPrcRuleRepo mvSaPrcRuleRepo;

    @Autowired
    private final VwSaCalcRuleRepo mvSaCalcRuleRepo;

    @Autowired
    private final VwTosSubmissionRepo mvTosSubmissionRepo;

    @Autowired
    private final VwTosSubmissionDtlRepo mvTosSubmissionDtlRepo;

    @Autowired
    private final MAmLateChargeRepo mAmLateChargeRepo;

    @Autowired
    private final MAmLateChargeRuleRepo mAmLateChargeRuleRepo;

    @Autowired
    private final MApprovalHierarchyRepo mApprovalHierarchyRepo;

    @Autowired
    private final VWApprovalHierarchyDtlRepo vwApprovalHierarycyDtlRepo;

    @Autowired
    private final MAttachmentRepo mAttachmentRepo;

    @Autowired
    private final MTaxImplicationRepo mTaxImplicationRepo;

    @Autowired
    private final MTaxImplicationCriteriaDataRepo mTaxImplicationCriteriaDataRepo;

    @Autowired
    private final MTaxImplicationRuleRepo mTaxImplicationRuleRepo;

    @Autowired
    private final TAmSADetailHeaderRepo tAmSADetailHeaderRepo;

    @Autowired
    private final TAmSACalcRuleHeaderRepo tAmSACalcRuleHeaderRepo;

    @Autowired
    private final TAmSaPrcRulePrcAdjustmentHeaderRepo tAmSaPrcRulePrcAdjustmentHeaderRepo;

    @Autowired
    private ApprovalServices approvalServices;

    @Autowired
    private TAmSATosHeaderRepo tAmSATosHeaderRepo;

    @Autowired
    private TAmSADetailDTLRepo tAmSADetailDTLRepo;

    @Autowired
    private TAmSaCalcRuleDTLRepo tAmSaCalcRuleDTLRepo;

    @Autowired
    private TAmSATosDTLRepo tAmSATosDTLRepo;

    @Autowired
    private TAmSAPrcRuleDTLRepo tAmSAPrcRuleDTLRepo;

    @Autowired
    private TApprovalRepo tApprovalRepo;
    
    @Autowired
    private VWHierFlowRepo vWHierFlowRepo;
    
    @Autowired
    private TApprovalHistoryRepo tApprovalHistoryRepo;

    @Autowired
    private MApprovalHierarchyDtlRepo mApprovalHierarchyDtlRepo;

    @Autowired
    private GlobalTypeValueService globalTypeService;

    @Autowired
    private MTosRepo mTosRepo;

    @Autowired
    private MProductRepo mProductRepo;

    @Autowired
    private MProductVersionRepo mProductVersionRepo;

    @Autowired
    private MProductDetailRepo mProductDetailRepo;

    @Autowired
    private MProductPricingRepo mProductPricingRepo;

    @Autowired
    private final MPricingRepo mPricingRepo;

    @Autowired
    private final TAmSaPrcAdjustmentHeaderRepo tAmSaPrcAdjustmentHeaderRepo;

    @Autowired
    private RPricingDetailRepo rPricingDetailRepo;

    @Autowired
    private MPricingRuleRepo mPricingRuleRepo;

    @Autowired
    private MPricingRuleDetailRepo mPricingRuleDetailRepo;

    @Autowired
    private final MRbiTermsOfPaymentRepo mRbiTermsOfPaymentRepo;

    @Autowired
    private MProductCalculationRuleRepo mProductCalculationRuleRepo;

    @Autowired
    private MProductTermOfServiceRepo mProductTermOfServiceRepo;

    @Autowired
    private RProductTermOfServiceRepo rProductTermOfServiceRepo;

    @Autowired
    private MCustomerRepo mCustomerRepo;

    @Autowired
    private UtilsAccount.GlobalTypeServiceAccount gtAccount;

    private static final String WAITING_APPROVAL = "WAITING APPROVAL";

    @Autowired
    private SaDdlService saDdlService;

    public ResponseEntity<ResponseObject> serviceAgreementView(
            MaterialTablePagingRequest pagingData,
            PagedResourcesAssembler<VW_SA> assembler,
            Integer accountId
    ) {
//        logger.info("Get List View Service Agreement with Paging");

        ResponseObject result = new ResponseObject();

        try {

            Page<VW_SA> saPage;
            Map<String, Object> filter = new HashMap<>();
            filter.put("accountId", accountId);

            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key+"~"+value);
                }
            }

            if (!pagingData.getSearch().isEmpty()) {
                saPage = vwSaRepo.findAll(vwSaRepo.getSpecificationFromFilters(pagingData, filter), PagingUtils.getPaging(pagingData));
            } else {
                saPage = vwSaRepo.findAll(vwSaRepo.getSpecificationDefault(filter), PagingUtils.getPaging(pagingData));
            }
            logger.info("Service Agreement View : {}", saPage);
            logger.info("Service Agreement search : {}", pagingData.getSearch());

            List<VW_SA> listSaView = saPage.getContent();
            List<SaViewDTO> resultData = new ArrayList<>();

            if (!listSaView.isEmpty()) {
                for (VW_SA data : listSaView) {
                    Optional<VW_SA> getViewSa = vwSaRepo.findById(data.getId());
                    if (getViewSa.isPresent()) {
                        SaViewDTO newData = getSaViewDTO(data, getViewSa);
                        resultData.add(newData);
                    }
                }

                var pagedData = assembler.toModel(saPage);
                Map<String, Object> dataResult = new HashMap<>();
                dataResult.put(Constant.RESULT, resultData);
                dataResult.put(Constant.PAGE, pagedData.getMetadata());
                dataResult.put(Constant.LINK, pagedData.getLinks());

                result.setSuccess(ResponseUtils.SUCCESS_TRUE);
                result.setCode(HttpStatus.OK);
                result.setMessage("Success Get List Service Agreement with Paging");
                result.setData(dataResult);

                return new ResponseEntity<>(result, result.getHttpCode());
            } else {
                Map<String, Object> dataResult = new HashMap<>();
                dataResult.put(Constant.RESULT, null);
                dataResult.put(Constant.PAGE, 0);
                dataResult.put(Constant.LINK, 0);

                result.setSuccess(ResponseUtils.SUCCESS_TRUE);
                result.setCode(HttpStatus.OK);
                result.setMessage("No Data");
                result.setData(dataResult);

                return new ResponseEntity<>(result, result.getHttpCode());
            }

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    private SaViewDTO getSaViewDTO(VW_SA data, Optional<VW_SA> getViewSa) {
        SaViewDTO newData = new SaViewDTO();
        newData.setId(data.getId());
        newData.setAccountId(data.getAccountId());
        //SA Number
        if (data.getSaNumber() != null)
            newData.setSaNumber(data.getSaNumber());
        //SA Reference
        if (data.getSaReferenceNumber() != null) {
            newData.setSaReference(data.getSaReferenceNumber());
        }
        //Service Type
        if (data.getSaServiceType() != null && getViewSa.isPresent()) {
            SaObjectMapperDTO map = new SaObjectMapperDTO();
            map.setId(getViewSa.get().getSaServiceTypeId());
            map.setValue(getViewSa.get().getSaServiceType());
            newData.setServiceType(map);
        }
        //Type
        if (data.getSaType() != null && getViewSa.isPresent()) {
            SaObjectMapperDTO map = new SaObjectMapperDTO();
            map.setId(getViewSa.get().getSaTypeId());
            map.setValue(getViewSa.get().getSaType());
            newData.setSaType(map);
        }
        //PJBG Type
        if (data.getPjbgType() != null && getViewSa.isPresent()) {
            SaObjectMapperDTO map = new SaObjectMapperDTO();
            map.setId(getViewSa.get().getPjbgTypeId());
            map.setValue(getViewSa.get().getPjbgType());
            newData.setPjbgType(map);
        }
        //SA Date
        if (data.getSaDate() != null)
            newData.setSaDate(data.getSaDate());
        //Start Date
        if (data.getStartDate() != null)
            newData.setStartDate(data.getStartDate());
        //End Date
        if (data.getEndDate() != null)
            newData.setEndDate(data.getEndDate());
        //Commitment Date
        if (data.getComitmentDate() != null)
            newData.setCommitmentDate(data.getComitmentDate());
        //Billing Cycle
        if (data.getBillingCycle() != null && getViewSa.isPresent()) {
            SaObjectMapperDTO map = new SaObjectMapperDTO();
            map.setId(getViewSa.get().getBillingCycleId());
            map.setValue(getViewSa.get().getBillingCycle());
            newData.setBillingCycle(map);
        }
        //Term of Payment
        if (data.getTermOfPaymentId() != null && getViewSa.isPresent() && getViewSa.get().getTermOfPaymentId() != null) {
            SaObjectMapperDTO map = new SaObjectMapperDTO();
            map.setId(getViewSa.get().getTermOfPaymentId());
            map.setValue(getViewSa.get().getTermsOfPaymentName());
            newData.setTermOfPayment(map);
        }
        //Invoice Template
        if (data.getInvoiceTemplate() != null && getViewSa.isPresent() && getViewSa.get().getInvoiceTemplateId() != null) {
            SaObjectMapperDTO map = new SaObjectMapperDTO();
            map.setId(getViewSa.get().getInvoiceTemplateId());
            map.setValue(getViewSa.get().getInvoiceTemplate());
            newData.setInvoiceTemplate(map);
        }
        //Invoice Template
        if (data.getGasInPlanDate() != null)
            newData.setGasInPlanDate(data.getGasInPlanDate());
        //Late Charge
        if (getViewSa.isPresent() && getViewSa.get().getIdrLateChargeId() != null) {
            SaObjectMapperDTO map = new SaObjectMapperDTO();
            map.setId(getViewSa.get().getIdrLateChargeId());
            map.setValue(getViewSa.get().getIdrLateCharge());
            newData.setLateCharge(map);
        }
        newData.setStatus(data.getStatus());
        newData.setApprovalStatus(data.getApprovalStatus());
        newData.setIsMain(data.getIsMain());
        newData.setCreatedBy(data.getCreatedBy());
        newData.setCreatedDate(data.getCreatedDate());
        newData.setUpdatedBy(data.getUpdatedBy());
        newData.setUpdatedDate(data.getUpdatedDate());
        return newData;
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    public PriceCodeDetailAdjustmentDTO getPriceAdjustmentTieringByCriteriaToDTO(R_PRICING_DETAIL rPricingDetail, Integer accountId) {
        logger.info("get List Product");
        try {
            Optional<VW_ACCOUNT_CRITERIA> dataAccount = vwAccountCriteriaRepo.findByAccountId(accountId);
            LinkedHashMap<String, Object> allDataCheck = new LinkedHashMap<>();
            allDataCheck.put("tasCrit", null);
            List<Map<String, Integer>> tasCrits = new ArrayList<>();
            if(dataAccount.isPresent()) {
                Map<String, Integer> newTasCriteria = new HashMap<>();
                newTasCriteria.put("customer", dataAccount.get().getCustomerId());
                newTasCriteria.put("budget", dataAccount.get().getAccountBudget());
                newTasCriteria.put("subDistrict", dataAccount.get().getPremiseSubdistrict());
                newTasCriteria.put("district", dataAccount.get().getPremiseDistrict());
                newTasCriteria.put("city", dataAccount.get().getPremiseCity());
                newTasCriteria.put("province", dataAccount.get().getPremiseProvince());
                newTasCriteria.put("area", dataAccount.get().getCostCenter());
                newTasCriteria.put("sor", dataAccount.get().getSor());
                newTasCriteria.put("industrialSector", dataAccount.get().getAccountIndustrialSector());
                newTasCriteria.put("product", dataAccount.get().getSaProductVersion());
                newTasCriteria.put("gsizes", dataAccount.get().getPremiseSpAssetGSize());
                newTasCriteria.put("customerSegment", dataAccount.get().getAccountSegment());
                newTasCriteria.put("accountGroup", dataAccount.get().getAccountGroupType());
                newTasCriteria.put("serviceType", dataAccount.get().getSaServiceType());
                newTasCriteria.put("accountCategory", dataAccount.get().getAccountCategory());
                newTasCriteria.put("allCriteria", null);
                tasCrits.add(newTasCriteria);
            }
            allDataCheck.put("tasCrit", tasCrits);

            List<Integer> idCritFiltered = new ArrayList<>();
            boolean valid = false;

            // criteria filter
            List<Map<String, Integer>> tasCritses = (List<Map<String, Integer>>) allDataCheck.get("tasCrit");
            if (!tasCritses.isEmpty()) {

                List<M_PRICING_ADJUSTMENT_HEADER> mPricingAdjustmentHeaders = mPricingAdjustmentHeaderRepo.findAllBymPricingDetailId(rPricingDetail.getId());

                if (!mPricingAdjustmentHeaders.isEmpty()) {
                    for (M_PRICING_ADJUSTMENT_HEADER mPricingAdjustmentHeader : mPricingAdjustmentHeaders) {
                        List<R_PRICING_ADJUSTMENT_DETAIL> critDatas = rPricingAdjustmentDetailRepo.findAllByPricingAdjustmentId(mPricingAdjustmentHeader.getId());
                        if (!critDatas.isEmpty()) {
                            for (R_PRICING_ADJUSTMENT_DETAIL critData : critDatas) {
                                if (critData.getAllCriteria() != null && Boolean.TRUE.equals(critData.getAllCriteria())) {
                                    idCritFiltered.add(critData.getId());
                                    break;

                                }
                                Map<String, Integer> pricingCrit;
                                ProductCriteriaFilterDto filterCrit = objectMapper.convertValue(critData,
                                        ProductCriteriaFilterDto.class);
                                pricingCrit = objectMapper.convertValue(filterCrit,
                                        new TypeReference<Map<String, Integer>>() {
                                        });

                                for (Map<String, Integer> fil : tasCritses) {

                                    valid = CheckCriteria.compareCriteria(CheckCriteria.PRODUCT, fil, pricingCrit);
                                    if (valid) {
                                        idCritFiltered.add(critData.getId());
                                        break;
                                    }

                                }

                                if (valid) {
                                    break;
                                }

                            }
                        }
                    }
                }

                for (Integer idCrit : idCritFiltered) {
                    Optional<R_PRICING_ADJUSTMENT_DETAIL> dataPricing = rPricingAdjustmentDetailRepo.findById(idCrit);
                    PriceCodeDetailAdjustmentDTO dataAdjustment = new PriceCodeDetailAdjustmentDTO();
                    if (dataPricing.isPresent()) {
                        Optional<R_GLOBAL_TYPE_VALUE> type = rGlobalTypeValueRepo.findByGlbTypeValId(dataPricing.get().getAdjustmentType());
                        type.ifPresent(rGlobalTypeValue -> dataAdjustment.setAdjustmentType(rGlobalTypeValue.getName()));
                        dataAdjustment.setAdjustmentValue(dataPricing.get().getAdjustmentValue());
                        dataAdjustment.setPriceAdjustmentDetailId(dataPricing.get().getId());

                        if (dataPricing.get().getAdjustmentType() == 221) {
                            dataAdjustment.setFinalAdjustmentValue(dataPricing.get().getAdjustmentValue());
                        } else if (dataPricing.get().getAdjustmentType() == 222) {
                            BigDecimal total = BigDecimal.ZERO;
                            BigDecimal bd2 = rPricingDetail.getValue();
                            total = total.add(bd2);
                            total = total.add(dataPricing.get().getAdjustmentValue());
                            dataAdjustment.setFinalAdjustmentValue(total);
                        } else {
                            BigDecimal bd1 = dataPricing.get().getAdjustmentValue();
                            BigDecimal bd2 = rPricingDetail.getValue();
                            BigDecimal total = bd2.subtract(bd1);
                            dataAdjustment.setFinalAdjustmentValue(total);
                        }

                        String currencyName = "";
                        String uomName = "";

                        Optional<R_GLOBAL_TYPE_VALUE> getCurrency = rGlobalTypeValueRepo.findByGlbTypeValId(Integer.valueOf(rPricingDetail.getCurrency()));
                        if(getCurrency.isPresent()) {
                            currencyName = getCurrency.get().getName();
                        }

                        Optional<R_GLOBAL_TYPE_VALUE> getUom = rGlobalTypeValueRepo.findByGlbTypeValId(Integer.valueOf(rPricingDetail.getUom()));
                        if(getUom.isPresent()) {
                            uomName = getUom.get().getName();
                        }

                        Optional<M_PRICING_ADJUSTMENT_HEADER> mPricingAdjustmentHeader = mPricingAdjustmentHeaderRepo.findById(dataPricing.get().getPricingAdjustmentId());
                        if (mPricingAdjustmentHeader.isPresent()) {
                            dataAdjustment.setAdjustmentName(mPricingAdjustmentHeader.get().getName());
                            dataAdjustment.setAdjustmentText(mPricingAdjustmentHeader.get().getName() + " - " + currencyName + "/" + dataAdjustment.getFinalAdjustmentValue() + "/" + uomName);
                        }

                        return dataAdjustment;
                    }
                }
            }

            return null;

        } catch (Exception e) {
            logger.error(CommonVariables.ERROR_IN, e.getMessage(), e);
            return null;
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> serviceAgreementViewDetail(Integer saId, HttpServletRequest httpServletRequest) {
        logger.info("Get List View Detail Service Agreement");

        ResponseObject result = new ResponseObject();

        try {
            SaViewDetailDTO newData = new SaViewDetailDTO();

            List<String> appCategory = new ArrayList<>();
            appCategory.add(ApprovalCategory.SERVICE_AGREEMENT.name());
            appCategory.add(ApprovalCategory.INACTIVE_SERVICE_AGREEMENT.name());
            Optional<T_APPROVAL> appId = tApprovalRepo.findFirstByIdTransAndCategoryInAndStatus(saId.toString(),
                    appCategory, ApprovalStatus.WAITING_FOR_APPROVAL.name());

            if (appId.isPresent()) {
                // change get isapprover and add isapprover from m_forward_task
                Optional<VW_HIER_FLOW> appHierDtlOpt = vWHierFlowRepo.findBytAppId(appId.get().getTAppId());
                if (appHierDtlOpt.isPresent()) {
                    VW_HIER_FLOW appHierDtl = appHierDtlOpt.get();
                    if (StringUtils.hasValue(appHierDtl.getForwardTo()) && appHierDtl.getTaskForward() > 0) {
                        Optional<M_USER> mUser = mUserRepo.findByUsername(UserDetailUtils.getUsernameFromToken(httpServletRequest));
                        String employeeCode = "";
                        if (mUser.isPresent()) {
                            employeeCode = mUser.get().getEmployee().getEmployeeCode();
                        }
                       if (appHierDtl.getForwardTo().equals(UserDetailUtils.getPositionFromToken(httpServletRequest))
                                    && (StringUtils.hasValue(appHierDtl.getEmployeeForward()) 
                                    && appHierDtl.getEmployeeForward().equalsIgnoreCase(employeeCode))) {
                            newData.setAppHierId(appHierDtl.getAppHierId());
                            newData.setTAppId(appId.get().getTAppId());
                            newData.setApprovalType(appId.get().getCategory());
                            newData.setIsApprover(Boolean.TRUE);
                        } else {
                            newData.setAppHierId(appId.get().getAppHierId());
                            newData.setIsApprover(Boolean.FALSE);
                        }
                    } else {
                        if (appHierDtl.getPositionId().equals(UserDetailUtils.getPositionFromToken(httpServletRequest))) {
                            newData.setAppHierId(appHierDtl.getAppHierId());
                            newData.setTAppId(appId.get().getTAppId());
                            newData.setApprovalType(appId.get().getCategory());
                            newData.setIsApprover(Boolean.TRUE);
                        } else {
                            newData.setAppHierId(appId.get().getAppHierId());
                            newData.setIsApprover(Boolean.FALSE);
                        }
                    }
                } else {
                    Optional<M_APPROVAL_HIERARCHY_DTL> mappHierDtlOpt = mApprovalHierarchyDtlRepo
                        .findFirstByAppHierIdAndApprovalLevel(appId.get().getAppHierId(),
                                appId.get().getApprovalLevel());
                    if (mappHierDtlOpt.isPresent()) {
                        M_APPROVAL_HIERARCHY_DTL appHierDtl = mappHierDtlOpt.get();
                        if (appHierDtl.getPositionId().equals(UserDetailUtils.getPositionFromToken(httpServletRequest))) {
                            newData.setAppHierId(appHierDtl.getAppHierId());
                            newData.setTAppId(appId.get().getTAppId());
                            newData.setApprovalType(appId.get().getCategory());
                            newData.setIsApprover(Boolean.TRUE);
                        } else {
                            newData.setAppHierId(appId.get().getAppHierId());
                            newData.setIsApprover(Boolean.FALSE);
                        }
                    }
                }

                Integer appHist = tApprovalHistoryRepo.findTopSubmittedByRefIdAndApprovalType(saId,
                        appId.get().getCategory());

                Optional<T_APPROVAL_HISTORY> dtlAppOpt = tApprovalHistoryRepo.findFirstBytAppId(appHist);

                if (dtlAppOpt.isPresent()) {
                    T_APPROVAL_HISTORY dtlApp = dtlAppOpt.get();
                    ApprovalHeaderViewDto appDtl = new ApprovalHeaderViewDto();
                    appDtl.setRequestedBy(dtlApp.getFullName());
                    appDtl.setRequestedDate(dtlApp.getEventDate());
                    appDtl.setRemarks(dtlApp.getDescription());
                    appDtl.setApprovalType(dtlApp.getApprovalType());
                    if(dtlApp.getDescription().contains("Update")) {
                        appDtl.setType("update");
                   
                    } else if(dtlApp.getDescription().contains("Create")) {
                        appDtl.setType("create");
                    } else {
                        appDtl.setType("inactive");
                    }
                    newData.setApprovalDetail(appDtl);
                }
            } else {
                newData.setIsApprover(Boolean.FALSE);
            }

            // SA History
            Optional<T_AM_SA> logData = tAmSARepo.findById(saId);
            if (logData.isPresent()) {
                HistorySaLogDTO logHistory = new HistorySaLogDTO();
                logHistory.setSaId(logData.get().getId());
                logHistory.setCreatedDate(logData.get().getCreatedDate());
                logHistory.setCreatedBy(logData.get().getCreatedBy());
                logHistory.setUpdateDate(logData.get().getUpdatedDate());
                logHistory.setUpdatedBy(logData.get().getUpdatedBy());
                logHistory.setApprovalStatus(logData.get().getApprovalStatus());
                logHistory.setDescription(logData.get().getDescription());
                newData.setSaHistory(logHistory);
            }
            // SA info
            Optional<VW_SA> content = vwSaRepo.findById(saId);
            if (content.isPresent()) {
                SaInfoDetailDTO dataInfo = objectMapper.convertValue(content, SaInfoDetailDTO.class);
                Optional<M_RBI_TERMS_OF_PAYMENT> listTop = mRbiTermsOfPaymentRepo.findById(dataInfo.getTermOfPaymentId());
                if(listTop.isPresent()) {
                    switch (listTop.get().getTopTypeName()) {
                        case "After":
                            dataInfo.setTermsOfPaymentName("H" + " + " + listTop.get().getTopTerms());
                            break;
                        case "Date":
                            dataInfo.setTermsOfPaymentName("Tanggal " + listTop.get().getTopTerms());
                            break;
                        default:
                            break;
                    }
                }
                // IDR Adjustment
                if(content.get().getAdjustmentIdrId() != null) {
                    PriceCodeDetailAdjustmentDTO idrAdjustment = new PriceCodeDetailAdjustmentDTO();
                    idrAdjustment.setPriceAdjustmentDetailId(content.get().getAdjustmentIdrId());
                    idrAdjustment.setAdjustmentName(content.get().getPriceCode());
                    idrAdjustment.setAdjustmentType(content.get().getAdjustmentTypeIdr());
                    idrAdjustment.setAdjustmentValue(BigDecimal.valueOf(content.get().getAdjustmentIdrValue()));
                    idrAdjustment.setFinalAdjustmentValue(BigDecimal.valueOf(content.get().getFinalAdjustmentIdrValue()));
                    idrAdjustment.setAdjustmentText(content.get().getAdjustmentIdrFullPriceCode());
                    dataInfo.setIdrAdjustment(idrAdjustment);
                }
                // USD Adjustment
                if(content.get().getAdjustmentUsdId() != null) {
                    PriceCodeDetailAdjustmentDTO usdAdjustment = new PriceCodeDetailAdjustmentDTO();
                    usdAdjustment.setPriceAdjustmentDetailId(content.get().getAdjustmentUsdId());
                    usdAdjustment.setAdjustmentName(content.get().getPriceCode());
                    usdAdjustment.setAdjustmentType(content.get().getAdjustmentTypeUsd());
                    usdAdjustment.setAdjustmentValue(BigDecimal.valueOf(content.get().getAdjustmentUsdValue()));
                    usdAdjustment.setFinalAdjustmentValue(BigDecimal.valueOf(content.get().getFinalAdjustmentUsdValue()));
                    usdAdjustment.setAdjustmentText(content.get().getAdjustmentUsdFullPriceCode());
                    dataInfo.setUsdAdjustment(usdAdjustment);
                }

                newData.setSaInfo(dataInfo);
                // SA Detail
                List<VW_SA_DETAIL> saDetail = mvSaDetailRepo.findAllBySaId(saId);
                if (!saDetail.isEmpty())
                    newData.setSaDetail(saDetail);
                List<T_AM_SA_PRC_RULE_DTL> saPricings = tAmSAPrcRuleDTLRepo.findAllBySaId(saId);
                List<SaPriceRuleDTO> dataPrcTiering = new ArrayList<>();
                for (T_AM_SA_PRC_RULE_DTL saPricing: saPricings) {
                    Optional<M_PRICING> mPricing = mPricingRepo.findById(saPricing.getIdMPricing());
                    if(mPricing.isPresent()) {
                        List<R_PRICING_DETAIL> rPricingDetails = rPricingDetailRepo.findAllByIdPricing(mPricing.get().getId());
                        if(!rPricingDetails.isEmpty()) {
                            for (R_PRICING_DETAIL rPricingDetail : rPricingDetails) {
                                SaPriceRuleDTO dataTiering = new SaPriceRuleDTO();
                                dataTiering.setId(saPricing.getId());
                                dataTiering.setSaId(saPricing.getSaId());
                                dataTiering.setSaNumber(content.get().getSaNumber());
                                dataTiering.setLineNumber(saPricing.getLineNumber());
                                dataTiering.setMin(saPricing.getMin());
                                dataTiering.setMax(saPricing.getMax());
                                dataTiering.setIsUnlim(saPricing.getIsUnlim());
                                dataTiering.setIdMPricing(saPricing.getIdMPricing());
                                dataTiering.setPriceCode(mPricing.get().getPriceCode());
                                String uomName = "";
                                String currencyName = "";
                                if(rPricingDetail.getUom() != null) {
                                    dataTiering.setUomId(Integer.valueOf(rPricingDetail.getUom()));
                                    Optional<R_GLOBAL_TYPE_VALUE> getUomName = rGlobalTypeValueRepo.findByGlbTypeValId(Integer.valueOf(rPricingDetail.getUom()));
                                    if(getUomName.isPresent()) {
                                        uomName = getUomName.get().getName();
                                        dataTiering.setUom(uomName);
                                    }
                                }
                                if(rPricingDetail.getCurrency() != null) {
                                    dataTiering.setCurrencyId(Integer.valueOf(rPricingDetail.getCurrency()));
                                    Optional<R_GLOBAL_TYPE_VALUE> getCurrencyName = rGlobalTypeValueRepo.findByGlbTypeValId(Integer.valueOf(rPricingDetail.getCurrency()));
                                    if(getCurrencyName.isPresent()) {
                                        currencyName = getCurrencyName.get().getName();
                                        dataTiering.setCurrency(currencyName);
                                    }
                                }
                                if(rPricingDetail.getValue() != null) {
                                    dataTiering.setValue(rPricingDetail.getValue());
                                }
                                List<T_AM_SA_PRC_RULE_PRCADJUSTMENT_HEADER> tAmSaPrcRulePrcadjustmentHeader = tAmSaPrcRulePrcAdjustmentHeaderRepo.findAllByPrcRuleDtlId(saPricing.getId());
                                if(!tAmSaPrcRulePrcadjustmentHeader.isEmpty()) {
                                    for (T_AM_SA_PRC_RULE_PRCADJUSTMENT_HEADER data : tAmSaPrcRulePrcadjustmentHeader) {
                                        if(Objects.equals(saPricing.getId(), data.getPrcRuleDtlId())) {
                                            PriceCodeDetailAdjustmentDTO priceAdjustment = this.getPriceAdjustmentTieringByCriteriaToDTO(rPricingDetail, dataInfo.getAccountId());
                                            dataTiering.setAdjustment(priceAdjustment);
                                        }
                                    }
                                }
                                dataPrcTiering.add(dataTiering);
                            }
                        }
                    }
                }
                if(!dataPrcTiering.isEmpty()) {
                    newData.setSaPricing(dataPrcTiering);
                }
                // SA Calc Rule
                List<VW_SA_CALCRULE> saCalcRuleNew = new ArrayList<>();
                List<VW_SA_CALCRULE> saCalcRule = mvSaCalcRuleRepo.findAllBySaId(saId);
                if (!saCalcRule.isEmpty()) {
                    for (VW_SA_CALCRULE calcRule : saCalcRule) {
                        VW_SA_CALCRULE dtl = new VW_SA_CALCRULE();
                        dtl.setId(calcRule.getId());
                        dtl.setSaId(calcRule.getSaId());
                        dtl.setSaNumber(calcRule.getSaNumber());
                        dtl.setName(calcRule.getName());
                        dtl.setNameId(calcRule.getNameId());
                        if(calcRule.getUnitId() != null) {
                            if(isStringAllDigits(calcRule.getUnitId())) {
                                dtl.setUnit(calcRule.getUnit());
                            } else {
                                Optional<M_RBI_TAX_CODE> mRbiTaxCode = mRbiTaxCodeRepo.findFirstByTaxCode(calcRule.getUnitId());
                                mRbiTaxCode.ifPresent(taxCode -> dtl.setUnit(taxCode.getTaxCodeName()));
                            }
                            dtl.setUnitId(calcRule.getUnitId());
                        }
                        dtl.setValue(calcRule.getValue());
                        dtl.setDescription(calcRule.getDescription());
                        saCalcRuleNew.add(dtl);
                    }
                    newData.setSaCalcRule(saCalcRuleNew);
                }
                // SA TOS
                List<SaViewTosDTO> saTos = new ArrayList<>();
                List<T_AM_SA_TOS_HEADER> listTos = tAmSATosHeaderRepo.findAllBySaId(saId);
                if (!listTos.isEmpty()) {
                    for (T_AM_SA_TOS_HEADER data : listTos) {
                        SaViewTosDTO newDataTos = new SaViewTosDTO();
                        newDataTos.setTosId(data.getMTosId());
                        newDataTos.setSaTosId(data.getId());
                        Optional<M_TOS> mTos = mTosRepo.findById(data.getMTosId());
                        if(mTos.isPresent()) {
                            newDataTos.setTosName(mTos.get().getName());
                            newDataTos.setDescription(mTos.get().getDescription());
                        }
                        List<T_AM_SA_TOS_DTL> getDataDTL = tAmSATosDTLRepo.findAllBySaTosId(data.getId());
                        List<ViewTosDtlDTO> listDtl = new ArrayList<>();
                        if (!getDataDTL.isEmpty()) {
                            for (T_AM_SA_TOS_DTL dataDtl : getDataDTL) {
                                ViewTosDtlDTO setNewDataDtl = new ViewTosDtlDTO();

                                setNewDataDtl.setId(dataDtl.getId());
                                setNewDataDtl.setSaTosId(dataDtl.getSaTosId());
                                setNewDataDtl.setAttributeId(dataDtl.getAttribute());
                                Optional<R_GLOBAL_TYPE_VALUE> attributeName = rGlobalTypeValueRepo.findByGlbTypeValId(dataDtl.getAttribute());
                                attributeName.ifPresent(rGlobalTypeValue -> setNewDataDtl.setAttribute(rGlobalTypeValue.getName()));
                                setNewDataDtl.setValue(dataDtl.getValue());
                                setNewDataDtl.setUnit(dataDtl.getUnit());
                                setNewDataDtl.setFromItem(dataDtl.getFromItem());
                                listDtl.add(setNewDataDtl);
                            }
                            newDataTos.setTosDetail(listDtl);
                        }
                        saTos.add(newDataTos);
                    }
                }
                newData.setSaTOS(saTos);
                // SA Late Charge
                List<T_AM_SA_LATECHARGE_HEADER> saLateCharge = tAmSALateChargeHeaderRepo.findAllBySaIdAndStatus(content.get().getId(), FlowStatus.ACTIVE.name());
                LinkedHashMap<String, Object> allDataChecks = new LinkedHashMap<>();
                allDataChecks.put("lateChargeUSD", null);
                allDataChecks.put("lateChargeIDR", null);
                for (T_AM_SA_LATECHARGE_HEADER data : saLateCharge) {
                    SaLateChargeDetailDTO dataLc = new SaLateChargeDetailDTO();
                    dataLc.setCreatedBy(data.getCreatedBy());
                    dataLc.setCreatedDate(data.getCreatedDate());
                    dataLc.setStatus(data.getStatus());
                    dataLc.setUpdatedDate(data.getUpdatedDate());
                    dataLc.setUpdatedBy(data.getUpdatedBy());

                    dataLc.setId(data.getId());
                    dataLc.setSaId(data.getSaId());
                    dataLc.setLateChargeId(data.getLateChargesId());
                    Optional<M_AM_LATECHARGE> getLc = mAmLateChargeRepo.findById(data.getLateChargesId());
                    if (getLc.isPresent()) {
                        dataLc.setLateChargeName(getLc.get().getLateChargeName());
                        Optional<R_GLOBAL_TYPE_VALUE> currencyValue = rGlobalTypeValueRepo.findByGlbTypeValId(getLc.get().getCurrency());
                        currencyValue.ifPresent(rGlobalTypeValue -> dataLc.setCurrency(rGlobalTypeValue.getName()));
                        Optional<M_AM_LATECHARGE_RULE> latechargeRule = mAmLateChargeRuleRepo.findByLatechargeIdAndStatusActive(data.getLateChargesId());
                        latechargeRule.ifPresent(mAmLatechargeRule -> dataLc.setMaxAmount(mAmLatechargeRule.getMaxAmount()));

                        StringBuilder results = new StringBuilder();
                        Float value = new Float(0.00);
                        List<M_AM_LATECHARGE_RULE_FORMULA> dataFormula = latechargeRuleFormulaRepo.findAllByLatechargeRuleIdAndStatusOrderByIdAsc(getLc.get().getId(), "ACTIVE");
                        List<String> variable = new ArrayList<>();
                        List<String> operation = new ArrayList<>();
                        for(M_AM_LATECHARGE_RULE_FORMULA ff : dataFormula) {
                            if(ff.getType().equalsIgnoreCase("CONSTANT")){
                                value = ff.getValue();
                            }
                            if(ff.getOperation() != null) {
                                Optional<R_GLOBAL_TYPE_VALUE> getOperation = rGlobalTypeValueRepo.findByGlbTypeValId(ff.getOperation());
                                if(getOperation.get().getName().equalsIgnoreCase("SUBSTRACT")){
                                    operation.add(" - ");
                                } else if(getOperation.get().getName().equalsIgnoreCase("ADDITION")){
                                    operation.add(" + ");
                                } else if(getOperation.get().getName().equalsIgnoreCase("MULTIPLY")){
                                    operation.add(" * ");
                                } else if(getOperation.get().getName().equalsIgnoreCase("DIVIDE")){
                                    operation.add(" / ");
                                }

                            }
                            if(ff.getType().equalsIgnoreCase("VARIABLE")){
                                Optional<R_GLOBAL_TYPE_VALUE> getVariable = rGlobalTypeValueRepo.findByGlbTypeValId(ff.getVariableName());
                                variable.add(getVariable.isPresent()? getVariable.get().getName() : null);
                            }
                        }
                        for (int i = 0; i < variable.size(); i++) {
                            results.append(operation.get(i));

                            if (i < operation.size()) {
                                results.append(variable.get(i));
                            }
                        }
                        dataLc.setFormula(value + "" + results);
                    }

                    dataLc.setStartDate(data.getStartDate());
                    dataLc.setEndDate(null);
                    dataLc.setIsActive(null);
                    if(Objects.equals(dataLc.getCurrency(), "IDR")) {
                        allDataChecks.put("lateChargeIDR", dataLc);
                    }else if(Objects.equals(dataLc.getCurrency(), "USD")) {
                        allDataChecks.put("lateChargeUSD", dataLc);
                    }
                }
                newData.setSaLateCharge(allDataChecks);
                // SA Tax Implication
                List<T_AM_SA_TAXIMP_HEADER> saTaxImp = tAmSATaxImpHeaderRepo.findAllBySaIdAndStatus(content.get().getId(), FlowStatus.ACTIVE.name());
                LinkedHashMap<String, Object> taxImpliData = new LinkedHashMap<>();
                taxImpliData.put("taxImplicationPPN", null);
                taxImpliData.put("taxImplicationPPH", null);
                if (!saTaxImp.isEmpty())
                    for (T_AM_SA_TAXIMP_HEADER data : saTaxImp) {
                        Optional<M_AM_TAXIMPLICATION> dataTaxImplication = mTaxImplicationRepo.findById(data.getTaxImplicationId());
                        SaViewTaxImplicationDTO dataTax = new SaViewTaxImplicationDTO();
                        dataTax.setCreatedBy(data.getCreatedBy());
                        dataTax.setCreatedDate(data.getCreatedDate());
                        dataTax.setStatus(data.getStatus());
                        dataTax.setUpdatedDate(data.getUpdatedDate());
                        dataTax.setUpdatedBy(data.getUpdatedBy());

                        dataTax.setId(data.getId());
                        dataTax.setSaId(data.getSaId());
                        dataTax.setTaxImplicationId(data.getTaxImplicationId());
                        if (dataTaxImplication.isPresent()) {
                            Optional<R_GLOBAL_TYPE_VALUE> dataCategory = rGlobalTypeValueRepo.findByGlbTypeValId(dataTaxImplication.get().getCategory());
                            dataCategory.ifPresent(rGlobalTypeValue -> dataTax.setCategory(rGlobalTypeValue.getName()));
                            dataTax.setTaxImplicationName(dataTaxImplication.get().getTaxImplicationName());
                            Optional<R_GLOBAL_TYPE_VALUE> dataServiceType = rGlobalTypeValueRepo.findByGlbTypeValId(dataTaxImplication.get().getServiceType());
                            dataServiceType.ifPresent(rGlobalTypeValue -> dataTax.setServiceType(rGlobalTypeValue.getName()));
                            dataTax.setDescription(dataTaxImplication.get().getDescription());
                            dataTax.setIsActive(Objects.equals(dataTaxImplication.get().getStatus(), "ACTIVE"));
                            Optional<M_AM_TAXIMPLICATION_RULE> dataTaxRule = mTaxImplicationRuleRepo.findByTaximplicationIdAndStatus(data.getTaxImplicationId(), FlowStatus.ACTIVE.name());
                            if (dataTaxRule.isPresent()) {
                                dataTax.setGunggung(Objects.equals(dataTaxRule.get().getIsGunggung(), "Y"));
                                dataTax.setTransactionCode(dataTaxRule.get().getTransCode());
                                Optional<R_GLOBAL_TYPE_VALUE> dataImplication = rGlobalTypeValueRepo.findByGlbTypeValId(dataTaxRule.get().getImplicationTypeId());
                                dataImplication.ifPresent(rGlobalTypeValue -> dataTax.setImplicationType(rGlobalTypeValue.getName()));
                            }
                        }
                        if(Objects.equals(dataTax.getCategory(), "PPH")) {
                            taxImpliData.put("taxImplicationPPH", dataTax);
                        }else if(Objects.equals(dataTax.getCategory(), "PPN")) {
                            taxImpliData.put("taxImplicationPPN", dataTax);
                        }
                    }
                newData.setSaTaxImplication(taxImpliData);
                // Sa Attachment
                Optional<List<M_ATTACHMENT>> saAttachments = mAttachmentRepo.findByReferenceIdAndCategoryIgnoreCase(content.get().getId(), "SERVICE_AGREEMENT");
                if (!saAttachments.isEmpty())
                    newData.setAttachment(saAttachments);

                Optional<List<M_PRODUCT_VERSION>> mProductVersionOpt = mProductVersionRepo.findAllByProductIdOrderByCreatedDateDesc(content.get().getProductId());

                if (mProductVersionOpt.isEmpty()) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
                            "Product Version not found", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }

                List<M_PRODUCT_VERSION> mProductVersion = mProductVersionOpt.get();
                List<VersionListDTO> vers = new ArrayList<>();

                if(!mProductVersion.isEmpty()) {
                    for(M_PRODUCT_VERSION  version: mProductVersion) {
                        VersionListDTO newArray = new VersionListDTO();
                        newArray.setId(version.getId());
                        newArray.setName(version.getVersion());
                        vers.add(newArray);
                    }

                    int productId = mProductVersion.get(0).getProductId();
                    int productVersionId = mProductVersion.get(0).getId();
                    Optional<M_PRODUCT> getProduct = mProductRepo.findById(productId);
                    ProductDetailDTO2 newDataProduct = new ProductDetailDTO2();
                    if(getProduct.isPresent()) {
                        newDataProduct.setProductName(getProduct.get().getProductName());
                        newDataProduct.setDescription(getProduct.get().getProductDescription());
                        newDataProduct.setProductType(getProduct.get().getProductTypeName());
                        newDataProduct.setServiceType(getProduct.get().getServiceTypeName());
                        newDataProduct.setProductClass(getProduct.get().getProductClassName());
                        // Product Detail
                        List<M_PRODUCT_DETAIL> productDetail = mProductDetailRepo.findAllByProductVersionId(productVersionId);
                        List<ProductDetailViewDto> productDetailDtos = new ArrayList<>();
                        if (!productDetail.isEmpty()) {
                            for (M_PRODUCT_DETAIL dtl : productDetail) {
                                ProductDetailViewDto productDetailDto = objectMapper.convertValue(dtl, ProductDetailViewDto.class);
                                if(dtl.getUom() != null) {
                                    productDetailDto.setUnitName(dtl.getUnitName());
                                }
                                productDetailDtos.add(productDetailDto);
                            }
                            newDataProduct.setProductDetail(productDetailDtos);
                        }
                        // Product Pricing
                        ProductDetailPricingDTO productDetailPricingDTO = new ProductDetailPricingDTO();
                        // priceCodeList
                        List<PriceCodeVersionDTO> pricingCodeDto = new ArrayList<>();
                        List<Integer> priceCodeId = new ArrayList<>();
                        // priceRuleList
                        List<PricingRuleDTO> pricingRuleDTO = new ArrayList<>();
                        // priceRuleTiering
                        List<PricingRuleDetailDTO> pricingRuleDetailDTO = new ArrayList<>();
                        List<M_PRODUCT_PRICING> mProductPricings = mProductPricingRepo.findAllByProductVersionId(productVersionId);

                        if (!mProductPricings.isEmpty()) {
                            for (M_PRODUCT_PRICING data : mProductPricings) {
                                PriceCodeVersionDTO newDto = new PriceCodeVersionDTO();
                                List<PriceCodeDetailDTO> listPriceCodeDetailDTO = new ArrayList<>();
                                newDto.setCreatedBy(data.getCreatedBy());
                                newDto.setCreatedDate(data.getCreatedDate());
                                newDto.setId(data.getPriceCodeId());
                                priceCodeId.add(data.getPriceCodeId());
                                List<R_PRICING_DETAIL> mPricingDetails = rPricingDetailRepo.findAllByIdPricing(data.getPriceCodeId());
                                for (R_PRICING_DETAIL dataDetail: mPricingDetails) {
                                    PriceCodeDetailDTO newDetailDto = new PriceCodeDetailDTO();
                                    newDetailDto.setCurrencyId(dataDetail.getCurrency());
                                    newDetailDto.setIdPricing(dataDetail.getIdPricing());
                                    newDetailDto.setCurrencyId(dataDetail.getCurrency());
                                    newDetailDto.setId(dataDetail.getId());
                                    Optional<R_GLOBAL_TYPE_VALUE> rGlobalTypeValue = rGlobalTypeValueRepo.findByGlbTypeValId(Integer.valueOf(dataDetail.getCurrency()));
                                    rGlobalTypeValue.ifPresent(globalType -> newDetailDto.setCurrency(globalType.getName()));
                                    newDetailDto.setDescription(dataDetail.getDescription());
                                    newDetailDto.setValue(dataDetail.getValue());
                                    newDetailDto.setUom(dataDetail.getUom());
                                    if(dataDetail.getUom() != null) {
                                        Optional<R_GLOBAL_TYPE_VALUE> rGlobalTypeValueUom = rGlobalTypeValueRepo.findByGlbTypeValId(Integer.valueOf(dataDetail.getUom()));
                                        rGlobalTypeValueUom.ifPresent(globalType -> newDetailDto.setUomName(globalType.getName()));
                                    }

                                    newDetailDto.setAdjustment(saDdlService.getPriceAdjustmentByCriteriaToDTO(dataInfo.getAccountId(), dataDetail, httpServletRequest));
                                    listPriceCodeDetailDTO.add(newDetailDto);
                                }
                                newDto.setMPricingDetail(listPriceCodeDetailDTO);
                                newDto.setPriceCode(data.getPriceCode());
                                newDto.setPriceDescription(data.getPricingDescription());
                                if(data.getPricingRuleValue() != null) {
                                    newDto.setStatus(data.getPricingRuleValue().getStatus());
                                }
                                newDto.setUpdateBy(data.getUpdatedBy());
                                newDto.setUpdateDate(data.getUpdatedDate());
                                // Price Code List
                                pricingCodeDto.add(newDto);

                                Optional<M_PRICING_RULE> mPricingRule = mPricingRuleRepo.findByPricingRuleId(data.getPricingRuleId());
                                PricingRuleDTO newPricingRule = new PricingRuleDTO();
                                if(mPricingRule.isPresent()) {

                                    // Price Rule List
                                    newPricingRule.setName(mPricingRule.get().getName());
                                    newPricingRule.setPricingRuleId(mPricingRule.get().getPricingRuleId());
                                    pricingRuleDTO.add(newPricingRule);

                                    // Price Rule Tiering
                                    List<VW_PRICING_RULE_DETAIL> mPricingRuleDetails = vwPricingRuleDetailRepo.findAllByPricingRuleId(mPricingRule.get().getPricingRuleId());
                                    if(!mPricingRuleDetails.isEmpty()) {
                                        for (VW_PRICING_RULE_DETAIL dataPricingRule : mPricingRuleDetails ) {
                                            // List version
                                            pricingRuleDetailDTO.add(saDdlService.getPriceAdjustmentTieringByCriteriaToDTO(dataPricingRule, dataInfo.getAccountId(), httpServletRequest));

                                        }
                                    }
                                }
                            }
                        }
                        productDetailPricingDTO.setPriceCodeList(pricingCodeDto);
                        productDetailPricingDTO.setPriceRuleList(pricingRuleDTO);
                        productDetailPricingDTO.setPriceRuleTiering(pricingRuleDetailDTO);
                        // Set Product Pricing
                        newDataProduct.setProductPricing(productDetailPricingDTO);
                        // Calc Rule
                        List<M_PRODUCT_CALCULATION_RULE> productCalculationRule = mProductCalculationRuleRepo
                                .findAllByProductVersionId(productVersionId);

                        List<CalcRuleProductDTO> calculationRuleViewDtos = new ArrayList<>();

                        if (!productCalculationRule.isEmpty()) {

                            for (M_PRODUCT_CALCULATION_RULE dtl : productCalculationRule) {
                                CalcRuleProductDTO calculationRuleViewDto = new CalcRuleProductDTO();
                                calculationRuleViewDto.setName(dtl.getName());
                                calculationRuleViewDto.setId(dtl.getId());
                                calculationRuleViewDto.setValue(dtl.getValue());
                                calculationRuleViewDto.setUom(dtl.getUom());
                                if(dtl.getUom() != null) {
                                    if(isStringAllDigits(dtl.getUom())) {
                                        Optional<R_GLOBAL_TYPE_VALUE> getUomName = rGlobalTypeValueRepo.findByGlbTypeValId(Integer.valueOf(dtl.getUom()));
                                        getUomName.ifPresent(rGlobalTypeValue -> calculationRuleViewDto.setUomName(rGlobalTypeValue.getName()));
                                    } else {
                                        Optional<M_RBI_TAX_CODE> mRbiTaxCode = mRbiTaxCodeRepo.findFirstByTaxCode(dtl.getUom());
                                        mRbiTaxCode.ifPresent(taxCode -> calculationRuleViewDto.setUomName(taxCode.getTaxCodeName()));
                                    }
                                }
                                calculationRuleViewDto.setProductVersionId(dtl.getProductVersionId());
                                calculationRuleViewDto.setNameId(dtl.getNameId());
                                calculationRuleViewDto.setCreatedBy(dtl.getCreatedBy());
                                calculationRuleViewDto.setCreatedDate(dtl.getCreatedDate());
                                calculationRuleViewDto.setUpdatedBy(dtl.getUpdatedBy());
                                calculationRuleViewDto.setUpdatedDate(dtl.getUpdatedDate());
                                calculationRuleViewDto.setDescription(dtl.getDescription());
                                calculationRuleViewDto.setCalculationType(dtl.getCalculationType());
                                calculationRuleViewDto.setFlag(null);
                                calculationRuleViewDto.setDeleted(dtl.getIsDeleted());
                                calculationRuleViewDtos.add(calculationRuleViewDto);
                            }

                            newDataProduct.setProductCalcRule(calculationRuleViewDtos);
                        }
                        // Product Tos
                        List<M_PRODUCT_TERM_OF_SERVICE> mProductTos = mProductTermOfServiceRepo.findAllByProductVersionId(productVersionId);
                        List<ViewTosProductDTO> tosDtos = new ArrayList<>();
                        if (!mProductTos.isEmpty()) {
                            for (M_PRODUCT_TERM_OF_SERVICE dtl : mProductTos) {
                                ViewTosProductDTO tosDto = new ViewTosProductDTO();
                                tosDto.setTosId(dtl.getTosId());
                                tosDto.setDescription(dtl.getDescription());
                                Optional<M_TOS> getTosName = mTosRepo.findById(dtl.getTosId());
                                getTosName.ifPresent(mTos -> tosDto.setTosName(mTos.getName()));
                                List<R_PRODUCT_TOS> newProductTos = rProductTermOfServiceRepo.findByIdMProductTos(dtl.getId());
                                if(!newProductTos.isEmpty()) {
                                    tosDto.setTosDetail(newProductTos);
                                }
                                tosDtos.add(tosDto);
                            }

                            newDataProduct.setProductTos(tosDtos);
                        }
                        // LateCharge
                        CriteriaDataDTO criteriaData = new CriteriaDataDTO();
                        criteriaData.setProductVersionId(productVersionId);
                        criteriaData.setServiceType(getProduct.get().getServiceType());
                        criteriaData.setAccountId(dataInfo.getAccountId());
                        criteriaData.setPriceCode(priceCodeId);
                        LinkedHashMap<String, Object> getLateCharge = saDdlService.lateChargeForDetail(criteriaData);
                        if(!getLateCharge.isEmpty()) {
                            newDataProduct.setLateCharge(getLateCharge);
                        }
                    }

                    newData.setVersionList(vers);
                    newData.setProduct(newDataProduct);
                }
                return new ResponseEntity<>(
                        ResponseObject.builder()
                                .success(ResponseUtils.SUCCESS_TRUE)
                                .code(HttpStatus.OK.value())
                                .message("Success Get Detail Service Agreement with Sa Id")
                                .data(newData)
                                .build(),
                        HttpStatus.OK
                );
            } else {
                result.setSuccess(ResponseUtils.SUCCESS_TRUE);
                result.setCode(HttpStatus.OK);
                result.setMessage("No Data!");
                result.setData(null);

                return new ResponseEntity<>(result, result.getHttpCode());
            }

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static boolean isStringAllDigits(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public ResponseEntity<ResponseObject> serviceAgreementViewDetailDraft(Integer saId) {
        logger.info("Get List View Detail Draft Service Agreement");

        ResponseObject result = new ResponseObject();

        try {
            SaDetailDraftDTO newData = new SaDetailDraftDTO();
            // SA info
            Optional<VW_SA> content = vwSaRepo.findById(saId);
            if (content.isPresent() && content.get().getJsonData() != null) {
                String jsonStr = content.get().getJsonData();
                objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                SaUpdateDraftDTO parameter = objectMapper.readValue(jsonStr, SaUpdateDraftDTO.class);
//                SaInfoDetailDraftDTO newDataInfo = objectMapper.convertValue(parameter, SaInfoDetailDraftDTO.class);
                SaInfoDetailDraftDTO newDataInfo = new SaInfoDetailDraftDTO();
                VW_SA getVwSa = content.get();
                newDataInfo.setAccountId(getVwSa.getAccountId());
                newDataInfo.setIsMain(getVwSa.getIsMain().equals("Y")?Boolean.TRUE:Boolean.FALSE);
                newDataInfo.setServiceType(content.get().getSaServiceTypeId());
                newDataInfo.setSaNumber(getVwSa.getSaNumber());
                newDataInfo.setSaType(getVwSa.getSaTypeId());
                newDataInfo.setPjbgType(getVwSa.getPjbgTypeId());
                newDataInfo.setSaDate(getVwSa.getSaDate());
                newDataInfo.setStartDate(getVwSa.getStartDate());
                newDataInfo.setSaReferenceNumber(getVwSa.getSaReferenceNumber());
                newDataInfo.setBillingCycle(getVwSa.getBillingCycleId());
                newDataInfo.setTermOfPayment(getVwSa.getTermOfPaymentId());
                newDataInfo.setInvoiceTemplate(getVwSa.getInvoiceTemplateId());
                newDataInfo.setGasInPlanDate(getVwSa.getGasInPlanDate());
                newDataInfo.setAlreadyGasIn(getVwSa.getAlreadyGasIn());
                newDataInfo.setEndDate(parameter.getSaInfo().getEndDate()!=null?parameter.getSaInfo().getEndDate():content.get().getEndDate());
                newDataInfo.setCommitmentDate(content.get().getComitmentDate());
                newDataInfo.setDescription(parameter.getSaInfo().getDescription()!=null?parameter.getSaInfo().getDescription():content.get().getDescription());

                newData.setSaInfo(newDataInfo);
                newData.setSaId(saId);
                newData.setAppHierId(parameter.getAppHierId());
                // Sa Attachment
                Optional<List<M_ATTACHMENT>> saAttachments = mAttachmentRepo.findByReferenceIdAndCategoryIgnoreCase(content.get().getId(), "SERVICE_AGREEMENT");
                if (saAttachments.isPresent()) {
                    newData.setAttachment(saAttachments);
                }
            }

            return new ResponseEntity<>(
                    ResponseObject.builder()
                            .success(ResponseUtils.SUCCESS_TRUE)
                            .code(HttpStatus.OK.value())
                            .message("Success Get Detail Service Agreement with Sa Id")
                            .data(newData.getSaId() != null ? newData : null)
                            .build(),
                    HttpStatus.OK
            );

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    @Transactional(rollbackFor = Exception.class, readOnly = false)
    public ResponseEntity<ResponseObject> serviceAgreementCreate(SaCreateDTO request, HttpServletRequest requestHttp) {
        logger.info("Create Service Agreement");
        Date newDate = new Date();
        ResponseObject result = new ResponseObject();

        try {
            // Sa info
            T_AM_SA newData = new T_AM_SA();

            newData.setCreatedBy(UserDetailUtils.getUsername());
            newData.setCreatedDate(newDate);

            if (request.getIsDraft()) {
                newData.setStatus("DRAFT");
                newData.setApprovalStatus("DRAFT");
            } else {
                newData.setStatus("DRAFT");
                newData.setApprovalStatus("WAITING APPROVAL");

                if(request.getSaInfo().getIsMain()) {
                    // active main
                    List<String> statusIn = new ArrayList<>();
                    statusIn.add(FlowStatus.APPROVED.name());
                    statusIn.add("WAITING APPROVAL");
                    Optional<VW_SA> saActive = vwSaRepo.findTopByAccountIdAndIsMainAndApprovalStatusIn(request.getSaInfo().getAccountId(), "Y", statusIn);
                    if(saActive.isPresent()) {
                        String message = UtilsAccount.messageValidateExist(ConstantAccount.SA_MAIN, (saActive.get().getApprovalStatus().equalsIgnoreCase(ConstantAccount.APPROVED)? ConstantAccount.APPROVED : ConstantAccount.WAITING_APPROVAL));
                        return this.serviceValidate(false, HttpStatus.BAD_REQUEST, message, null);
                    }
                }
            }
            // AccountId
            if (request.getSaInfo().getAccountId() == null) {
                String message = "Account id is required!";
                return this.serviceValidate(false, HttpStatus.BAD_REQUEST, message, null);
            }
            // customer not inactive
            Optional<VW_ACCOUNT_CRITERIA> getCustomerStatus = vwAccountCriteriaRepo.findByAccountId(request.getSaInfo().getAccountId());
            if(getCustomerStatus.isPresent()) {
                VW_ACCOUNT_CRITERIA getVwAccountCriteria = getCustomerStatus.get();
                if(getVwAccountCriteria.getCustomerStatus().equalsIgnoreCase(FlowStatus.INACTIVE.name())) {
                    String message = "You can't create service agreement, becuse this customer has been inactive!";
                    return this.serviceValidate(false, HttpStatus.BAD_REQUEST, message, null);
                }
            }
            newData.setAccountId(request.getSaInfo().getAccountId());
            // Service Type
            if (request.getSaInfo().getServiceType() == null) {
                String message = "Service type field is required!";
                return this.serviceValidate(false, HttpStatus.BAD_REQUEST, message, null);
            }
            newData.setSaServiceType(request.getSaInfo().getServiceType());
            // Service Agreement Number
            if (request.getSaInfo().getSaNumber() == null) {
                String message = "Service Agreement Number field is required!";
                return this.serviceValidate(false, HttpStatus.BAD_REQUEST, message, null);
            }
            Optional<T_AM_SA> getDuplicateSaNumber = tAmSARepo.findBySaNumber(request.getSaInfo().getSaNumber());
            if(getDuplicateSaNumber.isPresent()) {
                boolean isDuplicate = getDuplicateSaNumber.get().getSaNumber().equals(request.getSaInfo().getSaNumber());
                if(isDuplicate) {
                    String message = "Service Agreement Number is already exist!";
                    return this.serviceValidate(false, HttpStatus.BAD_REQUEST, message, null);
                }
            }
            newData.setSaNumber(request.getSaInfo().getSaNumber());
            // Service Agreement Reference Number
            if (!request.getSaInfo().getIsMain() && request.getSaInfo().getSaReferenceNumber() == null) {
                String message = "SA Reference Id field is required!";
                return this.serviceValidate(false, HttpStatus.BAD_REQUEST, message, null);
            }
            newData.setSaReferenceNumber(request.getSaInfo().getSaReferenceNumber());
            // Service Agreement Type
            if (request.getSaInfo().getSaType() == null) {
                String message = "Service Agreement Type field is required!";
                return this.serviceValidate(false, HttpStatus.BAD_REQUEST, message, null);
            }
            newData.setSaType(request.getSaInfo().getSaType());
            // PJBG Type
            if (request.getSaInfo().getSaType() == null) {
                String message = "PJBG type field is required!";
                return this.serviceValidate(false, HttpStatus.BAD_REQUEST, message, null);
            }
            newData.setPjbgType(request.getSaInfo().getPjbgType());
            // Sa Date
            if (request.getSaInfo().getSaDate() == null) {
                String message = "Service Agreement date field is required!";
                return this.serviceValidate(false, HttpStatus.BAD_REQUEST, message, null);
            }
            newData.setSaDate(request.getSaInfo().getSaDate());
            // Start Date
            if (request.getSaInfo().getStartDate() == null) {
                String message = "Start date field is required!";
                return this.serviceValidate(false, HttpStatus.BAD_REQUEST, message, null);
            }
            newData.setStartDate(request.getSaInfo().getStartDate());
            if (request.getSaInfo().getEndDate() != null) {
                newData.setEndDate(request.getSaInfo().getEndDate());
            }
            // End Date
            if (request.getSaInfo().getBillingCycle() == null) {
                String message = "Billing Cycle field is required!";
                return this.serviceValidate(false, HttpStatus.BAD_REQUEST, message, null);
            }
            newData.setBillingCycle(request.getSaInfo().getBillingCycle());
            // Term of Payment
            if (request.getSaInfo().getTermOfPayment() == null) {
                String message = "Term of payment field is required!";
                return this.serviceValidate(false, HttpStatus.BAD_REQUEST, message, null);
            }
            newData.setTermOfPayment(request.getSaInfo().getTermOfPayment());
            // Invoice Template
            if (request.getSaInfo().getInvoiceTemplate() == null) {
                String message = "Invoice template field is required!";
                return this.serviceValidate(false, HttpStatus.BAD_REQUEST, message, null);
            }
            newData.setInvoiceTemplate(request.getSaInfo().getInvoiceTemplate());
            // Gas in Plan Date
            if (request.getSaInfo().getInvoiceTemplate() == null) {
                String message = "Gas in plan date field is required!";
                return this.serviceValidate(false, HttpStatus.BAD_REQUEST, message, null);
            }
            newData.setAlreadyGasIn(request.getSaInfo().getAlreadyGasIn());
            newData.setGasInPlanDate(request.getSaInfo().getGasInPlanDate());
            newData.setComitmentDate(request.getSaInfo().getCommitmentDate());
            newData.setDescription(removeSpace(request.getSaInfo().getDescription()));
            if (request.getAppHierId() != null) {
                newData.setAppHierId(request.getAppHierId());
            }
            // Not required Data
            if (request.getSaInfo().getIsMain())
                newData.setIsMain("Y");
            else
                newData.setIsMain("N");
            newData.setIsCustom(request.getSaDetail().getIsCustom());
            // ID_M_PRICING
            if (request.getSaDetail().getProductPricing().getPriceCodeId() != null) {
                newData.setIdMPricing(request.getSaDetail().getProductPricing().getPriceCodeId());
            }
            // IS_PRICING_RULE
            if (request.getSaDetail().getProductPricing().getPriceRuleId() != null) {
                newData.setIsPricingRule("Y");
                newData.setIdPricingRule(request.getSaDetail().getProductPricing().getPriceRuleId());
            } else {
                newData.setIsPricingRule("N");
            }
            if (request.getSaDetail().getProductVersionId() != null) {
                newData.setIdProductVersion(request.getSaDetail().getProductVersionId());
            }

            // Sa Detail Header
            T_AM_SA_DETAIL_HEADER tAmSaDetailHeader = new T_AM_SA_DETAIL_HEADER();
            tAmSaDetailHeader.setCreatedDate(newDate);
            tAmSaDetailHeader.setCreatedBy(UserDetailUtils.getUsername());
            tAmSaDetailHeader.setStatus("ACTIVE");
            tAmSaDetailHeader.setStartDate(newDate);

            // CalcRule Header
            T_AM_SA_CALCRULE_HEADER tAmSaCalcruleHeader = new T_AM_SA_CALCRULE_HEADER();

            tAmSaCalcruleHeader.setCreatedDate(newDate);
            tAmSaCalcruleHeader.setCreatedBy(UserDetailUtils.getUsername());
            tAmSaCalcruleHeader.setStatus("ACTIVE");
            tAmSaCalcruleHeader.setStartDate(newDate);

            // Save SA
            T_AM_SA sa = tAmSARepo.save(newData);
            // T_AM_SA_DETAIL CREATE
            tAmSaDetailHeader.setSaId(sa.getId());
            T_AM_SA_DETAIL_HEADER getDetailHeader = tAmSADetailHeaderRepo.save(tAmSaDetailHeader);
            // T_AM_SA_DETAIL_DTL CREATE
            if (!request.getSaDetail().getProductDetail().isEmpty()) {
                for (ListSaDetailDTO data : request.getSaDetail().getProductDetail()) {
                    T_AM_SA_DETAIL_DTL newDtl = new T_AM_SA_DETAIL_DTL();
                    newDtl.setCreatedDate(newDate);
                    newDtl.setStatus("ACTIVE");
                    newDtl.setCreatedBy(UserDetailUtils.getUsername());
                    newDtl.setSaDetailHeaderId(getDetailHeader.getId());
                    newDtl.setDescription(data.getDescription());
                    newDtl.setName(data.getName());
                    if (data.getUnit() != null) {
                        newDtl.setUnit(Integer.valueOf(data.getUnit()));
                    }
                    newDtl.setValue(data.getValue());
                    tAmSADetailDTLRepo.save(newDtl);
                }
            }
            // T_AM_SA_CALC CREATE
            tAmSaCalcruleHeader.setSaId(sa.getId());
            T_AM_SA_CALCRULE_HEADER getCalcHeader = tAmSACalcRuleHeaderRepo.save(tAmSaCalcruleHeader);
            // T_AM_SA_CALC_DTL CREATE
            if (!request.getSaDetail().getProductCalcRule().isEmpty()) {
                for (ListSaDetailDTO data : request.getSaDetail().getProductCalcRule()) {
                    T_AM_SA_CALCRULE_DTL newDtl = new T_AM_SA_CALCRULE_DTL();
                    newDtl.setCreatedDate(newDate);
                    newDtl.setStatus("ACTIVE");
                    newDtl.setCreatedBy(UserDetailUtils.getUsername());
                    newDtl.setSaCalcruleHeaderId(getCalcHeader.getId());
                    newDtl.setName(data.getName());
                    newDtl.setUnit(data.getUnit());
                    newDtl.setValue(data.getValue());
                    newDtl.setDescription(data.getDescription());
                    tAmSaCalcRuleDTLRepo.save(newDtl);
                }
            }
            // T_AM_SA_TOS CREATE
            for (SaProductTosCreateDTO getRequestTos : request.getSaDetail().getProductTOS()) {
                T_AM_SA_TOS_HEADER tAmSaTosHeader = new T_AM_SA_TOS_HEADER();

                tAmSaTosHeader.setCreatedDate(newDate);
                tAmSaTosHeader.setCreatedBy(UserDetailUtils.getUsername());
                tAmSaTosHeader.setStatus("ACTIVE");
                tAmSaTosHeader.setMTosId(getRequestTos.getTosId());
                tAmSaTosHeader.setSaId(sa.getId());
                T_AM_SA_TOS_HEADER getTosHeader = tAmSATosHeaderRepo.save(tAmSaTosHeader);
                // T_AM_SA_TOS_DTL CREATE
                if (getRequestTos.getTosDetail() != null) {
                    for (SaTosDetailDTO data : getRequestTos.getTosDetail()) {
                        T_AM_SA_TOS_DTL newDtl = new T_AM_SA_TOS_DTL();
                        newDtl.setCreatedDate(newDate);
                        newDtl.setStatus("ACTIVE");
                        newDtl.setCreatedBy(UserDetailUtils.getUsername());
                        newDtl.setSaTosId(getTosHeader.getId());
                        newDtl.setFromItem(data.getFromItem());
                        newDtl.setAttribute(data.getAttribute());
                        newDtl.setUnit(data.getUnit());
                        newDtl.setValue(data.getValue());
                        tAmSATosDTLRepo.save(newDtl);
                    }
                }
            }
            // PRC ADJUSTMENT HEADER SAVE
            if(request.getSaDetail().getProductPricing().getPriceAdjustment() != null) {
                for (Integer priceAdjustmentId : request.getSaDetail().getProductPricing().getPriceAdjustment()) {
                    T_AM_SA_PRCADJUSTMENT_HEADER prcAdjustmentHeader = new T_AM_SA_PRCADJUSTMENT_HEADER();
                    prcAdjustmentHeader.setSaId(sa.getId());
                    prcAdjustmentHeader.setRPricingAdjustmentDetailId(priceAdjustmentId);
                    prcAdjustmentHeader.setCreatedBy(UserDetailUtils.getUsername());
                    prcAdjustmentHeader.setCreatedDate(newDate);
                    prcAdjustmentHeader.setStartDate(newDate);
                    prcAdjustmentHeader.setStatus("ACTIVE");
                    tAmSaPrcAdjustmentHeaderRepo.save(prcAdjustmentHeader);
                }
            }
            // T_AM_SA_PRC_RULE
            if (!request.getSaDetail().getProductPricing().getPriceRuleTiering().isEmpty()) {
                for (SaPrcRuleDTO data : request.getSaDetail().getProductPricing().getPriceRuleTiering()) {
                    T_AM_SA_PRC_RULE_DTL newDtl = new T_AM_SA_PRC_RULE_DTL();
                    newDtl.setSaId(sa.getId());
                    newDtl.setCreatedDate(newDate);
                    newDtl.setStatus("ACTIVE");
                    newDtl.setCreatedBy(UserDetailUtils.getUsername());
                    newDtl.setIdMPricing(data.getPriceId());
                    if (data.getIsUnlimited() != null) {
                        newDtl.setIsUnlim(data.getIsUnlimited() ? "Y" : "N");
                    }
                    newDtl.setLineNumber(data.getLine());
                    newDtl.setMax(data.getMax());
                    newDtl.setMin(data.getMin());

                    T_AM_SA_PRC_RULE_DTL prcRuleId = tAmSAPrcRuleDTLRepo.save(newDtl);

                    // Price Code PRC adjustment
                    if(data.getPriceAdjustment() != null) {
                        for (Integer priceAdjustmentId : data.getPriceAdjustment()) {
                            T_AM_SA_PRC_RULE_PRCADJUSTMENT_HEADER tAmSaPrcRulePrcadjustmentHeader = new T_AM_SA_PRC_RULE_PRCADJUSTMENT_HEADER();
                            tAmSaPrcRulePrcadjustmentHeader.setPrcRuleDtlId(prcRuleId.getId());
                            tAmSaPrcRulePrcadjustmentHeader.setRPricingAdjustmentDetailId(priceAdjustmentId);
                            tAmSaPrcRulePrcadjustmentHeader.setCreatedBy(UserDetailUtils.getUsername());
                            tAmSaPrcRulePrcadjustmentHeader.setCreatedDate(newDate);
                            tAmSaPrcRulePrcadjustmentHeader.setStartDate(newDate);
                            tAmSaPrcRulePrcadjustmentHeader.setStatus("ACTIVE");
                            tAmSaPrcRulePrcAdjustmentHeaderRepo.save(tAmSaPrcRulePrcadjustmentHeader);
                        }
                    }
                }
            }

            // Tax Implication Header
            if (request.getSaDetail().getTaxImplicationPPN() != null) {
                T_AM_SA_TAXIMP_HEADER tAmSaTaximpHeader = new T_AM_SA_TAXIMP_HEADER();
                tAmSaTaximpHeader.setCreatedDate(newDate);
                tAmSaTaximpHeader.setCreatedBy(UserDetailUtils.getUsername());
                tAmSaTaximpHeader.setStatus("ACTIVE");
                tAmSaTaximpHeader.setStartDate(newDate);
                tAmSaTaximpHeader.setTaxImplicationId(request.getSaDetail().getTaxImplicationPPN());
                tAmSaTaximpHeader.setSaId(sa.getId());
                tAmSATaxImpHeaderRepo.save(tAmSaTaximpHeader);
            }

            if (request.getSaDetail().getTaxImplicationPPh() != null) {
                T_AM_SA_TAXIMP_HEADER tAmSaTaximpHeader = new T_AM_SA_TAXIMP_HEADER();
                tAmSaTaximpHeader.setCreatedDate(newDate);
                tAmSaTaximpHeader.setCreatedBy(UserDetailUtils.getUsername());
                tAmSaTaximpHeader.setStatus("ACTIVE");
                tAmSaTaximpHeader.setStartDate(newDate);
                tAmSaTaximpHeader.setTaxImplicationId(request.getSaDetail().getTaxImplicationPPh());
                tAmSaTaximpHeader.setSaId(sa.getId());
                tAmSATaxImpHeaderRepo.save(tAmSaTaximpHeader);
            }

            // Late Charge Header
            if (request.getSaDetail().getLateChargeIDR() != null) {
                T_AM_SA_LATECHARGE_HEADER tAmSaLatechargeHeader = new T_AM_SA_LATECHARGE_HEADER();
                tAmSaLatechargeHeader.setCreatedDate(newDate);
                tAmSaLatechargeHeader.setCreatedBy(UserDetailUtils.getUsername());
                tAmSaLatechargeHeader.setStatus("ACTIVE");
                tAmSaLatechargeHeader.setStartDate(newDate);
                tAmSaLatechargeHeader.setLateChargesId(request.getSaDetail().getLateChargeIDR());
                tAmSaLatechargeHeader.setSaId(sa.getId());
                tAmSALateChargeHeaderRepo.save(tAmSaLatechargeHeader);
            }

            if (request.getSaDetail().getLateChargeUSD() != null) {
                T_AM_SA_LATECHARGE_HEADER tAmSaLatechargeHeader = new T_AM_SA_LATECHARGE_HEADER();
                tAmSaLatechargeHeader.setCreatedDate(newDate);
                tAmSaLatechargeHeader.setCreatedBy(UserDetailUtils.getUsername());
                tAmSaLatechargeHeader.setStatus("ACTIVE");
                tAmSaLatechargeHeader.setStartDate(newDate);
                tAmSaLatechargeHeader.setLateChargesId(request.getSaDetail().getLateChargeUSD());
                tAmSaLatechargeHeader.setSaId(sa.getId());
                tAmSALateChargeHeaderRepo.save(tAmSaLatechargeHeader);
            }

            if (!request.getIsDraft()) {
                Integer tAppId = approvalServices.addApprovalFlowR(request.getAppHierId(),
                        ApprovalCategory.SERVICE_AGREEMENT.name(), sa.getId().toString(), null);
                approvalServices.setApprovalHistory(tAppId, sa.getId(), "Create new Service Agreement", APP_CATEGORY,
                        SUBMIT, requestHttp);
            }

            LinkedHashMap<String, Object> successTosResponse = new LinkedHashMap<>();
            successTosResponse.put("saId", sa.getId());
            successTosResponse.put("appHierId", request.getAppHierId());
            successTosResponse.put("description", request.getSaInfo().getDescription());
            successTosResponse.put("createdDate", new Date());
            successTosResponse.put("createdBy", UserDetailUtils.getUsername());

            return new ResponseEntity<>(
                    ResponseObject.builder()
                            .success(ResponseUtils.SUCCESS_TRUE)
                            .code(HttpStatus.OK.value())
                            .message("Success Create Service Agreement")
                            .data(successTosResponse)
                            .build(),
                    HttpStatus.OK
            );

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    @Transactional(rollbackFor = Exception.class, readOnly = false)
    public T_AM_SA saUpdateDraft(SaUpdateDraftDTO request, Integer saId, HttpServletRequest httpServletRequest) {
        logger.info("Update Draft Service Agreement");
        Date newDate = new Date();

        Optional<T_AM_SA> dataSa = tAmSARepo.findById(saId);
        if (dataSa.isPresent()) {
            // Sa info
            T_AM_SA newData = dataSa.get();
            newData.setUpdatedBy(UserDetailUtils.getUsername());
            newData.setUpdatedDate(newDate);
            // Account Id
            newData.setAccountId(request.getSaInfo().getAccountId());
            // Service Type
            newData.setSaServiceType(request.getSaInfo().getServiceType());
            // Service Agreement Number
            newData.setSaNumber(request.getSaInfo().getSaNumber());
            // Service Agreement Reference Number
            newData.setSaReferenceNumber(request.getSaInfo().getSaReferenceNumber());
            // Service Agreement Type
            newData.setSaType(request.getSaInfo().getSaType());
            // PJBG Type
            newData.setPjbgType(request.getSaInfo().getPjbgType());
            // Sa Date
            newData.setSaDate(request.getSaInfo().getSaDate());
            // Start Date
            newData.setStartDate(request.getSaInfo().getStartDate());
            // End Date
            newData.setEndDate(request.getSaInfo().getEndDate());
            // Billing Cycle
            newData.setBillingCycle(request.getSaInfo().getBillingCycle());
            // Term of Payment
            newData.setTermOfPayment(request.getSaInfo().getTermOfPayment());
            // Invoice Template
            newData.setInvoiceTemplate(request.getSaInfo().getInvoiceTemplate());
            // already gas in
            newData.setAlreadyGasIn(request.getSaInfo().getAlreadyGasIn());
            // Gas in Plan Date
            newData.setGasInPlanDate(request.getSaInfo().getGasInPlanDate());
            // Commitment date
            newData.setComitmentDate(request.getSaInfo().getCommitmentDate());
            // Description
            newData.setDescription(request.getSaInfo().getDescription());
            // Not required Data
            if (request.getSaInfo().getIsMain())
                newData.setIsMain("Y");
            else
                newData.setIsMain("N");
            newData.setIsCustom(request.getSaDetail().getIsCustom());
            // ID_M_PRICING
            if (request.getSaDetail().getProductPricing().getPriceCodeId() != null) {
                newData.setIdMPricing(request.getSaDetail().getProductPricing().getPriceCodeId());
            }
            // IS_PRICING_RULE
            if (request.getSaDetail().getProductPricing().getPriceRuleId() != null) {
                newData.setIsPricingRule("Y");
                newData.setIdPricingRule(request.getSaDetail().getProductPricing().getPriceRuleId());
            } else {
                newData.setIsPricingRule("N");
            }
            if (request.getSaDetail().getProductVersionId() != null) {
                newData.setIdProductVersion(request.getSaDetail().getProductVersionId());
            }

            // Sa Detail Header
            Optional<T_AM_SA_DETAIL_HEADER> tAmSaDetailHeader = tAmSADetailHeaderRepo.findBySaId(saId);
            if (tAmSaDetailHeader.isPresent()) {
                tAmSaDetailHeader.get().setUpdatedDate(newDate);
                tAmSaDetailHeader.get().setUpdatedBy(UserDetailUtils.getUsername());
            }

            // CalcRule Header
            Optional<T_AM_SA_CALCRULE_HEADER> tAmSaCalcruleHeader = tAmSACalcRuleHeaderRepo.findBySaId(saId);
            if (tAmSaCalcruleHeader.isPresent()) {
                tAmSaCalcruleHeader.get().setUpdatedDate(newDate);
                tAmSaCalcruleHeader.get().setUpdatedBy(UserDetailUtils.getUsername());
            }

            // Save SA
            T_AM_SA sa = tAmSARepo.save(newData);
            // T_AM_SA_DETAIL Update
            if (tAmSaDetailHeader.isPresent()) {
                T_AM_SA_DETAIL_HEADER getDetailHeader = tAmSADetailHeaderRepo.save(tAmSaDetailHeader.get());
                // T_AM_SA_DETAIL_DTL Update
                List<T_AM_SA_DETAIL_DTL> oldDetailDtl = tAmSADetailDTLRepo.findAllBySaDetailHeaderId(getDetailHeader.getId());
                if (!oldDetailDtl.isEmpty()) {
                    for (T_AM_SA_DETAIL_DTL dataDetailDtl : oldDetailDtl) {
                        tAmSADetailDTLRepo.deleteById(dataDetailDtl.getId());
                    }
                }
                if (!request.getSaDetail().getProductDetail().isEmpty()) {
                    for (ListSaDetailDTO data : request.getSaDetail().getProductDetail()) {
                        T_AM_SA_DETAIL_DTL newDtl = new T_AM_SA_DETAIL_DTL();
                        newDtl.setCreatedDate(newDate);
                        newDtl.setStatus("ACTIVE");
                        newDtl.setCreatedBy(UserDetailUtils.getUsername());
                        newDtl.setSaDetailHeaderId(getDetailHeader.getId());
                        newDtl.setName(data.getName());
                        if(data.getUnit() != null) {
                            newDtl.setUnit(Integer.valueOf(data.getUnit()));
                        }
                        newDtl.setValue(data.getValue());
                        tAmSADetailDTLRepo.save(newDtl);
                    }
                }
            }
            // T_AM_SA_CALC CREATE
            if (tAmSaCalcruleHeader.isPresent()) {
                T_AM_SA_CALCRULE_HEADER getCalcHeader = tAmSACalcRuleHeaderRepo.save(tAmSaCalcruleHeader.get());
                // T_AM_SA_CALC_DTL CREATE
                List<T_AM_SA_CALCRULE_DTL> dataOldCalcRule = tAmSaCalcRuleDTLRepo.findAllBySaCalcruleHeaderId(getCalcHeader.getId());
                if (!dataOldCalcRule.isEmpty()) {
                    for (T_AM_SA_CALCRULE_DTL dataDtlCalcule : dataOldCalcRule) {
                        tAmSaCalcRuleDTLRepo.deleteById(dataDtlCalcule.getId());
                    }
                }
                if (!request.getSaDetail().getProductCalcRule().isEmpty()) {
                    for (ListSaDetailDTO data : request.getSaDetail().getProductCalcRule()) {
                        T_AM_SA_CALCRULE_DTL newDtl = new T_AM_SA_CALCRULE_DTL();
                        newDtl.setCreatedDate(newDate);
                        newDtl.setStatus("ACTIVE");
                        newDtl.setCreatedBy(UserDetailUtils.getUsername());
                        newDtl.setSaCalcruleHeaderId(getCalcHeader.getId());
                        newDtl.setName(data.getName());
                        newDtl.setUnit(data.getUnit());
                        newDtl.setValue(data.getValue());
                        tAmSaCalcRuleDTLRepo.save(newDtl);
                    }
                }
            }
            // T_AM_SA_TOS CREATE
            List<T_AM_SA_TOS_HEADER> getDataTosHeaderOld = tAmSATosHeaderRepo.findAllBySaId(saId);
            if (!getDataTosHeaderOld.isEmpty()) {
                for (T_AM_SA_TOS_HEADER tosHeaders : getDataTosHeaderOld) {
                    List<T_AM_SA_TOS_DTL> getDataOldTosDtl = tAmSATosDTLRepo.findAllBySaTosId(tosHeaders.getId());
                    if (!getDataOldTosDtl.isEmpty()) {
                        for (T_AM_SA_TOS_DTL dataOldTosDtl : getDataOldTosDtl) {
                            tAmSATosDTLRepo.deleteById(dataOldTosDtl.getId());
                        }
                    }
                    tAmSATosHeaderRepo.deleteById(tosHeaders.getId());
                }
            }
            for (SaProductTosCreateDTO getRequestTos : request.getSaDetail().getProductTOS()) {
                T_AM_SA_TOS_HEADER tAmSaTosHeader = new T_AM_SA_TOS_HEADER();

                tAmSaTosHeader.setCreatedDate(newDate);
                tAmSaTosHeader.setCreatedBy(UserDetailUtils.getUsername());
                tAmSaTosHeader.setStatus("ACTIVE");
                tAmSaTosHeader.setMTosId(getRequestTos.getTosId());
                tAmSaTosHeader.setSaId(sa.getId());
                T_AM_SA_TOS_HEADER getTosHeader = tAmSATosHeaderRepo.save(tAmSaTosHeader);
                // T_AM_SA_TOS_DTL CREATE
                if (!getRequestTos.getTosDetail().isEmpty()) {
                    for (SaTosDetailDTO data : getRequestTos.getTosDetail()) {
                        T_AM_SA_TOS_DTL newDtl = new T_AM_SA_TOS_DTL();
                        newDtl.setCreatedDate(newDate);
                        newDtl.setStatus("ACTIVE");
                        newDtl.setCreatedBy(UserDetailUtils.getUsername());
                        newDtl.setSaTosId(getTosHeader.getId());
                        newDtl.setFromItem(data.getFromItem());
                        newDtl.setAttribute(data.getAttribute());
                        newDtl.setUnit(data.getUnit());
                        newDtl.setValue(data.getValue());
                        tAmSATosDTLRepo.save(newDtl);
                    }
                }
            }

            // PRC CODE ADJUSTMENT UPDATE
            if(request.getSaDetail().getProductPricing().getPriceAdjustment() != null) {
                List<T_AM_SA_PRCADJUSTMENT_HEADER> tAmSaPrcadjustmentHeaderOlds = tAmSaPrcAdjustmentHeaderRepo.findAllBySaId(saId);
                if(!tAmSaPrcadjustmentHeaderOlds.isEmpty()) {
                    for (T_AM_SA_PRCADJUSTMENT_HEADER dataPrcAdjustmentOld : tAmSaPrcadjustmentHeaderOlds) {
                        tAmSaPrcAdjustmentHeaderRepo.deleteById(dataPrcAdjustmentOld.getId());
                    }
                }
                for (Integer priceAdjustmentId : request.getSaDetail().getProductPricing().getPriceAdjustment()) {
                    T_AM_SA_PRCADJUSTMENT_HEADER prcAdjustmentHeader = new T_AM_SA_PRCADJUSTMENT_HEADER();
                    prcAdjustmentHeader.setSaId(sa.getId());
                    prcAdjustmentHeader.setRPricingAdjustmentDetailId(priceAdjustmentId);
                    prcAdjustmentHeader.setCreatedBy(UserDetailUtils.getUsername());
                    prcAdjustmentHeader.setCreatedDate(newDate);
                    prcAdjustmentHeader.setStartDate(newDate);
                    prcAdjustmentHeader.setStatus("ACTIVE");
                    tAmSaPrcAdjustmentHeaderRepo.save(prcAdjustmentHeader);
                }
            }

            // T_AM_SA_PRC_RULE
            if (!request.getSaDetail().getProductPricing().getPriceRuleTiering().isEmpty()) {
                List<T_AM_SA_PRC_RULE_DTL> tAmSaPrcRuleDtlOld = tAmSAPrcRuleDTLRepo.findAllBySaId(saId);
                if (!tAmSaPrcRuleDtlOld.isEmpty()) {
                    for (T_AM_SA_PRC_RULE_DTL dataTAmSaPrcRuleDtl : tAmSaPrcRuleDtlOld) {
                        tAmSAPrcRuleDTLRepo.deleteById(dataTAmSaPrcRuleDtl.getId());
                    }
                }

                for (SaPrcRuleDTO data : request.getSaDetail().getProductPricing().getPriceRuleTiering()) {
                    T_AM_SA_PRC_RULE_DTL newDtl = new T_AM_SA_PRC_RULE_DTL();
                    newDtl.setSaId(saId);
                    newDtl.setCreatedDate(newDate);
                    newDtl.setStatus("ACTIVE");
                    newDtl.setCreatedBy(UserDetailUtils.getUsername());
                    newDtl.setIdMPricing(data.getPriceId());
                    newDtl.setIsUnlim(data.getIsUnlimited() ? "Y" : "N");
                    newDtl.setLineNumber(data.getLine());
                    newDtl.setMax(data.getMax());
                    newDtl.setMin(data.getMin());

                    T_AM_SA_PRC_RULE_DTL prcRuleId = tAmSAPrcRuleDTLRepo.save(newDtl);

                    // Price Code PRC adjustment
                    List<T_AM_SA_PRC_RULE_PRCADJUSTMENT_HEADER> tAmSaPrcRuleadjustmentHeaders = tAmSaPrcRulePrcAdjustmentHeaderRepo.findAllByPrcRuleDtlId(prcRuleId.getId());
                    if(!tAmSaPrcRuleadjustmentHeaders.isEmpty()) {
                        for (T_AM_SA_PRC_RULE_PRCADJUSTMENT_HEADER dataPrcRuleAdjustmentOld : tAmSaPrcRuleadjustmentHeaders) {
                            tAmSaPrcRulePrcAdjustmentHeaderRepo.deleteById(dataPrcRuleAdjustmentOld.getId());
                        }
                    }
                    if(!data.getPriceAdjustment().isEmpty()){
                        for (Integer priceAdjustmentId : data.getPriceAdjustment()) {
                            T_AM_SA_PRC_RULE_PRCADJUSTMENT_HEADER tAmSaPrcRulePrcadjustmentHeader = new T_AM_SA_PRC_RULE_PRCADJUSTMENT_HEADER();
                            tAmSaPrcRulePrcadjustmentHeader.setPrcRuleDtlId(prcRuleId.getId());
                            tAmSaPrcRulePrcadjustmentHeader.setRPricingAdjustmentDetailId(priceAdjustmentId);
                            tAmSaPrcRulePrcadjustmentHeader.setCreatedBy(UserDetailUtils.getUsername());
                            tAmSaPrcRulePrcadjustmentHeader.setCreatedDate(newDate);
                            tAmSaPrcRulePrcadjustmentHeader.setStartDate(newDate);
                            tAmSaPrcRulePrcadjustmentHeader.setStatus("ACTIVE");
                            tAmSaPrcRulePrcAdjustmentHeaderRepo.save(tAmSaPrcRulePrcadjustmentHeader);
                        }
                    }
                }
            }

            // Tax Implication Header
            List<T_AM_SA_TAXIMP_HEADER> tAmSaTaximpHeadersOld = tAmSATaxImpHeaderRepo.findAllBySaId(saId);
            if (!tAmSaTaximpHeadersOld.isEmpty()) {
                for (T_AM_SA_TAXIMP_HEADER taxImpliData : tAmSaTaximpHeadersOld) {
                    tAmSATaxImpHeaderRepo.deleteById(taxImpliData.getId());
                }
            }
            if (request.getSaDetail().getTaxImplicationPPN() != null) {
                T_AM_SA_TAXIMP_HEADER tAmSaTaximpHeader = new T_AM_SA_TAXIMP_HEADER();
                tAmSaTaximpHeader.setCreatedDate(newDate);
                tAmSaTaximpHeader.setCreatedBy(UserDetailUtils.getUsername());
                tAmSaTaximpHeader.setStatus("ACTIVE");
                tAmSaTaximpHeader.setStartDate(newDate);
                tAmSaTaximpHeader.setTaxImplicationId(request.getSaDetail().getTaxImplicationPPN());
                tAmSaTaximpHeader.setSaId(sa.getId());
                tAmSATaxImpHeaderRepo.save(tAmSaTaximpHeader);
            }

            if (request.getSaDetail().getTaxImplicationPPh() != null) {
                T_AM_SA_TAXIMP_HEADER tAmSaTaximpHeader = new T_AM_SA_TAXIMP_HEADER();
                tAmSaTaximpHeader.setCreatedDate(newDate);
                tAmSaTaximpHeader.setCreatedBy(UserDetailUtils.getUsername());
                tAmSaTaximpHeader.setStatus("ACTIVE");
                tAmSaTaximpHeader.setStartDate(newDate);
                tAmSaTaximpHeader.setTaxImplicationId(request.getSaDetail().getTaxImplicationPPh());
                tAmSaTaximpHeader.setSaId(sa.getId());
                tAmSATaxImpHeaderRepo.save(tAmSaTaximpHeader);
            }
            // Late Charge Header
            List<T_AM_SA_LATECHARGE_HEADER> tAmSaLatechargeHeaderOld = tAmSALateChargeHeaderRepo.findAllBySaId(saId);
            if (!tAmSaLatechargeHeaderOld.isEmpty()) {
                for (T_AM_SA_LATECHARGE_HEADER lateChargeData : tAmSaLatechargeHeaderOld) {
                    tAmSALateChargeHeaderRepo.deleteById(lateChargeData.getId());
                }
            }
            if (request.getSaDetail().getLateChargeIDR() != null) {
                T_AM_SA_LATECHARGE_HEADER tAmSaLatechargeHeader = new T_AM_SA_LATECHARGE_HEADER();
                tAmSaLatechargeHeader.setCreatedDate(newDate);
                tAmSaLatechargeHeader.setCreatedBy(UserDetailUtils.getUsername());
                tAmSaLatechargeHeader.setStatus("ACTIVE");
                tAmSaLatechargeHeader.setStartDate(newDate);
                tAmSaLatechargeHeader.setLateChargesId(request.getSaDetail().getLateChargeIDR());
                tAmSaLatechargeHeader.setSaId(sa.getId());
                tAmSALateChargeHeaderRepo.save(tAmSaLatechargeHeader);
            }

            if (request.getSaDetail().getLateChargeUSD() != null) {
                T_AM_SA_LATECHARGE_HEADER tAmSaLatechargeHeader = new T_AM_SA_LATECHARGE_HEADER();
                tAmSaLatechargeHeader.setCreatedDate(newDate);
                tAmSaLatechargeHeader.setCreatedBy(UserDetailUtils.getUsername());
                tAmSaLatechargeHeader.setStatus("ACTIVE");
                tAmSaLatechargeHeader.setStartDate(newDate);
                tAmSaLatechargeHeader.setLateChargesId(request.getSaDetail().getLateChargeUSD());
                tAmSaLatechargeHeader.setSaId(sa.getId());
                tAmSALateChargeHeaderRepo.save(tAmSaLatechargeHeader);
            }

            LinkedHashMap<String, Object> successTosResponse = new LinkedHashMap<>();
            successTosResponse.put("saId", saId);
            successTosResponse.put("appHierId", request.getAppHierId());
            successTosResponse.put("description", request.getSaInfo().getDescription());
            successTosResponse.put("createdDate", new Date());
            successTosResponse.put("createdBy", UserDetailUtils.getUsername());

            return sa;
        }

        return null;
    }

    public ResponseEntity<ResponseObject> updateServiceAgreement(Map<String, Object> param, HttpServletRequest httpServletRequest) {
        ResponseObject result;
        try {
            SaUpdateDraftDTO request = objectMapper.convertValue(param, SaUpdateDraftDTO.class);
            Integer saId = request.getSaId();
            Optional<T_AM_SA> tAmSaOpt = tAmSARepo.findById(saId);
            if (tAmSaOpt.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        ResponseUtils.MESSAGE_NOT_FOUND, ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            if (ApprovalStatus.WAITING_FOR_APPROVAL.name().equalsIgnoreCase(tAmSaOpt.get().getStatus())) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Data is Waiting For Approval can't change", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            boolean isSubmiter = approvalServices.checkIsSubmitter(httpServletRequest, request.getAppHierId());
            if (!isSubmiter) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "You are not Submitter",
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            T_AM_SA tAmSa = tAmSaOpt.get();
            String json = "";

            json = new Gson().toJson(param);
            if (FlowStatus.DRAFT.name().equalsIgnoreCase(tAmSa.getStatus())) {
                tAmSa = saUpdateDraft(request, tAmSa.getId(), httpServletRequest);
            } else {
                tAmSa.setJsonData(json);
            }

            if (!request.getIsSubmit()) {
                tAmSa.setApprovalStatus(ApprovalStatus.DRAFT.name());
                tAmSARepo.save(tAmSa);
            } else {

//                if(request.getSaInfo().getIsMain()) {
//                    // active main
//                    List<String> statusIn = new ArrayList<>();
//                    statusIn.add(FlowStatus.APPROVED.name());
//                    statusIn.add("WAITING APPROVAL");
//                    Optional<VW_SA> saActive = vwSaRepo.findTopByAccountIdAndIsMainAndApprovalStatusInAndIdNot(request.getSaInfo().getAccountId(), "Y", statusIn, request.getSaId());
//                    if(saActive.isPresent()) {
//                        String message = UtilsAccount.messageValidateExist(ConstantAccount.SA_MAIN, (saActive.get().getApprovalStatus().equalsIgnoreCase(ConstantAccount.APPROVED)? ConstantAccount.APPROVED : ConstantAccount.WAITING_APPROVAL));
//                        return this.serviceValidate(false, HttpStatus.BAD_REQUEST, message, null);
//                    }
//                }

                tAmSa.setApprovalStatus(WAITING_APPROVAL);
                tAmSARepo.save(tAmSa);

                Integer tappId = approvalServices.addApprovalFlowR(request.getAppHierId(),
                        ApprovalCategory.SERVICE_AGREEMENT.name(), saId.toString(), "Update Service Agreement");

                approvalServices.setApprovalHistory(tappId, saId, "Update Service Agreement", ApprovalCategory.SERVICE_AGREEMENT.name(),
                        SUBMIT, httpServletRequest);

            }

            LinkedHashMap<String, Object> successTosResponse = new LinkedHashMap<>();
            successTosResponse.put("saId", request.getSaId());
            successTosResponse.put("description", request.getSaInfo().getDescription());
            successTosResponse.put("updateDate", new Date());
            successTosResponse.put("updatedBy", UserDetailUtils.getUsername());

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED, "Success update Service Agreement",
                    successTosResponse);

            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(CommonVariables.ERROR_IN, e.getMessage(), e);
            throw e;
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> inactiveServiceAgreement(InactiveSaDTO request, HttpServletRequest httpServletRequest) {
        ResponseObject result;

        try {
            Optional<T_AM_SA> tAmSaOpt = tAmSARepo.findById(request.getSaId());
            if (tAmSaOpt.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, ResponseUtils.MESSAGE_NOT_FOUND,
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            } else {
                if (FlowStatus.INACTIVE.name().equalsIgnoreCase(tAmSaOpt.get().getStatus())) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Service Agreement can't be inactivated", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                if (WAITING_APPROVAL.equalsIgnoreCase(tAmSaOpt.get().getApprovalStatus())) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Data is Waiting For Approval can't inactive",
                            ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                //Check addon sa jika ada yang aktif
                if(Objects.equals(tAmSaOpt.get().getIsMain(), "Y")) {
                    Map<String, Object> data = new HashMap<>();
                    List<String> statusIn = new ArrayList<>();
                    statusIn.add(FlowStatus.APPROVED.name());
                    statusIn.add("WAITING APPROVAL");

                    List<VW_SA> addonActive = vwSaRepo.findAllByAccountIdAndSaReferenceNumberAndSaTypeAndApprovalStatusInOrderByIdAsc(tAmSaOpt.get().getAccountId(),  tAmSaOpt.get().getSaReferenceNumber(),"Addon", statusIn);
                    if(!addonActive.isEmpty()) {
                        for(VW_SA addon : addonActive) {
                            result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                                    UtilsAccount.messageValidateExist(ConstantAccount.SA_AMANDEMEN_INACTIVE, (addon.getApprovalStatus().equalsIgnoreCase(ConstantAccount.APPROVED)? ConstantAccount.APPROVED : ConstantAccount.WAITING_APPROVAL)),
                                    ResponseUtils.DATA_EMPTY);
                            return new ResponseEntity<>(result, result.getHttpCode());
                        }
                    }
                    List<VW_SA> amandemenActive = vwSaRepo.findAllByAccountIdAndSaReferenceNumberAndSaTypeAndApprovalStatusInOrderByIdAsc(tAmSaOpt.get().getAccountId(),  tAmSaOpt.get().getSaReferenceNumber(), "Amendment", statusIn);
                    if(!amandemenActive.isEmpty()) {
                        for(VW_SA amendment : amandemenActive) {
                            result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                                    UtilsAccount.messageValidateExist(ConstantAccount.SA_ADDON_INACTIVE, (amendment.getApprovalStatus().equalsIgnoreCase(ConstantAccount.APPROVED)? ConstantAccount.APPROVED : ConstantAccount.WAITING_APPROVAL)),
                                    ResponseUtils.DATA_EMPTY);
                            return new ResponseEntity<>(result, result.getHttpCode());
                        }
                    }
                }
            }

            boolean isSubmiter = approvalServices.checkIsSubmitter(httpServletRequest, request.getAppHierId());
            if (!isSubmiter) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, CommonVariables.NOT_SUBMITTER,
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            Date newDate = new Date();
            T_AM_SA tAmSa = tAmSaOpt.get();
            if (WAITING_APPROVAL.equalsIgnoreCase(tAmSa.getApprovalStatus())
                    || ApprovalStatus.REJECT.name().equalsIgnoreCase(tAmSa.getStatus())) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
                        "can't inactive service agreement because status " + tAmSa.getStatus(),
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            tAmSa.setApprovalStatus(WAITING_APPROVAL);
            tAmSa.setUpdatedDate(newDate);
            tAmSa.setUpdatedBy(UserDetailUtils.getUsername());
            tAmSARepo.save(tAmSa);

            Integer tappId = approvalServices.addApprovalFlowR(request.getAppHierId(),
                    ApprovalCategory.INACTIVE_SERVICE_AGREEMENT.name(),
                    tAmSaOpt.get().getId().toString(), request.getRemark());

            approvalServices.setApprovalHistory(tappId, tAmSa.getId(), request.getRemark(),
                    ApprovalCategory.SERVICE_AGREEMENT.name(), "SUBMIT", httpServletRequest);

            LinkedHashMap<String, Object> successTosResponse = new LinkedHashMap<>();
            successTosResponse.put("saId", tAmSa.getId());
            successTosResponse.put("appHierId", request.getAppHierId());
            successTosResponse.put("description", request.getRemark());
            successTosResponse.put("createdDate", new Date());
            successTosResponse.put("createdBy", UserDetailUtils.getUsername());

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success request inactive service agreement", successTosResponse);

            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(CommonVariables.ERROR_IN, e.getMessage(), e);
            throw e;
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    @Transactional(rollbackFor = IllegalArgumentException.class, readOnly = false)
    public ResponseEntity<?> approveSa(@RequestBody ApprovalSaDTO dto, HttpServletRequest httpServletRequest)
            throws IllegalArgumentException, JSONException, JsonProcessingException {
        try {
            Optional<T_AM_SA> dataSa = tAmSARepo.findById(dto.getSaId());
            if (dataSa.isPresent()) {
                T_AM_SA mServiceAgreement = dataSa.get();
                boolean isForward = approvalServices.isForwardPosition(dto.getApprovalId());
                if (isForward) {
                    approvalServices.setApprHistoryForward(dto.getApprovalId(), mServiceAgreement.getId(), dto.getDescription(),
                        ApprovalCategory.SERVICE_AGREEMENT.name(), dto.getAction(), httpServletRequest);
                } else {
                    approvalServices.setApprovalHistory(dto.getApprovalId(), mServiceAgreement.getId(), dto.getDescription(),
                        ApprovalCategory.SERVICE_AGREEMENT.name(), dto.getAction(), httpServletRequest);
                }
                
                
                if (ApprovalStatus.REJECT.name().equalsIgnoreCase(dto.getAction())) {
                    mServiceAgreement.setApprovalStatus(FlowStatus.REJECTED.name());
                    mServiceAgreement.setUpdatedDate(new Date());
                    mServiceAgreement.setUpdatedBy(UserDetailUtils.getUsername());
                    approvalServices.actionNextFlowApproval(dto.getApprovalId(), dto.getDescription(),
                            ApprovalStatus.REJECT);
                } else {
                    boolean isFinal = approvalServices.actionNextFlowApproval(dto.getApprovalId(), dto.getDescription(),
                            ApprovalStatus.APPROVE);
                    if (isFinal) {
                        if (mServiceAgreement.getJsonData() != null) {
                            String jsonStr = mServiceAgreement.getJsonData();
                            Map<String, Object> param = objectMapper.readValue(jsonStr, Map.class);
                            SaInfoDTO saInfo = objectMapper.convertValue(param.get("saInfo"), SaInfoDTO.class);
                            mServiceAgreement.setDescription(saInfo.getDescription());
                            mServiceAgreement.setEndDate(saInfo.getEndDate());
                            mServiceAgreement.setJsonData(null);
                        }
                        if (mServiceAgreement.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())) {
                            // UPDATE ATTACHMENT STATUS

                            // SOFT DELETE OLD ATTACHMENT

                            Optional<List<M_ATTACHMENT>> attachOldOpt = mAttachmentRepo
                                    .findByReferenceIdAndIsDraftAndIsDeletedAndCategoryIgnoreCase(
                                            mServiceAgreement.getId(), false, false, APP_CATEGORY);

                            if (attachOldOpt.isPresent() && !attachOldOpt.get().isEmpty()) {
                                for (M_ATTACHMENT attach : attachOldOpt.get()) {
                                    attach.setIsDeleted(true);
                                    attach.setUpdatedBy(UserDetailUtils.getUsername());
                                    attach.setUpdatedDate(new Date());
                                    mAttachmentRepo.save(attach);
                                }
                            }

                            // UPDATE DRAFT ATTACHMENT

                            Optional<List<M_ATTACHMENT>> attachDraftOpt = mAttachmentRepo
                                    .findByReferenceIdAndIsDraftAndIsDeletedAndCategoryIgnoreCase(
                                            mServiceAgreement.getId(), true, false, APP_CATEGORY);

                            if (attachDraftOpt.isPresent() && !attachDraftOpt.get().isEmpty()) {
                                for (M_ATTACHMENT attach2 : attachDraftOpt.get()) {
                                    attach2.setIsDraft(false);
                                    attach2.setUpdatedBy(UserDetailUtils.getUsername());
                                    attach2.setUpdatedDate(new Date());

                                    mAttachmentRepo.save(attach2);
                                }
                            }
                        }
                        mServiceAgreement.setStatus(FlowStatus.ACTIVE.name());
                        mServiceAgreement.setApprovalStatus(ApprovalStatus.APPROVED.name());
                        mServiceAgreement.setUpdatedBy(UserDetailUtils.getUsername());
                        mServiceAgreement.setUpdatedDate(new Date());
                        // Update Status M_ACCOUNT
                        Optional<M_ACCOUNT> account = mAccountRepo.findByAccountId(dataSa.get().getAccountId());
                        if(account.isPresent() && Objects.equals(account.get().getStatus(), "REGISTERED")) {
                            M_ACCOUNT updateAccount = account.get();
                            updateAccount.setUpdatedBy(UserDetailUtils.getUsername());
                            updateAccount.setUpdatedDate(new Date());
                            updateAccount.setStatus("ACTIVE");
                            mAccountRepo.save(updateAccount);
                        }
                    }
                }
                tAmSARepo.save(mServiceAgreement);

                ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        ResponseUtils.MESSAGE_SUCCESS, null);

                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        ResponseUtils.MESSAGE_NOT_FOUND, ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = IllegalArgumentException.class, readOnly = false)
    public boolean updateApprovedData(Integer saId, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        try {

            Optional<T_AM_SA> tAmSa = tAmSARepo.findById(saId);
            if (!tAmSa.isPresent()) {
                return false;
            }
            T_AM_SA sa = tAmSa.get();
            String jsonStr = sa.getJsonData();
            objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            T_AM_SA parameter = objectMapper.readValue(jsonStr, T_AM_SA.class);
            sa.setDescription(parameter.getDescription());
            sa.setUpdatedDate(new Date());
            sa.setUpdatedBy(UserDetailUtils.getUsername());
            tAmSARepo.save(sa);
            return true;

        } catch (Exception e) {
            logger.error(CommonVariables.ERROR_IN, e.getMessage(), e);
            throw e;
        }
    }

    @Transactional(rollbackFor = IllegalArgumentException.class, readOnly = false)
    public ResponseEntity<ResponseObject> approveInactive(ApprovalSaDTO dto,
                                                                 HttpServletRequest httpServletRequest) {
        try {
            Optional<T_AM_SA> optSa = tAmSARepo.findById(dto.getSaId());
            if (optSa.isPresent()) {
                T_AM_SA tSa = optSa.get();
                boolean isForward = approvalServices.isForwardPosition(dto.getApprovalId());
                if (isForward) {
                    approvalServices.setApprHistoryForward(dto.getApprovalId(), tSa.getId(), dto.getDescription(),
                        APP_CATEGORY, dto.getAction(), httpServletRequest);
                } else {
                   // APPROVAL HISTORY
                    approvalServices.setApprovalHistory(dto.getApprovalId(), tSa.getId(), dto.getDescription(),
                        APP_CATEGORY, dto.getAction(), httpServletRequest);
                }
                
                if (ApprovalStatus.REJECT.name().equalsIgnoreCase(dto.getAction())) {
                    approvalServices.actionNextFlowApproval(dto.getApprovalId(), dto.getDescription(),
                            ApprovalStatus.REJECT);
                    tSa.setUpdatedDate(new Date());
                    tSa.setUpdatedBy(UserDetailUtils.getUsername());
                    tSa.setApprovalStatus(ApprovalStatus.REJECTED.name());
                } else {
                    boolean isFinal = approvalServices.actionNextFlowApproval(dto.getApprovalId(), dto.getDescription(),
                            ApprovalStatus.APPROVE);
                    if (isFinal) {
                        tSa.setUpdatedDate(new Date());
                        tSa.setUpdatedBy(UserDetailUtils.getUsername());
                        tSa.setApprovalStatus(ApprovalStatus.APPROVED.name());
                        tSa.setStatus(FlowStatus.INACTIVE.name());
                    }
                }
                tAmSARepo.save(tSa);

                ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        ResponseUtils.MESSAGE_SUCCESS, null);

                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Id not existing", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<ResponseObject> getApprovalHistory(Integer refId) {

        List<String> appCategory = new ArrayList<>();
        appCategory.add(ApprovalCategory.SERVICE_AGREEMENT.name());
        appCategory.add(ApprovalCategory.INACTIVE_SERVICE_AGREEMENT.name());

        var resp = approvalServices.getApprovalHistory(refId, ApprovalCategory.SERVICE_AGREEMENT.name(), appCategory);
        if (resp != null) {
            ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    ResponseUtils.MESSAGE_SUCCESS, resp);

            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, "Data not found",
                ResponseUtils.DATA_EMPTY);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> getAllApprovalHeaderList(HttpServletRequest httpServletRequest) {
        log.info("getAllApprovalHeaderList");
        try {
            var mApprovalHierarchies = mApprovalHierarchyRepo.findApprovalHierarchyByPositionAndType(UserDetailUtils.getPositionFromToken(httpServletRequest), "Y", "APR_ACC_SA");
            List<ApprovalHierarchyDto> approvals = new ArrayList<>();
            if (!mApprovalHierarchies.isEmpty()) {
                for (M_APPROVAL_HIERARCHY mApprovalHierarchy : mApprovalHierarchies) {
                    approvals.add(ApprovalHierarchyDto.builder()
                            .appHierCode("")
                            .appHierId(mApprovalHierarchy.getAppHierId())
                            .approvalName(mApprovalHierarchy.getApprovalName())
                            .approvalType(mApprovalHierarchy.getApprovalType())
                            .desc(mApprovalHierarchy.getDesc())
                            .entityId(mApprovalHierarchy.getEntityId())
                            .build());
                }
            }

            log.info("data approvals : {}", approvals);

            return new ResponseEntity<>(
                    ResponseObject.builder()
                            .success(ResponseUtils.SUCCESS_TRUE)
                            .code(HttpStatus.OK.value())
                            .message("Success Get Data Header Approval Hierarchies")
                            .data(approvals)
                            .build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> getApprovalHeaderById(Integer appHierId) {
        log.info("getApprovalHeaderById : {}", appHierId);

        ResponseObject result = new ResponseObject();
        try {
            Optional<List<VW_APPROVAL_HIERARCHY_DETAIL>> appDtl = vwApprovalHierarycyDtlRepo.findByApphierId(appHierId);

            List<ApprovalHierarchyDetailDto> dtos = new ArrayList<>();

            if (appDtl.isPresent()) {

                List<String> pos = vwApprovalHierarycyDtlRepo.findPositionById(appHierId);

                for (String pos_ : pos) {
                    List<HashMap<String, Object>> detailList = new ArrayList<>();
                    ApprovalHierarchyDetailDto hierarchyDetailDto = new ApprovalHierarchyDetailDto();

                    List<VW_APPROVAL_HIERARCHY_DETAIL> submitter = vwApprovalHierarycyDtlRepo
                            .findByApphierIdAndPositionAndIsSubmitterAndIsFinal(appHierId, pos_, "Y", "N").get();
                    if (!submitter.isEmpty()) {
                        hierarchyDetailDto.setApphierId(appHierId);
                        hierarchyDetailDto.setApprovalLevel("Submitter");
                        hierarchyDetailDto.setPosition(pos_);
                        for (VW_APPROVAL_HIERARCHY_DETAIL appDtl_ : submitter) {

                            HashMap<String, Object> ar = new HashMap<>();
                            ar.put("appHierId", appDtl_.getApphierId());
                            ar.put("employeeId", appDtl_.getEmployeeId());
                            ar.put("employeeName", appDtl_.getFullName());

                            detailList.add(ar);
                        }
                        hierarchyDetailDto.setEmployeeDetail(detailList);
                        dtos.add(hierarchyDetailDto);
                    }

                }
            }

            if (appDtl.isPresent()) {

                List<String> pos = vwApprovalHierarycyDtlRepo.findPositionById(appHierId);

                for (String pos_ : pos) {
                    List<HashMap<String, Object>> detailList = new ArrayList<>();
                    ApprovalHierarchyDetailDto hierarchyDetailDto = new ApprovalHierarchyDetailDto();

                    // Approver
                    for (int i = 1; i < 6; i++) {
                        List<VW_APPROVAL_HIERARCHY_DETAIL> approver;
                        approver = vwApprovalHierarycyDtlRepo
                                .findByApphierIdAndApprovalLevelAndPositionAndIsSubmitterAndIsFinal(appHierId, i, pos_,
                                        "N", "N")
                                .get();
                        if (!approver.isEmpty()) {
                            hierarchyDetailDto.setApphierId(appHierId);
                            hierarchyDetailDto.setApprovalLevel("Approver " + i);
                            hierarchyDetailDto.setPosition(pos_);
                            for (VW_APPROVAL_HIERARCHY_DETAIL appDtl_ : approver) {

                                HashMap<String, Object> ar = new HashMap<>();
                                ar.put("appHierId", appDtl_.getApphierId());
                                ar.put("employeeId", appDtl_.getEmployeeId());
                                ar.put("employeeName", appDtl_.getFullName());

                                detailList.add(ar);
                            }
                            hierarchyDetailDto.setEmployeeDetail(detailList);
                            dtos.add(hierarchyDetailDto);
                        }
                    }
                }
            }

            if (appDtl.isPresent()) {

                List<String> pos = vwApprovalHierarycyDtlRepo.findPositionById(appHierId);

                for (String pos_ : pos) {
                    List<HashMap<String, Object>> detailList = new ArrayList<>();
                    ApprovalHierarchyDetailDto hierarchyDetailDto = new ApprovalHierarchyDetailDto();

                    // Final Approver
                    List<VW_APPROVAL_HIERARCHY_DETAIL> finalApp = vwApprovalHierarycyDtlRepo
                            .findByApphierIdAndPositionAndIsSubmitterAndIsFinal(appHierId, pos_, "N", "Y").get();

                    if (!finalApp.isEmpty()) {
                        hierarchyDetailDto.setApphierId(appHierId);
                        hierarchyDetailDto.setApprovalLevel("Final Approver");
                        hierarchyDetailDto.setPosition(pos_);
                        for (VW_APPROVAL_HIERARCHY_DETAIL appDtl_ : finalApp) {

                            HashMap<String, Object> ar = new HashMap<>();
                            ar.put("appHierId", appDtl_.getApphierId());
                            ar.put("employeeId", appDtl_.getEmployeeId());
                            ar.put("employeeName", appDtl_.getFullName());

                            detailList.add(ar);
                        }
                        log.info("Size: " + detailList.size());
                        hierarchyDetailDto.setEmployeeDetail(detailList);
                        dtos.add(hierarchyDetailDto);
                    }
                }
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, "Success get data", dtos);
            log.info("Response Success ->" + result);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> uploadFile(Integer fileCategoryId, boolean isUpdate, List<MultipartFile> files, Integer saId) {
        ResponseObject result = new ResponseObject();
        try {
            Optional<T_AM_SA> saData = tAmSARepo.findById(saId);
            if (saData.isEmpty()) {

                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Service Agreement " + ResponseUtils.MESSAGE_NOT_FOUND, ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            if(isUpdate) {
                List<M_ATTACHMENT> listAttachmentOld = mAttachmentRepo.findAllByReferenceId(saId);
                if(!listAttachmentOld.isEmpty()) {
                    for (M_ATTACHMENT mAttachment : listAttachmentOld) {
                        mAttachment.setIsDeleted(true);
                        mAttachment.setIsDraft(Objects.equals(saData.get().getStatus(), "DRAFT"));
                        mAttachmentRepo.save(mAttachment);
                    }
                }
            }

            if(!files.isEmpty()) {
                for (MultipartFile file : files) {
                    M_ATTACHMENT mAttachment = new M_ATTACHMENT();
                    String generatedFileName = UserDetailUtils.generateFileName(file.getOriginalFilename());
                    mAttachment.setCategory(APP_CATEGORY);
                    mAttachment.setFileCategoryId(fileCategoryId);
                    mAttachment.setReferenceId(saId);
                    mAttachment.setType(file.getContentType());
                    mAttachment.setCreatedBy(UserDetailUtils.getUsername());
                    mAttachment.setCreatedDate(new Date());
                    mAttachment.setPathFile(PATH_FILE);
                    mAttachment.setFileName(generatedFileName);
                    mAttachment.setFileSize(file.getSize());
                    mAttachment.setIsDraft(saData.get().getStatus().equalsIgnoreCase(ACTIVE));
                    mAttachment.setIsDeleted(Boolean.FALSE);
                    mAttachmentRepo.save(mAttachment);

                    R_GLOBAL_TYPE_VALUE rGlobalTypeValue = rGlobalTypeValueRepo
                            .findTopByGlbValueIgnoreCaseAndIsDeleted(PATH_FILE, false);
                    String fullPath = rGlobalTypeValue.getName() + generatedFileName;

                    String fullobject = "FILE" + fullPath;
                    this.minioClient.putObject(this.configurationProperties.getBucket(), fullobject,
                            file.getInputStream(), file.getContentType());
                }
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, "Success upload file",
                    ResponseUtils.DATA_EMPTY);

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    @Transactional(rollbackFor = IllegalArgumentException.class, readOnly = false)
    public ResponseEntity<ResponseObject> deleteSaDraft(Integer saId) {
        ResponseObject result = new ResponseObject();
        try {
            // Data SA
            Optional<T_AM_SA> data = tAmSARepo.findById(saId);
            if(data.isPresent()) {
                if(Objects.equals(data.get().getStatus(), "DRAFT")) {
                    // T_AM_SA_DETAIL_HEADER
                    Optional<T_AM_SA_DETAIL_HEADER> tAmSADetailHeader = tAmSADetailHeaderRepo.findBySaId(saId);
                    if(tAmSADetailHeader.isPresent()) {
                        // T_AM_SA_DETAIL_DTL
                        List<T_AM_SA_DETAIL_DTL> tAmSaDetailDtls = tAmSADetailDTLRepo.findAllBySaDetailHeaderId(tAmSADetailHeader.get().getId());
                        for(T_AM_SA_DETAIL_DTL dtl : tAmSaDetailDtls) {
                            tAmSADetailDTLRepo.deleteById(dtl.getId());
                        }
                        tAmSADetailHeaderRepo.deleteById(tAmSADetailHeader.get().getId());
                    }
                    // T_AM_SA_CALCRULE_HEADER
                    Optional<T_AM_SA_CALCRULE_HEADER> tAmSaCalcruleHeader = tAmSACalcRuleHeaderRepo.findBySaId(saId);
                    if(tAmSaCalcruleHeader.isPresent()) {
                        // T_AM_SA_CALCRULE_DTL
                        List<T_AM_SA_CALCRULE_DTL> tAmSaDetailDtls = tAmSaCalcRuleDTLRepo.findAllBySaCalcruleHeaderId(tAmSaCalcruleHeader.get().getId());
                        if(!tAmSaDetailDtls.isEmpty()) {
                            for(T_AM_SA_CALCRULE_DTL CalcDtl : tAmSaDetailDtls) {
                                tAmSaCalcRuleDTLRepo.deleteById(CalcDtl.getId());
                            }
                        }
                        tAmSACalcRuleHeaderRepo.deleteById(tAmSaCalcruleHeader.get().getId());
                    }
                    // T_AM_SA_PRC_RULE_DTL
                    List<T_AM_SA_PRC_RULE_DTL> tAmSaPrcRuleDtls = tAmSAPrcRuleDTLRepo.findAllBySaId(saId);
                    if(!tAmSaPrcRuleDtls.isEmpty()) {
                        for (T_AM_SA_PRC_RULE_DTL PrcDtl : tAmSaPrcRuleDtls) {
                            tAmSAPrcRuleDTLRepo.deleteById(PrcDtl.getId());
                        }
                    }
                    // T_AM_SA_LATECHARGE_HEADER
                    List<T_AM_SA_LATECHARGE_HEADER> tAmSaLatechargeHeaders = tAmSALateChargeHeaderRepo.findAllBySaId(saId);
                    if(!tAmSaLatechargeHeaders.isEmpty()) {
                        for (T_AM_SA_LATECHARGE_HEADER LateChargeDtl : tAmSaLatechargeHeaders) {
                            tAmSALateChargeHeaderRepo.deleteById(LateChargeDtl.getId());
                        }
                    }
                    // T_AM_SA_TAXIMP_HEADER
                    List<T_AM_SA_TAXIMP_HEADER> tAmSaTaximpHeaders = tAmSATaxImpHeaderRepo.findAllBySaId(saId);
                    if(!tAmSaTaximpHeaders.isEmpty()) {
                        for (T_AM_SA_TAXIMP_HEADER TaxImpliDtl : tAmSaTaximpHeaders) {
                            tAmSATaxImpHeaderRepo.deleteById(TaxImpliDtl.getId());
                        }
                    }
                    // T_AM_SA_TOS_HEADER
                    List<T_AM_SA_TOS_HEADER> tAmSaTosHeaders = tAmSATosHeaderRepo.findAllBySaId(saId);
                    if(!tAmSaTosHeaders.isEmpty()) {
                        for (T_AM_SA_TOS_HEADER TosDtl : tAmSaTosHeaders) {
                            // T_AM_SA_TOS_DTL
                            List<T_AM_SA_TOS_DTL> tAmSaTosDtls = tAmSATosDTLRepo.findAllBySaTosId(TosDtl.getMTosId());
                            for (T_AM_SA_TOS_DTL dtl2 : tAmSaTosDtls) {
                                tAmSATosDTLRepo.deleteById(dtl2.getId());
                            }
                            tAmSATosHeaderRepo.deleteById(TosDtl.getId());
                        }
                    }
                    // ATTACHMENT
                    List<M_ATTACHMENT> dataAttachments = mAttachmentRepo.findAllByReferenceId(saId);
                    log.info("data "+dataAttachments);
                    if(!dataAttachments.isEmpty()) {
                        for (M_ATTACHMENT mAttachment : dataAttachments) {
                            mAttachment.setIsDraft(Objects.equals(data.get().getStatus(), "DRAFT"));
                            mAttachment.setIsDeleted(true);
                            mAttachmentRepo.save(mAttachment);
                        }
                    }

                    tAmSARepo.deleteById(saId);
                } else {
                    log.info("NOT DRAFT");
                }
            }
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, "Success Delete Draft Service Agreement",
                    ResponseUtils.DATA_EMPTY);

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static boolean isOverlapping(Date start1, Date end1, Date start2, Date end2) {
        try {
            // Memeriksa apakah rentang 1 beririsan dengan rentang 2 atau sebaliknya
            return start1.before(end2)
                    && end1.after(start2)
                    || (UtilsDate.dateToString(start1, Constant.FORMAT_DATE).equalsIgnoreCase(UtilsDate.dateToString(end2, Constant.FORMAT_DATE)))
                    || (UtilsDate.dateToString(end1, Constant.FORMAT_DATE).equalsIgnoreCase(UtilsDate.dateToString(start2, Constant.FORMAT_DATE)));

        } catch (Exception e) {
           log.error(Constant.LOG_ERROR, e.getMessage(), e);
            return false; // Jika terjadi kesalahan parsing tanggal
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> checkServicePointAndGasSource(ValidationCreateSaDTO request) {

        Optional<VW_SA> getSa = StringUtils.hasValue(request.getSaId()) ?
                vwSaRepo.findById(request.getSaId()) :
                Optional.empty();

        Map<String, Object> data = new HashMap<>();
        List<String> statusIn = new ArrayList<>();
        statusIn.add(FlowStatus.APPROVED.name());
        statusIn.add("WAITING APPROVAL");

        if(!request.getIsMain()) {
            if (request.getSaType().equalsIgnoreCase("Amendment")) {
                List<VW_SA> amandemenActive =
                        StringUtils.hasValue(request.getSaId())
                        && getSa.isPresent()
                        && getSa.get().getApprovalStatus().equalsIgnoreCase(FlowStatus.APPROVED.name()) ?
                            vwSaRepo.findAllByAccountIdAndSaReferenceNumberAndSaTypeAndApprovalStatusInAndIdNotOrderByIdAsc(request.getAccountId(), request.getSaReferenceNumber(), "Amendment", statusIn, request.getSaId()) :
                            vwSaRepo.findAllByAccountIdAndSaReferenceNumberAndSaTypeAndApprovalStatusInOrderByIdAsc(request.getAccountId(), request.getSaReferenceNumber(), "Amendment", statusIn);
                if(!amandemenActive.isEmpty()) {
                    for (VW_SA amandemen : amandemenActive) {
                        if (isOverlapping(amandemen.getStartDate(), amandemen.getEndDate(), request.getStartDate(), request.getEndDate())) {
                            data.put("isCreated", false);
                            String message = UtilsAccount.messageValidateExist(ConstantAccount.SA_AMANDEMEN, (amandemen.getApprovalStatus().equalsIgnoreCase(ConstantAccount.APPROVED)? ConstantAccount.APPROVED : ConstantAccount.WAITING_APPROVAL));
                            return this.serviceValidate(true, HttpStatus.BAD_REQUEST, message, data);
                        }
                    }
                }
            }
            
            if (request.getSaType().equalsIgnoreCase("addon") && request.getProductId() != null) {
                List<VW_SA> addonActive =
                        StringUtils.hasValue(request.getSaId())
                        && getSa.isPresent()
                        && getSa.get().getApprovalStatus().equalsIgnoreCase(FlowStatus.APPROVED.name()) ?
                                vwSaRepo.findAllByAccountIdAndSaReferenceNumberAndSaTypeAndApprovalStatusInAndIdNotOrderByIdAsc(request.getAccountId(), request.getSaReferenceNumber(), "Addon", statusIn, request.getSaId()) :
                                vwSaRepo.findAllByAccountIdAndSaReferenceNumberAndSaTypeAndApprovalStatusInOrderByIdAsc(request.getAccountId(), request.getSaReferenceNumber(), "Addon", statusIn);
                if(!addonActive.isEmpty()) {
                    for (VW_SA addon : addonActive) {
                        if (isOverlapping(addon.getStartDate(), addon.getEndDate(), request.getStartDate(), request.getEndDate()) && 
                                Objects.equals(addon.getProductId(), request.getProductId())) {
                                data.put("isCreated", false);
                                String message = UtilsAccount.messageValidateExist(ConstantAccount.SA_ADDON, (addon.getApprovalStatus().equalsIgnoreCase(ConstantAccount.APPROVED)? ConstantAccount.APPROVED : ConstantAccount.WAITING_APPROVAL));
                                return this.serviceValidate(true, HttpStatus.BAD_REQUEST, message, data);
                        }
                    }
                }
            }
        } else {
            R_GLOBAL_TYPE_VALUE rServiceType = gtAccount.getIdGlobalTypeByGroupNameAndValue("Service Type", "GAS");
            if(!StringUtils.hasValue(rServiceType.getGlbTypeValId())) {
                data.put("isCreated", false);
                String message = rServiceType.getGlbValue();
                return this.serviceValidate(true, HttpStatus.BAD_REQUEST, message, data);
            }
            // Get List T_AM_SA with account id dan service type gas
            Optional<T_AM_SA> getListSaGas =
                    StringUtils.hasValue(request.getSaId())
                            && getSa.isPresent()
                            && getSa.get().getApprovalStatus().equalsIgnoreCase(FlowStatus.APPROVED.name()) ?
                            tAmSARepo.findTopByAccountIdAndSaServiceTypeAndIsMainAndApprovalStatusInAndIdNotOrderByIdAsc(request.getAccountId(), rServiceType.getGlbTypeValId(), "Y", statusIn, request.getSaId()) :
                            tAmSARepo.findTopByAccountIdAndSaServiceTypeAndIsMainAndApprovalStatusInOrderByIdAsc(request.getAccountId(), rServiceType.getGlbTypeValId(), "Y", statusIn);
            if(getListSaGas.isPresent()) {
                data.put("isCreated", false);
                String message = UtilsAccount.messageValidateExist(ConstantAccount.SA_MAIN, (getListSaGas.get().getApprovalStatus().equalsIgnoreCase(ConstantAccount.APPROVED)? ConstantAccount.APPROVED : ConstantAccount.WAITING_APPROVAL));
                return this.serviceValidate(true, HttpStatus.BAD_REQUEST, message, data);
            }
        }
        data.put("isCreated", true);
        String message = "You can create Service Agreement data!";
        return this.serviceValidate(true, HttpStatus.OK, message, data);
    }

    // Validate
    public ResponseEntity<ResponseObject> serviceValidate(Boolean bool, HttpStatus status, String message, Object data) {
        ResponseObject result = new ResponseObject();
        result.setSuccess(bool);
        result.setCode(status);
        result.setMessage(message);
        result.setData(data);
        return new ResponseEntity<>(result, status);
    }
}
