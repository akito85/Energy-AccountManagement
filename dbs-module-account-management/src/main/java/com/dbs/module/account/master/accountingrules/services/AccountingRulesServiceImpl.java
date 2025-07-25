package com.dbs.module.account.master.accountingrules.services;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.*;
import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNTING_RULE;
import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import com.dbs.database.crm.entities.usermanagement.M_USER;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.accountmanagement.MAccountingRulesRepo;
import com.dbs.database.crm.repositories.usermanagement.AuditTrailRepo;
import com.dbs.database.crm.repositories.usermanagement.MUserRepo;
import com.dbs.module.account.master.accountingrules.dto.AccountingRulesDTO;
import com.dbs.module.account.master.accountingrules.dto.AccountingRulesResponseDTO;
import com.dbs.module.account.utils.ConstantAccount;
import com.dbs.module.account.utils.UtilsAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;

import static com.dbs.common.base.utils.CommonHelper.removeSpace;
import static com.dbs.common.library.utils.StringUtils.capitalizeFully;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class AccountingRulesServiceImpl {
    
    private static final Logger logger = LoggerFactory.getLogger(AccountingRulesServiceImpl.class);
    @Autowired
    private MAccountingRulesRepo accountingRulesRepo;
    @Autowired
    private Validator validator;
    @Autowired
    private MUserRepo mUserRepo;
    @Autowired
    private AuditTrailRepo auditTrailRepo;
    @Autowired
    private GlobalTypeValueService globalTypeValueService;
    @Autowired
    private ObjectMapper objectMapper;


    public ResponseEntity<ResponseObject> create(AccountingRulesDTO request) {
        try {

            ResponseEntity<ResponseObject> validate = this.validateCreate(Boolean.FALSE, request);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            M_ACCOUNTING_RULE mAccountingRule = new M_ACCOUNTING_RULE();
            mAccountingRule.setClassificationTypeName(request.getClassificationTypeName().strip());
            mAccountingRule.setCode(request.getCode().strip());
            mAccountingRule.setReceivableAccount(request.getReceivableAccount().strip());
            mAccountingRule.setRevenueAccount(request.getRevenueAccount().strip());
            mAccountingRule.setDescription(request.getDescription());
            mAccountingRule.setEntityId(UserDetailUtils.getUserEntity());
            mAccountingRule.setStatus(FlowStatus.ACTIVE.name());
            mAccountingRule.setCreatedBy(UserDetailUtils.getUsername());
            mAccountingRule.setCreatedDate(new Date());
            M_ACCOUNTING_RULE newAccountingRule = accountingRulesRepo.save(mAccountingRule);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                    UtilsAccount.messageSuccess(ConstantAccount.CREATE, ConstantAccount.MASTER_ACCOUNTINGRULES), newAccountingRule), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateCreate(Boolean api, AccountingRulesDTO request) {
        try {
            var violations = validator.validate(request);
            if (!violations.isEmpty()) {
                List<Map<String, Object>> violationHeaderList = new ArrayList<>();
                List<String> validateHeader = new ArrayList<>();
                for (var violation : violations) {
                    logger.error(violation.getMessage());
                    Map<String, Object> data = new HashMap<>();
                    validateHeader.add(violation.getMessage());
                    data.put(violation.getPropertyPath().toString(), violation.getMessage());
                    violationHeaderList.add(data);
                }
                ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, validateHeader.get(0), violationHeaderList);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            // VALIDASI UNIQUE
            Optional<M_ACCOUNTING_RULE> cekClassUnique = accountingRulesRepo.findTopByClassificationTypeNameIgnoreCase(removeSpace(request.getClassificationTypeName()));
            Optional<M_ACCOUNTING_RULE> cekCodeUnique = accountingRulesRepo.findTopByCodeIgnoreCase(removeSpace(request.getCode()));
            if(cekClassUnique.isPresent() && cekCodeUnique.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Classification type and Code already exist!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
            if(cekClassUnique.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Classification type already exist!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
            if(cekCodeUnique.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Code already exist!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }

            return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.MASTER_ACCOUNTINGRULES);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> update(AccountingRulesDTO request) {
        try {

            ResponseEntity<ResponseObject> validate = this.validateUpdate(Boolean.FALSE, request);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            Optional<M_ACCOUNTING_RULE> mAccountingRule = accountingRulesRepo.findByMasterAccountingRuleId(request.getMasterAccountingRuleId());
            M_ACCOUNTING_RULE data = mAccountingRule.get();
            data.setClassificationTypeName(request.getClassificationTypeName().strip());
            data.setCode(request.getCode().strip());
            data.setReceivableAccount(request.getReceivableAccount().strip());
            data.setRevenueAccount(request.getRevenueAccount().strip());
            data.setDescription(request.getDescription());
            data.setUpdatedBy(UserDetailUtils.getUsername());
            data.setUpdatedDate(new Date());
            accountingRulesRepo.save(data);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.UPDATE, ConstantAccount.MASTER_ACCOUNTINGRULES), data), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateUpdate(Boolean api, AccountingRulesDTO request) {
        try {
            var violations = validator.validate(request);
            if (!violations.isEmpty()) {
                List<Map<String, Object>> violationHeaderList = new ArrayList<>();
                List<String> validateHeader = new ArrayList<>();
                for (var violation : violations) {
                    logger.error(violation.getMessage());
                    Map<String, Object> data = new HashMap<>();
                    validateHeader.add(violation.getMessage());
                    data.put(violation.getPropertyPath().toString(), violation.getMessage());
                    violationHeaderList.add(data);
                }
                ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, validateHeader.get(0), violationHeaderList);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            Optional<M_ACCOUNTING_RULE> mAccountingRule = accountingRulesRepo.findByMasterAccountingRuleId(request.getMasterAccountingRuleId());
            if (!mAccountingRule.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        UtilsAccount.messageDataNotFound(ConstantAccount.MASTER_ACCOUNTINGRULES, request.getMasterAccountingRuleId()), ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
            }
            Optional<M_ACCOUNTING_RULE> cekClassUnique = accountingRulesRepo.findTopByClassificationTypeNameIgnoreCase(request.getClassificationTypeName().strip());
            Optional<M_ACCOUNTING_RULE> cekCodeUnique = accountingRulesRepo.findTopByCodeIgnoreCase(request.getCode().strip());
            if(cekClassUnique.isPresent() && cekCodeUnique.isPresent() && !cekClassUnique.get().getMasterAccountingRuleId().equals(request.getMasterAccountingRuleId()) && !cekCodeUnique.get().getMasterAccountingRuleId().equals(request.getMasterAccountingRuleId())) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Classification type and Code already exist!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
            if(cekClassUnique.isPresent() && !cekClassUnique.get().getMasterAccountingRuleId().equals(request.getMasterAccountingRuleId())) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Classification type already exist!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
            if(cekCodeUnique.isPresent() && !cekCodeUnique.get().getMasterAccountingRuleId().equals(request.getMasterAccountingRuleId())) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Code already exist!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }

            return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.MASTER_ACCOUNTINGRULES);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> activeInactive(AccountingRulesDTO request) {
        try {
            Optional<M_ACCOUNTING_RULE> mAccountingRule = accountingRulesRepo.findByMasterAccountingRuleId(request.getMasterAccountingRuleId());
            if (mAccountingRule.isPresent()) {
                M_ACCOUNTING_RULE data = mAccountingRule.get();
                LinkedHashMap<String, Object> oldAccountingRule = new LinkedHashMap<>();
                oldAccountingRule.put("masterAccountingRuleId", data.getMasterAccountingRuleId());
                oldAccountingRule.put("classificationTypeName", data.getClassificationTypeName());
                oldAccountingRule.put("code", data.getCode());
                oldAccountingRule.put("receivableAccount", data.getReceivableAccount());
                oldAccountingRule.put("revenueAccount", data.getRevenueAccount());
                oldAccountingRule.put("description", data.getDescription());
                oldAccountingRule.put("status", data.getStatus());
                AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
                ObjectMapper mapper = new ObjectMapper();
                String oldValue = mapper.writeValueAsString(oldAccountingRule);
                auditTrail.setOldValue(oldValue);
                if (request.getRemark() == null) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Remark not be empty", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
                auditTrail.setRemark(request.getRemark());
                auditTrail.setTableName("M_ACCOUNTING_RULE");
                auditTrail.setDataId(data.getMasterAccountingRuleId().toString());
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
                auditTrail.setCreatedBy(user.get().getUsername());
                auditTrail.setCreatedDate(new Date());
                if (data.getStatus().equalsIgnoreCase(FlowStatus.INACTIVE.name())) {
                    data.setStatus(FlowStatus.ACTIVE.name());
                    data.setUpdatedBy(UserDetailUtils.getUsername());
                    data.setUpdatedDate(new Date());
                    M_ACCOUNTING_RULE saveAR = accountingRulesRepo.save(data);
                    LinkedHashMap<String, Object> newAccountingRule = new LinkedHashMap<>();
                    newAccountingRule.put("masterAccountingRuleId", saveAR.getMasterAccountingRuleId());
                    newAccountingRule.put("classificationTypeName", saveAR.getClassificationTypeName());
                    newAccountingRule.put("code", saveAR.getCode());
                    newAccountingRule.put("receivableAccount", saveAR.getReceivableAccount());
                    newAccountingRule.put("revenueAccount", saveAR.getRevenueAccount());
                    newAccountingRule.put("description", saveAR.getDescription());
                    newAccountingRule.put("status", saveAR.getStatus());
                    String newValue = mapper.writeValueAsString(newAccountingRule);
                    auditTrail.setNewValue(newValue);
                    auditTrail.setOperation(FlowStatus.ACTIVE.name());
                    auditTrailRepo.save(auditTrail);
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            ResponseUtils.MESSAGE_ACTIVE, ResponseUtils.DATA_EMPTY), HttpStatus.OK);
                }
                data.setStatus(FlowStatus.INACTIVE.name());
                data.setUpdatedBy(UserDetailUtils.getUsername());
                data.setUpdatedDate(new Date());
                M_ACCOUNTING_RULE saveAR = accountingRulesRepo.save(data);
                LinkedHashMap<String, Object> newAccountingRule = new LinkedHashMap<>();
                newAccountingRule.put("masterAccountingRuleId", saveAR.getMasterAccountingRuleId());
                newAccountingRule.put("classificationTypeName", saveAR.getClassificationTypeName());
                newAccountingRule.put("code", saveAR.getCode());
                newAccountingRule.put("receivableAccount", saveAR.getReceivableAccount());
                newAccountingRule.put("revenueAccount", saveAR.getRevenueAccount());
                newAccountingRule.put("description", saveAR.getDescription());
                newAccountingRule.put("status", saveAR.getStatus());
                String newValue = mapper.writeValueAsString(newAccountingRule);
                auditTrail.setNewValue(newValue);
                auditTrail.setOperation(FlowStatus.INACTIVE.name());
                auditTrailRepo.save(auditTrail);
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        ResponseUtils.MESSAGE_INACTIVE, ResponseUtils.DATA_EMPTY), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Master accounting rule id not found", ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> paging(MaterialTablePagingRequest pagingData,
                                                 PagedResourcesAssembler<M_ACCOUNTING_RULE> assembler) {
        try {
            Page<M_ACCOUNTING_RULE> data;
            Map<String, Object> filter = new HashMap<>();
            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key+"~"+value);
                }
            }
            if (!pagingData.getSearch().isEmpty()) {
                data = this.accountingRulesRepo.findAll(this.accountingRulesRepo.getSpecificationFromFilters(pagingData, filter),
                        PagingUtils.getPaging(pagingData));
            } else {
                data = this.accountingRulesRepo.findAll(this.accountingRulesRepo.getSpecificationDefault(filter),
                        PagingUtils.getPaging(pagingData));
            }
            PagedModel<EntityModel<M_ACCOUNTING_RULE>> pagedData = assembler.toModel(data);
            Map<String, Object> d = new HashMap<>();
            d.put("result", data.getContent());
            d.put("page", pagedData.getMetadata());
            d.put("links", pagedData.getLinks());
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get view paging", d), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<InputStreamResource> downloadFilter(MaterialTablePagingRequest pagingData) {
        try {
            Page<M_ACCOUNTING_RULE> data;
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
                long maxData = StreamSupport.stream(accountingRulesRepo.findAll().spliterator(), false).count();
                pagingData.setSize((int) maxData);
                pagingData.setPage(1);
            }
            if (!pagingData.getSearch().isEmpty()) {
                data = this.accountingRulesRepo.findAll(this.accountingRulesRepo.getSpecificationFromFilters(pagingData, filter),
                        PagingUtils.getPaging(pagingData));
            } else {
                data = this.accountingRulesRepo.findAll(this.accountingRulesRepo.getSpecificationDefault(filter),
                        PagingUtils.getPaging(pagingData));
            }
            List<M_ACCOUNTING_RULE> listAction = data.getContent();
            List<LinkedHashMap<String, Object>> allData = new ArrayList<>();
            Integer no = 1;
            HttpHeaders headers = new HttpHeaders();
            if(data.hasContent()) {
                for (M_ACCOUNTING_RULE a : listAction) {
                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                    response.put("NO", no);
                    response.put("CLASSIFICATION TYPE", a.getClassificationTypeName());
                    response.put("CODE", a.getCode());
                    response.put("RECEIVABLE ACCOUNT", a.getReceivableAccount());
                    response.put("REVENUE ACCOUNT", a.getRevenueAccount());
                    response.put("DESCRIPTION", a.getDescription());
                    response.put("STATUS", capitalizeFully(a.getStatus()));
                    allData.add(response);
                    no = no + 1;
                }
                ByteArrayInputStream in = DownloadToExcel.downloadsFiles(allData, "Excel");
                headers.add(Constant.CONTENT_DISPOSITION, "attachment; filename=" +
                        "ACCOUNTING_RULES_" + UtilsDate.dateToString(new Date(), "yyyyMMddHHmmss") + ".xlsx");
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

    public ResponseEntity<ResponseObject> detail(Integer id) {
        try {
            Optional<M_ACCOUNTING_RULE> mAccountingRule = accountingRulesRepo.findByMasterAccountingRuleId(id);
            if (mAccountingRule.isEmpty()){
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "id not found", ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
            }
            M_ACCOUNTING_RULE data = mAccountingRule.get();
            AccountingRulesResponseDTO response = objectMapper.convertValue(data ,AccountingRulesResponseDTO.class);

            //acr
            List<AUDIT_TRAIL> auditTrail = auditTrailRepo.findAll().stream()
                    .filter(e -> e.getTableName().equalsIgnoreCase("M_ACCOUNTING_RULE"))
                    .filter(f -> f.getDataId().equalsIgnoreCase(data.getMasterAccountingRuleId().toString()))
                    .collect(Collectors.toList());
            response.setActiveInactiveLog(auditTrail);

            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    ResponseUtils.MESSAGE_OK, data), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
