package com.dbs.module.account.main.services;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.FlowStatus;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.database.crm.entities.accountmanagement.*;
import com.dbs.database.crm.entities.usermanagement.*;
import com.dbs.database.crm.repositories.accountmanagement.Account.*;
import com.dbs.database.crm.repositories.accountmanagement.MAccountingRulesRepo;
import com.dbs.database.crm.repositories.accountmanagement.VwChooseAddressRepo;
import com.dbs.database.crm.repositories.accountmanagement.VwChooseContactRepo;
import com.dbs.database.crm.repositories.mastermanagement.MLocationsRepo;
import com.dbs.database.crm.repositories.usermanagement.*;
import com.dbs.module.account.main.dto.AccountDTO;
import com.dbs.module.account.main.dto.MLocationDetailDTO;
import com.dbs.module.account.main.dto.PositionNameDTO;
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

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class AccountService {
    
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    // Repositories
    private final MAccountRepo accountRepo;
    @Autowired
    private MGlobalTypeRepo mGlobalTypeRepo;
    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;
    @Autowired
    private MAccountingRulesRepo mAccountingRulesRepo;
    @Autowired
    private MMeterReadingCodesRepo mMeterReadingCodesRepo;
    @Autowired
    private MPositionRepo mPositionRepo;
    @Autowired
    private MAccountAddressRepo accountAddressRepo;
    @Autowired
    private MaddressRepo mAddressRepo;
    @Autowired
    private MLocationsRepo mLocationRepo;
    @Autowired
    private MContactRepo cRepo;
    @Autowired
    private MContactDetailsRepo cdRepo;
    @Autowired
    private MAccountContactRepo acRepo;
    @Autowired
    private VwCustomerContactRepo vcRepo;
    @Autowired
    private VwCustomerContactDtlRepo vcdRepo;
    @Autowired
    private VWCustomerAddressRepo vwAddressRepo;
    @Autowired
    private VwChooseContactRepo vwChooseContactRepo;

    @Autowired
    private VwChooseAddressRepo vwChooseAddressRepo;

    @Autowired
    private MUserRepo userRepo;

    @Autowired
    private GlobalTypeValueService globalTypeService;

    @Autowired
    private ChooseAddressRepoCustom chooseAddressRepoCustom;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuditTrailRepo auditTrailRepo;
    public AccountService(MAccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    public List<M_ACCOUNT> getAllAccount() { 

        return accountRepo.findAll();
    }

    public ResponseEntity<M_ACCOUNT> getAccountById(Integer id) {
        Optional<M_ACCOUNT> account = accountRepo.findById(id);
        if (account.isPresent()) {
            return ResponseEntity.ok(account.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<ResponseObject> createAccount(M_ACCOUNT account) {

        ResponseObject result;
        Date newDate = new Date();

        M_ACCOUNT acc = new M_ACCOUNT();
        acc.setCreatedBy(UserDetailUtils.getUserId());
        acc.setStatus("Inactive");
        acc.setCreatedDate(newDate);

        acc.setCustomerId(account.getCustomerId());
        acc.setRegistrationNumber(account.getRegistrationNumber());
        acc.setAccountNumber(account.getAccountNumber());
        acc.setAccountName(account.getAccountName());
        acc.setAccountSegment(account.getAccountSegment());
        acc.setAccountGroupType(account.getAccountGroupType());
        acc.setAccountCategory(account.getAccountCategory());
        acc.setAccountRuleId(account.getAccountRuleId());
        acc.setAccountType(account.getAccountType());
        acc.setPaymentChannel(account.getPaymentChannel());
        acc.setDescription(account.getDescription());
        acc.setSor(account.getSor());
        acc.setCostCenter(account.getCostCenter());
        acc.setMeterReadingCode(account.getMeterReadingCode());
        acc.setBudgetYear(account.getBudgetYear());
        acc.setBudget(account.getBudget());
        acc.setTeritory(account.getTeritory());
        acc.setIsCorporate(account.getIsCorporate());
        acc.setIsBadDebt(account.getIsBadDebt());
        acc.setSyncFlag(account.getSyncFlag());
        acc.setAccountReferenceId(account.getAccountReferenceId());
        acc.setEntityId(account.getEntityId());
        accountRepo.save(acc);

        LinkedHashMap<String, Object> successTosResponse = new LinkedHashMap<>();
            successTosResponse.put("Account id: ", acc.getAccountId());

        result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                    "Success create Account", acc);
        return new ResponseEntity<>(result, result.getHttpCode());
    }
    
    public ResponseEntity<ResponseObject> updatePaymentChannel(AccountDTO request) {
        ResponseObject result;
        try {
            Optional<M_ACCOUNT> data = accountRepo.findByAccountId(request.getAccountId());
            if(data.isPresent()) {
                M_ACCOUNT wt = data.get();

                //acr
                AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
                ObjectMapper mapper = new ObjectMapper();
                String oldValue = mapper.writeValueAsString(wt);
                auditTrail.setOldValue(oldValue);

                wt.setPaymentChannel(request.getPaymentChannel());
                wt.setUpdatedBy(UserDetailUtils.getUsername() != null ? UserDetailUtils.getUsername() : null);
                wt.setUpdatedDate(new Date());
                M_ACCOUNT saveWT = accountRepo.save(wt);

                //acr
                auditTrail.setOperation("MODIFY_PAYMENT_CHANNEL");
                auditTrail.setTableName("M_ACCOUNT");
                auditTrail.setDataId(wt.getAccountId().toString());
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
                String newValue = mapper.writeValueAsString(saveWT);
                auditTrail.setNewValue(newValue);
                auditTrailRepo.save(auditTrail);

                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        ResponseUtils.MESSAGE_UPDATED, saveWT);
                return new ResponseEntity<>(result, result.getHttpCode());
            } else {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Account with id " + request.getAccountId() + " is not found.", ResponseUtils.DATA_EMPTY);
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
    
    public ResponseEntity<ResponseObject> getPaymentChannel(Integer accountId) {
        ResponseObject result;
        try {
            Optional<M_ACCOUNT> data = accountRepo.findByAccountId(accountId);
            if(data.isPresent()) {
                M_ACCOUNT wt = data.get();
                LinkedHashMap<String, Object> dataAC = new LinkedHashMap<>();
                if(wt.getPaymentChannel() != null) {
                    Optional<R_GLOBAL_TYPE_VALUE> ptype = rGlobalTypeValueRepo.findById(wt.getPaymentChannel());
                    dataAC.put("paymentChannel", wt.getPaymentChannel());
                    dataAC.put("paymentChannelValue", ptype.get().getName());

                    //acr
                    List<AUDIT_TRAIL> auditTrail = auditTrailRepo.findTopByTableNameOrderByCreatedDateDesc("M_ACCOUNT").stream()
                            .filter(f -> f.getDataId().equalsIgnoreCase(wt.getAccountId().toString()))
                            .collect(Collectors.toList());
                    dataAC.put("historyLog", auditTrail);

                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            ResponseUtils.MESSAGE_OK, dataAC);
                    return new ResponseEntity<>(result, result.getHttpCode());
                } else {
                    dataAC.put("paymentChannel", null);
                    dataAC.put("paymentChannelValue", null);
                    dataAC.put("historyLog", null);
                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            ResponseUtils.MESSAGE_OK, dataAC);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
            } else {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Account with id " + accountId + " is not found.", ResponseUtils.DATA_EMPTY);
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
    public ResponseEntity<ResponseObject> getPaymentChannelType() {
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Account Payment Channel", FlowStatus.ACTIVE.name(), false);
            logger.info("Payment Channel Type " + auths);
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
    
    public ResponseEntity<ResponseObject> getBudgetYears() {
        logger.info("Find Budget Years");
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Account Budget Year",
                    FlowStatus.ACTIVE.name(), false);
            logger.info("Account Budget Year" + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;

            if (!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name())).
                        sorted((o1, o2)->o1.getName().
                                compareTo(o2.getName()))
                        .collect(Collectors.toList());
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if (!areas.isEmpty()) {
                    for (R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        ar.put("id", rgtv.getGlbTypeValId());
                        ar.put("name", rgtv.getName());
                        areaList.add(ar);
                    }
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Success get Account Budget Year");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Failed get Account Budget Year");
                    result.setData(ResponseUtils.DATA_EMPTY);
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Failed get Account Budget Year");
                result.setData(ResponseUtils.DATA_EMPTY);
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
    public ResponseEntity<ResponseObject> getIndustrialSectors() {
        logger.info("Find Industrial Sector");
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Industrial Sector",
                    FlowStatus.ACTIVE.name(), false);
            logger.info("Industrial Sector" + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;

            if (!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name())).filter( b -> b.getParentValue().equals(18))
                        .collect(Collectors.toList());
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if (!areas.isEmpty()) {
                    for (R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        ar.put("value", rgtv.getGlbTypeValId());
                        ar.put("title", rgtv.getGlbValue() + " - " + rgtv.getName());
                        List<R_GLOBAL_TYPE_VALUE> listOne = rGlobalTypeValueRepo.findAllByParentValueAndIsDeleted(rgtv.getGlbTypeValId(), false);
                        if(!listOne.isEmpty()) {
                            List<LinkedHashMap<String, Object>> adw = new ArrayList<>();
                                for(R_GLOBAL_TYPE_VALUE rgtv2 : listOne) {
                                    LinkedHashMap<String, Object> arOne = new LinkedHashMap<>();
                                    arOne.put("value", rgtv2.getGlbTypeValId());
                                    arOne.put("title", rgtv2.getGlbValue() + " - " + rgtv2.getName());
                                    List<R_GLOBAL_TYPE_VALUE> listTwo = rGlobalTypeValueRepo.findAllByParentValueAndIsDeleted(rgtv2.getGlbTypeValId(), false);
                                    if(!listTwo.isEmpty()) {
                                        List<LinkedHashMap<String, Object>> adw2 = new ArrayList<>();
                                            for(R_GLOBAL_TYPE_VALUE rgtv3 : listTwo) {
                                                LinkedHashMap<String, Object> arTwo= new LinkedHashMap<>();
                                                arTwo.put("value", rgtv3.getGlbTypeValId());
                                                arTwo.put("title", rgtv3.getGlbValue() + " - " + rgtv3.getName());
                                                adw2.add(arTwo);
                                            }
                                        arOne.put("children", adw2);
                                    }
                                    adw.add(arOne);
                                }
                            ar.put("children", adw);
                        }
                        areaList.add(ar);
                    }
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Success get Industrial Sector");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Failed get Industrial Sector");
                    result.setData(ResponseUtils.DATA_EMPTY);
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Failed get Industrial Sector");
                result.setData(ResponseUtils.DATA_EMPTY);
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
    public ResponseEntity<ResponseObject> getAccountBudgets() {
        logger.info("Find Account Budget");
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Account Budget",
                    FlowStatus.ACTIVE.name(), false);
            logger.info("Account Budget" + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;

            if (!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name()))
                        .collect(Collectors.toList());
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if (!areas.isEmpty()) {
                    for (R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        ar.put("id", rgtv.getGlbTypeValId());
                        ar.put("name", rgtv.getName());
                        areaList.add(ar);
                    }
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Success get Account Budget");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Failed get Account Budget");
                    result.setData(ResponseUtils.DATA_EMPTY);
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Failed get Account Budget");
                result.setData(ResponseUtils.DATA_EMPTY);
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
    public ResponseEntity<ResponseObject> getAccountCategories() {
        logger.info("Find Account Category");
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Account Category",
                    FlowStatus.ACTIVE.name(), false);
            logger.info("Account Category" + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;

            if (!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name()))
                        .collect(Collectors.toList());
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if (!areas.isEmpty()) {
                    for (R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        ar.put("id", rgtv.getGlbTypeValId());
                        ar.put("name", rgtv.getName());
                        areaList.add(ar);
                    }
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Success get Account Category");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Failed get Account Category");
                    result.setData(ResponseUtils.DATA_EMPTY);
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Failed get Account Category");
                result.setData(ResponseUtils.DATA_EMPTY);
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
    public ResponseEntity<ResponseObject> getAccountTeritories() {
        logger.info("Find Account Teritory");
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Account Teritory",
                    FlowStatus.ACTIVE.name(), false);
            logger.info("Account Teritory" + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;

            if (!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name()))
                        .collect(Collectors.toList());
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if (!areas.isEmpty()) {
                    for (R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        ar.put("id", rgtv.getGlbTypeValId());
                        ar.put("name", rgtv.getName());
                        areaList.add(ar);
                    }
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Success get Account Teritory");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Failed get Account Teritory");
                    result.setData(ResponseUtils.DATA_EMPTY);
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Failed get Account Teritory");
                result.setData(ResponseUtils.DATA_EMPTY);
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
    public ResponseEntity<ResponseObject> getAccountSegments() {
        logger.info("Find Account Segment");
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Account Segment",
                    FlowStatus.ACTIVE.name(), false);
            logger.info("Account Segment" + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;

            if (!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name()))
                        .collect(Collectors.toList());
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if (!areas.isEmpty()) {
                    for (R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        ar.put("id", rgtv.getGlbTypeValId());
                        ar.put("name", rgtv.getName());
                        areaList.add(ar);
                    }
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Success get Account Segment");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Failed get Account Segment");
                    result.setData(ResponseUtils.DATA_EMPTY);
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Failed get Account Segment");
                result.setData(ResponseUtils.DATA_EMPTY);
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
    public ResponseEntity<ResponseObject> getMartialStatus() {
        logger.info("Find Martial Status");
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Martial Status",
                    FlowStatus.ACTIVE.name(), false);
            logger.info("Martial Status" + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;

            if (!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name()))
                        .collect(Collectors.toList());
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if (!areas.isEmpty()) {
                    for (R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        ar.put("id", rgtv.getGlbTypeValId());
                        ar.put("name", rgtv.getName());
                        areaList.add(ar);
                    }
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Success get Martial Status");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Failed get Martial Status");
                    result.setData(ResponseUtils.DATA_EMPTY);
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Failed get Martial Status");
                result.setData(ResponseUtils.DATA_EMPTY);
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
    public ResponseEntity<ResponseObject> getSex() {
        logger.info("Find Sex");
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Sex",
                    FlowStatus.ACTIVE.name(), false);
            logger.info("Sex" + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;

            if (!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name()))
                        .collect(Collectors.toList());
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if (!areas.isEmpty()) {
                    for (R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        ar.put("id", rgtv.getGlbTypeValId());
                        ar.put("name", rgtv.getName());
                        areaList.add(ar);
                    }
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Success get Sex");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Failed get Sex");
                    result.setData(ResponseUtils.DATA_EMPTY);
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Failed get Sex");
                result.setData(ResponseUtils.DATA_EMPTY);
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
    public ResponseEntity<ResponseObject> getAccountGroups() {
        logger.info("Find Account Group");
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Account Group",
                    FlowStatus.ACTIVE.name(), false);
            logger.info("Account Group" + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;

            if (!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name()))
                        .collect(Collectors.toList());
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if (!areas.isEmpty()) {
                    for (R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        ar.put("id", rgtv.getGlbTypeValId());
                        ar.put("name", rgtv.getName());
                        areaList.add(ar);
                    }
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Success get Account Group");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Failed get Account Group");
                    result.setData(ResponseUtils.DATA_EMPTY);
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Failed get Account Group");
                result.setData(ResponseUtils.DATA_EMPTY);
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
    public ResponseEntity<ResponseObject> getAccountTypes() {
        logger.info("Find Account Types");
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Account Type",
                    FlowStatus.ACTIVE.name(), false);
            logger.info("Account Types" + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;

            if (!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name()))
                        .collect(Collectors.toList());
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if (!areas.isEmpty()) {
                    for (R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        ar.put("id", rgtv.getGlbTypeValId());
                        ar.put("name", rgtv.getName());
                        areaList.add(ar);
                    }
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Success get Account Types");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Failed get Account Types");
                    result.setData(ResponseUtils.DATA_EMPTY);
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Failed get Account Types");
                result.setData(ResponseUtils.DATA_EMPTY);
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
    public ResponseEntity<ResponseObject> getAccountGroupType(Integer idSegment) {
        logger.info("Find Account Group Type");
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Account Group Type",
                    FlowStatus.ACTIVE.name(), false);
            logger.info("Account Group Type" + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = rGlobalTypeValueRepo.findByParentValueAndIsDeleted(idSegment,false);
            List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
            for (R_GLOBAL_TYPE_VALUE rgtv : areas) {
                LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                ar.put("id", rgtv.getGlbTypeValId());
                ar.put("name", rgtv.getName());
                areaList.add(ar);
            }
            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success get Account Group Type");
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
    public ResponseEntity<ResponseObject> getAccountPriorities() {
        logger.info("Find Account Priority");
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Account Priority",
                    FlowStatus.ACTIVE.name(), false);
            logger.info("Account Priority" + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;

            if (!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name()))
                        .collect(Collectors.toList());
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if (!areas.isEmpty()) {
                    for (R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        ar.put("id", rgtv.getGlbTypeValId());
                        ar.put("name", rgtv.getName());
                        areaList.add(ar);
                    }
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Success get Account Priority");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Failed get Account Priority");
                    result.setData(ResponseUtils.DATA_EMPTY);
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Failed get Account Priority");
                result.setData(ResponseUtils.DATA_EMPTY);
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
    public ResponseEntity<ResponseObject> getMeterReadingCodes(HttpServletRequest request) {
        logger.info("Find Meter Reading Codes");
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_POSITION> mPosition = mPositionRepo.findByPositionIdAndStatusAndIsDeleted(UserDetailUtils.getPositionFromToken(request),FlowStatus.ACTIVE.name(),false);
            if (mPosition.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Failed to get position from token",
                        ResponseUtils.DATA_EMPTY);
            }else {
                M_POSITION position = mPosition.get();
                Integer ccId = position.getCcId().getCcId();
                List<M_METER_READING_CODES> meterReadingCodeList = mMeterReadingCodesRepo.findAllByStatusAndCostCenterId(FlowStatus.ACTIVE.name(),ccId);
                logger.info("Meter Codes" + meterReadingCodeList);
                List<LinkedHashMap<String, Object>> meterReadingCodes = new LinkedList<>();
                if (meterReadingCodeList != null) {
                    for (M_METER_READING_CODES mmrc : meterReadingCodeList) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        ar.put("id", mmrc.getMeterReadingCodeId());
                        ar.put("name", mmrc.getCode() + " - " + (mmrc.getDescription()!=null? mmrc.getDescription() : ""));
                        meterReadingCodes.add(ar);
                    }
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Success get Meter Reading Code");
                    result.setData(meterReadingCodes);
                } else {
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Data is empty");
                    result.setData(ResponseUtils.DATA_EMPTY);
                }
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
    public ResponseEntity<ResponseObject> getClassificationTypes() {
        logger.info("Find Classification Types");
        ResponseObject result = new ResponseObject();
        try {
            List<M_ACCOUNTING_RULE> accountingRuleList = mAccountingRulesRepo.findAllByStatus(FlowStatus.ACTIVE.name());
            logger.info("Classification Types" + accountingRuleList);
            List<LinkedHashMap<String, Object>> accountingRules = new LinkedList<>();
            if (accountingRuleList != null) {
                for (M_ACCOUNTING_RULE mar : accountingRuleList) {
                    LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                    ar.put("id", mar.getMasterAccountingRuleId());
                    ar.put("name", mar.getClassificationTypeName());
                    accountingRules.add(ar);
                }
                result.setSuccess(true);
                result.setCode(HttpStatus.OK);
                result.setMessage("Success get Classification Types");
                result.setData(accountingRules);
            } else {
                result.setSuccess(true);
                result.setCode(HttpStatus.OK);
                result.setMessage("Data is empty");
                result.setData(ResponseUtils.DATA_EMPTY);
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
    public ResponseEntity<ResponseObject> getPositionName(HttpServletRequest request){
        ResponseObject result = new ResponseObject();
        Optional<M_POSITION> mPosition = mPositionRepo.findByPositionIdAndStatusAndIsDeleted(UserDetailUtils.getPositionFromToken(request),FlowStatus.ACTIVE.name(),false);
        if (mPosition.isEmpty()) {
            result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    "Failed to get position from token",
                    ResponseUtils.DATA_EMPTY);
        }else{
            PositionNameDTO positionNameDTO = new PositionNameDTO();
            M_POSITION position = mPosition.get();
            positionNameDTO.setCustomerManagement(position.getName());
            positionNameDTO.setCc(position.getCcId().getCode() + " - " + position.getCcId().getName());
            positionNameDTO.setSor(position.getCcId().getValCode() + " - " + position.getCcId().getValName());

            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success get Position Name");
            result.setData(positionNameDTO);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    public ResponseEntity<ResponseObject> getBusinessPurposeList() {
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Business Purpose",
                    FlowStatus.ACTIVE.name(), false);
            logger.info("List Business Purpose" + auths);
            if (!auths.isEmpty()) {
                List<R_GLOBAL_TYPE_VALUE> areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name()))
                        .collect(Collectors.toList());
                List<MLocationDetailDTO> listType = new ArrayList<>();
                if (!areas.isEmpty()) {
                    int index = 0;
                    for (R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        MLocationDetailDTO locDTO = new MLocationDetailDTO();
                        locDTO.setId(rgtv.getGlbTypeValId());
                        locDTO.setName(rgtv.getName());
                        listType.add(index++, locDTO);
                    }
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Success Get List Business Purpose");
                    result.setData(listType);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Failed Get List Business Purpose");
                    result.setData(ResponseUtils.DATA_EMPTY);
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Failed Get List Business Purpose");
                result.setData(ResponseUtils.DATA_EMPTY);
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
    
    public ResponseEntity<ResponseObject> getCountryCode() {
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Contact Country Code Phone", FlowStatus.ACTIVE.name(), false);
            logger.info("Contact Country Code Phone " + auths);
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
                        ar.put("name", rgtv.getName());
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
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Contact Zone Code Phone", FlowStatus.ACTIVE.name(), false);
            logger.info("Contact Zone Code Phone " + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = rGlobalTypeValueRepo.findByParentValueAndIsDeleted(idCountry,false);
            List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
            for (R_GLOBAL_TYPE_VALUE rgtv : areas) {
                LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                ar.put("id", rgtv.getGlbTypeValId());
                ar.put("code", rgtv.getGlbValue());
                ar.put("name", rgtv.getName());
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
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Contact Job", FlowStatus.ACTIVE.name(), false);
            logger.info("Contact Job " + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;

            if(!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name()))
                        .collect(Collectors.toList());
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if(!areas.isEmpty()) {
                    for(R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        ar.put("id", rgtv.getGlbTypeValId());
                        ar.put("name", rgtv.getName());
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
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Contact Position", FlowStatus.ACTIVE.name(), false);
            logger.info("Contact Position " + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;

            if(!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name()))
                        .collect(Collectors.toList());
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if(!areas.isEmpty()) {
                    for(R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        ar.put("id", rgtv.getGlbTypeValId());
                        ar.put("name", rgtv.getName());
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
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Input Type", FlowStatus.ACTIVE.name(), false);
            logger.info("Input Type " + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;

            if(!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name()))
                        .collect(Collectors.toList());
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if(!areas.isEmpty()) {
                    for(R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        ar.put("id", rgtv.getGlbTypeValId());
                        ar.put("name", rgtv.getName());
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
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Contact Type", FlowStatus.ACTIVE.name(), false);
            logger.info("Contact Type " + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;

            if(!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name()))
                        .collect(Collectors.toList());
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if(!areas.isEmpty()) {
                    for(R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        ar.put("id", rgtv.getGlbTypeValId());
                        ar.put("name", rgtv.getName());
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
    
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> getListChooseAccountAddress(String type, Integer customerId, MaterialTablePagingRequest pagingData,
			PagedResourcesAssembler<VW_CHOOSE_ADDRESS> assembler) {
            logger.info("Get List Address");
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

                List<Integer> idAddress = new ArrayList<>();
                if(customerId!=null) {
                    List<VW_CUSTOMER_ADDDRESS> optAddress = vwAddressRepo.findAllByCustomerId(customerId);
                    if(!optAddress.isEmpty()){
                        for(VW_CUSTOMER_ADDDRESS ctc : optAddress) {
                            idAddress.add(ctc.getAddressId());
                        }
                        // delete duplicate id address
                        Set<Integer> uniqueIds = new HashSet<>(idAddress);
                        idAddress.clear();
                        idAddress.addAll(uniqueIds);
                    }
                }

                // delete duplicate id address
                Set<Integer> uniqueIds = new HashSet<>(idAddress);
                idAddress.clear();
                idAddress.addAll(uniqueIds);

                Page<VW_CHOOSE_ADDRESS> data = chooseAddressRepoCustom.findByAddressIdIn(type, vwChooseAddressRepo.getSpecificationFromFilters(pagingData, filter), pagingData, PagingUtils.getPaging(pagingData), idAddress, VW_CHOOSE_ADDRESS.class);

                List<LinkedHashMap<String, Object>> allData = data.stream()
                .map(g -> {
                    LinkedHashMap<String, Object> acontact = new LinkedHashMap<>();
//                        acontact.put("accountId", g.getAccountId()); testing
                    acontact.put("addressId", g.getAddressId());
                    acontact.put("fullAddress", g.getFullAddress());
                    acontact.put("description", g.getDescription());
                    acontact.put("additionalNote", g.getAdditionalInfo());
                    acontact.put("houseName", g.getHouseName());
                    acontact.put("streetName", g.getStreetName());
                    acontact.put("streetNumber", g.getStreetNumber());
                    acontact.put("houseNumber", g.getHouseNumber());
                    acontact.put("rt", g.getNeighborhood1());
                    acontact.put("rw", g.getNeighborhood2());
                    acontact.put("building", g.getBuilding());
                    acontact.put("floor", g.getFloor());
                    acontact.put("postalCode", g.getPostalCode());
                    acontact.put("subDistrict", g.getSubDistrict());
                    acontact.put("city", g.getCity());
                    acontact.put("province", g.getProvince());
                    acontact.put("country", g.getCountry());
                    acontact.put("district", g.getDistrict());
                    acontact.put("type", g.getType());
                    acontact.put("typeId", g.getTypeId());
                    acontact.put("postalCodeId", g.getPostalCodeId());
                    acontact.put("subDistrictId", g.getSubDistrictId());
                    acontact.put("districtId", g.getDistrictId());
                    acontact.put("cityId", g.getCityId());
                    acontact.put("provinceId", g.getProvinceId());
                    acontact.put("countryId", g.getCountryId());
                    acontact.put("source", g.getSource());
                    return acontact;
                })
                .collect(Collectors.toList());

            PagedModel<EntityModel<VW_CHOOSE_ADDRESS>> pagedData = assembler.toModel(data);
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
    public ResponseEntity<ResponseObject> getListContact(Integer customerId, MaterialTablePagingRequest pagingData,
			PagedResourcesAssembler<VW_CHOOSE_CONTACT> assembler) {
            logger.info("Get List Contact");
            try {

                Map<String, Object> filter = new HashMap<>();
                filter.put("status", FlowStatus.ACTIVE.name());

                Page<VW_CHOOSE_CONTACT> data;

                if(!pagingData.getSearch().isEmpty()) {
                    pagingData.setPage(0);
                    data = vwChooseContactRepo.findAll(vwChooseContactRepo.getSpecificationFromFilters(pagingData, filter), PagingUtils.getPaging(pagingData));
                } else {
                    data = vwChooseContactRepo.findAll(vwChooseContactRepo.getSpecificationDefault(filter), PagingUtils.getPaging(pagingData));
                }

                List<Integer> idContact = new ArrayList<>();
                List<VW_CUSTOMER_CONTACT> optContact = customerId!=null?vcRepo.findAllByCustomerId(customerId):null;
                if(optContact!=null && !optContact.isEmpty()){
                    for(VW_CUSTOMER_CONTACT ctc : optContact) {
                        idContact.add(ctc.getContactId());
                    }
                    // delete duplicate id contact
                    Set<Integer> uniqueIds = new HashSet<>(idContact);
                    idContact.clear();
                    idContact.addAll(uniqueIds);
                }
                
                List<LinkedHashMap<String, Object>> allData = data.stream()
                    .filter(distinctByKey(g -> g.getContactId()))
                    .map(g -> {
                        LinkedHashMap<String, Object> acontact = new LinkedHashMap<>();
                        acontact.put("contactId", g.getContactId());
                        acontact.put("firstName", g.getFirstName());
                        acontact.put("middleName", g.getMiddleName());
                        acontact.put("lastName", g.getLastName());
                        acontact.put("contactName", g.getContactName());
                        acontact.put("contactAddress", g.getContactAddress());
                        acontact.put("jobId", g.getJobId());
                        acontact.put("jobName", g.getJobName());
                        acontact.put("positionId", g.getPositionId());
                        acontact.put("positionName", g.getPositionName());
                        acontact.put("source", idContact.contains(g.getContactId())? "CUSTOMER":"MASTER");

                        List<LinkedHashMap<String, Object>> allContactDetail = cdRepo.findAll().stream()
                            .filter(d -> Objects.equals(g.getContactId(), d.getContactId()))
                            .map(d -> {
                                LinkedHashMap<String, Object> dd = new LinkedHashMap<>();
                                dd.put("contactId", d.getContactId());
                                dd.put("typeId", d.getType());
                                Optional<R_GLOBAL_TYPE_VALUE> tn = rGlobalTypeValueRepo.findByGlbTypeValId(d.getType());
                                dd.put("type", tn.isPresent()? tn.get().getName() : null);
                                dd.put("inputTypeId", d.getInputType());
                                Optional<R_GLOBAL_TYPE_VALUE> it = rGlobalTypeValueRepo.findByGlbTypeValId(d.getInputType());
                                dd.put("inputType", it.isPresent()? it.get().getName() : null);
                                dd.put("value", d.getFullValue());
                                return dd;
                            })
                            .collect(Collectors.toList());
                        acontact.put("contactDetails", allContactDetail);
                        acontact.put("status", g.getStatus());
                        return acontact;
                    })
                    .collect(Collectors.toList());

                PagedModel<EntityModel<VW_CHOOSE_CONTACT>> pagedData = assembler.toModel(data);
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
    public ResponseEntity<ResponseObject> getTypeHomeList() {
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Address Type",
                    FlowStatus.ACTIVE.name(), false);
            logger.info("List Type Home" + auths);
            if (!auths.isEmpty()) {
                List<R_GLOBAL_TYPE_VALUE> areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name()))
                        .collect(Collectors.toList());
                List<MLocationDetailDTO> listType = new ArrayList<>();
                if (!areas.isEmpty()) {
                    int index = 0;
                    for (R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        MLocationDetailDTO locDTO = new MLocationDetailDTO();
                        locDTO.setId(rgtv.getGlbTypeValId());
                        locDTO.setName(rgtv.getName());
                        listType.add(index++, locDTO);
                    }
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Success Get List Type Home");
                    result.setData(listType);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Failed Get List Type Home");
                    result.setData(ResponseUtils.DATA_EMPTY);
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Failed Get List Type Home");
                result.setData(ResponseUtils.DATA_EMPTY);
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
    
    public ResponseEntity<ResponseObject> getAccountPaymentChannelType() {
        logger.info("Find Payment Channel Type");
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Account Payment Channel",
                    FlowStatus.ACTIVE.name(), false);
            logger.info("Payment Channel Type" + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;

            if (!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name()))
                        .collect(Collectors.toList());
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if (!areas.isEmpty()) {
                    for (R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        ar.put("id", rgtv.getGlbTypeValId());
                        ar.put("name", rgtv.getName());
                        areaList.add(ar);
                    }
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Success get Payment Channel Type");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Failed get Payment Channel Type");
                    result.setData(ResponseUtils.DATA_EMPTY);
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Failed get Payment Channel Type");
                result.setData(ResponseUtils.DATA_EMPTY);
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
    
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
