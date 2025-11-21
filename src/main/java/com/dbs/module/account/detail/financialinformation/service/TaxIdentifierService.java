package com.dbs.module.account.detail.financialinformation.service;

import com.dbs.common.base.utils.*;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.security.CryptoSecurity;
import com.dbs.common.library.utils.FlowStatus;
import com.dbs.common.library.utils.StringUtils;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.common.library.utils.UtilsDate;
import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNT_ADDRESS;
import com.dbs.database.crm.entities.accountmanagement.M_ADDRESSES;
import com.dbs.database.crm.entities.accountmanagement.M_TAX_IDENTIFIER;
import com.dbs.database.crm.entities.accountmanagement.VW_TAX_IDENTIFIER;
import com.dbs.database.crm.entities.usermanagement.M_GLOBAL_TYPE;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.accountmanagement.Account.MAccountAddressRepo;
import com.dbs.database.crm.repositories.accountmanagement.Account.MTaxIdentifierRepo;
import com.dbs.database.crm.repositories.accountmanagement.Account.MaddressRepo;
import com.dbs.database.crm.repositories.accountmanagement.Account.VwTaxIdentifierRepo;
import com.dbs.database.crm.repositories.usermanagement.AuditTrailRepo;
import com.dbs.database.crm.repositories.usermanagement.MGlobalTypeRepo;
import com.dbs.database.crm.repositories.usermanagement.MUserRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import com.dbs.module.account.detail.financialinformation.dto.TaxIdentifierCreateDTO;
import com.dbs.module.account.utils.ConstantAccount;
import com.dbs.module.account.utils.UtilsAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
public class TaxIdentifierService {
    
    private static final Logger logger = LoggerFactory.getLogger(TaxIdentifierService.class);
    @Autowired
    private MTaxIdentifierRepo taxRepo;
    @Autowired
    private Validator validator;
    @Autowired
    private MUserRepo userRepo;
    @Autowired
    private AuditTrailRepo auditTrailRepo;
    @Autowired
    private MGlobalTypeRepo mGlobalTypeRepo;
    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;
    @Autowired
    private MaddressRepo addrRepo;
    @Autowired
    private VwTaxIdentifierRepo vwTaxRepo;
    @Autowired
    private MAccountAddressRepo acaddrRepo;
    @Autowired
    private ObjectMapper objectMapper;
    
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @SuppressWarnings("java:S1192")
    public ResponseEntity<ResponseObject> create(TaxIdentifierCreateDTO request) {
        try {
            SimpleDateFormat formatDate = new SimpleDateFormat(Constant.FORMAT_START_END_DATE);

            ResponseEntity<ResponseObject> validate = this.validateCreate(request);
            if(!validate.getBody().getSuccess()) {
                return validate;
            }

            // INACTIVE OLD TAX IDENTIFIER
            Optional<VW_TAX_IDENTIFIER> inactive = vwTaxRepo.findTopByAccountIdAndStatusOrderByCreatedDateDesc(request.getAccountId(), FlowStatus.ACTIVE.name());
            if(inactive.isPresent()) {
                Optional<M_TAX_IDENTIFIER> getInactiveTI = taxRepo.findById(inactive.get().getTaxIdentifierId());
                if(getInactiveTI.isPresent()) {
                    M_TAX_IDENTIFIER inactiveTI = getInactiveTI.get();
                    inactiveTI.setEndDate(UtilsDate.getDateMinusDay(formatDate.parse(request.getStartDate()), 1));
                    inactiveTI.setUpdatedBy(UserDetailUtils.getUsername());
                    inactiveTI.setUpdatedDate(new Date());
                    taxRepo.save(inactiveTI);
                }
            }
            
            M_TAX_IDENTIFIER newTI = new M_TAX_IDENTIFIER();
            newTI.setAccountId(request.getAccountId());
            newTI.setTaxIdentifierType(request.getTaxIdentifierType());
            
            //ENCRYPT
            String encryptNumber = CryptoSecurity.encrypt(request.getTaxIdentifierNumber());
            newTI.setTaxIdentifierNumber(encryptNumber);
            
            newTI.setTaxIdentifierName(removeSpace(request.getTaxIdentifierName()));
            newTI.setTaxIdentifierAddress(request.getTaxIdentifierAddress());
            
            newTI.setStartDate(formatDate.parse(request.getStartDate()));
            newTI.setCreatedBy(StringUtils.hasValue(UserDetailUtils.getUsername()) ? UserDetailUtils.getUsername() : null);
            newTI.setDescription(removeSpace(request.getDescription()));
            newTI.setStatus(FlowStatus.ACTIVE.name());
            newTI.setCreatedDate(new Date());
            newTI.setCreatedBy(UserDetailUtils.getUsername());
            taxRepo.save(newTI);

            LinkedHashMap<String, Object> response = new LinkedHashMap<>();
            response.put("taxIdentifierId", newTI.getTaxIdentifierId());
            response.put("AccountId", newTI.getAccountId());
            Optional<R_GLOBAL_TYPE_VALUE> taxType = rGlobalTypeValueRepo.findById(newTI.getTaxIdentifierType());
            response.put("taxIdentiferType", taxType.get().getName());
            
            //DECRYPT
            String decryptNumber = CryptoSecurity.decrypt(newTI.getTaxIdentifierNumber());
            if(taxType.get().getName().equalsIgnoreCase("NPWP")){
                response.put("taxIdentifierNumber", CommonHelper.getNpwpFormat(decryptNumber));
            } else {
                response.put("taxIdentifierNumber", decryptNumber);
            }
            
            response.put("taxIdentifierName", newTI.getTaxIdentifierName());
            response.put("taxIdentifierAddress", newTI.getTaxIdentifierAddress());
            response.put("startDate", newTI.getStartDate());
            response.put("CreatedBy", newTI.getCreatedBy());
            response.put("UpdateBy", newTI.getUpdatedBy());
            response.put("CreatedDate", newTI.getCreatedDate());
            response.put("UpdateDate", newTI.getUpdatedDate());

            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.CREATE, ConstantAccount.TAX_IDENTIFIER), ResponseUtils.DATA_EMPTY), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateCreate(TaxIdentifierCreateDTO request) {

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

            // VALIDATE STARTDATE
            Optional<VW_TAX_IDENTIFIER> getLastTaxId = vwTaxRepo.findTopByAccountIdAndStatusOrderByCreatedDateDesc(request.getAccountId(), FlowStatus.INACTIVE.name());
            if(getLastTaxId.isPresent()) {
                VW_TAX_IDENTIFIER taxIden = getLastTaxId.get();
                // VALIDATION DATE
                if(taxIden.getEndDate()==null) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "The new tax identifier will be active on " + UtilsDate.dateToString(taxIden.getStartDate(), Constant.FORMAT_START_END_DATE), ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
            }

            Optional<VW_TAX_IDENTIFIER> inactive = vwTaxRepo.findTopByAccountIdAndStatusOrderByCreatedDateDesc(request.getAccountId(), FlowStatus.ACTIVE.name());
            if(inactive.isPresent()) {
                VW_TAX_IDENTIFIER inactiveTI = inactive.get();
                // VALIDATION DATE
                if(formatDate.parse(request.getStartDate()).before(inactiveTI.getStartDate()) || formatDate.format(inactiveTI.getStartDate()).equals(request.getStartDate())) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "Start date must be greater than last tax identifier start date!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
            }

            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.VALIDATE, ConstantAccount.TAX_IDENTIFIER), ResponseUtils.DATA_EMPTY), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public ResponseEntity<ResponseObject> getListTaxIdentifier(Integer accountId, MaterialTablePagingRequest pagingData,
			PagedResourcesAssembler<VW_TAX_IDENTIFIER> assembler) {
            logger.info("Get List Account Contact");
            try {
                Map<String, Object> filter = new HashMap<>();
                filter.put("accountId", accountId);

                Page<VW_TAX_IDENTIFIER> data;

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

                if(!pagingData.getSearch().isEmpty()) {
                    pagingData.setPage(0);
                    data = vwTaxRepo.findAll(vwTaxRepo.getSpecificationFromFilters(pagingData, filter), PagingUtils.getPaging(pagingData));
                } else {
                    data = vwTaxRepo.findAll(vwTaxRepo.getSpecificationDefault(filter), PagingUtils.getPaging(pagingData));
                }
                
                List<LinkedHashMap<String, Object>> allData = data.getContent().stream()
                    .map(g -> {
                        LinkedHashMap<String, Object> taxIdentifier = new LinkedHashMap<>();
                        taxIdentifier.put("id", g.getTaxIdentifierId());
                        taxIdentifier.put("accountId", g.getAccountId());
                        taxIdentifier.put("taxIdentifierType", g.getTaxIdentifierType());
                        taxIdentifier.put("taxIdentifierTypeValue", g.getTaxIdentifierTypeValue());
                        String decryptNumber = CryptoSecurity.decrypt(g.getTaxIdentifierNumber());
                        if(g.getTaxIdentifierTypeValue().equalsIgnoreCase("NPWP")){
                            taxIdentifier.put("taxIdentifierNumber", CommonHelper.getNpwpFormat(decryptNumber));
                        } else {
                            taxIdentifier.put("taxIdentifierNumber", decryptNumber);
                        }
                        taxIdentifier.put("taxIdentifierName", g.getTaxIdentifierName());
                        taxIdentifier.put("taxIdentifierAddress", g.getTaxIdentifierAddress());
                        taxIdentifier.put("taxIdentifierAddressValue", g.getTaxIdentifierAddressValue());
                        String dateString = "yyyy-MM-dd";
                        taxIdentifier.put("startDate", CommonHelper.convertDateToString(dateString, g.getStartDate()));
                        if(g.getEndDate() != null) {
                            taxIdentifier.put("endDate", CommonHelper.convertDateToString(dateString, g.getEndDate()));
                        }
                        taxIdentifier.put("description", g.getDescription());
                        taxIdentifier.put("status", g.getStatus());
                        taxIdentifier.put("createdDate", g.getCreatedDate());
                        taxIdentifier.put("createdBy", g.getCreatedBy());
                        taxIdentifier.put("updatedDate", g.getUpdatedDate());
                        taxIdentifier.put("updatedBy", g.getUpdatedBy());
                        return taxIdentifier;
                    })
                    .collect(Collectors.toList());
          
            PagedModel<EntityModel<VW_TAX_IDENTIFIER>> pagedData = assembler.toModel(data);
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
    
    public ResponseEntity<ResponseObject> getTaxIdentificationType() {
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Tax Identification Type", FlowStatus.ACTIVE.name(), false);
            logger.info("Tax Identification Type " + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;
            
            if(!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name()))
                        .collect(Collectors.toList());
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if(!areas.isEmpty()) {
                    for(R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        ar.put("id", rgtv.getGlbTypeValId());
                        ar.put("code", rgtv.getGlbValue());
                        ar.put("text", rgtv.getName());
                        areaList.add(ar);
                    }
                    
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Success get type");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Fail get type");
                    result.setData("There is no Type");
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Fail get Type");
                result.setData("There is no Type");
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
    
    public ResponseEntity<ResponseObject> getListAddressTax(Integer accountId) {
        ResponseObject result = new ResponseObject();
        logger.info("Get address Tax");
        try {
            List<M_ACCOUNT_ADDRESS> data = acaddrRepo.findAllByAccountIdAndBusinessPurposeAndStatus(accountId, 162, FlowStatus.ACTIVE.name());
            if(data != null) {
                List<LinkedHashMap<String, Object>> addressTax = new LinkedList<>();
                for(M_ACCOUNT_ADDRESS addTax : data) {
                    LinkedHashMap<String, Object> at = new LinkedHashMap<>();
                    M_ADDRESSES dataAddress = addrRepo.findByAddressId(addTax.getAddressId());
                    at.put("addressId", dataAddress.getAddressId());
                    at.put("fullAddress", dataAddress.getFullAddress());
                    addressTax.add(at);
                }
                
                if(!addressTax.isEmpty()) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, "Success get data", addressTax);
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, "Address with businessPurpose Tax not Found", null);
                    return new ResponseEntity<>(result, HttpStatus.OK);
                }
            } else {
                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, "Address with businessPurpose Tax not Found", null);
                logger.info("Response Success ->" + result);
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
}
