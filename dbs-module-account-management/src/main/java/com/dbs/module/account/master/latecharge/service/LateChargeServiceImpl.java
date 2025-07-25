package com.dbs.module.account.master.latecharge.service;

import com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement.TAmSALateChargeHeaderRepo;
import com.dbs.module.account.master.latecharge.dto.ViewLateChargeDto;
import com.dbs.module.account.master.latecharge.dto.ApprovalHeaderViewDto;
import com.dbs.module.account.master.latecharge.dto.CriteriaDataLatechargeDTO;
import com.dbs.module.account.master.latecharge.dto.GlobalTypeDTO;
import com.dbs.module.account.master.latecharge.dto.MAmLateChargeCriteriaDto;
import com.dbs.module.account.master.latecharge.dto.InactiveLateChargeDto;
import com.dbs.module.account.master.latecharge.dto.MAmLateChargeRuleFormulaDto;
import com.dbs.module.account.master.latecharge.dto.HistoryLogInformationDto;
import com.dbs.module.account.master.latecharge.dto.JsonForUpdateLateChargeRuleDto;
import com.dbs.module.account.master.latecharge.dto.MAmLateChargeRuleRequestDto;
import com.dbs.module.account.master.latecharge.dto.ApprovalLateChargeDto;
import com.dbs.module.account.master.latecharge.dto.MLateChargeRuleUpdateDto;
import com.dbs.module.account.master.latecharge.dto.ViewLateChargeRuleDto;
import com.dbs.module.account.master.latecharge.dto.CriteriaListDTO;
import com.dbs.module.account.master.latecharge.dto.MAmLateChargeRuleConditionDto;
import com.dbs.module.account.master.latecharge.dto.BooleanDTO;
import com.dbs.module.account.master.latecharge.dto.ViewDetailLateChargeDto;
import com.dbs.module.account.master.latecharge.dto.MAmLateChargeDto;
import com.dbs.module.account.master.latecharge.dto.ApprovalHierarchyDetailDto;
import com.dbs.common.base.utils.*;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.ApprovalServices;
import com.dbs.common.library.services.CriteriaServices;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.*;
import com.dbs.database.crm.entities.accountmanagement.*;
import com.dbs.database.crm.entities.product.T_APPROVAL_HISTORY;
import com.dbs.database.crm.entities.product.VW_APPROVAL_HIERARCHY_DETAIL;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_ACCOUNT_INFORMATION;
import com.dbs.database.crm.entities.usermanagement.*;
import com.dbs.database.crm.repositories.accountmanagement.*;
import com.dbs.database.crm.repositories.accountmanagement.Account.*;
import com.dbs.database.crm.repositories.product.TApprovalHistoryRepo;
import com.dbs.database.crm.repositories.product.VWApprovalHierarchyDtlRepo;
import com.dbs.database.crm.repositories.usermanagement.*;
import com.dbs.module.account.main.dto.GlobalTypeResponseDto;
import com.dbs.module.account.master.gassource.services.GasSourceServiceImpl;
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
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.ApprovalHierarchyDto;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.dbs.common.library.utils.StringUtils.capitalizeFully;
import com.dbs.common.library.utils.dto.DropDownCriteriaDto;
import com.dbs.database.crm.entities.usermanagement.view.VW_HIER_FLOW;
import com.dbs.database.crm.repositories.usermanagement.view.VWHierFlowRepo;
import com.dbs.module.account.detail.serviceagreement.dto.AttachmentListDto;
import com.dbs.module.account.master.latecharge.validate.ValidateLateCharge;

import static com.dbs.common.library.utils.StringUtils.capitalizeFullyApproval;
import com.dbs.database.crm.repositories.mastermanagement.MLocationsRepo;
import java.text.DecimalFormat;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class LateChargeServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(GasSourceServiceImpl.class);
    private static final String SUBMIT = "SUBMIT";
    @Autowired
    private Validator validator;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MAmLateChargeRepo mAmLateChargeRepo;
    @Autowired
    private MAmLateChargeCriteriaDataRepo mAmLateChargeCriteriaDataRepo;
    @Autowired
    private MAmLateChargeRuleConditionRepo mAmLateChargeRuleConditionRepo;
    @Autowired
    private MAmLateChargeRuleFormulaRepo mAmLateChargeRuleFormulaRepo;
    @Autowired
    private MAmLateChargeRuleRepo mAmLateChargeRuleRepo;
    @Autowired
    private MAttachmentRepo mAttachmentRepo;
    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioConfigurationProperties configurationProperties;
    @Autowired
    private MApprovalHierarchyRepo apphierRepo;
    @Autowired
    private ApprovalServices approvalServices;
    @Autowired
    private VWApprovalHierarchyDtlRepo vwApprovalHierarycyDtlRepo;
    @Autowired
    private VwMasterLateChargeRepo vwMasterLateChargeRepo;
    @Autowired
    private VwLateChargeCriteriaDataRepo vwLateChargeCriteriaDataRepo;
    @Autowired
    private MLocationsRepo mlocationRepo;
    @Autowired
    private MCostCenterRepo mCostCenterRepo;
    @Autowired
    private VWAccountInfoRepo vwAccountInfoRepo;
    @Autowired
    private MAccountingRulesRepo mAccountingRulesRepo;

    @Autowired
    private VwLateChargeRuleRepo vwLateChargeRuleRepo;
    @Autowired
    private MGlobalPropertiesRepo mGlobalPropertiesRepo;
    @Autowired
    private MGlobalTypeRepo mGlobalTypeRepo;
    @Autowired
    private MAmLateChargeCriteriaRepo mAmLateChargeCriteriaRepo;
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
    private MAccountRepo mAccountRepo;

    @Autowired
    private VwLateChargeRuleFormulaRepo vwLateChargeRuleFormulaRepo;

    @Autowired
    private VwLateChargeRuleConditionRepo vwLateChargeRuleConditionRepo;

    @Autowired
    private GlobalTypeValueService globalTypeValueService;

    @Autowired
    private AuditTrailRepo auditTrailRepo;
    
    @Autowired
    private CriteriaServices criteriaServices;
    
    @Autowired
    private ValidateLateCharge validateLateCharge;

    @Autowired
    private TAmSALateChargeHeaderRepo tAmSALateChargeHeaderRepo;

    
    public ResponseEntity<ResponseObject> validateCreateOrUpdate(MAmLateChargeDto param, String type) {
        try {
            ResponseObject resp = validateLateCharge.validatecreateMasterLateCharge(param, type);
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

    @SuppressWarnings("java:S3776")
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> createMasterLateCharge(MAmLateChargeDto inputRequest){
        ResponseObject result;
        try {
            
            ResponseObject resp = validateLateCharge.validatecreateMasterLateCharge(inputRequest, Constant.CREATE);
            if (StringUtils.hasValue(resp)) {
                return new ResponseEntity<>(resp, resp.getHttpCode());
            }
            
            M_AM_LATECHARGE latechargeInput = new M_AM_LATECHARGE();

            latechargeInput.setCreatedBy(UserDetailUtils.getUsername());
            latechargeInput.setCreatedDate(new Date());
            latechargeInput.setStatus(FlowStatus.ACTIVE.name());
            latechargeInput.setLateChargeName(inputRequest.getLateChargeName().strip());
            latechargeInput.setCurrency(inputRequest.getCurrency());
            latechargeInput.setDescription(inputRequest.getDescription());
            M_AM_LATECHARGE saveLateCharge = mAmLateChargeRepo.save(latechargeInput);

            for(CriteriaListDTO criteriaId : inputRequest.getCriteria()) {
                M_AM_LATECHARGE_CRITERIA savedCriteria = new M_AM_LATECHARGE_CRITERIA();
                savedCriteria.setCreatedBy(UserDetailUtils.getUsername());
                savedCriteria.setCreatedDate(new Date());
                savedCriteria.setStatus(FlowStatus.ACTIVE.name());
                savedCriteria.setCriteriaId(criteriaId.getValue());
                savedCriteria.setLatechargeId(saveLateCharge.getId());
                mAmLateChargeCriteriaRepo.save(savedCriteria);
            }

//            late charge criteria detail
            List<M_AM_LATECHARGE_CRITERIA_DATA> latechargeCriteriaDatas = new ArrayList<>();
            for(MAmLateChargeCriteriaDto lateChargeCriteria : inputRequest.getLateChargeCriteriaDatas()){
                M_AM_LATECHARGE_CRITERIA_DATA criteriaData = new M_AM_LATECHARGE_CRITERIA_DATA();
                criteriaData.setLatechargeId(saveLateCharge.getId());
                criteriaData.setPremiseCountry(lateChargeCriteria.getPremiseCountry());
                criteriaData.setPremiseProvince(lateChargeCriteria.getPremiseProvince());
                criteriaData.setPremiseCity(lateChargeCriteria.getPremiseCity());
                criteriaData.setPremiseDistrict(lateChargeCriteria.getPremiseDistrict());
                criteriaData.setPremiseSubdistrict(lateChargeCriteria.getPremiseSubdistrict());
                criteriaData.setSor(lateChargeCriteria.getSor());
                criteriaData.setCostCenter(lateChargeCriteria.getCostCenter());
                criteriaData.setAccountCategory(lateChargeCriteria.getAccountCategory());
                criteriaData.setClassificationType(lateChargeCriteria.getClassificationType());
                criteriaData.setAccountSegment(lateChargeCriteria.getAccountSegment());
                criteriaData.setAccountGroupType(lateChargeCriteria.getAccountGroupType());
                criteriaData.setAccountType(lateChargeCriteria.getAccountType());
                if(lateChargeCriteria.getCorporateFlag()!=null) {
                    criteriaData.setCorporateFlag(lateChargeCriteria.getCorporateFlag());
                } else {
                    criteriaData.setCorporateFlag(null);
                }
                if(lateChargeCriteria.getWapuFlag()!=null) {
                    criteriaData.setWapuFlag(lateChargeCriteria.getWapuFlag());
                } else {
                    criteriaData.setWapuFlag(null);
                }
                Optional<M_ACCOUNT> getAccountNumber = mAccountRepo.findByAccountId(lateChargeCriteria.getAccountNumber());
                criteriaData.setAccountNumber(getAccountNumber.isPresent()?getAccountNumber.get().getAccountNumber():null);
                if(lateChargeCriteria.getAllCriteria()!=null) {
                    criteriaData.setIsAll(lateChargeCriteria.getAllCriteria());
                } else {
                    criteriaData.setIsAll(null);
                }
                criteriaData.setSaType(lateChargeCriteria.getSaType());
                criteriaData.setCreatedBy(UserDetailUtils.getUsername());
                criteriaData.setCreatedDate(new Date());
                criteriaData.setDescription(lateChargeCriteria.getDescription());
                if (!ObjectUtils.isEmpty(lateChargeCriteria.getStartDate())) {
                    criteriaData.setStartDate(UtilsDate.stringToDate(lateChargeCriteria.getStartDate(), Constant.FORMAT_START_END_DATE));
                }
                if (!ObjectUtils.isEmpty(lateChargeCriteria.getEndDate())) {
                    criteriaData.setEndDate(UtilsDate.stringToDate(lateChargeCriteria.getEndDate(), Constant.FORMAT_START_END_DATE));
                }
                latechargeCriteriaDatas.add(criteriaData);
            }
            mAmLateChargeCriteriaDataRepo.saveAll(latechargeCriteriaDatas);

            Map<String, Object> d = new HashMap<>();
            d.put("masterLateCharge", saveLateCharge);
            d.put("lateChargeCriteriaData", latechargeCriteriaDatas);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success Create Master late Charge", d), HttpStatus.OK);
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> checkStartDateRule(MAmLateChargeRuleRequestDto request) {
        ResponseObject result;
        try {
            SimpleDateFormat formatDate = new SimpleDateFormat(Constant.FORMAT_DATE);
            Optional<M_AM_LATECHARGE_RULE> cekStartDate = mAmLateChargeRuleRepo.findTopByLatechargeIdAndStatusOrderByCreatedDateDesc(request.getLateChargeId(), FlowStatus.INACTIVE.name());
            if(cekStartDate.isPresent() && cekStartDate.get().getEndDate()!=null && isOverlapping(formatDate.parse(request.getStartDate()), cekStartDate.get().getEndDate())) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Start date must be greater than existing End Date!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success check validate start date", ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @SuppressWarnings("java:S3776")
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> createLateChargeRule(MAmLateChargeRuleRequestDto inputRequest, HttpServletRequest httpServletRequest){
        try {

            ResponseEntity<ResponseObject> validate = this.validateCreateLatechargeRule(inputRequest, httpServletRequest, Boolean.FALSE);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

//            create master late charge rule
            M_AM_LATECHARGE_RULE mAmLatechargeRule = new M_AM_LATECHARGE_RULE();

            mAmLatechargeRule.setCreatedBy(UserDetailUtils.getUsername());
            mAmLatechargeRule.setCreatedDate(new Date());
            mAmLatechargeRule.setLatechargeId(inputRequest.getLateChargeId());
            mAmLatechargeRule.setMaxAmount(inputRequest.getMaxAmount());
            if(!ObjectUtils.isEmpty(inputRequest.getStartDate())){
                Date dateStartDate = UtilsDate.stringToDate(inputRequest.getStartDate(), Constant.FORMAT_DATE_OTHER);
                mAmLatechargeRule.setStartDate(dateStartDate);
            }
            mAmLatechargeRule.setDocumentNumber(inputRequest.getDocumentNumber());
            mAmLatechargeRule.setDescription(inputRequest.getDescription());
            mAmLatechargeRule.setApphierId(inputRequest.getAppHierId());
            if(inputRequest.getIsSubmit()) {
                mAmLatechargeRule.setStatus(ApprovalStatus.DRAFT.name());
                mAmLatechargeRule.setApprovalStatus(ApprovalStatus.WAITING_APPROVAL.name());
                mAmLateChargeRuleRepo.save(mAmLatechargeRule);
                Integer tAppId = approvalServices.addApprovalFlowR(inputRequest.getAppHierId(),
                        ApprovalCategory.LATE_CHARGE_RULE.name(), mAmLatechargeRule.getId().toString(), null);
                approvalServices.setApprovalHistory(tAppId, mAmLatechargeRule.getId(), "Create New Approval Late Charge Rule", ApprovalCategory.LATE_CHARGE_RULE.name(),
                        SUBMIT, httpServletRequest);
            }else{
                mAmLatechargeRule.setStatus(ApprovalStatus.DRAFT.name());
                mAmLatechargeRule.setApprovalStatus(ApprovalStatus.DRAFT.name());
                mAmLateChargeRuleRepo.save(mAmLatechargeRule);
            }

            List<M_AM_LATECHARGE_RULE_FORMULA> mAmLatechargeRuleFormulas = new ArrayList<>();
            for(MAmLateChargeRuleFormulaDto mAmLateChargeRuleFormulaDto : inputRequest.getListRuleFormula()){
                M_AM_LATECHARGE_RULE_FORMULA latechargeRuleFormula = new M_AM_LATECHARGE_RULE_FORMULA();

                latechargeRuleFormula.setCreatedBy(UserDetailUtils.getUsername());
                latechargeRuleFormula.setCreatedDate(new Date());
                latechargeRuleFormula.setLatechargeRuleId(mAmLatechargeRule.getId());
                latechargeRuleFormula.setOperation(mAmLateChargeRuleFormulaDto.getOperation());
                Optional<R_GLOBAL_TYPE_VALUE> getOperation = rGlobalTypeValueRepo.findByGlbTypeValId(mAmLateChargeRuleFormulaDto.getOperation());
                latechargeRuleFormula.setOperationValue(getOperation.isPresent()?getOperation.get().getName():null);
                latechargeRuleFormula.setType(mAmLateChargeRuleFormulaDto.getType());
                if(mAmLateChargeRuleFormulaDto.getType().equals(Constant.CONSTANT_VALUE)){
                    latechargeRuleFormula.setConstantName(mAmLateChargeRuleFormulaDto.getVariableName());
                    latechargeRuleFormula.setVariabelNameValue(mAmLateChargeRuleFormulaDto.getVariableName());
                } else {
                    latechargeRuleFormula.setVariableName(Integer.parseInt(mAmLateChargeRuleFormulaDto.getVariableName()));
                    Optional<R_GLOBAL_TYPE_VALUE> getVariableName = rGlobalTypeValueRepo.findByGlbTypeValId(Integer.parseInt(mAmLateChargeRuleFormulaDto.getVariableName()));
                    latechargeRuleFormula.setVariabelNameValue(getVariableName.isPresent()?getVariableName.get().getName():null);
                }
                latechargeRuleFormula.setValue(mAmLateChargeRuleFormulaDto.getValue());
                latechargeRuleFormula.setStatus(ApprovalStatus.DRAFT.name());

                mAmLatechargeRuleFormulas.add(latechargeRuleFormula);
            }
            mAmLateChargeRuleFormulaRepo.saveAll(mAmLatechargeRuleFormulas);

            List<M_AM_LATECHARGE_RULE_CONDITION> mAmLatechargeRuleConditions = new ArrayList<>();
            for(MAmLateChargeRuleConditionDto mAmLateChargeRuleConditionDto : inputRequest.getListRuleCondition()){
                M_AM_LATECHARGE_RULE_CONDITION latechargeRuleCondition = new M_AM_LATECHARGE_RULE_CONDITION();

                latechargeRuleCondition.setCreatedBy(UserDetailUtils.getUsername());
                latechargeRuleCondition.setCreatedDate(new Date());
                latechargeRuleCondition.setLatechargeRuleId(mAmLatechargeRule.getId());
                latechargeRuleCondition.setName(mAmLateChargeRuleConditionDto.getName());
                Optional<R_GLOBAL_TYPE_VALUE> getName = rGlobalTypeValueRepo.findByGlbTypeValId(mAmLateChargeRuleConditionDto.getName());
                latechargeRuleCondition.setNameValue(getName.isPresent()?getName.get().getName():null);
                latechargeRuleCondition.setOperator(mAmLateChargeRuleConditionDto.getOperator());
                Optional<R_GLOBAL_TYPE_VALUE> getOperator = rGlobalTypeValueRepo.findByGlbTypeValId(mAmLateChargeRuleConditionDto.getOperator());
                latechargeRuleCondition.setOperatorValue(getOperator.isPresent()?getOperator.get().getName():null);
                latechargeRuleCondition.setDataType(mAmLateChargeRuleConditionDto.getDataType());
                Optional<R_GLOBAL_TYPE_VALUE> getDataType = rGlobalTypeValueRepo.findByGlbTypeValId(mAmLateChargeRuleConditionDto.getDataType());
                latechargeRuleCondition.setDataTypeValue(getDataType.isPresent()?getDataType.get().getName():null);
                latechargeRuleCondition.setValue(mAmLateChargeRuleConditionDto.getValue().toString());
                latechargeRuleCondition.setValueNew(mAmLateChargeRuleConditionDto.getValue());
                latechargeRuleCondition.setStatus(ApprovalStatus.DRAFT.name());

                mAmLatechargeRuleConditions.add(latechargeRuleCondition);
            }
            mAmLateChargeRuleConditionRepo.saveAll(mAmLatechargeRuleConditions);

            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Your data has been submitted", mAmLatechargeRule), HttpStatus.OK);
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> checkDuplicateConditionLatecharge(MAmLateChargeRuleRequestDto request) {
        ResponseObject result;
        try {
            Set<String> uniqueConditions = new HashSet<>();
            for (MAmLateChargeRuleConditionDto check : request.getListRuleCondition()) {
                String combination = check.getName() + "-" + check.getOperator();
                if (!uniqueConditions.add(combination)) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Condition with same name & operation is already exist", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.VALIDATE, ConstantAccount.MASTER_LATECHARGE), ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateCreateLatechargeRule(MAmLateChargeRuleRequestDto inputRequest, HttpServletRequest httpServletRequest, Boolean api) {
        ResponseObject result;
        try {
            Set<ConstraintViolation<MAmLateChargeRuleRequestDto>> violations = this.validator.validate(inputRequest);
            List<Map<String, Object>> violationList = new ArrayList<>();
            if (violations.size() > 0) {
                for (ConstraintViolation<MAmLateChargeRuleRequestDto> violation : violations) {
                    logger.error(violation.getMessage());
                    Map<String, Object> datas = new HashMap<>();
                    datas.put(violation.getPropertyPath().toString(), violation.getMessage());
                    violationList.add(datas);
                }
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        ResponseUtils.MESSAGE_BAD_REQUEST, violationList);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            boolean isSubmiter = approvalServices.checkIsSubmitter(httpServletRequest, inputRequest.getAppHierId());
            if (!isSubmiter) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "You are not submitter",
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            Optional<M_AM_LATECHARGE_RULE> cekDocUnique = mAmLateChargeRuleRepo.findTopByLatechargeIdAndDocumentNumberIgnoreCase(inputRequest.getLateChargeId(), inputRequest.getDocumentNumber());
            if(cekDocUnique.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Document Number already Exist", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }

            Set<String> uniqueConditions = new HashSet<>();
            for (MAmLateChargeRuleConditionDto check : inputRequest.getListRuleCondition()) {
                String combination = check.getName() + "-" + check.getOperator();
                if (!uniqueConditions.add(combination)) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Condition with same name & operation is already exist", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
            }

            if(inputRequest.getIsSubmit()) {
                // CEK ACTIVE RULE
                Optional<List<M_AM_LATECHARGE_RULE>> cekActiveRule = mAmLateChargeRuleRepo.findAllByLatechargeIdAndStatus(inputRequest.getLateChargeId(), FlowStatus.ACTIVE.name());
                if(cekActiveRule.isPresent() && !cekActiveRule.get().isEmpty()) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            UtilsAccount.messageValidateExist(ConstantAccount.MASTER_LATECHARGE, ConstantAccount.APPROVED), ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
                // CEK WAITING APPROVAL
                Optional<List<M_AM_LATECHARGE_RULE>> cekWaitingRule = mAmLateChargeRuleRepo.findAllByLatechargeIdAndStatusAndApprovalStatus(inputRequest.getLateChargeId(), FlowStatus.DRAFT.name(), ApprovalStatus.WAITING_APPROVAL.name());
                if(cekWaitingRule.isPresent() && !cekWaitingRule.get().isEmpty()) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            UtilsAccount.messageValidateExist(ConstantAccount.MASTER_LATECHARGE, ConstantAccount.WAITING_APPROVAL), ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
                SimpleDateFormat formatDate = new SimpleDateFormat(Constant.FORMAT_DATE_OTHER);
                Optional<M_AM_LATECHARGE_RULE> cekStartDate = mAmLateChargeRuleRepo.findTopByLatechargeIdAndStatusOrderByCreatedDateDesc(inputRequest.getLateChargeId(), FlowStatus.INACTIVE.name());
                if(cekStartDate.isPresent() && cekStartDate.get().getEndDate()!=null && isOverlapping(formatDate.parse(inputRequest.getStartDate()), cekStartDate.get().getEndDate())) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Start date must be greater than existing End Date!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
            }

            return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.MASTER_LATECHARGE);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(),e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }

    public ResponseEntity<ResponseObject> deleteDraftRule(Integer latechargeRuleId){
        ResponseObject result;
        try {

            Optional<M_AM_LATECHARGE_RULE> findRule = mAmLateChargeRuleRepo.findById(latechargeRuleId);
            if(!findRule.isPresent()){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Tax Implication Rule with id " + latechargeRuleId + " not found!", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            if((!findRule.get().getStatus().equalsIgnoreCase(FlowStatus.DRAFT.name())) && (!findRule.get().getApprovalStatus().equalsIgnoreCase(FlowStatus.DRAFT.name()))){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Tax Implication Rule with id " + latechargeRuleId + " is not draft!", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            mAmLateChargeRuleRepo.deleteById(latechargeRuleId);
            List<M_AM_LATECHARGE_RULE_FORMULA> ruleOvr = mAmLateChargeRuleFormulaRepo.findAllByLatechargeRuleId(latechargeRuleId);
            for(M_AM_LATECHARGE_RULE_FORMULA ovr : ruleOvr){
                mAmLateChargeRuleFormulaRepo.deleteById(ovr.getId());
            }
            List<M_AM_LATECHARGE_RULE_CONDITION> ruleCdt = mAmLateChargeRuleConditionRepo.findAllByLatechargeRuleId(latechargeRuleId);
            for(M_AM_LATECHARGE_RULE_CONDITION cdt : ruleCdt){
                mAmLateChargeRuleConditionRepo.deleteById(cdt.getId());
            }
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Successs delete draft Latecharge Rule", ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> lateChargeAttachment(Integer fileCategoryId, List<MultipartFile> files, Integer lateChargeRuleId) throws Exception {
        ResponseObject result;
        try {

            Optional<M_AM_LATECHARGE_RULE> mAmLatechargeRule = mAmLateChargeRuleRepo.findById(lateChargeRuleId);
            if (mAmLatechargeRule.isEmpty()) {

                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Latecharge rule " + ResponseUtils.MESSAGE_NOT_FOUND, ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            for(MultipartFile fileObject : files){
                String generatedFileName = UserDetailUtils.generateFileName(fileObject.getOriginalFilename());
                M_ATTACHMENT mAttachment = new M_ATTACHMENT();
                mAttachment.setCategory(ApprovalCategory.LATE_CHARGE_RULE.name());
                mAttachment.setFileCategoryId(fileCategoryId);
                mAttachment.setReferenceId(lateChargeRuleId);
                mAttachment.setType(fileObject.getContentType());
                mAttachment.setCreatedBy(UserDetailUtils.getUsername());
                mAttachment.setCreatedDate(new Date());
                mAttachment.setPathFile("PATH_24");
                mAttachment.setFileName(generatedFileName);
                mAttachment.setFileSize(fileObject.getSize());
                mAttachment.setIsDraft(mAmLatechargeRule.get().getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name()));
                mAttachment.setIsDeleted(Boolean.FALSE);

                R_GLOBAL_TYPE_VALUE rGlobalTypeValue = rGlobalTypeValueRepo
                        .findTopByGlbValueIgnoreCaseAndIsDeleted("PATH_24", false);
                String fullPath = rGlobalTypeValue.getName() + generatedFileName;

                String fullobject = "FILE" + fullPath;
                mAttachmentRepo.save(mAttachment);
                this.minioClient.putObject(this.configurationProperties.getBucket(), fullobject,
                        fileObject.getInputStream(), fileObject.getContentType());
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Late Charge Rule Attachment Uploaded", ResponseUtils.MESSAGE_OK);

            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getAllApprovalHeaderList(HttpServletRequest httpServletRequest){
        try{
            var mApprovalHierarchies = apphierRepo.findApprovalHierarchyByPositionAndType(UserDetailUtils.getPositionFromToken(httpServletRequest), "Y", "APR_ACC_LATE_CHARGE_RULE");
            logger.info(UserDetailUtils.getPositionFromToken(httpServletRequest).toString());
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
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> getApprovalHeaderId(Integer appHierId){
        ResponseObject result;
        try{
            Optional<List<VW_APPROVAL_HIERARCHY_DETAIL>> appDtl = vwApprovalHierarycyDtlRepo.findByApphierId(appHierId);
            if (appDtl.isPresent()) {

                List<String> pos = vwApprovalHierarycyDtlRepo.findPositionById(appHierId);

                List<ApprovalHierarchyDetailDto> dtos = new ArrayList<>();

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
                            ar.put("apphierId", appDtl_.getApphierId());
                            ar.put("employeeId", appDtl_.getEmployeeId());
                            ar.put("employeeName", appDtl_.getFullName());

                            detailList.add(ar);
                        }
                        hierarchyDetailDto.setEmployeeDetail(detailList);
                        dtos.add(hierarchyDetailDto);
                    }

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
                                logger.info("Approver " + i + " :" + appDtl_.getFullName());
                            }
                            logger.info("Size: " + detailList.size());
                            hierarchyDetailDto.setEmployeeDetail(detailList);
                            dtos.add(hierarchyDetailDto);
                        }
                    }

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
                        logger.info("Size: " + detailList.size());
                        hierarchyDetailDto.setEmployeeDetail(detailList);
                        dtos.add(hierarchyDetailDto);
                    }

                }
                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, "Success get data", dtos);
                logger.info("Response Success ->" + result);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND, "Failed get data",
                    ResponseUtils.DATA_EMPTY);
            logger.info("Response Failed ");
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    @Transactional(rollbackFor = Exception.class, readOnly = false)
    public ResponseEntity<ResponseObject> updateLatechargeHeader(MAmLateChargeDto mAmLateChargeUpdateDto){
        ResponseObject result;
        try{
            
            ResponseObject resp = validateLateCharge.validatecreateMasterLateCharge(mAmLateChargeUpdateDto, Constant.UPDATE);
            if (StringUtils.hasValue(resp)) {
                return new ResponseEntity<>(resp, resp.getHttpCode());
            }

            Optional<M_AM_LATECHARGE> getLateCharge = mAmLateChargeRepo.findById(mAmLateChargeUpdateDto.getLateChargeId());
            M_AM_LATECHARGE latechargeData = getLateCharge.get();
            latechargeData.setUpdatedDate(new Date());
            latechargeData.setUpdatedBy(UserDetailUtils.getUsername());
            latechargeData.setLateChargeName(mAmLateChargeUpdateDto.getLateChargeName().strip());
            latechargeData.setCurrency(mAmLateChargeUpdateDto.getCurrency());
            latechargeData.setDescription(mAmLateChargeUpdateDto.getDescription());

            if (!mAmLateChargeUpdateDto.getCriteria().isEmpty()){
                List<Integer> receivedIdCriteria = new ArrayList<>();
                List<M_AM_LATECHARGE_CRITERIA> getCriteriaId = mAmLateChargeCriteriaRepo.findAllByLatechargeId(mAmLateChargeUpdateDto.getLateChargeId());
                for(CriteriaListDTO criteriaId : mAmLateChargeUpdateDto.getCriteria()) {
                    Optional<M_AM_LATECHARGE_CRITERIA> getCriteria = criteriaId.getId()!=null? mAmLateChargeCriteriaRepo.findById(criteriaId.getId()):null;
                    M_AM_LATECHARGE_CRITERIA criteria = new M_AM_LATECHARGE_CRITERIA();
                    if(getCriteria!=null) {
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
                    criteria.setLatechargeId(mAmLateChargeUpdateDto.getLateChargeId());
                    mAmLateChargeCriteriaRepo.save(criteria);
                }
                for(M_AM_LATECHARGE_CRITERIA dd : getCriteriaId) {
                    if (receivedIdCriteria.isEmpty() || !receivedIdCriteria.contains(dd.getId())) {
                        mAmLateChargeCriteriaRepo.deleteById(dd.getId());
                    }
                }
            }

            if (!mAmLateChargeUpdateDto.getLateChargeCriteriaDatas().isEmpty()){
                List<M_AM_LATECHARGE_CRITERIA_DATA> requestupdateDatas = new ArrayList<>();
                List<Integer> receivedIdCriteriaDatas = new ArrayList<>();
                List<M_AM_LATECHARGE_CRITERIA_DATA> dataCriteriaDatas = mAmLateChargeCriteriaDataRepo.findAllByLatechargeId(mAmLateChargeUpdateDto.getLateChargeId());
                for(MAmLateChargeCriteriaDto requestupdateData : mAmLateChargeUpdateDto.getLateChargeCriteriaDatas()){
                    Optional<M_AM_LATECHARGE_CRITERIA_DATA> getCriteriaDatas = requestupdateData.getId()!=null?mAmLateChargeCriteriaDataRepo.findById(requestupdateData.getId()):Optional.empty();
                    M_AM_LATECHARGE_CRITERIA_DATA criteriaData = new M_AM_LATECHARGE_CRITERIA_DATA();
                    if(getCriteriaDatas.isPresent()){
                        criteriaData = getCriteriaDatas.get();
                        criteriaData.setUpdatedBy(UserDetailUtils.getUsername());
                        criteriaData.setUpdatedDate(new Date());
                        receivedIdCriteriaDatas.add(criteriaData.getId());
                    } else {
                        criteriaData.setCreatedBy(UserDetailUtils.getUsername());
                        criteriaData.setCreatedDate(new Date());
                    }
                    criteriaData.setLatechargeId(latechargeData.getId());
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
                    criteriaData.setAccountNumber(getAccountNumber.isPresent()?getAccountNumber.get().getAccountNumber():null);
                    criteriaData.setAccountSegment(requestupdateData.getAccountSegment());
                    criteriaData.setAccountGroupType(requestupdateData.getAccountGroupType());
                    criteriaData.setAccountType(requestupdateData.getAccountType());
                    criteriaData.setSaType(requestupdateData.getSaType());
                    criteriaData.setDescription(requestupdateData.getDescription());
                    if(!ObjectUtils.isEmpty(requestupdateData.getCorporateFlag())){
                        criteriaData.setCorporateFlag(requestupdateData.getCorporateFlag());
                    }
                    if(!ObjectUtils.isEmpty(requestupdateData.getWapuFlag())){
                        criteriaData.setWapuFlag(requestupdateData.getWapuFlag());
                    }
                    if(!ObjectUtils.isEmpty(requestupdateData.getAllCriteria())){
                        criteriaData.setIsAll(requestupdateData.getAllCriteria());
                    }
                    if (!ObjectUtils.isEmpty(requestupdateData.getStartDate())) {
                        criteriaData.setStartDate(UtilsDate.stringToDate(requestupdateData.getStartDate(), Constant.FORMAT_START_END_DATE));
                    }
                    if (!ObjectUtils.isEmpty(requestupdateData.getEndDate())) {
                        criteriaData.setEndDate(UtilsDate.stringToDate(requestupdateData.getEndDate(), Constant.FORMAT_START_END_DATE));
                    }
                    requestupdateDatas.add(criteriaData);

                }
                if(!requestupdateDatas.isEmpty()){
                    mAmLateChargeCriteriaDataRepo.saveAll(requestupdateDatas);
                }
                for(M_AM_LATECHARGE_CRITERIA_DATA dd : dataCriteriaDatas) {
                    if (receivedIdCriteriaDatas.isEmpty() || !receivedIdCriteriaDatas.contains(dd.getId())) {
                        mAmLateChargeCriteriaDataRepo.deleteById(dd.getId());
                    }
                }
            }

            mAmLateChargeRepo.save(latechargeData);
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Updated", latechargeData);
            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> viewMaster(MaterialTablePagingRequest pagingRequest,
                                                     PagedResourcesAssembler<VW_MASTER_LATE_CHARGE> assembler){
        try{
            Map<String, Object> filter = new HashMap<>();
            Page<VW_MASTER_LATE_CHARGE> datas;

            if (isNotBlank(pagingRequest.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingRequest.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingRequest.getSearch().add(key+"~"+value);
                }
            }

            if(!pagingRequest.getSearch().isEmpty()) {
                datas = vwMasterLateChargeRepo.findAll(vwMasterLateChargeRepo.getSpecificationFromFilters(pagingRequest, filter), PagingUtils.getPaging(pagingRequest));
            } else {
                datas = vwMasterLateChargeRepo.findAll(vwMasterLateChargeRepo.getSpecificationDefault(filter), PagingUtils.getPaging(pagingRequest));
            }

            List<ViewLateChargeDto> responseDatas = new ArrayList<>();
            for(VW_MASTER_LATE_CHARGE data : datas.getContent()){
                ViewLateChargeDto responseData = new ViewLateChargeDto();
                responseData.setLateChargeId(data.getLateChargeId());
                responseData.setName(data.getName());
                responseData.setDescription(data.getDescription());
                responseData.setCurrency(data.getCurrency());
                responseData.setStatus(data.getStatus());
                responseData.setCriteria(data.getCriteria());
                if(data.getLateChargeRuleId()!=null){
                    responseData.setFormula(data.getFormula());
                    responseData.setMaxAmount(data.getMaxAmount());
                    responseData.setMaxAmountReal(data.getMaxAmountReal());
                } else {
                    responseData.setFormula(null);
                    responseData.setMaxAmount(null);
                    responseData.setMaxAmountReal(null);
                }
                responseDatas.add(responseData);
            }
            PagedModel<EntityModel<VW_MASTER_LATE_CHARGE>> pagedData = assembler.toModel(datas);

            Map<String, Object> d = new HashMap<>();
            d.put("result", responseDatas);
            d.put("page", pagedData.getMetadata());
            d.put("links", pagedData.getLinks());
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get List Master Late Charge", d), HttpStatus.OK);


        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getFromGlobalType(String name){
        ResponseObject result;
        try {
            List<LinkedHashMap<String, Object>> allData = criteriaServices.getCriteriaByGlobalType(name);
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_OK, allData);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class, readOnly = false)
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> updateLateChargeRuleHeader(MLateChargeRuleUpdateDto updateRequest, HttpServletRequest httpServletRequest){
        try {

            ResponseEntity<ResponseObject> validate = this.validateUpdateLatechargeRule(updateRequest, httpServletRequest, Boolean.FALSE);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            ResponseObject result;
            Optional<M_AM_LATECHARGE_RULE> findLateChargeRuleId = mAmLateChargeRuleRepo.findById(updateRequest.getLateChargeRuleId());

            M_AM_LATECHARGE_RULE mAmLatechargeRule = findLateChargeRuleId.get();
            List<M_AM_LATECHARGE_RULE_FORMULA> datasFml = mAmLateChargeRuleFormulaRepo.findAllByLatechargeRuleId(mAmLatechargeRule.getId());
            List<M_AM_LATECHARGE_RULE_CONDITION> datasCdt = mAmLateChargeRuleConditionRepo.findAllByLatechargeRuleId(mAmLatechargeRule.getId());

            if(mAmLatechargeRule.getStatus().equalsIgnoreCase(FlowStatus.DRAFT.name())) {
                mAmLatechargeRule.setUpdatedBy(UserDetailUtils.getUsername());
                mAmLatechargeRule.setUpdatedDate(new Date());
                mAmLatechargeRule.setDocumentNumber(updateRequest.getDocumentNumber());
                mAmLatechargeRule.setMaxAmount(updateRequest.getMaxAmount());
                if(!ObjectUtils.isEmpty(updateRequest.getStartDate())) {
                    Date dateStartDate = UtilsDate.stringToDate(updateRequest.getStartDate(), "dd-MM-yyyy");
                    mAmLatechargeRule.setStartDate(dateStartDate);
                }
                mAmLatechargeRule.setDescription(updateRequest.getDescription());
                mAmLatechargeRule.setApphierId(updateRequest.getAppHierId());
                if(updateRequest.getIsSubmit()) {
                    mAmLatechargeRule.setStatus(ApprovalStatus.DRAFT.name());
                    mAmLatechargeRule.setApprovalStatus(ApprovalStatus.WAITING_APPROVAL.name());
                }else{
                    mAmLatechargeRule.setStatus(ApprovalStatus.DRAFT.name());
                    mAmLatechargeRule.setApprovalStatus(ApprovalStatus.DRAFT.name());
                }
                mAmLateChargeRuleRepo.save(mAmLatechargeRule);

                List<Integer> receivedIdFml = new ArrayList<>();
                List<M_AM_LATECHARGE_RULE_FORMULA> mAmLatechargeRuleFormulas = new ArrayList<>();
                for(MAmLateChargeRuleFormulaDto mAmLateChargeRuleFormulaDto : updateRequest.getListRuleFormula()){
                    Optional<M_AM_LATECHARGE_RULE_FORMULA> dataFml = mAmLateChargeRuleFormulaDto.getLateChargeRuleFormulaId()!=null?mAmLateChargeRuleFormulaRepo.findById(mAmLateChargeRuleFormulaDto.getLateChargeRuleFormulaId()):null;
                    M_AM_LATECHARGE_RULE_FORMULA fml = new M_AM_LATECHARGE_RULE_FORMULA();
                    if(dataFml!=null) {
                        fml = dataFml.get();
                        fml.setUpdatedDate(new Date());
                        fml.setUpdatedBy(UserDetailUtils.getUsername());
                        receivedIdFml.add(fml.getId());
                    } else {
                        fml.setCreatedDate(new Date());
                        fml.setCreatedBy(UserDetailUtils.getUsername());
                    }
                    fml.setLatechargeRuleId(mAmLatechargeRule.getId());
                    fml.setOperation(mAmLateChargeRuleFormulaDto.getOperation());
                    Optional<R_GLOBAL_TYPE_VALUE> getOperation = rGlobalTypeValueRepo.findByGlbTypeValId(mAmLateChargeRuleFormulaDto.getOperation());
                    fml.setOperationValue(getOperation.isPresent()?getOperation.get().getName():null);
                    fml.setType(mAmLateChargeRuleFormulaDto.getType());
                    if(mAmLateChargeRuleFormulaDto.getType().equals(Constant.CONSTANT_VALUE)){
                        fml.setConstantName(mAmLateChargeRuleFormulaDto.getVariableName());
                        fml.setVariabelNameValue(mAmLateChargeRuleFormulaDto.getVariableName());
                    } else {
                        fml.setVariableName(Integer.parseInt(mAmLateChargeRuleFormulaDto.getVariableName()));
                        Optional<R_GLOBAL_TYPE_VALUE> getVariableName = rGlobalTypeValueRepo.findByGlbTypeValId(Integer.parseInt(mAmLateChargeRuleFormulaDto.getVariableName()));
                        fml.setVariabelNameValue(getVariableName.isPresent()?getVariableName.get().getName():null);
                    }
                    fml.setValue(mAmLateChargeRuleFormulaDto.getValue());
                    fml.setStatus(ApprovalStatus.DRAFT.name());
                    mAmLatechargeRuleFormulas.add(fml);
                }
                mAmLateChargeRuleFormulaRepo.saveAll(mAmLatechargeRuleFormulas);
                // delete
                for(M_AM_LATECHARGE_RULE_FORMULA dd : datasFml) {
                    if(receivedIdFml.isEmpty() || !receivedIdFml.contains(dd.getId())) {
                        mAmLateChargeRuleFormulaRepo.deleteById(dd.getId());
                    }
                }

                List<Integer> receivedIdCdt = new ArrayList<>();
                List<M_AM_LATECHARGE_RULE_CONDITION> mAmLatechargeRuleConditions = new ArrayList<>();
                for(MAmLateChargeRuleConditionDto mAmLateChargeRuleConditionDto : updateRequest.getListRuleCondition()){
                    Optional<M_AM_LATECHARGE_RULE_CONDITION> dataCdt = mAmLateChargeRuleConditionDto.getLateChargeRuleConditionId()!=null?mAmLateChargeRuleConditionRepo.findById(mAmLateChargeRuleConditionDto.getLateChargeRuleConditionId()):null;
                    M_AM_LATECHARGE_RULE_CONDITION cdt = new M_AM_LATECHARGE_RULE_CONDITION();
                    if(dataCdt!=null) {
                        cdt = dataCdt.get();
                        cdt.setUpdatedDate(new Date());
                        cdt.setUpdatedBy(UserDetailUtils.getUsername());
                        receivedIdCdt.add(cdt.getId());
                    } else {
                        cdt.setCreatedDate(new Date());
                        cdt.setCreatedBy(UserDetailUtils.getUsername());
                    }
                    cdt.setLatechargeRuleId(mAmLatechargeRule.getId());
                    cdt.setName(mAmLateChargeRuleConditionDto.getName());
                    Optional<R_GLOBAL_TYPE_VALUE> getName = rGlobalTypeValueRepo.findByGlbTypeValId(mAmLateChargeRuleConditionDto.getName());
                    cdt.setNameValue(getName.isPresent()?getName.get().getName():null);
                    cdt.setOperator(mAmLateChargeRuleConditionDto.getOperator());
                    Optional<R_GLOBAL_TYPE_VALUE> getOperator = rGlobalTypeValueRepo.findByGlbTypeValId(mAmLateChargeRuleConditionDto.getOperator());
                    cdt.setOperatorValue(getOperator.isPresent()?getOperator.get().getName():null);
                    cdt.setDataType(mAmLateChargeRuleConditionDto.getDataType());
                    Optional<R_GLOBAL_TYPE_VALUE> getDataType = rGlobalTypeValueRepo.findByGlbTypeValId(mAmLateChargeRuleConditionDto.getDataType());
                    cdt.setDataTypeValue(getDataType.isPresent()?getDataType.get().getName():null);
                    cdt.setValue(mAmLateChargeRuleConditionDto.getValue().toString());
                    cdt.setValueNew(mAmLateChargeRuleConditionDto.getValue());
                    cdt.setStatus(ApprovalStatus.DRAFT.name());
                    mAmLatechargeRuleConditions.add(cdt);
                }
                mAmLateChargeRuleConditionRepo.saveAll(mAmLatechargeRuleConditions);
                // delete
                for(M_AM_LATECHARGE_RULE_CONDITION dd : datasCdt) {
                    if(receivedIdCdt.isEmpty() || !receivedIdCdt.contains(dd.getId())) {
                        mAmLateChargeRuleConditionRepo.deleteById(dd.getId());
                    }
                }
            }else {
                JsonForUpdateLateChargeRuleDto jsonData = new JsonForUpdateLateChargeRuleDto();
                jsonData.setDescription(updateRequest.getDescription());
                jsonData.setEndDate(null);
                String json = "";
                json = new Gson().toJson(jsonData);
                mAmLatechargeRule.setJsonData(json);
                mAmLatechargeRule.setUpdatedBy(UserDetailUtils.getUsername());
                mAmLatechargeRule.setUpdatedDate(new Date());
            }

            if (updateRequest.getIsSubmit()) {
                mAmLatechargeRule.setApprovalStatus(ApprovalStatus.WAITING_APPROVAL.name());
                M_AM_LATECHARGE_RULE savemLateChargeRule = mAmLateChargeRuleRepo.save(mAmLatechargeRule);

                Integer tappId = approvalServices.addApprovalFlowR(updateRequest.getAppHierId(),
                        ApprovalCategory.LATE_CHARGE_RULE.name(), savemLateChargeRule.getId().toString(), "Update latecharge rule");

                approvalServices.setApprovalHistory(tappId, savemLateChargeRule.getId(), "Update Late Charge Rule", ApprovalCategory.LATE_CHARGE_RULE.name(),
                        SUBMIT, httpServletRequest);
            } else {
                mAmLatechargeRule.setApprovalStatus(ApprovalStatus.DRAFT.name());
                mAmLateChargeRuleRepo.save(mAmLatechargeRule);
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Late Charge Update", mAmLatechargeRule);
            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateUpdateLatechargeRule(MLateChargeRuleUpdateDto updateRequest, HttpServletRequest httpServletRequest, Boolean api) {
        ResponseObject result;
        try {
            boolean isSubmiter = approvalServices.checkIsSubmitter(httpServletRequest, updateRequest.getAppHierId());
            if (!isSubmiter) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "You are not submitter",
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            Optional<M_AM_LATECHARGE_RULE> findLateChargeRuleId = mAmLateChargeRuleRepo.findById(updateRequest.getLateChargeRuleId());
            if(findLateChargeRuleId.isEmpty()){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "lateChargeRule with id " + updateRequest.getLateChargeRuleId() + " not found", null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            if (findLateChargeRuleId.get().getApprovalStatus().equalsIgnoreCase(ApprovalStatus.WAITING_APPROVAL.name())){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Data is Waiting For Approval Cannot change", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            M_AM_LATECHARGE_RULE mAmLatechargeRule = findLateChargeRuleId.get();
            Set<String> uniqueConditions = new HashSet<>();
            for (MAmLateChargeRuleConditionDto check : updateRequest.getListRuleCondition()) {
                String combination = check.getName() + "-" + check.getOperator();
                if (!uniqueConditions.add(combination)) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Condition with same name & operation is already exist", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
            }

            Optional<M_AM_LATECHARGE_RULE> cekDocUnique = mAmLateChargeRuleRepo.findTopByLatechargeIdAndDocumentNumberIgnoreCase(mAmLatechargeRule.getLatechargeId(), updateRequest.getDocumentNumber());
            if(cekDocUnique.isPresent() && !cekDocUnique.get().getId().equals(mAmLatechargeRule.getId())) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Document Number already Exist", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }

            if(updateRequest.getIsSubmit()) {
                Optional<List<M_AM_LATECHARGE_RULE>> cekActiveRule = mAmLateChargeRuleRepo.findAllByLatechargeIdAndStatus(mAmLatechargeRule.getLatechargeId(), FlowStatus.ACTIVE.name());
                if(cekActiveRule.isPresent() && !cekActiveRule.get().isEmpty()) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, UtilsAccount.messageValidateExist(ConstantAccount.MASTER_LATECHARGE, ConstantAccount.APPROVED), ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
                // CEK WAITING APPROVAL
                Optional<List<M_AM_LATECHARGE_RULE>> cekWaitingRule = mAmLateChargeRuleRepo.findAllByLatechargeIdAndStatusAndApprovalStatus(mAmLatechargeRule.getLatechargeId(), FlowStatus.DRAFT.name(), ApprovalStatus.WAITING_APPROVAL.name());
                if(cekWaitingRule.isPresent() && !cekWaitingRule.get().isEmpty()) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            UtilsAccount.messageValidateExist(ConstantAccount.MASTER_LATECHARGE, ConstantAccount.WAITING_APPROVAL), ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
                SimpleDateFormat formatDate = new SimpleDateFormat(Constant.FORMAT_DATE_OTHER);
                Optional<M_AM_LATECHARGE_RULE> cekStartDate = mAmLateChargeRuleRepo.findTopByLatechargeIdAndStatusOrderByCreatedDateDesc(mAmLatechargeRule.getLatechargeId(), FlowStatus.INACTIVE.name());
                if(cekStartDate.isPresent() && cekStartDate.get().getEndDate()!=null && isOverlapping(formatDate.parse(updateRequest.getStartDate()), cekStartDate.get().getEndDate())) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Start date must be greater than existing End Date!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
            }

            return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.MASTER_LATECHARGE);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(),e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }

    public ResponseEntity<ResponseObject> deleteDraft(Integer attachmentId){
        try{
            ResponseObject result;
            Optional<M_ATTACHMENT> findAttachment = mAttachmentRepo.findById(attachmentId);
            if(findAttachment.isEmpty()){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            M_ATTACHMENT deletedData = findAttachment.get();
            deletedData.setIsDeleted(Boolean.TRUE);
            mAttachmentRepo.save(deletedData);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Data Deleted", ResponseUtils.MESSAGE_OK);
            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> viewDetail(MaterialTablePagingRequest pagingRequest, Integer lateChargeId){
        ResponseObject result;
        try{

//            late charge information0 ->(name, description)
            Map<String, Object> filter = new HashMap<>();
//            Page<VW_LATE_CHARGE_CRITERIA_DATA_REAL> datas = null;
            Optional<M_AM_LATECHARGE> findLateCharge = mAmLateChargeRepo.findById(lateChargeId);
            ViewDetailLateChargeDto detailLateChargeResponse = new ViewDetailLateChargeDto();
            if(findLateCharge.isEmpty()){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            M_AM_LATECHARGE latecharge = findLateCharge.get();
            detailLateChargeResponse.setLateChargeId(latecharge.getId());
            detailLateChargeResponse.setLateChargeName(latecharge.getLateChargeName());

            GlobalTypeDTO dtoCurrency = new GlobalTypeDTO();
            dtoCurrency.setId(latecharge.getCurrency());
            Optional<R_GLOBAL_TYPE_VALUE> getTypeValue = rGlobalTypeValueRepo.findByGlbTypeValId(latecharge.getCurrency());
            dtoCurrency.setName(getTypeValue.isPresent()?getTypeValue.get().getName():null);
            detailLateChargeResponse.setCurrency(dtoCurrency);

            List<M_AM_LATECHARGE_CRITERIA> getCriteriaId = mAmLateChargeCriteriaRepo.findAllByLatechargeId(latecharge.getId());
            List<CriteriaListDTO> allListCriteria = new ArrayList<>();
            for(M_AM_LATECHARGE_CRITERIA getId : getCriteriaId){
                CriteriaListDTO cc = new CriteriaListDTO();
                cc.setId(getId.getId());
                cc.setValue(getId.getCriteriaId());
                Optional<R_GLOBAL_TYPE_VALUE> getLabelCriteria = rGlobalTypeValueRepo.findByGlbTypeValId(getId.getCriteriaId());
                cc.setLabel(getLabelCriteria.isPresent()?getLabelCriteria.get().getName():null);
                allListCriteria.add(cc);
            }

            detailLateChargeResponse.setCriteria(allListCriteria);
            detailLateChargeResponse.setDescription(latecharge.getDescription());
            detailLateChargeResponse.setStatus(latecharge.getStatus());

            //            history log information created date, created by, updated date, updated by
            HistoryLogInformationDto historyLogInformationDto = new HistoryLogInformationDto();
            historyLogInformationDto.setId(latecharge.getId());
            historyLogInformationDto.setUpdatedBy(latecharge.getUpdatedBy());
            if(!ObjectUtils.isEmpty(latecharge.getCreatedDate())) {
                historyLogInformationDto.setCreatedDate(UtilsDate.dateToString(latecharge.getCreatedDate(),"dd MMM yyyy HH:mm:ss"));
            }
            historyLogInformationDto.setCreatedBy(latecharge.getCreatedBy());
            if (!ObjectUtils.isEmpty(latecharge.getUpdatedDate())){
                historyLogInformationDto.setUpdatedDate(UtilsDate.dateToString(latecharge.getUpdatedDate(), "dd MMM yyyy HH:mm:ss"));
            }
            historyLogInformationDto.setUpdatedBy(latecharge.getUpdatedBy());
            detailLateChargeResponse.setHistoryLogInformation(historyLogInformationDto);

            filter.put("lateChargeId", latecharge.getId());
            if (isNotBlank(pagingRequest.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingRequest.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingRequest.getSearch().add(key+"~"+value);
                }
            }
            List<VW_LATE_CHARGE_CRITERIA_DATA_REAL> datas = vwLateChargeCriteriaDataRepo.findAllByLateChargeIdOrderByIdAsc(latecharge.getId());
            List<CriteriaDataLatechargeDTO> allData = datas.stream()
                    .map(g -> {
                        CriteriaDataLatechargeDTO criteria = new CriteriaDataLatechargeDTO();
                        criteria.setId(g.getId());
                        criteria.setLateChargeId(g.getLateChargeId());
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

            if(!datas.isEmpty()){
                detailLateChargeResponse.setLateChargeCriterias(allData);
            }

            Optional<List<M_AM_LATECHARGE_RULE>> cekActiveRule = mAmLateChargeRuleRepo.findAllByLatechargeIdAndStatus(latecharge.getId(), FlowStatus.ACTIVE.name());
            detailLateChargeResponse.setIsRuleActive(cekActiveRule.isPresent() && !cekActiveRule.get().isEmpty());

            //acr
            List<AUDIT_TRAIL> auditTrail = auditTrailRepo.findAll().stream()
                    .filter(e -> e.getTableName().equalsIgnoreCase("M_AM_LATECHARGE"))
                    .filter(f -> f.getDataId().equalsIgnoreCase(latecharge.getId().toString()))
                    .collect(Collectors.toList());
            detailLateChargeResponse.setActiveInactiveLog(auditTrail);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Detail Late Charge", detailLateChargeResponse);
            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> viewLateChargeRule(MaterialTablePagingRequest pagingRequest,
                                                             PagedResourcesAssembler<VW_LATE_CHARGE_RULE> assembler,
                                                             Integer lateChargeId) {
        ResponseObject result;
        try{
            Page<VW_LATE_CHARGE_RULE> datas = null;
            Map<String, Object> filter = new HashMap<>();

            filter.put("latechargeId", lateChargeId);
            if (isNotBlank(pagingRequest.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingRequest.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingRequest.getSearch().add(key+"~"+value);
                }
            }
            Specification<VW_LATE_CHARGE_RULE> specification = pagingRequest.getSearch().isEmpty() ? vwLateChargeRuleRepo.getSpecificationDefault(filter) : vwLateChargeRuleRepo.getSpecificationFromFilters(pagingRequest, filter);
            datas = vwLateChargeRuleRepo.findAll(specification, PagingUtils.getPaging(pagingRequest));
            PagedModel<EntityModel<VW_LATE_CHARGE_RULE>> pagedData = assembler.toModel(datas);
            List<ViewLateChargeRuleDto> responseDatas = new ArrayList<>();
            if(!datas.isEmpty()){
                for(VW_LATE_CHARGE_RULE data : datas.getContent()){
                    ViewLateChargeRuleDto responseData = new ViewLateChargeRuleDto();
                    responseData.setId(data.getId());
                    responseData.setLatechargeId(data.getLatechargeId());
                    responseData.setCurrency(data.getCurrency());
                    responseData.setDocumentNumber(data.getDocumentNumber());
                    responseData.setMaxAmount(data.getMaxAmount());
                    responseData.setMaxAmountReal(data.getMaxAmountReal());
                    if(!ObjectUtils.isEmpty(data.getStartDate())){
                        responseData.setStartDate(UtilsDate.dateToString(data.getStartDate(), Constant.FORMAT_START_END_DATE));
                    }
                    if(!ObjectUtils.isEmpty(data.getEndDate())){
                        responseData.setEndDate(UtilsDate.dateToString(data.getEndDate(), Constant.FORMAT_START_END_DATE));
                    }
                    responseData.setDescription(data.getDescription());
                    responseData.setStatus(data.getStatus());
                    responseData.setApprovalStatus(data.getApprovalStatus());
                    responseDatas.add(responseData);
                }
            }

            Map<String, Object> detailLateChargeRuleResponse = new HashMap<>();

            detailLateChargeRuleResponse.put("result",responseDatas);
            detailLateChargeRuleResponse.put("page",pagedData.getMetadata());
            detailLateChargeRuleResponse.put("links",pagedData.getLinks());

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Detail Late Charge", detailLateChargeRuleResponse);
            return new ResponseEntity<>(result, result.getHttpCode());

        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> detailLateChargeRule(Integer lataChargeRuleId, HttpServletRequest request){
        ResponseObject result;
        try{//        information ->(name, description)
            Optional<VW_LATE_CHARGE_RULE> findLateChargeRule = vwLateChargeRuleRepo.findById(lataChargeRuleId);
            if (findLateChargeRule.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "id latecharge rule " + lataChargeRuleId + " is not found" , null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            VW_LATE_CHARGE_RULE latechargeRule = findLateChargeRule.get();
            LinkedHashMap<String, Object> detailResponse = new LinkedHashMap<>();

            detailResponse.put("lateChargeId", latechargeRule.getLatechargeId());
            detailResponse.put("lateChargeRuleId", latechargeRule.getId());
            detailResponse.put("documentNumber", latechargeRule.getDocumentNumber());
            GlobalTypeDTO currency = new GlobalTypeDTO();
            currency.setId((latechargeRule.getCurrencyId()));
            currency.setName(latechargeRule.getCurrency());
            detailResponse.put("currency", currency);
            detailResponse.put("maxAmount", latechargeRule.getMaxAmount());
            if(!ObjectUtils.isEmpty(latechargeRule.getStartDate())){
                detailResponse.put("startDate", UtilsDate.dateToString(latechargeRule.getStartDate(), Constant.FORMAT_START_END_DATE));
            }
            if(!ObjectUtils.isEmpty(latechargeRule.getEndDate())){
                detailResponse.put("endDate", UtilsDate.dateToString(latechargeRule.getEndDate(), Constant.FORMAT_START_END_DATE));
            }else{
                detailResponse.put("endDate", null);
            }
            detailResponse.put("status", latechargeRule.getStatus());
            detailResponse.put("approvalStatus", latechargeRule.getApprovalStatus());
            detailResponse.put("description", latechargeRule.getDescription());

//        list rule formula detail (operation, type, item_name, value_type, value)
            List<VW_LATE_CHARGE_RULE_FORMULA> ruleFormulas = vwLateChargeRuleFormulaRepo.findAllByLatechargeRuleId(latechargeRule.getId());
            List<LinkedHashMap<String, Object>> dataFormula = ruleFormulas.stream()
                    .map(g -> {
                        LinkedHashMap<String, Object> acontact = new LinkedHashMap<>();
                        acontact.put("id", g.getId());
                        acontact.put("lateChargeRuleId", g.getLatechargeRuleId());
                            GlobalTypeDTO ruleOperation = new GlobalTypeDTO();
                            ruleOperation.setId(g.getOperation());
                            ruleOperation.setName(g.getOperationValue());
                        acontact.put("operation", ruleOperation);
                        acontact.put("type", g.getType());
                            GlobalTypeDTO ruleVariable = new GlobalTypeDTO();
                            ruleVariable.setId(g.getVariableName());
                            ruleVariable.setName(g.getVariableNameValue());
                        acontact.put("variableName", ruleVariable);
                        acontact.put("value", g.getValue());
                        acontact.put("valueReal", g.getValueReal());
                        acontact.put("status", g.getStatus());
                        acontact.put("createdDate", g.getCreatedDate());
                        acontact.put("createdBy", g.getCreatedBy());
                        acontact.put("updatedDate", g.getUpdatedDate());
                        acontact.put("updatedBy", g.getUpdatedBy());

                        return acontact;
                    })
                    .collect(Collectors.toList());

            detailResponse.put("listRuleFormula",dataFormula);
            List<VW_LATE_CHARGE_RULE_CONDITION> latechargeRuleConditions = vwLateChargeRuleConditionRepo.findAllByLatechargeRuleId(lataChargeRuleId);
            List<LinkedHashMap<String, Object>> dataCondition = latechargeRuleConditions.stream()
                    .map(g -> {
                        LinkedHashMap<String, Object> acontact = new LinkedHashMap<>();
                        acontact.put("id", g.getId());
                        acontact.put("lateChargeRuleId", g.getLatechargeRuleId());
                            GlobalTypeDTO conditionName = new GlobalTypeDTO();
                            conditionName.setId(g.getName());
                            conditionName.setName(g.getNameValue());
                        acontact.put("name", conditionName);
                            GlobalTypeDTO conditionOperator = new GlobalTypeDTO();
                            conditionOperator.setId(g.getOperator());
                            conditionOperator.setName(g.getOperatorValue());
                        acontact.put("operator", conditionOperator);
                            GlobalTypeDTO conditionDataType = new GlobalTypeDTO();
                            conditionDataType.setId(g.getDataType());
                            conditionDataType.setName(g.getDataTypeValue());
                        acontact.put("dataType", conditionDataType);
                        acontact.put("value", g.getValue());
                        acontact.put("valueReal", g.getValueReal());
                        acontact.put("status", g.getStatus());
                        acontact.put("createdDate", g.getCreatedDate());
                        acontact.put("createdBy", g.getCreatedBy());
                        acontact.put("updatedDate", g.getUpdatedDate());
                        acontact.put("updatedBy", g.getUpdatedBy());
                        return acontact;
                    })
                    .collect(Collectors.toList());
            detailResponse.put("listRuleCondition", dataCondition);

            // list attachment
            Optional<List<M_ATTACHMENT>> attach = mAttachmentRepo
                    .findByReferenceIdAndIsDeletedAndCategoryIgnoreCase(latechargeRule.getId(), Boolean.FALSE, ApprovalCategory.LATE_CHARGE_RULE.name());
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
                    attachDto.setCreatedBy(dtl_.getCreatedBy());
                    attachRule.add(attachDto);
                }
            }
            detailResponse.put("attachments", attachRule);
            detailResponse.put("createdDate", latechargeRule.getCreatedDate());
            detailResponse.put("createdBy", latechargeRule.getCreatedBy());
            detailResponse.put("updatedDate", latechargeRule.getUpdatedDate());
            detailResponse.put("updatedBy", latechargeRule.getUpdatedBy());

            // get current Approval Id
            List<String> appCategory = new ArrayList<>();
            appCategory.add(ApprovalCategory.LATE_CHARGE_RULE.name());
            appCategory.add(ApprovalCategory.INACTIVE_LATE_CHARGE_RULE.name());

            Optional<T_APPROVAL> appId = tApprovalRepo.findFirstByIdTransAndCategoryInAndStatus(lataChargeRuleId.toString(),
                    appCategory, ApprovalStatus.WAITING_FOR_APPROVAL.name());

            if (appId.isPresent()) {

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
                Integer appHist = tApprovalHistoryRepo.findTopSubmittedByRefIdAndCategory(lataChargeRuleId,
                        ApprovalCategory.LATE_CHARGE_RULE.name());

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
                detailResponse.put("apphierId", latechargeRule.getApphierId());
                detailResponse.put("isApprover", Boolean.FALSE);
                detailResponse.put("tAppId", null);
            }


            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Detail Late Charge Rule", detailResponse);
            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> detailLateChargeRuleDraft(Integer lateChargeRuleId){
        ResponseObject result;
        Map<String, Object> detailResponse = new HashMap<>();
        try {
            Optional<M_AM_LATECHARGE_RULE> findLateChargeRule = mAmLateChargeRuleRepo.findById(lateChargeRuleId);
            if(findLateChargeRule.isEmpty()){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            M_AM_LATECHARGE_RULE latechargeRule = findLateChargeRule.get();

            if (latechargeRule.getJsonData() == null) {
                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        "Temporary data for Latecharge Rule with id : " + lateChargeRuleId + ResponseUtils.MESSAGE_NOT_FOUND,
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            if (!latechargeRule.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())
                    && !(latechargeRule.getApprovalStatus().equalsIgnoreCase("DRAFT")
                    || latechargeRule.getApprovalStatus().equalsIgnoreCase("WAITING FOR APPROVAL"))) {
                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        "Status Latecharge must be ACTIVE to view draft data", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            if(!ObjectUtils.isEmpty(latechargeRule.getJsonData())){
                String jsonStr = latechargeRule.getJsonData();
                objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                JsonForUpdateLateChargeRuleDto jsonForUpdateLateChargeRuleDto = objectMapper.readValue(jsonStr, JsonForUpdateLateChargeRuleDto.class);
                detailResponse.put("description", jsonForUpdateLateChargeRuleDto.getDescription());
                detailResponse.put("apphierId",latechargeRule.getApphierId());
                detailResponse.put("lateChargeRuleId",latechargeRule.getId());

                Optional<List<M_ATTACHMENT>> saAttachments = mAttachmentRepo.findByReferenceIdAndIsDraftAndIsDeletedAndCategoryIgnoreCase(latechargeRule.getId(), Boolean.TRUE, Boolean.FALSE, ApprovalCategory.LATE_CHARGE_RULE.name());
                if (saAttachments.isPresent()) {
                    detailResponse.put("attachments",saAttachments.get());
                }
            }

            detailResponse.put("LateChargeMaximumAccount", latechargeRule.getMaxAmount());
            if(!ObjectUtils.isEmpty(latechargeRule.getStartDate())){
                detailResponse.put("startDate", UtilsDate.dateToString(latechargeRule.getStartDate(), Constant.FORMAT_START_END_DATE));
            }
            if(!ObjectUtils.isEmpty(latechargeRule.getEndDate())){
                detailResponse.put("endDate", UtilsDate.dateToString(latechargeRule.getEndDate(), Constant.FORMAT_START_END_DATE));
            }else{
                detailResponse.put("endDate", null);
            }
            detailResponse.put("status", latechargeRule.getStatus());
            detailResponse.put("approvalStatus", latechargeRule.getApprovalStatus());

//        list rule formula detail (operation, type, item_name, value_type, value)
            List<VW_LATE_CHARGE_RULE_FORMULA> ruleFormulas = vwLateChargeRuleFormulaRepo.findAllByLatechargeRuleId(latechargeRule.getId());
            detailResponse.put("ruleFormulaDetail", ruleFormulas);
//        list formula condition (name, operator, datatype, value)
            List<VW_LATE_CHARGE_RULE_CONDITION> latechargeRuleConditions = vwLateChargeRuleConditionRepo.findAllByLatechargeRuleId(lateChargeRuleId);
            detailResponse.put("ruleFormulaCondition", latechargeRuleConditions);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Detail Late Charge Rule", detailResponse);
            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> inactiveLateCharge(InactiveLateChargeDto request){
        ResponseObject result;
        try{
            Optional<M_AM_LATECHARGE> findLateCharge = mAmLateChargeRepo.findById(request.getLateChargeId());
            if(findLateCharge.isEmpty()){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND, "Latecharge with id + " + request.getLateChargeId() + " not found",
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            M_AM_LATECHARGE latechargeData = findLateCharge.get();

            List<M_AM_LATECHARGE_RULE> latechargeRule = mAmLateChargeRuleRepo.findAllByLatechargeIdAndStatusActiveOrWaitingApproval(request.getLateChargeId());
            if(!latechargeRule.isEmpty()){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "You cannot inactivate this latecharge because there is a latecharge rule that are still active/waiting approval", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            Optional<T_AM_SA_LATECHARGE_HEADER> checkActiveSa = tAmSALateChargeHeaderRepo.findTopBylateChargesIdAndStatus(latechargeData.getId(), FlowStatus.ACTIVE.name());
            if(checkActiveSa.isPresent()){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "You can't inactivate late charge. This late charge used by account service agreement.", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
            ObjectMapper mapper = new ObjectMapper();
            String oldValue = mapper.writeValueAsString(latechargeData);
            auditTrail.setOldValue(oldValue);
            if(request.getRemark()==null) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "remark not be empty", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            auditTrail.setRemark(request.getRemark());
            auditTrail.setTableName("M_AM_LATECHARGE");
            auditTrail.setDataId(latechargeData.getId().toString());
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

            if (latechargeData.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())) {
                latechargeData.setStatus(FlowStatus.INACTIVE.name());
                latechargeData.setUpdatedDate(new Date());
                latechargeData.setUpdatedBy(UserDetailUtils.getUsername());
                mAmLateChargeRepo.save(latechargeData);
                auditTrail.setOperation(FlowStatus.INACTIVE.name());
                auditTrail.setNewValue(latechargeData.toString());
                auditTrailRepo.save(auditTrail);
            } else if(latechargeData.getStatus().equalsIgnoreCase(FlowStatus.INACTIVE.name())) {
                latechargeData.setStatus(FlowStatus.ACTIVE.name());
                latechargeData.setUpdatedDate(new Date());
                latechargeData.setUpdatedBy(UserDetailUtils.getUsername());
                mAmLateChargeRepo.save(latechargeData);
                auditTrail.setOperation(FlowStatus.INACTIVE.name());
                auditTrail.setNewValue(latechargeData.toString());
                auditTrailRepo.save(auditTrail);
            }

            LinkedHashMap<String, Object> successTosResponse = new LinkedHashMap<>();
            successTosResponse.put("lateChargeId", latechargeData.getId());
            successTosResponse.put("updatedDate", latechargeData.getUpdatedDate());
            successTosResponse.put("updatedBy", latechargeData.getUpdatedBy());

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success request " + latechargeData.getStatus().toLowerCase() + " late charge", successTosResponse);

            return new ResponseEntity<>(result, result.getHttpCode());

        }catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> inactiveLateChargeRule(InactiveLateChargeDto request, HttpServletRequest httpServletRequest){
        ResponseObject result;
        try{
            boolean isSubmiter = approvalServices.checkIsSubmitter(httpServletRequest, request.getAppHierId());
            if (!isSubmiter) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, CommonVariables.NOT_SUBMITTER,
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            Optional<M_AM_LATECHARGE_RULE> findLateCharge = mAmLateChargeRuleRepo.findById(request.getLateChargeRuleId());
            if(findLateCharge.isEmpty()){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, ResponseUtils.MESSAGE_NOT_FOUND,
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            M_AM_LATECHARGE_RULE latechargeRuleData = findLateCharge.get();
            if (latechargeRuleData.getApprovalStatus().equalsIgnoreCase(ApprovalStatus.WAITING_APPROVAL.name())){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Data is Waiting For Approval Cannot change", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            if(latechargeRuleData.getStatus().equalsIgnoreCase(FlowStatus.INACTIVE.name())){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
                        "Late Charge cannot be inactivated", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            if(!ObjectUtils.isEmpty(latechargeRuleData.getApprovalStatus())) {
                if (latechargeRuleData.getApprovalStatus().equalsIgnoreCase(ApprovalStatus.WAITING_APPROVAL.name())) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Late Charge cannot be inactivated because it has been on approval request", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
            }else {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Late Charge rule approval status is null", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            JsonForUpdateLateChargeRuleDto jsonData = new JsonForUpdateLateChargeRuleDto();
            jsonData.setDescription(null);
            jsonData.setEndDate(request.getEndDate());
            String json = "";
            json = new Gson().toJson(jsonData);
            latechargeRuleData.setJsonData(json);

            latechargeRuleData.setUpdatedBy(UserDetailUtils.getUsername());
            latechargeRuleData.setUpdatedDate(new Date());
            latechargeRuleData.setApprovalStatus(ApprovalStatus.WAITING_APPROVAL.name());
            mAmLateChargeRuleRepo.save(latechargeRuleData);

            Integer tappId = approvalServices.addApprovalFlowR(request.getAppHierId(),
                    ApprovalCategory.INACTIVE_LATE_CHARGE_RULE.name(),
                    latechargeRuleData.getId().toString(), request.getRemark());

            approvalServices.setApprovalHistory(tappId, latechargeRuleData.getId(), request.getRemark(),
                    ApprovalCategory.LATE_CHARGE_RULE.name(), SUBMIT, httpServletRequest);

            LinkedHashMap<String, Object> successTosResponse = new LinkedHashMap<>();
            successTosResponse.put("lateChargeId", latechargeRuleData.getLatechargeId());
            successTosResponse.put("lateChargeRuleId", latechargeRuleData.getId());
            successTosResponse.put("appHierId", request.getAppHierId());
            successTosResponse.put("description", request.getRemark());
            successTosResponse.put("updatedDate", latechargeRuleData.getUpdatedDate());
            successTosResponse.put("updatedBy", latechargeRuleData.getUpdatedBy());

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success request inactive late charge", successTosResponse);

            return new ResponseEntity<>(result, result.getHttpCode());

        }catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class, readOnly = false)
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> approveLateChargeRule(ApprovalLateChargeDto approvalLateChargeDto, HttpServletRequest httpServletRequest)
            throws IllegalArgumentException, JSONException, JsonProcessingException{
        ResponseObject result = new ResponseObject();
        try{
            Optional<M_AM_LATECHARGE_RULE> findLateChargeRule = mAmLateChargeRuleRepo.findById(approvalLateChargeDto.getLateChargeRuleId());
            if(findLateChargeRule.isEmpty()){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND, ResponseUtils.MESSAGE_NOT_FOUND,
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            M_AM_LATECHARGE_RULE latechargeRuleData = findLateChargeRule.get();
            Optional<List<M_ATTACHMENT>> attachOpt = mAttachmentRepo
                    .findByReferenceIdAndIsDraftAndIsDeletedAndCategoryIgnoreCase(
                            latechargeRuleData.getId(), Boolean.TRUE, Boolean.FALSE, ApprovalCategory.LATE_CHARGE_RULE.name());

            boolean isFinal = approvalServices.actionNextFlowApproval(approvalLateChargeDto.getApprovalId(), approvalLateChargeDto.getDescription(),
                    ApprovalStatus.APPROVE);
            if(isFinal){
                if(approvalLateChargeDto.getAction().equalsIgnoreCase(ApprovalStatus.REJECT.name())) {
                    // LATECHARGE
                    latechargeRuleData.setApprovalStatus(FlowStatus.REJECTED.name());
                    latechargeRuleData.setUpdatedDate(new Date());
                    latechargeRuleData.setUpdatedBy(UserDetailUtils.getUsername());
                    latechargeRuleData.setJsonData(null);
                    mAmLateChargeRuleRepo.save(latechargeRuleData);
                    approvalServices.actionNextFlowApproval(approvalLateChargeDto.getApprovalId(), approvalLateChargeDto.getDescription(),
                            ApprovalStatus.REJECT);

                    // ATTACHMENT
//                    if (attachOpt.isPresent() && !attachOpt.get().isEmpty()) {
//                        for (M_ATTACHMENT attach : attachOpt.get()) {
//                            attach.setIsDeleted(Boolean.TRUE);
//                            attach.setUpdatedBy(UserDetailUtils.getUsername());
//                            attach.setUpdatedDate(new Date());
//                            mAttachmentRepo.save(attach);
//                        }
//                    }

                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            "Your data has been rejected", null);
                }else {
                    if(FlowStatus.ACTIVE.name().equalsIgnoreCase(latechargeRuleData.getStatus())) {

                        // UPDATE DRAFT
                        if(!ObjectUtils.isEmpty(latechargeRuleData.getJsonData())){
                            boolean updateData = updateApprovedData(latechargeRuleData.getId(), httpServletRequest);
                            if(updateData){
                                latechargeRuleData.setJsonData(null);
                            }
                        }
                        // UPDATE ATTACHMENT
                        if (attachOpt.isPresent() && !attachOpt.get().isEmpty()) {
                            for (M_ATTACHMENT attach : attachOpt.get()) {
                                attach.setIsDraft(Boolean.FALSE);
                                attach.setUpdatedBy(UserDetailUtils.getUsername());
                                attach.setUpdatedDate(new Date());
                                mAttachmentRepo.save(attach);
                            }
                        }
                    }

                    latechargeRuleData.setStatus(FlowStatus.ACTIVE.name());
                    latechargeRuleData.setApprovalStatus(ApprovalStatus.APPROVED.name());
                    mAmLateChargeRuleRepo.save(latechargeRuleData);
                    List<M_AM_LATECHARGE_RULE_CONDITION> allCondition = mAmLateChargeRuleConditionRepo.findAllByLatechargeRuleId(latechargeRuleData.getId());
                    for(M_AM_LATECHARGE_RULE_CONDITION updateCondition : allCondition) {
                        updateCondition.setStatus(FlowStatus.ACTIVE.name());
                        updateCondition.setUpdatedDate(new Date());
                        updateCondition.setUpdatedBy(UserDetailUtils.getUsername());
                        mAmLateChargeRuleConditionRepo.save(updateCondition);
                    }
                    List<M_AM_LATECHARGE_RULE_FORMULA> allFormula = mAmLateChargeRuleFormulaRepo.findAllByLatechargeRuleId(latechargeRuleData.getId());
                    for(M_AM_LATECHARGE_RULE_FORMULA updateFormula : allFormula) {
                        updateFormula.setStatus(FlowStatus.ACTIVE.name());
                        updateFormula.setUpdatedDate(new Date());
                        updateFormula.setUpdatedBy(UserDetailUtils.getUsername());
                        mAmLateChargeRuleFormulaRepo.save(updateFormula);
                    }
                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            "Late charge rule has been approved", null);
                }
            }

            approvalServices.setApprovalHistory(approvalLateChargeDto.getApprovalId(), latechargeRuleData.getId(), approvalLateChargeDto.getDescription(),
                    ApprovalCategory.LATE_CHARGE_RULE.name(), approvalLateChargeDto.getAction(), httpServletRequest);
            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class, readOnly = false)
    public ResponseEntity<ResponseObject> approveInactiveLateCharge(ApprovalLateChargeDto inactiveApprovalRequestDto, HttpServletRequest httpServletRequest){
        ResponseObject result;
        try{
            Optional<M_AM_LATECHARGE> findLateCharge = mAmLateChargeRepo.findById(inactiveApprovalRequestDto.getLateChargeId());
            if(findLateCharge.isEmpty()){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, CommonVariables.NOT_SUBMITTER,
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            M_AM_LATECHARGE latechargeData = findLateCharge.get();

            if(inactiveApprovalRequestDto.getAction().equalsIgnoreCase(ApprovalStatus.REJECT.name())){
                approvalServices.actionNextFlowApproval(inactiveApprovalRequestDto.getApprovalId(), inactiveApprovalRequestDto.getDescription(),
                        ApprovalStatus.REJECT);
                latechargeData.setStatus(FlowStatus.REJECTED.name());
                latechargeData.setUpdatedDate(new Date());
                latechargeData.setUpdatedBy(UserDetailUtils.getUsername());
                mAmLateChargeRepo.save(latechargeData);
                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        "Late Charge Inactive request has been rejected", null);
            }else{
                boolean isFinal = approvalServices.actionNextFlowApproval(inactiveApprovalRequestDto.getApprovalId(), inactiveApprovalRequestDto.getDescription(),
                        ApprovalStatus.APPROVE);
                if (isFinal) {
                    latechargeData.setUpdatedDate(new Date());
                    latechargeData.setUpdatedBy(UserDetailUtils.getUsername());
                    latechargeData.setStatus(FlowStatus.INACTIVE.name());
                    mAmLateChargeRepo.save(latechargeData);
                }
                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        ResponseUtils.MESSAGE_SUCCESS, null);
            }

            approvalServices.setApprovalHistory(inactiveApprovalRequestDto.getApprovalId(), latechargeData.getId(), inactiveApprovalRequestDto.getDescription(),
                    ApprovalCategory.LATE_CHARGE_RULE.name(), inactiveApprovalRequestDto.getAction(), httpServletRequest);

            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class, readOnly = false)
    public ResponseEntity<ResponseObject> approveInactiveLateChargeRule(ApprovalLateChargeDto inactiveLateChargeRuleApprovalRequestDto, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        ResponseObject result = new ResponseObject();
        try{
            Optional<M_AM_LATECHARGE_RULE> findLateChargeRule = mAmLateChargeRuleRepo.findById(inactiveLateChargeRuleApprovalRequestDto.getLateChargeRuleId());
            if(findLateChargeRule.isEmpty()){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, CommonVariables.NOT_SUBMITTER,
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            M_AM_LATECHARGE_RULE latechargeRuleData = findLateChargeRule.get();
            boolean isFinal = approvalServices.actionNextFlowApproval(inactiveLateChargeRuleApprovalRequestDto.getApprovalId(), inactiveLateChargeRuleApprovalRequestDto.getDescription(),
                    ApprovalStatus.APPROVE);
            if(isFinal){
                if(inactiveLateChargeRuleApprovalRequestDto.getAction().equalsIgnoreCase(ApprovalStatus.REJECT.name())){
                    approvalServices.actionNextFlowApproval(inactiveLateChargeRuleApprovalRequestDto.getApprovalId(), inactiveLateChargeRuleApprovalRequestDto.getDescription(),
                            ApprovalStatus.REJECT);
                    latechargeRuleData.setApprovalStatus(FlowStatus.REJECTED.name());
                    latechargeRuleData.setUpdatedDate(new Date());
                    latechargeRuleData.setUpdatedBy(UserDetailUtils.getUsername());
                    latechargeRuleData.setJsonData(null);
                    mAmLateChargeRuleRepo.save(latechargeRuleData);
                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            "Your data has been rejected", null);
                }else {
                    String jsonStr = latechargeRuleData.getJsonData();
                    objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                    JsonForUpdateLateChargeRuleDto jsonObject = objectMapper.readValue(jsonStr, JsonForUpdateLateChargeRuleDto.class);
                    latechargeRuleData.setEndDate(jsonObject.getEndDate()!=null?UtilsDate.stringToDate(jsonObject.getEndDate(), "dd-MM-yyyy"):null);
                    latechargeRuleData.setUpdatedDate(new Date());
                    latechargeRuleData.setUpdatedBy(UserDetailUtils.getUsername());
                    latechargeRuleData.setApprovalStatus(ApprovalStatus.APPROVED.name());
                    latechargeRuleData.setStatus(FlowStatus.INACTIVE.name());
                    latechargeRuleData.setJsonData(null);
                    mAmLateChargeRuleRepo.save(latechargeRuleData);

                    List<M_AM_LATECHARGE_RULE_CONDITION> allCondition = mAmLateChargeRuleConditionRepo.findAllByLatechargeRuleId(latechargeRuleData.getId());
                    for(M_AM_LATECHARGE_RULE_CONDITION updateCondition : allCondition) {
                        updateCondition.setStatus(FlowStatus.INACTIVE.name());
                        updateCondition.setUpdatedDate(new Date());
                        updateCondition.setUpdatedBy(UserDetailUtils.getUsername());
                        mAmLateChargeRuleConditionRepo.save(updateCondition);
                    }
                    List<M_AM_LATECHARGE_RULE_FORMULA> allFormula = mAmLateChargeRuleFormulaRepo.findAllByLatechargeRuleId(latechargeRuleData.getId());
                    for(M_AM_LATECHARGE_RULE_FORMULA updateFormula : allFormula) {
                        updateFormula.setStatus(FlowStatus.INACTIVE.name());
                        updateFormula.setUpdatedDate(new Date());
                        updateFormula.setUpdatedBy(UserDetailUtils.getUsername());
                        mAmLateChargeRuleFormulaRepo.save(updateFormula);
                    }
                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            "Your data has been Approved", null);
                }
            }
            approvalServices.setApprovalHistory(inactiveLateChargeRuleApprovalRequestDto.getApprovalId(), latechargeRuleData.getId(), inactiveLateChargeRuleApprovalRequestDto.getDescription(),
                    ApprovalCategory.LATE_CHARGE_RULE.name(), inactiveLateChargeRuleApprovalRequestDto.getAction(), httpServletRequest);
            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> listPremiseCountry(){
        ResponseObject result;
        try {
            List<DropDownCriteriaDto> dataDtos = criteriaServices.getCriteriaLocation(Constant.LOCATION_TYPE_COUNTRY, "", null);
            if (dataDtos.isEmpty()){
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, null), HttpStatus.NOT_FOUND);
            }
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    ResponseUtils.MESSAGE_SUCCESS, dataDtos);

            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> listPremiseHaveParents(String locationType, Integer locationParent){
        ResponseObject result;
        try {
            List<DropDownCriteriaDto> dataDtos = criteriaServices.getCriteriaLocation("X", locationType, locationParent);
            if (dataDtos.isEmpty()){
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, null), HttpStatus.NOT_FOUND);
            }
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    ResponseUtils.MESSAGE_SUCCESS, dataDtos);

            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> listSor(){
        ResponseObject result;
        try {
            List<DropDownCriteriaDto> dataDtos = criteriaServices.getCriteriaAreaAndSor(Constant.SOR_TYPE);
            if (dataDtos.isEmpty()){
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, null), HttpStatus.NOT_FOUND);
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    ResponseUtils.MESSAGE_SUCCESS, dataDtos);

            return new ResponseEntity<>(result, result.getHttpCode());

        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> listCostCenter(){
        ResponseObject result;
        try {
            List<DropDownCriteriaDto> dataDtos = criteriaServices.getCriteriaAreaAndSor(Constant.COST_CENTER_TYPE);
            if (dataDtos.isEmpty()){
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, null), HttpStatus.NOT_FOUND);
            }
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    ResponseUtils.MESSAGE_SUCCESS, dataDtos);

            return new ResponseEntity<>(result, result.getHttpCode());

        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> listAccountGroupType(String groupName, Integer parentId){
        ResponseObject result;
        try {
            List<R_GLOBAL_TYPE_VALUE> accountGroupData = globalTypeValueService.getGlobalTypeByParentValue(groupName, parentId);
            if(accountGroupData.isEmpty()){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            List<GlobalTypeResponseDto> responseDatas = new ArrayList<>();
            accountGroupData.forEach(accountType->{
                GlobalTypeResponseDto responseData = new GlobalTypeResponseDto();
                responseData.setId(accountType.getGlbTypeValId());
                responseData.setName(accountType.getName());
                responseDatas.add(responseData);
            });

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    ResponseUtils.MESSAGE_OK, responseDatas);
            return new ResponseEntity<>(result, result.getHttpCode());

        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> listAccountNumber(){
        ResponseObject result;
        try {
            List<VW_ACCOUNT_INFORMATION> allAccountNumber = vwAccountInfoRepo.findAllByAccountStatus(FlowStatus.ACTIVE.name());

            if(allAccountNumber.isEmpty()){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            List<GlobalTypeResponseDto> responseDatas = new ArrayList<>();
            allAccountNumber.forEach(accountType->{
                GlobalTypeResponseDto responseData = new GlobalTypeResponseDto();
                responseData.setId(accountType.getAccountId());
                responseData.setName(accountType.getAccountNumber()+" - "+accountType.getAccountName());
                responseDatas.add(responseData);
            });

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    ResponseUtils.MESSAGE_OK, responseDatas);
            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> listClassificationType(){
        ResponseObject result;
        try {
            List<M_ACCOUNTING_RULE> findClassificationTypeData = mAccountingRulesRepo.findAllByStatus(FlowStatus.ACTIVE.name());

            if (findClassificationTypeData.isEmpty()){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            List<GlobalTypeResponseDto> responseDatas = new ArrayList<>();
            findClassificationTypeData.forEach(classificationType->{
                GlobalTypeResponseDto responseData = new GlobalTypeResponseDto();
                responseData.setId(classificationType.getMasterAccountingRuleId());
                responseData.setName(classificationType.getClassificationTypeName());
                responseDatas.add(responseData);
            });

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    ResponseUtils.MESSAGE_OK, responseDatas);
            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> booleanDdl(){
        ResponseObject result;
        try{
            Map<String, Boolean> responseData = new HashMap<>();
            responseData.put("Y", Boolean.TRUE);
            responseData.put("N", Boolean.FALSE);
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "get data", responseData);
            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class, readOnly = false)
    private Boolean updateApprovedData(Integer lateChargeRuleId, HttpServletRequest httpServletRequest)throws JsonProcessingException {
        try {
            Optional<M_AM_LATECHARGE_RULE> findLateChargeRule = mAmLateChargeRuleRepo.findById(lateChargeRuleId);
            if (findLateChargeRule.isEmpty()) {
                return Boolean.FALSE;
            }
            M_AM_LATECHARGE_RULE latechargeRule = findLateChargeRule.get();
            String jsonStr = latechargeRule.getJsonData();
            objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            JsonForUpdateLateChargeRuleDto jsonObject = objectMapper.readValue(jsonStr, JsonForUpdateLateChargeRuleDto.class);
            latechargeRule.setDescription(jsonObject.getDescription()!=null?jsonObject.getDescription():null);
            latechargeRule.setUpdatedDate(new Date());
            latechargeRule.setUpdatedBy(UserDetailUtils.getUsername());
            mAmLateChargeRuleRepo.save(latechargeRule);
            return Boolean.TRUE;
        }catch (Exception e){
            logger.error(CommonVariables.ERROR_IN, e.getMessage(), e);
            throw e;
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

    public ResponseEntity<InputStreamResource> downloadFilter(MaterialTablePagingRequest pagingData) {
        try {
            Page<VW_MASTER_LATE_CHARGE> data;
            Map<String, Object> filter = new HashMap<>();
            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key+"~"+value);
                }
            }
            if (pagingData.getPage() != null && pagingData.getSize() != null) {
                long maxData = StreamSupport.stream(vwMasterLateChargeRepo.findAll().spliterator(), false).count();
                pagingData.setSize((int) maxData);
                pagingData.setPage(1);
            }
            if (!pagingData.getSearch().isEmpty()) {
                data = this.vwMasterLateChargeRepo.findAll(this.vwMasterLateChargeRepo.getSpecificationFromFilters(pagingData, filter),
                        PagingUtils.getPaging(pagingData));
            } else {
                data = this.vwMasterLateChargeRepo.findAll(this.vwMasterLateChargeRepo.getSpecificationDefault(filter),
                        PagingUtils.getPaging(pagingData));
            }
            List<VW_MASTER_LATE_CHARGE> listAction = data.getContent();
            List<LinkedHashMap<String, Object>> allData = new ArrayList<>();
            Integer no = 1;
            HttpHeaders headers = new HttpHeaders();
            if(data.hasContent()) {
                for (VW_MASTER_LATE_CHARGE a : listAction) {
                    StringBuilder criteriaValue = new StringBuilder();
                    DecimalFormat decimalFormat = new DecimalFormat("##0.00");
                    List<VW_LATE_CHARGE_RULE> dataRule = vwLateChargeRuleRepo.findAllByLatechargeId(a.getLateChargeId());
                    if(!dataRule.isEmpty()) {
                        for(VW_LATE_CHARGE_RULE rule : dataRule) {
                            LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                            response.put("NO", no);
                            response.put("LATE CHARGE NAME", a.getName());
                            response.put("CURRENCY", a.getCurrency());
                            String formatCriteria = a.getCriteria();
                            if(StringUtils.hasValue(a.getCriteria()) && !a.getCriteria().contains("All")) {
                                formatCriteria = a.getCriteria() + ", Start Date, End Date, Description";
                            }
                            criteriaValue = this.getCriteriaValue(formatCriteria, a.getLateChargeId());
                            response.put("CRITERIA", formatCriteria);
                            response.put("CRITERIA VALUE", criteriaValue);
                            response.put("CURRENT LATE CHARGE MAXIMUM AMOUNT", a.getMaxAmount());
                            if(a.getLateChargeRuleId()!=null) {
                                response.put("CURRENT LATE CHARGE RULE FORMULA", a.getFormula());
                            } else {
                                response.put("CURRENT LATE CHARGE RULE FORMULA", null);
                            }
                            response.put("DESCRIPTION", a.getDescription());
                            response.put("STATUS", capitalizeFully(a.getStatus()));
                            response.put("DOCUMENT NUMBER", rule.getDocumentNumber());
                            response.put("LATE CHARGE MAX AMOUNT", rule.getMaxAmount());
                            response.put("FORMULA", rule.getFormula());
                            response.put("CONDITION", this.getRuleCondition(rule.getId()));
                            response.put("START DATE", CommonHelper.dateToString(Constant.FORMAT_START_END_DATE, rule.getStartDate()));
                            response.put("END DATE", CommonHelper.dateToString(Constant.FORMAT_START_END_DATE, rule.getEndDate()));
                            response.put("DESCRIPTION LATE CHARGE RULE", rule.getDescription());
                            response.put("STATUS LATE CHARGE RULE", capitalizeFully(rule.getStatus()));
                            response.put("STATUS APPROVAL LATE CHARGE RULE", capitalizeFullyApproval(rule.getApprovalStatus()));
                            allData.add(response);
                            no = no + 1;
                        }
                    } else {
                        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                        response.put("NO", no);
                        response.put("LATE CHARGE NAME", a.getName());
                        response.put("CURRENCY", a.getCurrency());
                        String formatCriteria = a.getCriteria();
                        if(StringUtils.hasValue(a.getCriteria()) && !a.getCriteria().contains("All")) {
                            formatCriteria = a.getCriteria() + ", Start Date, End Date, Description";
                        }
                        criteriaValue = this.getCriteriaValue(formatCriteria, a.getLateChargeId());
                        response.put("CRITERIA", formatCriteria);
                        response.put("CRITERIA VALUE", criteriaValue);
                        response.put("CURRENT LATE CHARGE MAXIMUM AMOUNT", a.getMaxAmount());
                        if(a.getLateChargeRuleId()!=null) {
                            response.put("CURRENT LATE CHARGE RULE FORMULA", a.getFormula());
                        } else {
                            response.put("CURRENT LATE CHARGE RULE FORMULA", null);
                        }
                        response.put("DESCRIPTION", a.getDescription());
                        response.put("STATUS", capitalizeFully(a.getStatus()));
                        response.put("DOCUMENT NUMBER", null);
                        response.put("LATE CHARGE MAX AMOUNT", null);
                        response.put("FORMULA", null);
                        response.put("CONDITION", null);
                        response.put("START DATE", null);
                        response.put("END DATE", null);
                        response.put("DESCRIPTION LATE CHARGE RULE", null);
                        response.put("STATUS LATE CHARGE RULE", null);
                        response.put("STATUS APPROVAL LATE CHARGE RULE", null);
                        allData.add(response);
                        no = no + 1;
                    }
                }
                ByteArrayInputStream in = DownloadToExcel.downloadsFiles(allData, "Excel");
                headers.add(Constant.CONTENT_DISPOSITION, "attachment; filename=" +
                        "LATECHARGE_" + UtilsDate.dateToString(new Date(), "yyyyMMddHHmmss") + ".xlsx");
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

    public ResponseEntity<ResponseObject> listCategory(HttpServletRequest httpServletRequest) {
        ResponseObject result;
        try {
            List<LinkedHashMap<String, Object>> allData = globalTypeValueService.getGlobalTypeOther("ATTACHMENT_CATEGORY_LATECHARGE_TAXIMP_RULE", httpServletRequest);
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

    public ResponseEntity<ResponseObject> getApprovalHistory(Integer refId) {

        List<String> appCategory = new ArrayList<>();
        appCategory.add(ApprovalCategory.LATE_CHARGE_RULE.name());
        appCategory.add(ApprovalCategory.INACTIVE_LATE_CHARGE_RULE.name());

        var resp = approvalServices.getApprovalHistory(refId, ApprovalCategory.LATE_CHARGE_RULE.name(), appCategory);
        if (resp != null) {
            ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    ResponseUtils.MESSAGE_SUCCESS, resp);

            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, "Data not found",
                ResponseUtils.DATA_EMPTY);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    public String getFormula (Integer latechargeRuleId) {
        StringBuilder result = new StringBuilder();
        List<String> variable = new ArrayList<>();
        List<String> operation = new ArrayList<>();
        Float value = null;
        String formulaValue = "data formula not found";
        List<M_AM_LATECHARGE_RULE_FORMULA> getLateChargeRule = mAmLateChargeRuleFormulaRepo.findAllByLatechargeRuleIdAndStatus(latechargeRuleId);
        if(!getLateChargeRule.isEmpty()){
            for (M_AM_LATECHARGE_RULE_FORMULA formula : getLateChargeRule) {
                if (formula.getType().equalsIgnoreCase("CONSTANT")) {
                    value = formula.getValue();
                }
                if (formula.getOperation() != null) {
                    Optional<R_GLOBAL_TYPE_VALUE> getOperation = rGlobalTypeValueRepo.findByGlbTypeValId(formula.getOperation());
                    if (getOperation.get().getName().equalsIgnoreCase("SUBSTRACT")) {
                        operation.add(" - ");
                    } else if (getOperation.get().getName().equalsIgnoreCase("ADDITION")) {
                        operation.add(" + ");
                    } else if (getOperation.get().getName().equalsIgnoreCase("MULTIPLY")) {
                        operation.add(" * ");
                    } else if (getOperation.get().getName().equalsIgnoreCase("DIVIDE")) {
                        operation.add(" / ");
                    }
                }

                if (formula.getType().equalsIgnoreCase("VARIABLE")) {
                    Optional<R_GLOBAL_TYPE_VALUE> getVariable = rGlobalTypeValueRepo.findByGlbTypeValId(formula.getVariableName());
                    variable.add(getVariable.isPresent() ? getVariable.get().getName() : null);
                }
            }
            if(value==null) {
                for (int i = 0; i < variable.size(); i++) {

                    if (i < variable.size()) {
                        result.append(variable.get(i));
                    }

                    if (i < operation.size()) {
                        result.append(operation.get(i));
                    }
                }
                formulaValue = result.toString();
            } else {
                for (int i = 0; i < variable.size(); i++) {

                    if (i < operation.size()) {
                        result.append(operation.get(i));
                    }

                    if (i < variable.size()) {
                        result.append(variable.get(i));
                    }
                }
                formulaValue = value.toString()+ result;
            }
        }
        return formulaValue;
    }

    public ResponseEntity<ResponseObject> getAccountCategory() {
        logger.info("Find Account Category");
        try {
            List<LinkedHashMap<String, Object>> allData = globalTypeValueService.getGlobalTypeCriteria("Account Category");
            ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_OK, allData);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // utils
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

    public StringBuilder getRuleCondition(Integer ruleId) {
        // condition
        StringBuilder condition = new StringBuilder();
        List<VW_LATE_CHARGE_RULE_CONDITION> allDataCdt = vwLateChargeRuleConditionRepo.findAllByLatechargeRuleIdOrderByIdAsc(ruleId);
        Integer i = 0;
        for(VW_LATE_CHARGE_RULE_CONDITION cdt : allDataCdt) {
            if(i>0) {
                condition.append(", ");
            }
            Optional<R_GLOBAL_TYPE_VALUE> optName = globalTypeValueService.getOptionalGlobalTypeByGlbTypeValId("Late Charge Component", cdt.getName());
            condition.append(optName.map(R_GLOBAL_TYPE_VALUE::getName).orElse(null));
            Optional<R_GLOBAL_TYPE_VALUE> optOperator = globalTypeValueService.getOptionalGlobalTypeByGlbTypeValId("Math Equation", cdt.getOperator());
            if(optOperator.isPresent()) {
                switch (optOperator.get().getName()) {
                    case "GREATER THAN" :
                        condition.append(" > " + cdt.getValue().toString());
                        break;
                    case "LESS THAN" :
                        condition.append(" < " + cdt.getValue().toString());
                        break;
                    case "EQUALS" :
                        condition.append(" = " + cdt.getValue().toString());
                        break;
                }
            }
            i++;
        }

        return condition;
    }

    public StringBuilder getCriteriaValue(String criteriaData, Integer id) {
        StringBuilder criteriaValue = new StringBuilder();
        if(StringUtils.hasValue(criteriaData)) {
            List<String> criteria = Arrays.asList(criteriaData.split("\\s*,\\s*"));
            //looping data
            List<VW_LATE_CHARGE_CRITERIA_DATA_REAL> listCriteriaData = vwLateChargeCriteriaDataRepo.findAllByLateChargeId(id);
            Integer count = 0;
            for(VW_LATE_CHARGE_CRITERIA_DATA_REAL vwData : listCriteriaData) {
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
