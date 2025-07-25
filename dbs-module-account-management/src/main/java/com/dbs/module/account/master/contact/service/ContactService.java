package com.dbs.module.account.master.contact.service;

import com.dbs.module.account.master.contact.dto.ViewValueContactDetailsDTO;
import com.dbs.module.account.master.contact.dto.ContactUpdateRequestDTO;
import com.dbs.module.account.master.contact.dto.ContactDetailsCreateRequestDTO;
import com.dbs.module.account.master.contact.dto.ContactCreateRequestDTO;
import com.dbs.module.account.master.contact.dto.ContactDetailsUpdateRequestDTO;
import com.dbs.module.account.master.contact.dto.ViewContactDTO;
import com.dbs.module.account.master.contact.dto.ViewContactDetailsDTO;
import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.*;
import com.dbs.database.crm.entities.accountmanagement.*;
import com.dbs.database.crm.entities.accountmanagement.view.VW_CONTACT;
import com.dbs.database.crm.entities.payment.R_PAY_BANK_CONTACT;
import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import com.dbs.database.crm.entities.usermanagement.M_USER;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.accountmanagement.Account.*;
import com.dbs.database.crm.repositories.accountmanagement.VwChooseContactRepo;
import com.dbs.database.crm.repositories.accountmanagement.VwContactRepo;
import com.dbs.database.crm.repositories.payment.BankContactRepo;
import com.dbs.database.crm.repositories.usermanagement.AuditTrailRepo;
import com.dbs.database.crm.repositories.usermanagement.MGlobalTypeRepo;
import com.dbs.database.crm.repositories.usermanagement.MUserRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import com.dbs.module.account.master.gassource.dto.ActiveInactiveDTO;
import com.dbs.module.account.utils.ConstantAccount;
import com.dbs.module.account.utils.UtilsAccount;
import com.dbs.module.account.utils.dtoLabelValue;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import javax.annotation.Nullable;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.dbs.common.base.utils.CommonHelper.removeSpace;
import static com.dbs.common.library.utils.StringUtils.capitalizeFully;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class ContactService {
    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);
    
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
    private GlobalTypeValueService globalTypeService;

    @Autowired
    private VwChooseContactRepo vwChooseContactRepo;

    @Autowired
    private CreateParty party;

    @Autowired
    private MAccountContactRepo mAccountContactRepo;

    @Autowired
    private ContactUtils contactUtils;

    @Autowired
    private BankContactRepo bankContactRepo;

    @Autowired
    private VwCustomerContactRepo vwCustomerContactRepo;

    @Autowired
    private ChooseContactRepoCustom chooseContactRepoCustom;

    @Autowired
    private VwContactRepo vwContactRepo;

    @SuppressWarnings({"java:S3776","java:S1192"})
    @Transactional(rollbackFor = Exception.class, readOnly = false)
    public ResponseEntity<ResponseObject> create(ContactCreateRequestDTO request) {
        ResponseObject result;
        try {

            ResponseEntity<ResponseObject> validate = this.validateCreate(Boolean.FALSE, request);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }
            
            M_CONTACT newContact = this.saveContact(request);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED, UtilsAccount.messageSuccess(ConstantAccount.CREATE, ConstantAccount.MASTER_CONTACT), newContact);
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public M_CONTACT saveContact(ContactCreateRequestDTO request) {
        // MASTER CONTACT
        M_CONTACT newContact = new M_CONTACT();
        newContact.setFirstName(removeSpace(request.getFirstName()));
        newContact.setMiddleName(removeSpace(request.getMiddleName()));
        newContact.setLastName(removeSpace(request.getLastName()));
        newContact.setContactName(removeSpace(request.getContactName()));
        newContact.setJobId(request.getJobId());
        newContact.setPositionId(request.getPositionId());
        newContact.setStatus(FlowStatus.ACTIVE.name());
        newContact.setCreatedDate(new Date());
        newContact.setCreatedBy(UserDetailUtils.getUsername());
        cRepo.save(newContact);

        Integer partyId = party.createParty(CreateParty.CONTACT, newContact.getContactId().toString(), newContact.getContactName(), newContact.getContactName(), UserDetailUtils.getUserEntity());

        newContact.setPartyId(partyId);
        cRepo.save(newContact);

        //CONTACT DETAIL
        List<ContactDetailsCreateRequestDTO> contactDetailNew = request.getViewDetails();
        List<M_CONTACT_DETAILS> addedDetail = new ArrayList<>();
        if (contactDetailNew != null) {
            for (ContactDetailsCreateRequestDTO avv : contactDetailNew) {
                M_CONTACT_DETAILS newContactDetails = new M_CONTACT_DETAILS();
                List<String> value = contactUtils.getContactDetailValue(avv.getPrefix1(), avv.getPrefix2(), avv.getValue(), avv.getSufix());
                newContactDetails.setFullValue(value.get(0));
                newContactDetails.setContactValue(value.get(1));
                newContactDetails.setContactId(newContact.getContactId());
                newContactDetails.setType(avv.getType());
                newContactDetails.setInputType(avv.getInputType());
                newContactDetails.setPrefix1(avv.getPrefix1());
                newContactDetails.setPrefix2(avv.getPrefix2());
                newContactDetails.setValue(avv.getValue());
                newContactDetails.setSufix(avv.getSufix());
                newContactDetails.setCreatedDate(new Date());
                newContactDetails.setCreatedBy(UserDetailUtils.getUsername());
                newContactDetails.setStatus(FlowStatus.ACTIVE.name());
                addedDetail.add(newContactDetails);
            }
            cdRepo.saveAll(addedDetail);
        }

        return newContact;
    }

    public ResponseEntity<ResponseObject> validateCreate(Boolean api, ContactCreateRequestDTO request) {
        ResponseObject result;
        try {
            Set<ConstraintViolation<ContactCreateRequestDTO>> violations = this.validator.validate(request);
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
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, validateHeader.get(0), violationHeaderList);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            Optional<M_CONTACT> cekExist = cRepo.findTopByContactNameAndJobIdAndPositionId(request.getContactName().strip(), request.getJobId(), request.getPositionId());
            if(cekExist.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "The contact data name, job and position have been registered", null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.MASTER_CONTACT);

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
    public ResponseEntity<ResponseObject> update(ContactUpdateRequestDTO request) {
        ResponseObject result;
        try {
            ResponseEntity<ResponseObject> validate = this.validateUpdate(Boolean.FALSE, request);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            M_CONTACT updateContact = cRepo.findByContactId(request.getContactId());
            updateContact.setFirstName(removeSpace(request.getFirstName()));
            updateContact.setMiddleName(removeSpace(request.getMiddleName()));
            updateContact.setLastName(removeSpace(request.getLastName()));
            updateContact.setContactName(removeSpace(request.getContactName()));
            updateContact.setJobId(request.getJobId());
            updateContact.setPositionId(request.getPositionId());
            updateContact.setUpdatedBy(UserDetailUtils.getUsername());
            updateContact.setUpdatedDate(new Date());
            cRepo.save(updateContact);

            // UPDATE PARTY
            party.updateParty(updateContact.getPartyId(), updateContact.getContactName(), updateContact.getContactName());

            List<M_CONTACT_DETAILS> dataContactDetails = cdRepo.findAllByContactId(updateContact.getContactId());
            List<ContactDetailsUpdateRequestDTO> contactDetailUpdate = request.getViewDetails();
            List<M_CONTACT_DETAILS> addedDetail = new ArrayList<>();
            if (contactDetailUpdate != null) {
                List<Integer> receivedIdCd = new ArrayList<>();
                for (ContactDetailsUpdateRequestDTO avv : contactDetailUpdate) {
                    M_CONTACT_DETAILS checkDataContactDetails = avv.getContactDetailId()!=null? cdRepo.findByContactDetailsId(avv.getContactDetailId()):null;
                    M_CONTACT_DETAILS updateContactDetail = new M_CONTACT_DETAILS();
                    if(checkDataContactDetails!=null) {
                        updateContactDetail = checkDataContactDetails;
                        updateContactDetail.setUpdatedBy(UserDetailUtils.getUsername());
                        updateContactDetail.setUpdatedDate(new Date());
                        receivedIdCd.add(avv.getContactDetailId());
                    } else {
                        updateContactDetail.setCreatedBy(UserDetailUtils.getUsername());
                        updateContactDetail.setCreatedDate(new Date());
                    }
                    List<String> value = contactUtils.getContactDetailValue(avv.getPrefix1(), avv.getPrefix2(), avv.getValue(), avv.getSufix());
                    updateContactDetail.setFullValue(value.get(0));
                    updateContactDetail.setContactValue(value.get(1));
                    updateContactDetail.setContactId(updateContact.getContactId());
                    updateContactDetail.setType(avv.getType());
                    updateContactDetail.setInputType(avv.getInputType());
                    updateContactDetail.setPrefix1(avv.getPrefix1());
                    updateContactDetail.setPrefix2(avv.getPrefix2());
                    updateContactDetail.setValue(avv.getValue());
                    updateContactDetail.setSufix(avv.getSufix());
                    updateContactDetail.setStatus(FlowStatus.ACTIVE.name());
                    addedDetail.add(updateContactDetail);
                }
                cdRepo.saveAll(addedDetail);

                // DELETE CONTACT DETAILS
                for(M_CONTACT_DETAILS del : dataContactDetails) {
                    if(receivedIdCd.isEmpty() || !receivedIdCd.contains(del.getContactDetailsId())) {
                        cdRepo.deleteById(del.getContactDetailsId());
                    }
                }
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.UPDATE, ConstantAccount.MASTER_CONTACT), ResponseUtils.MESSAGE_OK);
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateUpdate(Boolean api, ContactUpdateRequestDTO request) {
        ResponseObject result;
        try {
            M_CONTACT data = cRepo.findByContactId(request.getContactId());
            if(data==null) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        UtilsAccount.messageDataNotFound(ConstantAccount.MASTER_CONTACT, request.getContactId()), ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            Optional<M_CONTACT> checkExist = cRepo.findTopByContactNameAndJobIdAndPositionId(removeSpace(request.getContactName()), request.getJobId(), request.getPositionId());
            if(checkExist.isPresent() && !checkExist.get().getContactId().equals(request.getContactId())) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "The contact data name, job and position have been registered", null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.MASTER_CONTACT);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> activeinactive(ActiveInactiveDTO request) {
        ResponseObject result;
        try {
            M_CONTACT data = cRepo.findByContactId(request.getId());
            if(data != null) {
                M_CONTACT c = data;
                LinkedHashMap<String, Object> oldAC = new LinkedHashMap<>();
                oldAC.put("contactId", c.getContactId());
                oldAC.put("contactName", c.getContactName());
                oldAC.put("status", c.getStatus());
                oldAC.put("desription", c.getDescription());
                AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
                ObjectMapper mapper = new ObjectMapper();
                String oldValue = mapper.writeValueAsString(oldAC);
                auditTrail.setOldValue(oldValue);
                if(request.getRemark().equalsIgnoreCase("")) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "remark not be empty", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                auditTrail.setRemark(request.getRemark());
                auditTrail.setTableName("M_CONTACT");
                auditTrail.setDataId(c.getContactId().toString());
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
                List<M_CONTACT_DETAILS> dataContactDetails = cdRepo.findAllByContactId(c.getContactId());
                if (c.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())) {
                    Optional<M_ACCOUNT_CONTACT> checkAccount = mAccountContactRepo.findTopByContactIdAndStatus(request.getId(), FlowStatus.ACTIVE.name());
                    if(checkAccount.isPresent()) {
                        result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                                "You cannot inactivate contact because this contact is being used in account", ResponseUtils.DATA_EMPTY);
                        return new ResponseEntity<>(result, result.getHttpCode());
                    }
                    c.setStatus(FlowStatus.INACTIVE.name());
                    c.setUpdatedDate(new Date());
                    c.setUpdatedBy(UserDetailUtils.getUsername() != null ? UserDetailUtils.getUsername() : null);
                    M_CONTACT saveC = cRepo.save(c);
                    // UPDATE PARTY
                    party.activeInactiveParty(c.getPartyId());
                    // CONTACT DETAILS
                    for(M_CONTACT_DETAILS rmv : dataContactDetails) {
                        rmv.setStatus(FlowStatus.INACTIVE.name());
                        cdRepo.save(rmv);
                    }

                    LinkedHashMap<String, Object> newAC = new LinkedHashMap<>();
                    newAC.put("contactId", saveC.getContactId());
                    newAC.put("contactName", saveC.getContactName());
                    newAC.put("status", saveC.getStatus());
                    newAC.put("desription", saveC.getDescription());
                    String newValue = mapper.writeValueAsString(newAC);
                    auditTrail.setNewValue(newValue);
                    auditTrail.setOperation(FlowStatus.INACTIVE.name());
                    auditTrailRepo.save(auditTrail);
                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            ResponseUtils.MESSAGE_INACTIVE, newAC);
                return new ResponseEntity<>(result, result.getHttpCode());
                } else if(c.getStatus().equalsIgnoreCase(FlowStatus.INACTIVE.name())) {
                    c.setStatus(FlowStatus.ACTIVE.name());
                    c.setUpdatedDate(new Date());
                    c.setUpdatedBy(StringUtils.hasValue(UserDetailUtils.getUsername()) ? UserDetailUtils.getUsername() : null);
                    M_CONTACT saveC = cRepo.save(c);
                    // UPDATE PARTY
                    party.activeInactiveParty(c.getPartyId());
                    // CONTACT DETAILS
                    for(M_CONTACT_DETAILS rmv : dataContactDetails) {
                        rmv.setStatus(FlowStatus.ACTIVE.name());
                        cdRepo.save(rmv);
                    }

                    LinkedHashMap<String, Object> newAC = new LinkedHashMap<>();
                    newAC.put("contactId", saveC.getContactId());
                    newAC.put("contactName", saveC.getContactName());
                    newAC.put("status", saveC.getStatus());
                    newAC.put("desription", saveC.getDescription());
                    String newValue = mapper.writeValueAsString(newAC);
                    auditTrail.setNewValue(newValue);
                    auditTrail.setOperation(FlowStatus.ACTIVE.name());
                    auditTrailRepo.save(auditTrail);
                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            ResponseUtils.MESSAGE_ACTIVE, newAC);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        "Contact don't have is active status", ResponseUtils.DATA_EMPTY);
                logger.info("Response Success ->" + result);
                return new ResponseEntity<>(result, result.getHttpCode());
                
            } else {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Contact with id " + request.getId() + " is not found.", ResponseUtils.DATA_EMPTY);
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
    
    public ResponseEntity<ResponseObject> pagingContact(MaterialTablePagingRequest pagingRequest,
                                                        PagedResourcesAssembler<VW_CONTACT> assembler) {
        ResponseObject result = new ResponseObject();
        logger.info("Get List Contact");
        try {
            Map<String, Object> filter = new HashMap<>();
            if (isNotBlank(pagingRequest.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingRequest.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingRequest.getSearch().add(key+"~"+value);
                }
            }
            Specification<VW_CONTACT> specification = pagingRequest.getSearch().isEmpty() ?
                    vwContactRepo.getSpecificationDefault(filter)
                    :
                    vwContactRepo.getSpecificationFromFilters(pagingRequest, filter);

            Page<VW_CONTACT> dataa = vwContactRepo.findAll(specification,PagingUtils.getPaging(pagingRequest));
            List<LinkedHashMap<String, Object>> datas = new LinkedList<>();

            for(VW_CONTACT dataContact : dataa.getContent())
            {
                LinkedHashMap<String, Object> data = new LinkedHashMap<>();
                data.put("contactId", dataContact.getId());
                data.put("firstName", dataContact.getFirstName());
                data.put("middleName", dataContact.getMiddleName());
                data.put("lastName", dataContact.getLastName());
                data.put("contactName", dataContact.getContactName());
                data.put("jobName", dataContact.getJobName());
                data.put("positionName", dataContact.getPositionName());

                List<LinkedHashMap<String, Object>> allDetail = new ArrayList<>();
                List<M_CONTACT_DETAILS> allCDet = cdRepo.findAllByContactIdOrderByContactDetailsIdAsc(dataContact.getId());
                for (M_CONTACT_DETAILS dq : allCDet) {
                    LinkedHashMap<String, Object> dDetail = new LinkedHashMap<>();
                    Optional<R_GLOBAL_TYPE_VALUE> ctype = rGlobalTypeValueRepo.findById(dq.getType());
                    Optional<R_GLOBAL_TYPE_VALUE> itype = rGlobalTypeValueRepo.findById(dq.getInputType());
                    dDetail.put("contactId", dq.getContactId());
                    dDetail.put("type", ctype.get().getName());
                    dDetail.put("inputType", itype.get().getName());
                    dDetail.put("fullValue", dq.getFullValue());
                    dDetail.put("contactValue", dq.getContactValue());
                    allDetail.add(dDetail);
                }

                data.put("contactDetail", allDetail);

                data.put("status", dataContact.getStatus());
                data.put("createdDate", dataContact.getCreatedDate());
                data.put("createdBy", dataContact.getCreatedBy());
                data.put("updatedDate", dataContact.getUpdatedDate());
                data.put("updatedBy", dataContact.getUpdatedBy());

                datas.add(data);
            }

            PagedModel<EntityModel<VW_CONTACT>> pagedData = assembler.toModel(dataa);
            Map<String, Object> d = new HashMap<>();
            d.put("result", datas);
            d.put("page", pagedData.getMetadata());
            d.put("links", pagedData.getLinks());

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, "Success get data", d);
            logger.info("Response Success ->" + result);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<InputStreamResource> downloadFilter(MaterialTablePagingRequest pagingData) {
        try {
            Page<VW_CONTACT> data;
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
                long maxData = StreamSupport.stream(vwContactRepo.findAll().spliterator(), false).count();
                pagingData.setSize((int) maxData);
                pagingData.setPage(1);
            }
            if (!pagingData.getSearch().isEmpty()) {
                data = this.vwContactRepo.findAll(this.vwContactRepo.getSpecificationFromFilters(pagingData, filter),
                        PagingUtils.getPaging(pagingData));
            } else {
                data = this.vwContactRepo.findAll(this.vwContactRepo.getSpecificationDefault(filter),
                        PagingUtils.getPaging(pagingData));
            }
            List<VW_CONTACT> listAction = data.getContent();
            List<LinkedHashMap<String, Object>> allData = new ArrayList<>();
            Integer no = 1;
            HttpHeaders headers = new HttpHeaders();
            if(data.hasContent()) {
                for (VW_CONTACT a : listAction) {
                    List<M_CONTACT_DETAILS> contactDetails = cdRepo.findAllByContactId(a.getId());
                    for (M_CONTACT_DETAILS detail : contactDetails) {
                        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                        response.put("NO", no);
                        response.put("CONTACT NAME", a.getContactName());
                        response.put("JOB", a.getJobName());
                        response.put("POSITION", a.getPositionName());
                        response.put("STATUS", capitalizeFully(a.getStatus()));
                        Optional<R_GLOBAL_TYPE_VALUE> inputTypeValue = rGlobalTypeValueRepo.findById(detail.getInputType());
                        response.put("CONTACT TYPE", inputTypeValue.isPresent()?inputTypeValue.get().getName():null);
                        response.put("CONTACT VALUE", detail.getContactValue());
                        allData.add(response);
                        no++;
                    }
                }
                ByteArrayInputStream in = DownloadToExcel.downloadsFiles(allData, "Excel");
                headers.add(Constant.CONTENT_DISPOSITION, "attachment; filename=" +
                        "CONTACT_" + UtilsDate.dateToString(new Date(), "yyyyMMddHHmmss") + ".xlsx");
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
    
    public ResponseEntity<ResponseObject> getDetail(Integer contactId) {
        ResponseObject result;
        logger.info("Get Contact Detail");
        try {
            Optional<VW_CONTACT> dataContact = vwContactRepo.findById(contactId);
            if(dataContact.isPresent()) {
                ViewContactDTO data = this.getResponseContact(dataContact.get());
                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, UtilsAccount.messageSuccess(ConstantAccount.DETAIL, ConstantAccount.MASTER_CONTACT), data);
            } else {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND, UtilsAccount.messageDataNotFound(ConstantAccount.MASTER_CONTACT, contactId), ResponseUtils.DATA_EMPTY);
            }

                //acr
//                List<AUDIT_TRAIL> auditTrail = auditTrailRepo.findAll().stream()
//                        .filter(e -> e.getTableName().equalsIgnoreCase("M_CONTACT"))
//                        .filter(f -> f.getDataId().equalsIgnoreCase(d.getContactId().toString()))
//                        .collect(Collectors.toList());
//                data.put("activeInactiveLog", auditTrail);


            return new ResponseEntity<>(result, HttpStatus.OK);
                        
		} catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}

    public ResponseEntity<ResponseObject> getFromGlobalType(String groupname){
        ResponseObject result = new ResponseObject();

        try {

            List<R_GLOBAL_TYPE_VALUE> data = globalTypeService.getDetailGlobalType(groupname);
            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success Get List Service Type");
            result.setData(data);

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getChooseContactCustom(String type, @Nullable String bankCode, @Nullable Integer customerId, MaterialTablePagingRequest pagingData,
                                                           PagedResourcesAssembler<VW_CHOOSE_CONTACT> assembler) {
        logger.info("Get List Choose Contact");
        try {
            Map<String, Object> filter = new HashMap<>();

            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key+"~"+value);
                }
            }

            List<Integer> idContact = new ArrayList<>();
            if(bankCode!=null){
                List<R_PAY_BANK_CONTACT> optContact = bankContactRepo.findAllByBankCode(bankCode);
                for(R_PAY_BANK_CONTACT ctc : optContact) {
                    idContact.add(ctc.getContact().getContactId());
                }
            } else if(customerId!=null) {
                List<VW_CUSTOMER_CONTACT> optContact = vwCustomerContactRepo.findAllByCustomerId(customerId);
                if(!optContact.isEmpty()){
                    for(VW_CUSTOMER_CONTACT ctc : optContact) {
                        idContact.add(ctc.getContactId());
                    }
                }
            }
            // delete duplicate id contact
            Set<Integer> uniqueIds = new HashSet<>(idContact);
            idContact.clear();
            idContact.addAll(uniqueIds);

            Page<VW_CHOOSE_CONTACT> data = chooseContactRepoCustom.findByContactIdIn(type, vwChooseContactRepo.getSpecificationFromFilters(pagingData, filter), pagingData, PagingUtils.getPaging(pagingData), idContact, VW_CHOOSE_CONTACT.class);

            List<LinkedHashMap<String, Object>> allData = this.getResponseChoose(data.getContent());

            PagedModel<EntityModel<VW_CHOOSE_CONTACT>> pagedData = assembler.toModel(data);
            Map<String, Object> d = new HashMap<>();
            d.put("result", allData);
            d.put("page", pagedData.getMetadata());
            d.put("links", pagedData.getLinks());
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.PAGING, ConstantAccount.MASTER_CONTACT), d), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<LinkedHashMap<String, Object>> getResponseChoose(List<VW_CHOOSE_CONTACT> dataList) {
        return dataList.stream()
                .map(g -> {
                    LinkedHashMap<String, Object> acontact = new LinkedHashMap<>();
                    acontact.put("id", g.getContactId());
                    acontact.put("contactId", g.getContactId());
                    acontact.put("firstName", g.getFirstName());
                    acontact.put("middleName", g.getMiddleName());
                    acontact.put("lastName", g.getLastName());
                    acontact.put("contactName", g.getContactName());
                    acontact.put("jobId", g.getJobId());
                    acontact.put("jobName", g.getJobName());
                    acontact.put("positionId", g.getPositionId());
                    acontact.put("positionName", g.getPositionName());
                    acontact.put("source", g.getSource());

                    List<M_CONTACT_DETAILS> allCDet = cdRepo.findAllByContactIdOrderByContactDetailsIdAsc(g.getContactId());

                    List<LinkedHashMap<String, Object>> allContactDetail = allCDet.stream()
                            .map(d -> {
                                LinkedHashMap<String, Object> dd = new LinkedHashMap<>();
                                dd.put("contactDetailsId", d.getContactDetailsId());
                                dd.put("contactId", d.getContactId());
                                dd.put("type", d.getType());
                                Optional<R_GLOBAL_TYPE_VALUE> tn = rGlobalTypeValueRepo.findByGlbTypeValId(d.getType());
                                dd.put("typeName", tn.get().getName());
                                dd.put("inputType", d.getInputType());
                                Optional<R_GLOBAL_TYPE_VALUE> it = rGlobalTypeValueRepo.findByGlbTypeValId(d.getInputType());
                                dd.put("inputTypeName", it.get().getName());
                                dd.put("fullValue", d.getFullValue());
                                return dd;
                            })
                            .collect(Collectors.toList());
                    acontact.put("contactDetails", allContactDetail);
                    acontact.put("status", g.getStatus());
                    return acontact;
                })
                .collect(Collectors.toList());
    }

    public ViewContactDTO getResponseContact (VW_CONTACT data) {

        ViewContactDTO resp = objectMapper.convertValue(data, ViewContactDTO.class);

        List<M_CONTACT_DETAILS> allContactDetail = cdRepo.findAllByContactIdOrderByContactDetailsIdAsc(data.getId());
        List<ViewContactDetailsDTO> respDetail = new ArrayList<>();
        for(M_CONTACT_DETAILS detail : allContactDetail) {
            ViewContactDetailsDTO dataDetail = new ViewContactDetailsDTO();
            dataDetail.setContactDetailId(detail.getContactDetailsId());
            dataDetail.setContactId(detail.getContactId());
            dataDetail.setFullValue(detail.getFullValue());
            dataDetail.setContactValue(detail.getContactValue());
            R_GLOBAL_TYPE_VALUE gtType = rGlobalTypeValueRepo.findTopByGlbTypeValIdAndIsDeleted(detail.getType(), Boolean.FALSE);
            dataDetail.setType(UtilsAccount.getLabelValue(gtType));
            R_GLOBAL_TYPE_VALUE gtInputType = rGlobalTypeValueRepo.findTopByGlbTypeValIdAndIsDeleted(detail.getInputType(), Boolean.FALSE);
            dataDetail.setInputType(UtilsAccount.getLabelValue(gtInputType));
            ViewValueContactDetailsDTO respValue = new ViewValueContactDetailsDTO();
            R_GLOBAL_TYPE_VALUE gtPrefix1 = detail.getPrefix1() !=null ? rGlobalTypeValueRepo.findTopByGlbTypeValIdAndIsDeleted(detail.getPrefix1(), Boolean.FALSE) : null;
            respValue.setPrefix1(UtilsAccount.getLabelValue(gtPrefix1));
            R_GLOBAL_TYPE_VALUE gtPrefix2 = detail.getPrefix2() !=null ? rGlobalTypeValueRepo.findTopByGlbTypeValIdAndIsDeleted(detail.getPrefix2(), Boolean.FALSE) : null;
            respValue.setPrefix2(UtilsAccount.getLabelValue(gtPrefix2));
            respValue.setValue(detail.getValue());
            respValue.setSufix(detail.getSufix());
            dataDetail.setValueDetail(respValue);
            respDetail.add(dataDetail);
            resp.setContactDetail(respDetail);
        }
        return resp;
    }

}
