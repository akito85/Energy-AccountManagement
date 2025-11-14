package com.dbs.module.account.detail.financialinformation.service;

import com.dbs.common.base.utils.*;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.FlowStatus;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.common.library.utils.UtilsDate;
import com.dbs.database.crm.entities.accountmanagement.M_WITHOLDING_TAX;
import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import com.dbs.database.crm.entities.usermanagement.M_USER;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.accountmanagement.Account.MWithholdingTaxRepo;
import com.dbs.database.crm.repositories.usermanagement.AuditTrailRepo;
import com.dbs.database.crm.repositories.usermanagement.MUserRepo;
import com.dbs.module.account.detail.financialinformation.dto.InactiveDTO;
import com.dbs.module.account.detail.financialinformation.dto.WithholdingTaxCreateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.dbs.common.base.utils.CommonHelper.removeSpace;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class WithholdingTaxService {
    
    private static final Logger logger = LoggerFactory.getLogger(WithholdingTaxService.class);
    
    private final MWithholdingTaxRepo wtaxRepo;
    
    private final Validator validator;
    
    private final MUserRepo userRepo;
    
    private final AuditTrailRepo auditTrailRepo;
    
    @Autowired 
    private GlobalTypeValueService globalTypeService;

    @Autowired
    private ObjectMapper objectMapper;
    
        public WithholdingTaxService(MWithholdingTaxRepo wtaxRepo, Validator validator, MUserRepo userRepo, AuditTrailRepo auditTrailRepo) {
        this.wtaxRepo = wtaxRepo;
        this.validator = validator;
        this.userRepo = userRepo;
        this.auditTrailRepo = auditTrailRepo;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @SuppressWarnings({"java:S1192","java:S1141"})
    public ResponseEntity<ResponseObject> createWithholdingTax(WithholdingTaxCreateDTO request) {
        logger.info("paramsRequest -> {}", request);
        ResponseObject result;
        try {
            Set<ConstraintViolation<WithholdingTaxCreateDTO>> violations = this.validator.validate(request);
            List<Map<String, Object>> violationList = new ArrayList<>();
            if(violations.size() > 0) {
                for(ConstraintViolation<WithholdingTaxCreateDTO> violation : violations) {
                    logger.error(violation.getMessage());
                    Map<String, Object> datas = new HashMap<>();
                    datas.put(violation.getPropertyPath().toString(), violation.getMessage());
                    violationList.add(datas);
                }
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, ResponseUtils.MESSAGE_BAD_REQUEST, violationList);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            M_WITHOLDING_TAX newWitax = new M_WITHOLDING_TAX();
            newWitax.setAccountId(request.getAccountId());
            SimpleDateFormat formatDate = new SimpleDateFormat(Constant.FORMAT_START_END_DATE);
            Optional<M_WITHOLDING_TAX> checkCreate = wtaxRepo.findTopByAccountIdOrderByCreatedDateDesc(request.getAccountId());
            if(checkCreate.isPresent()) {
                if(checkCreate.get().getEndDate() == null) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "There is Withholding Tax are still active!",
                            ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                if(formatDate.parse(request.getStartDate()).getTime() < checkCreate.get().getEndDate().getTime()) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Start date must be greater than existing End Date!",
                            ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
            }
            if(!UtilsDate.validateDate(request.getStartDate())) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "Start date must be greater than Current Date!", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            } 
            try {
                newWitax.setStartDate(formatDate.parse(request.getStartDate()));
            } catch (Exception e) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "Invalid start date", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            try {
                newWitax.setCreatedBy(UserDetailUtils.getUsername());
            } catch (Exception e) {
                newWitax.setCreatedBy(null);
            }
            newWitax.setDescription(removeSpace(request.getDescription()));
            newWitax.setStatus(FlowStatus.ACTIVE.name());
            newWitax.setCreatedDate(new Date());
            wtaxRepo.save(newWitax);

            LinkedHashMap<String, Object> response = new LinkedHashMap<>();
            response.put("withholdingTaxId", newWitax.getId());
            response.put("accountId", newWitax.getAccountId());
            String dateString = Constant.FORMAT_DATE;
            response.put("startDate", CommonHelper.convertDateToString(dateString, newWitax.getStartDate()));
            response.put("description", newWitax.getDescription());
            response.put("createdBy", newWitax.getCreatedBy());
            response.put("updateBy", newWitax.getUpdatedBy());
            response.put("createdDate", newWitax.getCreatedDate());
            response.put("updateDate", newWitax.getUpdatedDate());

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED, "Success create", response);
            logger.info("Response Success ->" + result);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @SuppressWarnings({"java:S3776","java:S1192","java:S1141"})
    public ResponseEntity<ResponseObject> inactiveWitax(InactiveDTO requestDTO) {
        ResponseObject result;
        try {
            Optional<M_WITHOLDING_TAX> data = wtaxRepo.findById(requestDTO.getId());
            if(data.isPresent()) {
                M_WITHOLDING_TAX wt = data.get();
                LinkedHashMap<String, Object> oldWt = new LinkedHashMap<>();
                oldWt.put("witholdingTaxId", wt.getId());
                oldWt.put("accoundId", wt.getAccountId());
                oldWt.put("description", wt.getDescription());
                oldWt.put("startDate", wt.getStartDate());
                oldWt.put("endDate", wt.getEndDate());
                AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
                ObjectMapper mapper = new ObjectMapper();
                String oldValue = mapper.writeValueAsString(oldWt);
                auditTrail.setOldValue(oldValue);
                auditTrail.setRemark(removeSpace(requestDTO.getRemark()));
                auditTrail.setOperation("MODIFY");
                auditTrail.setTableName("M_WITHOLDING_TAX");
                auditTrail.setDataId(wt.getId().toString());
                Optional<M_USER> user = userRepo.findByUsername(UserDetailUtils.getUsername());
                if (user.isEmpty()) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "user with username " + UserDetailUtils.getUsername() + " not found", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                R_GLOBAL_TYPE_VALUE rUserLevel = globalTypeService.getGlobalTypeByGlbValue(Constant.USER_LEVEL, user.get().getUserLevel());
                String userLevel = rUserLevel.getName() != null ? rUserLevel.getName() : "";
                auditTrail.setUserLevel(userLevel);
                auditTrail.setCreatedBy(UserDetailUtils.getUsername() != null ? UserDetailUtils.getUsername() : null);
                auditTrail.setCreatedDate(new Date());
                if (wt.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())) {
                    wt.setStatus(FlowStatus.INACTIVE.name());
                    SimpleDateFormat formatDate = new SimpleDateFormat(Constant.FORMAT_START_END_DATE);
                    if(!UtilsDate.validateDate(requestDTO.getEndDate())) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "End date must be greater than Current Date!", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            try {
                wt.setEndDate(formatDate.parse(requestDTO.getEndDate()));
            } catch (Exception e) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "Invalid end date", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            if(formatDate.parse(requestDTO.getEndDate()).getTime() < wt.getStartDate().getTime()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "End date must be greater than Start Date!", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
                    try {
                        wt.setUpdatedBy(UserDetailUtils.getUsername());
                    } catch (Exception e) {
                        wt.setUpdatedBy(null);
                    }
                    wt.setUpdatedDate(new Date());
                    M_WITHOLDING_TAX saveWT = wtaxRepo.save(wt);
                    LinkedHashMap<String, Object> newWt = new LinkedHashMap<>();
                    newWt.put("witholdingTaxId", saveWT.getId());
                    newWt.put("accoundId", saveWT.getAccountId());
                    newWt.put("description", saveWT.getDescription());
                    String dateString = Constant.FORMAT_DATE;
                    newWt.put("startDate", CommonHelper.convertDateToString(dateString, saveWT.getStartDate()));
                    newWt.put("endDate", CommonHelper.convertDateToString(dateString, saveWT.getEndDate()));
                    String newValue = mapper.writeValueAsString(newWt);
                    auditTrail.setNewValue(newValue);
                    auditTrailRepo.save(auditTrail);
                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            ResponseUtils.MESSAGE_INACTIVE, saveWT);
                    return new ResponseEntity<>(result, result.getHttpCode());
                } else {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Withholding Tax Status not found or null", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
            } else {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Withholding Tax with id " + requestDTO.getId() + " is not found.", ResponseUtils.DATA_EMPTY);
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
    
    @SuppressWarnings("java:S1192")
    public ResponseEntity<ResponseObject> getWapuListData(Integer accountId, MaterialTablePagingRequest pagingData,
			PagedResourcesAssembler<M_WITHOLDING_TAX> assembler)
    {
        logger.info("Get List Wapu");
        try {
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

            Specification<M_WITHOLDING_TAX> specification = pagingData.getSearch().isEmpty()
                    ? wtaxRepo.getSpecificationDefault(filter)
                    : wtaxRepo.getSpecificationFromFilters(pagingData, filter);

            Page<M_WITHOLDING_TAX> data = wtaxRepo.findAll(specification, PagingUtils.getPaging(pagingData));
            
            List<LinkedHashMap<String, Object>> allData = data.getContent().stream()
                    .map(g -> {
                        LinkedHashMap<String, Object> wapu = new LinkedHashMap<>();
                        wapu.put("id", g.getId());
                        wapu.put("createdBy", g.getCreatedBy());
                        wapu.put("updatedBy", g.getUpdatedBy());
                        wapu.put("createdDate", g.getCreatedDate());
                        wapu.put("updatedDate", g.getUpdatedDate());
                        wapu.put("status", g.getStatus());
                        wapu.put("accountId", g.getCreatedBy());
                        wapu.put("description", g.getDescription());
                        String dateString = Constant.FORMAT_DATE;
                        wapu.put("startDate", CommonHelper.convertDateToString(dateString, g.getStartDate()));
                        if(g.getEndDate() != null) {
                            wapu.put("endDate", CommonHelper.convertDateToString(dateString, g.getEndDate()));   
                        }
                        
                        return wapu;
                    })
                    .collect(Collectors.toList());

            PagedModel<EntityModel<M_WITHOLDING_TAX>> pagedData = assembler.toModel(data);
            Map<String, Object> d = new HashMap<>();
            Optional<M_WITHOLDING_TAX> check = wtaxRepo.findTopByAccountIdOrderByCreatedDateDesc(accountId);
            if(check.isPresent()) {
                if(check.get().getEndDate() == null) {
                    d.put("status", true);
                } else if(check.get().getEndDate() != null) {
                    d.put("status", false);
                }
            } else if(check.isEmpty()) {
                d.put("status", false);
                
            }
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
}
