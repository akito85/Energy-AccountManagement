package com.dbs.module.account.detail.premise.service;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.CriteriaServices;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.CommonVariables;
import com.dbs.common.library.utils.FlowStatus;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.database.crm.entities.accountmanagement.*;
import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import com.dbs.database.crm.entities.usermanagement.M_USER;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.accountmanagement.Account.*;
import com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement.VwSaRepo;
import com.dbs.database.crm.repositories.usermanagement.AuditTrailRepo;
import com.dbs.database.crm.repositories.usermanagement.MUserRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import com.dbs.module.account.detail.address.service.AccountAddressService;
import com.dbs.module.account.detail.premise.dto.ServicePointCreateDTO;
import com.dbs.module.account.detail.premise.dto.ServicePointNameDTO;
import com.dbs.module.account.detail.premise.dto.UpdateServicePointDTO;
import com.dbs.module.account.utils.ConstantAccount;
import com.dbs.module.account.utils.UtilsAccount;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.*;

import static com.dbs.common.base.utils.CommonHelper.removeSpace;
import com.dbs.database.crm.repositories.mastermanagement.MLocationsRepo;

@Service
public class ServicePointService {

    private static final Logger logger = LoggerFactory.getLogger(AccountAddressService.class);

    @Autowired
    private MAccountAddressRepo accountAddressRepo;
    @Autowired
    private MUserRepo userRepo;
    @Autowired
    private AuditTrailRepo auditTrailRepo;
    @Autowired
    private MaddressRepo maddressRepo;
    @Autowired
    private MLocationsRepo mLocationRepo;
    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;
    @Autowired
    private MServicePointRepo mServicePointRepo;
    @Autowired
    private Validator validator;
    @Autowired
    private VwAssetAssignmentRepo vwAssetAssignRepo;
    @Autowired 
    private GlobalTypeValueService globalTypeService;
    @Autowired
    private VwSaRepo vwSaRepo;
    @Autowired
    private MAccountAddressRepo acaddrRepo;

    @Autowired
    private VWCustomerAddressRepo vwCustomerAddressRepo;
    @Autowired
    private CriteriaServices criteriaServices;

    public ResponseEntity<ResponseObject> getServicePointNameList() {
        logger.info("Service Point Name");
        ResponseObject result;
        try {
            List<LinkedHashMap<String, Object>> allData = criteriaServices.getCriteriaByGlobalType("Service Point Name");
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_OK, allData);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(CommonVariables.ERROR_IN, e.getMessage(), e);
            throw e;
        }
//        ResponseObject result = new ResponseObject();
//
//        try {
//
//            List<R_GLOBAL_TYPE_VALUE> getListSp = rGlobalTypeValueRepo.findByGlobalType(37);
//            List<ServicePointNameDTO> listSp = new ArrayList<>();
//            int index = 0;
//
//            for (R_GLOBAL_TYPE_VALUE sp : getListSp) {
//                ServicePointNameDTO spDTO = new ServicePointNameDTO();
//                spDTO.setId(sp.getGlbTypeValId());
//                spDTO.setValue(sp.getGlbValue());
//                spDTO.setText(sp.getName());
//                listSp.add(index++, spDTO);
//            }
//
//            result.setSuccess(true);
//            result.setCode(HttpStatus.OK);
//            result.setMessage("Success Get List Service Point Name");
//            result.setData(listSp);
//
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

    public ResponseEntity<ResponseObject> getServicePointAddress(Integer accountId) {
        ResponseObject result = new ResponseObject();
        try {
            List<VW_CUSTOMER_ADDDRESS> getAllAddress = vwCustomerAddressRepo.findAllByAccountIdAndStatusAndPremiseFlagIsTrue(accountId, FlowStatus.ACTIVE.name());
            List<ServicePointNameDTO> listAddress = new ArrayList<>();
            int index = 0;

            for (VW_CUSTOMER_ADDDRESS address : getAllAddress) {
                ServicePointNameDTO spDTO = new ServicePointNameDTO();
                spDTO.setId(address.getAccountAddressId());
                spDTO.setValue(address.getFullAddress());
                listAddress.add(index++, spDTO);
            }

            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success Get List Service Point Address");
            result.setData(listAddress);

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {

            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> createServicePoint(ServicePointCreateDTO request) {
        ResponseObject result;
        try {

            ResponseEntity<ResponseObject> validate = this.validateCreateServicePoint(request);
            if(!validate.getBody().getSuccess()) {
                return validate;
            }

            M_SERVICE_POINT servicePoint = new M_SERVICE_POINT();
            servicePoint.setAccountAddressId(request.getAccountAddressId());
            servicePoint.setServicePointName(request.getServicePointName());
            servicePoint.setDescription(removeSpace(request.getDescription()));
            servicePoint.setCreatedBy(UserDetailUtils.getUsername());
            servicePoint.setStatus(FlowStatus.ACTIVE.name());
            servicePoint.setCreatedDate(new Date());
            mServicePointRepo.save(servicePoint);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                        "Success create Service Point", servicePoint);

            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateCreateServicePoint(ServicePointCreateDTO request) {
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

        Optional<M_SERVICE_POINT> cek = mServicePointRepo.findByServicePointNameAndAccountAddressIdAndStatus(request.getServicePointName(), request.getAccountAddressId(), FlowStatus.ACTIVE.name());
        if(cek.isPresent()) {
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                    "Service point name is already exist!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                "Success validate create account address", ResponseUtils.DATA_EMPTY), HttpStatus.OK);
    }

    @SuppressWarnings("java:S3776")
    public ResponseEntity<ResponseObject> inactiveServicePoint(UpdateServicePointDTO request) {
        logger.info("paramRequest -> {}", request);

        ResponseObject result;

        try {
            Optional<M_SERVICE_POINT> data = mServicePointRepo.findById(request.getServicePointId());
            if(data.isPresent()) {
                Optional<VW_ASSET_ASSIGNMENT> cekIsThereActive = vwAssetAssignRepo.findTopByServicePointIdAndStatusOrderByIdDesc(request.getServicePointId(), FlowStatus.ACTIVE.name());
                if (cekIsThereActive.isPresent()) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "You cannot inactivate service point because this service point still have active asset assignment", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                
                Optional<M_ACCOUNT_ADDRESS> getAccountId = acaddrRepo.findById(data.get().getAccountAddressId());
                if(getAccountId.isPresent()) {
                    Optional<List<VW_SA>> cekIsThereExist = vwSaRepo.findAllByAccountIdAndServiceTypeAndStatusAndIsMain(getAccountId.get().getAccountId(), "Gas", FlowStatus.ACTIVE.name(), "Y");
                    if(cekIsThereExist.isPresent()) {
                        for(VW_SA cekk : cekIsThereExist.get()) {
                            if(cekk.getStartDate().getTime() <= new Date().getTime() && cekk.getEndDate().getTime() >= new Date().getTime()) {
                                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                                        "You cannot inactivate service point because this account has active main gas service agreement, account should have at least one active service point", ResponseUtils.DATA_EMPTY);
                                return new ResponseEntity<>(result, result.getHttpCode());
                            }
                        }
                    }
                }
                
                M_SERVICE_POINT wt = data.get();
                LinkedHashMap<String, Object> oldWt = new LinkedHashMap<>();
                oldWt.put("servicePointId", wt.getId());
                oldWt.put("accoundAddressId", wt.getAccountAddressId());
                oldWt.put("description", wt.getDescription());
                AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
                ObjectMapper mapper = new ObjectMapper();
                String oldValue = mapper.writeValueAsString(oldWt);
                auditTrail.setOldValue(oldValue);
                auditTrail.setRemark(removeSpace(request.getRemarks()));
                auditTrail.setTableName("M_SERVICE_POINT");
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
                    wt.setUpdatedBy(UserDetailUtils.getUsername() != null ? UserDetailUtils.getUsername() : null);
                        
                    wt.setUpdatedDate(new Date());
                    M_SERVICE_POINT saveWT = mServicePointRepo.save(wt);
                    LinkedHashMap<String, Object> newWt = new LinkedHashMap<>();
                    newWt.put("servicePointId", saveWT.getId());
                    newWt.put("accountAddressId", saveWT.getAccountAddressId());
                    String newValue = mapper.writeValueAsString(newWt);
                    auditTrail.setNewValue(newValue);
                    auditTrail.setOperation(FlowStatus.INACTIVE.name());
                    auditTrailRepo.save(auditTrail);
                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            ResponseUtils.MESSAGE_INACTIVE, saveWT);
                } else {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Service Point Status not found or null", ResponseUtils.DATA_EMPTY);
                }

                return new ResponseEntity<>(result, result.getHttpCode());

            } else {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Service Point with id " + request.getServicePointId() + " is not found.", ResponseUtils.DATA_EMPTY);

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

    public ResponseEntity<ResponseObject> updateServicePoint(UpdateServicePointDTO request) {
        logger.info("paramRequest -> {}", request);

        ResponseObject result;
        Date newDate = new Date();

        try {

            ResponseEntity<ResponseObject> validate = this.validateUpdateServicePoint(request);
            if(!validate.getBody().getSuccess()) {
                return validate;
            }

            Optional<M_SERVICE_POINT> servicePoint = mServicePointRepo.findById(request.getServicePointId());
            M_SERVICE_POINT servicePointData = servicePoint.get();
            servicePointData.setUpdatedBy(UserDetailUtils.getUsername());
            servicePointData.setUpdatedDate(newDate);
            servicePointData.setDescription(removeSpace(request.getDescription()));
            mServicePointRepo.save(servicePointData);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                        UtilsAccount.messageSuccess(ConstantAccount.UPDATE, ConstantAccount.SERVICE_POINT), servicePointData);

            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateUpdateServicePoint(UpdateServicePointDTO request) {
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

        Optional<M_SERVICE_POINT> servicePoint = mServicePointRepo.findById(request.getServicePointId());
        if(!servicePoint.isPresent()) {
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                    UtilsAccount.messageDataNotFound(ConstantAccount.SERVICE_POINT, request.getServicePointId()), ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                UtilsAccount.messageSuccess(ConstantAccount.VALIDATE, ConstantAccount.SERVICE_POINT), ResponseUtils.DATA_EMPTY), HttpStatus.OK);
    }
}
