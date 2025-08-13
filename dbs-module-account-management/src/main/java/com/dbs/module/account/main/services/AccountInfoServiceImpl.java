package com.dbs.module.account.main.services;

import com.dbs.module.account.main.dto.accountinformation.ListOfValueDto;
import com.dbs.module.account.main.dto.accountinformation.UpdateCustomerDTO;
import com.dbs.module.account.main.dto.accountinformation.FilterRequestDTO;
import com.dbs.module.account.main.dto.accountinformation.ActiveInactiveDTO;
import com.dbs.module.account.main.dto.accountinformation.SearchFilterDTO;
import com.dbs.module.account.main.dto.accountinformation.CustomerInformationDto;
import com.dbs.common.base.utils.*;
import com.dbs.common.library.config.jwt.JwtUtils;
import com.dbs.common.library.ctrl.PagingDTO;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.*;
import com.dbs.database.crm.entities.accountmanagement.*;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_ACCOUNT_INFORMATION;
import com.dbs.database.crm.entities.usermanagement.*;
import com.dbs.database.crm.repositories.accountmanagement.Account.*;
import com.dbs.database.crm.repositories.accountmanagement.MAttachmentAccountRepo;
import com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement.TAmSARepo;
import com.dbs.database.crm.repositories.accountmanagement.VWAccountInfoRepo;
import com.dbs.database.crm.repositories.accountmanagement.VwCusInfoCcRepo;
import com.dbs.database.crm.repositories.usermanagement.*;
import com.dbs.database.crm.utils.CostCenterUtils;
import com.dbs.module.account.main.dto.UpdateAccountStandartDto;
import com.dbs.module.account.utils.ConstantAccount;
import com.dbs.module.account.utils.UtilsAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jlefebure.spring.boot.minio.MinioConfigurationProperties;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.dbs.common.base.utils.CommonHelper.removeSpace;
import static com.dbs.common.base.utils.Constant.GET_CC_CHILD;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class AccountInfoServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(AccountInfoServiceImpl.class);
    @Autowired
    private VWAccountInfoRepo vwAccInfoRepo;
    @Autowired
    private MAccountRepo accountRepo;
    @Autowired
    private MCustomerRepo mCustomerRepo;
    @Autowired
    private VWCustomerAddressRepo vwCustomerAddressRepo;
    @Autowired
    private MUserRepo mUserRepo;
    @Autowired
    private AuditTrailRepo auditTrailRepo;
    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;
    @Autowired
    private VwCustomerContactRepo vwCustomerContactRepo;
    @Autowired
    private Validator validator;
    @Autowired
    private MGlobalTypeRepo mGlobalTypeRepo;
    @Autowired
    private VwCustomerInformationRepo vwCustomerInformationRepo;
    @Autowired
    private MAttachmentAccountRepo mAttachmentAccountRepo;
    @Autowired
    private MAttachmentRepo mAttachmentRepo;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioConfigurationProperties configurationProperties;
    @Autowired
    private MContactDetailsRepo cdRepo;
    @Autowired
    private TAmSARepo tAmSARepo;
    @Autowired
    private MCustomerCostCenterRepo mCustomerCostCenterRepo;
    @Autowired
    private VwCusInfoCcRepo vwCusInfoCcRepo;
    @Autowired
    private MCostCenterRepo ccRepo;
    @Autowired
    private CostCenterUtils costCenterUtils;
    @Autowired
    private MCustomerEntityRepo mCustomerEntityRepo;
    @Autowired
    private MAccountCustomerManagementRepo mAccountCMRepo;
    @Autowired
    private MPositionRepo mPositionRepo;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private VwCustomerContactRepo vcRepo;
    @Autowired
    private CreateParty party;
    @Autowired
    private GlobalTypeValueService globalTypeService;
    @Autowired
    private MUserRepo userRepo;
    @Autowired
    private GlobalTypeValueService globalTypeValueService;
    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("java:S1192")
    private final List<String> customerColumn = Arrays.asList("customerName","customerNumber","customerType","foundedBirthDate","firstName","identificationType","lastName","maritalStatus","middleName","foundedBirthPlace","searchKey","sex","customerIdentificationNumber","description");
    @SuppressWarnings("java:S1192")
    private final List<String> accountColumn = Arrays.asList("registrationNumber","accountGroup","accountNumber","accountName","accountSegment","accountGroupType","accountCategory","classificationType","accountType","paymentChannel","accountDescription","sor","costCenter","meterReadingCode","industrialSector","budgetYear","budget","teritory","taxIdentifierType","taxIdentifierNumber","taxIdentifierName","taxIdentifierAddress","taxRelationIdentifierType","taxRelationIdentifierNumber","taxRelationIdentifierName","taxRelationIdentifierAddress","priority","isCorporate","isException","isBadDebt","isSync","accountReferenceId","accountStatus","customerManagement","customerDescription","createdDate","createdBy","updatedDate","updatedBy");
    @SuppressWarnings("java:S1192")
    public ResponseEntity<ResponseObject> pagingAccount(MaterialTablePagingRequest pagingData,
                                                        PagedResourcesAssembler<VW_ACCOUNT_INFORMATION> assembler,
                                                        String accountGroup, HttpServletRequest request) {
        try {
            Map<String, Object> filter = new HashMap<>();
            filter.put(Constant.ACCOUNT_GROUP, accountGroup);
            filter.put(Constant.UNIQUE_ACCOUNT, "USE");
            filter.put(Constant.ENTITY_ID, UserDetailUtils.getUserEntity());

            // super user
            boolean isItSuperUser = isItSuperUser(Integer.parseInt(UserDetailUtils.getUserId()));

            if(!isItSuperUser){
                Optional<M_USER> user = userRepo.findByUsername(UserDetailUtils.getUsername());
                Optional<M_POSITION> cekPosition = (user.get().getUserType().equalsIgnoreCase("EMP")?mPositionRepo.findById(UserDetailUtils.getPositionFromToken(request)): Optional.empty());

                // admin entity
                if(user.get().getUserLevel().equalsIgnoreCase("AE")) {
                    List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(UserDetailUtils.getPositionFromToken(request), GET_CC_CHILD);
                    filter.put("costCenterId", ccList);
                    logger.info("AE costCenterId : " + ccList);
                }
                // end user (CM)
                else if(cekPosition.isPresent() && Pattern.matches(".*\\bCM\\b.*", cekPosition.get().getName())) {
                    filter.put("customerManagementId", UserDetailUtils.getPositionFromToken(request));
                    logger.info("customerManagementId : " + UserDetailUtils.getPositionFromToken(request).toString() + " - " + cekPosition.get().getName());
                }
                // end user (employee)
                else if(user.get().getUserType().equalsIgnoreCase("EMP")) {
                    List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(UserDetailUtils.getPositionFromToken(request), GET_CC_CHILD);
                    filter.put("costCenterId", ccList);
                    logger.info("costCenterId : " + ccList);
                }
                // end user (non employee)
                else if(user.get().getUserType().equalsIgnoreCase("NON_EMP")) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, "Not employee", null), HttpStatus.OK);
                }
            }

            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key+"~"+value);
                }
            }

            Specification<VW_ACCOUNT_INFORMATION> specification = pagingData.getSearch().isEmpty()
                    ? vwAccInfoRepo.getSpecificationDefault2(filter)
                    : vwAccInfoRepo.getSpecificationFromFilters2(pagingData, filter);

            Page<VW_ACCOUNT_INFORMATION> data = vwAccInfoRepo.findAll(specification, PagingUtils.getPaging(pagingData));

            PagedModel<EntityModel<VW_ACCOUNT_INFORMATION>> pagedData = assembler.toModel(data);
            Map<String, Object> d = new HashMap<>();
            d.put(Constant.RESULT, data.getContent());
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
    @SuppressWarnings({"java:S1192","java:S3776"})
    public ResponseEntity<ResponseObject> pagingCustomer(SearchFilterDTO dto, MaterialTablePagingRequest pagingData,
                                                         HttpServletRequest request,
                                                         PagedResourcesAssembler<VW_CUS_INFO_CC> assemblerDto) {
        ResponseObject result = new ResponseObject();
        try {
            Map<String, Object> filter = new HashMap<>();
            Map<String, Object> filterAccount = new HashMap<>();
            Page<VW_CUS_INFO_CC> data;
            List<VW_ACCOUNT_INFORMATION> listAccountData = null;
            boolean isItSuperUser = isItSuperUser(Integer.parseInt(UserDetailUtils.getUserId()));
            logger.info("current entity --> " + UserDetailUtils.getUserEntity());
            filter.put("entityId", UserDetailUtils.getUserEntity());
            if(!isItSuperUser){
                Optional<M_POSITION> cekPosition = mPositionRepo.findById(UserDetailUtils.getPositionFromToken(request));
                if(cekPosition.isPresent() && cekPosition.get().getName().contains("CM ")) {
                    filter.put("positionId", UserDetailUtils.getPositionFromToken(request));
                    filter.put("pagingCustomerCm", "USE");
                    logger.info("current positionId --> " + UserDetailUtils.getPositionFromToken(request));
                } else {
                    filter.put("accountCostCenterId", UserDetailUtils.getPositionFromToken(request));
                    filter.put("pagingCustomerHead", "USE");
                    logger.info("current accountCostCenterId --> " + UserDetailUtils.getPositionFromToken(request));
                }

                // FILTER ACCOUNT
                filterAccount.put(Constant.UNIQUE_ACCOUNT, "USE");
                Optional<M_USER> user = userRepo.findByUsername(UserDetailUtils.getUsername());
                Optional<M_POSITION> cekPositionAccount = (user.get().getUserType().equalsIgnoreCase("EMP")?mPositionRepo.findById(UserDetailUtils.getPositionFromToken(request)): Optional.empty());
                filterAccount.put(Constant.ENTITY_ID, UserDetailUtils.getUserEntity());
                // admin entity
                if(user.get().getUserLevel().equalsIgnoreCase("AE")) {
                    List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(UserDetailUtils.getPositionFromToken(request), GET_CC_CHILD);
                    filterAccount.put("costCenterId", ccList);
                    logger.info("AE costCenterId : " + ccList);
                }
                // end user (CM)
                else if(cekPositionAccount.isPresent() && Pattern.matches(".*\\bCM\\b.*", cekPositionAccount.get().getName())) {
                    filterAccount.put("customerManagementId", UserDetailUtils.getPositionFromToken(request));
                    logger.info("customerManagementId : " + UserDetailUtils.getPositionFromToken(request).toString() + " - " + cekPositionAccount.get().getName());
                }
                // end user (employee)
                else if(user.get().getUserType().equalsIgnoreCase("EMP")) {
                    List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(UserDetailUtils.getPositionFromToken(request), GET_CC_CHILD);
                    filterAccount.put("costCenterId", ccList);
                    logger.info("costCenterId : " + ccList);
                }
                // end user (non employee)
                else if(user.get().getUserType().equalsIgnoreCase("NON_EMP")) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, "Not employee", null), HttpStatus.OK);
                }
            }

            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key+"~"+value);
                }
            }

            Specification<VW_CUS_INFO_CC> specification = pagingData.getSearch().isEmpty()
                    ? vwCusInfoCcRepo.getSpecificationDefault(filter)
                    : vwCusInfoCcRepo.getSpecificationFromFilters(pagingData, filter);

            Specification<VW_ACCOUNT_INFORMATION> specificationAccount;

            // ADVANCED FILTER
            List<FilterRequestDTO> filterList= dto.getInputFields();
            List<AdvanceFilter> advanceFilterCustomer = new ArrayList<>();
            List<AdvanceFilter> advanceFilterAccount = new ArrayList<>();
            if(!filterList.isEmpty() && !ObjectUtils.isEmpty(filterList.get(0).getColumn())){
                int i = 0 ;
                for(FilterRequestDTO frd: filterList){
                    AdvanceFilter advanceFilter = new AdvanceFilter();
                    Optional<R_GLOBAL_TYPE_VALUE> columnOpt = rGlobalTypeValueRepo.findByGlbTypeValId(frd.getColumn());
                    Optional<R_GLOBAL_TYPE_VALUE> operatorOpt = rGlobalTypeValueRepo.findByGlbTypeValId(frd.getOperator());
                    if(i ==0){
                        advanceFilter.setCondition("AND");
                    }else{
                        Optional<R_GLOBAL_TYPE_VALUE> conditionOpt = rGlobalTypeValueRepo.findByGlbTypeValId(frd.getCondition());
                        if(conditionOpt.isPresent()){
                            advanceFilter.setCondition(conditionOpt.get().getGlbValue());
                        }
                    }

                    if(operatorOpt.isPresent()){
                        advanceFilter.setOperator(operatorOpt.get().getGlbValue());
                    }
                    advanceFilter.setValue(frd.getValue());
                    if(columnOpt.isPresent()){
                        advanceFilter.setColumn(columnOpt.get().getGlbValue());
                        if(this.customerColumn.contains(columnOpt.get().getGlbValue())){
                            advanceFilterCustomer.add(advanceFilter);
                        }else if(this.accountColumn.contains(columnOpt.get().getGlbValue())){
                            advanceFilterAccount.add(advanceFilter);
                        }
                    }
                    i++;
                }

                // ACCOUNT
                if(!advanceFilterAccount.isEmpty()){
                    specificationAccount = vwAccInfoRepo.getSpecificationFromAdvanceFilters(advanceFilterAccount, filterAccount);
                    listAccountData = vwAccInfoRepo.findAll(specificationAccount);
                    List<Integer> customerIdFiltering = listAccountData.stream()
                            .map(VW_ACCOUNT_INFORMATION::getCustomerId)
                            .distinct()
                            .collect(Collectors.toList());
                    specification = vwCusInfoCcRepo.getSpecificationFromAdvanceFiltersWithAccount(advanceFilterCustomer, specification, customerIdFiltering);
                } else {
                    specification = vwCusInfoCcRepo.getSpecificationFromAdvanceFilters(advanceFilterCustomer, specification);
                }
            }

            data = vwCusInfoCcRepo.findAll(specification, PagingUtils.getPaging(pagingData));
            List<CustomerInformationDto> listData = new ArrayList<>();

            for(VW_CUS_INFO_CC c : data.getContent()) {
                CustomerInformationDto customerInformationDto = new CustomerInformationDto();
                customerInformationDto.setCustomerId(c.getCustomerId());
                customerInformationDto.setCustomerName(c.getCustomerName());
                customerInformationDto.setCustomerNumber(c.getCustomerNumber());
                customerInformationDto.setCustomerType(c.getCustomerType());
                customerInformationDto.setFoundedBirthDate(c.getFoundedBirthDate());
                customerInformationDto.setFirstName(c.getFirstName());
                customerInformationDto.setIdentificationType(c.getIdentificationType());
                customerInformationDto.setLastName(c.getLastName());
                customerInformationDto.setMaritalStatus(c.getMaritalStatus());
                customerInformationDto.setMiddleName(c.getMiddleName());
                customerInformationDto.setFoundedBirthPlace(c.getFoundedBirthPlace());
                customerInformationDto.setSearchKey(c.getSearchKey());
                customerInformationDto.setSex(c.getSex());
                customerInformationDto.setCustomerIdentificationNumber(c.getCustomerIdentificationNumber());
                customerInformationDto.setCmPositionId(c.getCmPositionId());
                customerInformationDto.setDescription(c.getDescription());
                customerInformationDto.setEntityId(c.getEntityId());
                customerInformationDto.setCustomerManagement(c.getCustomerManagement());
                customerInformationDto.setCcId(c.getCcId());
                customerInformationDto.setPositionId(c.getPositionId());
                customerInformationDto.setCreatedBy(c.getCreatedBy());
                customerInformationDto.setUpdatedBy(c.getUpdatedBy());
                customerInformationDto.setStatus(c.getStatus());
                customerInformationDto.setCreatedDate(c.getCreatedDate());
                customerInformationDto.setUpdatedDate(c.getUpdatedDate());

                // LIST ACCOUNT
                filterAccount.put("customerId", c.getCustomerId());

                if(!advanceFilterAccount.isEmpty()){
                    specificationAccount = vwAccInfoRepo.getSpecificationFromAdvanceFilters(advanceFilterAccount, filterAccount);
                    listAccountData = vwAccInfoRepo.findAll(specificationAccount);
                }else{
                    specificationAccount = vwAccInfoRepo.getSpecificationDefault2(filterAccount);
                    listAccountData = vwAccInfoRepo.findAll(specificationAccount);
                }
                customerInformationDto.setAllAccount(listAccountData);

                if(!advanceFilterAccount.isEmpty() && listAccountData.isEmpty()) {
                    continue;
                } else {
                    listData.add(customerInformationDto);
                }
            }
            PagedModel pagedData = assemblerDto.toModel(data);

            Map<String, Object> d = new HashMap<>();
            d.put(Constant.RESULT, listData);
            d.put(Constant.PAGE, pagedData.getMetadata());
            d.put(Constant.LINK, pagedData.getLinks());

            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success Get List Customer");
            result.setData(d);

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
    public ResponseEntity<ResponseObject> detailCustomer(Integer customerId, HttpServletRequest request) {
        try {
            Optional<VW_CUS_INFO_CC> customer = vwCusInfoCcRepo.findTopByCustomerId(customerId);
            if (customer.isPresent()) {

                VW_CUS_INFO_CC c = customer.get();
                LinkedHashMap<String, Object> data = new LinkedHashMap<>();

                // CEK AKSES UPDATE
                Optional<M_CUSTOMER_ENTITY> cekEntity = mCustomerEntityRepo.findByCustomerId(customerId);
                if(cekEntity.isPresent()){
                    Optional<M_CUSTOMER_COST_CENTER> cekCostCenter = mCustomerCostCenterRepo.findBymCustomerEntityId(cekEntity.get().getCustomerEntityId());
                    if(cekCostCenter.isPresent()){
                        if(cekCostCenter.get().getCostCenterId().equals(costCenterUtils.findCostCenterByPositionId(UserDetailUtils.getPositionFromToken(request))) && cekEntity.get().getEntityId().equals(UserDetailUtils.getUserEntity())){
                            data.put("isEditor", true);
                        } else {
                            data.put("isEditor", false);
                        }

                        logger.info("cc : " + costCenterUtils.findCostCenterByPositionId(UserDetailUtils.getPositionFromToken(request)).toString());
                        logger.info("entity : " + UserDetailUtils.getUserEntity().toString());
                    } else {
                        data.put("isEditor", false);
                    }
                } else {
                    data.put("isEditor", false);
                }

                data.put("customerId", c.getCustomerId());
                data.put("customerNumber", c.getCustomerNumber());
                Optional<R_GLOBAL_TYPE_VALUE> identificationType = rGlobalTypeValueRepo.findByGlbTypeValId(c.getIdentificationTypeId());
                data.put("identificationTypeId", identificationType.isPresent() ? identificationType.get().getGlbTypeValId() : "");
                data.put("identificationType", identificationType.isPresent() ? identificationType.get().getGlbValue() : "");
                data.put("customerIdentificationNumber", c.getCustomerIdentificationNumber());
                data.put("firstName", c.getFirstName());
                data.put("middleName", c.getMiddleName());
                data.put("lastName", c.getLastName());
                data.put("customerName", c.getCustomerName());
                Optional<R_GLOBAL_TYPE_VALUE> customerType = rGlobalTypeValueRepo.findByGlbTypeValId(c.getCustomerTypeId());
                data.put("customerTypeId", customerType.isPresent() ? customerType.get().getGlbTypeValId() : "");
                data.put("customerType", customerType.isPresent() ? customerType.get().getGlbValue() : "");
                data.put("status", c.getStatus());
                data.put("foundedBirthPlace", c.getFoundedBirthPlace());
                data.put("foundedBirthDate", c.getFoundedBirthDate());
                Optional<R_GLOBAL_TYPE_VALUE> sex = rGlobalTypeValueRepo.findByGlbTypeValId(c.getSexId());
                data.put("sexId", sex.isPresent() ? sex.get().getGlbTypeValId() : "");
                data.put("sex", sex.isPresent() ? sex.get().getName() : "");
                data.put("searchKey", c.getSearchKey());
                data.put("maritalStatusId", c.getMaritalStatusId());
                Optional<R_GLOBAL_TYPE_VALUE> maritalStatus = rGlobalTypeValueRepo.findByGlbTypeValId(c.getMaritalStatusId());
                data.put("maritalStatus", maritalStatus.isPresent() ? maritalStatus.get().getGlbValue() : "");
                Optional<M_POSITION> mPosition = mPositionRepo.findByPositionIdAndStatusAndIsDeleted(c.getPositionId(),FlowStatus.ACTIVE.name(),false);
                data.put("customerManagement", mPosition.isPresent()? mPosition.get().getName():null);

                data.put("description", c.getDescription());
                data.put("recordId", c.getCustomerId());
                data.put("createdDate", c.getCreatedDate());
                data.put("createdBy", c.getCreatedBy());
                data.put("updatedDate", c.getUpdatedDate());
                data.put("updatedBy", c.getUpdatedBy());

                //acr
                List<AUDIT_TRAIL> auditTrail = auditTrailRepo.findAll().stream()
                        .filter(e -> e.getTableName().equalsIgnoreCase("M_CUSTOMER"))
                        .filter(f -> f.getDataId().equalsIgnoreCase(c.getCustomerId().toString()))
                        .collect(Collectors.toList());
                data.put("activeInactiveLog", auditTrail);

                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        ResponseUtils.MESSAGE_OK, data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
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
    public ResponseEntity<ResponseObject> detailAccount(Integer customerId, Integer accountId, String accountGroup, HttpServletRequest request) {
        try {
            Optional<VW_ACCOUNT_INFORMATION> accountInformation = vwAccInfoRepo.findByCustomerIdAndAccountId(customerId, accountId);
            if (accountInformation.isEmpty()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
            }
            VW_ACCOUNT_INFORMATION data = accountInformation.get();
            if ("standard".equalsIgnoreCase(accountGroup)) {
                if (data.getAccountGroup().equalsIgnoreCase("Standard")) {

                    LinkedHashMap<String, Object> accountSummary = new LinkedHashMap<>();
                    accountSummary.put("customerNumber", data.getCustomerNumber());
                    accountSummary.put("customerIdentificationType", data.getCustomerIdentificationType());
                    accountSummary.put("customerIdentificationNumber", data.getCustomerIdentificationNumber());
                    accountSummary.put("customerName", data.getCustomerName());
                    accountSummary.put("customerType", data.getCustomerType());
                    accountSummary.put("description", data.getCustomerDescription());
                    accountSummary.put("customerStatus", data.getCustomerStatus());
                    accountSummary.put("accountNumber", data.getAccountNumber());
                    accountSummary.put("registrationNumber", data.getRegistrationNumber());
                    accountSummary.put("accountName", data.getAccountName());
                    accountSummary.put("accountCategory", data.getAccountCategory());
                    accountSummary.put("sor", data.getSor());
                    accountSummary.put("costCenter", data.getCostCenter());
                    accountSummary.put("meterReadingCodes", data.getMeterReadingCode());
                    accountSummary.put("customerManagement", data.getCustomerManagement());
                    accountSummary.put("classificationType", data.getClassificationType());
                    accountSummary.put("segment", data.getAccountSegment());
                    accountSummary.put("accountGroupType", data.getAccountGroupType());
                    accountSummary.put("accountStatus", data.getAccountStatus());
                    accountSummary.put("birthFoundedDate", data.getFoundedBirthDate());
                    accountSummary.put("birthFoundedPlace", data.getFoundedBirthPlace());
                    accountSummary.put("sex", data.getSex());
                    accountSummary.put("maritalStatus", data.getMaritalStatus());
                    accountSummary.put("searchKey", data.getSearchKey());


                    LinkedHashMap<String, Object> accountInfo = new LinkedHashMap<>();

                    // CEK AKSES UPDATE
                    Optional<M_ACCOUNT_CUSTOMER_MANAGEMENT> cekPosition = mAccountCMRepo.findByAccountIdAndStatus(accountId, FlowStatus.ACTIVE.name());
                    if(cekPosition.isPresent()){
                        String tokenFromHeader = jwtUtils.parseJwt(request);
                        Integer currentPosition = jwtUtils.getPositionFromJwtToken(tokenFromHeader);
                        Optional<M_POSITION> position = mPositionRepo.findByPositionId(currentPosition);
                        logger.info("position : " + position.get().getPositionId().toString());
                        if(cekPosition.get().getPositionId().equals(position.get().getPositionId())){
                            accountInfo.put("isEditor", true);
                        } else {
                            accountInfo.put("isEditor", false);
                        }
                    } else {
                        accountInfo.put("isEditor", false);
                    }

                    accountInfo.put("accountId", data.getAccountId());
                    accountInfo.put("accountGroup", data.getAccountGroup());
                    accountInfo.put("accountGroupId", data.getAccountGroupId());
                    accountInfo.put("accountReferenceId", data.getAccountReferenceId());
                    accountInfo.put("customerManagement", data.getCustomerManagement());
                    accountInfo.put("sor", data.getSor());
                    accountInfo.put("sorId", data.getSorId());
                    accountInfo.put("costCenter", data.getCostCenter());
                    accountInfo.put("costCenterId", data.getCostCenterId());
                    accountInfo.put("meterReadingCodes", data.getMeterReadingCode());
                    accountInfo.put("accountNumber", data.getAccountNumber());
                    accountInfo.put("registrationNumber", data.getRegistrationNumber());
                    accountInfo.put("accountName", data.getAccountName());
                    accountInfo.put("categoryId", data.getAccountCategoryId());
                    accountInfo.put("category", data.getAccountCategory());
                    accountInfo.put("description", data.getAccountDescription());
                    accountInfo.put("status", data.getAccountStatus());
                    accountInfo.put("segmentId", data.getAccountSegmentId());
                    accountInfo.put("segment", data.getAccountSegment());
                    accountInfo.put("accountGroupTypeId", data.getAccountGroupTypeId());
                    accountInfo.put("accountGroupType", data.getAccountGroupType());
                    accountInfo.put("accountTypeId", data.getAccountTypeId());
                    accountInfo.put("accountType", data.getAccountType());
                    accountInfo.put("classificationTypeId", data.getClassificationTypeId());
                    accountInfo.put("classificationType", data.getClassificationType());

                    R_GLOBAL_TYPE_VALUE priorityId = rGlobalTypeValueRepo.findTopByGlbValueIgnoreCaseAndIsDeleted(data.getPriority(), Boolean.FALSE);
                    accountInfo.put("priorityId", ObjectUtils.isEmpty(priorityId) ? "" : priorityId.getGlbTypeValId());
                    accountInfo.put("priority", data.getPriority());

                    if(data.getIsCorporate().equalsIgnoreCase("Yes")){
                        accountInfo.put("corporateCustomer", Boolean.TRUE);
                    }else{
                        accountInfo.put("corporateCustomer", Boolean.FALSE);
                    }

                    if (data.getIsException().equalsIgnoreCase("Yes")) {
                        accountInfo.put("ratingAndBillingException", Boolean.TRUE);
                    } else {
                        accountInfo.put("ratingAndBillingException", Boolean.FALSE);
                    }

                    accountInfo.put("industrialSectorId", data.getIndustrialSectorId());
                    accountInfo.put("industrialSector", data.getIndustrialSector());

                    accountInfo.put("budgetYearId", data.getBudgetYearId());
                    accountInfo.put("budgetYear", data.getBudgetYear());

                    accountInfo.put("budgetId", data.getBudgetId());
                    accountInfo.put("budget", data.getBudget());
                    accountInfo.put("teritoryId", data.getTeritoryId());
                    accountInfo.put("teritory", data.getTeritory());

                    accountInfo.put("recordId", customerId);
                    accountInfo.put("createdDate", data.getCreatedDate());
                    accountInfo.put("createdBy", data.getCreatedBy());
                    accountInfo.put("updatedDate", data.getUpdatedDate());
                    accountInfo.put("updatedBy", data.getUpdatedBy());

                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                    response.put("accountSummary", accountSummary);
                    response.put("accountInformation", accountInfo);
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            ResponseUtils.MESSAGE_OK, response), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                            ResponseUtils.MESSAGE_NOT_FOUND, ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
                }
            } else if ("One Time".equalsIgnoreCase(accountGroup)) {
                if (data.getAccountGroup().equalsIgnoreCase("One Time")) {

                    LinkedHashMap<String, Object> accountSummary = new LinkedHashMap<>();
                    accountSummary.put("customerNumber", data.getCustomerNumber());
                    accountSummary.put("customerIdentificationType", data.getCustomerIdentificationType());
                    accountSummary.put("customerIdentificationNumber", data.getCustomerIdentificationNumber());
                    accountSummary.put("customerName", data.getCustomerName());
                    accountSummary.put("customerType", data.getCustomerType());
                    accountSummary.put("description", data.getCustomerDescription());
                    accountSummary.put("customerStatus", data.getCustomerStatus());
                    accountSummary.put("accountNumber", data.getAccountNumber());
                    accountSummary.put("registrationNumber", data.getRegistrationNumber());
                    accountSummary.put("accountName", data.getAccountName());
                    accountSummary.put("accountCategory", data.getAccountCategory());
                    accountSummary.put("sor", data.getSor());
                    accountSummary.put("costCenter", data.getCostCenter());
                    accountSummary.put("meterReadingCodes", data.getMeterReadingCode());
                    accountSummary.put("customerManagement", data.getCustomerManagement());
                    accountSummary.put("classificationType", data.getClassificationType());
                    accountSummary.put("segment", data.getAccountSegment());
                    accountSummary.put("accountGroupType", data.getAccountGroupType());
                    accountSummary.put("accountStatus", data.getAccountStatus());

                    LinkedHashMap<String, Object> accountInfo = new LinkedHashMap<>();

                    // CEK AKSES UPDATE
                    Optional<M_ACCOUNT_CUSTOMER_MANAGEMENT> cekPosition = mAccountCMRepo.findByAccountIdAndStatus(accountId, FlowStatus.ACTIVE.name());
                    if(cekPosition.isPresent()){
                        String tokenFromHeader = jwtUtils.parseJwt(request);
                        Integer currentPosition = jwtUtils.getPositionFromJwtToken(tokenFromHeader);
                        Optional<M_POSITION> position = mPositionRepo.findByPositionId(currentPosition);
                        logger.info("position : " + position.get().getPositionId().toString());
                        if(cekPosition.get().getPositionId().equals(position.get().getPositionId())){
                            accountInfo.put("isEditor", true);
                        } else {
                            accountInfo.put("isEditor", false);
                        }
                    } else {
                        accountInfo.put("isEditor", false);
                    }

                    accountInfo.put("accountId", data.getAccountId());
                    accountInfo.put("accountGroup", data.getAccountGroup());
                    accountInfo.put("accountGroupId", data.getAccountGroupId());
                    accountInfo.put("accountReferenceId", data.getAccountReferenceId());
                    accountInfo.put("customerManagement", data.getCustomerManagement());
                    accountInfo.put("sor", data.getSor());
                    accountInfo.put("sorId", data.getSorId());
                    accountInfo.put("costCenter", data.getCostCenter());
                    accountInfo.put("costCenterId", data.getCostCenterId());
                    accountInfo.put("meterReadingCodes", data.getMeterReadingCode());
                    accountInfo.put("accountNumber", data.getAccountNumber());
                    accountInfo.put("registrationNumber", data.getRegistrationNumber());
                    accountInfo.put("accountName", data.getAccountName());

                    accountInfo.put("categoryId", data.getAccountCategoryId());
                    accountInfo.put("category", data.getAccountCategory());

                    accountInfo.put("description", data.getAccountDescription());
                    accountInfo.put("status", data.getAccountStatus());

                    accountInfo.put("segmentId", data.getAccountSegmentId());
                    accountInfo.put("segment", data.getAccountSegment());
                    accountInfo.put("accountGroupTypeId", data.getAccountGroupTypeId());
                    accountInfo.put("accountGroupType", data.getAccountGroupType());
                    accountInfo.put("accountTypeId", data.getAccountTypeId());
                    accountInfo.put("accountType", data.getAccountType());
                    accountInfo.put("classificationTypeId", data.getClassificationTypeId());
                    accountInfo.put("classificationType", data.getClassificationType());

                    R_GLOBAL_TYPE_VALUE priorityId = rGlobalTypeValueRepo.findTopByGlbValueIgnoreCaseAndIsDeleted(data.getPriority(), Boolean.FALSE);
                    accountInfo.put("priorityId", ObjectUtils.isEmpty(priorityId) ? "" : priorityId.getGlbTypeValId());
                    accountInfo.put("priority", data.getPriority());


                    if(data.getIsCorporate().equalsIgnoreCase("Yes")){
                        accountInfo.put("corporateCustomer", Boolean.TRUE);
                    }else{
                        accountInfo.put("corporateCustomer", Boolean.FALSE);
                    }

                    if (data.getIsException().equalsIgnoreCase("Yes")) {
                        accountInfo.put("ratingAndBillingException", Boolean.TRUE);
                    } else {
                        accountInfo.put("ratingAndBillingException", Boolean.FALSE);
                    }

                    accountInfo.put("industrialSectorId", data.getIndustrialSectorId());
                    accountInfo.put("industrialSector", data.getIndustrialSector());

                    accountInfo.put("budgetYearId", data.getBudgetYearId());
                    accountInfo.put("budgetYear", data.getBudgetYear());

                    accountInfo.put("budgetId", data.getBudgetId());
                    accountInfo.put("budget", data.getBudget());
                    accountInfo.put("teritoryId", data.getTeritoryId());
                    accountInfo.put("teritory", data.getTeritory());


                    accountInfo.put("createdDate", data.getCreatedDate());
                    accountInfo.put("createdBy", data.getCreatedBy());
                    accountInfo.put("updatedDate", data.getUpdatedDate());
                    accountInfo.put("updatedBy", data.getUpdatedBy());

                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                    response.put("accountSummary", accountSummary);
                    response.put("accountInformation", accountInfo);

                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            ResponseUtils.MESSAGE_OK, response), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                            ResponseUtils.MESSAGE_NOT_FOUND, ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("java:S3776")
    public ResponseEntity<ResponseObject> activeInactiveCustomer(ActiveInactiveDTO request) {
        try {
            Optional<M_CUSTOMER> customer = mCustomerRepo.findById(request.getId());
            if (customer.isPresent()) {
                M_CUSTOMER c = customer.get();
                List<M_ACCOUNT> allAccount = accountRepo.findByCustomerId(c.getCustomerId());
                if (!allAccount.isEmpty()) {
                    for (M_ACCOUNT a : allAccount) {
                        if (a.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())) {
                            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                                    Constant.CUSTOMER_INACTIVE_ACCOUNT, ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                        }
                    }
                }
                LinkedHashMap<String, Object> oldCustomer = new LinkedHashMap<>();
                oldCustomer.put("customerId", c.getCustomerId());
                oldCustomer.put("customerName", c.getCustomerName());
                oldCustomer.put("customerNumber", c.getCustomerNumber());
                oldCustomer.put("status", c.getStatus());
                AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
                ObjectMapper mapper = new ObjectMapper();
                String oldValue = mapper.writeValueAsString(oldCustomer);
                auditTrail.setOldValue(oldValue);
                auditTrail.setRemark(request.getRemark());
                auditTrail.setTableName("M_CUSTOMER");
                auditTrail.setDataId(c.getCustomerId().toString());
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
                if (c.getStatus().equalsIgnoreCase(FlowStatus.INACTIVE.name())) {
                    c.setStatus(FlowStatus.ACTIVE.name());
                    c.setUpdatedBy(UserDetailUtils.getUsername());
                    c.setUpdatedDate(new Date());
                    M_CUSTOMER saveCustomer = mCustomerRepo.save(c);

                    if(saveCustomer.getPartyId()!=null) {
                        // ACTIVE PARTY
                        party.activeInactiveParty(saveCustomer.getPartyId());
                    }

                    LinkedHashMap<String, Object> newCustomer = new LinkedHashMap<>();
                    newCustomer.put("customerId", saveCustomer.getCustomerId());
                    newCustomer.put("customerName", saveCustomer.getCustomerName());
                    newCustomer.put("customerNumber", saveCustomer.getCustomerNumber());
                    newCustomer.put("status", saveCustomer.getStatus());
                    String newValue = mapper.writeValueAsString(newCustomer);
                    auditTrail.setOperation(FlowStatus.ACTIVE.name());
                    auditTrail.setNewValue(newValue);
                    auditTrailRepo.save(auditTrail);
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            ResponseUtils.MESSAGE_ACTIVE, ResponseUtils.DATA_EMPTY), HttpStatus.OK);
                }
                c.setStatus(FlowStatus.INACTIVE.name());
                c.setUpdatedBy(UserDetailUtils.getUsername());
                c.setUpdatedDate(new Date());
                M_CUSTOMER saveCustomer = mCustomerRepo.save(c);

                if(saveCustomer.getPartyId()!=null) {
                    // INACTIVE PARTY
                    party.activeInactiveParty(saveCustomer.getPartyId());
                }

                LinkedHashMap<String, Object> newCustomer = new LinkedHashMap<>();
                newCustomer.put("customerId", saveCustomer.getCustomerId());
                newCustomer.put("customerName", saveCustomer.getCustomerName());
                newCustomer.put("customerNumber", saveCustomer.getCustomerNumber());
                newCustomer.put("status", saveCustomer.getStatus());
                String newValue = mapper.writeValueAsString(newCustomer);
                auditTrail.setOperation(FlowStatus.INACTIVE.name());
                auditTrail.setNewValue(newValue);
                auditTrailRepo.save(auditTrail);
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        ResponseUtils.MESSAGE_INACTIVE, ResponseUtils.DATA_EMPTY), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    public ResponseEntity<InputStreamResource> downloadFilter(Integer locationType, MaterialTablePagingRequest pagingData) {
//        try {
//            Page<VW_LOCATION> data;
//            Map<String, Object> filter = new HashMap<>();
//            filter.put("locationTypeId", locationType);
//
//            if (isNotBlank(pagingData.getSearchs())) {
//                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
//                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
//                    String key = entry.getKey();
//                    Object value = entry.getValue();
//                    pagingData.getSearch().add(key+"~"+value);
//                }
//            }
//
//            if (pagingData.getPage() != null && pagingData.getSize() != null) {
//
//                long maxData = StreamSupport.stream(mlocationRepo.findAll().spliterator(), false).count();
//                pagingData.setSize((int) maxData);
//                pagingData.setPage(1);
//
//            }
//            if (!pagingData.getSearch().isEmpty()) {
//                data = this.vwLocationRepo.findAll(this.vwLocationRepo.getSpecificationFromFilters(pagingData, filter),
//                        PagingUtils.getPaging(pagingData));
//            } else {
//                data = this.vwLocationRepo.findAll(this.vwLocationRepo.getSpecificationDefault(filter),
//                        PagingUtils.getPaging(pagingData));
//            }
//            List<VW_LOCATION> listAction = data.getContent();
//            List<LinkedHashMap<String, Object>> allData = new ArrayList<>();
//            Integer no = 1;
//            HttpHeaders headers = new HttpHeaders();
//            if(data.hasContent()) {
//                for (CustomerInformationDto cc : customerInformationData) {
//                    LinkedHashMap<String, Object> d = new LinkedHashMap<>();
//                    d.put("NO", i++);
//                    d.put("CUSTOMER NUMBER", cc.getCustomerNumber());
//                    d.put("IDENTIFICATION TYPE", cc.getIdentificationType());
//                    d.put("CUSTOMER IDENTIFICATION NUMBER", cc.getCustomerIdentificationNumber());
//                    d.put("CUSTOMER NAME", cc.getCustomerName());
//                    d.put("CUSTOMER TYPE", cc.getCustomerType());
//                    d.put("CM", cc.getCustomerManagement());
//                    d.put("SEARCH KEY", cc.getSearchKey());
//                    d.put("BIRTH/FOUNDED PLACE", cc.getFoundedBirthPlace());
//                    d.put("BIRTH/FOUNDED DATE", cc.getFoundedBirthDate());
//                    d.put("SEX", cc.getSex());
//                    d.put("MARITAL STATUS", cc.getMaritalStatus());
//                    d.put("STATUS", cc.getStatus());
//                    dataList.add(d);
//                }
//
//                for (VW_LOCATION a : listAction) {
//                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
//                    response.put("NO", no);
//                    response.put("CODE", a.getLocationCode());
//                    response.put("LOCATION TYPE", a.getLocationType());
//                    response.put("LOCATION NAME", a.getLocationName());
//                    response.put("LOCATION PARENT", a.getLocationParent());
//                    response.put("LOCATION PARENT TYPE", a.getLocationParentType());
//                    response.put("LOCATION REFERENCE", a.getLocationReference());
//                    response.put("STATUS", capitalizeFully(a.getStatus()));
//                    allData.add(response);
//                    no = no + 1;
//                }
//                ByteArrayInputStream in = DownloadToExcel.downloadsFiles(allData, "Excel");
//                headers.add(Constant.CONTENT_DISPOSITION, "attachment; filename=" +
//                        "LOCATION_" + UtilsDate.dateToString(new Date(), "yyyyMMddHHmmss") + ".xlsx");
//                return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
//            } else {
//                headers.add(Constant.CONTENT_DISPOSITION, "No Data");
//                return ResponseEntity.noContent().build();
//            }
//        } catch (Exception e) {
//            logger.info(String.format(CommonVariables.ERROR_IN_PAR, e.getMessage()));
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().body(null);
//        }
//    }

    @SuppressWarnings("java:S3776")
    public ResponseEntity<InputStreamResource> downloadCustomer(SearchFilterDTO dto, MaterialTablePagingRequest pagingData,
                                                                HttpServletRequest request, PagedResourcesAssembler<VW_CUS_INFO_CC> assemblerDto) throws IOException {

        if (pagingData.getPage() != null && pagingData.getSize() != null) {
            Long maxData = vwCusInfoCcRepo.findAll().stream().count();
            pagingData.setSize(maxData.intValue());
            pagingData.setPage(1);
        }
        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> filter = new HashMap<>();
        Map<String, Object> filterAccount = new HashMap<>();
        Page<VW_CUS_INFO_CC> data;
        List<VW_ACCOUNT_INFORMATION> listAccountData = null;
        logger.info("current entity " + UserDetailUtils.getUserEntity());
        filter.put("entityId", UserDetailUtils.getUserEntity());
        boolean isItSuperUser = isItSuperUser(Integer.parseInt(UserDetailUtils.getUserId()));
        if(!isItSuperUser){
            Optional<M_POSITION> cekPosition = mPositionRepo.findById(UserDetailUtils.getPositionFromToken(request));
            if(cekPosition.isPresent() && cekPosition.get().getName().contains("CM ")) {
                filter.put("positionId", UserDetailUtils.getPositionFromToken(request));
                filter.put("pagingCustomerCm", "USE");
                logger.info("current positionId --> " + UserDetailUtils.getPositionFromToken(request));
            } else {
                filter.put("accountCostCenterId", UserDetailUtils.getPositionFromToken(request));
                filter.put("pagingCustomerHead", "USE");
                logger.info("current accountCostCenterId --> " + UserDetailUtils.getPositionFromToken(request));
            }

            // FILTER ACCOUNT
            filterAccount.put(Constant.UNIQUE_ACCOUNT, "USE");
            Optional<M_USER> user = userRepo.findByUsername(UserDetailUtils.getUsername());
            Optional<M_POSITION> cekPositionAccount = (user.get().getUserType().equalsIgnoreCase("EMP")?mPositionRepo.findById(UserDetailUtils.getPositionFromToken(request)): Optional.empty());
            filterAccount.put(Constant.ENTITY_ID, UserDetailUtils.getUserEntity());
            // admin entity
            if(user.get().getUserLevel().equalsIgnoreCase("AE")) {
                List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(UserDetailUtils.getPositionFromToken(request), GET_CC_CHILD);
                filterAccount.put("costCenterId", ccList);
                logger.info("AE costCenterId : " + ccList);
            }
            // end user (CM)
            else if(cekPositionAccount.isPresent() && Pattern.matches(".*\\bCM\\b.*", cekPositionAccount.get().getName())) {
                filterAccount.put("customerManagementId", UserDetailUtils.getPositionFromToken(request));
                logger.info("customerManagementId : " + UserDetailUtils.getPositionFromToken(request).toString() + " - " + cekPositionAccount.get().getName());
            }
            // end user (employee)
            else if(user.get().getUserType().equalsIgnoreCase("EMP")) {
                List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(UserDetailUtils.getPositionFromToken(request), GET_CC_CHILD);
                filterAccount.put("costCenterId", ccList);
                logger.info("costCenterId : " + ccList);
            }
            // end user (non employee)
            else if(user.get().getUserType().equalsIgnoreCase("NON_EMP")) {
                headers.add(Constant.CONTENT_DISPOSITION, "No Data");
                return ResponseEntity.noContent().build();
            }
        }

        if (isNotBlank(pagingData.getSearchs())) {
            Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
            for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                pagingData.getSearch().add(key+"~"+value);
            }
        }

        Specification<VW_CUS_INFO_CC> specification = pagingData.getSearch().isEmpty()
                ? vwCusInfoCcRepo.getSpecificationDefault(filter)
                : vwCusInfoCcRepo.getSpecificationFromFilters(pagingData, filter);

        Specification<VW_ACCOUNT_INFORMATION> specificationAccount;

        // ADVANCED FILTER
        List<FilterRequestDTO> filterList= dto.getInputFields();
        List<AdvanceFilter> advanceFilterCustomer = new ArrayList<>();
        List<AdvanceFilter> advanceFilterAccount = new ArrayList<>();
        if(!filterList.isEmpty() && !ObjectUtils.isEmpty(filterList.get(0).getColumn())){
            int i = 0 ;
            for(FilterRequestDTO frd: filterList){
                AdvanceFilter advanceFilter = new AdvanceFilter();
                Optional<R_GLOBAL_TYPE_VALUE> columnOpt = rGlobalTypeValueRepo.findByGlbTypeValId(frd.getColumn());
                Optional<R_GLOBAL_TYPE_VALUE> operatorOpt = rGlobalTypeValueRepo.findByGlbTypeValId(frd.getOperator());
                if(i ==0){
                    advanceFilter.setCondition("AND");
                }else{
                    Optional<R_GLOBAL_TYPE_VALUE> conditionOpt = rGlobalTypeValueRepo.findByGlbTypeValId(frd.getCondition());
                    if(conditionOpt.isPresent()){
                        advanceFilter.setCondition(conditionOpt.get().getGlbValue());
                    }
                }

                if(operatorOpt.isPresent()){
                    advanceFilter.setOperator(operatorOpt.get().getGlbValue());
                }
                advanceFilter.setValue(frd.getValue());
                if(columnOpt.isPresent()){
                    advanceFilter.setColumn(columnOpt.get().getGlbValue());
                    if(this.customerColumn.contains(columnOpt.get().getGlbValue())){
                        advanceFilterCustomer.add(advanceFilter);
                    }else if(this.accountColumn.contains(columnOpt.get().getGlbValue())){
                        advanceFilterAccount.add(advanceFilter);
                    }
                }
                i++;
            }

            // ACCOUNT
            if(!advanceFilterAccount.isEmpty()){
                specificationAccount = vwAccInfoRepo.getSpecificationFromAdvanceFilters(advanceFilterAccount, filterAccount);
                listAccountData = vwAccInfoRepo.findAll(specificationAccount);
                List<Integer> customerIdFiltering = listAccountData.stream()
                        .map(VW_ACCOUNT_INFORMATION::getCustomerId)
                        .distinct()
                        .collect(Collectors.toList());
                specification = vwCusInfoCcRepo.getSpecificationFromAdvanceFiltersWithAccount(advanceFilterCustomer, specification, customerIdFiltering);
            } else {
                specification = vwCusInfoCcRepo.getSpecificationFromAdvanceFilters(advanceFilterCustomer, specification);
            }
        }

        data = vwCusInfoCcRepo.findAll(specification, PagingUtils.getPaging(pagingData));

        List<VW_CUS_INFO_CC> dataList = data.getContent();
        List<LinkedHashMap<String, Object>> allData = new ArrayList<>();
        Integer i = 1;
        if(data.hasContent()) {
            for (VW_CUS_INFO_CC customer : dataList) {
                // LIST ACCOUNT
                filterAccount.put("customerId", customer.getCustomerId());
                if(!advanceFilterAccount.isEmpty()){
                    specificationAccount = vwAccInfoRepo.getSpecificationFromAdvanceFilters(advanceFilterAccount, filterAccount);
                    listAccountData = vwAccInfoRepo.findAll(specificationAccount);
                }else{
                    specificationAccount = vwAccInfoRepo.getSpecificationDefault2(filterAccount);
                    listAccountData = vwAccInfoRepo.findAll(specificationAccount);
                }
                for(VW_ACCOUNT_INFORMATION account : listAccountData) {
                    LinkedHashMap<String, Object> d = new LinkedHashMap<>();
                    d.put("NO", i);

                    // CUSTOMER
                    d.put("CUSTOMER NUMBER", customer.getCustomerNumber());
                    d.put("CUSTOMER NAME", customer.getCustomerName());
                    d.put("FIRST NAME", customer.getCustomerName());
                    d.put("MIDDLE NAME", customer.getCustomerName());
                    d.put("LAST NAME", customer.getCustomerName());
                    d.put("CUSTOMER TYPE", customer.getCustomerType());
                    d.put("IDENTIFICATION TYPE", customer.getIdentificationType());
                    d.put("CUSTOMER IDENTIFICATION NUMBER", customer.getCustomerIdentificationNumber());
                    d.put("SEARCH KEY", customer.getSearchKey());
                    d.put("BIRTH/FOUNDED PLACE", customer.getFoundedBirthPlace());
                    d.put("BIRTH/FOUNDED DATE", customer.getFoundedBirthDate());
                    d.put("SEX", customer.getSex());
                    d.put("MARITAL STATUS", customer.getMaritalStatus());
                    d.put("STATUS", customer.getStatus());

                    //ACCOUNT
                    d.put("ACCOUNT NUMBER", account.getAccountNumber());
                    d.put("ACCOUNT NAME", account.getAccountName());
                    d.put("ACCOUNT REGISTRATION NUMBER", account.getRegistrationNumber());
                    d.put("SOR", account.getSor());
                    d.put("COST CENTER", account.getCostCenter());
                    d.put("METER READING CODE", account.getMeterReadingCode());
                    d.put("ACCOUNT SEGMENT", account.getAccountSegment());
                    d.put("ACCOUNT GROUP TYPE", account.getAccountGroupType());
                    d.put("CATEGORY", account.getAccountCategory());
                    d.put("CLASSIFICATION TYPE", account.getClassificationType());
                    d.put("ACCOUNT TYPE", account.getAccountType());
                    d.put("INDUSTRIAL SECTOR", account.getIndustrialSector());
                    d.put("BUDGET YEAR", account.getBudgetYear());
                    d.put("BUDGET", account.getBudget());
                    d.put("TERITORY", account.getTeritory());
                    d.put("ACCOUNT GROUP", account.getAccountGroup());
                    d.put("PRIORITY", account.getPriority());
                    d.put("CORPORATE", account.getIsCorporate());
                    d.put("RATING & BILLING EXCEPTION", account.getIsException());
                    d.put("CUSTOMER MANAGEMENT", account.getCustomerManagement());
                    d.put("DESCRIPTION ACCOUNT", account.getAccountDescription());
                    d.put("STATUS ACCOUNT", account.getAccountStatus());

                    allData.add(d);
                    i++;
                }
            }
            ByteArrayInputStream in = DownloadToExcel.downloadsFiles(allData, "Excel");
            headers.add("Content-Disposition", "attachment; filename=" +
                    "CUSTOMER_" + UtilsDate.dateToString(new Date(), "yyyyMMddHHmmss") + ".xlsx");
            return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
        } else {
            headers.add(Constant.CONTENT_DISPOSITION, "No Data");
            return ResponseEntity.noContent().build();
        }
    }

    public ResponseEntity<ResponseObject> pagingCustomerAccount(MaterialTablePagingRequest pagingData,
                                                                PagedResourcesAssembler<VW_ACCOUNT_INFORMATION> assembler,
                                                                Integer customerId, HttpServletRequest request) {
        try {
            Map<String, Object> filter = new HashMap<>();
            filter.put("entityId", UserDetailUtils.getUserEntity());
            filter.put("customerId", customerId);
            filter.put(Constant.UNIQUE_ACCOUNT, "USE");
            boolean isItSuperUser = isItSuperUser(Integer.parseInt(UserDetailUtils.getUserId()));
            if(!isItSuperUser){

                Optional<M_POSITION> cekPosition = mPositionRepo.findById(UserDetailUtils.getPositionFromToken(request));
                if(cekPosition.isPresent() && cekPosition.get().getName().contains("CM ")) {
                    filter.put("customerManagementId", UserDetailUtils.getPositionFromToken(request));
                } else {
                    filter.put("costCenterId", costCenterUtils.findCostCenterByPositionId(UserDetailUtils.getPositionFromToken(request)));
                }
            }

            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key+"~"+value);
                }
            }

            Specification<VW_ACCOUNT_INFORMATION> specification = pagingData.getSearch().isEmpty()
                    ? vwAccInfoRepo.getSpecificationDefault(filter)
                    : vwAccInfoRepo.getSpecificationFromFilters(pagingData, filter);

            Page<VW_ACCOUNT_INFORMATION> data = vwAccInfoRepo.findAll(specification, PagingUtils.getPaging(pagingData));
            PagedModel<EntityModel<VW_ACCOUNT_INFORMATION>> pagedData = assembler.toModel(data);

            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get view paging", new PagingDTO(data.getContent(), pagedData.getMetadata(),
                    pagedData.getLinks())), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> pagingCustomerAddress(MaterialTablePagingRequest pagingData,
                                                                PagedResourcesAssembler<VW_CUSTOMER_ADDDRESS> assembler,
                                                                Integer customerId, HttpServletRequest request) {
        try {
            Map<String, Object> filter = new HashMap<>();
            filter.put("customerId", customerId == null ? 0 : customerId);
            boolean isItSuperUser = isItSuperUser(Integer.parseInt(UserDetailUtils.getUserId()));
            if(!isItSuperUser){
                Optional<M_POSITION> cekPosition = mPositionRepo.findById(UserDetailUtils.getPositionFromToken(request));
                if(cekPosition.isPresent() && cekPosition.get().getName().contains("CM ")) {
                    filter.put("customerManagementId", UserDetailUtils.getPositionFromToken(request));
                    logger.info("cm : " + UserDetailUtils.getPositionFromToken(request));
                } else {
                    filter.put("costCenterId", costCenterUtils.findCostCenterByPositionId(UserDetailUtils.getPositionFromToken(request)));
                    logger.info("cc : " + costCenterUtils.findCostCenterByPositionId(UserDetailUtils.getPositionFromToken(request)));
                }
            }

            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key+"~"+value);
                }
            }

            Specification<VW_CUSTOMER_ADDDRESS> specification = pagingData.getSearch().isEmpty()
                    ? vwCustomerAddressRepo.getSpecificationDefault(filter)
                    : vwCustomerAddressRepo.getSpecificationFromFilters(pagingData, filter);

            Page<VW_CUSTOMER_ADDDRESS> data = vwCustomerAddressRepo.findAll(specification, PagingUtils.getPaging(pagingData));

            PagedModel<EntityModel<VW_CUSTOMER_ADDDRESS>> renderPagedData = assembler.toModel(data);

            Map<String, Object> d = new HashMap<>();
            d.put("result", renderPagedData.getContent());
            d.put("page", renderPagedData.getMetadata());
            d.put("links", renderPagedData.getLinks());
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

    public ResponseEntity<ResponseObject> pagingCustomerContact(MaterialTablePagingRequest pagingData,
			PagedResourcesAssembler<VW_CUSTOMER_CONTACT> assembler, Integer customerId, HttpServletRequest request) {
            logger.info("Get List Account Contact");
            try {
                Map<String, Object> filter = new HashMap<>();
                filter.put("customerId", customerId);
                boolean isItSuperUser = isItSuperUser(Integer.parseInt(UserDetailUtils.getUserId()));
                if(!isItSuperUser){
                    Optional<M_POSITION> cekPosition = mPositionRepo.findById(UserDetailUtils.getPositionFromToken(request));
                    if(cekPosition.isPresent() && cekPosition.get().getName().contains("CM ")) {
                        filter.put("customerManagementId", UserDetailUtils.getPositionFromToken(request));
                    } else {
                        filter.put("costCenterId", costCenterUtils.findCostCenterByPositionId(UserDetailUtils.getPositionFromToken(request)));
                    }
                }

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
                        acontact.put("accountNumber", g.getAccountNumber());
                        acontact.put("accountName", g.getAccountName());
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
                        List<M_CONTACT_DETAILS> allCDet = cdRepo.findAllByContactId(g.getContactId());
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

    public ResponseEntity<ResponseObject> pagingCustomerAttachment(Integer customerId) {
        try {

            List<M_ATTACHMENT> listAttachment = mAttachmentAccountRepo.findAllByReferenceId(customerId);
            List<M_ATTACHMENT> dataAttachment = new ArrayList<>();
            for(M_ATTACHMENT attach : listAttachment) {
                if(attach.getCategory().equalsIgnoreCase("CUSTOMER_ATTACHMENT")) {
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

    @Transactional(rollbackFor = Exception.class, readOnly = false)
    public ResponseEntity<ResponseObject> updateCustomer(UpdateCustomerDTO request) {
        try {
            ResponseEntity<ResponseObject> validate = this.validateUpdateCustomer(request);
            if(!validate.getBody().getSuccess()) {
                return validate;
            }

            Optional<M_CUSTOMER> customer = mCustomerRepo.findById(request.getCustomerId());
            M_CUSTOMER data = customer.get();
            if(!request.getCustomerType().equals(58)) {
                data.setFirstName(removeSpace(request.getFirstName()));
                data.setMiddleName(removeSpace(request.getMiddleName()));
                data.setLastName(removeSpace(request.getLastName()));
            }
            data.setCustomerType(request.getCustomerType());
            data.setIdentificationType(request.getIdentificationType());
            data.setCustomerIdentificationNumber(request.getCustomerIdentificationNumber());
            data.setCustomerName(removeSpace(request.getCustomerName()));
            data.setFoundedBirthDate(CommonHelper.convertStringToDate(Constant.FORMAT_START_END_DATE, request.getFoundedBirthDate()));
            data.setFoundedBirthPlace(removeSpace(request.getFoundedBirthPlace()));
            data.setSex(request.getSex());
            data.setMaritalStatus(request.getMaritalStatus());
            data.setSearchKey(removeSpace(request.getSearchKey()));
            data.setDescription(removeSpace(request.getDescription()));
            data.setUpdatedBy(UserDetailUtils.getUsername());
            data.setUpdatedDate(new Date());
            M_CUSTOMER updateCustomer = mCustomerRepo.save(data);

            if(updateCustomer.getPartyId()!=null) {
                // UPDATE PARTY
                party.updateParty(updateCustomer.getPartyId(), updateCustomer.getCustomerIdentificationNumber(), updateCustomer.getCustomerName());
            }

            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.UPDATE, ConstantAccount.CUSTOMER), updateCustomer), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateUpdateCustomer(UpdateCustomerDTO request) {
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

            Optional<M_CUSTOMER> customer = mCustomerRepo.findById(request.getCustomerId());
            if (customer.isEmpty()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        UtilsAccount.messageDataNotFound(ConstantAccount.CUSTOMER, request.getCustomerId()), ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.VALIDATE, ConstantAccount.CUSTOMER), ResponseUtils.DATA_EMPTY), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<LinkedHashMap<String, Object>> getGlobalType(Integer id) {
        Optional<M_GLOBAL_TYPE> mGlobalType = mGlobalTypeRepo.findByGlbTypeId(id);
        if (mGlobalType.isEmpty() || !FlowStatus.ACTIVE.name().equalsIgnoreCase(mGlobalType.get().getStatus())) {
            return Collections.emptyList();
        }
        return rGlobalTypeValueRepo.findAll().stream()
                .filter(v -> Objects.equals(v.getGlobalType(), mGlobalType.get().getGlbTypeId()) &&
                        FlowStatus.ACTIVE.name().equalsIgnoreCase(v.getStatus()))
                .map(v -> {
                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                    response.put("id", v.getGlbTypeValId());
                    response.put("value", v.getName());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public ResponseEntity<ResponseObject> getDataGlobalType(Integer id) {
        try {
            List<LinkedHashMap<String, Object>> allResponse = getGlobalType(id);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    ResponseUtils.MESSAGE_OK, allResponse), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<InputStreamResource> downloadFilterAccount(MaterialTablePagingRequest pagingData, String accountGroup, HttpServletRequest httpServletRequest) throws IOException {
        try {
            Page<VW_ACCOUNT_INFORMATION> data;
            Map<String, Object> filter = new HashMap<>();
            filter.put("entityId", UserDetailUtils.getUserEntity());
            if ("standard".equalsIgnoreCase(accountGroup)) {
                filter.put("accountGroup", "Standard");
            } else if ("One Time".equalsIgnoreCase(accountGroup)) {
                filter.put("accountGroup", "One Time");
            }

            boolean isItSuperUser = isItSuperUser(Integer.parseInt(UserDetailUtils.getUserId()));
            if (!isItSuperUser) {
                Optional<M_COSTCENTER> cekCM = ccRepo.findByccId(costCenterUtils.findCostCenterByPositionId(UserDetailUtils.getPositionFromToken(httpServletRequest)));
                if (cekCM.isPresent() && cekCM.get().getCcType().equalsIgnoreCase("AREA")) {
                    filter.put("customerManagementId", UserDetailUtils.getPositionFromToken(httpServletRequest));
                } else {
                    filter.put("positionId", UserDetailUtils.getPositionFromToken(httpServletRequest));
                }
            }

            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key + "~" + value);
                }
            }

            if (pagingData.getPage() != null && pagingData.getSize() != null) {

                long maxData = StreamSupport.stream(vwAccInfoRepo.findAll().spliterator(), false).count();
                pagingData.setSize((int) maxData);
                pagingData.setPage(1);

            }

            if (!pagingData.getSearch().isEmpty()) {
                data = this.vwAccInfoRepo.findAll(this.vwAccInfoRepo.getSpecificationFromFilters2(pagingData, filter),
                        PagingUtils.getPaging(pagingData));
            } else {
                data = this.vwAccInfoRepo.findAll(this.vwAccInfoRepo.getSpecificationDefault2(filter),
                        PagingUtils.getPaging(pagingData));
            }
            List<VW_ACCOUNT_INFORMATION> listAction = data.getContent();
            List<LinkedHashMap<String, Object>> allData = new ArrayList<>();
            Integer no = 1;
            HttpHeaders headers = new HttpHeaders();

            //        Specification<VW_ACCOUNT_INFORMATION> specification = pagingData.getSearch().isEmpty()
            //                ? vwAccInfoRepo.getSpecificationDefault(filter)
            //                : vwAccInfoRepo.getSpecificationFromFilters(pagingData, filter);
            //
            //        List<VW_ACCOUNT_INFORMATION> data = vwAccInfoRepo.findAll(specification);
            //
            //        int i = 1;

            if (data.hasContent()) {
                for (VW_ACCOUNT_INFORMATION d : listAction) {
                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                    response.put("NO", no);
                    response.put("ACCOUNT NUMBER", d.getAccountNumber());
                    response.put("ACCOUNT NAME", d.getAccountName());
                    response.put("ACCOUNT REGISTRATION NUMBER", d.getRegistrationNumber());
                    response.put("CUSTOMER NUMBER", d.getCustomerNumber());
                    response.put("CUSTOMER IDENTIFICATION NUMBER", d.getCustomerIdentificationNumber());
                    response.put("CUSTOMER NAME", d.getCustomerName());
                    response.put("CUSTOMER TYPE", d.getCustomerType());
                    response.put("SOR", d.getSor());
                    response.put("COST CENTER", d.getCostCenter());
                    response.put("METER READING CODE", d.getMeterReadingCode());
                    response.put("ACCOUNT SEGMENT", d.getAccountSegment());
                    response.put("ACCOUNT GROUP TYPE", d.getAccountGroupType());
                    response.put("CATEGORY", d.getAccountCategory());
                    response.put("CLASSIFICATION TYPE", d.getClassificationType());
                    response.put("ACCOUNT TYPE", d.getAccountType());
                    response.put("INDUSTRIAL SECTOR", d.getIndustrialSector());
                    response.put("BUDGET YEAR", d.getBudgetYear());
                    response.put("BUDGET", d.getBudget());
                    response.put("TERITORY", d.getTeritory());
                    response.put("ACCOUNT GROUP", d.getAccountGroup());
                    response.put("PRIORITY", d.getPriority());
                    response.put("CORPORATE", d.getIsCorporate());
                    response.put("RATING & BILLING EXCEPTION", d.getIsException());
                    response.put("CUSTOMER MANAGEMENT", d.getCustomerManagement());
                    response.put("CUSTOMER MANAGEMENT NAME", d.getCustomerManagementName());
                    response.put("DESCRIPTION", d.getAccountDescription());
                    response.put("STATUS", d.getAccountStatus());
                    allData.add(response);
                    no++;
                }
                ByteArrayInputStream in = DownloadToExcel.downloadsFiles(allData, "Excel");
                headers.add(Constant.CONTENT_DISPOSITION, "attachment; filename=" +
                        "ACCOUNT_" + accountGroup.toUpperCase() + "_" + UtilsDate.dateToString(new Date(), "yyyyMMddHHmmss") + ".xlsx");
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

    @Transactional
    public ResponseEntity<ResponseObject> uploadAttachment(Integer fileCategoryId, List<MultipartFile> files, Integer refId) {
        try {
            Optional<M_CUSTOMER> customer = mCustomerRepo.findById(refId);
            if (customer.isEmpty()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
            }
            M_CUSTOMER data = customer.get();
            for (MultipartFile file : files) {
                M_ATTACHMENT mAttachment = new M_ATTACHMENT();
                String generatedFileName = UserDetailUtils.generateFileName(file.getOriginalFilename());
                mAttachment.setCategory("CUSTOMER_ATTACHMENT");
                mAttachment.setFileCategoryId(fileCategoryId);
                mAttachment.setReferenceId(refId);
                mAttachment.setType(file.getContentType());
                mAttachment.setCreatedBy(UserDetailUtils.getUsername());
                mAttachment.setCreatedDate(new Date());
                mAttachment.setPathFile("PATH_09");
                mAttachment.setFileName(generatedFileName);
                mAttachment.setFileSize(file.getSize());
                mAttachment.setIsDraft(data.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name()));
                mAttachment.setIsDeleted(Boolean.FALSE);
                mAttachmentRepo.save(mAttachment);

                R_GLOBAL_TYPE_VALUE rGlobalTypeValue = rGlobalTypeValueRepo
                        .findTopByGlbValueIgnoreCaseAndIsDeleted("PATH_09", false);
                String fullPath = rGlobalTypeValue.getName() + generatedFileName;
                String fullobject = "FILE" + fullPath;
                this.minioClient.putObject(this.configurationProperties.getBucket(), fullobject,
                        file.getInputStream(), file.getContentType());
            }
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success upload file", ResponseUtils.DATA_EMPTY), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> updateAccount(UpdateAccountStandartDto accountDtoRequest){
        try{

            ResponseEntity<ResponseObject> validate = this.validateUpdateAccount(accountDtoRequest);
            if(!validate.getBody().getSuccess()) {
                return validate;
            }

            Optional<M_ACCOUNT> getAccountStandardInfo = accountRepo.findByAccountNumber(accountDtoRequest.getAccountNumber());

            M_ACCOUNT accountWantToUpdate = getAccountStandardInfo.get();

            accountWantToUpdate.setAccountName(removeSpace(accountDtoRequest.getAccountName()));
            accountWantToUpdate.setRegistrationNumber(accountDtoRequest.getAccountRegistrationNumber());
            accountWantToUpdate.setAccountCategory(accountDtoRequest.getCategory());
            accountWantToUpdate.setDescription(removeSpace(accountDtoRequest.getDescription()));
            accountWantToUpdate.setAccountGroupType(accountDtoRequest.getAccountGroupType());
            accountWantToUpdate.setAccountType(accountDtoRequest.getAccountType());
            accountWantToUpdate.setPriority(accountDtoRequest.getPriority());
            accountWantToUpdate.setIsCorporate(accountDtoRequest.getCorporateCustomer());
            accountWantToUpdate.setAccountSegment(accountDtoRequest.getSegment());
            accountWantToUpdate.setIsException(accountDtoRequest.getRatingAndBillingException());
            accountWantToUpdate.setIndustrialSector(accountDtoRequest.getIndustrialSector());
            accountWantToUpdate.setBudget(accountDtoRequest.getBudget());
            accountWantToUpdate.setBudgetYear(accountDtoRequest.getBudgetYear());
            accountWantToUpdate.setTeritory(accountDtoRequest.getTeritory());
            accountWantToUpdate.setAccountRuleId(accountDtoRequest.getClassificationType());
            accountWantToUpdate.setUpdatedBy(UserDetailUtils.getUsername());
            accountWantToUpdate.setUpdatedDate(new Date());

            M_ACCOUNT savedAccount= accountRepo.save(accountWantToUpdate);

            if(savedAccount.getPartyId()!=null) {
                // UPDATE PARTY
                party.updateParty(savedAccount.getPartyId(), savedAccount.getAccountNumber(), savedAccount.getAccountName());
            }

            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.UPDATE, ConstantAccount.ACCOUNT), savedAccount), HttpStatus.OK);
        }catch (Exception e){
           logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateUpdateAccount(UpdateAccountStandartDto request){
        Optional<M_ACCOUNT> getAccountStandardInfo = accountRepo.findByAccountNumber(request.getAccountNumber());
        if(!getAccountStandardInfo.isPresent()) {
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                    "Account with account number " + request.getAccountNumber() + " not found!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
        }

        ResponseObject result = new ResponseObject();
        if(!getAccountStandardInfo.isPresent()){
            result.setSuccess(ResponseUtils.SUCCESS_FALSE);
            result.setMessage(ResponseUtils.MESSAGE_NOT_FOUND);
            result.setCode(HttpStatus.NOT_FOUND);
            result.setData(ResponseUtils.DATA_EMPTY);
            result.setHttpCode(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }

        M_ACCOUNT accountWantToUpdate = getAccountStandardInfo.get();

        if(!accountWantToUpdate.getAccountSegment().equals(request.getSegment()) || !accountWantToUpdate.getAccountCategory().equals(request.getCategory()) || !accountWantToUpdate.getAccountGroupType().equals(request.getAccountGroupType()) || !accountWantToUpdate.getAccountType().equals(request.getAccountType()) || !accountWantToUpdate.getAccountRuleId().equals(request.getClassificationType())) {
            //            Check SA
            List<T_AM_SA> checkActiveSa = tAmSARepo.findAllByAccountIdAndStatus(getAccountStandardInfo.get().getAccountId(), FlowStatus.ACTIVE.name());
            if (!checkActiveSa.isEmpty()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "You cannot inactivate account with active service agreement", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                UtilsAccount.messageSuccess(ConstantAccount.VALIDATE, ConstantAccount.ACCOUNT), ResponseUtils.DATA_EMPTY), HttpStatus.OK);

    }

    public ResponseEntity<ResponseObject> listAdvanceSearchCondition() {
        ResponseObject responseObject = new ResponseObject();
        List<R_GLOBAL_TYPE_VALUE> listOfValue = globalTypeService.getDetailGlobalType("Advance Search Condition");
//                rGlobalTypeValueRepo.findByGlobalType(196075);
        List<ListOfValueDto> responseListOfValue = new ArrayList<>();
        listOfValue.forEach(data->{
            ListOfValueDto listOfValueDto = new ListOfValueDto();
            listOfValueDto.setId(data.getGlbTypeValId());
            listOfValueDto.setValue(data.getName());
            responseListOfValue.add(listOfValueDto);
        });
        responseObject.setHttpCode(HttpStatus.OK);
        responseObject.setCode(HttpStatus.OK);
        responseObject.setSuccess(ResponseUtils.SUCCESS_TRUE);
        responseObject.setMessage("Success get list of value for search condition");
        responseObject.setData(responseListOfValue);
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> listAdvanceSearchOperator() {
        ResponseObject responseObject = new ResponseObject();
        List<R_GLOBAL_TYPE_VALUE> listOfValue = globalTypeService.getDetailGlobalType("Advance Search Operator");
        List<ListOfValueDto> responseListOfValue = new ArrayList<>();
        listOfValue.forEach(data->{
            ListOfValueDto listOfValueDto = new ListOfValueDto();
            listOfValueDto.setId(data.getGlbTypeValId());
            listOfValueDto.setValue(data.getName());
            responseListOfValue.add(listOfValueDto);
        });
        responseObject.setHttpCode(HttpStatus.OK);
        responseObject.setCode(HttpStatus.OK);
        responseObject.setSuccess(ResponseUtils.SUCCESS_TRUE);
        responseObject.setMessage("Success get list of value for search");
        responseObject.setData(responseListOfValue);
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> listAdvanceSearchCusAccColumn() {
        ResponseObject responseObject = new ResponseObject();
        List<R_GLOBAL_TYPE_VALUE> listOfValue = globalTypeService.getDetailGlobalType("Customer Account Column");
        List<ListOfValueDto> responseListOfValue = new ArrayList<>();
        listOfValue.forEach(data->{
            ListOfValueDto listOfValueDto = new ListOfValueDto();
            listOfValueDto.setId(data.getGlbTypeValId());
            listOfValueDto.setValue(data.getName());
            responseListOfValue.add(listOfValueDto);
        });
        responseObject.setHttpCode(HttpStatus.OK);
        responseObject.setCode(HttpStatus.OK);
        responseObject.setSuccess(ResponseUtils.SUCCESS_TRUE);
        responseObject.setMessage("Success get list of value for list");
        responseObject.setData(responseListOfValue);
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    private Boolean isItSuperUser(Integer userId){
        Optional<M_USER> userInformation = mUserRepo.findByUserId(userId);
        if(!userInformation.isPresent()){
            return Boolean.FALSE;
        }

        return  userInformation.get().getUserLevel().equalsIgnoreCase("SU") ? Boolean.TRUE : Boolean.FALSE;
    }
}
