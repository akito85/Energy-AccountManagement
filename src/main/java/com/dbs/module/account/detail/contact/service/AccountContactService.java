package com.dbs.module.account.detail.contact.service;

import com.dbs.module.account.utils.UtilsAccount;
import com.dbs.module.account.utils.ConstantAccount;
import com.dbs.module.account.detail.contact.dto.ViewAccountContactDTO;
import com.dbs.module.account.detail.contact.dto.AccountContactDTO;
import com.dbs.module.account.detail.contact.dto.AccountContactUpdateRequestDTO;
import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.*;
import com.dbs.database.crm.entities.accountmanagement.*;
import com.dbs.database.crm.entities.accountmanagement.view.VW_CONTACT;
import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import com.dbs.database.crm.entities.usermanagement.M_GLOBAL_TYPE;
import com.dbs.database.crm.entities.usermanagement.M_USER;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.accountmanagement.Account.*;
import com.dbs.database.crm.repositories.accountmanagement.VwChooseContactRepo;
import com.dbs.database.crm.repositories.accountmanagement.VwContactRepo;
import com.dbs.database.crm.repositories.usermanagement.AuditTrailRepo;
import com.dbs.database.crm.repositories.usermanagement.MGlobalTypeRepo;
import com.dbs.database.crm.repositories.usermanagement.MUserRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import com.dbs.module.account.master.contact.dto.ViewContactDTO;
import com.dbs.module.account.master.contact.service.ContactService;
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

import javax.validation.Validator;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.dbs.common.base.utils.CommonHelper.removeSpace;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class AccountContactService {
    
    private static final Logger logger = LoggerFactory.getLogger(AccountContactService.class);
    
    @Autowired
    private MAccountContactRepo acRepo;
    @Autowired
    private MContactRepo cRepo;
    @Autowired
    private MContactDetailsRepo cdRepo;
    @Autowired
    private AuditTrailRepo auditTrailRepo;
    @Autowired
    private Validator validator;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MUserRepo userRepo;
    @Autowired
    private MGlobalTypeRepo mGlobalTypeRepo;
    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;
    @Autowired
    private MaddressRepo addrRepo;
    @Autowired
    private MAccountAddressRepo acaddrRepo;
    @Autowired
    private VwCustomerContactRepo vcRepo;
    @Autowired
    private VwCustomerContactDtlRepo vcdRepo;
    @Autowired
    private RGlobalTypeValueRepo glbRepo;
    @Autowired
    private VwChooseContactRepo vwChooseContactRepo;
    
    @Autowired 
    private GlobalTypeValueService globalTypeService;
    @Autowired
    private VWCustomerAddressRepo vwAddressRepo;
    
    @Autowired
    private CreateParty party;

    @Autowired
    private ContactUtils contactUtils;

    @Autowired
    private ContactService contactService;

    @Autowired
    private VwContactRepo vwContactRepo;

    @SuppressWarnings({"java:S3776","java:S1192"})
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> create(AccountContactDTO request) {
        ResponseObject result;
        try {

            ResponseEntity<ResponseObject> validate = this.validateCreateAccountContact(Boolean.FALSE, request);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            List<M_ACCOUNT_CONTACT> accountContactHavePrimaryAndActive = acRepo.findAllByAccountIdAndPrimaryFlagAndStatus(request.getAccountId(), Boolean.TRUE, FlowStatus.ACTIVE.name());
            if(request.getNeedValidation()!=null && !request.getNeedValidation() && !accountContactHavePrimaryAndActive.isEmpty() && request.getPrimaryFlag()){
                for(M_ACCOUNT_CONTACT accContactPrimary : accountContactHavePrimaryAndActive) {
                    accContactPrimary.setPrimaryFlag(Boolean.FALSE);
                    accContactPrimary.setUpdatedBy(UserDetailUtils.getUsername());
                    accContactPrimary.setUpdatedDate(new Date());
                    acRepo.save(accContactPrimary);
                }
            }

            M_CONTACT mContact = new M_CONTACT();
            if(request.getContact().getContactId()==null) {
                mContact = contactService.saveContact(request.getContact());
            }

            // ACCOUNT CONTACT
            M_ACCOUNT_CONTACT accContact = new M_ACCOUNT_CONTACT();
            accContact.setContactId(request.getContact().getContactId()==null?mContact.getContactId():request.getContact().getContactId());
            accContact.setPrimaryFlag(request.getPrimaryFlag());
            accContact.setContactAddressId(request.getContactAddressId());
            accContact.setAccountId(request.getAccountId());
            accContact.setAdditionalNote(removeSpace(request.getAdditionalNote()));
            accContact.setDescription(removeSpace(request.getDescription()));
            accContact.setCreatedBy(UserDetailUtils.getUsername());
            accContact.setStatus(FlowStatus.ACTIVE.name());
            accContact.setCreatedDate(new Date());
            acRepo.save(accContact);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED, ResponseUtils.MESSAGE_CREATED, accContact);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateCreateAccountContact(Boolean api, AccountContactDTO request) {
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

        // VALIDASI CONTACT DAN ADDRESS YG SAMA
        if(request.getContact().getContactId()!=null) {
            Optional<M_ACCOUNT_CONTACT> checkContactAndAddressExist = acRepo.findTopByAccountIdAndContactIdAndContactAddressId(request.getAccountId(), request.getContact().getContactId(), request.getContactAddressId());
            if(checkContactAndAddressExist.isPresent()){
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "The selected contact and address is already exist", null), HttpStatus.BAD_REQUEST);
            }
        } else {
            Optional<M_CONTACT> checkContactExist = cRepo.findTopByContactNameAndJobIdAndPositionId(removeSpace(request.getContact().getContactName()), request.getContact().getJobId(), request.getContact().getPositionId());
            if(checkContactExist.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "The contact data name, job and position have been registered", null), HttpStatus.BAD_REQUEST);
            }
        }

        List<M_ACCOUNT_CONTACT> accountContactHavePrimaryAndActive = acRepo.findAllByAccountIdAndPrimaryFlagAndStatus(request.getAccountId(), Boolean.TRUE, FlowStatus.ACTIVE.name());
        if(request.getNeedValidation()!=null && request.getNeedValidation() && request.getPrimaryFlag() && !accountContactHavePrimaryAndActive.isEmpty()) {
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND, UtilsAccount.warningInactive(ConstantAccount.ACCOUNT_CONTACT)
                    , UtilsAccount.messageWarningInactivePrimary(ConstantAccount.ACCOUNT_CONTACT)), HttpStatus.NOT_FOUND);
        }

        return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.ACCOUNT_CONTACT);
    }

    @SuppressWarnings("java:S1192")
    public ResponseEntity<ResponseObject> getDetailChoose(Integer contactId) {
        ResponseObject result;
        logger.info("Get Contact Detail Choose");
        try {
            Optional<VW_CONTACT> dataContact = vwContactRepo.findById(contactId);
            if(dataContact.isPresent()) {
                ViewContactDTO data = contactService.getResponseContact(dataContact.get());
                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, UtilsAccount.messageSuccess(ConstantAccount.DETAIL, ConstantAccount.ACCOUNT_CONTACT), data);
            } else {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND, UtilsAccount.messageDataNotFound(ConstantAccount.ACCOUNT_CONTACT, contactId), ResponseUtils.DATA_EMPTY);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
                        
		} catch (

		Exception e) {

			logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
    @SuppressWarnings("java:S1192")
    public ResponseEntity<ResponseObject> getDetail(Integer accountContactId) {
        ResponseObject result;
        logger.info("Get Account Contact Detail");
        try {
            Optional<VW_CUSTOMER_CONTACT> dataAC = vcRepo.findTopByAccountContactId(accountContactId);
            if(dataAC.isPresent()) {
                ViewAccountContactDTO resp = objectMapper.convertValue(dataAC.get(), ViewAccountContactDTO.class);
                resp.setPrimaryFlagValue(Boolean.TRUE.equals(dataAC.get().getPrimaryFlag())? "Primary" : "-");
               Optional<VW_CONTACT> contact = vwContactRepo.findById(dataAC.get().getContactId());
               if(contact.isPresent()) {
                   ViewContactDTO respContact = contactService.getResponseContact(contact.get());
                   resp.setContact(respContact);
               }
                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, UtilsAccount.messageSuccess(ConstantAccount.DETAIL, ConstantAccount.ACCOUNT_CONTACT), resp);
            } else {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND, UtilsAccount.messageDataNotFound(ConstantAccount.ACCOUNT_CONTACT, accountContactId), ResponseUtils.DATA_EMPTY);
            }

            //acr
//            List<AUDIT_TRAIL> auditTrail = auditTrailRepo.findAll().stream()
//                    .filter(e -> e.getTableName().equalsIgnoreCase("M_ACCOUNT_CONTACT"))
//                    .filter(f -> f.getDataId().equalsIgnoreCase(dataAccountContact.getAccountContactId().toString()))
//                    .collect(Collectors.toList());
//            data.put("activeInactiveLog", auditTrail);

            return new ResponseEntity<>(result, HttpStatus.OK);
                        
		} catch (

		Exception e) {

			logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
    
    
    public ResponseEntity<ResponseObject> getListAccountContact(Integer accountId, MaterialTablePagingRequest pagingData,
			PagedResourcesAssembler<VW_CUSTOMER_CONTACT> assembler) {
            logger.info("Get List Account Contact");
            try {
                Map<String, Object> filter = new HashMap<>();
                filter.put("accountId", accountId);
                
                Page<VW_CUSTOMER_CONTACT> data;

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
                    data = vcRepo.findAll(vcRepo.getSpecificationFromFilters(pagingData, filter), PagingUtils.getPaging(pagingData));
                } else {
                    data = vcRepo.findAll(vcRepo.getSpecificationDefault(filter), PagingUtils.getPaging(pagingData));
                }
                
                List<LinkedHashMap<String, Object>> allData = data.getContent().stream()
                    .map(g -> {
                        LinkedHashMap<String, Object> acontact = new LinkedHashMap<>();
                        acontact.put("accountContactId", g.getAccountContactId());
                        acontact.put("accountId", g.getAccountId());
                        acontact.put("contactId", g.getContactId());
                        acontact.put("contactAddress", g.getContactAddress());
                        acontact.put("additionalNote", g.getAdditionalNote());
                        acontact.put("description", g.getDescription());
                        acontact.put("primaryFlag", g.getPrimaryFlag() ? "Primary" : "-");
                        acontact.put("primaryFlagValue", g.getPrimaryFlag());
                        acontact.put("firstName", g.getFirstName());
                        acontact.put("middleName", g.getMiddleName());
                        acontact.put("lastName", g.getLastName());
                        acontact.put("contactName", g.getContactName());
                        acontact.put("jobId", g.getJobId());
                        acontact.put("jobName", g.getJobName());
                        acontact.put("positionId", g.getPositionId());
                        acontact.put("positionName", g.getPositionName());
                        
                        List<LinkedHashMap<String, Object>> allDetail = new ArrayList<>();
                        List<M_CONTACT_DETAILS> allCDet = cdRepo.findAllByContactIdOrderByContactDetailsIdAsc(g.getContactId());
                        for (M_CONTACT_DETAILS dq : allCDet) {
                            LinkedHashMap<String, Object> dDetail = new LinkedHashMap<>();
                            Optional<R_GLOBAL_TYPE_VALUE> ctype = rGlobalTypeValueRepo.findById(dq.getType());
                            Optional<R_GLOBAL_TYPE_VALUE> itype = rGlobalTypeValueRepo.findById(dq.getInputType());
                            dDetail.put("contactId", dq.getContactId());
                            dDetail.put("typeId", dq.getType());
                            dDetail.put("typeName", ctype.get().getName());
                            dDetail.put("inputTypeId", dq.getInputType());
                            dDetail.put("inputTypeName", itype.get().getName());
                            dDetail.put("fullValue", dq.getFullValue());
                            dDetail.put("contactValue", dq.getContactValue());
                            allDetail.add(dDetail);
                        }

                        acontact.put("contactDetails", allDetail);
                        acontact.put("status", g.getStatus());
                        acontact.put("createdBy", g.getCreatedBy());
                        acontact.put("createdDate", g.getCreatedDate());
                        acontact.put("updatedBy", g.getUpdatedBy());
                        acontact.put("updatedDate", g.getUpdatedDate());
                                   
                        return acontact;
                    })
                    .collect(Collectors.toList());
          
            PagedModel<EntityModel<VW_CUSTOMER_CONTACT>> pagedData = assembler.toModel(data);
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
    public ResponseEntity<ResponseObject> update(AccountContactUpdateRequestDTO request) {
        try {

            ResponseEntity<ResponseObject> validate = this.validateUpdate(Boolean.FALSE, request);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            Optional<M_ACCOUNT_CONTACT> data = acRepo.findById(request.getAccountContactId());
            M_ACCOUNT_CONTACT accountContact = data.get();
            List<M_ACCOUNT_CONTACT> accountAddressesHavePrimaryAndActive = acRepo.findAllByAccountIdAndPrimaryFlagAndStatus(accountContact.getAccountId(), Boolean.TRUE, FlowStatus.ACTIVE.name());
            for(M_ACCOUNT_CONTACT accContactPrimary : accountAddressesHavePrimaryAndActive) {
                if(request.getNeedValidation()!=null && !request.getNeedValidation() && request.getPrimaryFlag()!=null && request.getPrimaryFlag() && !accContactPrimary.getAccountContactId().equals(request.getAccountContactId())){
                    accContactPrimary.setPrimaryFlag(false);
                    accContactPrimary.setUpdatedBy(UserDetailUtils.getUsername());
                    accContactPrimary.setUpdatedDate(new Date());
                    acRepo.save(accContactPrimary);
                }
            }

            accountContact.setDescription(removeSpace(request.getDescription()));
            accountContact.setPrimaryFlag(request.getPrimaryFlag());
            accountContact.setUpdatedBy(UserDetailUtils.getUsername());
            accountContact.setUpdatedDate(new Date());
            M_ACCOUNT_CONTACT saveAC = acRepo.save(accountContact);

//            //acr
//            M_CONTACT d = cRepo.findByContactId(saveAC.getContactId());
//            d.setDescription(removeSpace(request.getDescriptionContact()));
//            cRepo.save(d);

            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.CREATE, ConstantAccount.ACCOUNT_ADDRESS), saveAC), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateUpdate(Boolean api, AccountContactUpdateRequestDTO request) {
        try {
            Optional<M_ACCOUNT_CONTACT> data = acRepo.findById(request.getAccountContactId());
            if(data.isPresent()) {
                M_ACCOUNT_CONTACT accountContact = data.get();
                List<M_ACCOUNT_CONTACT> accountAddressesHavePrimaryAndActive = acRepo.findAllByAccountIdAndPrimaryFlagAndStatus(accountContact.getAccountId(), Boolean.TRUE, FlowStatus.ACTIVE.name());
                if(!accountAddressesHavePrimaryAndActive.isEmpty()){
                    for(M_ACCOUNT_CONTACT cek : accountAddressesHavePrimaryAndActive) {
                        if(Boolean.TRUE.equals(request.getNeedValidation()!=null && request.getNeedValidation() && request.getPrimaryFlag() != null && request.getPrimaryFlag()) && !cek.getAccountContactId().equals(request.getAccountContactId())){
                            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                                    UtilsAccount.warningInactive(ConstantAccount.ACCOUNT_CONTACT), UtilsAccount.messageWarningInactivePrimary(ConstantAccount.ACCOUNT_CONTACT)), HttpStatus.NOT_FOUND);
                        } else if(Boolean.TRUE.equals(data.get().getPrimaryFlag() && data.get().getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())) && Boolean.FALSE.equals(request.getPrimaryFlag())){
                            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                                    "You cannot inactivate this contact because there must be at least one primary flag contact", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                        }
                    }
                }
                return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.ACCOUNT_CONTACT);
            } else {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        UtilsAccount.messageDataNotFound(ConstantAccount.ACCOUNT_ADDRESS, request.getAccountContactId()), ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
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
    public ResponseEntity<ResponseObject> activeinactive(AccountContactUpdateRequestDTO request) {
        ResponseObject result;
        try {
            Optional<M_ACCOUNT_CONTACT> data = acRepo.findById(request.getAccountContactId());
            Optional<M_ACCOUNT_CONTACT> cekPrimaryFlag = acRepo.findByAccountIdAndPrimaryFlagAndStatus(request.getAccountId(), true, FlowStatus.ACTIVE.name());
            if(data.isPresent()) {
                // tdk boleh lebih dari satu primaryFlag
                if(Boolean.TRUE.equals(data.get().getPrimaryFlag() && cekPrimaryFlag.isPresent()) && !cekPrimaryFlag.get().getAccountContactId().equals(request.getAccountContactId())) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                            "There is Primary Flag are still active!", null), HttpStatus.NOT_FOUND);
                } else if(Boolean.TRUE.equals(data.get().getPrimaryFlag()) && data.get().getStatus().equalsIgnoreCase("ACTIVE")){
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "You cannot inactivate this contact because there must be at least one primary flag contact", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                } else {
                    M_ACCOUNT_CONTACT ac = data.get();
                    LinkedHashMap<String, Object> oldAC = new LinkedHashMap<>();
                    oldAC.put("accountContactId", ac.getAccountContactId());
                    oldAC.put("contactId", ac.getContactId());
                    oldAC.put("primaryFlag", ac.getPrimaryFlag());
                    oldAC.put("status", ac.getStatus());
                    oldAC.put("desription", ac.getDescription());
                    AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
                    ObjectMapper mapper = new ObjectMapper();
                    String oldValue = mapper.writeValueAsString(oldAC);
                    auditTrail.setOldValue(oldValue);
                    auditTrail.setTableName("M_ACCOUNT_CONTACT");
                    auditTrail.setDataId(ac.getAccountContactId().toString());
                    auditTrail.setRemark(removeSpace(request.getRemark()));
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
                    if (ac.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())) {
                        ac.setStatus(FlowStatus.INACTIVE.name());
                        ac.setUpdatedDate(new Date());
                        ac.setUpdatedBy(UserDetailUtils.getUsername() != null ? UserDetailUtils.getUsername() : null);
                        M_ACCOUNT_CONTACT saveAC = acRepo.save(ac);
                        LinkedHashMap<String, Object> newAC = new LinkedHashMap<>();
                        newAC.put("accountContactId", saveAC.getAccountContactId());
                        newAC.put("contactId", saveAC.getContactId());
                        newAC.put("primaryFlag", saveAC.getPrimaryFlag());
                        newAC.put("status", saveAC.getStatus());
                        newAC.put("desription", saveAC.getDescription());
                        String newValue = mapper.writeValueAsString(newAC);
                        auditTrail.setNewValue(newValue);
                        auditTrail.setOperation(FlowStatus.INACTIVE.name());
                        auditTrailRepo.save(auditTrail);
                        result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                                ResponseUtils.MESSAGE_INACTIVE, newAC);
                        return new ResponseEntity<>(result, result.getHttpCode());
                    } else if(ac.getStatus().equalsIgnoreCase(FlowStatus.INACTIVE.name())) {
                        if(ac.getPrimaryFlag()) {
                            List<M_ACCOUNT_CONTACT> cek = acRepo.findAllByAccountIdAndPrimaryFlagAndStatus(ac.getAccountId(), Boolean.TRUE, FlowStatus.ACTIVE.name());
                            if(cek.isEmpty()) {
                                ac.setStatus(FlowStatus.ACTIVE.name());
                                ac.setUpdatedDate(new Date());
                                ac.setUpdatedBy(UserDetailUtils.getUsername() != null ? UserDetailUtils.getUsername() : null);
                                M_ACCOUNT_CONTACT saveAC = acRepo.save(ac);
                                LinkedHashMap<String, Object> newAC = new LinkedHashMap<>();
                                newAC.put("accountContactId", saveAC.getAccountContactId());
                                newAC.put("contactId", saveAC.getContactId());
                                newAC.put("primaryFlag", saveAC.getPrimaryFlag());
                                newAC.put("status", saveAC.getStatus());
                                newAC.put("desription", saveAC.getDescription());
                                String newValue = mapper.writeValueAsString(newAC);
                                auditTrail.setNewValue(newValue);
                                auditTrail.setOperation(FlowStatus.ACTIVE.name());
                                auditTrailRepo.save(auditTrail);
                                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                                        ResponseUtils.MESSAGE_ACTIVE, newAC);
                                return new ResponseEntity<>(result, result.getHttpCode());
                            } else {
                                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                                        "This contact can't be activated because there is only one primary contact allowed! please inactivate the active primary contact first!", ResponseUtils.DATA_EMPTY);
                                logger.info("Response Success ->" + result);
                                return new ResponseEntity<>(result, result.getHttpCode());
                            }
                        } else {
                            ac.setStatus(FlowStatus.ACTIVE.name());
                            ac.setUpdatedDate(new Date());
                            ac.setUpdatedBy(UserDetailUtils.getUsername() != null ? UserDetailUtils.getUsername() : null);
                            M_ACCOUNT_CONTACT saveAC = acRepo.save(ac);
                            LinkedHashMap<String, Object> newAC = new LinkedHashMap<>();
                            newAC.put("accountContactId", saveAC.getAccountContactId());
                            newAC.put("contactId", saveAC.getContactId());
                            newAC.put("primaryFlag", saveAC.getPrimaryFlag());
                            newAC.put("status", saveAC.getStatus());
                            newAC.put("desription", saveAC.getDescription());
                            String newValue = mapper.writeValueAsString(newAC);
                            auditTrail.setNewValue(newValue);
                            auditTrail.setOperation(FlowStatus.ACTIVE.name());
                            auditTrailRepo.save(auditTrail);
                            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                                    ResponseUtils.MESSAGE_ACTIVE, newAC);
                            return new ResponseEntity<>(result, result.getHttpCode());
                        }
                    }

                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            "Account contact don't have is active and status ", ResponseUtils.DATA_EMPTY);
                    logger.info("Response Success ->" + result);
                    return new ResponseEntity<>(result, result.getHttpCode());

                }
            }else {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Account Contact with account id " + request.getAccountId()+ " and contact id" + request.getContactId() + " is not found.", ResponseUtils.DATA_EMPTY);
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
    
    public ResponseEntity<ResponseObject> getCountryCode() {
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Contact Country Code Phone", "ACTIVE", false);
            logger.info("Contact Country Code Phone " + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;
            
            if(!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals("ACTIVE"))
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
                    result.setMessage("Success get Contact Country Code Phone Type");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Fail get Contact Country Code Phone Type");
                    result.setData("There is no Contact Country Code Phone");
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Fail get Contact Country Code Phone");
                result.setData("There is no Contact Country Code Phone");
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
    
    public ResponseEntity<ResponseObject> getZoneCode(Integer idCountry) {
        logger.info("Find Account Group Type");
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Contact Zone Code Phone",
                    "ACTIVE", false);
            logger.info("Zone Code" + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = rGlobalTypeValueRepo.findByParentValueAndIsDeleted(idCountry,false);
            List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
            for (R_GLOBAL_TYPE_VALUE rgtv : areas) {
                LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                ar.put("id", rgtv.getGlbTypeValId());
                ar.put("code", rgtv.getGlbValue());
                ar.put("text", rgtv.getName());
                areaList.add(ar);
            }
            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success get Zone Code");
            result.setData(areaList);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public ResponseEntity<ResponseObject> getJob() {
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Contact Job", "ACTIVE", false);
            logger.info("Contact Job " + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;
            
            if(!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals("ACTIVE"))
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
                    result.setMessage("Success get Contact Job");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Fail Contact Job");
                    result.setData("There is no Contact Job");
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Fail get Contact Job");
                result.setData("There is no Contact Job");
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
    
    public ResponseEntity<ResponseObject> getPosition() {
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Contact Position", "ACTIVE", false);
            logger.info("Contact Position " + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;
            
            if(!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals("ACTIVE"))
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
                    result.setMessage("Success get Contact Position");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Fail Contact Position");
                    result.setData("There is no Contact Position");
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Fail get Contact Position");
                result.setData("There is no Contact Position");
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
    
    public ResponseEntity<ResponseObject> getInputType() {
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Input Type", "ACTIVE", false);
            logger.info("Input Type " + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;
            
            if(!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals("ACTIVE"))
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
                    result.setMessage("Success get Input Type");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Fail Input Type");
                    result.setData("There is no Input Type");
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Fail get Input Type");
                result.setData("There is no Input Type");
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
    
    public ResponseEntity<ResponseObject> getContactType() {
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Contact Type", "ACTIVE", false);
            logger.info("Contact Type " + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;
            
            if(!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals("ACTIVE"))
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
                    result.setMessage("Success get Contact Type");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Fail Contact Type");
                    result.setData("There is no Contact Type");
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Fail get Contact Type");
                result.setData("There is no Contact Type");
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
    
    public ResponseEntity<ResponseObject> getListAddress(Integer accountId) {
		ResponseObject result = new ResponseObject();
		logger.info("Get List address");
		try {
                List<M_ACCOUNT_ADDRESS> dataa = acaddrRepo.findAllByAccountIdAndStatusIgnoreCase(accountId, "ACTIVE");
                List<LinkedHashMap<String, Object>> datas = new LinkedList<>();
                
                List<M_ACCOUNT_ADDRESS> dataax = dataa.stream().filter(distinctByKey(g -> g.getAddressId())).collect(Collectors.toList());
                        
                logger.info(dataa.toString());
                for(M_ACCOUNT_ADDRESS addr : dataax) 
                {
                    M_ADDRESSES dataAddress = addrRepo.findByAddressId(addr.getAddressId());
                    LinkedHashMap<String, Object> data = new LinkedHashMap<>();
                    data.put("addressId", dataAddress.getAddressId());
                    data.put("fullAddress", dataAddress.getFullAddress());
                    data.put("status", dataAddress.getStatus());
                    data.put("createdDate", dataAddress.getCreatedDate());
                    data.put("createdBy", dataAddress.getCreatedBy());
                    data.put("updatedDate", dataAddress.getUpdatedDate());
                    data.put("updatedBy", dataAddress.getUpdatedBy());

                    datas.add(data);
                }
                        
                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, "Success get data", datas);
                logger.info("Response Success ->" + result);
                return new ResponseEntity<>(result, HttpStatus.OK);
                        
		} catch (

		Exception e) {

                    logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);

		}
    }
    
 
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}
