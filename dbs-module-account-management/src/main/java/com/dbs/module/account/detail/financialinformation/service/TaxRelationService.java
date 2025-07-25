package com.dbs.module.account.detail.financialinformation.service;

import com.dbs.common.base.utils.*;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.security.CryptoSecurity;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.FlowStatus;
import com.dbs.common.library.utils.StringUtils;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.common.library.utils.UtilsDate;
import com.dbs.database.crm.entities.accountmanagement.M_TAX_RELATION;
import com.dbs.database.crm.entities.accountmanagement.VW_CHOOSE_TAX_RELATION;
import com.dbs.database.crm.entities.accountmanagement.VW_TAX_RELATION;
import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import com.dbs.database.crm.entities.usermanagement.M_USER;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.accountmanagement.Account.*;
import com.dbs.database.crm.repositories.usermanagement.AuditTrailRepo;
import com.dbs.database.crm.repositories.usermanagement.MGlobalTypeRepo;
import com.dbs.database.crm.repositories.usermanagement.MUserRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import com.dbs.module.account.detail.financialinformation.dto.InactiveDTO;
import com.dbs.module.account.detail.financialinformation.dto.TaxRelationCreateDTO;
import com.dbs.module.account.utils.ConstantAccount;
import com.dbs.module.account.utils.UtilsAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jshell.execution.Util;
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
public class TaxRelationService {
    private static final Logger logger = LoggerFactory.getLogger(TaxRelationService.class);
    @Autowired
    private MTaxRelationRepo taxRepo;
    @Autowired
    private MAccountRepo accountRepo;
    @Autowired
    private MCustomerRepo cusRepo;
    @Autowired
    private Validator validator;
    @Autowired
    private MUserRepo userRepo;
    @Autowired
    private AuditTrailRepo auditTrailRepo;
    @Autowired
    private MTaxIdentifierRepo taxIdentifierRepo;
    @Autowired
    private MGlobalTypeRepo mGlobalTypeRepo;
    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;
    @Autowired
    private MaddressRepo addrRepo;
    @Autowired 
    private GlobalTypeValueService globalTypeService;
    @Autowired
    private VwChooseTaxRelationRepo vwChooseTax;
    @Autowired
    private VwTaxRelationRepo vwTaxRelation;
    @Autowired
    private ObjectMapper objectMapper;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @SuppressWarnings({"java:S1192","java:S1141"})
    public ResponseEntity<ResponseObject> create(TaxRelationCreateDTO request) {
        try {
            SimpleDateFormat formatDate = new SimpleDateFormat(Constant.FORMAT_START_END_DATE);

            ResponseEntity<ResponseObject> validate = this.validateCreate(request);
            if(!validate.getBody().getSuccess()) {
                return validate;
            }
            
            M_TAX_RELATION newTR = new M_TAX_RELATION();
            newTR.setAccountId(request.getAccountId());
            newTR.setRelatedAccountId(request.getRelatedAccountId());
            newTR.setStartDate(formatDate.parse(request.getStartDate()));
            newTR.setDescription(removeSpace(request.getDescription()));
            newTR.setCreatedBy(UserDetailUtils.getUsername());
            newTR.setCreatedDate(new Date());
            taxRepo.save(newTR);

            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.CREATE, ConstantAccount.TAX_RELATION), ResponseUtils.DATA_EMPTY), HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings({"java:S1192","java:S1141"})
    public ResponseEntity<ResponseObject> validateCreate(TaxRelationCreateDTO request) {
        try {
            SimpleDateFormat formatDate = new SimpleDateFormat(Constant.FORMAT_START_END_DATE);
            // DTO
            var violationsAccount = validator.validate(request);
            if (!violationsAccount.isEmpty()) {
                List<Map<String, Object>> violationHeaderList = new ArrayList<>();
                List<String> validateHeader = new ArrayList<>();
                for (var violation : violationsAccount) {
                    logger.error(violation.getMessage());
                    Map<String, Object> data = new HashMap<>();
                    validateHeader.add(violation.getMessage());
                    data.put(violation.getPropertyPath().toString(), violation.getMessage());
                    violationHeaderList.add(data);
                }
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, validateHeader.get(0), violationHeaderList), HttpStatus.BAD_REQUEST);
            }

            Optional<VW_TAX_RELATION> checkCreate = vwTaxRelation.findTopByAccountIdOrderByCreatedDateDesc(request.getAccountId());
            if(checkCreate.isPresent()) {
                VW_TAX_RELATION taxRel = checkCreate.get();
                if(taxRel.getEndDate()==null && taxRel.getStatus().equalsIgnoreCase(FlowStatus.INACTIVE.name())) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "The new tax relation will be active on " + taxRel.getStartDate(), ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
                if(formatDate.parse(request.getStartDate()).getTime() < taxRel.getEndDate().getTime()) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Start date must be greater than last tax relation end date!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
            }


            try {
                formatDate.parse(request.getStartDate());
            } catch (Exception e) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Invalid start date", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.VALIDATE, ConstantAccount.TAX_RELATION), ResponseUtils.DATA_EMPTY), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("java:S1192")
    public ResponseEntity<ResponseObject> getList(Integer accountId, MaterialTablePagingRequest pagingData,
			PagedResourcesAssembler<VW_TAX_RELATION> assembler) {
        
            logger.info("Get List Tax Relation");
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

                // encrypt search tax identifier number
                pagingData.setSearch(UtilsAccount.encryptTaxForFiltering(pagingData.getSearch()));

                Specification<VW_TAX_RELATION> specification = pagingData.getSearch().isEmpty()
                        ? vwTaxRelation.getSpecificationDefault(filter)
                        : vwTaxRelation.getSpecificationFromFilters(pagingData, filter);

                Page<VW_TAX_RELATION> data = vwTaxRelation.findAll(specification, PagingUtils.getPaging(pagingData));
                
                List<LinkedHashMap<String, Object>> allData = data.getContent().stream()
                    .map(g -> {
                        LinkedHashMap<String, Object> taxRel = new LinkedHashMap<>();
                        taxRel.put("id", g.getTaxRelationId());
                        
                        String dateString = "yyyy-MM-dd";
                        taxRel.put("startDate", CommonHelper.convertDateToString(dateString, g.getStartDate()));
                        if(g.getEndDate() != null) {
                            taxRel.put("endDate", CommonHelper.convertDateToString(dateString, g.getEndDate()));
                        } else {
                            taxRel.put("endDate", null);
                        }
                        taxRel.put("status", g.getStatus());
                        taxRel.put("accountNumber", g.getAccountNumber());
                        taxRel.put("accountName", g.getAccountName());
                        
                        String decryptNumber = CryptoSecurity.decrypt(g.getTaxIdentifierNumber());
                        if(g.getTaxIdentifierType().equalsIgnoreCase("NPWP")){
                            taxRel.put("taxIdentifierNumber", CommonHelper.getNpwpFormat(decryptNumber));
                        } else {
                            taxRel.put("taxIdentifierNumber", decryptNumber);
                        }
                        taxRel.put("taxIdentifierAddressValue", g.getTaxIdentifierAddressValue());
                        taxRel.put("description", g.getDescription());
                        taxRel.put("createdBy", g.getCreatedBy());
                        taxRel.put("createdDate", g.getCreatedDate());
                        taxRel.put("updatedBy", g.getUpdatedBy());
                        taxRel.put("updatedDate", g.getUpdatedDate());
                        
                        return taxRel;
                    })
                    .collect(Collectors.toList());
          
            PagedModel<EntityModel<VW_TAX_RELATION>> pagedData = assembler.toModel(data);
            Map<String, Object> d = new HashMap<>();
            d.put(Constant.RESULT, allData);
            d.put(Constant.PAGE, pagedData.getMetadata());
            d.put(Constant.LINK, pagedData.getLinks());
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
    
    @SuppressWarnings("java:S3776")
    public ResponseEntity<ResponseObject> getChooseTax(Integer accountId, MaterialTablePagingRequest pagingData,
			PagedResourcesAssembler<VW_CHOOSE_TAX_RELATION> assembler) {
            logger.info("Get List Tax Relation");
            try {
                Map<String, Object> filter = new HashMap<>();
                
                Page<VW_CHOOSE_TAX_RELATION> data;

                if (isNotBlank(pagingData.getSearchs())) {
                    Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                    for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        pagingData.getSearch().add(key+"~"+value);
                    }
                }

                if(!pagingData.getSearch().isEmpty()) {
                    data = vwChooseTax.findAll(vwChooseTax.getSpecificationFromFiltersNot(pagingData, filter, accountId), PagingUtils.getPaging(pagingData));
                } else {
                    data = vwChooseTax.findAll(vwChooseTax.getSpecificationDefaultNot(filter, accountId), PagingUtils.getPaging(pagingData));
                }

                List<LinkedHashMap<String, Object>> allData = data.getContent().stream()
//                    .filter(g -> !accountId.equals(g.getAccountId()))
                    .map(g -> {
                        LinkedHashMap<String, Object> tr = new LinkedHashMap<>();
                        tr.put("customerId", g.getCustomerId());
                        tr.put("customerNumber", g.getCustomerNumber());
                        tr.put("customerName", g.getCustomerName());
                        tr.put("accountId", g.getAccountId());
                        tr.put("accountNumber", g.getAccountNumber());
                        tr.put("accountName", g.getAccountName());
                        tr.put("taxIdentifierType", g.getTaxIdentifierType());
                        tr.put("taxIdentifierTypeValue", g.getTaxIdentifierTypeValue());
                        String decryptNumber = CryptoSecurity.decrypt(g.getTaxIdentifierNumber());
                        if(g.getTaxIdentifierTypeValue().equalsIgnoreCase("NPWP")){
                            tr.put("taxIdentifierNumber", CommonHelper.getNpwpFormat(decryptNumber));
                        } else {
                            tr.put("taxIdentifierNumber", decryptNumber);
                        }
                        tr.put("taxIdentifierName", g.getTaxIdentifierName());
                        tr.put("taxIdentifierAddressId", g.getTaxIdentifierAddress());
                        tr.put("taxIdentifierAddressValue", g.getTaxIdentifierAddressValue());
                        return tr;
                    }).collect(Collectors.toList());
          
            PagedModel<EntityModel<VW_CHOOSE_TAX_RELATION>> pagedData = assembler.toModel(data);
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
    
    @SuppressWarnings("java:S3776")
    public ResponseEntity<ResponseObject> getChooseAllTax(MaterialTablePagingRequest pagingData,
			PagedResourcesAssembler<VW_CHOOSE_TAX_RELATION> assembler) {
            logger.info("Get List Tax Relation");
            try {
                Map<String, Object> filter = new HashMap<>();
                
                Page<VW_CHOOSE_TAX_RELATION> data;

                if (isNotBlank(pagingData.getSearchs())) {
                    Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                    for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        pagingData.getSearch().add(key+"~"+value);
                    }
                }

                if(!pagingData.getSearch().isEmpty()) {
                    pagingData.setPage(0);
                    data = vwChooseTax.findAll(vwChooseTax.getSpecificationFromFiltersNot(pagingData, filter, null), PagingUtils.getPaging(pagingData));
                } else {
                    data = vwChooseTax.findAll(vwChooseTax.getSpecificationDefaultNot(filter, null), PagingUtils.getPaging(pagingData));
                }
                List<LinkedHashMap<String, Object>> allData = data.getContent().stream()
                    .map(g -> {
                        LinkedHashMap<String, Object> tr = new LinkedHashMap<>();
                        tr.put("customerId", g.getCustomerId());
                        tr.put("customerNumber", g.getCustomerNumber());
                        tr.put("customerName", g.getCustomerName());
                        tr.put("accountId", g.getAccountId());
                        tr.put("accountNumber", g.getAccountNumber());
                        tr.put("accountName", g.getAccountName());
                        tr.put("taxIdentifierType", g.getTaxIdentifierType());
                        tr.put("taxIdentifierTypeValue", g.getTaxIdentifierTypeValue());
                        String decryptNumber = CryptoSecurity.decrypt(g.getTaxIdentifierNumber());
                        if(g.getTaxIdentifierTypeValue().equalsIgnoreCase("NPWP")){
                            tr.put("taxIdentifierNumber", CommonHelper.getNpwpFormat(decryptNumber));
                        } else {
                            tr.put("taxIdentifierNumber", decryptNumber);
                        }
                        tr.put("taxIdentifierName", g.getTaxIdentifierName());
                        tr.put("taxIdentifierAddressId", g.getTaxIdentifierAddress());
                        tr.put("taxIdentifierAddressValue", g.getTaxIdentifierAddressValue());
                        
                        return tr;
                    
                    })
                    .collect(Collectors.toList());
          
            PagedModel<EntityModel<VW_CHOOSE_TAX_RELATION>> pagedData = assembler.toModel(data);
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
    
    @SuppressWarnings({"java:S3776","java:S1141"})
    public ResponseEntity<ResponseObject> inactive(InactiveDTO requestDTO) {
        ResponseObject result;
        try {
            ObjectMapper mapper = new ObjectMapper();
            Optional<M_TAX_RELATION> data = taxRepo.findById(requestDTO.getId());
            if(data.isPresent()) {
                M_TAX_RELATION wt = data.get();
                SimpleDateFormat formatDate = new SimpleDateFormat("dd MMM yyyy");
                if(wt.getEndDate()!=null) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND, "Tax Relation will be inactive on " + UtilsDate.dateToString(wt.getEndDate(), Constant.FORMAT_START_END_DATE), ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
                }

                AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
                String oldValue = mapper.writeValueAsString(wt);
                auditTrail.setOldValue(oldValue);
                auditTrail.setRemark(requestDTO.getRemark());
                auditTrail.setOperation(ConstantAccount.MODIFY);
                auditTrail.setTableName("M_TAX_RELATION");
                auditTrail.setDataId(wt.getTaxRelationId().toString());
                Optional<M_USER> user = userRepo.findByUsername(UserDetailUtils.getUsername());
                if (user.isEmpty()) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "user with username " + UserDetailUtils.getUsername() + " not found", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                R_GLOBAL_TYPE_VALUE rUserLevel = globalTypeService.getGlobalTypeByGlbValue(Constant.USER_LEVEL, user.get().getUserLevel());
                String userLevel = rUserLevel.getName() != null ? rUserLevel.getName() : "";
                auditTrail.setUserLevel(userLevel);

                try {
                    wt.setEndDate(formatDate.parse(requestDTO.getEndDate()));
                } catch (Exception e) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "Invalid end date", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                wt.setUpdatedBy(UserDetailUtils.getUsername());
                wt.setUpdatedDate(new Date());
                taxRepo.save(wt);

                String newValue = mapper.writeValueAsString(wt);
                auditTrail.setNewValue(newValue);
                auditTrail.setCreatedBy(UserDetailUtils.getUsername());
                auditTrail.setCreatedDate(new Date());
                auditTrailRepo.save(auditTrail);

                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        ResponseUtils.MESSAGE_INACTIVE, wt);
                return new ResponseEntity<>(result, result.getHttpCode());
            } else {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        UtilsAccount.messageDataNotFound(ConstantAccount.TAX_RELATION, requestDTO.getId()), ResponseUtils.DATA_EMPTY);
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
    
}
