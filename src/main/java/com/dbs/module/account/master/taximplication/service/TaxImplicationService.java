package com.dbs.module.account.master.taximplication.service;

import com.dbs.common.base.utils.*;
import com.dbs.common.library.ctrl.PagingDTO;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.ApprovalServices;
import com.dbs.common.library.services.CriteriaServices;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.*;
import com.dbs.database.crm.entities.accountmanagement.*;
import com.dbs.database.crm.entities.product.T_APPROVAL_HISTORY;
import com.dbs.database.crm.entities.usermanagement.*;
import com.dbs.database.crm.entities.usermanagement.view.VW_HIER_FLOW;
import com.dbs.database.crm.repositories.accountmanagement.Account.*;
import com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement.TAmSATaxImpHeaderRepo;
import com.dbs.database.crm.repositories.accountmanagement.VwAmTaxImplicationCriteriaDataRepo;
import com.dbs.database.crm.repositories.mastermanagement.MAmTaxImplicationRuleOvrConditionRepo;
import com.dbs.database.crm.repositories.mastermanagement.MAmTaxImplicationRuleOvrRepo;
import com.dbs.database.crm.repositories.product.TApprovalHistoryRepo;
import com.dbs.database.crm.repositories.usermanagement.*;
import com.dbs.database.crm.repositories.usermanagement.view.VWHierFlowRepo;
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.ApprovalHierarchyDto;
import com.dbs.module.account.master.latecharge.dto.*;
import com.dbs.module.account.utils.ConstantAccount;
import com.dbs.module.account.utils.UtilsAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jlefebure.spring.boot.minio.MinioConfigurationProperties;
import com.unboundid.util.json.JSONException;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;
import java.io.ByteArrayInputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.dbs.common.library.utils.StringUtils.capitalizeFully;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_FACTURE_CODE;
import com.dbs.database.crm.repositories.rbi.MRbiFactureCodeRepo;
import com.dbs.module.account.master.taximplication.dto.ApprovalHeaderViewDto;
import com.dbs.module.account.master.taximplication.dto.ApprovalTaxImplicationDto;
import com.dbs.module.account.master.taximplication.dto.ApprovalTaxImplicationRuleDto;
import com.dbs.module.account.master.taximplication.dto.AttachmentListDto;
import com.dbs.module.account.master.taximplication.dto.CreateMasterTaxImplicationRequestDto;
import com.dbs.module.account.master.taximplication.dto.CriteriaDataTaxImpliDTO;
import com.dbs.module.account.master.taximplication.dto.DetailTaxImplicationDto;
import com.dbs.module.account.master.taximplication.dto.HistoryLogTaxImplicationDto;
import com.dbs.module.account.master.taximplication.dto.InactiveTaxImplicationDto;
import com.dbs.module.account.master.taximplication.dto.JsonForUpdateTaxImplicationRule;
import com.dbs.module.account.master.taximplication.dto.TaxImplicationCriteriaDto;
import com.dbs.module.account.master.taximplication.dto.TaxImplicationRuleOverrideConditionReqDto;
import com.dbs.module.account.master.taximplication.dto.TaxImplicationRuleOverrideRequestDto;
import com.dbs.module.account.master.taximplication.dto.TaxImplicationRuleRequestDto;
import com.dbs.module.account.master.taximplication.dto.UpdateTaxImplicationRuleDto;
import com.dbs.module.account.master.taximplication.dto.ViewTaxImplicationRuleDto;
import com.dbs.module.account.master.taximplication.validate.ValidateTaxImplication;

import static com.dbs.common.library.utils.StringUtils.capitalizeFullyApproval;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class TaxImplicationService {

    private static final Logger logger = LoggerFactory.getLogger(TaxImplicationService.class);
    private static final String APP_CATEGORY = "TAX_IMPLICATION_RULE";
    private static final String SUBMIT = "SUBMIT";

    @Autowired
    private ApprovalServices approvalServices;

    @Autowired
    private MTaxImplicationCriteriaDataRepo taxImplicationCriteriaDataRepo;

    @Autowired
    private MTaxImplicationRepo taxImpliRepo;

    @Autowired
    private MTaxImplicationRuleRepo taxImpliRuleRepo;

    @Autowired
    private RGlobalTypeValueRepo glbRepo;
    @Autowired
    private MAccountTaxImplicationRepo accountTaxImpliRepo;
    @Autowired
    private MAmTaxImplicationRuleOvrRepo mAmTaxImplicationRuleOvrRepo;
    @Autowired
    private MAmTaxImplicationRuleOvrConditionRepo mAmTaxImplicationRuleOvrConditionRepo;
    @Autowired
    private MAttachmentRepo mAttachmentRepo;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioConfigurationProperties configurationProperties;
    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MTaxImplicationCriteriaRepo mTaxImplicationCriteriaRepo;
    @Autowired
    private MAccountRepo mAccountRepo;
    @Autowired
    private VwAmTaxImplicationCriteriaDataRepo vwAmTaxImplicationCriteriaDataRepo;

    @Autowired
    private TApprovalRepo tApprovalRepo;
    @Autowired
    private VWHierFlowRepo vWHierFlowRepo;
    @Autowired
    private MUserRepo mUserRepo;
    @Autowired
    private MApprovalHierarchyDtlRepo mApprovalHierarchyDtlRepo;
    @Autowired
    private TApprovalHistoryRepo tApprovalHistoryRepo;

    @Autowired
    private MGlobalPropertiesRepo mGlobalPropertiesRepo;

    @Autowired
    private MApprovalHierarchyRepo apphierRepo;

    @Autowired
    private VwAmTaxImplicationRepo vwAmTaxImplicationRepo;

    @Autowired
    private GlobalTypeValueService globalTypeValueService;

    @Autowired
    private AuditTrailRepo auditTrailRepo;

    @Autowired
    private Validator validator;
    
    @Autowired
    private CriteriaServices criteriaServices;
    
    @Autowired
    private ValidateTaxImplication validateTaxImplication;
    
    @Autowired
    private MRbiFactureCodeRepo mRbiFactureCodeRepo;

    @Autowired
    private TAmSATaxImpHeaderRepo tAmSATaxImpHeaderRepo;

    public ResponseEntity<ResponseObject> validateCreateOrUpdate(CreateMasterTaxImplicationRequestDto param, String type) {
        try {
            ResponseObject resp = validateTaxImplication.validateCreateOrUpdate(param, type);
            if (StringUtils.hasValue(resp)) {
                return new ResponseEntity<>(resp, resp.getHttpCode());
            }
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
        ResponseObject response = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    ResponseUtils.MESSAGE_SUCCESS, ResponseUtils.DATA_EMPTY);
        return new ResponseEntity<>(response, response.getHttpCode());
    }
    
    public ResponseEntity<ResponseObject> getListTaxImplication(Integer accountId, MaterialTablePagingRequest pagingData,
            PagedResourcesAssembler<VW_AM_TAXIMPLICATION> assembler) {
        logger.info("Get Tax Implication");
        try {

            Map<String, Object> filter = new HashMap<>();
            List<M_ACCOUNT_TAX_IMPLICATION> allBB = accountTaxImpliRepo.findAllByAccountId(accountId);
            List<Integer> idTax = new ArrayList<>();
            for (M_ACCOUNT_TAX_IMPLICATION allID : allBB) {
                idTax.add(allID.getTaxImplicationId());
            }
            filter.put("id", idTax);

            Page<VW_AM_TAXIMPLICATION> data;

            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key + "~" + value);
                }
            }

            if (!pagingData.getSearch().isEmpty()) {
                data = vwAmTaxImplicationRepo.findAll(vwAmTaxImplicationRepo.getSpecificationFromFiltersAccount(pagingData, filter), PagingUtils.getPaging(pagingData));
            } else {
                data = vwAmTaxImplicationRepo.findAll(vwAmTaxImplicationRepo.getSpecificationDefaultAccount(filter), PagingUtils.getPaging(pagingData));
            }

            List<LinkedHashMap<String, Object>> allData = data.get()
                    .map(g -> {
                        LinkedHashMap<String, Object> dd = new LinkedHashMap<>();
                        dd.put("id", g.getId());
                        dd.put("taxImplicationName", g.getTaxImplicationName());
                        dd.put("serviceType", g.getServiceType());
                        dd.put("category", g.getCategory());
                        dd.put("description", g.getDescription());
                        dd.put("type", g.getType());
                        dd.put("gunggung", g.getGunggung());
                        dd.put("vatInv", g.getVatInv());
                        dd.put("transCode", g.getTransCode());
                        dd.put("transCodeName", g.getTransCodeName());
                        return dd;
                    })
                    .collect(Collectors.toList());

            PagedModel<EntityModel<VW_AM_TAXIMPLICATION>> pagedData = assembler.toModel(data);
            Map<String, Object> d = new HashMap<>();
            d.put("result", allData);
            d.put("page", pagedData.getMetadata());
            d.put("links", pagedData.getLinks());
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get view paging", d), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("java:S1192")
    public ResponseEntity<ResponseObject> getDetailTaxImplication(Integer id) {
        logger.info("Get Tax Implication");
        try {

            Optional<VW_AM_TAXIMPLICATION> dataTax = vwAmTaxImplicationRepo.findTopById(id);

            if (!dataTax.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "Tax Implication with id " + id + " not found!",
                        ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
            VW_AM_TAXIMPLICATION data = dataTax.get();
            LinkedHashMap<String, Object> dd = new LinkedHashMap<>();
            dd.put("id", data.getId());
            dd.put("name", data.getTaxImplicationName());
            dd.put("serviceType", data.getServiceType());
            dd.put("category", data.getCategory());
            dd.put("description", data.getDescription());
            dd.put("type", data.getType());
            dd.put("gunggung", data.getGunggung());
            dd.put("vatInv", data.getVatInv());
            dd.put("transCode", data.getTransCode());
            List<M_AM_TAXIMPLICATION_RULE_OVR> ruleOvr = mAmTaxImplicationRuleOvrRepo.findAllByTaximplicationRuleId(data.getTaxImplicationRuleId());
            List<LinkedHashMap<String, Object>> listOvr = new ArrayList<>();
            for (M_AM_TAXIMPLICATION_RULE_OVR dataOvr : ruleOvr) {
                LinkedHashMap<String, Object> cc = new LinkedHashMap<>();
                cc.put("id", dataOvr.getId());
                cc.put("implicationType", dataOvr.getImplicationTypeOvrValue());
                cc.put("transCode", dataOvr.getTransCodeOvr());
                cc.put("description", dataOvr.getDescription());
                List<LinkedHashMap<String, Object>> listOvrCdt = new ArrayList<>();
                List<M_AM_TAXIMPLICATION_RULE_OVR_CONDITION> ruleOvrCdt = mAmTaxImplicationRuleOvrConditionRepo.findAllByTaximplicationRuleOvrId(dataOvr.getId());
                for (M_AM_TAXIMPLICATION_RULE_OVR_CONDITION dataOvrCdt : ruleOvrCdt) {
                    LinkedHashMap<String, Object> bb = new LinkedHashMap<>();
                    bb.put("id", dataOvrCdt.getId());
                    bb.put("conditionName", dataOvrCdt.getNameValue());
                    bb.put("operator", dataOvrCdt.getOperatorValue());
                    bb.put("value", dataOvrCdt.getValue());
                    listOvrCdt.add(bb);
                }
                cc.put("listRuleOvrCondition", listOvrCdt);
                listOvr.add(cc);
            }
            dd.put("listRuleOvr", listOvr);
            Optional<List<M_AM_TAXIMPLICATION_RULE>> cekActiveRule = taxImpliRuleRepo.findAllByTaximplicationIdAndStatus(data.getId(), FlowStatus.ACTIVE.name());
            if (cekActiveRule.isPresent() && !cekActiveRule.get().isEmpty()) {
                dd.put("isRuleActive", true);
            } else {
                dd.put("isRuleActive", false);
            }
            dd.put("createdDate", data.getCreatedDate());
            dd.put("createdBy", data.getCreatedBy());
            dd.put("updatedDate", data.getUpdatedDate());
            dd.put("updatedBy", data.getUpdatedBy());

            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get detail tax implication", dd), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings({"java:S3776", "java:S1192"})
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> createTaxImplication(CreateMasterTaxImplicationRequestDto requestDto, HttpServletRequest httpServletRequest) {
        ResponseObject result;
        try {

            ResponseObject resp = validateTaxImplication.validateCreateOrUpdate(requestDto, Constant.CREATE);
            if (StringUtils.hasValue(resp)) {
                return new ResponseEntity<>(resp, resp.getHttpCode());
            }

            M_AM_TAXIMPLICATION newData = new M_AM_TAXIMPLICATION();
            newData.setCreatedBy(UserDetailUtils.getUsername());
            newData.setCreatedDate(new Date());
            newData.setTaxImplicationName(requestDto.getTaxImplicationName().strip());
            newData.setCategory(requestDto.getCategory());
            newData.setServiceType(requestDto.getServiceType());
            newData.setDescription(requestDto.getDescription());
            newData.setStatus(FlowStatus.ACTIVE.name());
            M_AM_TAXIMPLICATION savedTaxImplication = taxImpliRepo.save(newData);

            for (CriteriaListDTO criteriaId : requestDto.getCriteria()) {
                M_AM_TAXIMPLICATION_CRITERIA savedCriteria = new M_AM_TAXIMPLICATION_CRITERIA();
                savedCriteria.setCreatedBy(UserDetailUtils.getUsername());
                savedCriteria.setCreatedDate(new Date());
                savedCriteria.setStatus(FlowStatus.ACTIVE.name());
                savedCriteria.setCriteriaId(criteriaId.getValue());
                savedCriteria.setTaximplicationId(savedTaxImplication.getId());
                mTaxImplicationCriteriaRepo.save(savedCriteria);
            }

            for (TaxImplicationCriteriaDto request : requestDto.getTaxImplicationCriterias()) {
                M_AM_TAXIMPLICATION_CRITERIA_DATA data = new M_AM_TAXIMPLICATION_CRITERIA_DATA();
                data.setCreatedBy(UserDetailUtils.getUsername());
                data.setCreatedDate(new Date());
                data.setTaximplicationId(savedTaxImplication.getId());
                data.setAccountCategory(request.getAccountCategory());
                data.setAccountGroupType(request.getAccountGroupType());
                Optional<M_ACCOUNT> getAccountNumber = mAccountRepo.findByAccountId(request.getAccountNumber());
                data.setAccountNumber(getAccountNumber.isPresent() ? getAccountNumber.get().getAccountNumber() : null);
                data.setAccountSegment(request.getAccountSegment());
                data.setAccountType(request.getAccountType());
                data.setClassificationType(request.getClassificationType());
                if (request.getCorporateFlag() != null) {
                    data.setCorporateFlag(request.getCorporateFlag());
                } else {
                    data.setCorporateFlag(null);
                }
                data.setCostCenter(request.getCostCenter());
                if (request.getIsAll() != null) {
                    data.setIsAll(request.getIsAll());
                } else {
                    data.setIsAll(null);
                }
                data.setPremiseCity(request.getPremiseCity());
                data.setPremiseCountry(request.getPremiseCountry());
                data.setPremiseDistrict(request.getPremiseDistrict());
                data.setPremiseProvince(request.getPremiseProvince());
                data.setPremiseSubdistrict(request.getPremiseSubdistrict());
                data.setSaType(request.getSaType());
                data.setSor(request.getSor());
                if (request.getWapuFlag() != null) {
                    data.setWapuFlag(request.getWapuFlag());
                } else {
                    data.setWapuFlag(null);
                }
                data.setStatus(FlowStatus.ACTIVE.name());
                if (!ObjectUtils.isEmpty(request.getStartDate())) {
                    data.setStartDate(UtilsDate.stringToDate(request.getStartDate(), Constant.FORMAT_START_END_DATE));
                }
                if (!ObjectUtils.isEmpty(request.getEndDate())) {
                    data.setEndDate(UtilsDate.stringToDate(request.getEndDate(), Constant.FORMAT_START_END_DATE));
                }
                data.setDescription(request.getDescription());
                taxImplicationCriteriaDataRepo.save(data);
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                    UtilsAccount.messageSuccess(ConstantAccount.CREATE, ConstantAccount.MASTER_TAXIMPLI), savedTaxImplication);

            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> checkStartDateRule(TaxImplicationRuleRequestDto request) {
        ResponseObject result;
        try {
            SimpleDateFormat formatDate = new SimpleDateFormat(Constant.FORMAT_DATE);

            Optional<M_AM_TAXIMPLICATION_RULE> cekStartDate = taxImpliRuleRepo.findTopByTaximplicationIdAndStatusOrderByCreatedDateDesc(request.getTaxImplicationId(), FlowStatus.INACTIVE.name());
            if (cekStartDate.isPresent() && cekStartDate.get().getEndDate() != null && isOverlapping(formatDate.parse(request.getStartDate()), cekStartDate.get().getEndDate())) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Start date must be greater than existing End Date!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success check validate start date", ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings({"java:S3776", "java:S1192"})
    @Transactional(rollbackFor = Exception.class, readOnly = false)
    public ResponseEntity<ResponseObject> createTaxImplicationRule(TaxImplicationRuleRequestDto requestDto, HttpServletRequest httpServletRequest) {
        ResponseObject result;
        try {

            ResponseEntity<ResponseObject> validate = this.validateCreateTaxImpliRule(requestDto, httpServletRequest, Boolean.FALSE);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

//          insert data tax implication rule
            M_AM_TAXIMPLICATION_RULE mAmTaximplicationRule = new M_AM_TAXIMPLICATION_RULE();
            mAmTaximplicationRule.setCreatedBy(UserDetailUtils.getUsername());
            mAmTaximplicationRule.setCreatedDate(new Date());
            mAmTaximplicationRule.setTaximplicationId(requestDto.getTaxImplicationId());
            mAmTaximplicationRule.setDocumentNumber(requestDto.getDocumentNumber());
            mAmTaximplicationRule.setImplicationTypeId(requestDto.getImplicationType());
            Optional<R_GLOBAL_TYPE_VALUE> impliType = glbRepo.findByGlbTypeValId(requestDto.getImplicationType());
            mAmTaximplicationRule.setImplicationType(impliType.isPresent() ? impliType.get().getName() : null);
            mAmTaximplicationRule.setIsVatInv(requestDto.getIsVatInv() ? "Y" : "N");
            mAmTaximplicationRule.setIsGunggung(requestDto.getIsGunggung() ? "Y" : "N");
            mAmTaximplicationRule.setTransCode(requestDto.getTransCode());
//            mAmTaximplicationRule.setTransCodeString(requestDto.getTransCodeString());
            if (!ObjectUtils.isEmpty(requestDto.getStartDate())) {
                mAmTaximplicationRule.setStartDate(UtilsDate.stringToDate(requestDto.getStartDate(), Constant.FORMAT_DATE_OTHER));
            }
            mAmTaximplicationRule.setDescription(requestDto.getDescription());
            mAmTaximplicationRule.setIsOverride(requestDto.getListRuleOverride().isEmpty() ? "N" : "Y");
            mAmTaximplicationRule.setApphierId(requestDto.getAppHierId());

            if (requestDto.getIsSubmit()) {
                mAmTaximplicationRule.setStatus(FlowStatus.DRAFT.name());
                mAmTaximplicationRule.setApprovalStatus(ApprovalStatus.WAITING_APPROVAL.name());
                taxImpliRuleRepo.save(mAmTaximplicationRule);
                Integer tappId = approvalServices.addApprovalFlowR(requestDto.getAppHierId(),
                        ApprovalCategory.TAX_IMPLICATION_RULE.name(), mAmTaximplicationRule.getId().toString(), null);
                approvalServices.setApprovalHistory(tappId, mAmTaximplicationRule.getId(), "Create Tax Implication Rule", APP_CATEGORY,
                        SUBMIT, httpServletRequest);

            } else {
                mAmTaximplicationRule.setStatus(FlowStatus.DRAFT.name());
                mAmTaximplicationRule.setApprovalStatus(ApprovalStatus.DRAFT.name());
                taxImpliRuleRepo.save(mAmTaximplicationRule);
            }

            List<M_AM_TAXIMPLICATION_RULE_OVR_CONDITION> datas2 = new ArrayList<>();
            for (TaxImplicationRuleOverrideRequestDto mAmTaximplicationRuleOvr : requestDto.getListRuleOverride()) {
                M_AM_TAXIMPLICATION_RULE_OVR data = new M_AM_TAXIMPLICATION_RULE_OVR();
                data.setCreatedBy(UserDetailUtils.getUsername());
                data.setCreatedDate(new Date());
                data.setTaximplicationRuleId(mAmTaximplicationRule.getId());
                data.setImplicationTypeOvr(mAmTaximplicationRuleOvr.getImplicationType());
                Optional<R_GLOBAL_TYPE_VALUE> impliTypeOvr = glbRepo.findByGlbTypeValId(mAmTaximplicationRuleOvr.getImplicationType());
                data.setImplicationTypeOvrValue(impliTypeOvr.isPresent() ? impliTypeOvr.get().getName() : null);
                data.setTransCodeOvr(mAmTaximplicationRuleOvr.getTransCode());
                data.setDescription(mAmTaximplicationRuleOvr.getDescription());
                data.setStatus(FlowStatus.DRAFT.name());
                M_AM_TAXIMPLICATION_RULE_OVR saveRuleOvr = mAmTaxImplicationRuleOvrRepo.save(data);
                for (TaxImplicationRuleOverrideConditionReqDto taxImplicationRuleOverrideConditionReqDto : mAmTaximplicationRuleOvr.getListRuleOverrideCondition()) {
                    M_AM_TAXIMPLICATION_RULE_OVR_CONDITION data2 = new M_AM_TAXIMPLICATION_RULE_OVR_CONDITION();
                    data2.setCreatedBy(UserDetailUtils.getUsername());
                    data2.setCreatedDate(new Date());
                    data2.setTaximplicationRuleOvrId(saveRuleOvr.getId());
                    data2.setName(taxImplicationRuleOverrideConditionReqDto.getName());
                    Optional<R_GLOBAL_TYPE_VALUE> nameValue = glbRepo.findByGlbTypeValId(taxImplicationRuleOverrideConditionReqDto.getName());
                    data2.setNameValue(nameValue.isPresent() ? nameValue.get().getName() : null);
                    data2.setOperator(taxImplicationRuleOverrideConditionReqDto.getOperator());
                    Optional<R_GLOBAL_TYPE_VALUE> operatorValue = glbRepo.findByGlbTypeValId(taxImplicationRuleOverrideConditionReqDto.getOperator());
                    data2.setOperatorValue(operatorValue.isPresent() ? operatorValue.get().getName() : null);
                    data2.setDataType(taxImplicationRuleOverrideConditionReqDto.getDataType());
                    data2.setValue(taxImplicationRuleOverrideConditionReqDto.getValue());
                    data2.setStatus(FlowStatus.DRAFT.name());
                    datas2.add(data2);
                }
                mAmTaxImplicationRuleOvrConditionRepo.saveAll(datas2);
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Your data has been submitted", mAmTaximplicationRule);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> checkDuplicateConditionTaxImpli(TaxImplicationRuleRequestDto request) {
        ResponseObject result;
        try {
            for(TaxImplicationRuleOverrideRequestDto req : request.getListRuleOverride()) {
                Set<String> uniqueConditions = new HashSet<>();
                for (TaxImplicationRuleOverrideConditionReqDto check : req.getListRuleOverrideCondition()) {
                    String combination = check.getName().toString() + "-" + check.getOperator().toString();
                    if (!uniqueConditions.add(combination)) {
                        return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                                "Condition with same name & operation is already exist", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                    }
                }
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.VALIDATE, ConstantAccount.MASTER_TAXIMPLI), ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateCreateTaxImpliRule(TaxImplicationRuleRequestDto requestDto, HttpServletRequest httpServletRequest, Boolean api) {
        ResponseObject result;
        try {
            boolean isSubmiter = approvalServices.checkIsSubmitter(httpServletRequest, requestDto.getAppHierId());
            if (!isSubmiter) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "You are not submitter",
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            Optional<M_AM_TAXIMPLICATION_RULE> cekDocUnique = taxImpliRuleRepo.findTopByTaximplicationIdAndDocumentNumberIgnoreCase(requestDto.getTaxImplicationId(),requestDto.getDocumentNumber());
            if(cekDocUnique.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Document Number already Exist", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
            for(TaxImplicationRuleOverrideRequestDto req : requestDto.getListRuleOverride()) {
                Set<String> uniqueConditions = new HashSet<>();
                for (TaxImplicationRuleOverrideConditionReqDto check : req.getListRuleOverrideCondition()) {
                    String combination = check.getName().toString() + "-" + check.getOperator().toString();
                    if (!uniqueConditions.add(combination)) {
                        return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                                "Condition with same name & operation is already exist", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                    }
                }
            }

            if (requestDto.getIsSubmit()) {
                // CEK ACTIVE
                Optional<List<M_AM_TAXIMPLICATION_RULE>> cekActiveRule = taxImpliRuleRepo.findAllByTaximplicationIdAndStatus(requestDto.getTaxImplicationId(), FlowStatus.ACTIVE.name());
                if (cekActiveRule.isPresent() && !cekActiveRule.get().isEmpty()) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            UtilsAccount.messageValidateExist(ConstantAccount.MASTER_TAXIMPLI, ConstantAccount.APPROVED), ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
                // CEK WAITING APPROVAL
                Optional<List<M_AM_TAXIMPLICATION_RULE>> cekWaitingRule = taxImpliRuleRepo.findAllByTaximplicationIdAndStatusAndApprovalStatus(requestDto.getTaxImplicationId(), FlowStatus.DRAFT.name(), ApprovalStatus.WAITING_APPROVAL.name());
                if (cekWaitingRule.isPresent() && !cekWaitingRule.get().isEmpty()) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            UtilsAccount.messageValidateExist(ConstantAccount.MASTER_TAXIMPLI, ConstantAccount.WAITING_APPROVAL), ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
            }

            return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.MASTER_TAXIMPLI);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(),e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }


    public ResponseEntity<ResponseObject> deleteDraftRule(Integer taxImplicationRuleId) {
        ResponseObject result;
        try {
            Optional<M_AM_TAXIMPLICATION_RULE> findRule = taxImpliRuleRepo.findById(taxImplicationRuleId);
            if (!findRule.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Tax Implication Rule with id " + taxImplicationRuleId + " not found!", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            if ((!findRule.get().getStatus().equalsIgnoreCase(FlowStatus.DRAFT.name())) && (!findRule.get().getApprovalStatus().equalsIgnoreCase(FlowStatus.DRAFT.name()))) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Tax Implication Rule with id " + taxImplicationRuleId + " is not draft!", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            taxImpliRuleRepo.deleteById(taxImplicationRuleId);
            List<M_AM_TAXIMPLICATION_RULE_OVR> ruleOvr = mAmTaxImplicationRuleOvrRepo.findAllByTaximplicationRuleId(taxImplicationRuleId);
            for (M_AM_TAXIMPLICATION_RULE_OVR ovr : ruleOvr) {
                mAmTaxImplicationRuleOvrRepo.deleteById(ovr.getId());
                List<M_AM_TAXIMPLICATION_RULE_OVR_CONDITION> ruleOvrCdt = mAmTaxImplicationRuleOvrConditionRepo.findAllByTaximplicationRuleOvrId(ovr.getId());
                for (M_AM_TAXIMPLICATION_RULE_OVR_CONDITION ovrCdt : ruleOvrCdt) {
                    mAmTaxImplicationRuleOvrConditionRepo.deleteById(ovrCdt.getId());
                }
            }
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Successs delete draft Tax Implication Rule", ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> attachmentRule(Integer fileCategoryId, Integer taxImplicationRuleId, List<MultipartFile> files) throws Exception {
        ResponseObject result;
        try {
            for (MultipartFile fileObject : files) {
                String generatedFileName = UserDetailUtils.generateFileName(fileObject.getOriginalFilename());
                M_ATTACHMENT mAttachment = new M_ATTACHMENT();
                mAttachment.setCategory("TAXIMPLICATION_RULE_ATTACHMENT");
                mAttachment.setFileCategoryId(fileCategoryId);
                mAttachment.setReferenceId(taxImplicationRuleId);
                mAttachment.setType(fileObject.getContentType());
                mAttachment.setCreatedBy(UserDetailUtils.getUsername());
                mAttachment.setCreatedDate(new Date());
                mAttachment.setPathFile("PATH_25");
                mAttachment.setFileName(generatedFileName);
                mAttachment.setFileSize(fileObject.getSize());
                mAttachment.setIsDraft(Boolean.TRUE);
                mAttachment.setIsDeleted(Boolean.FALSE);

                R_GLOBAL_TYPE_VALUE rGlobalTypeValue = rGlobalTypeValueRepo
                        .findTopByGlbValueIgnoreCaseAndIsDeleted("PATH_25", false);
                String fullPath = rGlobalTypeValue.getName() + generatedFileName;

                String fullobject = "FILE" + fullPath;
                mAttachmentRepo.save(mAttachment);
                this.minioClient.putObject(this.configurationProperties.getBucket(), fullobject,
                        fileObject.getInputStream(), fileObject.getContentType());

            }
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Tax Implication Rule Attachment Uploaded", ResponseUtils.MESSAGE_OK);

            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings({"java:S3776", "java:S1192"})
    public ResponseEntity<ResponseObject> updateTaxImplication(CreateMasterTaxImplicationRequestDto updateTaxImplicationDto) {
        ResponseObject result;
        try {
            
             ResponseObject resp = validateTaxImplication.validateCreateOrUpdate(updateTaxImplicationDto, Constant.UPDATE);
            if (StringUtils.hasValue(resp)) {
                return new ResponseEntity<>(resp, resp.getHttpCode());
            }
            
            Optional<M_AM_TAXIMPLICATION> getTaxImplication = taxImpliRepo.findById(updateTaxImplicationDto.getTaxImplicationId());

            M_AM_TAXIMPLICATION mAmTaximplication = getTaxImplication.get();
            mAmTaximplication.setUpdatedDate(new Date());
            mAmTaximplication.setUpdatedBy(UserDetailUtils.getUsername());
            mAmTaximplication.setTaxImplicationName(updateTaxImplicationDto.getTaxImplicationName());
            mAmTaximplication.setDescription(updateTaxImplicationDto.getDescription());
            mAmTaximplication.setCategory(updateTaxImplicationDto.getCategory());
            mAmTaximplication.setServiceType(updateTaxImplicationDto.getServiceType());

            if (!updateTaxImplicationDto.getCriteria().isEmpty()) {
                List<Integer> receivedIdCriteria = new ArrayList<>();
                List<M_AM_TAXIMPLICATION_CRITERIA> getCriteriaId = mTaxImplicationCriteriaRepo.findAllByTaximplicationId(updateTaxImplicationDto.getTaxImplicationId());
                for (CriteriaListDTO criteriaId : updateTaxImplicationDto.getCriteria()) {
                    Optional<M_AM_TAXIMPLICATION_CRITERIA> getCriteria = criteriaId.getId() != null ? mTaxImplicationCriteriaRepo.findById(criteriaId.getId()) : null;
                    M_AM_TAXIMPLICATION_CRITERIA criteria = new M_AM_TAXIMPLICATION_CRITERIA();
                    if (getCriteria != null) {
                        criteria = getCriteria.get();
                        criteria.setUpdatedBy(UserDetailUtils.getUsername());
                        criteria.setUpdatedDate(new Date());
                        receivedIdCriteria.add(criteria.getId());
                    } else {
                        criteria.setCreatedBy(UserDetailUtils.getUsername());
                        criteria.setCreatedDate(new Date());
                    }
                    criteria.setStatus(FlowStatus.ACTIVE.name());
                    criteria.setCriteriaId(criteriaId.getValue());
                    criteria.setTaximplicationId(updateTaxImplicationDto.getTaxImplicationId());
                    mTaxImplicationCriteriaRepo.save(criteria);
                }
                for (M_AM_TAXIMPLICATION_CRITERIA dd : getCriteriaId) {
                    if (receivedIdCriteria.isEmpty() || !receivedIdCriteria.contains(dd.getId())) {
                        mTaxImplicationCriteriaRepo.deleteById(dd.getId());
                    }
                }
            }

            if (!updateTaxImplicationDto.getTaxImplicationCriterias().isEmpty()) {
                List<M_AM_TAXIMPLICATION_CRITERIA_DATA> requestupdateDatas = new ArrayList<>();
                List<Integer> receivedIdCriteriaDatas = new ArrayList<>();
                List<M_AM_TAXIMPLICATION_CRITERIA_DATA> dataCriteriaDatas = taxImplicationCriteriaDataRepo.findAllByTaximplicationId(updateTaxImplicationDto.getTaxImplicationId());
                for (TaxImplicationCriteriaDto requestupdateData : updateTaxImplicationDto.getTaxImplicationCriterias()) {
                    Optional<M_AM_TAXIMPLICATION_CRITERIA_DATA> getCriteria = requestupdateData.getId() != null ? taxImplicationCriteriaDataRepo.findById(requestupdateData.getId()) : Optional.empty();
                    M_AM_TAXIMPLICATION_CRITERIA_DATA criteriaData = new M_AM_TAXIMPLICATION_CRITERIA_DATA();
                    if (getCriteria.isPresent()) {
                        criteriaData = getCriteria.get();
                        criteriaData.setUpdatedBy(UserDetailUtils.getUsername());
                        criteriaData.setUpdatedDate(new Date());
                        receivedIdCriteriaDatas.add(criteriaData.getId());
                    } else {
                        criteriaData.setCreatedBy(UserDetailUtils.getUsername());
                        criteriaData.setUpdatedDate(new Date());
                    }
                    criteriaData.setTaximplicationId(mAmTaximplication.getId());
                    criteriaData.setSor(requestupdateData.getSor());
                    criteriaData.setPremiseCountry(requestupdateData.getPremiseCountry());
                    criteriaData.setPremiseProvince(requestupdateData.getPremiseProvince());
                    criteriaData.setPremiseCity(requestupdateData.getPremiseCity());
                    criteriaData.setPremiseDistrict(requestupdateData.getPremiseDistrict());
                    criteriaData.setPremiseSubdistrict(requestupdateData.getPremiseSubdistrict());
                    criteriaData.setSor(requestupdateData.getSor());
                    criteriaData.setCostCenter(requestupdateData.getCostCenter());
                    criteriaData.setAccountCategory(requestupdateData.getAccountCategory());
                    criteriaData.setClassificationType(requestupdateData.getClassificationType());
                    Optional<M_ACCOUNT> getAccountNumber = mAccountRepo.findByAccountId(requestupdateData.getAccountNumber());
                    criteriaData.setAccountNumber(getAccountNumber.isPresent() ? getAccountNumber.get().getAccountNumber() : null);
                    criteriaData.setAccountSegment(requestupdateData.getAccountSegment());
                    criteriaData.setAccountGroupType(requestupdateData.getAccountGroupType());
                    criteriaData.setAccountType(requestupdateData.getAccountType());
                    criteriaData.setSaType(requestupdateData.getSaType());
                    criteriaData.setCorporateFlag(requestupdateData.getCorporateFlag());
                    criteriaData.setWapuFlag(requestupdateData.getWapuFlag());
                    criteriaData.setIsAll(requestupdateData.getIsAll());
                    criteriaData.setDescription(requestupdateData.getDescription());
                    if (!ObjectUtils.isEmpty(requestupdateData.getStartDate())) {
                        criteriaData.setStartDate(UtilsDate.stringToDate(requestupdateData.getStartDate(), Constant.FORMAT_START_END_DATE));
                    }
                    if (!ObjectUtils.isEmpty(requestupdateData.getEndDate())) {
                        criteriaData.setEndDate(UtilsDate.stringToDate(requestupdateData.getEndDate(), Constant.FORMAT_START_END_DATE));
                    }
//                    if (!ObjectUtils.isEmpty(requestupdateData.getCorporateFlag())) {
//                        criteriaData.setCorporateFlag(requestupdateData.getCorporateFlag());
//                    } else {
//                        criteriaData.setCorporateFlag(null);
//                    }
//                    if (!ObjectUtils.isEmpty(requestupdateData.getWapuFlag())) {
//                        criteriaData.setWapuFlag(requestupdateData.getWapuFlag());
//                    } else {
//                        criteriaData.setWapuFlag(null);
//                    }
//                    if (!ObjectUtils.isEmpty(requestupdateData.getIsAll())) {
//                        criteriaData.setIsAll(requestupdateData.getIsAll());
//                    } else {
//                        criteriaData.setIsAll(null);
//                    }
                    requestupdateDatas.add(criteriaData);
                }
                if (!requestupdateDatas.isEmpty()) {
                    taxImplicationCriteriaDataRepo.saveAll(requestupdateDatas);
                }
                for (M_AM_TAXIMPLICATION_CRITERIA_DATA dd : dataCriteriaDatas) {
                    if (receivedIdCriteriaDatas.isEmpty() || !receivedIdCriteriaDatas.contains(dd.getId())) {
                        taxImplicationCriteriaDataRepo.deleteById(dd.getId());
                    }
                }
            }

            taxImpliRepo.save(mAmTaximplication);
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Updated", mAmTaximplication);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @SuppressWarnings({"java:S3776", "java:S1192"})
    public ResponseEntity<ResponseObject> updateTaxImplicationRule(UpdateTaxImplicationRuleDto updateTaxImplicationRuleDto, HttpServletRequest httpServletRequest) {
        ResponseObject result;
        try {

            ResponseEntity<ResponseObject> validate = this.validateUpdateTaxImpliRule(updateTaxImplicationRuleDto, httpServletRequest, Boolean.FALSE);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            Optional<M_AM_TAXIMPLICATION_RULE> findTaximplicationRule = taxImpliRuleRepo.findById(updateTaxImplicationRuleDto.getTaxImplicationRuleId());
            M_AM_TAXIMPLICATION_RULE taximplicationRule = findTaximplicationRule.get();
            List<M_AM_TAXIMPLICATION_RULE_OVR> dataRule = mAmTaxImplicationRuleOvrRepo.findAllByTaximplicationRuleId(taximplicationRule.getId());
            List<Integer> receivedAllConditionId = new ArrayList<>();

            for (M_AM_TAXIMPLICATION_RULE_OVR loop : dataRule) {
                List<M_AM_TAXIMPLICATION_RULE_OVR_CONDITION> dataCdt = mAmTaxImplicationRuleOvrConditionRepo.findAllByTaximplicationRuleOvrId(loop.getId());
                for (M_AM_TAXIMPLICATION_RULE_OVR_CONDITION loopCdt : dataCdt) {
                    receivedAllConditionId.add(loopCdt.getId());
                }
            }

            if (taximplicationRule.getStatus().equalsIgnoreCase(FlowStatus.DRAFT.name())) {
                taximplicationRule.setUpdatedBy(UserDetailUtils.getUsername());
                taximplicationRule.setUpdatedDate(new Date());
                taximplicationRule.setImplicationTypeId(updateTaxImplicationRuleDto.getImplicationType());
                Optional<R_GLOBAL_TYPE_VALUE> impliType = glbRepo.findByGlbTypeValId(updateTaxImplicationRuleDto.getImplicationType());
                taximplicationRule.setImplicationType(impliType.isPresent() ? impliType.get().getName() : null);
                taximplicationRule.setIsVatInv(updateTaxImplicationRuleDto.getIsVatInv() ? "Y" : "N");
                taximplicationRule.setIsGunggung(updateTaxImplicationRuleDto.getIsGunggung() ? "Y" : "N");
                taximplicationRule.setTransCode(updateTaxImplicationRuleDto.getTransCode());
//                taximplicationRule.setTransCodeString(updateTaxImplicationRuleDto.getTransCodeString());
                if (!ObjectUtils.isEmpty(updateTaxImplicationRuleDto.getStartDate())) {
                    Date dateStartDate = UtilsDate.stringToDate(updateTaxImplicationRuleDto.getStartDate(), Constant.FORMAT_DATE_OTHER);
                    taximplicationRule.setStartDate(dateStartDate);
                }
                taximplicationRule.setDescription(updateTaxImplicationRuleDto.getDescription());
                taximplicationRule.setDocumentNumber(updateTaxImplicationRuleDto.getDocumentNumber());
                taximplicationRule.setApphierId(updateTaxImplicationRuleDto.getAppHierId());
                if (updateTaxImplicationRuleDto.getIsSubmit()) {
                    taximplicationRule.setStatus(ApprovalStatus.DRAFT.name());
                    taximplicationRule.setApprovalStatus(ApprovalStatus.WAITING_APPROVAL.name());
                } else {
                    taximplicationRule.setStatus(ApprovalStatus.DRAFT.name());
                    taximplicationRule.setApprovalStatus(ApprovalStatus.DRAFT.name());
                }
                taxImpliRuleRepo.save(taximplicationRule);

                List<Integer> receivedIdOvr = new ArrayList<>();
                List<Integer> receivedIdCdt = new ArrayList<>();
                List<M_AM_TAXIMPLICATION_RULE_OVR_CONDITION> datas = new ArrayList<>();
                for (TaxImplicationRuleOverrideRequestDto mAmTaximplicationRuleOvr : updateTaxImplicationRuleDto.getListRuleOverride()) {
                    Optional<M_AM_TAXIMPLICATION_RULE_OVR> dataOvr = mAmTaximplicationRuleOvr.getId() != null ? mAmTaxImplicationRuleOvrRepo.findById(mAmTaximplicationRuleOvr.getId()) : null;
                    M_AM_TAXIMPLICATION_RULE_OVR ovr = new M_AM_TAXIMPLICATION_RULE_OVR();
                    if (dataOvr != null) {
                        ovr = dataOvr.get();
                        ovr.setUpdatedDate(new Date());
                        ovr.setUpdatedBy(UserDetailUtils.getUsername());
                        receivedIdOvr.add(ovr.getId());
                    } else {
                        ovr.setCreatedDate(new Date());
                        ovr.setCreatedBy(UserDetailUtils.getUsername());
                    }
                    ovr.setTaximplicationRuleId(taximplicationRule.getId());
                    ovr.setImplicationTypeOvr(mAmTaximplicationRuleOvr.getImplicationType());
                    Optional<R_GLOBAL_TYPE_VALUE> impliTypeOvr = glbRepo.findByGlbTypeValId(mAmTaximplicationRuleOvr.getImplicationType());
                    ovr.setImplicationTypeOvrValue(impliTypeOvr.isPresent() ? impliTypeOvr.get().getName() : null);
                    ovr.setTransCodeOvr(mAmTaximplicationRuleOvr.getTransCode());
                    ovr.setDescription(mAmTaximplicationRuleOvr.getDescription());
                    ovr.setStatus(FlowStatus.DRAFT.name());
                    mAmTaxImplicationRuleOvrRepo.save(ovr);

                    for (TaxImplicationRuleOverrideConditionReqDto taxImplicationRuleOverrideConditionReqDto : mAmTaximplicationRuleOvr.getListRuleOverrideCondition()) {
                        Optional<M_AM_TAXIMPLICATION_RULE_OVR_CONDITION> dataCdt = taxImplicationRuleOverrideConditionReqDto.getId() != null ? mAmTaxImplicationRuleOvrConditionRepo.findById(taxImplicationRuleOverrideConditionReqDto.getId()) : null;
                        M_AM_TAXIMPLICATION_RULE_OVR_CONDITION cdt = new M_AM_TAXIMPLICATION_RULE_OVR_CONDITION();
                        if (dataCdt != null) {
                            cdt = dataCdt.get();
                            cdt.setUpdatedDate(new Date());
                            cdt.setUpdatedBy(UserDetailUtils.getUsername());
                            receivedIdCdt.add(cdt.getId());
                        } else {
                            cdt.setCreatedDate(new Date());
                            cdt.setCreatedBy(UserDetailUtils.getUsername());
                        }
                        cdt.setTaximplicationRuleOvrId(ovr.getId());
                        cdt.setName(taxImplicationRuleOverrideConditionReqDto.getName());
                        Optional<R_GLOBAL_TYPE_VALUE> nameValue = glbRepo.findByGlbTypeValId(taxImplicationRuleOverrideConditionReqDto.getName());
                        cdt.setNameValue(nameValue.isPresent() ? nameValue.get().getName() : null);
                        cdt.setOperator(taxImplicationRuleOverrideConditionReqDto.getOperator());
                        Optional<R_GLOBAL_TYPE_VALUE> operatorValue = glbRepo.findByGlbTypeValId(taxImplicationRuleOverrideConditionReqDto.getOperator());
                        cdt.setOperatorValue(operatorValue.isPresent() ? operatorValue.get().getName() : null);
                        cdt.setDataType(taxImplicationRuleOverrideConditionReqDto.getDataType());
                        cdt.setValue(taxImplicationRuleOverrideConditionReqDto.getValue());
                        cdt.setStatus(FlowStatus.DRAFT.name());
                        datas.add(cdt);
                    }
                    mAmTaxImplicationRuleOvrConditionRepo.saveAll(datas);
                }

                // delete
                for (M_AM_TAXIMPLICATION_RULE_OVR dd : dataRule) {
                    if (receivedIdOvr.isEmpty() || !receivedIdOvr.contains(dd.getId())) {
                        mAmTaxImplicationRuleOvrRepo.deleteById(dd.getId());
                    }
                }

                for (Integer cc : receivedAllConditionId) {
                    if (receivedIdCdt.isEmpty() || !receivedIdCdt.contains(cc)) {
                        mAmTaxImplicationRuleOvrConditionRepo.deleteById(cc);
                    }
                }

            } else {
                JsonForUpdateLateChargeRuleDto jsonData = new JsonForUpdateLateChargeRuleDto();
                jsonData.setDescription(updateTaxImplicationRuleDto.getDescription());
                jsonData.setEndDate(null);
                String json = "";
                json = new Gson().toJson(jsonData);
                taximplicationRule.setJsonData(json);
                taximplicationRule.setUpdatedBy(UserDetailUtils.getUsername());
                taximplicationRule.setUpdatedDate(new Date());
            }

            if (updateTaxImplicationRuleDto.getIsSubmit()) {
                taximplicationRule.setApprovalStatus(ApprovalStatus.WAITING_APPROVAL.name());
                taxImpliRuleRepo.save(taximplicationRule);

                Integer tappId = approvalServices.addApprovalFlowR(updateTaxImplicationRuleDto.getAppHierId(),
                        ApprovalCategory.TAX_IMPLICATION_RULE.name(), taximplicationRule.getId().toString(), "Update latecharge rule");

                approvalServices.setApprovalHistory(tappId, taximplicationRule.getId(), "Update Late Charge Rule", ApprovalCategory.TAX_IMPLICATION_RULE.name(),
                        SUBMIT, httpServletRequest);
            } else {
                taximplicationRule.setApprovalStatus(ApprovalStatus.DRAFT.name());
                taxImpliRuleRepo.save(taximplicationRule);
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Late Charge Update", taximplicationRule);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateUpdateTaxImpliRule(UpdateTaxImplicationRuleDto updateTaxImplicationRuleDto, HttpServletRequest httpServletRequest, Boolean api) {
        ResponseObject result;
        try {
            boolean isSubmiter = approvalServices.checkIsSubmitter(httpServletRequest, updateTaxImplicationRuleDto.getAppHierId());
            if (!isSubmiter) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "You are not submitter",
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            Optional<M_AM_TAXIMPLICATION_RULE> findTaximplicationRule = taxImpliRuleRepo.findById(updateTaxImplicationRuleDto.getTaxImplicationRuleId());
            if (findTaximplicationRule.isEmpty()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Tax implication rule with id " + updateTaxImplicationRuleDto.getTaxImplicationRuleId() + " not found", null), HttpStatus.NOT_FOUND);
            }
            M_AM_TAXIMPLICATION_RULE taximplicationRule = findTaximplicationRule.get();
            if (taximplicationRule.getApprovalStatus().equalsIgnoreCase(ApprovalStatus.WAITING_APPROVAL.name())) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Data is Waiting For Approval Cannot  change", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            Optional<M_AM_TAXIMPLICATION_RULE> cekDocUnique = taxImpliRuleRepo.findTopByTaximplicationIdAndDocumentNumberIgnoreCase(taximplicationRule.getTaximplicationId(), updateTaxImplicationRuleDto.getDocumentNumber());
            if(cekDocUnique.isPresent() && !cekDocUnique.get().getId().equals(taximplicationRule.getId())) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Document Number already Exist", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
            for(TaxImplicationRuleOverrideRequestDto req : updateTaxImplicationRuleDto.getListRuleOverride()) {
                Set<String> uniqueConditions = new HashSet<>();
                for (TaxImplicationRuleOverrideConditionReqDto check : req.getListRuleOverrideCondition()) {
                    String combination = check.getName().toString() + "-" + check.getOperator().toString();
                    if (StringUtils.hasValue(req.getId()) && !uniqueConditions.add(combination)) {
                        return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                                "Condition with same name & operation is already exist", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                    }
                }
                uniqueConditions.clear();
            }

            if (updateTaxImplicationRuleDto.getIsSubmit()) {
                Optional<List<M_AM_TAXIMPLICATION_RULE>> cekActiveRule = taxImpliRuleRepo.findAllByTaximplicationIdAndStatus(taximplicationRule.getTaximplicationId(), FlowStatus.ACTIVE.name());
                if (cekActiveRule.isPresent() && !cekActiveRule.get().isEmpty()) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            UtilsAccount.messageValidateExist(ConstantAccount.MASTER_TAXIMPLI, ConstantAccount.APPROVED), ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
                // CEK WAITING APPROVAL
                Optional<List<M_AM_TAXIMPLICATION_RULE>> cekWaitingRule = taxImpliRuleRepo.findAllByTaximplicationIdAndStatusAndApprovalStatus(taximplicationRule.getTaximplicationId(), FlowStatus.DRAFT.name(), ApprovalStatus.WAITING_APPROVAL.name());
                if (cekWaitingRule.isPresent() && !cekWaitingRule.get().isEmpty()) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            UtilsAccount.messageValidateExist(ConstantAccount.MASTER_TAXIMPLI, ConstantAccount.WAITING_APPROVAL), ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
                SimpleDateFormat formatDate = new SimpleDateFormat(Constant.FORMAT_DATE_OTHER);
                Optional<M_AM_TAXIMPLICATION_RULE> cekStartDate = taxImpliRuleRepo.findTopByTaximplicationIdAndStatusOrderByCreatedDateDesc(taximplicationRule.getTaximplicationId(), FlowStatus.INACTIVE.name());
                if (cekStartDate.isPresent() && cekStartDate.get().getEndDate() != null && isOverlapping(formatDate.parse(updateTaxImplicationRuleDto.getStartDate()), cekStartDate.get().getEndDate())) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Start date must be greater than existing End Date!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }

            }

            return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.MASTER_TAXIMPLI);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(),e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }

    @SuppressWarnings({"java:S3776", "java:S1192"})
    public ResponseEntity<ResponseObject> approveTaxImplicationRule(ApprovalTaxImplicationRuleDto approvalTaxImplicationRuleDto, HttpServletRequest httpServletRequest)
            throws IllegalArgumentException, JSONException, JsonProcessingException {
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_AM_TAXIMPLICATION_RULE> findTaxImplicationRule = taxImpliRuleRepo.findById(approvalTaxImplicationRuleDto.getTaxImplicationRuleId());
            if (findTaxImplicationRule.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND, ResponseUtils.MESSAGE_NOT_FOUND,
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            M_AM_TAXIMPLICATION_RULE taximplicationRule = findTaxImplicationRule.get();
            boolean isForward = approvalServices.isForwardPosition(approvalTaxImplicationRuleDto.getApprovalId());
            if (isForward) {
                approvalServices.setApprHistoryForward(approvalTaxImplicationRuleDto.getApprovalId(), taximplicationRule.getId(), approvalTaxImplicationRuleDto.getDescription(),
                    ApprovalCategory.TAX_IMPLICATION_RULE.name(), approvalTaxImplicationRuleDto.getAction(), httpServletRequest);

            } else {
                approvalServices.setApprovalHistory(approvalTaxImplicationRuleDto.getApprovalId(), taximplicationRule.getId(), approvalTaxImplicationRuleDto.getDescription(),
                    ApprovalCategory.TAX_IMPLICATION_RULE.name(), approvalTaxImplicationRuleDto.getAction(), httpServletRequest);
            }
            
            Optional<List<M_ATTACHMENT>> attachOpt = mAttachmentRepo
                    .findByReferenceIdAndIsDraftAndIsDeletedAndCategoryIgnoreCase(
                            taximplicationRule.getId(), Boolean.TRUE, Boolean.FALSE, "TAXIMPLICATION_RULE_ATTACHMENT");

            boolean isFinal = approvalServices.actionNextFlowApproval(approvalTaxImplicationRuleDto.getApprovalId(), approvalTaxImplicationRuleDto.getDescription(),
                    ApprovalStatus.APPROVE);
            if (isFinal) {
                if (approvalTaxImplicationRuleDto.getAction().equalsIgnoreCase(ApprovalStatus.REJECT.name())) {
                    // TAX IMPLI
                    taximplicationRule.setApprovalStatus(FlowStatus.REJECTED.name());
                    taximplicationRule.setUpdatedDate(new Date());
                    taximplicationRule.setUpdatedBy(UserDetailUtils.getUsername());
                    taximplicationRule.setJsonData(null);
                    taxImpliRuleRepo.save(taximplicationRule);
                    approvalServices.actionNextFlowApproval(approvalTaxImplicationRuleDto.getApprovalId(), approvalTaxImplicationRuleDto.getDescription(),
                            ApprovalStatus.REJECT);

                    // ATTACHMENT
//                    if (attachOpt.isPresent() && !attachOpt.get().isEmpty()) {
//                        for (M_ATTACHMENT attach2 : attachOpt.get()) {
//                            attach2.setIsDeleted(Boolean.TRUE);
//                            attach2.setUpdatedBy(UserDetailUtils.getUsername());
//                            attach2.setUpdatedDate(new Date());
//                            mAttachmentRepo.save(attach2);
//                        }
//                    }

                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            "Your data has been rejected", null);
                } else {

                    // UPDATE DRAFT
                    if (!ObjectUtils.isEmpty(taximplicationRule.getJsonData())) {
                        boolean updateData = this.updateApprovedData(taximplicationRule.getId(), httpServletRequest);
                        if (updateData) {
                            taximplicationRule.setJsonData(null);
                        }
                    }

                    // ATTACHMENT
                    if (attachOpt.isPresent() && !attachOpt.get().isEmpty()) {
                        for (M_ATTACHMENT attach2 : attachOpt.get()) {
                            attach2.setIsDraft(Boolean.FALSE);
                            attach2.setUpdatedBy(UserDetailUtils.getUsername());
                            attach2.setUpdatedDate(new Date());
                            mAttachmentRepo.save(attach2);
                        }
                    }

                    taximplicationRule.setStatus(FlowStatus.ACTIVE.name());
                    taximplicationRule.setApprovalStatus(ApprovalStatus.APPROVED.name());
                    taxImpliRuleRepo.save(taximplicationRule);
                    List<M_AM_TAXIMPLICATION_RULE_OVR> listOvr = mAmTaxImplicationRuleOvrRepo.findAllByTaximplicationRuleId(taximplicationRule.getId());
                    if (!listOvr.isEmpty()) {
                        for (M_AM_TAXIMPLICATION_RULE_OVR ovr : listOvr) {
                            ovr.setStatus(FlowStatus.ACTIVE.name());
                            mAmTaxImplicationRuleOvrRepo.save(ovr);
                            List<M_AM_TAXIMPLICATION_RULE_OVR_CONDITION> listOvrCdt = mAmTaxImplicationRuleOvrConditionRepo.findAllByTaximplicationRuleOvrId(ovr.getId());
                            if (!listOvrCdt.isEmpty()) {
                                for (M_AM_TAXIMPLICATION_RULE_OVR_CONDITION cdt : listOvrCdt) {
                                    cdt.setStatus(FlowStatus.ACTIVE.name());
                                    mAmTaxImplicationRuleOvrConditionRepo.save(cdt);
                                }
                            }
                        }
                    }
                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            "Tax Implication rule has been approved", null);
                }

            }
            
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class, readOnly = false)
    private Boolean updateApprovedData(Integer taxImplicationRuleId, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        try {
            Optional<M_AM_TAXIMPLICATION_RULE> findTaxImplicationRule = taxImpliRuleRepo.findById(taxImplicationRuleId);
            if (findTaxImplicationRule.isEmpty()) {
                return Boolean.FALSE;
            }
            M_AM_TAXIMPLICATION_RULE taxImplicationRule = findTaxImplicationRule.get();
            String jsonStr = taxImplicationRule.getJsonData();
            objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            JsonForUpdateTaxImplicationRule jsonObject = objectMapper.readValue(jsonStr, JsonForUpdateTaxImplicationRule.class);
            taxImplicationRule.setDescription(jsonObject.getDescription());
            taxImplicationRule.setUpdatedDate(new Date());
            taxImplicationRule.setUpdatedBy(UserDetailUtils.getUsername());
            taxImpliRuleRepo.save(taxImplicationRule);
            return Boolean.TRUE;
        } catch (Exception e) {
            logger.error(CommonVariables.ERROR_IN, e.getMessage(), e);
            throw e;
        }
    }

    public ResponseEntity<ResponseObject> inactiveTaxImplication(InactiveTaxImplicationDto request) {
        ResponseObject result;
        try {
            Optional<M_AM_TAXIMPLICATION> findTaxImplication = taxImpliRepo.findById(request.getTaxImplicationId());
            if (findTaxImplication.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND, ResponseUtils.MESSAGE_NOT_FOUND,
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            M_AM_TAXIMPLICATION taxImplication = findTaxImplication.get();

            List<M_AM_TAXIMPLICATION_RULE> taximplicationRules = taxImpliRuleRepo.findAllByTaximplicationIdAndStatusActiveOrWaitingApproval(request.getTaxImplicationId());
            if (!taximplicationRules.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "You cannot inactivate this tax implication because there is a tax implication rule that are still active/waiting approval", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            Optional<T_AM_SA_TAXIMP_HEADER> checkActiveSa = tAmSATaxImpHeaderRepo.findTopByTaxImplicationIdAndStatus(taxImplication.getId(), FlowStatus.ACTIVE.name());
            if(checkActiveSa.isPresent()){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "You can't inactivate tax implication. This tax implication used by account service agreement.", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
            ObjectMapper mapper = new ObjectMapper();
            String oldValue = mapper.writeValueAsString(taxImplication);
            auditTrail.setOldValue(oldValue);
            if(request.getRemark()==null) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "remark not be empty", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            auditTrail.setRemark(request.getRemark());
            auditTrail.setTableName("M_AM_TAXIMPLICATION");
            auditTrail.setDataId(taxImplication.getId().toString());
            Optional<M_USER> user = mUserRepo.findByUsername(UserDetailUtils.getUsername());
            if (user.isPresent()) {
                R_GLOBAL_TYPE_VALUE rUserLevel = globalTypeValueService.getGlobalTypeByGlbValue(Constant.USER_LEVEL, user.get().getUserLevel());
                String userLevel = rUserLevel.getName() != null ? rUserLevel.getName() : "";
                auditTrail.setUserLevel(userLevel);
            } else {
                auditTrail.setUserLevel("");
            }
            auditTrail.setCreatedBy(user.get().getUsername());
            auditTrail.setCreatedDate(new Date());

            if (taxImplication.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())) {
                taxImplication.setStatus(FlowStatus.INACTIVE.name());
                taxImplication.setUpdatedDate(new Date());
                taxImplication.setUpdatedBy(UserDetailUtils.getUsername());
                taxImpliRepo.save(taxImplication);
                auditTrail.setOperation(FlowStatus.INACTIVE.name());
                auditTrail.setNewValue(taxImplication.toString());
                auditTrailRepo.save(auditTrail);
            } else if (taxImplication.getStatus().equalsIgnoreCase(FlowStatus.INACTIVE.name())) {
                taxImplication.setStatus(FlowStatus.ACTIVE.name());
                taxImplication.setUpdatedDate(new Date());
                taxImplication.setUpdatedBy(UserDetailUtils.getUsername());
                taxImpliRepo.save(taxImplication);
                auditTrail.setOperation(FlowStatus.ACTIVE.name());
                auditTrail.setNewValue(taxImplication.toString());
                auditTrailRepo.save(auditTrail);
            }

            LinkedHashMap<String, Object> successTosResponse = new LinkedHashMap<>();
            successTosResponse.put("lateChargeId", taxImplication.getId());
            successTosResponse.put("updatedDate", taxImplication.getUpdatedDate());
            successTosResponse.put("updatedBy", taxImplication.getUpdatedBy());

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success request " + taxImplication.getStatus().toLowerCase() + " late charge", successTosResponse);

            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> inactiveTaxImplicationRule(InactiveTaxImplicationDto request, HttpServletRequest httpServletRequest) {
        ResponseObject result;
        try {
            boolean isSubmiter = approvalServices.checkIsSubmitter(httpServletRequest, request.getAppHierId());
            if (!isSubmiter) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, CommonVariables.NOT_SUBMITTER,
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            Optional<M_AM_TAXIMPLICATION_RULE> findTaxImplicationRule = taxImpliRuleRepo.findById(request.getTaxImplicationRuleId());
            if (findTaxImplicationRule.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, ResponseUtils.MESSAGE_NOT_FOUND,
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            M_AM_TAXIMPLICATION_RULE taximplicationRuleData = findTaxImplicationRule.get();
            if (taximplicationRuleData.getStatus().equalsIgnoreCase(FlowStatus.INACTIVE.name())) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
                        "Tax Implication cannot be inactivated", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            if (!ObjectUtils.isEmpty(taximplicationRuleData.getApprovalStatus())) {
                if (taximplicationRuleData.getApprovalStatus().equalsIgnoreCase(ApprovalStatus.WAITING_APPROVAL.name())) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Tax Implication Rule cannot be inactivated because it has been on approval request", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
            } else {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Tax Implication rule approval status is null", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            JsonForUpdateLateChargeRuleDto jsonData = new JsonForUpdateLateChargeRuleDto();
            jsonData.setDescription(null);
            jsonData.setEndDate(request.getEndDate());
            String json = "";
            json = new Gson().toJson(jsonData);
            taximplicationRuleData.setJsonData(json);

            taximplicationRuleData.setUpdatedBy(UserDetailUtils.getUsername());
            taximplicationRuleData.setUpdatedDate(new Date());
            taximplicationRuleData.setApprovalStatus(ApprovalStatus.WAITING_APPROVAL.name());
            taxImpliRuleRepo.save(taximplicationRuleData);

            Integer tappId = approvalServices.addApprovalFlowR(request.getAppHierId(),
                    ApprovalCategory.INACTIVE_TAX_IMPLICATION_RULE.name(),
                    taximplicationRuleData.getId().toString(), request.getRemark());

            approvalServices.setApprovalHistory(tappId, taximplicationRuleData.getId(), request.getRemark(),
                    ApprovalCategory.TAX_IMPLICATION_RULE.name(), SUBMIT, httpServletRequest);

            LinkedHashMap<String, Object> successTosResponse = new LinkedHashMap<>();
            successTosResponse.put("taxChargeId", taximplicationRuleData.getTaximplicationId());
            successTosResponse.put("taxChargeRuleId", taximplicationRuleData.getId());
            successTosResponse.put("appHierId", request.getAppHierId());
            successTosResponse.put("description", request.getRemark());
            successTosResponse.put("updatedDate", taximplicationRuleData.getUpdatedDate());
            successTosResponse.put("updatedBy", taximplicationRuleData.getUpdatedBy());

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success request inactive tax charge", successTosResponse);

            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class, readOnly = false)
    public ResponseEntity<ResponseObject> approveInactiveTaxImplication(ApprovalTaxImplicationDto inactiveApprovalRequestDto, HttpServletRequest httpServletRequest) {
        ResponseObject result;
        try {
            Optional<M_AM_TAXIMPLICATION> findTaxImplication = taxImpliRepo.findById(inactiveApprovalRequestDto.getTaxImplicationId());
            if (findTaxImplication.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, CommonVariables.NOT_SUBMITTER,
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            M_AM_TAXIMPLICATION mAmTaximplication = findTaxImplication.get();
            boolean isForward = approvalServices.isForwardPosition(inactiveApprovalRequestDto.getApprovalId());
            if (isForward) {
               approvalServices.setApprHistoryForward(inactiveApprovalRequestDto.getApprovalId(), mAmTaximplication.getId(), inactiveApprovalRequestDto.getDescription(),
                APP_CATEGORY, inactiveApprovalRequestDto.getAction(), httpServletRequest);
            } else {
                approvalServices.setApprovalHistory(inactiveApprovalRequestDto.getApprovalId(), mAmTaximplication.getId(), inactiveApprovalRequestDto.getDescription(),
                APP_CATEGORY, inactiveApprovalRequestDto.getAction(), httpServletRequest);
            }
                
            if (inactiveApprovalRequestDto.getAction().equalsIgnoreCase(ApprovalStatus.REJECT.name())) {
                approvalServices.actionNextFlowApproval(inactiveApprovalRequestDto.getApprovalId(), inactiveApprovalRequestDto.getDescription(),
                        ApprovalStatus.REJECT);
                mAmTaximplication.setStatus(FlowStatus.REJECTED.name());
                mAmTaximplication.setUpdatedDate(new Date());
                mAmTaximplication.setUpdatedBy(UserDetailUtils.getUsername());
                taxImpliRepo.save(mAmTaximplication);
                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        "Tax Implication Inactive request has been rejected", null);
            } else {
                boolean isFinal = approvalServices.actionNextFlowApproval(inactiveApprovalRequestDto.getApprovalId(), inactiveApprovalRequestDto.getDescription(),
                        ApprovalStatus.APPROVE);
                if (isFinal) {
                    mAmTaximplication.setUpdatedDate(new Date());
                    mAmTaximplication.setUpdatedBy(UserDetailUtils.getUsername());
                    mAmTaximplication.setStatus(FlowStatus.INACTIVE.name());
                    taxImpliRepo.save(mAmTaximplication);
                }
                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        ResponseUtils.MESSAGE_SUCCESS, null);
            }


            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings({"java:S3776", "java:S1192"})
    @Transactional(rollbackFor = Exception.class, readOnly = false)
    public ResponseEntity<ResponseObject> approveInactiveTaxImplicationRule(ApprovalTaxImplicationDto inactiveTaxImplicationRuleApprovalRequestDto, HttpServletRequest httpServletRequest) {
        ResponseObject result;
        try {
            Optional<M_AM_TAXIMPLICATION_RULE> findTaxImplicationRule = taxImpliRuleRepo.findById(inactiveTaxImplicationRuleApprovalRequestDto.getTaxImplicationRuleId());
            if (findTaxImplicationRule.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, "Tax implication rule with id " + inactiveTaxImplicationRuleApprovalRequestDto.getTaxImplicationRuleId() + " not found!",
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            M_AM_TAXIMPLICATION_RULE taximplicationRule = findTaxImplicationRule.get();
            boolean isForward = approvalServices.isForwardPosition(inactiveTaxImplicationRuleApprovalRequestDto.getApprovalId());
                if (isForward) {
                    approvalServices.setApprHistoryForward(
                            inactiveTaxImplicationRuleApprovalRequestDto.getApprovalId(), 
                            taximplicationRule.getId(), 
                            inactiveTaxImplicationRuleApprovalRequestDto.getDescription(),
                            ApprovalCategory.TAX_IMPLICATION_RULE.name(), 
                            inactiveTaxImplicationRuleApprovalRequestDto.getAction(), 
                            httpServletRequest);

                } else {
                    approvalServices.setApprovalHistory(
                            inactiveTaxImplicationRuleApprovalRequestDto.getApprovalId(), 
                            taximplicationRule.getId(), 
                            inactiveTaxImplicationRuleApprovalRequestDto.getDescription(),
                            ApprovalCategory.TAX_IMPLICATION_RULE.name(), 
                            inactiveTaxImplicationRuleApprovalRequestDto.getAction(), 
                            httpServletRequest);
                }
            
            if (inactiveTaxImplicationRuleApprovalRequestDto.getAction().equalsIgnoreCase(ApprovalStatus.REJECT.name())) {
                approvalServices.actionNextFlowApproval(inactiveTaxImplicationRuleApprovalRequestDto.getApprovalId(), inactiveTaxImplicationRuleApprovalRequestDto.getDescription(),
                        ApprovalStatus.REJECT);
                taximplicationRule.setApprovalStatus(FlowStatus.REJECTED.name());
                taximplicationRule.setUpdatedDate(new Date());
                taximplicationRule.setUpdatedBy(UserDetailUtils.getUsername());
                taxImpliRuleRepo.save(taximplicationRule);
                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        "Your data has been rejected", null);
            } else {
                boolean isFinal = approvalServices.actionNextFlowApproval(inactiveTaxImplicationRuleApprovalRequestDto.getApprovalId(), inactiveTaxImplicationRuleApprovalRequestDto.getDescription(),
                        ApprovalStatus.APPROVE);
                if (isFinal) {

                    String jsonStr = taximplicationRule.getJsonData();
                    objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                    JsonForUpdateLateChargeRuleDto jsonObject = objectMapper.readValue(jsonStr, JsonForUpdateLateChargeRuleDto.class);
                    taximplicationRule.setEndDate(jsonObject.getEndDate() != null ? UtilsDate.stringToDate(jsonObject.getEndDate(), Constant.FORMAT_DATE_OTHER) : null);

                    taximplicationRule.setUpdatedDate(new Date());
                    taximplicationRule.setUpdatedBy(UserDetailUtils.getUsername());
                    taximplicationRule.setApprovalStatus(ApprovalStatus.APPROVED.name());
                    taximplicationRule.setStatus(FlowStatus.INACTIVE.name());
                    taxImpliRuleRepo.save(taximplicationRule);

                    List<M_AM_TAXIMPLICATION_RULE_OVR> dataOvr = mAmTaxImplicationRuleOvrRepo.findAllByTaximplicationRuleId(taximplicationRule.getId());

                    for (M_AM_TAXIMPLICATION_RULE_OVR mAmTaximplicationRuleOvr : dataOvr) {
                        mAmTaximplicationRuleOvr.setStatus(FlowStatus.INACTIVE.name());
                        mAmTaximplicationRuleOvr.setUpdatedBy(UserDetailUtils.getUsername());
                        mAmTaximplicationRuleOvr.setUpdatedDate(new Date());
                        mAmTaxImplicationRuleOvrRepo.save(mAmTaximplicationRuleOvr);

                        List<M_AM_TAXIMPLICATION_RULE_OVR_CONDITION> dataOvrCdt = mAmTaxImplicationRuleOvrConditionRepo.findAllByTaximplicationRuleOvrId(mAmTaximplicationRuleOvr.getId());
                        for (M_AM_TAXIMPLICATION_RULE_OVR_CONDITION data2 : dataOvrCdt) {
                            data2.setStatus(FlowStatus.INACTIVE.name());
                            data2.setUpdatedBy(UserDetailUtils.getUsername());
                            data2.setUpdatedDate(new Date());
                            mAmTaxImplicationRuleOvrConditionRepo.save(data2);
                        }
                    }

                }
                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        "Your data has been Approved", null);
            }
            
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }

    public ResponseEntity<ResponseObject> pagingTaxImplication(MaterialTablePagingRequest pagingData,
            PagedResourcesAssembler<VW_AM_TAXIMPLICATION> assembler) {
        ResponseObject result;
        try {
            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key + "~" + value);
                }
            }
            Page<VW_AM_TAXIMPLICATION> data = !pagingData.getSearch().isEmpty()
                    ? this.vwAmTaxImplicationRepo.findAll(this.vwAmTaxImplicationRepo.getSpecificationFromFilters(pagingData),
                            PagingUtils.getPaging(pagingData))
                    : this.vwAmTaxImplicationRepo.findAll(this.vwAmTaxImplicationRepo.getSpecificationDefault(),
                            PagingUtils.getPaging(pagingData));
            PagedModel<EntityModel<VW_AM_TAXIMPLICATION>> pagedData = assembler.toModel(data);
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_OK,
                    new PagingDTO(data.getContent(), pagedData.getMetadata(), pagedData.getLinks()));
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }

    @SuppressWarnings({"java:S3776", "java:S1192"})
    public ResponseEntity<ResponseObject> detailTaxImplication(MaterialTablePagingRequest pagingRequest, Integer id) {
        ResponseObject result;
        try {
            Optional<VW_AM_TAXIMPLICATION> vwAmTaxImp = vwAmTaxImplicationRepo.findTopById(id);
            if (vwAmTaxImp.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            VW_AM_TAXIMPLICATION data = vwAmTaxImp.get();

            DetailTaxImplicationDto responseData = new DetailTaxImplicationDto();
            responseData.setTaxImplicationId(data.getId());
            responseData.setName(data.getTaxImplicationName());
            GlobalTypeDTO getCategory = new GlobalTypeDTO();
            getCategory.setId(data.getCategoryId());
            getCategory.setName(data.getCategory());
            responseData.setCategory(getCategory);
            GlobalTypeDTO getServiceType = new GlobalTypeDTO();
            getServiceType.setId(data.getServiceTypeId());
            getServiceType.setName(data.getServiceType());
            responseData.setServiceType(getServiceType);
            responseData.setDescription(data.getDescription());
            responseData.setStatus(data.getStatus());

            List<M_AM_TAXIMPLICATION_CRITERIA> getCriteriaId = mTaxImplicationCriteriaRepo.findAllByTaximplicationId(data.getId());
            List<CriteriaListDTO> allListCriteria = new ArrayList<>();
            for (M_AM_TAXIMPLICATION_CRITERIA getId : getCriteriaId) {
                CriteriaListDTO cc = new CriteriaListDTO();
                cc.setId(getId.getId());
                cc.setValue(getId.getCriteriaId());
                Optional<R_GLOBAL_TYPE_VALUE> getLabelCriteria = rGlobalTypeValueRepo.findByGlbTypeValId(getId.getCriteriaId());
                cc.setLabel(getLabelCriteria.isPresent() ? getLabelCriteria.get().getName() : null);
                allListCriteria.add(cc);
            }

            responseData.setCriteria(allListCriteria);

            HistoryLogTaxImplicationDto historyLogTaxImplicationDto = new HistoryLogTaxImplicationDto();
            historyLogTaxImplicationDto.setId(data.getId());
            if (!ObjectUtils.isEmpty(data.getCreatedDate())) {
                historyLogTaxImplicationDto.setCreatedDate(UtilsDate.dateToString(data.getCreatedDate(), Constant.FORMAT_DATETIME_VIEW));
            }
            historyLogTaxImplicationDto.setCreatedBy(data.getCreatedBy());
            historyLogTaxImplicationDto.setUpdatedBy(data.getUpdatedBy());
            if (!ObjectUtils.isEmpty(data.getUpdatedDate())) {
                historyLogTaxImplicationDto.setUpdatedDate(UtilsDate.dateToString(data.getUpdatedDate(), Constant.FORMAT_DATETIME_VIEW));
            }
            responseData.setHistoryLog(historyLogTaxImplicationDto);

            Map<String, Object> filter = new HashMap<>();
            Page<VW_AM_TAXIMPLICATION_CRITERIA_DATA> datas = null;
            filter.put("taxImplicationId", data.getId());
            if (isNotBlank(pagingRequest.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingRequest.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingRequest.getSearch().add(key + "~" + value);
                }
            }
            Specification<VW_AM_TAXIMPLICATION_CRITERIA_DATA> specification = pagingRequest.getSearch().isEmpty() ? vwAmTaxImplicationCriteriaDataRepo.getSpecificationDefault(filter) : vwAmTaxImplicationCriteriaDataRepo.getSpecificationFromFilters(pagingRequest, filter);
            datas = vwAmTaxImplicationCriteriaDataRepo.findAll(specification, PagingUtils.getPaging(pagingRequest));

            List<CriteriaDataTaxImpliDTO> allData = datas.stream()
                    .map(g -> {
                        CriteriaDataTaxImpliDTO criteria = new CriteriaDataTaxImpliDTO();
                        criteria.setId(g.getId());
                        criteria.setTaxImplicationId(g.getTaxImplicationId());
                        GlobalTypeDTO acGroupType = new GlobalTypeDTO();
                        acGroupType.setId(g.getAccountGroupType());
                        acGroupType.setName(g.getAccountGroupTypeName());
                        criteria.setAccountGroupType(acGroupType);
                        GlobalTypeDTO acCategory = new GlobalTypeDTO();
                        acCategory.setId(g.getAccountCategory());
                        acCategory.setName(g.getAccountCategoryName());
                        criteria.setAccountCategory(acCategory);
                        GlobalTypeDTO acType = new GlobalTypeDTO();
                        acType.setId(g.getAccountType());
                        acType.setName(g.getAccountTypeName());
                        criteria.setAccountType(acType);
                        GlobalTypeDTO acSa = new GlobalTypeDTO();
                        acSa.setId(g.getSaType());
                        acSa.setName(g.getSaTypeName());
                        criteria.setSaType(acSa);
                        GlobalTypeDTO acCc = new GlobalTypeDTO();
                        acCc.setId(g.getCostCenter());
                        acCc.setName(g.getCostCenterName());
                        criteria.setCostCenter(acCc);
                        GlobalTypeDTO acNum = new GlobalTypeDTO();
                        acNum.setId(g.getAccountNumberId());
                        acNum.setName(g.getAccountNumber());
                        criteria.setAccountNumber(acNum);
                        GlobalTypeDTO acSeg = new GlobalTypeDTO();
                        acSeg.setId(g.getAccountSegment());
                        acSeg.setName(g.getAccountSegmentName());
                        criteria.setAccountSegment(acSeg);
                        GlobalTypeDTO acCla = new GlobalTypeDTO();
                        acCla.setId(g.getClassificationType());
                        acCla.setName(g.getClassificationTypeName());
                        criteria.setClassificationType(acCla);
                        BooleanDTO acCorp = new BooleanDTO();
                        acCorp.setId(g.getCorporateFlagBoolean());
                        acCorp.setName(g.getCorporateFlag()!=null?g.getCorporateFlag():null);
                        criteria.setCorporateFlag(acCorp);
                        criteria.setAllCriteria(g.getAllCriteria()!=null?g.getAllCriteria():null);
                        GlobalTypeDTO acSor = new GlobalTypeDTO();
                        acSor.setId(g.getSor());
                        acSor.setName(g.getSorName());
                        criteria.setSor(acSor);
                        BooleanDTO acWapu = new BooleanDTO();
                        acWapu.setId(g.getWapuFlagBoolean());
                        acWapu.setName(g.getWapuFlag()!=null?g.getWapuFlag():null);
                        criteria.setWapuFlag(acWapu);
                        GlobalTypeDTO acCity = new GlobalTypeDTO();
                        acCity.setId(g.getCITY());
                        acCity.setName(g.getCITYNAME());
                        criteria.setPremiseCity(acCity);
                        GlobalTypeDTO acSub = new GlobalTypeDTO();
                        acSub.setId(g.getSUBDISTRICT());
                        acSub.setName(g.getSUBDISTRICTNAME());
                        criteria.setPremiseSubdistrict(acSub);
                        GlobalTypeDTO acDis = new GlobalTypeDTO();
                        acDis.setId(g.getDISTRICT());
                        acDis.setName(g.getDISTRICTNAME());
                        criteria.setPremiseDistrict(acDis);
                        GlobalTypeDTO acProv = new GlobalTypeDTO();
                        acProv.setId(g.getPROVINCE());
                        acProv.setName(g.getPROVINCENAME());
                        criteria.setPremiseProvince(acProv);
                        GlobalTypeDTO acCoun = new GlobalTypeDTO();
                        acCoun.setId(g.getCOUNTRY());
                        acCoun.setName(g.getCOUNTRYNAME());
                        criteria.setPremiseCountry(acCoun);
                        if (!ObjectUtils.isEmpty(g.getStartDate())) {
                            criteria.setStartDate(UtilsDate.dateToString(g.getStartDate(), Constant.FORMAT_START_END_DATE));
                        }
                        if (!ObjectUtils.isEmpty(g.getEndDate())) {
                            criteria.setEndDate(UtilsDate.dateToString(g.getEndDate(), Constant.FORMAT_START_END_DATE));
                        }
                        criteria.setDescription(g.getDescription());
                        criteria.setCreatedDate(g.getCreatedDate());
                        criteria.setUpdatedDate(g.getUpdatedDate());
                        criteria.setCreatedBy(g.getCreatedBy());
                        criteria.setUpdatedBy(g.getUpdatedBy());
                        return criteria;
                    })
                    .collect(Collectors.toList());

//            List<LinkedHashMap<String, Object>> allData = datas.stream()
//                    .map(g -> {
//                        LinkedHashMap<String, Object> acontact = new LinkedHashMap<>();
//                        acontact.put("id", g.getId());
//                        acontact.put("status", g.getStatus());
//                        acontact.put("createdDate", g.getCreatedDate());
//                        GlobalTypeDTO acGroupType = new GlobalTypeDTO();
//                        acGroupType.setId(g.getAccountGroupType());
//                        acGroupType.setName(g.getAccountGroupTypeName());
//                        acontact.put("accountGroupType", acGroupType);
//                        acontact.put("taxImplicationId", g.getTaxImplicationId());
//                        GlobalTypeDTO acCategory = new GlobalTypeDTO();
//                        acCategory.setId(g.getAccountCategory());
//                        acCategory.setName(g.getAccountCategoryName());
//                        acontact.put("accountCategory", acCategory);
//                        GlobalTypeDTO acType = new GlobalTypeDTO();
//                        acType.setId(g.getAccountType());
//                        acType.setName(g.getAccountTypeName());
//                        acontact.put("accountType", acType);
//                        GlobalTypeDTO acSa = new GlobalTypeDTO();
//                        acSa.setId(g.getSaType());
//                        acSa.setName(g.getSaTypeName());
//                        acontact.put("saType", acSa);
//                        GlobalTypeDTO acCc = new GlobalTypeDTO();
//                        acCc.setId(g.getCostCenter());
//                        acCc.setName(g.getCostCenterName());
//                        acontact.put("costCenter", acCc);
//                        GlobalTypeDTO acNum = new GlobalTypeDTO();
//                        acNum.setId(g.getAccountNumberId());
//                        acNum.setName(g.getAccountNumber());
//                        acontact.put("accountNumber", acNum);
//                        GlobalTypeDTO acSeg = new GlobalTypeDTO();
//                        acSeg.setId(g.getAccountSegment());
//                        acSeg.setName(g.getAccountSegmentName());
//                        acontact.put("accountSegment", acSeg);
//                        GlobalTypeDTO acCla = new GlobalTypeDTO();
//                        acCla.setId(g.getClassificationType());
//                        acCla.setName(g.getClassificationTypeName());
//                        acontact.put("classificationType", acCla);
//                        BooleanDTO acCorp = new BooleanDTO();
//                        acCorp.setId(g.getCorporateFlagBoolean());
//                        acCorp.setName(g.getCorporateFlag());
//                        acontact.put("corporateFlag", acCorp);
//                        acontact.put("allCriteria", g.getAllCriteria() != null ? g.getAllCriteria() : null);
//                        GlobalTypeDTO acSor = new GlobalTypeDTO();
//                        acSor.setId(g.getSor());
//                        acSor.setName(g.getSorName());
//                        acontact.put("sor", acSor);
//                        BooleanDTO acWapu = new BooleanDTO();
//                        acWapu.setId(g.getWapuFlagBoolean());
//                        acWapu.setName(g.getWapuFlag());
//                        acontact.put("wapuFlag", acWapu);
//                        GlobalTypeDTO acCity = new GlobalTypeDTO();
//                        acCity.setId(g.getCITY());
//                        acCity.setName(g.getCITYNAME());
//                        acontact.put("premiseCity", acCity);
//                        GlobalTypeDTO acSub = new GlobalTypeDTO();
//                        acSub.setId(g.getSUBDISTRICT());
//                        acSub.setName(g.getSUBDISTRICTNAME());
//                        acontact.put("premiseSubdistrict", acSub);
//                        GlobalTypeDTO acProv = new GlobalTypeDTO();
//                        acProv.setId(g.getPROVINCE());
//                        acProv.setName(g.getPROVINCENAME());
//                        acontact.put("premiseProvince", acProv);
//                        GlobalTypeDTO acCoun = new GlobalTypeDTO();
//                        acCoun.setId(g.getCOUNTRY());
//                        acCoun.setName(g.getCOUNTRYNAME());
//                        acontact.put("premiseCountry", acCoun);
//                        GlobalTypeDTO acDis = new GlobalTypeDTO();
//                        acDis.setId(g.getDISTRICT());
//                        acDis.setName(g.getDISTRICTNAME());
//                        acontact.put("premiseDistrict", acDis);
//                        return acontact;
//                    })
//                    .collect(Collectors.toList());

            if (!datas.isEmpty()) {
                responseData.setTaxImplicationCriterias(allData);
            }

            //acr
            List<AUDIT_TRAIL> auditTrail = auditTrailRepo.findAll().stream()
                    .filter(e -> e.getTableName().equalsIgnoreCase("M_AM_LATECHARGE"))
                    .filter(f -> f.getDataId().equalsIgnoreCase(data.getId().toString()))
                    .collect(Collectors.toList());
            responseData.setActiveInactiveLog(auditTrail);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_OK, responseData);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }

    public ResponseEntity<ResponseObject> viewTaxImplicationRule(MaterialTablePagingRequest pagingRequest,
            PagedResourcesAssembler<M_AM_TAXIMPLICATION_RULE> assembler,
            Integer taxImplicationId) {
        ResponseObject result;
        try {
            Page<M_AM_TAXIMPLICATION_RULE> datas = null;
            Map<String, Object> filter = new HashMap<>();
            Optional<M_AM_TAXIMPLICATION> findTaxImplication = taxImpliRepo.findById(taxImplicationId);
            if (findTaxImplication.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            M_AM_TAXIMPLICATION latecharge = findTaxImplication.get();
            if (isNotBlank(pagingRequest.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingRequest.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingRequest.getSearch().add(key + "~" + value);
                }
            }
            filter.put("taximplicationId", latecharge.getId());
            Specification<M_AM_TAXIMPLICATION_RULE> specification = pagingRequest.getSearch().isEmpty() ? taxImpliRuleRepo.getSpecificationDefault(filter) : taxImpliRuleRepo.getSpecificationFromFilters(pagingRequest, filter);
            datas = taxImpliRuleRepo.findAll(specification, PagingUtils.getPaging(pagingRequest));
            PagedModel<EntityModel<M_AM_TAXIMPLICATION_RULE>> pagedData = assembler.toModel(datas);
            List<ViewTaxImplicationRuleDto> responseDatas = new ArrayList<>();
            if (!datas.isEmpty()) {
                for (M_AM_TAXIMPLICATION_RULE data : datas.getContent()) {
                    ViewTaxImplicationRuleDto responseData = new ViewTaxImplicationRuleDto();
                    responseData.setId(data.getId());
                    if (!ObjectUtils.isEmpty(data.getStartDate())) {
                        responseData.setStartDate(UtilsDate.dateToString(data.getStartDate(), Constant.FORMAT_START_END_DATE));
                    }
                    if (!ObjectUtils.isEmpty(data.getEndDate())) {
                        responseData.setEndDate(UtilsDate.dateToString(data.getEndDate(), Constant.FORMAT_START_END_DATE));
                    }
                    responseData.setTransCode(data.getTransCode());
                    Optional<M_RBI_FACTURE_CODE> codeOpt = mRbiFactureCodeRepo.findById(data.getTransCode());
                    responseData.setTransCodeName(codeOpt.isPresent() ? codeOpt.get().getCode() : "");
                    responseData.setIsVatInv(data.getIsVatInv().equalsIgnoreCase("Y") ? "Yes" : "No");
                    responseData.setDocumentNumber(data.getDocumentNumber());
                    responseData.setImplicationType(data.getImplicationType());
                    responseData.setIsGunggung(data.getIsGunggung().equalsIgnoreCase("Y") ? "Yes" : "No");
                    responseData.setDescription(data.getDescription());
                    responseData.setStatus(data.getStatus());
                    responseData.setApprovalStatus(data.getApprovalStatus());
                    responseDatas.add(responseData);
                }
            }

            Map<String, Object> detailLateChargeRuleResponse = new HashMap<>();

            detailLateChargeRuleResponse.put("result", responseDatas);
            detailLateChargeRuleResponse.put("page", pagedData.getMetadata());
            detailLateChargeRuleResponse.put("links", pagedData.getLinks());

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Detail Late Charge", detailLateChargeRuleResponse);
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }

    public ResponseEntity<ResponseObject> listCriteria() {
        ResponseObject result;
        try {
            List<LinkedHashMap<String, Object>> allData = criteriaServices.getCriteriaByGlobalType(Constant.CRITERIA_LATE_CHARGE_NAME);
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_OK, allData);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings({"java:S3776", "java:S1192"})
    public ResponseEntity<ResponseObject> detailTaxImplicationRule(Integer taxRuleId, HttpServletRequest request) {
        ResponseObject result;
        try {
            Optional<M_AM_TAXIMPLICATION_RULE> findTaxRule = taxImpliRuleRepo.findById(taxRuleId);
            if (findTaxRule.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "id tax impli rule " + findTaxRule + " is not found", null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            M_AM_TAXIMPLICATION_RULE taxRule = findTaxRule.get();
            LinkedHashMap<String, Object> detailResponse = new LinkedHashMap<>();
            detailResponse.put("taxImplicationId", taxRule.getTaximplicationId());
            detailResponse.put("taxImplicationRuleId", taxRule.getId());
            detailResponse.put("documentNumber", taxRule.getDocumentNumber());
            GlobalTypeDTO impliType = new GlobalTypeDTO();
            impliType.setId(taxRule.getImplicationTypeId());
            impliType.setName(taxRule.getImplicationType());
            detailResponse.put("implicationType", impliType);
            detailResponse.put("isVatInv", taxRule.getIsVatInv());
            detailResponse.put("isGunggung", taxRule.getIsGunggung());
            detailResponse.put("transCode", taxRule.getTransCode());
            Optional<M_RBI_FACTURE_CODE> codeOpt = mRbiFactureCodeRepo.findById(taxRule.getTransCode());
            detailResponse.put("transCodeName", codeOpt.isPresent() ? codeOpt.get().getCode() : "");
            if (!ObjectUtils.isEmpty(taxRule.getStartDate())) {
                detailResponse.put("startDate", UtilsDate.dateToString(taxRule.getStartDate(), Constant.FORMAT_START_END_DATE));
            }
            if (!ObjectUtils.isEmpty(taxRule.getEndDate())) {
                detailResponse.put("endDate", UtilsDate.dateToString(taxRule.getEndDate(), Constant.FORMAT_START_END_DATE));
            } else {
                detailResponse.put("endDate", null);
            }
            detailResponse.put("status", taxRule.getStatus());
            detailResponse.put("approvalStatus", taxRule.getApprovalStatus());
            detailResponse.put("description", taxRule.getDescription());

            // LIST RULE OVERRIDE
            List<M_AM_TAXIMPLICATION_RULE_OVR> ruleOverride = mAmTaxImplicationRuleOvrRepo.findAllByTaximplicationRuleId(taxRule.getId());
            List<LinkedHashMap<String, Object>> dataRuleOverride = ruleOverride.stream()
                    .map(g -> {
                        LinkedHashMap<String, Object> ruleOvr = new LinkedHashMap<>();
                        ruleOvr.put("id", g.getId());
                        ruleOvr.put("taxImplicationRuleId", g.getTaximplicationRuleId());
                        GlobalTypeDTO impliTypeOvr = new GlobalTypeDTO();
                        impliTypeOvr.setId(g.getImplicationTypeOvr());
                        impliTypeOvr.setName(g.getImplicationTypeOvrValue());
                        ruleOvr.put("implicationType", impliTypeOvr);
                        ruleOvr.put("transCode", g.getTransCodeOvr());
                        Optional<M_RBI_FACTURE_CODE> codeOptOvr = mRbiFactureCodeRepo.findById(g.getTransCodeOvr());
                        ruleOvr.put("transCodeName", codeOptOvr.isPresent() ? codeOptOvr.get().getCode() : "");
                        ruleOvr.put("description", g.getDescription());
                        ruleOvr.put("status", g.getStatus());
                        ruleOvr.put("createdDate", g.getCreatedDate());
                        ruleOvr.put("updatedDate", g.getUpdatedDate());
                        ruleOvr.put("createdBy", g.getCreatedBy());
                        ruleOvr.put("updatedBy", g.getUpdatedBy());

                        NumberFormat formatIDR = NumberFormat.getCurrencyInstance(new Locale("en", "ID"));
                        // LIST RULE OVERRIDE CONDITION
                        List<M_AM_TAXIMPLICATION_RULE_OVR_CONDITION> ruleOverrideCondition = mAmTaxImplicationRuleOvrConditionRepo.findAllByTaximplicationRuleOvrId(g.getId());
                        List<LinkedHashMap<String, Object>> dataRuleOverrideCondition = ruleOverrideCondition.stream()
                                .map(s -> {
                                    LinkedHashMap<String, Object> ruleOvrCdt = new LinkedHashMap<>();
                                    ruleOvrCdt.put("id", s.getId());
                                    ruleOvrCdt.put("taximplicationRuleOvrId", s.getTaximplicationRuleOvrId());
                                    GlobalTypeDTO nameValue = new GlobalTypeDTO();
                                    nameValue.setId(s.getName());
                                    nameValue.setName(s.getNameValue());
                                    ruleOvrCdt.put("name", nameValue);
                                    GlobalTypeDTO operatorValue = new GlobalTypeDTO();
                                    operatorValue.setId(s.getOperator());
                                    operatorValue.setName(s.getOperatorValue());
                                    ruleOvrCdt.put("operator", operatorValue);
                                    if (s.getName().equals(2246) || s.getName().equals(2247) || s.getName().equals(2249)) {
                                        Double val = Double.parseDouble(s.getValue().replaceAll(",", ""));
                                        ruleOvrCdt.put("value", formatIDR.format(val).replaceAll("IDR", ""));
                                    } else {
                                        ruleOvrCdt.put("value", s.getValue());
                                    }
                                    ruleOvrCdt.put("status", s.getStatus());
                                    ruleOvrCdt.put("createdDate", s.getCreatedDate());
                                    ruleOvrCdt.put("updatedDate", s.getUpdatedDate());
                                    ruleOvrCdt.put("createdBy", s.getCreatedBy());
                                    ruleOvrCdt.put("updatedBy", s.getUpdatedBy());
                                    return ruleOvrCdt;
                                })
                                .collect(Collectors.toList());

                        ruleOvr.put("listRuleOverrideCondition", dataRuleOverrideCondition);

                        return ruleOvr;
                    })
                    .collect(Collectors.toList());

            detailResponse.put("listRuleOverride", dataRuleOverride);

            // LIST ATTACHMENT
            Optional<List<M_ATTACHMENT>> attach = mAttachmentRepo
                    .findByReferenceIdAndIsDeletedAndCategoryIgnoreCase(taxRule.getId(), Boolean.FALSE, "TAXIMPLICATION_RULE_ATTACHMENT");
            List<AttachmentListDto> attachRule = new ArrayList<>();
            if (attach.isPresent()) {
                M_GLOBAL_PROPERTIES mGlobalPropUrl = mGlobalPropertiesRepo.findAllByName("PATH_URL");
                Optional<R_GLOBAL_PROPERTIES_DTL> rgpUrlViewFile = mGlobalPropUrl.getRGlobalPropertiesDtls().stream()
                        .filter(e -> e.getGpdKey().equalsIgnoreCase("URL_VIEW_FILE"))
                        .findFirst();
                String urlViewFile = rgpUrlViewFile.isPresent() ? rgpUrlViewFile.get().getGpdVal() : "";

                for (M_ATTACHMENT dtl_ : attach.get()) {
                    AttachmentListDto attachDto = this.objectMapper.convertValue(dtl_, AttachmentListDto.class);

                    attachDto.setFileType(dtl_.getType());
                    attachDto.setUrlFile1("/v1/dbs/api/master/late-charge/download2/" + dtl_.getId().toString());
                    attachDto.setUrlFile2(urlViewFile + dtl_.getId());
                    attachDto.setFileCategoryName(dtl_.getFileCategoryName());
                    attachDto.setCreatedDate(dtl_.getCreatedDate());
                    attachDto.setCreatedBy(UserDetailUtils.getUsername());
                    attachRule.add(attachDto);
                }
            }
            detailResponse.put("attachments", attachRule);
            detailResponse.put("createdDate", taxRule.getCreatedDate());
            detailResponse.put("createdBy", taxRule.getCreatedBy());
            detailResponse.put("updatedDate", taxRule.getUpdatedDate());
            detailResponse.put("updatedBy", taxRule.getUpdatedBy());

            // CURRENT APPROVAL ID
            List<String> appCategory = new ArrayList<>();
            appCategory.add(ApprovalCategory.TAX_IMPLICATION_RULE.name());
            appCategory.add(ApprovalCategory.INACTIVE_TAX_IMPLICATION_RULE.name());

            Optional<T_APPROVAL> appId = tApprovalRepo.findFirstByIdTransAndCategoryInAndStatus(taxRuleId.toString(),
                    appCategory, ApprovalStatus.WAITING_FOR_APPROVAL.name());

            if (appId.isPresent()) {
                // change get isapprover and add isapprover from m_forward_task
                Optional<VW_HIER_FLOW> appHierDtlOpt = vWHierFlowRepo.findBytAppId(appId.get().getTAppId());

                if (appHierDtlOpt.isPresent()) {
                    VW_HIER_FLOW appHierDtl = appHierDtlOpt.get();
                    if (StringUtils.hasValue(appHierDtl.getForwardTo()) && appHierDtl.getTaskForward() > 0) {
                        Optional<M_USER> mUser = mUserRepo.findByUsername(UserDetailUtils.getUsernameFromToken(request));
                        String employeeCode = "";
                        if (mUser.isPresent()) {
                            employeeCode = mUser.get().getEmployee().getEmployeeCode();
                        }
                        if (appHierDtl.getForwardTo().equals(UserDetailUtils.getPositionFromToken(request))
                                    && (StringUtils.hasValue(appHierDtl.getEmployeeForward()) 
                                    && appHierDtl.getEmployeeForward().equalsIgnoreCase(employeeCode))) {
                            detailResponse.put("apphierId", appHierDtl.getAppHierId());
                            detailResponse.put("tAppId", appId.get().getTAppId());
                            detailResponse.put("isApprover", Boolean.TRUE);
                        } else {
                            detailResponse.put("apphierId", appHierDtl.getAppHierId());
                            detailResponse.put("isApprover", Boolean.FALSE);
                            detailResponse.put("tAppId", null);
                        }
                    } else {
                        if (appHierDtl.getPositionId().equals(UserDetailUtils.getPositionFromToken(request))) {
                            detailResponse.put("apphierId", appHierDtl.getAppHierId());
                            detailResponse.put("tAppId", appId.get().getTAppId());
                            detailResponse.put("isApprover", Boolean.TRUE);
                        } else {
                            detailResponse.put("apphierId", appHierDtl.getAppHierId());
                            detailResponse.put("isApprover", Boolean.FALSE);
                            detailResponse.put("tAppId", null);
                        }
                    }
                } else {
                    Optional<M_APPROVAL_HIERARCHY_DTL> mappHierDtlOpt = mApprovalHierarchyDtlRepo
                        .findFirstByAppHierIdAndApprovalLevel(appId.get().getAppHierId(),
                                appId.get().getApprovalLevel());
                    if (mappHierDtlOpt.isPresent()) {
                        M_APPROVAL_HIERARCHY_DTL appHierDtl = mappHierDtlOpt.get();
                        if (appHierDtl.getPositionId().equals(UserDetailUtils.getPositionFromToken(request))) {
                            detailResponse.put("apphierId", appHierDtl.getAppHierId());
                            detailResponse.put("tAppId", appId.get().getTAppId());
                            detailResponse.put("isApprover", Boolean.TRUE);
                        } else {
                            detailResponse.put("apphierId", appHierDtl.getAppHierId());
                            detailResponse.put("isApprover", Boolean.FALSE);
                            detailResponse.put("tAppId", null);
                        }
                    }
                }

                // getApproval detail
                Integer appHist = tApprovalHistoryRepo.findTopSubmittedByRefIdAndCategory(taxRuleId,
                        ApprovalCategory.TAX_IMPLICATION_RULE.name());

                Optional<T_APPROVAL_HISTORY> dtlAppOpt = tApprovalHistoryRepo.findFirstBytAppId(appHist);

                if (dtlAppOpt.isPresent()) {

                    T_APPROVAL_HISTORY dtlApp = dtlAppOpt.get();

                    ApprovalHeaderViewDto appDtl = new ApprovalHeaderViewDto();
                    appDtl.setRequestedBy(dtlApp.getFullName());
                    appDtl.setRequestedDate(dtlApp.getEventDate());
                    appDtl.setRemarks(dtlApp.getDescription());
                    appDtl.setApprovalType(dtlApp.getApprovalType());

                    detailResponse.put("approvalDetail", appDtl);
                    detailResponse.put("approvalType", dtlApp.getApprovalType());
                }

            } else {
                detailResponse.put("apphierId", taxRule.getApphierId());
                detailResponse.put("isApprover", Boolean.FALSE);
                detailResponse.put("tAppId", null);
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Detail Tax Implication Rule", detailResponse);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getAllApprovalHeaderList(HttpServletRequest httpServletRequest) {
        try {
            var mApprovalHierarchies = apphierRepo.findApprovalHierarchyByPositionAndType(UserDetailUtils.getPositionFromToken(httpServletRequest), "Y", "APR_ACC_TAX_IMP_RULE");
            List<ApprovalHierarchyDto> approvals = new ArrayList<>();
            if (!mApprovalHierarchies.isEmpty()) {
                for (M_APPROVAL_HIERARCHY mApprovalHierarchy : mApprovalHierarchies) {
                    approvals.add(ApprovalHierarchyDto.builder()
                            //                            .appHierCode(mApprovalHierarchy.getApprovalCode())
                            .appHierId(mApprovalHierarchy.getAppHierId())
                            .approvalName(mApprovalHierarchy.getApprovalName())
                            .approvalType(mApprovalHierarchy.getApprovalType())
                            .desc(mApprovalHierarchy.getDesc())
                            .entityId(mApprovalHierarchy.getEntityId())
                            .build());
                }
            }

            logger.info("data approvals : {}", approvals);

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
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> detailTaxImpliRuleDraft(Integer taxImplicationRuleId) {
        ResponseObject result;
        Map<String, Object> detailResponse = new HashMap<>();
        try {
            Optional<M_AM_TAXIMPLICATION_RULE> findRule = taxImpliRuleRepo.findById(taxImplicationRuleId);
            if (findRule.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            M_AM_TAXIMPLICATION_RULE taxImpliRule = findRule.get();

            if (taxImpliRule.getJsonData() == null) {
                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        "Temporary data for TaxImpli Rule with id : " + taxImplicationRuleId + ResponseUtils.MESSAGE_NOT_FOUND,
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            if (!taxImpliRule.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())
                    && !(taxImpliRule.getApprovalStatus().equalsIgnoreCase("DRAFT")
                    || taxImpliRule.getApprovalStatus().equalsIgnoreCase("WAITING FOR APPROVAL"))) {
                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        "Status Tax Impli must be ACTIVE to view draft data", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            if (!ObjectUtils.isEmpty(taxImpliRule.getJsonData())) {
                String jsonStr = taxImpliRule.getJsonData();
                objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                JsonForUpdateLateChargeRuleDto jsonForUpdateLateChargeRuleDto = objectMapper.readValue(jsonStr, JsonForUpdateLateChargeRuleDto.class);
                detailResponse.put("description", jsonForUpdateLateChargeRuleDto.getDescription());
                detailResponse.put("apphierId", taxImpliRule.getApphierId());
                detailResponse.put("taxImplicationRuleId", taxImpliRule.getId());

                Optional<List<M_ATTACHMENT>> saAttachments = mAttachmentRepo.findByReferenceIdAndCategoryIgnoreCase(taxImpliRule.getId(), ApprovalCategory.LATE_CHARGE_RULE.name());
                if (saAttachments.isPresent()) {
                    detailResponse.put("attachments", saAttachments.get());
                }
            }

            detailResponse.put("documentNumber", taxImpliRule.getDocumentNumber());
            detailResponse.put("implicationType", taxImpliRule.getImplicationType());
            detailResponse.put("isVatInv", taxImpliRule.getIsVatInv());
            detailResponse.put("isGunggung", taxImpliRule.getIsGunggung());
            detailResponse.put("transCode", taxImpliRule.getTransCode());
            Optional<M_RBI_FACTURE_CODE> codeOpt = mRbiFactureCodeRepo.findById(taxImpliRule.getTransCode());
            detailResponse.put("transCodeName", codeOpt.isPresent() ? codeOpt.get().getCode() : "");
            detailResponse.put("startDate", !ObjectUtils.isEmpty(taxImpliRule.getStartDate()) ? UtilsDate.dateToString(taxImpliRule.getStartDate(), Constant.FORMAT_START_END_DATE) : null);
            detailResponse.put("endDate", !ObjectUtils.isEmpty(taxImpliRule.getEndDate()) ? UtilsDate.dateToString(taxImpliRule.getEndDate(), Constant.FORMAT_START_END_DATE) : null);
            detailResponse.put("status", taxImpliRule.getStatus());
            detailResponse.put("approvalStatus", taxImpliRule.getApprovalStatus());

//            List<M_AM_TAXIMPLICATION_RULE_OVR> dataRuleOvr = mAmTaxImplicationRuleOvrRepo.findAllByTaximplicationRuleId(taxImpliRule.getId());
//            List<LinkedHashMap<String, Object>> allOvr = new ArrayList<>();
//            for (M_AM_TAXIMPLICATION_RULE_OVR pp : dataRuleOvr) {
//                LinkedHashMap<String, Object> dOvr = new LinkedHashMap<>();
//                dOvr.put("id", pp.getId());
//                dOvr.put("taxImplicationRuleId", pp.getTaximplicationRuleId());
//                dOvr.put("implicationType", pp.getImplicationTypeOvr());
//                dOvr.put("transCode", pp.getTransCodeOvr());
//                dOvr.put("description", pp.getDescription());
//                dOvr.put("createdDate", pp.getCreatedDate());awda
//                dOvr.put("createdBy", pp.getCreatedBy());
//                dOvr.put("updatedDate", pp.getUpdatedDate());
//                dOvr.put("updatedBy", pp.getUpdatedBy());
//                List<M_AM_TAXIMPLICATION_RULE_OVR_CONDITION> dataRuleOvrCdt = mAmTaxImplicationRuleOvrConditionRepo.findAllByTaximplicationRuleOvrId(pp.getId());
//                dOvr.put("listRuleOverrideCondition", dataRuleOvrCdt);
//                allOvr.add(dOvr);
//            }
//            detailResponse.put("listRuleOverride", allOvr);

            // LIST RULE OVERRIDE
            List<M_AM_TAXIMPLICATION_RULE_OVR> ruleOverride = mAmTaxImplicationRuleOvrRepo.findAllByTaximplicationRuleId(taxImpliRule.getId());
            List<LinkedHashMap<String, Object>> dataRuleOverride = ruleOverride.stream()
                    .map(g -> {
                        LinkedHashMap<String, Object> ruleOvr = new LinkedHashMap<>();
                        ruleOvr.put("id", g.getId());
                        ruleOvr.put("taxImplicationRuleId", g.getTaximplicationRuleId());
                        GlobalTypeDTO impliTypeOvr = new GlobalTypeDTO();
                        impliTypeOvr.setId(g.getImplicationTypeOvr());
                        impliTypeOvr.setName(g.getImplicationTypeOvrValue());
                        ruleOvr.put("implicationType", impliTypeOvr);
                        ruleOvr.put("transCode", g.getTransCodeOvr());
                        ruleOvr.put("description", g.getDescription());
                        ruleOvr.put("status", g.getStatus());
                        ruleOvr.put("createdDate", g.getCreatedDate());
                        ruleOvr.put("updatedDate", g.getUpdatedDate());
                        ruleOvr.put("createdBy", g.getCreatedBy());
                        ruleOvr.put("updatedBy", g.getUpdatedBy());

                        NumberFormat formatIDR = NumberFormat.getCurrencyInstance(new Locale("en", "ID"));
                        // LIST RULE OVERRIDE CONDITION
                        List<M_AM_TAXIMPLICATION_RULE_OVR_CONDITION> ruleOverrideCondition = mAmTaxImplicationRuleOvrConditionRepo.findAllByTaximplicationRuleOvrId(g.getId());
                        List<LinkedHashMap<String, Object>> dataRuleOverrideCondition = ruleOverrideCondition.stream()
                                .map(s -> {
                                    LinkedHashMap<String, Object> ruleOvrCdt = new LinkedHashMap<>();
                                    ruleOvrCdt.put("id", s.getId());
                                    ruleOvrCdt.put("taximplicationRuleOvrId", s.getTaximplicationRuleOvrId());
                                    GlobalTypeDTO nameValue = new GlobalTypeDTO();
                                    nameValue.setId(s.getName());
                                    nameValue.setName(s.getNameValue());
                                    ruleOvrCdt.put("name", nameValue);
                                    GlobalTypeDTO operatorValue = new GlobalTypeDTO();
                                    operatorValue.setId(s.getOperator());
                                    operatorValue.setName(s.getOperatorValue());
                                    ruleOvrCdt.put("operator", operatorValue);
                                    if (s.getName().equals(2246) || s.getName().equals(2247) || s.getName().equals(2249)) {
                                        Double val = Double.parseDouble(s.getValue().replaceAll(",", ""));
                                        ruleOvrCdt.put("value", formatIDR.format(val).replaceAll("IDR", ""));
                                    } else {
                                        ruleOvrCdt.put("value", s.getValue());
                                    }
                                    ruleOvrCdt.put("status", s.getStatus());
                                    ruleOvrCdt.put("createdDate", s.getCreatedDate());
                                    ruleOvrCdt.put("updatedDate", s.getUpdatedDate());
                                    ruleOvrCdt.put("createdBy", s.getCreatedBy());
                                    ruleOvrCdt.put("updatedBy", s.getUpdatedBy());
                                    return ruleOvrCdt;
                                })
                                .collect(Collectors.toList());

                        ruleOvr.put("listRuleOverrideCondition", dataRuleOverrideCondition);

                        return ruleOvr;
                    })
                    .collect(Collectors.toList());

            detailResponse.put("listRuleOverride", dataRuleOverride);

            Optional<List<M_ATTACHMENT>> lateChargeRuleAttachment = mAttachmentRepo.findByReferenceIdAndIsDraftAndIsDeletedAndCategoryIgnoreCase(taxImpliRule.getId(), Boolean.TRUE, Boolean.FALSE, "TAXIMPLICATION_RULE_ATTACHMENT");
            if (lateChargeRuleAttachment.isEmpty()) {
                detailResponse.put("attachments", new ArrayList<Object>());
            } else {
                detailResponse.put("attachments", lateChargeRuleAttachment.get());
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Detail Late Charge Rule", detailResponse);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<InputStreamResource> downloadFilter(MaterialTablePagingRequest pagingData) {
        try {
            Page<VW_AM_TAXIMPLICATION> data;
            Map<String, Object> filter = new HashMap<>();
            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key + "~" + value);
                }
            }
            if (pagingData.getPage() != null && pagingData.getSize() != null) {
                long maxData = StreamSupport.stream(vwAmTaxImplicationRepo.findAll().spliterator(), false).count();
                pagingData.setSize((int) maxData);
                pagingData.setPage(1);
            }
            if (!pagingData.getSearch().isEmpty()) {
                data = this.vwAmTaxImplicationRepo.findAll(this.vwAmTaxImplicationRepo.getSpecificationFromFiltersAccount(pagingData, filter),
                        PagingUtils.getPaging(pagingData));
            } else {
                data = this.vwAmTaxImplicationRepo.findAll(this.vwAmTaxImplicationRepo.getSpecificationDefaultAccount(filter),
                        PagingUtils.getPaging(pagingData));
            }
            List<VW_AM_TAXIMPLICATION> listAction = data.getContent();
            List<LinkedHashMap<String, Object>> allData = new ArrayList<>();
            Integer no = 1;
            HttpHeaders headers = new HttpHeaders();
            if(data.hasContent()) {
                for (VW_AM_TAXIMPLICATION a : listAction) {
                    StringBuilder criteriaValue = new StringBuilder();
                    List<M_AM_TAXIMPLICATION_RULE> dataRule = taxImpliRuleRepo.findAllByTaximplicationId(a.getId());
                    if(!dataRule.isEmpty()) {
                        for (M_AM_TAXIMPLICATION_RULE rule : dataRule) {
                            LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                            response.put("NO", no);
                            response.put("TAX IMPLICATION NAME", a.getTaxImplicationName());
                            response.put("CATEGORY", a.getCategory());
                            response.put("SERVICE TYPE", a.getServiceType());
                            response.put("CURRENT TRANSACTION CODE", a.getTransCodeName());
                            String formatCriteria = a.getCriteria();
                            if(StringUtils.hasValue(a.getCriteria()) && !a.getCriteria().contains("All")) {
                                formatCriteria = a.getCriteria() + ", Start Date, End Date, Description";
                            }
                            criteriaValue = this.getCriteriaValue(formatCriteria, a.getId());
                            response.put("CRITERIA", formatCriteria);
                            response.put("CRITERIA VALUE", criteriaValue);
                            response.put("CURRENT IMPLICATION TYPE", a.getImplicationType());
                            response.put("DESCRIPTION", a.getDescription());
                            response.put("STATUS", capitalizeFully(a.getStatus()));
                            response.put("DOCUMENT NUMBER", rule.getDocumentNumber());
                            response.put("IMPLICATION TYPE", rule.getImplicationType());
                            response.put("VAT INVOICE ISSUANCE", rule.getIsVatInv().equalsIgnoreCase("Y") ? "Yes" : "No");
                            response.put("GUNGGUNG", rule.getIsGunggung().equalsIgnoreCase("Y") ? "Yes" : "No");
                            Optional<M_RBI_FACTURE_CODE> codeOpt = mRbiFactureCodeRepo.findById(rule.getTransCode());
                            response.put("TRANSACTION CODE", codeOpt.isPresent() ? codeOpt.get().getCode() : "");
                            response.put("START DATE", CommonHelper.dateToString(Constant.FORMAT_START_END_DATE, rule.getStartDate()));
                            response.put("END DATE", CommonHelper.dateToString(Constant.FORMAT_START_END_DATE, rule.getEndDate()));
                            response.put("TAX IMPLICATION RULE OVERRIDE", this.getCondition(rule.getId()));
                            response.put("DESCRIPTION TAX IMPLICATION RULE", rule.getDescription());
                            response.put("STATUS TAX IMPLICATION RULE", capitalizeFully(rule.getStatus()));
                            response.put("STATUS APPROVAL TAX IMPLICATION RULE", capitalizeFullyApproval(rule.getApprovalStatus()));
                            allData.add(response);
                            no = no + 1;
                        }
                    } else {
                        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                        response.put("NO", no);
                        response.put("TAX IMPLICATION NAME", a.getTaxImplicationName());
                        response.put("CATEGORY", a.getCategory());
                        response.put("SERVICE TYPE", a.getServiceType());
                        response.put("CURRENT TRANSACTION CODE", a.getTransCodeName());
                        String formatCriteria = a.getCriteria();
                        if(StringUtils.hasValue(a.getCriteria()) && !a.getCriteria().contains("All")) {
                            formatCriteria = a.getCriteria() + ", Start Date, End Date, Description";
                        }
                        criteriaValue = this.getCriteriaValue(formatCriteria, a.getId());
                        response.put("CRITERIA", formatCriteria);
                        response.put("CRITERIA VALUE", criteriaValue);
                        response.put("CURRENT IMPLICATION TYPE", a.getImplicationType());
                        response.put("DESCRIPTION", a.getDescription());
                        response.put("STATUS", capitalizeFully(a.getStatus()));
                        response.put("DOCUMENT NUMBER", null);
                        response.put("IMPLICATION TYPE", null);
                        response.put("VAT INVOICE ISSUANCE", null);
                        response.put("GUNGGUNG", null);
                        response.put("TRANSACTION CODE", null);
                        response.put("START DATE", null);
                        response.put("END DATE", null);
                        response.put("TAX IMPLICATION RULE OVERRIDE", null);
                        response.put("DESCRIPTION TAX IMPLICATION RULE", null);
                        response.put("STATUS TAX IMPLICATION RULE", null);
                        response.put("STATUS APPROVAL TAX IMPLICATION RULE", null);
                        allData.add(response);
                        no = no + 1;
                    }
                }
                ByteArrayInputStream in = DownloadToExcel.downloadsFiles(allData, "Excel");
                headers.add(Constant.CONTENT_DISPOSITION, "attachment; filename=" +
                        "TAXIMPLICATION_" + UtilsDate.dateToString(new Date(), "yyyyMMddHHmmss") + ".xlsx");
                return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
            } else {
                headers.add(Constant.CONTENT_DISPOSITION, "No Data");
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            logger.info(String.format(CommonVariables.ERROR_IN_PAR, e.getMessage()));
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    public ResponseEntity<ResponseObject> getApprovalHistory(Integer refId) {

        List<String> appCategory = new ArrayList<>();
        appCategory.add(ApprovalCategory.TAX_IMPLICATION_RULE.name());
        appCategory.add(ApprovalCategory.INACTIVE_TAX_IMPLICATION_RULE.name());

        var resp = approvalServices.getApprovalHistory(refId, ApprovalCategory.TAX_IMPLICATION_RULE.name(), appCategory);
        if (resp != null) {
            ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    ResponseUtils.MESSAGE_SUCCESS, resp);

            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, "Data not found",
                ResponseUtils.DATA_EMPTY);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public static boolean isOverlapping(Date start1, Date end1) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.FORMAT_START_END_DATE);
            String startt1 = sdf.format(start1);
            String endd1 = sdf.format(end1);
            return (start1.before(end1) || startt1.equals(endd1));

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return false;
        }
    }
    
    public ResponseEntity<ResponseObject> listFactureCodes() {
        ResponseObject result;
        try {
            List<M_RBI_FACTURE_CODE> codes = mRbiFactureCodeRepo.findByStatus(FlowStatus.ACTIVE.name());
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_OK, codes);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public StringBuilder getCondition(Integer ruleId) {
        // override
        StringBuilder condition = new StringBuilder();
        List<M_AM_TAXIMPLICATION_RULE_OVR> allDataOvr = mAmTaxImplicationRuleOvrRepo.findAllByTaximplicationRuleIdOrderByIdAsc(ruleId);
        Integer j = 0;
        for(M_AM_TAXIMPLICATION_RULE_OVR ovr : allDataOvr) {
            if(j>0) {
                condition.append(", ");
            }
            //ovr
            Optional<R_GLOBAL_TYPE_VALUE> optName = globalTypeValueService.getOptionalGlobalTypeByGlbTypeValId("PPN Implication Type", ovr.getImplicationTypeOvr());
            condition.append(optName.map(R_GLOBAL_TYPE_VALUE::getName).orElse(null));
            Optional<M_RBI_FACTURE_CODE> facturCode = mRbiFactureCodeRepo.findById(ovr.getTransCodeOvr());
            condition.append("; " + facturCode.map(M_RBI_FACTURE_CODE::getCode).orElse(null));
            condition.append("; " + (StringUtils.hasValue(ovr.getDescription())?ovr.getDescription():""));

            List<M_AM_TAXIMPLICATION_RULE_OVR_CONDITION> allDataCdt = mAmTaxImplicationRuleOvrConditionRepo.findAllByTaximplicationRuleOvrIdOrderByIdAsc(ovr.getId());
            Integer i = 0;
            if(!allDataCdt.isEmpty()) {
                condition.append("(");
            }
            for(M_AM_TAXIMPLICATION_RULE_OVR_CONDITION cdt : allDataCdt) {
                // condition
                if(i>0) {
                    condition.append(", ");
                }
                Optional<R_GLOBAL_TYPE_VALUE> cdtName = globalTypeValueService.getOptionalGlobalTypeByGlbTypeValId("Tax Implication Rule Override Condition", cdt.getName());
                condition.append(cdtName.map(R_GLOBAL_TYPE_VALUE::getName).orElse(null));
                Optional<R_GLOBAL_TYPE_VALUE> optOperator = globalTypeValueService.getOptionalGlobalTypeByGlbTypeValId("Math Equation", cdt.getOperator());
                if(optOperator.isPresent()) {
                    switch (optOperator.get().getName()) {
                        case "GREATER THAN" :
                            condition.append(" > " + cdt.getValue());
                            break;
                        case "LESS THAN" :
                            condition.append(" < " + cdt.getValue());
                            break;
                        case "EQUALS" :
                            condition.append(" = " + cdt.getValue());
                            break;
                    }
                }
                i++;
            }
            if(!allDataCdt.isEmpty()) {
                condition.append(")");
            }
            j++;
        }


        return condition;
    }

    public StringBuilder getCriteriaValue(String criteriaData, Integer id) {
        StringBuilder criteriaValue = new StringBuilder();
        if(StringUtils.hasValue(criteriaData)) {
            List<String> criteria = Arrays.asList(criteriaData.split("\\s*,\\s*"));
            //looping data
            List<VW_AM_TAXIMPLICATION_CRITERIA_DATA> listCriteriaData = vwAmTaxImplicationCriteriaDataRepo.findAllByTaxImplicationId(id);
            Integer count = 0;
            for(VW_AM_TAXIMPLICATION_CRITERIA_DATA vwData : listCriteriaData) {
                Integer i = 0;
                if(count>0) {
                    criteriaValue.append(", ");
                }
                for(String cek : criteria) {
                    switch (cek) {
                        case "Account Number":
                            criteriaValue.append(i == 0 ? vwData.getAccountNumber() : "; " + vwData.getAccountNumber());
                            i++;
                            break;
                        case "Premise Subdistrict":
                            criteriaValue.append(i == 0 ? vwData.getSUBDISTRICTNAME() : "; " + vwData.getSUBDISTRICTNAME());
                            i++;
                            break;
                        case "Premise District":
                            criteriaValue.append(i == 0 ? vwData.getDISTRICTNAME() : "; " + vwData.getDISTRICTNAME());
                            i++;
                            break;
                        case "Premise City":
                            criteriaValue.append(i == 0 ? vwData.getCITYNAME() : "; " + vwData.getCITYNAME());
                            i++;
                            break;
                        case "Premise Province":
                            criteriaValue.append(i == 0 ? vwData.getPROVINCENAME() : "; " + vwData.getPROVINCENAME());
                            i++;
                            break;
                        case "Cost Center":
                            criteriaValue.append(i == 0 ? vwData.getCostCenterName() : "; " + vwData.getCostCenterName());
                            i++;
                            break;
                        case "SOR":
                            criteriaValue.append(i == 0 ? vwData.getSorName() : "; " + vwData.getSorName());
                            i++;
                            break;
                        case "WAPU Flag":
                            criteriaValue.append(i == 0 ? vwData.getWapuFlag() : "; " + vwData.getWapuFlag());
                            i++;
                            break;
                        case "Corporate Flag":
                            criteriaValue.append(i == 0 ? vwData.getCorporateFlag() : "; " + vwData.getCorporateFlag());
                            i++;
                            break;
                        case "Account Segment":
                            criteriaValue.append(i == 0 ? vwData.getAccountSegmentName() : "; " + vwData.getAccountSegmentName());
                            i++;
                            break;
                        case "Account Group Type":
                            criteriaValue.append(i == 0 ? vwData.getAccountGroupTypeName() : "; " + vwData.getAccountGroupTypeName());
                            i++;
                            break;
                        case "Account Category":
                            criteriaValue.append(i == 0 ? vwData.getAccountCategoryName() : "; " + vwData.getAccountCategoryName());
                            i++;
                            break;
                        case "SA Type":
                            criteriaValue.append(i == 0 ? vwData.getSaTypeName() : "; " + vwData.getSaTypeName());
                            i++;
                            break;
                        case "Account Type":
                            criteriaValue.append(i == 0 ? vwData.getAccountTypeName() : "; " + vwData.getAccountTypeName());
                            i++;
                            break;
                        case "Classification Type":
                            criteriaValue.append(i == 0 ? vwData.getClassificationTypeName() : "; " + vwData.getClassificationTypeName());
                            i++;
                            break;
                        case "Premise Country":
                            criteriaValue.append(i == 0 ? vwData.getCOUNTRYNAME() : "; " + vwData.getCOUNTRYNAME());
                            i++;
                            break;
                        case "Start Date":
                            if(StringUtils.hasValue(vwData.getStartDate())) {
                                criteriaValue.append(i == 0 ? UtilsDate.dateToString(vwData.getStartDate(), Constant.FORMAT_START_END_DATE) : "; " + UtilsDate.dateToString(vwData.getStartDate(), Constant.FORMAT_START_END_DATE));
                            }
                            i++;
                            break;
                        case "End Date":
                            if(StringUtils.hasValue(vwData.getEndDate())) {
                                criteriaValue.append(i == 0 ? UtilsDate.dateToString(vwData.getEndDate(), Constant.FORMAT_START_END_DATE) : "; " + UtilsDate.dateToString(vwData.getEndDate(), Constant.FORMAT_START_END_DATE));
                            }
                            i++;
                            break;
                        case "Description":
                            if(StringUtils.hasValue(vwData.getDescription())) {
                                criteriaValue.append(i == 0 ? vwData.getDescription() : "; " + vwData.getDescription());
                            }
                            i++;
                            break;
                        default:
                            break;
                    }
                }
                count++;
            }
        }
        return criteriaValue;
    }

}
