package com.dbs.module.account.main.services;

import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_ACCOUNT_INFORMATION;
import com.dbs.database.crm.repositories.accountmanagement.VWAccountInfoRepo;
import com.dbs.module.account.main.dto.DetailDTO;
import com.dbs.module.account.main.dto.PositionNameDTO;
import com.dbs.module.account.main.dto.VwCustomerDetailDTO;
import com.dbs.module.account.main.dto.GetFinancialInformationDTO;
import com.dbs.module.account.main.dto.CustomerExistRequestDTO;
import com.dbs.module.account.main.dto.AccountDTO;
import com.dbs.module.account.main.dto.CustomerInformationDTO;
import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.config.jwt.JwtUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.entities.CriteriaData;
import com.dbs.common.library.security.CryptoSecurity;
import com.dbs.common.library.utils.*;
import com.dbs.database.crm.entities.accountmanagement.*;
import com.dbs.database.crm.entities.product.M_LOCATION;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_BILLING_BUCKET;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_BILLING_BUCKET_CRITERIA_DATA;
import com.dbs.database.crm.entities.usermanagement.*;
import com.dbs.database.crm.repositories.accountmanagement.Account.*;
import com.dbs.database.crm.repositories.accountmanagement.MAccountingRulesRepo;
import com.dbs.database.crm.repositories.accountmanagement.VwAmTaxImplicationCriteriaDataRepo;
import com.dbs.database.crm.repositories.accountmanagement.VwCusInfoCcRepo;
import com.dbs.database.crm.repositories.rbi.MBillingBucketCriteriaDataRepo;
import com.dbs.database.crm.repositories.rbi.MBillingBucketDetailRepo;
import com.dbs.database.crm.repositories.rbi.MRbiBillingBucketRepo;
import com.dbs.database.crm.repositories.usermanagement.*;
import com.dbs.database.crm.utils.CostCenterUtils;
import com.dbs.module.account.detail.address.dto.AccountAddressCreateDTO;
import com.dbs.module.account.detail.contact.dto.AccountContactCreateDTO;
import com.dbs.module.account.detail.distributionmedia.dto.DismeCreateRequestDTO;
import com.dbs.module.account.detail.financialinformation.dto.PaymentChannelDTO;
import com.dbs.module.account.detail.financialinformation.dto.TaxIdentifierCreateDTO;
import com.dbs.module.account.detail.financialinformation.dto.TaxRelationCreateDTO;
import com.dbs.module.account.detail.financialinformation.dto.WithholdingTaxCreateDTO;
import com.dbs.module.account.main.dto.createaccount.CreateAccountStandartDTO;
import com.dbs.module.account.main.dto.createaccount.CreateAccountStandartDTOxyz;
import com.dbs.module.account.main.dto.createaccount.FinancialInformationDTO;
import com.dbs.module.account.master.contact.dto.ContactDetailsCreateRequestDTO;
import com.dbs.module.account.master.contact.service.ContactService;
import com.dbs.module.account.utils.UtilsAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jlefebure.spring.boot.minio.MinioConfigurationProperties;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Flow;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.dbs.common.base.utils.CommonHelper.removeSpace;
import static com.dbs.common.base.utils.Constant.GET_CC_CHILD;
import com.dbs.database.crm.repositories.mastermanagement.MLocationsRepo;
import com.dbs.module.account.main.dto.MCustomerDTO;
import javax.validation.ConstraintViolation;

@Service
@RequiredArgsConstructor
public class AccountStandartService {
    private static final Logger logger = LoggerFactory.getLogger(AccountStandartService.class);
    @Autowired
    private MGlobalTypeRepo mGlobalTypeRepo;
    @Autowired
    private MMeterReadingCodesRepo mMeterReadingCodesRepo;
    @Autowired

    private MAccountingRulesRepo mAccountingRulesRepo;
    @Autowired
    private MCustomerRepo mCustomerRepo;
    @Autowired
    private MAccountRepo mAccountRepo;
    @Autowired
    private VwCustomerInformationRepo vwCustomerInformationRepo;
    @Autowired
    private VwAccountRepo vwAccountRepo;
    @Autowired
    private VWAccountInfoRepo vwAccountInfoRepo;
    @Autowired
    private MPositionRepo mPositionRepo;
    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;
    @Autowired
    private Validator validator;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MaddressRepo mAddressRepo;
    @Autowired
    private MAccountAddressRepo accountAddressRepo;
    @Autowired
    private MBusinessPurposeRepo mBusinessPurposeRepo;
    @Autowired
    private MContactRepo mContactRepo;
    @Autowired
    private MAccountContactRepo mAccountContactRepo;
    @Autowired
    private MContactDetailsRepo mContactDetailsRepo;
    @Autowired
    private AutoGeneratedCode autoGeneratedCode;
    @Autowired
    private MDistributionMediaRepo mDismeRepo;
    @Autowired
    private MTaxIdentifierRepo taxIdenRepo;
    @Autowired
    private MTaxRelationRepo taxRelRepo;
    @Autowired
    private MWithholdingTaxRepo wtaxRepo;
    @Autowired
    private MTaxImplicationCriteriaDataRepo criteriaRepo;
    @Autowired
    private MTaxImplicationRepo taxImpliRepo;
    @Autowired
    private MTaxImplicationRuleRepo taxImpliRuleRepo;
    @Autowired
    private RGlobalTypeValueRepo glbRepo;
    @Autowired
    private MBillingBucketCriteriaDataRepo billingCriteriaRepo;
    @Autowired
    private MRbiBillingBucketRepo billingRepo;
    @Autowired
    private MBillingBucketDetailRepo detailRepo;
    @Autowired
    private MAccountingRulesRepo accountingRulesRepo;
    @Autowired
    private MAccountTaxImplicationRepo accountTaxImpliRepo;
    @Autowired
    private MAccountBillingBucketRepo accountBillingBucketRepo;
    @Autowired
    private MCustomerEntityRepo customerEntityRepo;
    @Autowired
    private MCustomerCostCenterRepo customerCostCenterRepo;
    @Autowired
    private MAccountCustomerManagementRepo accountCustomerManagementRepo;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private MAttachmentRepo mAttachmentRepo;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioConfigurationProperties configurationProperties;
    @Autowired
    private Environment env;
    @Autowired
    private CostCenterUtils costCenterUtils;
    @Autowired
    private MDataAccessHierarchyRepo mDataAccessHierarchyRepo;
    @Autowired
    private RDataAccessHierarchyRepo rDataAccessHierarchyRepo;
    @Autowired
    private MCostCenterRepo mCostCenterRepo;
    @Autowired
    private MLocationsRepo mLocationRepo;
    @Autowired
    private VwCusInfoCcRepo vwCusInfoCcRepo;
    @Autowired
    private CreateParty party;
    @Autowired
    private MGlobalPropertiesRepo mGlobalPropertiesRepo;
    @Autowired
    private ContactUtils contactUtils;

    @Autowired
    private GlobalTypeValueService globalTypeValueService;

    @Autowired
    private VwAmTaxImplicationCriteriaDataRepo vwAmTaxImplicationCriteriaDataRepo;

    @Autowired
    private MRbiBillingBucketRepo mRbiBillingBucketRepo;

    @Autowired
    private UtilsAccount.GlobalTypeServiceAccount globalTypeServiceAccount;

    @Autowired
    private MUserRepo mUserRepo;

    public ResponseEntity<ResponseObject> getCustomerTypes() {
        logger.info("Find Customer Type");
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Customer Type",
                    FlowStatus.ACTIVE.name(), false);
            logger.info("Customer Type " + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;

            if (!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name()))
                        .collect(Collectors.toList());
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if (!areas.isEmpty()) {
                    for (R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        if(!rgtv.getName().equalsIgnoreCase(Constant.ACC_ONETIME)) {
                            ar.put("id", rgtv.getGlbTypeValId());
                            ar.put("name", rgtv.getName());
                            areaList.add(ar);
                        }
                    }
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Success get Customer Type");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Failed get Customer Type");
                    result.setData(ResponseUtils.DATA_EMPTY);
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Failed get Customer Type");
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
    @SuppressWarnings({"java:S1192"})
    public ResponseEntity<ResponseObject> getIdentificationTypes() {
        logger.info("Find Identification Type");
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Identification Type",
                    FlowStatus.ACTIVE.name(), false);
            logger.info("Identification Type " + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;

            if (!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name()))
                        .collect(Collectors.toList());
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if (!areas.isEmpty()) {
                    for (R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        if(!rgtv.getName().equalsIgnoreCase("One Time")) {
                            ar.put("id", rgtv.getGlbTypeValId());
                            ar.put("name", rgtv.getName());
                            areaList.add(ar);
                        }
                    }
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Success get Identification Type");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Failed get Identification Type");
                    result.setData(ResponseUtils.DATA_EMPTY);
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Failed get Identification Type");
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

    //acr
    public ResponseEntity<ResponseObject> getIdentificationTypesWithCustomerType(Integer customerType) {
        logger.info("Find Identification Type");
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Identification Type",
                    FlowStatus.ACTIVE.name(), false);
            logger.info("Identification Type " + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;

            if (!auths.isEmpty()) {
                if(customerType.equals(59)) {
                    areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name()) && !b.getName().equalsIgnoreCase("DUNS")).collect(Collectors.toList());
                }else{
                    areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name())).collect(Collectors.toList());
                }
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if (!areas.isEmpty()) {
                    for (R_GLOBAL_TYPE_VALUE rgtv : areas) {
                            LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                            if(!rgtv.getName().equalsIgnoreCase("One Time")) {
                                ar.put("id", rgtv.getGlbTypeValId());
                                ar.put("name", rgtv.getName());
                                areaList.add(ar);
                            }
                    }
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Success get Identification Type");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Failed get Identification Type");
                    result.setData(ResponseUtils.DATA_EMPTY);
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Failed get Identification Type");
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
    @SuppressWarnings("java:S3776")
    public ResponseEntity<ResponseObject> checkIsCustomerExist(CustomerExistRequestDTO dto, HttpServletRequest request) {
        ResponseObject result;
        CustomerInformationDTO customerInformationDTO =  new CustomerInformationDTO();
        Optional<M_CUSTOMER> mCustomerOptional = mCustomerRepo.findByIdentificationTypeAndCustomerIdentificationNumber(dto.getIdentificationType(),dto.getIdentificationNumber());
        // Set Position Name
        Optional<M_POSITION> mPosition = mPositionRepo.findByPositionIdAndStatusAndIsDeleted(UserDetailUtils.getPositionFromToken(request),FlowStatus.ACTIVE.name(),false);
        if (mPosition.isEmpty()) {
            customerInformationDTO.setCustomerManagement(null);
            customerInformationDTO.setSor(null);
        } else {
            PositionNameDTO positionNameDTO = new PositionNameDTO();
            DetailDTO customerManagement = new DetailDTO();
            DetailDTO sor = new DetailDTO();
            DetailDTO cc = new DetailDTO();
            M_POSITION position = mPosition.get();
            positionNameDTO.setCustomerManagement(position.getName());
            positionNameDTO.setCc(position.getCcId().getCode() + " - " + position.getCcId().getName());
            positionNameDTO.setSor(position.getCcId().getCode() + " - " + position.getCcId().getName());
            // Set detail DTO
            customerManagement.setId(position.getPositionId());
            customerManagement.setName(position.getName());

            Optional<M_DATA_ACCESS_HIERARCHY> getDahId = mDataAccessHierarchyRepo.findTopByStatusOrderByCreatedDateDesc(FlowStatus.ACTIVE.name());
            if(getDahId.isPresent()) {
                Optional<R_DATA_ACCESS_HIERARCHY> getSorId = rDataAccessHierarchyRepo.findTopByDahIdAndCcIdAndStatusOrderByCreatedDateDesc(getDahId.get().getDahId(), costCenterUtils.findCostCenterByPositionId(UserDetailUtils.getPositionFromToken(request)), FlowStatus.ACTIVE.name());
                if(getSorId.isPresent()) {
                    sor.setId(getSorId.get().getParentId());
                    Optional<M_COSTCENTER> getNameSor = mCostCenterRepo.findByccId(getSorId.get().getParentId());
                    sor.setName(getNameSor.isPresent() ? getNameSor.get().getCode() + " - " + getNameSor.get().getName() : null);
                }
            }

            cc.setId(position.getCcId().getCcId());
            cc.setName(position.getCcId().getCode() + " - " + position.getCcId().getName());

            // Set customer Management
            customerInformationDTO.setCc(cc);
            customerInformationDTO.setSor(sor);
            customerInformationDTO.setCustomerManagement(customerManagement);
        }

        // Set Account Group
        Optional<M_GLOBAL_TYPE> mGlobalTypeOpt = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Account Group",
                FlowStatus.ACTIVE.name(), false);
        if(mGlobalTypeOpt.isPresent()){
            M_GLOBAL_TYPE mGlobalType = mGlobalTypeOpt.get();
            DetailDTO detailDTO = new DetailDTO();
//            ganti ke group name
            Optional<R_GLOBAL_TYPE_VALUE> rgtv = rGlobalTypeValueRepo.findTopByGlobalTypeAndGlbValue(mGlobalType.getGlbTypeId(),dto.getAccountGroup());
            if(rgtv.isPresent()){
                detailDTO.setId(rgtv.get().getGlbTypeValId());
                detailDTO.setName(rgtv.get().getName());
            }else{
                detailDTO.setId(null);
                detailDTO.setName(null);
            }
            customerInformationDTO.setAccountGroup(detailDTO);
        }
        if(mCustomerOptional.isPresent() ){
            M_CUSTOMER mCustomer = mCustomerOptional.get();
            Optional<VW_CUS_INFO_CC> customerOptional = vwCusInfoCcRepo.findTopByCustomerId(mCustomer.getCustomerId());
            VW_CUS_INFO_CC vwCustomer = customerOptional.get();

            List<VW_ACCOUNT_INFORMATION> accountList = null;

            // FILTERING ACCOUNT
            if(!globalTypeServiceAccount.isItSuperUser()) {
                Optional<M_USER> user = mUserRepo.findByUsername(UserDetailUtils.getUsername());
                Optional<M_POSITION> cekPosition = (user.get().getUserType().equalsIgnoreCase("EMP")?mPositionRepo.findById(UserDetailUtils.getPositionFromToken(request)): Optional.empty());
                // admin entity
                if(user.get().getUserLevel().equalsIgnoreCase("AE")) {
                    List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(UserDetailUtils.getPositionFromToken(request), GET_CC_CHILD);
                    accountList = vwAccountInfoRepo.findAllByCustomerIdAndUniqueAccountAndEntityIdAndCostCenterIdIn(mCustomer.getCustomerId(), "USE", UserDetailUtils.getUserEntity(), ccList);
                    logger.info("AE costCenterId : " + ccList);
                }
                // end user (CM)
                else if(cekPosition.isPresent() && Pattern.matches(".*\\bCM\\b.*", cekPosition.get().getName())) {
                    accountList = vwAccountInfoRepo.findAllByCustomerIdAndUniqueAccountAndEntityIdAndCustomerManagementId(mCustomer.getCustomerId(), "USE", UserDetailUtils.getUserEntity(), UserDetailUtils.getPositionFromToken(request));
                    logger.info("customerManagementId : " + UserDetailUtils.getPositionFromToken(request).toString() + " - " + cekPosition.get().getName());
                }
                // end user (employee)
                else if(user.get().getUserType().equalsIgnoreCase("EMP")) {
                    List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(UserDetailUtils.getPositionFromToken(request), GET_CC_CHILD);
                    accountList = vwAccountInfoRepo.findAllByCustomerIdAndUniqueAccountAndEntityIdAndCostCenterIdIn(mCustomer.getCustomerId(), "USE", UserDetailUtils.getUserEntity(), ccList);
                    logger.info("costCenterId : " + ccList);
                }
                // end user (non employee)
                else if(user.get().getUserType().equalsIgnoreCase("NON_EMP")) {
                    accountList = null;
                    logger.info("userType : " + user.get().getUserType());
                }
            } else {
                accountList = vwAccountInfoRepo.findAllByCustomerIdAndUniqueAccountAndEntityId(mCustomer.getCustomerId(), "USE", UserDetailUtils.getUserEntity());
                logger.info("userLevel : SU");
            }

//            if(cekPosition.isPresent() && Pattern.matches(".*\\bCM\\b.*", cekPosition.get().getName())) {
//                accountList = vwAccountRepo.findAllByCustomerIdAndCustomerManagementId(vwCustomer.getCustomerId(), UserDetailUtils.getPositionFromToken(request));
//                logger.info("cm : " + UserDetailUtils.getPositionFromToken(request));
//            } else {
//                accountList = vwAccountRepo.findAllByCustomerIdAndCostCenterId(vwCustomer.getCustomerId(), UserDetailUtils.getPositionFromToken(request));
//                logger.info("cc : " + costCenterUtils.findCostCenterByPositionId(UserDetailUtils.getPositionFromToken(request)));
//            }

            // Set Customer Info
            VwCustomerDetailDTO customerDetailDTO = new VwCustomerDetailDTO();
            customerDetailDTO.setCustomerId(vwCustomer.getCustomerId());
            customerDetailDTO.setCustomerName(vwCustomer.getCustomerName());
            customerDetailDTO.setCustomerNumber(vwCustomer.getCustomerNumber());
            customerDetailDTO.setCustomerType(vwCustomer.getCustomerType());
            customerDetailDTO.setFirstName(vwCustomer.getFirstName());
            customerDetailDTO.setDateOfBirth(vwCustomer.getFoundedBirthDate());
            customerDetailDTO.setPlaceOfBirth(vwCustomer.getFoundedBirthPlace());
            customerDetailDTO.setIdentificationType(vwCustomer.getIdentificationType());
            customerDetailDTO.setLastName(vwCustomer.getLastName());
            customerDetailDTO.setSex(vwCustomer.getSex());
            customerDetailDTO.setStatus(vwCustomer.getStatus());
            customerDetailDTO.setPosition(vwCustomer.getPositionId());
            customerDetailDTO.setMaritalStatus(vwCustomer.getMaritalStatus());
            customerDetailDTO.setMiddleName(vwCustomer.getMiddleName());
            customerDetailDTO.setPersonalIdentificationNumber(vwCustomer.getCustomerIdentificationNumber());
            customerDetailDTO.setSearchKey(vwCustomer.getSearchKey());
            customerDetailDTO.setDescription(vwCustomer.getDescription());

            // Set Customer Response
            customerInformationDTO.setRegistered(true);
            customerInformationDTO.setCustomerInformation(customerDetailDTO);
            customerInformationDTO.setAccountList(accountList);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Customer Already Registered", customerInformationDTO);


        } else {
            customerInformationDTO.setRegistered(false);
            customerInformationDTO.setCustomerInformation(null);
            customerInformationDTO.setAccountList(null);
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Customer not Registered", customerInformationDTO);
        }
        return new ResponseEntity<>(result, result.getHttpCode());
    }

    public ResponseEntity<ResponseObject> checkExistContact(AccountContactCreateDTO request) {
        ResponseObject result;
        try {
            Optional<M_CONTACT> cekExist = mContactRepo.findTopByContactNameAndJobIdAndPositionId(request.getContactName(), request.getJobId(), request.getPositionId());
            if(cekExist.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "The contact data name, job and position have been registered", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success check validate data contact", ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> checkPremiseAddress(AccountAddressCreateDTO request) {
        logger.info("paramRequest -> {}", request);
        ResponseObject result;
        try {
            //VALIDASI PREMISE ADDRESS
            if(request.getPremiseFlag() && request.getAddressId() != null) {
                Optional<M_ACCOUNT_ADDRESS> cekPremiseActive = accountAddressRepo.findTopByAddressIdAndPremiseFlagAndStatus(request.getAddressId(), Boolean.TRUE, FlowStatus.ACTIVE.name());
                if(cekPremiseActive.isPresent()) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
                    "This address is already registered as a premise on another account", ResponseUtils.DATA_EMPTY), HttpStatus.OK);
                }
            }
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success check validate data address", ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> checkExistRegistrationNumber(AccountDTO request) {
        logger.info("paramRequest -> {}", request);
        ResponseObject result;
        try {
            Optional<M_ACCOUNT> cekExist = mAccountRepo.findTopByRegistrationNumber(request.getRegistrationNumber());
            if(cekExist.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Account Registration Number already exist", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success check validate data registration number", ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<ResponseObject> validateCreateAccount(CreateAccountStandartDTO dto) {
        // == ACCOUNT ==
        // DTO
        Set<ConstraintViolation<MCustomerDTO>> violationsAccount = this.validator.validate(dto.getCustomerInformation());
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
            ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, validateHeader.get(0), violationHeaderList);
            return new ResponseEntity<>(result, result.getHttpCode());
        }
        // REGISTRASI NUMBER
        if(dto.getAccountInformation().getRegistrationNumber() != null) {
            Optional<M_ACCOUNT> cekRegisNumber = mAccountRepo.findTopByRegistrationNumber(dto.getAccountInformation().getRegistrationNumber());
            if (cekRegisNumber.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Account Registration Number already exist", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
        }
        // == CUSTOMER ==
        if(!dto.getRegistered()) {
            // DTO
            Set<ConstraintViolation<AccountDTO>> violationsCustomer = this.validator.validate(dto.getAccountInformation());
            if (!violationsCustomer.isEmpty()) {
                List<Map<String, Object>> violationHeaderList = new ArrayList<>();
                List<String> validateHeader = new ArrayList<>();
                for (var violation : violationsCustomer) {
                    logger.error(violation.getMessage());
                    Map<String, Object> data = new HashMap<>();
                    validateHeader.add(violation.getMessage());
                    data.put(violation.getPropertyPath().toString(), violation.getMessage());
                    violationHeaderList.add(data);
                }
                ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, validateHeader.get(0), violationHeaderList);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            // IF ONE TIME
            if(!StringUtils.hasValue(dto.getCustomerInformation().getCustomerType())) {
                Optional<R_GLOBAL_TYPE_VALUE> rCustomerType = globalTypeValueService.getOptionalGlobalTypeByGlbValue("Customer Type", "One Time");
                if(rCustomerType.isEmpty()) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Customer type not found!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
            }
            if(!StringUtils.hasValue(dto.getCustomerInformation().getIdentificationType())) {
                Optional<R_GLOBAL_TYPE_VALUE> rCustomerType = globalTypeValueService.getOptionalGlobalTypeByGlbValue("Identification Type", "One Time");
                if(rCustomerType.isEmpty()) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Customer identification type not found!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
            }
            // IF ORGANIZATION FIRSTNAME NOT BE EMPTY
            if(StringUtils.hasValue(dto.getCustomerInformation().getCustomerType()) && !dto.getCustomerInformation().getCustomerType().equals(58)
                    && removeSpace(dto.getCustomerInformation().getFirstName()) == null){
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Firstname not be empty", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
            // UNIQUE CUSTOMER IDENTIFICATION NUMBER
            if (StringUtils.hasValue(dto.getCustomerInformation().getCustomerType()) &&mCustomerRepo.existsByCustomerIdentificationNumber(dto.getCustomerInformation().getCustomerIdentificationNumber())) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Customer Identification Number already exist", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
        }
        // == ACCOUNT ADDRESS ==
        List<AccountAddressCreateDTO> allDataAddressCreate = new ArrayList<>();
        if(dto.getAccountAddress().getAddress1() != null) {
            allDataAddressCreate.add(dto.getAccountAddress().getAddress1());
        }
        if (dto.getAccountAddress().getAddress2() != null) {
            allDataAddressCreate.add(dto.getAccountAddress().getAddress2());
        }
        if(dto.getAccountAddress().getAddress3() != null) {
            allDataAddressCreate.add(dto.getAccountAddress().getAddress3());
        }
        if(dto.getAccountAddress().getAddress4() != null) {
            allDataAddressCreate.add(dto.getAccountAddress().getAddress4());
        }
        for(AccountAddressCreateDTO addressCreate : allDataAddressCreate) {
            // DTO
            Set<ConstraintViolation<AccountAddressCreateDTO>> violationsAddr = this.validator.validate(addressCreate);
            if (!violationsAddr.isEmpty()) {
                List<Map<String, Object>> violationList = new ArrayList<>();
                List<String> validate = new ArrayList<>();
                for (var violation : violationsAddr) {
                    logger.error(violation.getMessage());
                    Map<String, Object> data = new HashMap<>();
                    validate.add(violation.getMessage());
                    data.put(violation.getPropertyPath().toString(), violation.getMessage());
                    violationList.add(data);
                }
                ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, validate.get(0), violationList);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            if(addressCreate.getAddressId()==null) {
                Optional<M_ADDRESSES> cekFullAddressExist = mAddressRepo.findByFullAddress(addressCreate.getFullAddress());
                if(cekFullAddressExist.isPresent()){
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "The Address have been registered", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
            }
        }
        // == ACCOUNT CONTACT ==
        List<AccountContactCreateDTO> allDataContactCreate = new ArrayList<>();
        if(dto.getAccountContact().getContact1() != null) {
            allDataContactCreate.add(dto.getAccountContact().getContact1());
        }
        if (dto.getAccountContact().getContact2() != null) {
            allDataContactCreate.add(dto.getAccountContact().getContact2());
        }
        if(dto.getAccountContact().getContact3() != null) {
            allDataContactCreate.add(dto.getAccountContact().getContact3());
        }
        if(dto.getAccountContact().getContact4() != null) {
            allDataContactCreate.add(dto.getAccountContact().getContact4());
        }
        for(AccountContactCreateDTO contactCreate : allDataContactCreate) {
            if(contactCreate.getContactId()==null) {
                Optional<M_CONTACT> cekExist = mContactRepo.findTopByContactNameAndJobIdAndPositionId(removeSpace(contactCreate.getContactName()), contactCreate.getJobId(), contactCreate.getPositionId());
                if(cekExist.isPresent()) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "The contact data name, job and position have been registered", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
            }
        }
        // == DISTRIBUTION MEDIA ==
        for(DismeCreateRequestDTO dismeCreate : dto.getDistributionMedia()) {
            Set<ConstraintViolation<DismeCreateRequestDTO>> violationsDisme = this.validator.validate(dismeCreate);
            if (!violationsDisme.isEmpty()) {
                List<Map<String, Object>> violationList = new ArrayList<>();
                List<String> validate = new ArrayList<>();
                for (var violation : violationsDisme) {
                    logger.error(violation.getMessage());
                    Map<String, Object> data = new HashMap<>();
                    validate.add(violation.getMessage());
                    data.put(violation.getPropertyPath().toString(), violation.getMessage());
                    violationList.add(data);
                }
                ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, validate.get(0), violationList);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            if(dismeCreate.getStartDate()!=null && !UtilsDate.validateDate(dismeCreate.getStartDate())) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Start date must be greater than current date", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
        }
        // TAX RELATION ==
        if(dto.getFinancialInformation().getTaxRelation().getStartDate()!=null && !UtilsDate.validateDate(dto.getFinancialInformation().getTaxRelation().getStartDate())) {
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                    "Start date must be greater than current date", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
        }
        // WITHOLDING TAX ==
        if(dto.getFinancialInformation().getWapu().getStartDate()!=null && !UtilsDate.validateDate(dto.getFinancialInformation().getWapu().getStartDate())) {
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                    "Start date must be greater than current date", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                "Success validate create account", ResponseUtils.DATA_EMPTY), HttpStatus.OK);
    }

    @SuppressWarnings({"java:S3776","java:S1141","java:S1192","java:S3923"})
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> createAccount(CreateAccountStandartDTO dto, HttpServletRequest request) {
        logger.info("create Account Standart : {}", dto);
        ResponseObject result = new ResponseObject();

        try {

            ResponseEntity<ResponseObject> validate = this.validateCreateAccount(dto);
            if(!validate.getBody().getSuccess()) {
                return validate;
            }

            // FOR RESPONSE
            LinkedHashMap<String, Object> allData = new LinkedHashMap<>();
            SimpleDateFormat formatDate = new SimpleDateFormat("dd MMM yyyy");

            // SAVED CUSTOMER
            M_CUSTOMER mCustomer;
            if(dto.getRegistered()) {
                mCustomer = mCustomerRepo.findByCustomerId(dto.getCustomerId());
            } else {
                mCustomer = objectMapper.convertValue(dto.getCustomerInformation(), M_CUSTOMER.class);
                try {
                    mCustomer.setFoundedBirthDate(formatDate.parse(dto.getCustomerInformation().getFoundedBirthDate2()));
                } catch (Exception e) {
                    mCustomer.setFoundedBirthDate(null);
                }
                // IF ONE TIME
                if(!StringUtils.hasValue(dto.getCustomerInformation().getCustomerType())) {
                    Optional<R_GLOBAL_TYPE_VALUE> rCustomerType = globalTypeValueService.getOptionalGlobalTypeByGlbValue("Customer Type", "One Time");
                    mCustomer.setCustomerType(rCustomerType.map(R_GLOBAL_TYPE_VALUE::getGlbTypeValId).orElse(null));
                }
                if(!StringUtils.hasValue(dto.getCustomerInformation().getIdentificationType())) {
                    Optional<R_GLOBAL_TYPE_VALUE> rIdentificationType = globalTypeValueService.getOptionalGlobalTypeByGlbValue("Identification Type", "One Time");
                    mCustomer.setIdentificationType(rIdentificationType.map(R_GLOBAL_TYPE_VALUE::getGlbTypeValId).orElse(null));
                }
                if(!StringUtils.hasValue(dto.getCustomerInformation().getCustomerType()) && !StringUtils.hasValue(dto.getCustomerInformation().getCustomerIdentificationNumber())) {
                    mCustomer.setCustomerIdentificationNumber(autoGeneratedCode.createIdentificationNumber());
                }
                mCustomer.setCustomerName(removeSpace(dto.getCustomerInformation().getCustomerName()));
                mCustomer.setFoundedBirthPlace(removeSpace(dto.getCustomerInformation().getFoundedBirthPlace()));
                mCustomer.setSearchKey(removeSpace(dto.getCustomerInformation().getSearchKey()));
                mCustomer.setDescription(removeSpace(dto.getCustomerInformation().getDescription()));
                mCustomer.setCustomerNumber(autoGeneratedCode.createCustomer());
                mCustomer.setPositionId(UserDetailUtils.getPositionFromToken(request));
                mCustomer.setStatus(FlowStatus.ACTIVE.name());
                mCustomer.setCreatedBy(UserDetailUtils.getUsername());
                mCustomer.setCreatedDate(new Date());
                mCustomerRepo.save(mCustomer);

                // SAVED PARTY CUSTOMER
                Integer partyIdCustomer = party.createParty(CreateParty.CUSTOMER, mCustomer.getCustomerId().toString(), mCustomer.getCustomerIdentificationNumber(), mCustomer.getCustomerName(), UserDetailUtils.getUserEntity());
                mCustomer.setPartyId(partyIdCustomer);
                mCustomerRepo.save(mCustomer);
                allData.put("customer", mCustomer);

                //SAVED CUSTOMER ENTITY
                M_CUSTOMER_ENTITY newCustEnt = new M_CUSTOMER_ENTITY();
                newCustEnt.setCustomerId(mCustomer.getCustomerId());
                newCustEnt.setEntityId(UserDetailUtils.getUserEntity());
                newCustEnt.setStartDate(new Date());
                newCustEnt.setOwner("Y");
                newCustEnt.setCreatedBy(UserDetailUtils.getUsername());
                newCustEnt.setCreatedDate(new Date());
                newCustEnt.setStatus(FlowStatus.ACTIVE.name());
                M_CUSTOMER_ENTITY savedCustomerEntity = customerEntityRepo.save(newCustEnt);

                // SAVED CUSTOMER COST CENTER
                M_CUSTOMER_COST_CENTER newCustCC = new M_CUSTOMER_COST_CENTER();
                newCustCC.setCustomerId(mCustomer.getCustomerId());
                newCustCC.setCostCenterId(costCenterUtils.findCostCenterByPositionId(UserDetailUtils.getPositionFromToken(request))); //END USER
                newCustCC.setStartDate(new Date());
                newCustCC.setOwner("Y");
                newCustCC.setCreatedBy(UserDetailUtils.getUsername());
                newCustCC.setCreatedDate(new Date());
                newCustCC.setStatus(FlowStatus.ACTIVE.name());
                newCustCC.setMCustomerEntityId(savedCustomerEntity.getCustomerEntityId());
                customerCostCenterRepo.save(newCustCC);
            }

            // SAVED ACCOUNT
            M_ACCOUNT mAccount = objectMapper.convertValue(dto.getAccountInformation(),M_ACCOUNT.class);
            if(dto.getAccountInformation().getAccountGroup() != null) {
                mAccount.setAccountGroup(dto.getAccountInformation().getAccountGroup().toString());
            } else {
                mAccount.setAccountGroup("606");
            }
            mAccount.setCustomerId(mCustomer.getCustomerId());
            mAccount.setSyncFlag("N");
            mAccount.setAccountName(removeSpace(dto.getAccountInformation().getAccountName()));
            mAccount.setAccountNumber(autoGeneratedCode.createAccount());
            mAccount.setIsCorporate(dto.getAccountInformation().getCorporateFlag());
            mAccount.setIsException(dto.getAccountInformation().getExceptionFlag());
            mAccount.setEntityId(UserDetailUtils.getUserEntity());
            mAccount.setCreatedDate(new Date());
            mAccount.setCreatedBy(UserDetailUtils.getUsername());
            mAccount.setStatus(FlowStatus.REGISTERED.name());
            mAccount.setDescription(removeSpace(dto.getAccountInformation().getDescription()));
            mAccountRepo.save(mAccount);

            // SAVED PARTY ACCOUNT
            Integer partyIdAccount = party.createParty(CreateParty.ACCOUNT, mAccount.getAccountId().toString(), mAccount.getAccountNumber(), mAccount.getAccountName(), UserDetailUtils.getUserEntity());
            mAccount.setPartyId(partyIdAccount);
            mAccountRepo.save(mAccount);
            allData.put("account", mAccount);

            // SAVED ACCOUNT CUSTOMER MANAGEMENT
            M_ACCOUNT_CUSTOMER_MANAGEMENT newCM = new M_ACCOUNT_CUSTOMER_MANAGEMENT();
            newCM.setAccountNumber(mAccount.getAccountNumber());
            newCM.setAccountId(mAccount.getAccountId());
            String tokenFromHeader = jwtUtils.parseJwt(request);
            Integer currentPosition = jwtUtils.getPositionFromJwtToken(tokenFromHeader);
            Optional<M_POSITION> position = mPositionRepo.findByPositionId(currentPosition);
            newCM.setPositionId(position.get().getPositionId()); //END USER
            newCM.setStartDate(new Date());
            newCM.setOwner("Y");
            newCM.setCreatedBy(UserDetailUtils.getUsername());
            newCM.setCreatedDate(new Date());
            newCM.setStatus(FlowStatus.ACTIVE.name());
            accountCustomerManagementRepo.save(newCM);

            // GET ALL ACCOUNT ADDRESS
            List<AccountAddressCreateDTO> allDataAddressCreate = new ArrayList<>();
            if(dto.getAccountAddress().getAddress1() != null) {
                allDataAddressCreate.add(dto.getAccountAddress().getAddress1());
            }
            if (dto.getAccountAddress().getAddress2() != null) {
                allDataAddressCreate.add(dto.getAccountAddress().getAddress2());
            }
            if(dto.getAccountAddress().getAddress3() != null) {
                allDataAddressCreate.add(dto.getAccountAddress().getAddress3());
            }
            if(dto.getAccountAddress().getAddress4() != null) {
                allDataAddressCreate.add(dto.getAccountAddress().getAddress4());
            }

            // ADD BUSINESS PURPOSES TAX
            if(dto.getFinancialInformation().getTaxIdentifier().getTaxAddress().contains("TEMP")){
                String idTaxTemp = dto.getFinancialInformation().getTaxIdentifier().getTaxAddress().replaceAll("TEMP", "");
                if(idTaxTemp.equals("1")) {
                    List<Integer> bA1 = dto.getAccountAddress().getAddress1().getBusinessPurpose();
                    bA1.add(162);
                    dto.getAccountAddress().getAddress1().setBusinessPurpose(bA1);
                } else if(idTaxTemp.equals("2")) {
                    List<Integer> bA1 = dto.getAccountAddress().getAddress2().getBusinessPurpose();
                    bA1.add(162);
                    dto.getAccountAddress().getAddress2().setBusinessPurpose(bA1);
                } else if(idTaxTemp.equals("3")) {
                    List<Integer> bA1 = dto.getAccountAddress().getAddress3().getBusinessPurpose();
                    bA1.add(162);
                    dto.getAccountAddress().getAddress3().setBusinessPurpose(bA1);
                } else if(idTaxTemp.equals("4")) {
                    List<Integer> bA1 = dto.getAccountAddress().getAddress4().getBusinessPurpose();
                    bA1.add(162);
                    dto.getAccountAddress().getAddress4().setBusinessPurpose(bA1);
                }
            } else if(StringUtils.hasValue(dto.getAccountAddress().getAddress1()) && StringUtils.hasValue(dto.getAccountAddress().getAddress1().getAddressId()) && dto.getFinancialInformation().getTaxIdentifier().getTaxAddress().equalsIgnoreCase(dto.getAccountAddress().getAddress1().getAddressId().toString())) {
                List<Integer> bA1 = dto.getAccountAddress().getAddress1().getBusinessPurpose();
                bA1.add(162);
                dto.getAccountAddress().getAddress1().setBusinessPurpose(bA1);
            } else if(StringUtils.hasValue(dto.getAccountAddress().getAddress2()) && StringUtils.hasValue(dto.getAccountAddress().getAddress2().getAddressId()) && dto.getFinancialInformation().getTaxIdentifier().getTaxAddress().equalsIgnoreCase(dto.getAccountAddress().getAddress2().getAddressId().toString())) {
                List<Integer> bA1 = dto.getAccountAddress().getAddress2().getBusinessPurpose();
                bA1.add(162);
                dto.getAccountAddress().getAddress2().setBusinessPurpose(bA1);
            } else if(StringUtils.hasValue(dto.getAccountAddress().getAddress3()) && StringUtils.hasValue(dto.getAccountAddress().getAddress3().getAddressId()) && dto.getFinancialInformation().getTaxIdentifier().getTaxAddress().equalsIgnoreCase(dto.getAccountAddress().getAddress3().getAddressId().toString())) {
                List<Integer> bA1 = dto.getAccountAddress().getAddress3().getBusinessPurpose();
                bA1.add(162);
                dto.getAccountAddress().getAddress3().setBusinessPurpose(bA1);
            } else if(StringUtils.hasValue(dto.getAccountAddress().getAddress4()) && StringUtils.hasValue(dto.getAccountAddress().getAddress4().getAddressId()) && dto.getFinancialInformation().getTaxIdentifier().getTaxAddress().equalsIgnoreCase(dto.getAccountAddress().getAddress4().getAddressId().toString())) {
                List<Integer> bA1 = dto.getAccountAddress().getAddress4().getBusinessPurpose();
                bA1.add(162);
                dto.getAccountAddress().getAddress4().setBusinessPurpose(bA1);
            }

            // STEP ADDRESS
            List<Integer> saveIdAd = new ArrayList<>();
            Integer savedAddress;
            for(AccountAddressCreateDTO accountAddress : allDataAddressCreate){
                savedAddress = 0;
                Optional<Integer> bpShipTo = accountAddress.getBusinessPurpose().stream().filter(e->e.equals(166)).findAny();
                Optional<Integer> bpBillTo = accountAddress.getBusinessPurpose().stream().filter(e->e.equals(167)).findAny();
                int next = 0;
                for (Integer bp : accountAddress.getBusinessPurpose()) {
                    if(savedAddress<1) {
                        if(accountAddress.getAddressId()!=null) {
                            // ADD ADDRESS ID TO ARRAY
                            saveIdAd.add(accountAddress.getAddressId());
                        } else if(accountAddress.getAddressId()==null) {
                            // SAVED ADDRESS
                            M_ADDRESSES mAddress = objectMapper.convertValue(accountAddress,M_ADDRESSES.class);
                            try {
                                mAddress.setCreatedBy(UserDetailUtils.getUsername());
                            } catch (Exception e) {
                                mAddress.setCreatedBy(null);
                            }
                            mAddress.setBuilding(accountAddress.getBuilding()!=null?removeSpace(accountAddress.getBuilding().toUpperCase()):null);
                            mAddress.setHouseName(accountAddress.getHouseName()!=null?removeSpace(accountAddress.getHouseName().toUpperCase()):null);
                            mAddress.setStreetName(accountAddress.getStreetName()!=null?removeSpace(accountAddress.getStreetName().toUpperCase()):null);
                            mAddress.setBlock(accountAddress.getBlock()!=null?removeSpace(accountAddress.getBlock().toUpperCase()):null);
                            mAddress.setHouseNumber(accountAddress.getHouseNumber()!=null?removeSpace(accountAddress.getHouseNumber().toUpperCase()):null);
                            mAddress.setAdditionalInfo(accountAddress.getAdditionalNote()!=null?removeSpace(accountAddress.getAdditionalNote().toUpperCase()):null);
                            mAddress.setFullAddress(accountAddress.getFullAddress());
                            mAddress.setDescription(removeSpace(accountAddress.getDesc()));
                            mAddress.setNeighborhood1(accountAddress.getRt());
                            mAddress.setNeighborhood2(accountAddress.getRw());
                            mAddress.setType(accountAddress.getTypeId());
                            mAddress.setStatus(FlowStatus.ACTIVE.name());
                            mAddress.setCreatedDate(new Date());
                            mAddressRepo.save(mAddress);
                            // ADD ADDRESS ID TO ARRAY
                            saveIdAd.add(mAddress.getAddressId());
                        }
                        savedAddress++;
                    }
                    // SAVED ACCOUNT ADDRESS
                    M_ACCOUNT_ADDRESS accAddress = new M_ACCOUNT_ADDRESS();
                    // CEK PRIMARY FLAG & PREMISE FLAG
                    if (bpShipTo.isPresent() && bp.equals(166)) {
                        accAddress.setPremiseFlag(Boolean.TRUE);
                    } else {
                        if (bpShipTo.isEmpty() && next == 0) {
                            accAddress.setPremiseFlag(Boolean.FALSE);
                        } else {
                            accAddress.setPremiseFlag(Boolean.FALSE);
                        }
                    }
                    if (bpBillTo.isPresent() && bp.equals(167)) {
                        accAddress.setPrimaryFlag(Boolean.TRUE);
                    } else {
                        if (bpShipTo.isEmpty() && next == 0) {
                            accAddress.setPrimaryFlag(Boolean.TRUE);
                        } else {
                            accAddress.setPrimaryFlag(Boolean.FALSE);
                        }
                    }
                    Optional<M_ACCOUNT_ADDRESS> cekPrimaryFlag = accountAddressRepo.findTopByAccountIdAndStatusAndPrimaryFlagIsTrue(mAccount.getAccountId(), FlowStatus.ACTIVE.name());
                    if(cekPrimaryFlag.isPresent()){
                        accAddress.setPrimaryFlag(Boolean.FALSE);
                    }
                    accAddress.setAddressId(saveIdAd.get(saveIdAd.size()-1));
                    accAddress.setCreatedDate(new Date());
                    accAddress.setAccountId(mAccount.getAccountId());
                    accAddress.setBusinessPurpose(bp);
                    accAddress.setDescription(removeSpace(accountAddress.getDescAccountAddress()));
                    accAddress.setCreatedBy(UserDetailUtils.getUsername());
                    accAddress.setStatus(FlowStatus.ACTIVE.name());
                    accountAddressRepo.save(accAddress);
                    next++;
                }
            }

            // GET ALL ACCOUNT CONTACT
            List<AccountContactCreateDTO> allDataContactCreate = new ArrayList<>();
            if(dto.getAccountContact().getContact1() != null) {
                allDataContactCreate.add(dto.getAccountContact().getContact1());
            }
            if (dto.getAccountContact().getContact2() != null) {
                allDataContactCreate.add(dto.getAccountContact().getContact2());
            }
            if(dto.getAccountContact().getContact3() != null) {
                allDataContactCreate.add(dto.getAccountContact().getContact3());
            }
            if(dto.getAccountContact().getContact4() != null) {
                allDataContactCreate.add(dto.getAccountContact().getContact4());
            }

            // STEP CONTACT
            for(AccountContactCreateDTO accountContact : allDataContactCreate){
                M_CONTACT mContact = new M_CONTACT();
                if(accountContact.getContactId()==null) {
                    // MASTER CONTACT
                    mContact.setFirstName(removeSpace(accountContact.getFirstName()));
                    mContact.setMiddleName(removeSpace(accountContact.getMiddleName()));
                    mContact.setLastName(removeSpace(accountContact.getLastName()));
                    mContact.setContactName(removeSpace(accountContact.getContactName()));
                    mContact.setJobId(accountContact.getJobId());
                    mContact.setPositionId(accountContact.getPositionId());
                    mContact.setStatus(FlowStatus.ACTIVE.name());
                    mContact.setCreatedDate(new Date());
                    mContact.setCreatedBy(UserDetailUtils.getUsername());
                    mContactRepo.save(mContact);

                    // SAVED PARTY CONTACT
                    Integer partyIdContact = party.createParty(CreateParty.CONTACT, mContact.getContactId().toString(), mContact.getContactName(), mContact.getContactName(), UserDetailUtils.getUserEntity());
                    mContact.setPartyId(partyIdContact);
                    mContactRepo.save(mContact);

                    //CONTACT DETAIL
                    List<ContactDetailsCreateRequestDTO> contactDetailNew = accountContact.getContactDetail();
                    List<M_CONTACT_DETAILS> addedDetail = new ArrayList<>();
                    if (contactDetailNew != null) {
                        for (ContactDetailsCreateRequestDTO avv : contactDetailNew) {
                            M_CONTACT_DETAILS ddd = new M_CONTACT_DETAILS();
                            List<String> value = contactUtils.getContactDetailValue(avv.getPrefix1(), avv.getPrefix2(), avv.getValue(), avv.getSufix());
                            ddd.setFullValue(value.get(0));
                            ddd.setContactValue(value.get(1));
                            ddd.setContactId(mContact.getContactId());
                            ddd.setType(avv.getType());
                            ddd.setInputType(avv.getInputType());
                            ddd.setPrefix1(avv.getPrefix1());
                            ddd.setPrefix2(avv.getPrefix2());
                            ddd.setValue(avv.getValue());
                            ddd.setSufix(avv.getSufix());
                            ddd.setStatus(FlowStatus.ACTIVE.name());
                            ddd.setCreatedDate(new Date());
                            ddd.setCreatedBy(UserDetailUtils.getUsername());
                            addedDetail.add(ddd);
                        }
                        mContactDetailsRepo.saveAll(addedDetail);
                    }
                }

                // ACCOUNT CONTACT
                M_ACCOUNT_CONTACT accContact = new M_ACCOUNT_CONTACT();
                Optional<M_ACCOUNT_CONTACT> cekPrimaryFlag = mAccountContactRepo.findTopByAccountIdAndStatusAndPrimaryFlagIsTrue(mAccount.getAccountId(), FlowStatus.ACTIVE.name());
                accContact.setPrimaryFlag(cekPrimaryFlag.isPresent()?Boolean.FALSE:accountContact.getPrimaryFlag());
                accContact.setContactId(accountContact.getContactId()==null?mContact.getContactId():accountContact.getContactId());
                accContact.setContactAddressId(accountContact.getContactAddress().contains("TEMP")?saveIdAd.get(this.replaceTemp(accountContact.getContactAddress())-1):Integer.parseInt(accountContact.getContactAddress()));
                accContact.setAccountId(mAccount.getAccountId());
                accContact.setAdditionalNote(removeSpace(accountContact.getAdditionalNote()));
                accContact.setDescription(removeSpace(accountContact.getDescription()));
                accContact.setCreatedBy(UserDetailUtils.getUsername());
                accContact.setStatus(FlowStatus.ACTIVE.name());
                accContact.setCreatedDate(new Date());
                mAccountContactRepo.save(accContact);
            }

            // SAVE DISTRIBUTION MEDIA
            List<DismeCreateRequestDTO> requestDTODismeList = dto.getDistributionMedia();
            for(DismeCreateRequestDTO requestDTO : requestDTODismeList){
                M_DISTRIBUTION_MEDIA distributionMedia = new M_DISTRIBUTION_MEDIA();
                try {
                    distributionMedia.setStartDate(formatDate.parse(requestDTO.getStartDate()));
                } catch (Exception e) {
                    distributionMedia.setStartDate(null);
                }
                distributionMedia.setAccountId(mAccount.getAccountId());
                distributionMedia.setProductId(requestDTO.getProductId());
                distributionMedia.setDescription(removeSpace(requestDTO.getDescription()));
                distributionMedia.setStatus(FlowStatus.ACTIVE.name());
                distributionMedia.setCreatedDate(new Date());
                distributionMedia.setCreatedBy(UserDetailUtils.getUsername());
                mDismeRepo.save(distributionMedia);
            }

            // STEP FINANCIAL INFORMATION
            FinancialInformationDTO reqFinancial = dto.getFinancialInformation();

            // PAYMENT CHANNEL
            PaymentChannelDTO reqPaymentChannel = reqFinancial.getPaymentChannel();
            mAccount.setPaymentChannel(reqPaymentChannel.getPaymentChannelType());
            mAccountRepo.save(mAccount);

            // TAX IDENTIFIER
            TaxIdentifierCreateDTO reqTaxIden = reqFinancial.getTaxIdentifier();
            M_TAX_IDENTIFIER taxIdentifier = new M_TAX_IDENTIFIER();
            taxIdentifier.setTaxIdentifierAddress(reqTaxIden.getTaxAddress().contains("TEMP")?saveIdAd.get(this.replaceTemp(reqTaxIden.getTaxAddress())-1):Integer.parseInt(reqTaxIden.getTaxAddress()));
            // ENCRYPT
            String encryptNumber = CryptoSecurity.encrypt(reqTaxIden.getTaxIdentifierNumber());
            taxIdentifier.setTaxIdentifierNumber(encryptNumber);
            taxIdentifier.setAccountId(mAccount.getAccountId());
            taxIdentifier.setTaxIdentifierType(reqTaxIden.getTaxIdentifierType());
            taxIdentifier.setTaxIdentifierName(removeSpace(reqTaxIden.getTaxIdentifierName()));
            taxIdentifier.setStartDate(new Date());
            taxIdentifier.setStatus(FlowStatus.ACTIVE.name());
            taxIdentifier.setCreatedDate(new Date());
            taxIdentifier.setCreatedBy(UserDetailUtils.getUsername());
            taxIdenRepo.save(taxIdentifier);

            // TAX RELATION
            if(reqFinancial.getTaxRelation().getRelatedAccountId()!=null) {
                TaxRelationCreateDTO reqTaxRel = reqFinancial.getTaxRelation();
                M_TAX_RELATION taxRelation = new M_TAX_RELATION();
                try {
                    taxRelation.setStartDate(formatDate.parse(reqTaxRel.getStartDate()));
                } catch (Exception e) {
                    taxRelation.setStartDate(null);
                }
                taxRelation.setAccountId(mAccount.getAccountId());
                taxRelation.setRelatedAccountId(reqTaxRel.getRelatedAccountId());
                taxRelation.setDescription(removeSpace(reqTaxRel.getDescription()));
                taxRelation.setStatus(FlowStatus.ACTIVE.name());
                taxRelation.setCreatedDate(new Date());
                taxRelation.setCreatedBy(UserDetailUtils.getUsername());
                taxRelRepo.save(taxRelation);
            }

            // WITHOLDING TAX
            WithholdingTaxCreateDTO reqWiTax = reqFinancial.getWapu();
            if(reqWiTax.isWapuFlag()){
                M_WITHOLDING_TAX witholdingTax = new M_WITHOLDING_TAX();
                try {
                    witholdingTax.setStartDate(formatDate.parse(reqWiTax.getStartDate()));
                } catch (Exception e) {
                    witholdingTax.setStartDate(null);
                }
                witholdingTax.setAccountId(mAccount.getAccountId());
                witholdingTax.setDescription(removeSpace(reqWiTax.getDescription()));
                witholdingTax.setStatus(FlowStatus.ACTIVE.name());
                witholdingTax.setCreatedDate(new Date());
                witholdingTax.setCreatedBy(UserDetailUtils.getUsername());
                wtaxRepo.save(witholdingTax);
            }

            // SAVE TAX IMPLICATION
            List<Integer> dataTaxImpli = reqFinancial.getTaxImplication();
            for(Integer idTax : dataTaxImpli){
                M_ACCOUNT_TAX_IMPLICATION taxImplication = new M_ACCOUNT_TAX_IMPLICATION();
                taxImplication.setAccountId(mAccount.getAccountId());
                taxImplication.setTaxImplicationId(idTax);
                taxImplication.setStartDate(new Date());
                taxImplication.setCreatedBy(UserDetailUtils.getUsername());
                taxImplication.setCreatedDate(new Date());
                taxImplication.setStatus(FlowStatus.ACTIVE.name());
                accountTaxImpliRepo.save(taxImplication);
            }

            // SAVE BILLING BUCKET
            List<String> dataBillingBucket = reqFinancial.getBillingBucket();
            for(String idBB : dataBillingBucket){
                M_ACCOUNT_BILLING_BUCKET billingBucket = new M_ACCOUNT_BILLING_BUCKET();
                billingBucket.setAccountId(mAccount.getAccountId());
                billingBucket.setBillingBucketId(idBB);
                billingBucket.setStartDate(new Date());
                billingBucket.setCreatedBy(UserDetailUtils.getUsername());
                billingBucket.setCreatedDate(new Date());
                billingBucket.setStatus(FlowStatus.ACTIVE.name());
                accountBillingBucketRepo.save(billingBucket);
            }

            result.setSuccess(Boolean.TRUE);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success create account standart");
            result.setData(allData);
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (IllegalArgumentException e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }

    private Integer replaceTemp(String temp) {
        return Integer.parseInt(temp.replaceAll("TEMP", ""));
    }


    private String generateFullAddress(AccountAddressCreateDTO request) {
        StringBuilder fullAddressBuilder = new StringBuilder();

        List<String> commonStreetTemplate = Arrays.asList("JLN.", "JLN", "JALAN.", "JALAN", "GG.", "GG", "GANG.", "GANG", "JL.", "JL");

        String streetNameUpperCase = request.getStreetName().toUpperCase();
        for (String template : commonStreetTemplate) {
            if (streetNameUpperCase.contains(template)) {
                streetNameUpperCase = streetNameUpperCase.replaceAll(template, "");
                break;
            }
        }

        fullAddressBuilder.append("JL.");
        fullAddressBuilder.append(streetNameUpperCase);

        if (!ObjectUtils.isEmpty(request.getHouseNumber())) {
            fullAddressBuilder.append(", ").append(request.getHouseNumber());
        }

        if (!ObjectUtils.isEmpty(request.getRt())) {
            fullAddressBuilder.append(", RT. ").append(request.getRt());
        }

        if (!ObjectUtils.isEmpty(request.getRw())) {
            fullAddressBuilder.append(", RW ").append(request.getRw());
        }

        if (!ObjectUtils.isEmpty(request.getAdditionalNote())) {
            fullAddressBuilder.append(", ").append(request.getAdditionalNote());
        }

        appendLocation(fullAddressBuilder, request.getPostalCodeId(), "POSTAL_CODE");
        appendLocation(fullAddressBuilder, request.getSubDistrictId(), "SUB_DISTRICT");
        appendLocation(fullAddressBuilder, request.getDistrictId(), "DISTRICT");
        appendLocation(fullAddressBuilder, request.getCityId(), "CITY");
        appendLocation(fullAddressBuilder, request.getProvinceId(), "PROVINCE");
        appendLocation(fullAddressBuilder, request.getCountryId(), "COUNTRY");

        return fullAddressBuilder.toString().toUpperCase();
    }

    private void appendLocation(StringBuilder fullAddressBuilder, Integer locationId, String locationType) {
        if (!ObjectUtils.isEmpty(locationId)) {
            Optional<M_LOCATION> location = mLocationRepo.findByLocationIdAndLocationType(locationId, locationType);
            location.map(M_LOCATION::getLocationName).ifPresent(locationName -> fullAddressBuilder.append(", ").append(locationName));
        }
    }


    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> getTaxImplication(GetFinancialInformationDTO dto, HttpServletRequest request, MaterialTablePagingRequest pagingData,
                                                            PagedResourcesAssembler<M_RBI_BILLING_BUCKET> assembler) {
        logger.info("Get Tax Implication");
        try {

            LinkedHashMap<String, Object> allDataCheck = new LinkedHashMap<>();

            // GET DATA ACCOUNT
            Optional<VW_ACCOUNT_CRITERIA> optionalDataAccount = Optional.ofNullable(new VW_ACCOUNT_CRITERIA());
            AccountDTO dataAc = dto.getAccountInformation();
            VW_ACCOUNT_CRITERIA dd = optionalDataAccount.get();
            if(optionalDataAccount.isPresent()) {
                dd.setAccountSegment(dataAc.getAccountSegment());
                dd.setAccountGroupType(dataAc.getAccountGroupType());
                dd.setAccountBudget(dataAc.getBudget());
                dd.setAccountIndustrialSector(dataAc.getIndustrialSector());
                dd.setAccountCategory(dataAc.getAccountCategory());
                dd.setAccountAccountType(dataAc.getAccountType());
                dd.setAccountClassificationType(dataAc.getAccountRuleId());
                dd.setAccountCorporateFlag(dataAc.getCorporateFlag());

                for(AccountAddressCreateDTO aa : dto.getAccountAddress()) {
                    if(aa.getPremiseFlag()) {
                        if(aa.getAddressId() != null) {
                            Optional<M_ADDRESSES> newMAddress = mAddressRepo.findById(aa.getAddressId());
                            if(newMAddress.isPresent()) {
                                M_ADDRESSES addr = newMAddress.get();
                                dd.setPremiseCountry(addr.getCountryId());
                                dd.setPremiseProvince(addr.getProvinceId());
                                dd.setPremiseCity(addr.getCityId());
                                dd.setPremiseDistrict(addr.getDistrictId());
                                dd.setPremiseSubdistrict(addr.getSubDistrictId());
                            }
                        }else {
                            dd.setPremiseCountry(aa.getCountryId());
                            dd.setPremiseProvince(aa.getProvinceId());
                            dd.setPremiseCity(aa.getCityId());
                            dd.setPremiseDistrict(aa.getDistrictId());
                            dd.setPremiseSubdistrict(aa.getSubDistrictId());
                        }
                    }
                }
                dd.setSor(dataAc.getSor());
                dd.setCostCenter(dataAc.getCostCenter());
            }
            Optional<VW_ACCOUNT_CRITERIA> dataAccount = Optional.ofNullable(dd);

            //LIST FOR DATA ELIGIBLE
            List<LinkedHashMap<String, Object>> addedTaxImpli = new ArrayList<>();

            // GET ALL DATA CRITERIA PPN
            List<Integer> idPPN  = new ArrayList<>();
            List<M_AM_TAXIMPLICATION> dataPPN = taxImpliRepo.findAllByCategoryAndServiceType(127, 130);
            for(M_AM_TAXIMPLICATION ppn : dataPPN){
                idPPN.add(ppn.getId());
            }

            List<VW_AM_TAXIMPLICATION_CRITERIA_DATA> dataCriteriaPPN = vwAmTaxImplicationCriteriaDataRepo.findIn(idPPN);

            List<CriteriaData> addedCriteriaPPN = new ArrayList<>();

            for(VW_AM_TAXIMPLICATION_CRITERIA_DATA zz : dataCriteriaPPN){
                CriteriaData data = new CriteriaData();
                data.setId(zz.getId());
                data.setAccountCategory(zz.getAccountCategory());
                data.setAccountGroupType(zz.getAccountGroupType());
                data.setAccountNumber(zz.getAccountNumber());
                data.setAccountSegment(zz.getAccountSegment());
                data.setAccountType(zz.getAccountType());
                data.setClassificationType(zz.getClassificationType());
                data.setCorporateFlag(zz.getCorporateFlagBoolean());
                data.setCostCenter(zz.getCostCenter());
                data.setIsAll(zz.getAllCriteriaBoolean());
                data.setPremiseCity(zz.getCITY());
                data.setPremiseCountry(zz.getCOUNTRY());
                data.setPremiseDistrict(zz.getDISTRICT());
                data.setPremiseProvince(zz.getPROVINCE());
                data.setPremiseSubdistrict(zz.getSUBDISTRICT());
                data.setSaType(zz.getSaType());
                data.setSor(zz.getSor());
                data.setWapuFlag(zz.getWapuFlagBoolean());
//                UtilsAccount.getStatusByStartDateEndDate(zz.getStartDate(), zz.getEndDate());
                addedCriteriaPPN.add(data);

            }

            // CHECK CRITERIA
            Integer x = CheckCriteria.checkCondition(dataAccount, addedCriteriaPPN);

            Optional<M_AM_TAXIMPLICATION_CRITERIA_DATA> dataIdTaxImpli = criteriaRepo.findById(x);

            Optional<M_AM_TAXIMPLICATION> dataImpli = taxImpliRepo.findById(dataIdTaxImpli.get().getTaximplicationId());

            LinkedHashMap<String, Object> ppnData = new LinkedHashMap<>();

            ppnData.put("id", dataImpli.get().getId());
            ppnData.put("name", dataImpli.get().getTaxImplicationName());
            Optional<R_GLOBAL_TYPE_VALUE> st = glbRepo.findByGlbTypeValId(dataImpli.get().getServiceType());
            ppnData.put("serviceType", st.get().getName());
            Optional<R_GLOBAL_TYPE_VALUE> c = glbRepo.findByGlbTypeValId(dataImpli.get().getCategory());
            ppnData.put("category", c.get().getName());
            ppnData.put("description", dataImpli.get().getDescription());
            Optional<M_AM_TAXIMPLICATION_RULE> dataImpliRule = taxImpliRuleRepo.findByTaximplicationIdAndStatus(dataImpli.get().getId(), FlowStatus.ACTIVE.name());
            Optional<R_GLOBAL_TYPE_VALUE> t = glbRepo.findByGlbTypeValId(dataImpliRule.get().getImplicationTypeId());
            ppnData.put("type", t.get().getName());
            ppnData.put("gunggung", dataImpliRule.get().getIsGunggung());
            ppnData.put("vatInv", dataImpliRule.get().getIsVatInv());
            ppnData.put("transCode", dataImpliRule.get().getTransCode());
            addedTaxImpli.add(ppnData);

            allDataCheck.put("taxImpli", addedTaxImpli);


            // GET ALL DATA CRITERIA PPH
            List<Integer> idPPH  = new ArrayList<>();
            List<M_AM_TAXIMPLICATION> dataPPH = taxImpliRepo.findAllByCategoryAndServiceType(128, 130);
            for(M_AM_TAXIMPLICATION pph : dataPPH){
                idPPH.add(pph.getId());
            }
            List<VW_AM_TAXIMPLICATION_CRITERIA_DATA> dataCriteriaPPH = vwAmTaxImplicationCriteriaDataRepo.findIn(idPPH);

            List<CriteriaData> addedCriteriaPPH = new ArrayList<>();

            for(VW_AM_TAXIMPLICATION_CRITERIA_DATA zz : dataCriteriaPPH){
                CriteriaData data = new CriteriaData();
                data.setId(zz.getId());
                data.setAccountCategory(zz.getAccountCategory());
                data.setAccountGroupType(zz.getAccountGroupType());
                data.setAccountNumber(zz.getAccountNumber());
                data.setAccountSegment(zz.getAccountSegment());
                data.setAccountType(zz.getAccountType());
                data.setClassificationType(zz.getClassificationType());
                data.setCorporateFlag(zz.getCorporateFlagBoolean());
                data.setCostCenter(zz.getCostCenter());
                data.setIsAll(zz.getAllCriteriaBoolean());
                data.setPremiseCity(zz.getCITY());
                data.setPremiseCountry(zz.getCOUNTRY());
                data.setPremiseDistrict(zz.getDISTRICT());
                data.setPremiseProvince(zz.getPROVINCE());
                data.setPremiseSubdistrict(zz.getSUBDISTRICT());
                data.setSaType(zz.getSaType());
                data.setSor(zz.getSor());
                data.setWapuFlag(zz.getWapuFlagBoolean());

                addedCriteriaPPH.add(data);

            }

            // CHECK CRITERIA
            Integer y = CheckCriteria.checkCondition(dataAccount, addedCriteriaPPH);

            Optional<M_AM_TAXIMPLICATION_CRITERIA_DATA> dataIdTaxImpliPPH = criteriaRepo.findById(y);

            Optional<M_AM_TAXIMPLICATION> dataImpliPPH = taxImpliRepo.findById(dataIdTaxImpliPPH.get().getTaximplicationId());

            LinkedHashMap<String, Object> pphData = new LinkedHashMap<>();

            pphData.put("id", dataImpliPPH.get().getId());
            pphData.put("name", dataImpliPPH.get().getTaxImplicationName());
            Optional<R_GLOBAL_TYPE_VALUE> stt = glbRepo.findByGlbTypeValId(dataImpliPPH.get().getServiceType());
            pphData.put("serviceType", stt.get().getName());
            Optional<R_GLOBAL_TYPE_VALUE> cc = glbRepo.findByGlbTypeValId(dataImpliPPH.get().getCategory());
            pphData.put("category", cc.get().getName());
            pphData.put("description", dataImpliPPH.get().getDescription());
            Optional<M_AM_TAXIMPLICATION_RULE> dataImpliRulee = taxImpliRuleRepo.findByTaximplicationIdAndStatus(dataImpliPPH.get().getId(), FlowStatus.ACTIVE.name());
            Optional<R_GLOBAL_TYPE_VALUE> tt = glbRepo.findByGlbTypeValId(dataImpliRulee.get().getImplicationTypeId());
            pphData.put("type", tt.get().getName());
            pphData.put("gunggung", dataImpliRulee.get().getIsGunggung());
            pphData.put("vatInv", dataImpliRulee.get().getIsVatInv());
            pphData.put("transCode", dataImpliRulee.get().getTransCode());
            addedTaxImpli.add(pphData);

            //BILLING BUCKET
            List<M_RBI_BILLING_BUCKET> idActiveBillingBucket = mRbiBillingBucketRepo.findAllByStatus(FlowStatus.ACTIVE.name());
            List<String> codeBillingBucket = new ArrayList<>();
            for(M_RBI_BILLING_BUCKET code : idActiveBillingBucket) {
                codeBillingBucket.add(code.getBillingBucketCode());
            }
            codeBillingBucket = codeBillingBucket.stream().distinct().collect(Collectors.toList());
            List<M_RBI_BILLING_BUCKET_CRITERIA_DATA> dataCriteria = billingCriteriaRepo.findAllByBillingBucketCodeIn(codeBillingBucket);

                logger.info("Get Billing Bucket");

                List<CriteriaData> addedCriteria = new ArrayList<>();

                for(M_RBI_BILLING_BUCKET_CRITERIA_DATA zz : dataCriteria) {
                    if(StringUtils.hasValue(zz.getAllCriteria()) && zz.getAllCriteria().equals('Y')) {
                        CriteriaData data = new CriteriaData();
                        data.setId(zz.getId());
                        data.setAccountCategory(zz.getAccountCategory());
                        data.setAccountGroupType(zz.getAccountGroupType());
                        data.setAccountSegment(zz.getCustomerSegment());
                        data.setClassificationType(zz.getAccountClass());
                        data.setCostCenter(zz.getArea());
                        data.setIsAll(zz.getAllCriteria()!=null? zz.getAllCriteria().equals('Y')?Boolean.TRUE:Boolean.FALSE : null);
                        data.setPremiseCity(zz.getCity());
                        data.setPremiseDistrict(zz.getDistrict());
                        data.setPremiseProvince(zz.getProvince());
                        data.setPremiseSubdistrict(zz.getSubDistrict());
                        data.setSor(zz.getSor());
                        data.setAccountBudget(zz.getBudget());
                        data.setCostCenter(zz.getArea());
                        data.setAccountIndustrialSector(zz.getIndustrialSector());
                        data.setSaProductVersion(zz.getProduct());
                        data.setGsizes(zz.getGsizes());
                        addedCriteria.add(data);
                    } else if(isDateInRange(zz.getStartDate(), zz.getEndDate())) {
                        CriteriaData data = new CriteriaData();
                        data.setId(zz.getId());
                        data.setAccountCategory(zz.getAccountCategory());
                        data.setAccountGroupType(zz.getAccountGroupType());
                        data.setAccountSegment(zz.getCustomerSegment());
                        data.setClassificationType(zz.getAccountClass());
                        data.setCostCenter(zz.getArea());
                        data.setIsAll(zz.getAllCriteria()!=null? zz.getAllCriteria().equals('Y')?Boolean.TRUE:Boolean.FALSE : null);
                        data.setPremiseCity(zz.getCity());
                        data.setPremiseDistrict(zz.getDistrict());
                        data.setPremiseProvince(zz.getProvince());
                        data.setPremiseSubdistrict(zz.getSubDistrict());
                        data.setSor(zz.getSor());
                        data.setAccountBudget(zz.getBudget());
                        data.setCostCenter(zz.getArea());
                        data.setAccountIndustrialSector(zz.getIndustrialSector());
                        data.setSaProductVersion(zz.getProduct());
                        data.setGsizes(zz.getGsizes());
                        addedCriteria.add(data);
                    }
                }

                Integer bx = CheckCriteria.checkCondition(dataAccount, addedCriteria);

                if(bx.equals(0)){
                    allDataCheck.put("billingBucket", "-");
                } else {
                    Optional<M_RBI_BILLING_BUCKET_CRITERIA_DATA> dataId = billingCriteriaRepo.findById(bx);

                    List<M_RBI_BILLING_BUCKET> dataBillingBucket = billingRepo.findAllByBillingBucketCode(dataId.get().getBillingBucketCode());

                    List<LinkedHashMap<String, Object>> dataBilling = new ArrayList<>();
                    for(M_RBI_BILLING_BUCKET g : dataBillingBucket) {
                        LinkedHashMap<String, Object> bb = new LinkedHashMap<>();
                        bb.put("billingBucketCode", g.getBillingBucketCode());
                        bb.put("billingBucketName", g.getBilingBucketName());
                        bb.put("category", "-");
                        bb.put("description", g.getDescription());

                        List<LinkedHashMap<String, Object>> bdetail = detailRepo.findAll().stream()
                                .filter(d -> Objects.equals(g.getBillingBucketCode(), d.getBillingBucketCode()))
                                .map(d -> {
                                    LinkedHashMap<String, Object> ddd = new LinkedHashMap<>();
                                    ddd.put("billingCode", d.getBillingItemCode());
                                    ddd.put("billingItem", d.getBillingItemCode());
                                    ddd.put("priority", d.getPriority());
                                    ddd.put("glAccount", "-");
                                    ddd.put("type", "-");
                                    return ddd;
                                })
                                .collect(Collectors.toList());

                        bb.put("details", bdetail);
                        dataBilling.add(bb);
                    }
                    allDataCheck.put("billingBucket", dataBilling);
                }

                //ACCOUNTING RULE
                Optional<M_ACCOUNTING_RULE> mAccountingRule = accountingRulesRepo.findByMasterAccountingRuleId(dataAc.getAccountRuleId());
                if (mAccountingRule.isEmpty()){
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                            "Id not found", ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
                }
                M_ACCOUNTING_RULE dataAcRule = new M_ACCOUNTING_RULE();
                dataAcRule.setReceivableAccount(mAccountingRule.get().getCode() + " - " + mAccountingRule.get().getClassificationTypeName() + " - " + mAccountingRule.get().getReceivableAccount());
                dataAcRule.setRevenueAccount(mAccountingRule.get().getCode() + " - " + mAccountingRule.get().getClassificationTypeName() + " - " + mAccountingRule.get().getRevenueAccount());

                allDataCheck.put("accountingRule", dataAcRule);

            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get ALL", allDataCheck), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @Transactional(rollbackFor = Exception.class, readOnly = false)
    @SuppressWarnings("java:S1192")
    public ResponseEntity<ResponseObject> getListOneTime(MaterialTablePagingRequest pagingData,
			PagedResourcesAssembler<VW_CUSTOMER_INFORMATION> assembler) {
            logger.info("Get List Customer One Time");
            try {
                Map<String, Object> filter = new HashMap<>();
                filter.put("customerType", "One Time");
                filter.put("identificationType", "One Time");
                filter.put("entityId", UserDetailUtils.getUserEntity());

                logger.info("entityId : " + UserDetailUtils.getUserEntity().toString());

                Specification<VW_CUSTOMER_INFORMATION> specification = pagingData.getSearch().isEmpty()
                        ? vwCustomerInformationRepo.getSpecificationDefault(filter)
                        : vwCustomerInformationRepo.getSpecificationFromFilters(pagingData, filter);

                Page<VW_CUSTOMER_INFORMATION> data = vwCustomerInformationRepo.findAll(specification, PagingUtils.getPaging(pagingData));

                List<LinkedHashMap<String, Object>> allData = data.getContent().stream()
                    .map(g -> {
                        LinkedHashMap<String, Object> vwCus = new LinkedHashMap<>();
                        vwCus.put("customerId", g.getCustomerId());
                        vwCus.put("customerName", g.getCustomerName());
                        vwCus.put("customerNumber", g.getCustomerNumber());
                        vwCus.put("customerTypeId", g.getCustomerTypeId());
                        vwCus.put("identificationNumber", g.getCustomerIdentificationNumber());
                        vwCus.put("identificationTypeId", g.getIdentificationTypeId());
                        vwCus.put("status", g.getStatus());
                        Optional<M_POSITION> mPosition = mPositionRepo.findByPositionIdAndStatusAndIsDeleted(g.getPositionId(),FlowStatus.ACTIVE.name(),false);
                        vwCus.put("customerManagement", mPosition.isPresent()? mPosition.get().getName() : null);

                        List<LinkedHashMap<String, Object>> allAccount = vwAccountRepo.findAll().stream()
                            .filter(d -> Objects.equals(g.getCustomerId(), d.getCustomerId()))
//                            .filter(d -> Objects.equals(d.getAccountGroup(), "One Time"))
                            .map(d -> {
                                LinkedHashMap<String, Object> dd = new LinkedHashMap<>();
                                dd.put("accountId", d.getAccountId());
                                dd.put("accountNumber", d.getAccountNumber());
                                dd.put("accountName", d.getAccountName());
                                dd.put("accountSegment", d.getAccountSegment());
                                dd.put("accountGroupType", d.getAccountGroupType());
                                return dd;
                            })
                            .collect(Collectors.toList());
                        vwCus.put("accountList", allAccount);
                        return vwCus;
                    })
                    .collect(Collectors.toList());

            PagedModel<EntityModel<VW_CUSTOMER_INFORMATION>> pagedData = assembler.toModel(data);
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

    public ResponseEntity<ResponseObject> getDetailCustomerOneTime(Integer customerId) {
        ResponseObject result = new ResponseObject();
        logger.info("Get Detail Customer");
        try {
            LinkedHashMap<String, Object> data = new LinkedHashMap<>();
            M_CUSTOMER g = mCustomerRepo.findByCustomerId(customerId);
            Optional<VW_CUSTOMER_INFORMATION> h = vwCustomerInformationRepo.findTopByCustomerId(customerId);
            data.put("customerId", g.getCustomerId());
            data.put("customerName", g.getCustomerName());
            data.put("customerNumber", g.getCustomerNumber());
            data.put("customerType", h.get().getCustomerType());
            data.put("foundedBirthDate", g.getFoundedBirthDate());
            data.put("foundedBirthPlace", g.getFoundedBirthPlace());
            data.put("searchKey", g.getSearchKey());
            data.put("sex", h.get().getSex());
            data.put("identificationType", h.get().getIdentificationType());
            data.put("customerIdentificationNumber", g.getCustomerIdentificationNumber());
            data.put("description", g.getDescription());
            data.put("status", g.getStatus());

            List<LinkedHashMap<String, Object>> allDetail = new ArrayList<>();
            List<M_ACCOUNT> allAccount = mAccountRepo.findAllByCustomerId(g.getCustomerId());
            List<VW_ACCOUNT> allVwAccount = vwAccountRepo.findAllByCustomerId(g.getCustomerId());
            for(VW_ACCOUNT f : allVwAccount) {
                for (M_ACCOUNT d : allAccount) {
                    LinkedHashMap<String, Object> dd = new LinkedHashMap<>();
                    dd.put("accountId", d.getAccountId());
                    dd.put("registrationNumber", d.getRegistrationNumber());
                    dd.put("accountNumber", d.getAccountNumber());
                    dd.put("accountName", d.getAccountName());
                    dd.put("accountSegment", f.getAccountSegment());
                    dd.put("accountGroupType", f.getAccountGroupType());
                    dd.put("accountCategory", f.getAccountCategory());
                    dd.put("classificationType", f.getAccountRuleId());
                    dd.put("accountType", f.getAccountType());
                    dd.put("description", d.getDescription());
                    dd.put("status", d.getStatus());
                    dd.put("sor", f.getSor());
                    dd.put("costCenter", f.getCostCenter());
                    dd.put("meterReadingCode", f.getMeterReadingCode());
                    dd.put("budgetYear", d.getBudgetYear());
                    dd.put("budget", d.getBudget());
                    dd.put("teritory", f.getTeritory());
                    dd.put("industrialSector", d.getIndustrialSector());
                    dd.put("isCorporate", d.getIsCorporate());
                    dd.put("priority", f.getPriority());
                    dd.put("isException", d.getIsException());
                    dd.put("accountGroup", d.getAccountGroup());
                    dd.put("syncFlag", d.getSyncFlag());
                    dd.put("accountReferenceId", d.getAccountReferenceId());
                    allDetail.add(dd);
                }
            }

            data.put("accountList", allDetail);

            data.put("status", g.getStatus());
            data.put("createdDate", g.getCreatedDate());
            data.put("createdBy", g.getCreatedBy());
            data.put("updatedDate", g.getUpdatedDate());
            data.put("updatedBy", g.getUpdatedBy());

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, "Success get data", data);
                    logger.info("Response Success ->" + result);
                    return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean isDateInRange(Date startDate, Date endDate) {
        Date currentDate = new Date();
        if(startDate==null) {
            return false;
        } else if(endDate==null) {
            return currentDate.after(startDate);
        } else {
            return currentDate.after(startDate) && currentDate.before(endDate);
        }
    }

    //acr
    public ResponseEntity<ResponseObject> uploadFile(Integer categoryId, MultipartFile file, Integer refId) {
        ResponseObject result = new ResponseObject();

        try {
            String generatedFileName = UserDetailUtils.generateFileName(file.getOriginalFilename());
            M_ATTACHMENT mAttachment = new M_ATTACHMENT();
            mAttachment.setCategory("ACCOUNT_ATTACHMENT");
            mAttachment.setFileCategoryId(categoryId);
            mAttachment.setReferenceId(refId);
            mAttachment.setType(file.getContentType());
            mAttachment.setCreatedBy(UserDetailUtils.getUsername());
            mAttachment.setCreatedDate(new Date());
            mAttachment.setPathFile("PATH_20");
            mAttachment.setFileName(generatedFileName);
            mAttachment.setFileSize(file.getSize());
            mAttachment.setIsDraft(Boolean.TRUE);
            mAttachment.setIsDeleted(Boolean.FALSE);

            M_ATTACHMENT saveAttachment = this.mAttachmentRepo.save(mAttachment);
            if (ObjectUtils.isEmpty(saveAttachment)){
                result.setSuccess(false);
                result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
                result.setMessage("Account attachment not Created");
                result.setData(ResponseUtils.DATA_EMPTY);

                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            R_GLOBAL_TYPE_VALUE rGlobalTypeValue = rGlobalTypeValueRepo
                    .findTopByGlbValueIgnoreCaseAndIsDeleted("PATH_20", false);
            String fullPath = rGlobalTypeValue.getName() + generatedFileName;

            String fullobject = "FILE" + fullPath;
            this.minioClient.putObject(this.configurationProperties.getBucket(), fullobject,
                    file.getInputStream(), file.getContentType());

            Map<String, Object> responseSuccessData = new HashMap<>();
            responseSuccessData.put("attachmentId",saveAttachment.getId());
            responseSuccessData.put("attachmentFileName",saveAttachment.getFileName());
            result.setSuccess(true);
            result.setCode(HttpStatus.CREATED);
            result.setMessage("Account attachment Created");
            result.setData(responseSuccessData);

            return new ResponseEntity<>(result, HttpStatus.OK);

        }catch (Exception e){
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //acr
    public ResponseEntity<ResponseObject> getListAttachment(Integer referenceId) {
        try {
            List<M_ATTACHMENT> listAttachment = mAttachmentRepo.findAllByReferenceId(referenceId);
            List<M_ATTACHMENT> dataAttachment = new ArrayList<>();
            for(M_ATTACHMENT attach : listAttachment) {
                if(attach.getCategory().equalsIgnoreCase("ACCOUNT_ATTACHMENT")) {
                    dataAttachment.add(attach);
                }
            }
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get view paging", dataAttachment), HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
