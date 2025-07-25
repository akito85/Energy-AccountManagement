package com.dbs.module.account.detail.serviceagreement.service;

import com.dbs.common.library.services.CriteriaServices;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.*;
import com.dbs.database.crm.entities.ratingbillinginvoice.*;
import com.dbs.database.crm.repositories.rbi.*;
import com.dbs.module.account.detail.serviceagreement.dto.helper.ViewTosProductDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.ChooseProductDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.PriceCodeVersionDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.PriceCodeDetailAdjustmentDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.PriceCodeDetailDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.ListProductVersionDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.PricingRuleDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.DdlMapperDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.PriceCodeDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.CalcRuleProductDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.PricingRuleDetailDTO;
import com.dbs.module.account.detail.serviceagreement.dto.CriteriaDataDTO;
import com.dbs.module.account.detail.serviceagreement.dto.LatechargeCurrencyDTO;
import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.entities.CriteriaData;
import com.dbs.database.crm.entities.accountmanagement.*;
import com.dbs.database.crm.entities.mastermanagement.R_PRICING_DETAIL;
import com.dbs.database.crm.entities.product.*;
import com.dbs.database.crm.entities.usermanagement.M_GLOBAL_TYPE;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.accountmanagement.Account.*;
import com.dbs.database.crm.repositories.accountmanagement.*;
import com.dbs.database.crm.repositories.mastermanagement.RCriteriaAdjustmentPricingRepo;
import com.dbs.database.crm.repositories.mastermanagement.RPricingAdjustmentDetailRepo;
import com.dbs.database.crm.repositories.mastermanagement.RPricingDetailRepo;
import com.dbs.database.crm.repositories.product.*;
import com.dbs.database.crm.repositories.usermanagement.MAttachmentRepo;
import com.dbs.database.crm.repositories.usermanagement.MGlobalTypeRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import com.dbs.database.crm.repositories.usermanagement.TApprovalRepo;
import com.dbs.module.account.detail.serviceagreement.dto.createdto.ProductDetailDTO2;
import com.dbs.module.account.detail.serviceagreement.dto.createdto.ProductDetailPricingDTO;
import com.dbs.module.account.detail.serviceagreement.dto.createdto.VersionListDTO;
import com.dbs.module.account.detail.serviceagreement.dto.createdto.VersionListProductDTO;
import com.dbs.module.account.master.latecharge.service.LateChargeServiceImpl;
import com.dbs.module.account.detail.serviceagreement.dto.PricingRuleViewDto;
import com.dbs.module.account.detail.serviceagreement.dto.ProductCriteriaFilterDto;
import com.dbs.module.account.detail.serviceagreement.dto.ProductDetailViewDto;
import com.dbs.module.account.utils.UtilsAccount;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;


@Service
public class SaDdlService {

    private static final Logger logger = LoggerFactory.getLogger(SaDdlService.class);

    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;

    @Autowired
    private MProductRepo mProductRepo;

    @Autowired
    private MProductVersionRepo mProductVersionRepo;

    @Autowired
    private RPricingAdjustmentDetailRepo rPricingAdjustmentDetailRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MRbiBillingCycleRepo mRbiBillingCycleRepo;

    @Autowired
    private MRbiTermsOfPaymentRepo mRbiTermsOfPaymentRepo;

    @Autowired
    private MPricingAdjustmentHeaderRepo mPricingAdjustmentHeaderRepo;

    @Autowired
    private MRbiInvoiceTemplateRepo mRbiInvoiceTemplateRepo;

    @Autowired
    private MTaxImplicationRepo taxImpliRepo;

    @Autowired
    private MTaxImplicationCriteriaDataRepo taxCriteriaRepo;

    @Autowired
    private MTosRepo mTosRepo;

    @Autowired
    private MTaxImplicationRuleRepo taxRuleRepo;

    @Autowired
    private MAmLateChargeRepo latechargeRepo;

    @Autowired
    private MAmLateChargeCriteriaDataRepo latechargeCriteriaRepo;

    @Autowired
    private MProductPricingRepo mProductPricingRepo;

    @Autowired
    private RCriteriaAdjustmentPricingRepo rCriteriaAdjustmentPricingRepo;

    @Autowired
    private VWPricingRuleDetailRepo vwPricingRuleDetailRepo;

    @Autowired
    private MRbiTaxCodeRepo mRbiTaxCodeRepo;

    @Autowired
    private MAmLateChargeRuleRepo latechargeRuleRepo;

    @Autowired
    private MAmLateChargeRuleFormulaRepo latechargeRuleFormulaRepo;

    @Autowired
    private MProductClassRepo mProductClassRepo;

    @Autowired
    private TApprovalHistoryRepo tApprovalHistoryRepo;

    @Autowired
    private MPricingRepo mPricingRepo;

    @Autowired
    private MProductDetailRepo mProductDetailRepo;

    @Autowired
    private MPricingRuleRepo mPricingRuleRepo;

    @Autowired
    private MPricingRuleDetailRepo mPricingRuleDetailRepo;

    @Autowired
    private MProductTermOfServiceRepo mProductTermOfServiceRepo;

    @Autowired
    private MProductCalculationRuleRepo mProductCalculationRuleRepo;

    @Autowired
    private RPricingDetailRepo rPricingDetailRepo;

    @Autowired
    private MProductTargetAccountSellingCriteriaRepo mProductTargetAccountSellingCriteriaRepo;

    @Autowired
    private MProductTargetAccountSellingHeaderRepo mProductTargetAccountSellingHeaderRepo;

    @Autowired
    private RCriteriaDataProductTasRepo rCriteriaDataProductTasRepo;

    @Autowired
    private REligibilityProductRepo rEligibilityProductRepo;

    @Autowired
    private RProductBundlingRepo rProductBundlingRepo;

    @Autowired
    private TApprovalRepo tApprovalRepo;

    @Autowired
    private TExtTerHistoryRepo tExtTerHistoryRepo;

    @Autowired
    private MAttachmentRepo mAttachmentRepo;

    @Autowired
    private Environment env;

    @Autowired
    private RProductTermOfServiceRepo rProductTermOfServiceRepo;

    @Autowired
    private RPricingRuleCriteriaDataRepo rPricingRuleCriteriaDataRepo;

    @Autowired
    private VWProductRepo vwProductRepo;

    @Autowired
    private VWAccountCriteriaRepo vwAccountCriteriaRepo;

    @Autowired
    private RPricingCriteriaDataRepo rPricingCriteriaDataRepo;

    @Autowired
    private MGlobalTypeRepo mGlobalTypeRepo;

    @Autowired
    private LateChargeServiceImpl lateChargeService;

    @Autowired
    private VwAmTaxImplicationCriteriaDataRepo vwAmTaxImplicationCriteriaDataRepo;

    @Autowired
    private VwLateChargeCriteriaDataRepo vwLateChargeCriteriaDataRepo;

    @Autowired
    private MRbiFactureCodeRepo mRbiFactureCodeRepo;

    @Autowired
    private RRbiTopCriteriaDataRepo rRbiTopCriteriaDataRepo;

    @Autowired
    private RRbiInvoiceTemplateCriteriaDataRepo rRbiInvoiceTemplateCriteriaDataRepo;

    @Autowired
    private GlobalTypeValueService globalTypeValueService;
    @Autowired
    private CriteriaServices criteriaServices;

    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> getPriceAdjustmentByCriteria(Integer accountId, Integer priceCode, HttpServletRequest httpServlet) {
        logger.info("get List Product");
        ResponseObject result = new ResponseObject();
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

            List<R_PRICING_DETAIL> data = null;
            List<PriceCodeDetailDTO> rPricingDetails = new ArrayList<>();

            data = rPricingDetailRepo.findAllByIdPricing(priceCode);
            for (R_PRICING_DETAIL dataDetail : data) {
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
                rPricingDetails.add(newDetailDto);
            }

            List<Integer> idCritFiltered = new ArrayList<>();
            boolean valid = false;

            if (rPricingDetails.isEmpty()) {

                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, ResponseUtils.MESSAGE_NOT_FOUND,
                        ResponseUtils.DATA_EMPTY);

                return new ResponseEntity<>(result, HttpStatus.OK);
            }

            // criteria filter
            List<Map<String, Integer>> tasCritses = (List<Map<String, Integer>>) allDataCheck.get("tasCrit");
            if (!tasCritses.isEmpty()) {
                for (PriceCodeDetailDTO priceCodeDetail : rPricingDetails) {
                    List<M_PRICING_ADJUSTMENT_HEADER> mPricingAdjustmentHeaders = mPricingAdjustmentHeaderRepo.findAllBymPricingDetailId(priceCodeDetail.getId());

                    if(!mPricingAdjustmentHeaders.isEmpty()) {
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

                    PriceCodeDetailDTO newData = objectMapper.convertValue(priceCodeDetail,
                            PriceCodeDetailDTO.class);
                    for (Integer idCrit : idCritFiltered) {
                        Optional<R_PRICING_ADJUSTMENT_DETAIL> dataPricing = rPricingAdjustmentDetailRepo.findById(idCrit);
                        PriceCodeDetailAdjustmentDTO dataAdjustment = new PriceCodeDetailAdjustmentDTO();
                        if(dataPricing.isPresent()) {
                            Optional<R_GLOBAL_TYPE_VALUE> type = rGlobalTypeValueRepo.findByGlbTypeValId(dataPricing.get().getAdjustmentType());
                            if (type.isPresent()) {
                                dataAdjustment.setAdjustmentType(type.get().getName());
                            }
                            dataAdjustment.setAdjustmentValue(dataPricing.get().getAdjustmentValue());
                            dataAdjustment.setPriceAdjustmentDetailId(dataPricing.get().getId());

                            if(dataPricing.get().getAdjustmentType() == 221) {
                                dataAdjustment.setFinalAdjustmentValue(dataPricing.get().getAdjustmentValue());
                            } else if(dataPricing.get().getAdjustmentType() == 222) {
                                BigDecimal total = BigDecimal.ZERO;
                                total = total.add(newData.getValue());
                                total = total.add(dataPricing.get().getAdjustmentValue());
                                dataAdjustment.setFinalAdjustmentValue(total);
                            }else {
                                BigDecimal bd1 = dataPricing.get().getAdjustmentValue();
                                BigDecimal bd2 = newData.getValue();
                                BigDecimal total = bd2.subtract(bd1);
                                dataAdjustment.setFinalAdjustmentValue(total);
                            }

                            Optional<M_PRICING_ADJUSTMENT_HEADER> mPricingAdjustmentHeader = mPricingAdjustmentHeaderRepo.findById(dataPricing.get().getPricingAdjustmentId());
                            if(mPricingAdjustmentHeader.isPresent()) {
                                dataAdjustment.setAdjustmentName(mPricingAdjustmentHeader.get().getName());
                                dataAdjustment.setAdjustmentText(mPricingAdjustmentHeader.get().getName() + " - " + newData.getCurrency() + "/" + dataAdjustment.getFinalAdjustmentValue() + "/" + newData.getUomName());
                            }
                        }
                        newData.setAdjustment(dataAdjustment);
                    }

                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_SUCCESS, newData);
                }

            } else {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, ResponseUtils.MESSAGE_NOT_FOUND,
                        ResponseUtils.DATA_EMPTY);

                return new ResponseEntity<>(result, HttpStatus.OK);
            }

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
    public PriceCodeDetailAdjustmentDTO getPriceAdjustmentByCriteriaToDTO(Integer accountId, R_PRICING_DETAIL dataDetail, HttpServletRequest httpServlet) {
        logger.info("get List Product");
        PriceCodeDetailAdjustmentDTO result = new PriceCodeDetailAdjustmentDTO();
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

            List<Integer> idCritFiltered = new ArrayList<>();
            boolean valid = false;

            // criteria filter
            List<Map<String, Integer>> tasCritses = (List<Map<String, Integer>>) allDataCheck.get("tasCrit");
            if (!tasCritses.isEmpty()) {
                List<M_PRICING_ADJUSTMENT_HEADER> mPricingAdjustmentHeaders = mPricingAdjustmentHeaderRepo.findAllBymPricingDetailId(dataDetail.getId());

                if(!mPricingAdjustmentHeaders.isEmpty()) {
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
                    if(dataPricing.isPresent()) {
//                        PriceCodeDetailAdjustmentDTO dataAdjustment = objectMapper.convertValue(dataPricing, PriceCodeDetailAdjustmentDTO.class);
                        Optional<R_GLOBAL_TYPE_VALUE> type = rGlobalTypeValueRepo.findByGlbTypeValId(dataPricing.get().getAdjustmentType());
                        if (type.isPresent()) {
                            dataAdjustment.setAdjustmentType(type.get().getName());
                        }
                        BigDecimal adjusValue = dataPricing.get().getAdjustmentValue().setScale(2, RoundingMode.HALF_UP);
                        dataAdjustment.setAdjustmentValue(adjusValue);
                        dataAdjustment.setPriceAdjustmentDetailId(dataPricing.get().getId());

                        if(dataPricing.get().getAdjustmentType() == 221) {
                            dataAdjustment.setFinalAdjustmentValue(dataAdjustment.getAdjustmentValue());
                        } else if(dataPricing.get().getAdjustmentType() == 222) {
                            BigDecimal total = BigDecimal.ZERO;
                            total = total.add(dataDetail.getValue());
                            total = total.add(dataAdjustment.getAdjustmentValue());
                            total = total.setScale(2, RoundingMode.HALF_UP);
                            dataAdjustment.setFinalAdjustmentValue(total);
                        }else {
                            BigDecimal bd1 = dataAdjustment.getAdjustmentValue();
                            BigDecimal bd2 = dataDetail.getValue();
                            BigDecimal total = bd2.subtract(bd1);
                            total = total.setScale(2, RoundingMode.HALF_UP);
                            dataAdjustment.setFinalAdjustmentValue(total);
                        }

                        String uomName = "";
                        if(dataDetail.getUom() != null) {
                            Optional<R_GLOBAL_TYPE_VALUE> rGlobalTypeValueUom = rGlobalTypeValueRepo.findByGlbTypeValId(Integer.valueOf(dataDetail.getUom()));
                            if(rGlobalTypeValueUom.isPresent()) {
                                uomName = rGlobalTypeValueUom.get().getName();
                            }
                        }

                        String currencyName = "";
                        if(dataDetail.getUom() != null) {
                            Optional<R_GLOBAL_TYPE_VALUE> rGlobalTypeValueCurrency = rGlobalTypeValueRepo.findByGlbTypeValId(Integer.valueOf(dataDetail.getCurrency()));
                            if(rGlobalTypeValueCurrency.isPresent()) {
                                currencyName = rGlobalTypeValueCurrency.get().getName();
                            }
                        }

                        Optional<M_PRICING_ADJUSTMENT_HEADER> mPricingAdjustmentHeader = mPricingAdjustmentHeaderRepo.findById(dataPricing.get().getPricingAdjustmentId());
                        if(mPricingAdjustmentHeader.isPresent()) {
                            dataAdjustment.setAdjustmentName(mPricingAdjustmentHeader.get().getName());
                            dataAdjustment.setAdjustmentText(mPricingAdjustmentHeader.get().getName() + " - " + currencyName + "/" + dataAdjustment.getFinalAdjustmentValue() + "/" + uomName);
                        }

                        return dataAdjustment;

                    }else {
                        return null;
                    }

                }

            } else {
                result = null;

                return result;
            }

            return null;

        } catch (Exception e) {
            logger.error(CommonVariables.ERROR_IN, e.getMessage(), e);
            return null;
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    public PricingRuleDetailDTO getPriceAdjustmentTieringByCriteriaToDTO(VW_PRICING_RULE_DETAIL dataPricingRule, Integer accountId, HttpServletRequest httpServlet) {
        logger.info("get List Product");
        PricingRuleDetailDTO result = new PricingRuleDetailDTO();
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

            PricingRuleDetailDTO data = new PricingRuleDetailDTO();
            data.setCreatedBy(dataPricingRule.getCreatedBy());
            data.setCreatedDate(dataPricingRule.getCreatedDate());
            data.setDescription(dataPricingRule.getDescription());
            data.setIsDeleted(dataPricingRule.getIsDeleted());
            if(isStringAllDigits(dataPricingRule.getMax())) {
                data.setMax(Integer.valueOf(dataPricingRule.getMax()));
            }else {
                data.setMax(0);
            }
            data.setMin(dataPricingRule.getMin());
            data.setPriceCodeId(dataPricingRule.getPriceCodeId());
            data.setPriceCode(dataPricingRule.getPriceCode());
            data.setPricingRuleDetailId(dataPricingRule.getPricingRuleDetailId());
            data.setPricingRuleId(dataPricingRule.getPricingRuleId());
            data.setUpdatedBy(dataPricingRule.getUpdatedBy());
            data.setUpdatedDate(dataPricingRule.getUpdatedDate());
            data.setLineNumber(dataPricingRule.getLinenumber());
            data.setUom(dataPricingRule.getUom());
            data.setCurrency(dataPricingRule.getCurrency());
            data.setIsUnlim(dataPricingRule.getIsUnlim());
            data.setValue(dataPricingRule.getValue());

            List<Integer> idCritFiltered = new ArrayList<>();
            boolean valid = false;

            String currencyId = "";
            Optional<R_GLOBAL_TYPE_VALUE> getCurrencyId = rGlobalTypeValueRepo.findByNameAndGlobalType(data.getCurrency(), 242);
            if(getCurrencyId.isPresent()) {
                currencyId = String.valueOf(getCurrencyId.get().getGlbTypeValId());
            }

            String uomId = "";
            Optional<R_GLOBAL_TYPE_VALUE> getUomId = rGlobalTypeValueRepo.findByNameAndGlobalType(data.getUom(), 345);
            if(getUomId.isPresent()) {
                uomId = String.valueOf(getUomId.get().getGlbTypeValId());
            }

            List<R_PRICING_DETAIL> dataDetailPricing = rPricingDetailRepo.findAllByIdPricingAndCurrencyAndUom(data.getPriceCodeId(), currencyId, uomId);

            // criteria filter
            List<Map<String, Integer>> tasCritses = (List<Map<String, Integer>>) allDataCheck.get("tasCrit");
            if (!tasCritses.isEmpty()) {
                for (R_PRICING_DETAIL priceCodeDetail : dataDetailPricing) {
                    List<M_PRICING_ADJUSTMENT_HEADER> mPricingAdjustmentHeaders = mPricingAdjustmentHeaderRepo.findAllBymPricingDetailId(priceCodeDetail.getId());

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
//                        PriceCodeDetailAdjustmentDTO dataAdjustment = new PriceCodeDetailAdjustmentDTO();
                        if (dataPricing.isPresent()) {
                            PriceCodeDetailAdjustmentDTO dataAdjustment = objectMapper.convertValue(dataPricing,
                                    PriceCodeDetailAdjustmentDTO.class);
                            Optional<R_GLOBAL_TYPE_VALUE> type = rGlobalTypeValueRepo.findByGlbTypeValId(dataPricing.get().getAdjustmentType());
                            type.ifPresent(rGlobalTypeValue -> dataAdjustment.setAdjustmentType(rGlobalTypeValue.getName()));
//                            dataAdjustment.setAdjustmentValue(dataPricing.get().getAdjustmentValue());
                            dataAdjustment.setPriceAdjustmentDetailId(dataPricing.get().getId());

                            if (dataPricing.get().getAdjustmentType() == 221) {
                                dataAdjustment.setFinalAdjustmentValue(dataPricing.get().getAdjustmentValue());
                            } else if (dataPricing.get().getAdjustmentType() == 222) {
                                BigDecimal total = BigDecimal.ZERO;
                                String resultString = data.getValue().substring(0, data.getValue().length() - 3);
                                String validDecimalString = resultString.replaceAll("[^0-9]", "");
                                DecimalFormat decimalFormat = new DecimalFormat();
                                decimalFormat.setParseBigDecimal(true);
                                BigDecimal bd2 = (BigDecimal) decimalFormat.parse(validDecimalString);
                                total = total.add(bd2);
                                total = total.add(dataPricing.get().getAdjustmentValue());
                                dataAdjustment.setFinalAdjustmentValue(total);
                            } else {
                                BigDecimal bd1 = dataPricing.get().getAdjustmentValue();
                                String resultString = data.getValue().substring(0, data.getValue().length() - 3);
                                String validDecimalString = resultString.replaceAll("[^0-9]", "");
                                DecimalFormat decimalFormat = new DecimalFormat();
                                decimalFormat.setParseBigDecimal(true);
                                BigDecimal bd2 = (BigDecimal) decimalFormat.parse(validDecimalString);
                                BigDecimal total = bd2.subtract(bd1);
                                dataAdjustment.setFinalAdjustmentValue(total);
                            }

                            Optional<M_PRICING_ADJUSTMENT_HEADER> mPricingAdjustmentHeader = mPricingAdjustmentHeaderRepo.findById(dataPricing.get().getPricingAdjustmentId());
                            if (mPricingAdjustmentHeader.isPresent()) {
                                dataAdjustment.setAdjustmentName(mPricingAdjustmentHeader.get().getName());
                                dataAdjustment.setAdjustmentText(mPricingAdjustmentHeader.get().getName() + " - " + data.getCurrency() + "/" + dataAdjustment.getFinalAdjustmentValue() + "/" + data.getUom());
                            }
                            data.setAdjustment(dataAdjustment);
                        }
                    }
                }
            }

            result = data;

            return result;

        } catch (Exception e) {
            logger.error(CommonVariables.ERROR_IN, e.getMessage(), e);
            return null;
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> getChooseProductByCriteria(
            MaterialTablePagingRequest pagingData,
            PagedResourcesAssembler<VW_PRODUCT> assembler,
            Integer accountId, Integer productType, Integer serviceType, HttpServletRequest httpServlet) {
        logger.info("get List Product");
        ResponseObject result = new ResponseObject();
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

            List<VW_PRODUCT> data;
            List<VW_PRODUCT> mProducts = null;

            String serviceTypeName = "";
            String productTypeName = "";

            if(serviceType == 608) {
                serviceTypeName = "Gas";
            }
            else {
                serviceTypeName = "Non Gas";
            }
            if(productType == 245) {
                productTypeName = "Product";
            }else {
                productTypeName = "AddOn";
            }

            data = vwProductRepo.findAllByEntityIdAndServiceTypeNameAndProductTypeNameAndStatus(UserDetailUtils.getUserEntity(), serviceTypeName, productTypeName, FlowStatus.ACTIVE.name());

            if(!data.isEmpty()) {
                mProducts = data;
            }

            List<Integer> idCritFiltered = new ArrayList<>();
            boolean valid = false;

            if (mProducts == null || mProducts.isEmpty()) {

                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, ResponseUtils.MESSAGE_NOT_FOUND,
                        ResponseUtils.DATA_EMPTY);

                return new ResponseEntity<>(result, HttpStatus.OK);
            }

            // criteria filter
            List<Map<String, Integer>> tasCritses = (List<Map<String, Integer>>) allDataCheck.get("tasCrit");
            if (!tasCritses.isEmpty()) {
                for (VW_PRODUCT productId : mProducts) {
                    List<M_PRODUCT_VERSION> getVersion = mProductVersionRepo.findByProductIdOrderByCreatedDateDesc(productId.getId());
                    if(!getVersion.isEmpty()) {
                        for (M_PRODUCT_VERSION version : getVersion) {
                            M_PRODUCT_TARGET_ACCOUNT_SELLING getTas = mProductTargetAccountSellingHeaderRepo.findTopByProductVersionId(version.getId());
                            if(getTas != null) {
                                List<R_CRITERIA_DATA_PRODUCT_TAS> critDatas = rCriteriaDataProductTasRepo.findByIdProductTas(getTas.getId());
                                if (!critDatas.isEmpty()) {
                                    for (R_CRITERIA_DATA_PRODUCT_TAS critData : critDatas) {

                                        Date startDate = critData.getStartDate();
                                        Date endDate = critData.getEndDate();

                                        // filtered date now
                                        if(startDate != null && UtilsAccount.isDateInRange(startDate, endDate)) {
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
                                                    idCritFiltered.add(critData.getIdProductTas());
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
                        }
                    }
                }
                logger.info(idCritFiltered.toString());

                List<Integer> getProductVersionList = new ArrayList<>();

                for (Integer idCrit: idCritFiltered) {
                    Optional<M_PRODUCT_TARGET_ACCOUNT_SELLING> productTAS = mProductTargetAccountSellingHeaderRepo.findById(idCrit);
                    productTAS.ifPresent(mProductTargetAccountSelling -> getProductVersionList.add(mProductTargetAccountSelling.getProductVersionId()));
                }
                List<Integer> getProductId = new ArrayList<>();
                if(!getProductVersionList.isEmpty()) {
                    for (Integer versionProduct : getProductVersionList) {
                        Optional<M_PRODUCT_VERSION> productVersion = mProductVersionRepo.findByIdAndStatus(versionProduct, FlowStatus.ACTIVE.name());
                        productVersion.ifPresent(mProductVersion -> getProductId.add(mProductVersion.getProductId()));
                    }
                }

                if (!getProductId.isEmpty()) {

                    mProducts = mProducts.stream().filter(e -> getProductId.contains(e.getId()))
                            .collect(Collectors.toList());
                } else {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
                            ResponseUtils.MESSAGE_NOT_FOUND, ResponseUtils.DATA_EMPTY);

                    return new ResponseEntity<>(result, HttpStatus.OK);
                }

            } else {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, ResponseUtils.MESSAGE_NOT_FOUND,
                        ResponseUtils.DATA_EMPTY);

                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            List<ChooseProductDTO> vos = new ArrayList<>();

            for (VW_PRODUCT mProducts_ : mProducts) {
                ChooseProductDTO mProductDto = new ChooseProductDTO();
                mProductDto.setId(mProducts_.getId());
                mProductDto.setProductName(mProducts_.getProductName());
                mProductDto.setProductDescription(mProducts_.getProductDescription());
                mProductDto.setProductType(mProducts_.getProductTypeName());
                Optional<M_PRODUCT> getProduct = mProductRepo.findById(mProductDto.getId());
                if(getProduct.isPresent()) {
                    mProductDto.setProductTypeId(getProduct.get().getProductType());
                    mProductDto.setProductClassId(getProduct.get().getProductClass());
                }
                mProductDto.setProductClass(mProducts_.getProductClassName());
                mProductDto.setServiceType(mProducts_.getServiceTypeName());
                mProductDto.setStatus(mProducts_.getStatus());

                vos.add(mProductDto);
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_SUCCESS, vos);

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getServiceType() {
        logger.info("Get List Service Type");
        ResponseObject result = new ResponseObject();

        try {
            List<R_GLOBAL_TYPE_VALUE> datas = rGlobalTypeValueRepo.findAllByGlobalTypeAndStatus(602, FlowStatus.ACTIVE.name());
            List<DdlMapperDTO> newData = new ArrayList<>();
            for (R_GLOBAL_TYPE_VALUE data : datas) {
                DdlMapperDTO dataDto = new DdlMapperDTO();
                dataDto.setId(data.getGlbTypeValId());
                dataDto.setName(data.getName());
                dataDto.setValue(data.getGlbValue());
                newData.add(dataDto);
            }
            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success Get List Service Type");
            result.setData(newData);

            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getSaTypeMain(Integer serviceTypeId) {
        logger.info("Get List Type");
        ResponseObject result = new ResponseObject();

        try {
            List<R_GLOBAL_TYPE_VALUE> datas = rGlobalTypeValueRepo.findAllByGlobalTypeAndParentValue(56, serviceTypeId);
            List<DdlMapperDTO> newData = new ArrayList<>();
            for (R_GLOBAL_TYPE_VALUE data : datas) {
                DdlMapperDTO dataDto = new DdlMapperDTO();
                dataDto.setId(data.getGlbTypeValId());
                dataDto.setName(data.getName());
                dataDto.setValue(data.getGlbValue());
                newData.add(dataDto);
            }
            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success Get List Sa Type");
            result.setData(newData);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {

            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getSaTypeAddon(Integer serviceTypeId) {
        logger.info("Get List Type");
        ResponseObject result = new ResponseObject();

        try {
            List<R_GLOBAL_TYPE_VALUE> datas = rGlobalTypeValueRepo.findAllByGlobalTypeAndParentValue(196060, serviceTypeId);
            List<DdlMapperDTO> newData = new ArrayList<>();
            for (R_GLOBAL_TYPE_VALUE data : datas) {
                DdlMapperDTO dataDto = new DdlMapperDTO();
                dataDto.setId(data.getGlbTypeValId());
                dataDto.setName(data.getName());
                dataDto.setValue(data.getGlbValue());
                newData.add(dataDto);
            }
            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success Get List Sa Type Addon");
            result.setData(newData);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {

            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getPjbgType() {
        logger.info("PJBG TYPE");
        ResponseObject result;
        try {
            List<LinkedHashMap<String, Object>> allData = criteriaServices.getCriteriaByGlobalType("PJBG TYPE");
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_OK, allData);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(CommonVariables.ERROR_IN, e.getMessage(), e);
            throw e;
        }
//        logger.info("Get List PJBG Type");
//        ResponseObject result = new ResponseObject();
//
//        try {
//            List<R_GLOBAL_TYPE_VALUE> datas = rGlobalTypeValueRepo.findByGlobalType(43);
//            List<DdlMapperDTO> newData = new ArrayList<>();
//            for (R_GLOBAL_TYPE_VALUE data : datas) {
//                DdlMapperDTO dataDto = new DdlMapperDTO();
//                dataDto.setId(data.getGlbTypeValId());
//                dataDto.setName(data.getName());
//                dataDto.setValue(data.getGlbValue());
//                newData.add(dataDto);
//            }
//            result.setSuccess(true);
//            result.setCode(HttpStatus.OK);
//            result.setMessage("Success Get List Pjbg Type");
//            result.setData(newData);
//            return new ResponseEntity<>(result, HttpStatus.OK);
//
//        } catch (Exception e) {
//
//            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
//            return new ResponseEntity<>(
//                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
//                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
//                    HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> listVersion(ListProductVersionDTO request, HttpServletRequest httpServletRequest) {
        logger.info("Get List Version");
        ResponseObject result = new ResponseObject();
        try {
            Optional<List<M_PRODUCT_VERSION>> mProductVersionOpt = mProductVersionRepo.findAllByProductIdOrderByCreatedDateDesc(request.getProductId());

            if (mProductVersionOpt.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
                        "Product Version not found", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            List<M_PRODUCT_VERSION> mProductVersion = mProductVersionOpt.get();
            VersionListProductDTO newData = new VersionListProductDTO();
            List<VersionListDTO> vers = new ArrayList<>();
            Integer versionProduct = 0;

            if(!mProductVersion.isEmpty()) {
                for(M_PRODUCT_VERSION  version: mProductVersion) {
                    VersionListDTO newArray = new VersionListDTO();
                    newArray.setId(version.getId());
                    newArray.setName(version.getVersion());
                    vers.add(newArray);
                    // debug
                    if(versionProduct.equals(0)) {
                        versionProduct = version.getId();
                    }
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
                            if(dtl.getNameId() != null) {
                                R_GLOBAL_TYPE_VALUE getDetailName = globalTypeValueService.getGlobalTypeByGlbTypeValId("PRODUCT DETAIL NAME", dtl.getNameId());
                                productDetailDto.setName(getDetailName.getName());
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
                    List<PricingRuleDTO> sortedPricingRuleList = new ArrayList<>();
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
                                rGlobalTypeValue.ifPresent(g -> newDetailDto.setCurrency(g.getName()));
                                newDetailDto.setDescription(dataDetail.getDescription());
                                newDetailDto.setValue(dataDetail.getValue());
                                newDetailDto.setUom(dataDetail.getUom());
                                if(dataDetail.getUom() != null) {
                                    Optional<R_GLOBAL_TYPE_VALUE> rGlobalTypeValueUom = rGlobalTypeValueRepo.findByGlbTypeValId(Integer.valueOf(dataDetail.getUom()));
                                    rGlobalTypeValueUom.ifPresent(g -> newDetailDto.setUomName(g.getName()));
                                }

                                newDetailDto.setAdjustment(this.getPriceAdjustmentByCriteriaToDTO(request.getAccountId(), dataDetail, httpServletRequest));
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

//                            Optional<M_PRICING_RULE> mPricingRule = mPricingRuleRepo.findByPricingRuleId(data.getPricingRuleId());
                            List<M_PRICING_RULE> mPricingRule = this.getPricingRuleByCriteria(httpServletRequest, request.getAccountId());
                            List<PricingRuleDTO> pricingRuleDTO = new ArrayList<>();
                            Integer pricingRuleId = null;
                            if(StringUtils.hasValue(mPricingRule)) {
                                for(M_PRICING_RULE pricingRule : mPricingRule) {
                                    // Price Rule List
                                    PricingRuleDTO newPricingRule = new PricingRuleDTO();
                                    newPricingRule.setName(pricingRule.getName());
                                    newPricingRule.setPricingRuleId(pricingRule.getPricingRuleId());
                                    pricingRuleDTO.add(newPricingRule);

                                    // Price Rule Tiering
                                    Optional<M_PRODUCT_PRICING> mProductPricingOpt = mProductPricingRepo.findTopByProductVersionId(versionProduct);
                                    if(mProductPricingOpt.isPresent()) {
                                        M_PRODUCT_PRICING getProductPricing = mProductPricingOpt.get();
                                        if(StringUtils.hasValue(getProductPricing.getPricingRuleId()) && getProductPricing.getPricingRuleId().equals(pricingRule.getPricingRuleId())) {
                                            List<VW_PRICING_RULE_DETAIL> mPricingRuleDetails = vwPricingRuleDetailRepo.findAllByPricingRuleId(getProductPricing.getPricingRuleId());
                                            if (!mPricingRuleDetails.isEmpty()) {
                                                for (VW_PRICING_RULE_DETAIL dataPricingRule : mPricingRuleDetails) {
                                                    // List version
                                                    pricingRuleDetailDTO.add(this.getPriceAdjustmentTieringByCriteriaToDTO(dataPricingRule, request.getAccountId(), httpServletRequest));
                                                }
                                                pricingRuleId = pricingRule.getPricingRuleId();
                                            }
                                        }
                                    }
                                }

                                // sorted by pricingRule exist
                                if(StringUtils.hasValue(pricingRuleId)) {
                                    Integer finalPricingRuleId = pricingRuleId;
                                    sortedPricingRuleList = pricingRuleDTO.stream()
                                            .filter(dto -> dto.getPricingRuleId().equals(finalPricingRuleId))
                                            .collect(Collectors.toList());
                                    sortedPricingRuleList.addAll(
                                            pricingRuleDTO.stream()
                                                    .filter(dto -> !dto.getPricingRuleId().equals(finalPricingRuleId))
                                                    .collect(Collectors.toList())
                                    );
                                } else {
                                    sortedPricingRuleList.addAll(pricingRuleDTO);
                                }

                            }
                        }
                    }
                    productDetailPricingDTO.setPriceCodeList(pricingCodeDto);
                    productDetailPricingDTO.setPriceRuleList(sortedPricingRuleList);
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
                                    mRbiTaxCode.ifPresent(m -> calculationRuleViewDto.setUomName(m.getTaxCodeName()));
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
                            List<R_PRODUCT_TOS> newProductTos = rProductTermOfServiceRepo.findAllByIdMProductTos(dtl.getId());
                            tosDto.setTosDetail(newProductTos);
                            tosDtos.add(tosDto);
                        }

                        newDataProduct.setProductTos(tosDtos);
                    }
                    // LateCharge
                    CriteriaDataDTO criteriaData = new CriteriaDataDTO();
                    criteriaData.setProductVersionId(productVersionId);
                    criteriaData.setServiceType(getProduct.get().getServiceType());
                    criteriaData.setAccountId(request.getAccountId());
                    criteriaData.setPriceCode(priceCodeId);
                    LinkedHashMap<String, Object> getLateCharge = this.lateChargeForDetail(criteriaData);
                    if(!getLateCharge.isEmpty()) {
                        newDataProduct.setLateCharge(getLateCharge);
                    }
                }

                newData.setVersionList(vers);
                newData.setProduct(newDataProduct);
            }

            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success View Product Version List");
            result.setData(newData);
            return new ResponseEntity<>(result, HttpStatus.OK);

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
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> getProductVersionInformation(Integer id, Integer accountId, HttpServletRequest httpServletRequest) {
        logger.info("get product information");
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_PRODUCT_VERSION> mProductVersionOpt = mProductVersionRepo.findById(id);

            if (mProductVersionOpt.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
                        "Product Version not found", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            int productId = mProductVersionOpt.get().getProductId();
            int productVersionId = mProductVersionOpt.get().getId();
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
                            rGlobalTypeValue.ifPresent(r -> newDetailDto.setCurrency(r.getName()));
                            newDetailDto.setDescription(dataDetail.getDescription());
                            newDetailDto.setValue(dataDetail.getValue());
                            newDetailDto.setUom(dataDetail.getUom());
                            if(dataDetail.getUom() != null) {
                                Optional<R_GLOBAL_TYPE_VALUE> rGlobalTypeValueUom = rGlobalTypeValueRepo.findByGlbTypeValId(Integer.valueOf(dataDetail.getUom()));
                                rGlobalTypeValueUom.ifPresent(r -> newDetailDto.setUomName(r.getName()));
                            }
                            newDetailDto.setAdjustment(this.getPriceAdjustmentByCriteriaToDTO(accountId, dataDetail, httpServletRequest));
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
                        pricingCodeDto.add(newDto);

                        Optional<M_PRICING_RULE> mPricingRule = mPricingRuleRepo.findByPricingRuleId(data.getPricingRuleId());
                        PricingRuleDTO newPricingRule = new PricingRuleDTO();
                        if(mPricingRule.isPresent()) {
                            newPricingRule.setName(mPricingRule.get().getName());
                            newPricingRule.setPricingRuleId(mPricingRule.get().getPricingRuleId());
                            pricingRuleDTO.add(newPricingRule);

                            List<VW_PRICING_RULE_DETAIL> mPricingRuleDetails = vwPricingRuleDetailRepo.findAllByPricingRuleId(mPricingRule.get().getPricingRuleId());
                            if(!mPricingRuleDetails.isEmpty()) {
                                for (VW_PRICING_RULE_DETAIL dataPricingRule : mPricingRuleDetails ) {
                                    pricingRuleDetailDTO.add(this.getPriceAdjustmentTieringByCriteriaToDTO(dataPricingRule, accountId, httpServletRequest));
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
                                mRbiTaxCode.ifPresent(m -> calculationRuleViewDto.setUomName(m.getTaxCodeName()));
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
                criteriaData.setAccountId(accountId);
                criteriaData.setPriceCode(priceCodeId);
                LinkedHashMap<String, Object> getLateCharge = this.lateChargeForDetail(criteriaData);
                if(!getLateCharge.isEmpty()) {
                    newDataProduct.setLateCharge(getLateCharge);
                }
            }

            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success View Product Version List");
            result.setData(newDataProduct);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> listBillingCycle() {
        logger.info("Get List Billing Cycle");
        ResponseObject result = new ResponseObject();
        try {
            List<M_RBI_BILLING_CYCLE> datas = mRbiBillingCycleRepo.findAllByStatus(FlowStatus.ACTIVE.name());
            List<DdlMapperDTO> newData = new ArrayList<>();
            if(!datas.isEmpty()) {
                for (M_RBI_BILLING_CYCLE data : datas ) {
                    DdlMapperDTO dataDto = new DdlMapperDTO();
                    dataDto.setId(data.getBillingCycleId());
                    dataDto.setName(data.getBeginCycle() + " - " + data.getEndCycle() + " " + data.getTimeUnit());
                    dataDto.setValue(data.getBeginCycle() + " - " + data.getEndCycle() + " " + data.getTimeUnit());
                    newData.add(dataDto);
                }
            }

            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success Get List Billing Cycle");
            result.setData(newData);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> listInvoiceTemplate(Integer accountId) {
        logger.info("Get List Invoice Template");
        ResponseObject result = new ResponseObject();
        try {
            Optional<VW_ACCOUNT_CRITERIA> dataAccount = vwAccountCriteriaRepo.findByAccountId(accountId);
            Map<String, Integer> accountCriteria = new HashMap<>();
            if(dataAccount.isPresent()) {
                accountCriteria.put("customer", dataAccount.get().getCustomerId());
                accountCriteria.put("budget", dataAccount.get().getAccountBudget());
                accountCriteria.put("subDistrict", dataAccount.get().getPremiseSubdistrict());
                accountCriteria.put("district", dataAccount.get().getPremiseDistrict());
                accountCriteria.put("city", dataAccount.get().getPremiseCity());
                accountCriteria.put("province", dataAccount.get().getPremiseProvince());
                accountCriteria.put("area", dataAccount.get().getCostCenter());
                accountCriteria.put("sor", dataAccount.get().getSor());
                accountCriteria.put("industrialSector", dataAccount.get().getAccountIndustrialSector());
                accountCriteria.put("product", dataAccount.get().getSaProductVersion());
                accountCriteria.put("gSizes", dataAccount.get().getPremiseSpAssetGSize());
                accountCriteria.put("customerSegment", dataAccount.get().getAccountSegment());
                accountCriteria.put("accountGroup", dataAccount.get().getAccountGroupType());
                accountCriteria.put("serviceType", dataAccount.get().getSaServiceType());
                accountCriteria.put("accountCategory", dataAccount.get().getAccountCategory());
                accountCriteria.put("allCriteria", null);
            }

            boolean valid = false;
            List<Integer> idCritFiltered = new ArrayList<>();
            if (!accountCriteria.isEmpty()) {
                List<M_RBI_INVOICE_TEMPLATE> listTop = mRbiInvoiceTemplateRepo.findAllByStatus(FlowStatus.ACTIVE.name());
                System.out.println("listTop : " + listTop);
                for (M_RBI_INVOICE_TEMPLATE top : listTop) {
                    List<R_RBI_INVOICE_TEMPLATE_CRITERIA_DATA> critDatas = rRbiInvoiceTemplateCriteriaDataRepo.findAllByInvoiceTemplateId(top.getId());
                    System.out.println("critDatas : " + critDatas);
                    if (!critDatas.isEmpty()) {
                        for (R_RBI_INVOICE_TEMPLATE_CRITERIA_DATA critData : critDatas) {

                            Date startDate = critData.getStartDate();
                            Date endDate = critData.getEndDate();

                            // filtered date now
                            if (UtilsAccount.isDateInRange(startDate, endDate)) {
                                if (StringUtils.hasValue(critData.getAllCriteria()) && critData.getAllCriteria().equals('Y')) {
                                    idCritFiltered.add(critData.getInvoiceTemplateId());
                                    break;
                                }
                                Map<String, Integer> topCriteria = new HashMap<>();
                                topCriteria.put("customer", critData.getCustomer());
                                topCriteria.put("budget", critData.getBudget());
                                topCriteria.put("subDistrict", critData.getSubDistrict());
                                topCriteria.put("district", critData.getDistrict());
                                topCriteria.put("city", critData.getCity());
                                topCriteria.put("province", critData.getProvince());
                                topCriteria.put("sor", critData.getSor());
                                topCriteria.put("area", critData.getArea());
                                topCriteria.put("industrialSector", critData.getIndustrialSector());
                                topCriteria.put("serviceType", critData.getServiceType());
                                topCriteria.put("product", critData.getProduct());
                                topCriteria.put("gsizes", critData.getGSizes());
                                topCriteria.put("customerSegment", critData.getCustomerSegment());
                                topCriteria.put("accountGroupType", critData.getAccountGroup());
                                topCriteria.put("accountCategory", critData.getAccountCategory());

                                valid = CheckCriteria.compareCriteria(CheckCriteria.INVOICE_TEMPLATE, accountCriteria, topCriteria);
                                if (valid) {
                                    idCritFiltered.add(critData.getInvoiceTemplateId());
                                    break;
                                }

                                if (valid) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            idCritFiltered.stream().distinct().collect(Collectors.toList());
            System.out.println("idCritFiltered : " + idCritFiltered);
            List<M_RBI_INVOICE_TEMPLATE> datas = mRbiInvoiceTemplateRepo.findAllByIdIn(idCritFiltered);

            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success Get List Invoice Template");
            result.setData(datas);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> listTermOfPayment(Integer accountId) {
        logger.info("Get List Term of Payment");
        ResponseObject result = new ResponseObject();
        try {

            Optional<VW_ACCOUNT_CRITERIA> dataAccount = vwAccountCriteriaRepo.findByAccountId(accountId);
            Map<String, Integer> accountCriteria = new HashMap<>();
            if(dataAccount.isPresent()) {
                accountCriteria.put("customer", dataAccount.get().getCustomerId());
                accountCriteria.put("budget", dataAccount.get().getAccountBudget());
                accountCriteria.put("subDistrict", dataAccount.get().getPremiseSubdistrict());
                accountCriteria.put("district", dataAccount.get().getPremiseDistrict());
                accountCriteria.put("city", dataAccount.get().getPremiseCity());
                accountCriteria.put("province", dataAccount.get().getPremiseProvince());
                accountCriteria.put("area", dataAccount.get().getCostCenter());
                accountCriteria.put("sor", dataAccount.get().getSor());
                accountCriteria.put("industrialSector", dataAccount.get().getAccountIndustrialSector());
                accountCriteria.put("product", dataAccount.get().getSaProductVersion());
                accountCriteria.put("gsizes", dataAccount.get().getPremiseSpAssetGSize());
                accountCriteria.put("customerSegment", dataAccount.get().getAccountSegment());
                accountCriteria.put("accountGroupType", dataAccount.get().getAccountGroupType());
                accountCriteria.put("serviceType", dataAccount.get().getSaServiceType());
                accountCriteria.put("accountCategory", dataAccount.get().getAccountCategory());
                accountCriteria.put("allCriteria", null);
            }

            boolean valid = false;
            List<Integer> idCritFiltered = new ArrayList<>();
            if (!accountCriteria.isEmpty()) {
                List<M_RBI_TERMS_OF_PAYMENT> listTop = mRbiTermsOfPaymentRepo.findAllByStatus(FlowStatus.ACTIVE.name());
                for (M_RBI_TERMS_OF_PAYMENT top : listTop) {
                    List<M_RBI_TOP_CRITERIA_DATA> critDatas = rRbiTopCriteriaDataRepo.findAllByTermOfPaymentId(top.getTermsOfPaymentId());
                    if (!critDatas.isEmpty()) {
                        for (M_RBI_TOP_CRITERIA_DATA critData : critDatas) {

                            Date startDate = critData.getStartDate();
                            Date endDate = critData.getEndDate();

                            // filtered date now
                            if (UtilsAccount.isDateInRange(startDate, endDate)) {
                                if (StringUtils.hasValue(critData.getAllCriteria()) && critData.getAllCriteria().equals('Y')) {
                                    idCritFiltered.add(critData.getTermOfPaymentId());
                                    break;
                                }
                                Map<String, Integer> topCriteria = new HashMap<>();
                                topCriteria.put("customer", critData.getCustomer());
                                topCriteria.put("budget", critData.getBudget());
                                topCriteria.put("subDistrict", critData.getSubDistrict());
                                topCriteria.put("district", critData.getDistrict());
                                topCriteria.put("city", critData.getCity());
                                topCriteria.put("province", critData.getProvince());
                                topCriteria.put("sor", critData.getSor());
                                topCriteria.put("area", critData.getArea());
                                topCriteria.put("industrialSector", critData.getIndustrialSector());
                                topCriteria.put("serviceType", critData.getServiceType());
                                topCriteria.put("product", critData.getProduct());
                                topCriteria.put("gsizes", critData.getGsizes());
                                topCriteria.put("customerSegment", critData.getCustomerSegment());
                                topCriteria.put("accountGroupType", critData.getAccountGroupType());
                                topCriteria.put("accountCategory", critData.getAccountCategory());

                                valid = CheckCriteria.compareCriteria(CheckCriteria.TERM_OF_PAYMENT, accountCriteria, topCriteria);
                                if (valid) {
                                    idCritFiltered.add(critData.getTermOfPaymentId());
                                    break;
                                }

                                if (valid) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            idCritFiltered.stream().distinct().collect(Collectors.toList());
            List<M_RBI_TERMS_OF_PAYMENT> dataTop = mRbiTermsOfPaymentRepo.findAllByTermsOfPaymentIdIn(idCritFiltered);

            List<LinkedHashMap<String, Object>> gloList = new LinkedList<>();
            if (!dataTop.isEmpty()) {
                for (M_RBI_TERMS_OF_PAYMENT rgtv : dataTop) {
                    LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                    ar.put("termsOfPaymentId", rgtv.getTermsOfPaymentId());
                    switch (rgtv.getTopTypeName()) {
                        case "After":
                            ar.put("termsOfPaymentName", "H" + " + " + rgtv.getTopTerms());
                            break;
                        case "Date":
                            ar.put("termsOfPaymentName","Tanggal " + rgtv.getTopTerms());
                            break;
                        default:
                            break;
                    }

                    gloList.add(ar);
                }
                result.setSuccess(true);
                result.setCode(HttpStatus.OK);
                result.setMessage("Success get Term of Payment Data");
                result.setData(gloList);
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Fail get Term of Payment Data");
                result.setData(ResponseUtils.DATA_EMPTY);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error in -> {}", e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> getTaxImplication(CriteriaDataDTO request) {
        logger.info("Get Tax Implication");
        try {

            LinkedHashMap<String, Object> allDataCheck = new LinkedHashMap<>();

            // GET DATA ACCOUNT
            Optional<VW_ACCOUNT_CRITERIA> dataAccount = vwAccountCriteriaRepo.findByAccountId(request.getAccountId());

            if(dataAccount.isPresent()) {
                List<Integer> categoryId = new ArrayList<>(); //ppn pph
                categoryId.add(127);
                categoryId.add(128);

                for(Integer cat : categoryId) {
                    List<Integer> idPPNPPH  = new ArrayList<>();
                    List<M_AM_TAXIMPLICATION> dataPPNPPH;
                    if(request.getServiceType() == 608) {
                        dataPPNPPH = taxImpliRepo.findAllByCategoryAndServiceType(cat, 131);
                    }else if(request.getServiceType() == 609){
                        dataPPNPPH = taxImpliRepo.findAllByCategoryAndServiceType(cat, 129);
                    }else {
                        dataPPNPPH = taxImpliRepo.findAllByCategoryAndServiceType(cat, 130);
                    }

                    if(!dataPPNPPH.isEmpty()) {
                        for(M_AM_TAXIMPLICATION ppnPph : dataPPNPPH){
                            idPPNPPH.add(ppnPph.getId());
                        }
                    }
                    List<VW_AM_TAXIMPLICATION_CRITERIA_DATA> dataCriteriaPPNPPH = vwAmTaxImplicationCriteriaDataRepo.findIn(idPPNPPH);

                    List<CriteriaData> addedCriteriaPPNPPH = new ArrayList<>();

                    if(!dataCriteriaPPNPPH.isEmpty()) {
                        for(VW_AM_TAXIMPLICATION_CRITERIA_DATA zz : dataCriteriaPPNPPH){
                            CriteriaData data = new CriteriaData();
                            data.setId(zz.getId());
                            data.setAccountCategory(zz.getAccountCategory());
                            data.setAccountGroupType(zz.getAccountGroupType());
                            data.setAccountNumber(zz.getAccountNumber());
                            data.setAccountSegment(zz.getAccountSegment());
                            data.setAccountType(zz.getAccountType());
                            data.setClassificationType(zz.getClassificationType());
                            data.setCorporateFlag(zz.getCorporateFlagBoolean());
                            data.setCostCenter(zz.getCostCenter());
                            data.setIsAll(zz.getAllCriteriaBoolean());
                            data.setPremiseCity(zz.getCITY());
                            data.setPremiseCountry(zz.getCOUNTRY());
                            data.setPremiseDistrict(zz.getDISTRICT());
                            data.setPremiseProvince(zz.getPROVINCE());
                            data.setPremiseSubdistrict(zz.getSUBDISTRICT());
                            data.setSaType(zz.getSaType());
                            data.setSor(zz.getSor());
                            data.setWapuFlag(zz.getWapuFlagBoolean());

                            addedCriteriaPPNPPH.add(data);
                        }
                    }

                    // CHECK CRITERIA
                    Integer x = CheckCriteria.checkCondition(dataAccount, addedCriteriaPPNPPH);

                    logger.info(x.toString());

                    Optional<M_AM_TAXIMPLICATION_CRITERIA_DATA> dataIdTaxImpli = taxCriteriaRepo.findById(x);
                    Optional<M_AM_TAXIMPLICATION> dataImpli = taxImpliRepo.findById(dataIdTaxImpli.get().getTaximplicationId());
                    Optional<M_AM_TAXIMPLICATION_RULE> dataImpliRule = taxRuleRepo.findByTaximplicationIdAndStatus(dataImpli.get().getId(), FlowStatus.ACTIVE.name());

                    LinkedHashMap<String, Object> ppnPphdata = new LinkedHashMap<>();
                    ppnPphdata.put("createdBy", dataImpli.get().getCreatedBy());
                    ppnPphdata.put("updatedBy", dataImpli.get().getUpdatedBy());
                    ppnPphdata.put("status", dataImpli.get().getStatus());
                    ppnPphdata.put("createdDate", dataImpli.get().getCreatedDate());
                    ppnPphdata.put("updatedDate", dataImpli.get().getUpdatedDate());
                    ppnPphdata.put("taxImplicationId", dataImpli.get().getId());
                    Optional<R_GLOBAL_TYPE_VALUE> c = rGlobalTypeValueRepo.findByGlbTypeValId(dataImpli.get().getCategory());
                    ppnPphdata.put("category", c.get().getName());
                    ppnPphdata.put("taxImplicationName", dataImpli.get().getTaxImplicationName());
                    Optional<R_GLOBAL_TYPE_VALUE> st = rGlobalTypeValueRepo.findByGlbTypeValId(dataImpli.get().getServiceType());
                    ppnPphdata.put("serviceType", st.get().getName());
                    Optional<R_GLOBAL_TYPE_VALUE> t = rGlobalTypeValueRepo.findByGlbTypeValId(dataImpliRule.get().getImplicationTypeId());
                    ppnPphdata.put("implicationType", t.get().getName());
                    ppnPphdata.put("gunggung", dataImpliRule.get().getIsGunggung().equalsIgnoreCase("Y") ? "Yes" : "No");
                    ppnPphdata.put("vatInvoiceIssuance", dataImpliRule.get().getIsVatInv().equalsIgnoreCase("Y") ? "Yes" : "No");
                    Optional<M_RBI_FACTURE_CODE> optTransCode = mRbiFactureCodeRepo.findById(dataImpliRule.get().getTransCode());
                    ppnPphdata.put("transactionCode", optTransCode.isPresent()? optTransCode.get().getCode() : null);
                    ppnPphdata.put("description", dataImpli.get().getDescription());
                    if(cat == 127) {
                        allDataCheck.put("taxImplicationPPN", ppnPphdata);
                    } else {
                        allDataCheck.put("taxImplicationPPH", ppnPphdata);
                    }

                }
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        "Success get Tax Implication", allDataCheck), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Account Criteria Data is empty!", null), HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<ResponseObject> getLateCharge(CriteriaDataDTO request) {
        logger.info("Get Late Charge");
        try {
            LatechargeCurrencyDTO allDataCheck = new LatechargeCurrencyDTO();
            // GET DATA ACCOUNT
            Optional<VW_ACCOUNT_CRITERIA> dataAccount = vwAccountCriteriaRepo.findByAccountId(request.getAccountId());
            List<String> currency = new ArrayList<>();

            if(request.getPriceCode() != null) {

                for(Integer idPC : request.getPriceCode()) {
                    List<R_PRICING_DETAIL> dataRPricing = rPricingDetailRepo.findAllByIdPricing(idPC);
                    for(R_PRICING_DETAIL bb : dataRPricing) {
                        if(bb.getCurrency() != null) {
                            currency.add(bb.getCurrency());
                        }
                    }
                }

                HashSet<String> uniqueSet = new HashSet<>(currency);
                currency.clear();
                currency.addAll(uniqueSet);

                for(String cur : currency) {

                    if(cur.equalsIgnoreCase("244")) {
                        List<M_AM_LATECHARGE> dataLatec = latechargeRepo.findAllByCurrency(244);
                        List<Integer> idLatec = new ArrayList<>();
                        for(M_AM_LATECHARGE idLate : dataLatec) {
                            idLatec.add(idLate.getId());
                        }
                        List<VW_LATE_CHARGE_CRITERIA_DATA_REAL> dataCriteria = vwLateChargeCriteriaDataRepo.findIn(idLatec);

                        List<CriteriaData> addedCriteria = new ArrayList<>();
                        for(VW_LATE_CHARGE_CRITERIA_DATA_REAL zz : dataCriteria){
                            CriteriaData data = new CriteriaData();
                            data.setId(zz.getId());
                            data.setAccountCategory(zz.getAccountCategory());
                            data.setAccountGroupType(zz.getAccountGroupType());
                            data.setAccountNumber(zz.getAccountNumber());
                            data.setAccountSegment(zz.getAccountSegment());
                            data.setAccountType(zz.getAccountType());
                            data.setClassificationType(zz.getClassificationType());
                            data.setCorporateFlag(zz.getCorporateFlagBoolean());
                            data.setCostCenter(zz.getCostCenter());
                            data.setIsAll(zz.getAllCriteriaBoolean());
                            data.setPremiseCity(zz.getCITY());
                            data.setPremiseCountry(zz.getCOUNTRY());
                            data.setPremiseDistrict(zz.getDISTRICT());
                            data.setPremiseProvince(zz.getPROVINCE());
                            data.setPremiseSubdistrict(zz.getSUBDISTRICT());
                            data.setSaType(zz.getSaType());
                            data.setSor(zz.getSor());
                            data.setWapuFlag(zz.getWapuFlagBoolean());

                            addedCriteria.add(data);
                        }

                        // CHECK CRITERIA
                        Integer x = CheckCriteria.checkCondition(dataAccount, addedCriteria);

                        Optional<M_AM_LATECHARGE_CRITERIA_DATA> dataIdLateCharge = latechargeCriteriaRepo.findById(x);
                        if(dataIdLateCharge.isPresent()) {
                            Optional<M_AM_LATECHARGE> dataLateCharge = latechargeRepo.findById(dataIdLateCharge.get().getLatechargeId());
                            Optional<M_AM_LATECHARGE_RULE> dataLcRule = latechargeRuleRepo.findTopByLatechargeIdAndStatusOrderByCreatedDateDesc(dataLateCharge.get().getId(), FlowStatus.ACTIVE.name());

                            LinkedHashMap<String, Object> dataLC = new LinkedHashMap<>();
                            dataLC.put("createdDate", dataLateCharge.get().getCreatedDate());
                            dataLC.put("createdBy", dataLateCharge.get().getCreatedBy());
                            dataLC.put("updatedDate", dataLateCharge.get().getUpdatedDate());
                            dataLC.put("updatedBy", dataLateCharge.get().getUpdatedBy());
                            dataLC.put("status", dataLateCharge.get().getStatus());
                            dataLC.put("lateChargeId", dataLateCharge.get().getId());
                            dataLC.put("lateChargeName", dataLateCharge.get().getLateChargeName());
                            Optional<R_GLOBAL_TYPE_VALUE> c = rGlobalTypeValueRepo.findByGlbTypeValId(dataLateCharge.get().getCurrency());
                            dataLC.put("currency", c.get().getName());
                            dataLC.put("maxAmount", dataLcRule.get().getMaxAmount());
                            dataLC.put("formula", lateChargeService.getFormula(dataLcRule.get().getId()));
                            dataLC.put("description", dataLateCharge.get().getDescription());
                            allDataCheck.setLateChargeIDR(dataLC);
                        } else {
                            allDataCheck.setLateChargeIDR(null);
                        }

                    } else {
                        List<M_AM_LATECHARGE> dataLatec = latechargeRepo.findAllByCurrency(243);
                        List<Integer> idLatec = new ArrayList<>();
                        for(M_AM_LATECHARGE idLate : dataLatec) {
                            idLatec.add(idLate.getId());
                        }
                        List<VW_LATE_CHARGE_CRITERIA_DATA_REAL> dataCriteria = vwLateChargeCriteriaDataRepo.findIn(idLatec);

                        List<CriteriaData> addedCriteria = new ArrayList<>();
                        for(VW_LATE_CHARGE_CRITERIA_DATA_REAL zz : dataCriteria){
                            CriteriaData data = new CriteriaData();
                            data.setId(zz.getId());
                            data.setAccountCategory(zz.getAccountCategory());
                            data.setAccountGroupType(zz.getAccountGroupType());
                            data.setAccountNumber(zz.getAccountNumber());
                            data.setAccountSegment(zz.getAccountSegment());
                            data.setAccountType(zz.getAccountType());
                            data.setClassificationType(zz.getClassificationType());
                            data.setCorporateFlag(zz.getCorporateFlagBoolean());
                            data.setCostCenter(zz.getCostCenter());
                            data.setIsAll(zz.getAllCriteriaBoolean());
                            data.setPremiseCity(zz.getCITY());
                            data.setPremiseCountry(zz.getCOUNTRY());
                            data.setPremiseDistrict(zz.getDISTRICT());
                            data.setPremiseProvince(zz.getPROVINCE());
                            data.setPremiseSubdistrict(zz.getSUBDISTRICT());
                            data.setSaType(zz.getSaType());
                            data.setSor(zz.getSor());
                            data.setWapuFlag(zz.getWapuFlagBoolean());

                            addedCriteria.add(data);
                        }

                        // CHECK CRITERIA
                        Integer x = CheckCriteria.checkCondition(dataAccount, addedCriteria);

                        Optional<M_AM_LATECHARGE_CRITERIA_DATA> dataIdLateCharge = latechargeCriteriaRepo.findById(x);
                        if(dataIdLateCharge.isPresent()) {
                            Optional<M_AM_LATECHARGE> dataLateCharge = latechargeRepo.findById(dataIdLateCharge.get().getLatechargeId());
                            Optional<M_AM_LATECHARGE_RULE> dataLcRule = latechargeRuleRepo.findTopByLatechargeIdAndStatusOrderByCreatedDateDesc(dataLateCharge.get().getId(), FlowStatus.ACTIVE.name());

                            LinkedHashMap<String, Object> dataLC = new LinkedHashMap<>();
                            dataLC.put("createdDate", dataLateCharge.get().getCreatedDate());
                            dataLC.put("createdBy", dataLateCharge.get().getCreatedBy());
                            dataLC.put("updatedDate", dataLateCharge.get().getUpdatedDate());
                            dataLC.put("updatedBy", dataLateCharge.get().getUpdatedBy());
                            dataLC.put("status", dataLateCharge.get().getStatus());
                            dataLC.put("lateChargeId", dataLateCharge.get().getId());
                            dataLC.put("lateChargeName", dataLateCharge.get().getLateChargeName());
                            Optional<R_GLOBAL_TYPE_VALUE> c = rGlobalTypeValueRepo.findByGlbTypeValId(dataLateCharge.get().getCurrency());
                            dataLC.put("currency", c.get().getName());
                            dataLC.put("maxAmount", dataLcRule.get().getMaxAmount());
                            dataLC.put("formula", lateChargeService.getFormula(dataLcRule.get().getId()));
                            dataLC.put("description", dataLateCharge.get().getDescription());
                            allDataCheck.setLateChargeUSD(dataLC);
                        }else {
                            allDataCheck.setLateChargeUSD(null);
                        }
                    }

                }
            } else {
                //GET IDR OR USD
                List<M_PRODUCT_PRICING> dataProductPricing = mProductPricingRepo.findAllByProductVersionId(request.getProductVersionId());
                for(M_PRODUCT_PRICING aa : dataProductPricing) {
                    List<VW_PRICING_RULE_DETAIL> dataViewPricing = vwPricingRuleDetailRepo.findAllByPricingRuleId(aa.getPricingRuleId());
                    for(VW_PRICING_RULE_DETAIL bb : dataViewPricing) {
                        if(bb.getCurrency() != null) {
                            currency.add(bb.getCurrency());
                        }
                    }
                }
                HashSet<String> uniqueSet = new HashSet<>(currency);
                currency.clear();
                currency.addAll(uniqueSet);

                for(String cur : currency) {

                    if(cur.equalsIgnoreCase("IDR")) {
                        List<M_AM_LATECHARGE> dataLatec = latechargeRepo.findAllByCurrency(244);
                        List<Integer> idLatec = new ArrayList<>();
                        for(M_AM_LATECHARGE idLate : dataLatec) {
                            idLatec.add(idLate.getId());
                        }
                        List<VW_LATE_CHARGE_CRITERIA_DATA_REAL> dataCriteria = vwLateChargeCriteriaDataRepo.findIn(idLatec);

                        List<CriteriaData> addedCriteria = new ArrayList<>();
                        for(VW_LATE_CHARGE_CRITERIA_DATA_REAL zz : dataCriteria){
                            CriteriaData data = new CriteriaData();
                            data.setId(zz.getId());
                            data.setAccountCategory(zz.getAccountCategory());
                            data.setAccountGroupType(zz.getAccountGroupType());
                            data.setAccountNumber(zz.getAccountNumber());
                            data.setAccountSegment(zz.getAccountSegment());
                            data.setAccountType(zz.getAccountType());
                            data.setClassificationType(zz.getClassificationType());
                            data.setCorporateFlag(zz.getCorporateFlagBoolean());
                            data.setCostCenter(zz.getCostCenter());
                            data.setIsAll(zz.getAllCriteriaBoolean());
                            data.setPremiseCity(zz.getCITY());
                            data.setPremiseCountry(zz.getCOUNTRY());
                            data.setPremiseDistrict(zz.getDISTRICT());
                            data.setPremiseProvince(zz.getPROVINCE());
                            data.setPremiseSubdistrict(zz.getSUBDISTRICT());
                            data.setSaType(zz.getSaType());
                            data.setSor(zz.getSor());
                            data.setWapuFlag(zz.getWapuFlagBoolean());

                            addedCriteria.add(data);
                        }

                        // CHECK CRITERIA
                        Integer x = CheckCriteria.checkCondition(dataAccount, addedCriteria);

                        logger.info(x.toString());

                        Optional<M_AM_LATECHARGE_CRITERIA_DATA> dataIdLateCharge = latechargeCriteriaRepo.findById(x);
                        if(dataIdLateCharge.isPresent()) {
                            Optional<M_AM_LATECHARGE> dataLateCharge = latechargeRepo.findById(dataIdLateCharge.get().getLatechargeId());
                            Optional<M_AM_LATECHARGE_RULE> dataLcRule = latechargeRuleRepo.findTopByLatechargeIdAndStatusOrderByCreatedDateDesc(dataLateCharge.get().getId(), FlowStatus.ACTIVE.name());

                            LinkedHashMap<String, Object> dataLC = new LinkedHashMap<>();
                            dataLC.put("createdDate", dataLateCharge.get().getCreatedDate());
                            dataLC.put("createdBy", dataLateCharge.get().getCreatedBy());
                            dataLC.put("updatedDate", dataLateCharge.get().getUpdatedDate());
                            dataLC.put("updatedBy", dataLateCharge.get().getUpdatedBy());
                            dataLC.put("status", dataLateCharge.get().getStatus());
                            dataLC.put("lateChargeId", dataLateCharge.get().getId());
                            dataLC.put("lateChargeName", dataLateCharge.get().getLateChargeName());
                            Optional<R_GLOBAL_TYPE_VALUE> c = rGlobalTypeValueRepo.findByGlbTypeValId(dataLateCharge.get().getCurrency());
                            dataLC.put("currency", c.get().getName());
                            dataLC.put("maxAmount", dataLcRule.get().getMaxAmount());
                            dataLC.put("formula", lateChargeService.getFormula(dataLcRule.get().getId()));
                            dataLC.put("description", dataLateCharge.get().getDescription());
                            allDataCheck.setLateChargeIDR(dataLC);
                        } else {
                            allDataCheck.setLateChargeIDR(null);
                        }
                    } else {
                        List<M_AM_LATECHARGE> dataLatec = latechargeRepo.findAllByCurrency(243);
                        List<Integer> idLatec = new ArrayList<>();
                        for(M_AM_LATECHARGE idLate : dataLatec) {
                            idLatec.add(idLate.getId());
                        }
                        List<VW_LATE_CHARGE_CRITERIA_DATA_REAL> dataCriteria = vwLateChargeCriteriaDataRepo.findIn(idLatec);

                        List<CriteriaData> addedCriteria = new ArrayList<>();
                        for(VW_LATE_CHARGE_CRITERIA_DATA_REAL zz : dataCriteria){
                            CriteriaData data = new CriteriaData();
                            data.setId(zz.getId());
                            data.setAccountCategory(zz.getAccountCategory());
                            data.setAccountGroupType(zz.getAccountGroupType());
                            data.setAccountNumber(zz.getAccountNumber());
                            data.setAccountSegment(zz.getAccountSegment());
                            data.setAccountType(zz.getAccountType());
                            data.setClassificationType(zz.getClassificationType());
                            data.setCorporateFlag(zz.getCorporateFlagBoolean());
                            data.setCostCenter(zz.getCostCenter());
                            data.setIsAll(zz.getAllCriteriaBoolean());
                            data.setPremiseCity(zz.getCITY());
                            data.setPremiseCountry(zz.getCOUNTRY());
                            data.setPremiseDistrict(zz.getDISTRICT());
                            data.setPremiseProvince(zz.getPROVINCE());
                            data.setPremiseSubdistrict(zz.getSUBDISTRICT());
                            data.setSaType(zz.getSaType());
                            data.setSor(zz.getSor());
                            data.setWapuFlag(zz.getWapuFlagBoolean());

                            addedCriteria.add(data);
                        }

                        // CHECK CRITERIA
                        Integer x = CheckCriteria.checkCondition(dataAccount, addedCriteria);

                        Optional<M_AM_LATECHARGE_CRITERIA_DATA> dataIdLateCharge = latechargeCriteriaRepo.findById(x);
                        if(dataIdLateCharge.isPresent()) {
                            Optional<M_AM_LATECHARGE> dataLateCharge = latechargeRepo.findById(dataIdLateCharge.get().getLatechargeId());
                            Optional<M_AM_LATECHARGE_RULE> dataLcRule = latechargeRuleRepo.findTopByLatechargeIdAndStatusOrderByCreatedDateDesc(dataLateCharge.get().getId(), FlowStatus.ACTIVE.name());

                            LinkedHashMap<String, Object> dataLC = new LinkedHashMap<>();
                            dataLC.put("createdDate", dataLateCharge.get().getCreatedDate());
                            dataLC.put("createdBy", dataLateCharge.get().getCreatedBy());
                            dataLC.put("updatedDate", dataLateCharge.get().getUpdatedDate());
                            dataLC.put("updatedBy", dataLateCharge.get().getUpdatedBy());
                            dataLC.put("status", dataLateCharge.get().getStatus());
                            dataLC.put("lateChargeId", dataLateCharge.get().getId());
                            dataLC.put("lateChargeName", dataLateCharge.get().getLateChargeName());
                            Optional<R_GLOBAL_TYPE_VALUE> c = rGlobalTypeValueRepo.findByGlbTypeValId(dataLateCharge.get().getCurrency());
                            dataLC.put("currency", c.get().getName());
                            dataLC.put("maxAmount", dataLcRule.get().getMaxAmount());
                            dataLC.put("formula", lateChargeService.getFormula(dataLcRule.get().getId()));
                            dataLC.put("description", dataLateCharge.get().getDescription());
                            allDataCheck.setLateChargeUSD(dataLC);
                        }else {
                            allDataCheck.setLateChargeUSD(null);
                        }
                    }
                }
            }
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get Late Charge", allDataCheck), HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    LinkedHashMap<String, Object> lateChargeForDetail(CriteriaDataDTO request) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        logger.info("Get Late Charge");

        LinkedHashMap<String, Object> allDataCheck = new LinkedHashMap<>();
        allDataCheck.put("lateChargeUSD", null);
        allDataCheck.put("lateChargeIDR", null);
        // GET DATA ACCOUNT
        Optional<VW_ACCOUNT_CRITERIA> dataAccount = vwAccountCriteriaRepo.findByAccountId(request.getAccountId());
        List<String> currency = new ArrayList<>();

        if(request.getPriceCode() != null) {

            for(Integer idPC : request.getPriceCode()) {
                List<R_PRICING_DETAIL> dataRPricing = rPricingDetailRepo.findAllByIdPricing(idPC);
                for(R_PRICING_DETAIL bb : dataRPricing) {
                    if(bb.getCurrency() != null) {
                        currency.add(bb.getCurrency());
                    }
                }
            }

            HashSet<String> uniqueSet = new HashSet<>(currency);
            currency.clear();
            currency.addAll(uniqueSet);

            for(String cur : currency) {

                if(cur.equalsIgnoreCase("244")) {
                    List<M_AM_LATECHARGE> dataLatec = latechargeRepo.findAllByCurrency(244);
                    List<Integer> idLatec = new ArrayList<>();
                    for(M_AM_LATECHARGE idLate : dataLatec) {
                        idLatec.add(idLate.getId());
                    }
                    List<VW_LATE_CHARGE_CRITERIA_DATA_REAL> dataCriteria = vwLateChargeCriteriaDataRepo.findIn(idLatec);

                    List<CriteriaData> addedCriteria = new ArrayList<>();
                    for(VW_LATE_CHARGE_CRITERIA_DATA_REAL zz : dataCriteria){
                        CriteriaData data = new CriteriaData();
                        data.setId(zz.getId());
                        data.setAccountCategory(zz.getAccountCategory());
                        data.setAccountGroupType(zz.getAccountGroupType());
                        data.setAccountNumber(zz.getAccountNumber());
                        data.setAccountSegment(zz.getAccountSegment());
                        data.setAccountType(zz.getAccountType());
                        data.setClassificationType(zz.getClassificationType());
                        data.setCorporateFlag(zz.getCorporateFlagBoolean());
                        data.setCostCenter(zz.getCostCenter());
                        data.setIsAll(zz.getAllCriteriaBoolean());
                        data.setPremiseCity(zz.getCITY());
                        data.setPremiseCountry(zz.getCOUNTRY());
                        data.setPremiseDistrict(zz.getDISTRICT());
                        data.setPremiseProvince(zz.getPROVINCE());
                        data.setPremiseSubdistrict(zz.getSUBDISTRICT());
                        data.setSaType(zz.getSaType());
                        data.setSor(zz.getSor());
                        data.setWapuFlag(zz.getWapuFlagBoolean());

                        addedCriteria.add(data);
                    }

                    // CHECK CRITERIA
                    Integer x = CheckCriteria.checkCondition(dataAccount, addedCriteria);

                    Optional<M_AM_LATECHARGE_CRITERIA_DATA> dataIdLateCharge = latechargeCriteriaRepo.findById(x);
                    if(dataIdLateCharge.isPresent()) {
                        Optional<M_AM_LATECHARGE> dataLateCharge = latechargeRepo.findById(dataIdLateCharge.get().getLatechargeId());
                        Optional<M_AM_LATECHARGE_RULE> dataLcRule = latechargeRuleRepo.findTopByLatechargeIdAndStatusOrderByCreatedDateDesc(dataLateCharge.get().getId(), FlowStatus.ACTIVE.name());

                        LinkedHashMap<String, Object> dataLC = new LinkedHashMap<>();
                        dataLC.put("createdDate", dataLateCharge.get().getCreatedDate());
                        dataLC.put("createdBy", dataLateCharge.get().getCreatedBy());
                        dataLC.put("updatedDate", dataLateCharge.get().getUpdatedDate());
                        dataLC.put("updatedBy", dataLateCharge.get().getUpdatedBy());
                        dataLC.put("status", dataLateCharge.get().getStatus());
                        dataLC.put("lateChargeId", dataLateCharge.get().getId());
                        dataLC.put("lateChargeName", dataLateCharge.get().getLateChargeName());
                        Optional<R_GLOBAL_TYPE_VALUE> c = rGlobalTypeValueRepo.findByGlbTypeValId(dataLateCharge.get().getCurrency());
                        dataLC.put("currency", c.get().getName());
                        dataLC.put("maxAmount", dataLcRule.get().getMaxAmount());
                        dataLC.put("formula", lateChargeService.getFormula(dataLcRule.get().getId()));
                        dataLC.put("description", dataLateCharge.get().getDescription());
                        allDataCheck.put("lateChargeIDR", dataLC);
                    } else {
                        allDataCheck.put("lateChargeIDR", null);
                    }

                } else {
                    List<M_AM_LATECHARGE> dataLatec = latechargeRepo.findAllByCurrency(243);
                    List<Integer> idLatec = new ArrayList<>();
                    for(M_AM_LATECHARGE idLate : dataLatec) {
                        idLatec.add(idLate.getId());
                    }
                    List<VW_LATE_CHARGE_CRITERIA_DATA_REAL> dataCriteria = vwLateChargeCriteriaDataRepo.findIn(idLatec);

                    List<CriteriaData> addedCriteria = new ArrayList<>();
                    for(VW_LATE_CHARGE_CRITERIA_DATA_REAL zz : dataCriteria){
                        CriteriaData data = new CriteriaData();
                        data.setId(zz.getId());
                        data.setAccountCategory(zz.getAccountCategory());
                        data.setAccountGroupType(zz.getAccountGroupType());
                        data.setAccountNumber(zz.getAccountNumber());
                        data.setAccountSegment(zz.getAccountSegment());
                        data.setAccountType(zz.getAccountType());
                        data.setClassificationType(zz.getClassificationType());
                        data.setCorporateFlag(zz.getCorporateFlagBoolean());
                        data.setCostCenter(zz.getCostCenter());
                        data.setIsAll(zz.getAllCriteriaBoolean());
                        data.setPremiseCity(zz.getCITY());
                        data.setPremiseCountry(zz.getCOUNTRY());
                        data.setPremiseDistrict(zz.getDISTRICT());
                        data.setPremiseProvince(zz.getPROVINCE());
                        data.setPremiseSubdistrict(zz.getSUBDISTRICT());
                        data.setSaType(zz.getSaType());
                        data.setSor(zz.getSor());
                        data.setWapuFlag(zz.getWapuFlagBoolean());

                        addedCriteria.add(data);
                    }

                    // CHECK CRITERIA
                    Integer x = CheckCriteria.checkCondition(dataAccount, addedCriteria);

                    Optional<M_AM_LATECHARGE_CRITERIA_DATA> dataIdLateCharge = latechargeCriteriaRepo.findById(x);

                    if(dataIdLateCharge.isPresent()) {
                        Optional<M_AM_LATECHARGE> dataLateCharge = latechargeRepo.findById(dataIdLateCharge.get().getLatechargeId());
                        Optional<M_AM_LATECHARGE_RULE> dataLcRule = latechargeRuleRepo.findTopByLatechargeIdAndStatusOrderByCreatedDateDesc(dataLateCharge.get().getId(), FlowStatus.ACTIVE.name());

                        LinkedHashMap<String, Object> dataLC = new LinkedHashMap<>();
                        dataLC.put("createdDate", dataLateCharge.get().getCreatedDate());
                        dataLC.put("createdBy", dataLateCharge.get().getCreatedBy());
                        dataLC.put("updatedDate", dataLateCharge.get().getUpdatedDate());
                        dataLC.put("updatedBy", dataLateCharge.get().getUpdatedBy());
                        dataLC.put("status", dataLateCharge.get().getStatus());
                        dataLC.put("lateChargeId", dataLateCharge.get().getId());
                        dataLC.put("lateChargeName", dataLateCharge.get().getLateChargeName());
                        Optional<R_GLOBAL_TYPE_VALUE> c = rGlobalTypeValueRepo.findByGlbTypeValId(dataLateCharge.get().getCurrency());
                        dataLC.put("currency", c.get().getName());
                        dataLC.put("maxAmount", dataLcRule.get().getMaxAmount());
                        dataLC.put("formula", lateChargeService.getFormula(dataLcRule.get().getId()));
                        dataLC.put("description", dataLateCharge.get().getDescription());
                        allDataCheck.put("lateChargeUSD", dataLC);
                    } else {
                        allDataCheck.put("lateChargeUSD", null);
                    }
                }

            }
        } else {
            //GET IDR OR USD
            List<M_PRODUCT_PRICING> dataProductPricing = mProductPricingRepo.findAllByProductVersionId(request.getProductVersionId());
            for(M_PRODUCT_PRICING aa : dataProductPricing) {
                List<VW_PRICING_RULE_DETAIL> dataViewPricing = vwPricingRuleDetailRepo.findAllByPricingRuleId(aa.getPricingRuleId());
                for(VW_PRICING_RULE_DETAIL bb : dataViewPricing) {
                    if(bb.getCurrency() != null) {
                        currency.add(bb.getCurrency());
                    }
                }
            }
            HashSet<String> uniqueSet = new HashSet<>(currency);
            currency.clear();
            currency.addAll(uniqueSet);

            for(String cur : currency) {

                if(cur.equalsIgnoreCase("IDR")) {
                    List<M_AM_LATECHARGE> dataLatec = latechargeRepo.findAllByCurrency(244);
                    List<Integer> idLatec = new ArrayList<>();
                    for(M_AM_LATECHARGE idLate : dataLatec) {
                        idLatec.add(idLate.getId());
                    }
                    List<VW_LATE_CHARGE_CRITERIA_DATA_REAL> dataCriteria = vwLateChargeCriteriaDataRepo.findIn(idLatec);

                    List<CriteriaData> addedCriteria = new ArrayList<>();
                    for(VW_LATE_CHARGE_CRITERIA_DATA_REAL zz : dataCriteria){
                        CriteriaData data = new CriteriaData();
                        data.setId(zz.getId());
                        data.setAccountCategory(zz.getAccountCategory());
                        data.setAccountGroupType(zz.getAccountGroupType());
                        data.setAccountNumber(zz.getAccountNumber());
                        data.setAccountSegment(zz.getAccountSegment());
                        data.setAccountType(zz.getAccountType());
                        data.setClassificationType(zz.getClassificationType());
                        data.setCorporateFlag(zz.getCorporateFlagBoolean());
                        data.setCostCenter(zz.getCostCenter());
                        data.setIsAll(zz.getAllCriteriaBoolean());
                        data.setPremiseCity(zz.getCITY());
                        data.setPremiseCountry(zz.getCOUNTRY());
                        data.setPremiseDistrict(zz.getDISTRICT());
                        data.setPremiseProvince(zz.getPROVINCE());
                        data.setPremiseSubdistrict(zz.getSUBDISTRICT());
                        data.setSaType(zz.getSaType());
                        data.setSor(zz.getSor());
                        data.setWapuFlag(zz.getWapuFlagBoolean());

                        addedCriteria.add(data);
                    }

                    // CHECK CRITERIA
                    Integer x = CheckCriteria.checkCondition(dataAccount, addedCriteria);

                    logger.info(x.toString());

                    Optional<M_AM_LATECHARGE_CRITERIA_DATA> dataIdLateCharge = latechargeCriteriaRepo.findById(x);
                    if(dataIdLateCharge.isPresent()) {
                        Optional<M_AM_LATECHARGE> dataLateCharge = latechargeRepo.findById(dataIdLateCharge.get().getLatechargeId());
                        Optional<M_AM_LATECHARGE_RULE> dataLcRule = latechargeRuleRepo.findTopByLatechargeIdAndStatusOrderByCreatedDateDesc(dataLateCharge.get().getId(), FlowStatus.ACTIVE.name());


                        LinkedHashMap<String, Object> dataLC = new LinkedHashMap<>();
                        dataLC.put("createdDate", dataLateCharge.get().getCreatedDate());
                        dataLC.put("createdBy", dataLateCharge.get().getCreatedBy());
                        dataLC.put("updatedDate", dataLateCharge.get().getUpdatedDate());
                        dataLC.put("updatedBy", dataLateCharge.get().getUpdatedBy());
                        dataLC.put("status", dataLateCharge.get().getStatus());
                        dataLC.put("lateChargeId", dataLateCharge.get().getId());
                        dataLC.put("lateChargeName", dataLateCharge.get().getLateChargeName());
                        Optional<R_GLOBAL_TYPE_VALUE> c = rGlobalTypeValueRepo.findByGlbTypeValId(dataLateCharge.get().getCurrency());
                        dataLC.put("currency", c.get().getName());
                        dataLC.put("maxAmount", dataLcRule.get().getMaxAmount());
                        dataLC.put("formula", lateChargeService.getFormula(dataLcRule.get().getId()));
                        dataLC.put("description", dataLateCharge.get().getDescription());
                        allDataCheck.put("lateChargeIDR", dataLC);
                    }else {
                        allDataCheck.put("lateChargeIDR", null);
                    }

                } else {
                    List<M_AM_LATECHARGE> dataLatec = latechargeRepo.findAllByCurrency(243);
                    List<Integer> idLatec = new ArrayList<>();
                    for(M_AM_LATECHARGE idLate : dataLatec) {
                        idLatec.add(idLate.getId());
                    }
                    List<VW_LATE_CHARGE_CRITERIA_DATA_REAL> dataCriteria = vwLateChargeCriteriaDataRepo.findIn(idLatec);

                    List<CriteriaData> addedCriteria = new ArrayList<>();
                    for(VW_LATE_CHARGE_CRITERIA_DATA_REAL zz : dataCriteria){
                        CriteriaData data = new CriteriaData();
                        data.setId(zz.getId());
                        data.setAccountCategory(zz.getAccountCategory());
                        data.setAccountGroupType(zz.getAccountGroupType());
                        data.setAccountNumber(zz.getAccountNumber());
                        data.setAccountSegment(zz.getAccountSegment());
                        data.setAccountType(zz.getAccountType());
                        data.setClassificationType(zz.getClassificationType());
                        data.setCorporateFlag(zz.getCorporateFlagBoolean());
                        data.setCostCenter(zz.getCostCenter());
                        data.setIsAll(zz.getAllCriteriaBoolean());
                        data.setPremiseCity(zz.getCITY());
                        data.setPremiseCountry(zz.getCOUNTRY());
                        data.setPremiseDistrict(zz.getDISTRICT());
                        data.setPremiseProvince(zz.getPROVINCE());
                        data.setPremiseSubdistrict(zz.getSUBDISTRICT());
                        data.setSaType(zz.getSaType());
                        data.setSor(zz.getSor());
                        data.setWapuFlag(zz.getWapuFlagBoolean());
                        addedCriteria.add(data);
                    }

                    // CHECK CRITERIA
                    Integer x = CheckCriteria.checkCondition(dataAccount, addedCriteria);

                    Optional<M_AM_LATECHARGE_CRITERIA_DATA> dataIdLateCharge = latechargeCriteriaRepo.findById(x);
                    if(dataIdLateCharge.isPresent()) {
                        Optional<M_AM_LATECHARGE> dataLateCharge = latechargeRepo.findById(dataIdLateCharge.get().getLatechargeId());
                        Optional<M_AM_LATECHARGE_RULE> dataLcRule = latechargeRuleRepo.findTopByLatechargeIdAndStatusOrderByCreatedDateDesc(dataLateCharge.get().getId(), FlowStatus.ACTIVE.name());

                        LinkedHashMap<String, Object> dataLC = new LinkedHashMap<>();
                        dataLC.put("createdDate", dataLateCharge.get().getCreatedDate());
                        dataLC.put("createdBy", dataLateCharge.get().getCreatedBy());
                        dataLC.put("updatedDate", dataLateCharge.get().getUpdatedDate());
                        dataLC.put("updatedBy", dataLateCharge.get().getUpdatedBy());
                        dataLC.put("status", dataLateCharge.get().getStatus());
                        dataLC.put("lateChargeId", dataLateCharge.get().getId());
                        dataLC.put("lateChargeName", dataLateCharge.get().getLateChargeName());
                        Optional<R_GLOBAL_TYPE_VALUE> c = rGlobalTypeValueRepo.findByGlbTypeValId(dataLateCharge.get().getCurrency());
                        dataLC.put("currency", c.get().getName());
                        dataLC.put("maxAmount", dataLcRule.get().getMaxAmount());
                        dataLC.put("formula", lateChargeService.getFormula(dataLcRule.get().getId()));
                        dataLC.put("description", dataLateCharge.get().getDescription());
                        allDataCheck.put("lateChargeUSD", dataLC);
                    } else {
                        allDataCheck.put("lateChargeUSD", null);
                    }
                }
            }
        }
        return allDataCheck;
    }

    public ResponseEntity<ResponseObject> getPricingCodeList(Integer accountId) {
        logger.info("get List Pricing "+accountId);
        ResponseObject result = new ResponseObject();
        try {
            List<M_PRICING> mPricings = mPricingRepo.findAll();
            List<PriceCodeDTO> dtoPricingCode = new ArrayList<>();
            for (M_PRICING data : mPricings) {
                PriceCodeDTO newDto = new PriceCodeDTO();
                List<PriceCodeDetailDTO> listPriceCodeDetailDTO = new ArrayList<>();
                newDto.setCreatedBy(data.getCreatedBy());
                newDto.setCreatedDate(data.getCreatedDate());
                newDto.setEntityId(data.getEntityId());
                newDto.setId(data.getId());
                List<R_PRICING_DETAIL> mPricingDetails = rPricingDetailRepo.findAllByIdPricing(data.getId());
                for (R_PRICING_DETAIL dataDetail: mPricingDetails) {
                    PriceCodeDetailDTO newDetailDto = new PriceCodeDetailDTO();
                    newDetailDto.setCurrencyId(dataDetail.getCurrency());
                    newDetailDto.setIdPricing(dataDetail.getIdPricing());
                    newDetailDto.setCurrencyId(dataDetail.getCurrency());
                    newDetailDto.setId(dataDetail.getId());
                    Optional<R_GLOBAL_TYPE_VALUE> rGlobalTypeValue = rGlobalTypeValueRepo.findByGlbTypeValId(Integer.valueOf(dataDetail.getCurrency()));
                    rGlobalTypeValue.ifPresent(r -> newDetailDto.setCurrency(r.getName()));
                    newDetailDto.setDescription(dataDetail.getDescription());
                    newDetailDto.setValue(dataDetail.getValue());
                    newDetailDto.setUom(dataDetail.getUom());
                    if(dataDetail.getUom() != null) {
                        Optional<R_GLOBAL_TYPE_VALUE> rGlobalTypeValueUom = rGlobalTypeValueRepo.findByGlbTypeValId(Integer.valueOf(dataDetail.getUom()));
                        rGlobalTypeValueUom.ifPresent(r -> newDetailDto.setUomName(r.getName()));
                    }
                    listPriceCodeDetailDTO.add(newDetailDto);
                }
                newDto.setMPricingDetail(listPriceCodeDetailDTO);
                newDto.setPriceCode(data.getPriceCode());
                newDto.setPriceDescription(data.getPriceDescription());
                newDto.setStatus(data.getStatus());
                newDto.setUpdateBy(data.getUpdatedBy());
                newDto.setUpdateDate(data.getUpdatedDate());
                dtoPricingCode.add(newDto);
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, CommonVariables.SUCCESS, dtoPricingCode);

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
    public ResponseEntity<ResponseObject> getListPriceCode(HttpServletRequest httpServletRequest, Integer accountId) {
        logger.info("get List Pricing");
        ResponseObject result = new ResponseObject();
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
            logger.info("all data check "+allDataCheck);

            Optional<List<M_PRICING>> mPricing = Optional
                    .of(mPricingRepo.findAllByEntityId(UserDetailUtils.getEntityFromToken(httpServletRequest)));

            Optional<List<Integer>> dtl = rPricingDetailRepo.findByEndDate();

            List<M_PRICING> mPricings = mPricing.get().stream().filter(e -> dtl.get().contains(e.getId()) && Objects.nonNull(e.getStatus()) && e.getStatus().equals(CommonVariables.ACTIVE))
                    .collect(Collectors.toList());
            List<Integer> idCritFiltered = new ArrayList<>();
            boolean valid = false;
            if (mPricing.get().isEmpty()) {

                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, CommonVariables.FAILED,
                        ResponseUtils.DATA_EMPTY);
                logger.info("Response Failed");
                return new ResponseEntity<>(result, HttpStatus.OK);
            }

            List<Map<String, Integer>> tasCritses = (List<Map<String, Integer>>) allDataCheck.get("tasCrit");
            if (!tasCritses.isEmpty()) {
                for (M_PRICING mPrice : mPricings) {

                    List<R_PRICING_CRITERIA_DATA> critDatas = rPricingCriteriaDataRepo.findAllByIdPricing(mPrice.getId());
                    if (!critDatas.isEmpty()) {
                        for (R_PRICING_CRITERIA_DATA critData : critDatas) {
                            if (critData.getAllCriteria() != null && Boolean.TRUE.equals(critData.getAllCriteria())) {
                                idCritFiltered.add(critData.getIdPricing());
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
                                    idCritFiltered.add(critData.getIdPricing());
                                    break;
                                }

                            }

                            if (valid) {
                                break;
                            }

                        }
                    }
                }

                if (!idCritFiltered.isEmpty()) {
                    mPricings = mPricings.stream().filter(e -> idCritFiltered.contains(e.getId()))
                            .collect(Collectors.toList());
                } else {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
                            ResponseUtils.MESSAGE_NOT_FOUND, ResponseUtils.DATA_EMPTY);

                    return new ResponseEntity<>(result, HttpStatus.OK);
                }

            } else {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, ResponseUtils.MESSAGE_NOT_FOUND,
                        ResponseUtils.DATA_EMPTY);

                return new ResponseEntity<>(result, HttpStatus.OK);
            }

            List<PriceCodeDTO> dtoPricingCode = new ArrayList<>();

            for (M_PRICING data : mPricings) {
                PriceCodeDTO newDto = new PriceCodeDTO();
                List<PriceCodeDetailDTO> listPriceCodeDetailDTO = new ArrayList<>();
                newDto.setCreatedBy(data.getCreatedBy());
                newDto.setCreatedDate(data.getCreatedDate());
                newDto.setEntityId(data.getEntityId());
                newDto.setId(data.getId());
                List<R_PRICING_DETAIL> mPricingDetails = rPricingDetailRepo.findAllByIdPricing(data.getId());
                for (R_PRICING_DETAIL dataDetail: mPricingDetails) {
                    PriceCodeDetailDTO newDetailDto = new PriceCodeDetailDTO();
                    newDetailDto.setCurrencyId(dataDetail.getCurrency());
                    newDetailDto.setIdPricing(dataDetail.getIdPricing());
                    newDetailDto.setCurrencyId(dataDetail.getCurrency());
                    newDetailDto.setId(dataDetail.getId());
                    Optional<R_GLOBAL_TYPE_VALUE> rGlobalTypeValue = rGlobalTypeValueRepo.findByGlbTypeValId(Integer.valueOf(dataDetail.getCurrency()));
                    rGlobalTypeValue.ifPresent(r -> newDetailDto.setCurrency(r.getName()));
                    newDetailDto.setDescription(dataDetail.getDescription());
                    newDetailDto.setValue(dataDetail.getValue());
                    newDetailDto.setUom(dataDetail.getUom());
                    if(dataDetail.getUom() != null) {
                        Optional<R_GLOBAL_TYPE_VALUE> rGlobalTypeValueUom = rGlobalTypeValueRepo.findByGlbTypeValId(Integer.valueOf(dataDetail.getUom()));
                        rGlobalTypeValueUom.ifPresent(r -> newDetailDto.setUomName(r.getName()));
                    }
                    listPriceCodeDetailDTO.add(newDetailDto);
                }
                newDto.setMPricingDetail(listPriceCodeDetailDTO);
                newDto.setPriceCode(data.getPriceCode());
                newDto.setPriceDescription(data.getPriceDescription());
                newDto.setStatus(data.getStatus());
                newDto.setUpdateBy(data.getUpdatedBy());
                newDto.setUpdateDate(data.getUpdatedDate());
                dtoPricingCode.add(newDto);
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, CommonVariables.SUCCESS, dtoPricingCode);
//
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(CommonVariables.ERROR_IN, e.getMessage(), e);
            throw e;
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> getPricingRule(HttpServletRequest httpServletRequest, Integer accountId) {
        logger.info("get List Pricing Rule");
        ResponseObject result = new ResponseObject();
        try {

//            Optional<VW_ACCOUNT_CRITERIA> dataAccount = vwAccountCriteriaRepo.findByAccountId(accountId);
//            LinkedHashMap<String, Object> allDataCheck = new LinkedHashMap<>();
//            allDataCheck.put("tasCrit", null);
//            List<Map<String, Integer>> tasCrits = new ArrayList<>();
//            if(dataAccount.isPresent()) {
//                Map<String, Integer> newTasCriteria = new HashMap<>();
//                newTasCriteria.put("customer", dataAccount.get().getCustomerId());
//                newTasCriteria.put("budget", dataAccount.get().getAccountBudget());
//                newTasCriteria.put("subDistrict", dataAccount.get().getPremiseSubdistrict());
//                newTasCriteria.put("district", dataAccount.get().getPremiseDistrict());
//                newTasCriteria.put("city", dataAccount.get().getPremiseCity());
//                newTasCriteria.put("province", dataAccount.get().getPremiseProvince());
//                newTasCriteria.put("area", dataAccount.get().getCostCenter());
//                newTasCriteria.put("sor", dataAccount.get().getSor());
//                newTasCriteria.put("industrialSector", dataAccount.get().getAccountIndustrialSector());
//                newTasCriteria.put("product", dataAccount.get().getSaProductVersion());
//                newTasCriteria.put("gsizes", dataAccount.get().getPremiseSpAssetGSize());
//                newTasCriteria.put("customerSegment", dataAccount.get().getAccountSegment());
//                newTasCriteria.put("accountGroup", dataAccount.get().getAccountGroupType());
//                newTasCriteria.put("serviceType", dataAccount.get().getSaServiceType());
//                newTasCriteria.put("accountCategory", dataAccount.get().getAccountCategory());
//                newTasCriteria.put("allCriteria", null);
//                tasCrits.add(newTasCriteria);
//            }
//            allDataCheck.put("tasCrit", tasCrits);
//
//            List<M_PRICING_RULE> mPricingRule = mPricingRuleRepo
//                    .findAllByEntityIdAndIsDeleted(UserDetailUtils.getEntityFromToken(httpServletRequest), "N");
//
//            Optional<List<Integer>> dtl = mPricingRuleRepo.findByEndDate();
//
//            List<M_PRICING_RULE> mPricingRules = mPricingRule.stream()
//                    .filter(e -> dtl.get().contains(e.getPricingRuleId())).collect(Collectors.toList());
//            List<Integer> idCritFiltered = new ArrayList<>();
//            boolean valid = false;
//
//            if (mPricingRules.isEmpty()) {
//
//                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, ResponseUtils.MESSAGE_NOT_FOUND,
//                        ResponseUtils.DATA_EMPTY);
//
//                return new ResponseEntity<>(result, HttpStatus.OK);
//            }
//
//            // criteria filter
//            List<Map<String, Integer>> tasCritses = (List<Map<String, Integer>>) allDataCheck.get("tasCrit");
//            if (!tasCritses.isEmpty()) {
//                for (M_PRICING_RULE pricingRuleId : mPricingRules) {
//
//                    List<R_PRICING_RULE_CRITERIA_DATA> critDatas = rPricingRuleCriteriaDataRepo.findAllByIdPricingRule(pricingRuleId.getPricingRuleId());
//                    if (!critDatas.isEmpty()) {
//                        for (R_PRICING_RULE_CRITERIA_DATA critData : critDatas) {
//                            if (critData.getAllCriteria() != null && Boolean.TRUE.equals(critData.getAllCriteria())) {
//                                idCritFiltered.add(critData.getIdPricingRule());
//                                break;
//
//                            }
//                            Map<String, Integer> pricingCrit;
//                            ProductCriteriaFilterDto filterCrit = objectMapper.convertValue(critData,
//                                    ProductCriteriaFilterDto.class);
//                            pricingCrit = objectMapper.convertValue(filterCrit,
//                                    new TypeReference<Map<String, Integer>>() {
//                                    });
//
//                            for (Map<String, Integer> fil : tasCritses) {
//
//                                valid = CheckCriteria.compareCriteria(CheckCriteria.PRODUCT, fil, pricingCrit);
//                                if (valid) {
//                                    idCritFiltered.add(critData.getIdPricingRule());
//                                    break;
//                                }
//
//                            }
//
//                            if (valid) {
//                                break;
//                            }
//
//                        }
//                    }
//                }
//
//                if (!idCritFiltered.isEmpty()) {
//                    mPricingRules = mPricingRules.stream().filter(e -> idCritFiltered.contains(e.getPricingRuleId()))
//                            .collect(Collectors.toList());
//                } else {
//                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
//                            ResponseUtils.MESSAGE_NOT_FOUND, ResponseUtils.DATA_EMPTY);
//
//                    return new ResponseEntity<>(result, HttpStatus.OK);
//                }
//
//            } else {
//                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, ResponseUtils.MESSAGE_NOT_FOUND,
//                        ResponseUtils.DATA_EMPTY);
//
//                return new ResponseEntity<>(result, HttpStatus.OK);
//            }

            List<M_PRICING_RULE> dataPricingRule = this.getPricingRuleByCriteria(httpServletRequest, accountId);
            if(StringUtils.hasValue(dataPricingRule)) {
                PricingRuleViewDto vo;
                List<PricingRuleViewDto> vos = new ArrayList<>();

                for (M_PRICING_RULE mPricingRule_ : dataPricingRule) {
                    vo = objectMapper.convertValue(mPricingRule_, PricingRuleViewDto.class);
                    vos.add(vo);
                }

                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_SUCCESS, vos);

                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, ResponseUtils.MESSAGE_NOT_FOUND, ResponseUtils.DATA_EMPTY);

                return new ResponseEntity<>(result, HttpStatus.OK);
            }

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getPricingRuleById(Integer pricingRuleId) {
        logger.info("get List Pricing");
        ResponseObject result = new ResponseObject();
        try {
            List<PricingRuleDetailDTO> newData = new ArrayList<>();
            List<VW_PRICING_RULE_DETAIL> mPricingDetail = vwPricingRuleDetailRepo.findAllByPricingRuleId(pricingRuleId);
            for (VW_PRICING_RULE_DETAIL data : mPricingDetail) {
                PricingRuleDetailDTO vo = new PricingRuleDetailDTO();
                vo.setCreatedDate(data.getCreatedDate());
                vo.setCreatedBy(data.getCreatedBy());
                vo.setCurrency(data.getCurrency());
                vo.setDescription(data.getDescription());
                vo.setIsDeleted(data.getIsDeleted());
                vo.setLineNumber(data.getLinenumber());
                if(isStringAllDigits(data.getMax())) {
                    vo.setMax(Integer.valueOf(data.getMax()));
                }else {
                    vo.setMax(0);
                }
                vo.setMin(data.getMin());
                vo.setPriceCode(data.getPriceCode());
                vo.setPriceCodeId(data.getPriceCodeId());
                vo.setPricingRuleId(data.getPricingRuleId());
                vo.setPricingRuleDetailId(data.getPricingRuleDetailId());
                vo.setUom(data.getUom());
                vo.setIsUnlim(data.getIsUnlim());
                vo.setUpdatedDate(data.getUpdatedDate());
                vo.setUpdatedBy(data.getUpdatedBy());
                vo.setValue(data.getValue());
                newData.add(vo);
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, CommonVariables.SUCCESS, newData);

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
    public ResponseEntity<ResponseObject> getPricingRuleHeaderList(HttpServletRequest httpServletRequest,
                                                                   Map<String, Object> tasCrits) {
        logger.info("View Pricing Rule Header LIst");
        ResponseObject result = new ResponseObject();

        try {
            List<M_PRICING_RULE> mPricingRule = mPricingRuleRepo
                    .findAllByEntityIdAndIsDeleted(UserDetailUtils.getEntityFromToken(httpServletRequest), "N");

            Optional<List<Integer>> dtl = mPricingRuleRepo.findByEndDate();

            List<M_PRICING_RULE> mPricingRules = mPricingRule.stream()
                    .filter(e -> dtl.get().contains(e.getPricingRuleId())).collect(Collectors.toList());
            List<Integer> idCritFiltered = new ArrayList<>();
            boolean valid = false;

            if (mPricingRules.isEmpty()) {

                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, ResponseUtils.MESSAGE_NOT_FOUND,
                        ResponseUtils.DATA_EMPTY);

                return new ResponseEntity<>(result, HttpStatus.OK);
            }

            // criteria filter
            List<Map<String, Integer>> tasCritses = (List<Map<String, Integer>>) tasCrits.get("tasCrit");
            if (!tasCritses.isEmpty()) {
                for (M_PRICING_RULE pricingRuleId : mPricingRules) {

                    List<R_PRICING_RULE_CRITERIA_DATA> critDatas = rPricingRuleCriteriaDataRepo.findAllByIdPricingRule(pricingRuleId.getPricingRuleId());
                    if (!critDatas.isEmpty()) {
                        for (R_PRICING_RULE_CRITERIA_DATA critData : critDatas) {
                            if (critData.getAllCriteria() != null && Boolean.TRUE.equals(critData.getAllCriteria())) {
                                idCritFiltered.add(critData.getIdPricingRule());
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
                                    idCritFiltered.add(critData.getIdPricingRule());
                                    break;
                                }

                            }

                            if (valid) {
                                break;
                            }

                        }
                    }
                }

                if (!idCritFiltered.isEmpty()) {
                    mPricingRules = mPricingRules.stream().filter(e -> idCritFiltered.contains(e.getPricingRuleId()))
                            .collect(Collectors.toList());
                } else {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
                            ResponseUtils.MESSAGE_NOT_FOUND, ResponseUtils.DATA_EMPTY);

                    return new ResponseEntity<>(result, HttpStatus.OK);
                }

            }
            PricingRuleViewDto vo;
            List<PricingRuleViewDto> vos = new ArrayList<>();

            for (M_PRICING_RULE mPricingRule_ : mPricingRules) {
                vo = objectMapper.convertValue(mPricingRule_, PricingRuleViewDto.class);

                vos.add(vo);
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_SUCCESS, vos);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<ResponseObject> getPricingRuleList(Integer priceRuleId) {
        logger.info("get List Pricing");
        ResponseObject result = new ResponseObject();
        try {
            List<M_PRICING_RULE_DETAIL> data = null;
            List<M_PRICING_RULE_DETAIL> mPricing = mPricingRuleDetailRepo.findAllByPricingRuleId(priceRuleId);

            if(!mPricing.isEmpty())
                data = mPricing;

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, CommonVariables.SUCCESS, data);

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getTosList(
            MaterialTablePagingRequest pagingData,
            PagedResourcesAssembler<M_TOS> assembler,
            Integer accountId,
            HttpServletRequest httpServletRequest
    ) {
        logger.info("Get List Terms of service with Paging "+accountId);

        ResponseObject result = new ResponseObject();

        try {

            if (ObjectUtils.isEmpty(pagingData.getSize()) || pagingData.getSize() <= 0)
                pagingData.setSize(10);

            Page<M_TOS> saPage;
            Map<String, Object> filter = new HashMap<>();
            filter.put("entityId", UserDetailUtils.getEntityFromToken(httpServletRequest));

            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key+"~"+value);
                }
            }
            if (!pagingData.getSearch().isEmpty())
                saPage = mTosRepo.findAll(mTosRepo.getSpecificationFromFilters(pagingData, filter), PagingUtils.getPaging(pagingData));
            else
                saPage = mTosRepo.findAll(mTosRepo.getSpecificationDefault(filter), PagingUtils.getPaging(pagingData));

            logger.info("List TOS : {}", saPage);

            List<M_TOS> listTos = saPage.getContent();

            if (!listTos.isEmpty()) {
                var pagedData = assembler.toModel(saPage);
                Map<String, Object> dataResult = new HashMap<>();
                dataResult.put(Constant.RESULT, listTos);
                dataResult.put(Constant.PAGE, pagedData.getMetadata());
                dataResult.put(Constant.LINK, pagedData.getLinks());

                result.setSuccess(ResponseUtils.SUCCESS_TRUE);
                result.setCode(HttpStatus.OK);
                result.setMessage("Success Get List Terms of service with Paging");
                result.setData(dataResult);

                return new ResponseEntity<>(result, result.getHttpCode());
            } else {
                result.setSuccess(Boolean.FALSE);
                result.setCode(HttpStatus.BAD_REQUEST);
                result.setMessage("ERROR");
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

    public ResponseEntity<ResponseObject> listCategory() {
        logger.info("ATTACHMENT_CATEGORY_SA");
        ResponseObject result;
        try {
            List<LinkedHashMap<String, Object>> allData = criteriaServices.getCriteriaByGlobalType("ATTACHMENT_CATEGORY_SA");
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_OK, allData);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(CommonVariables.ERROR_IN, e.getMessage(), e);
            throw e;
        }
//        ResponseObject result;
//        try {
//            M_GLOBAL_TYPE getCategory = mGlobalTypeRepo.findTopByGlbTypeIdAndIsDeleted(196077, Boolean.FALSE);
//
//            LinkedHashMap<String, Object> responseData = new LinkedHashMap<>();
//            responseData.put("data", getCategory.getRGlobalTypeValues().stream().sorted(Comparator.comparing(e->e.getGlbOrder())));
//
//            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_OK,
//                    responseData);
//            return new ResponseEntity<>(result, result.getHttpCode());
//        } catch (Exception e) {
//            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
//            return new ResponseEntity<>(
//                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
//                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
//                    HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }

    public List<M_PRICING_RULE> getPricingRuleByCriteria(HttpServletRequest httpServletRequest, Integer accountId) {
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

        List<M_PRICING_RULE> mPricingRule = mPricingRuleRepo
                .findAllByEntityIdAndIsDeleted(UserDetailUtils.getEntityFromToken(httpServletRequest), "N");

        Optional<List<Integer>> dtl = mPricingRuleRepo.findByEndDate();

        List<M_PRICING_RULE> mPricingRules = mPricingRule.stream()
                .filter(e -> dtl.get().contains(e.getPricingRuleId())).collect(Collectors.toList());
        List<Integer> idCritFiltered = new ArrayList<>();
        boolean valid = false;

        // criteria filter
        List<Map<String, Integer>> tasCritses = (List<Map<String, Integer>>) allDataCheck.get("tasCrit");
        if (!tasCritses.isEmpty()) {
            for (M_PRICING_RULE pricingRuleId : mPricingRules) {

                List<R_PRICING_RULE_CRITERIA_DATA> critDatas = rPricingRuleCriteriaDataRepo.findAllByIdPricingRule(pricingRuleId.getPricingRuleId());
                if (!critDatas.isEmpty()) {
                    for (R_PRICING_RULE_CRITERIA_DATA critData : critDatas) {
                        if (critData.getAllCriteria() != null && Boolean.TRUE.equals(critData.getAllCriteria())) {
                            idCritFiltered.add(critData.getIdPricingRule());
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
                                idCritFiltered.add(critData.getIdPricingRule());
                                break;
                            }

                        }

                        if (valid) {
                            break;
                        }

                    }
                }
            }

            if (!idCritFiltered.isEmpty()) {
                mPricingRules = mPricingRules.stream().filter(e -> idCritFiltered.contains(e.getPricingRuleId()))
                        .collect(Collectors.toList());
                return mPricingRules;
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

}
