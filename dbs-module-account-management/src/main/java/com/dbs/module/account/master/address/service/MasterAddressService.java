package com.dbs.module.account.master.address.service;

import com.dbs.database.crm.entities.accountmanagement.*;
import com.dbs.database.crm.entities.accountmanagement.view.VW_ADDRESS;
import com.dbs.database.crm.repositories.accountmanagement.Account.ChooseAddressRepoCustom;
import com.dbs.database.crm.repositories.accountmanagement.Account.VWCustomerAddressRepo;
import com.dbs.database.crm.repositories.accountmanagement.VwAddressRepo;
import com.dbs.module.account.master.address.dto.MasterAddressInactiveDTO;
import com.dbs.module.account.master.address.dto.AddressInformationResponseDto;
import com.dbs.module.account.master.address.dto.DetailAddressResponseDto;
import com.dbs.module.account.master.address.dto.CreateUpdateDTO;
import com.dbs.module.account.master.address.dto.HistoryLogInformationResponseDto;
import com.dbs.module.account.master.address.dto.AddressCoordinateInformationDto;
import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.*;
import com.dbs.database.crm.entities.product.M_LOCATION;
import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import com.dbs.database.crm.entities.usermanagement.M_USER;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.accountmanagement.Account.MAccountAddressRepo;
import com.dbs.database.crm.repositories.accountmanagement.Account.MaddressRepo;
import com.dbs.database.crm.repositories.accountmanagement.VwChooseAddressRepo;
import com.dbs.database.crm.repositories.usermanagement.AuditTrailRepo;
import com.dbs.database.crm.repositories.usermanagement.MUserRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import com.dbs.module.account.detail.address.service.AccountAddressService;
import com.dbs.module.account.utils.ConstantAccount;
import com.dbs.module.account.utils.UtilsAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Nullable;
import javax.validation.Validator;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.dbs.common.library.utils.StringUtils.capitalizeFully;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import com.dbs.database.crm.repositories.mastermanagement.MLocationsRepo;
import com.dbs.module.account.main.dto.AccountDTO;
import javax.validation.ConstraintViolation;

@Service
public class MasterAddressService {
    
    private static final Logger logger = LoggerFactory.getLogger(AccountAddressService.class);

    private final MaddressRepo mAddressRepo;
    private final AuditTrailRepo auditTrailRepo;
    private final MLocationsRepo mLocationRepo;
    @Autowired
    private VwChooseAddressRepo vwChooseAddressRepo;
    @Autowired
    private MAccountAddressRepo mAccountAddressRepo;
    @Autowired
    private ObjectMapper objectMapper;

    private final Validator validator;

    @Autowired
    private MUserRepo userRepo;
    @Autowired
    private GlobalTypeValueService globalTypeValueService;

    @Autowired
    private ChooseAddressRepoCustom chooseAddressRepoCustom;

    @Autowired
    private VwAddressRepo vwAddressRepo;

    @Autowired
    private VWCustomerAddressRepo vwCustomerAddressRepo;

    public MasterAddressService(MaddressRepo mAddressRepo, Validator validator, MUserRepo userRepo, AuditTrailRepo auditTrailRepo, MLocationsRepo mLocationRepo, RGlobalTypeValueRepo rGlobalTypeValueRepo) {
        this.mAddressRepo = mAddressRepo;
        this.validator = validator;
        this.auditTrailRepo = auditTrailRepo;
        this.mLocationRepo = mLocationRepo;
    }

    public ResponseEntity<ResponseObject> getListMasterAddress(MaterialTablePagingRequest pagingData, PagedResourcesAssembler<VW_ADDRESS> assembler) {
        
        // Logger Info Service
        logger.info("Get List Master Address");

        // Response Object Result
        ResponseObject result = new ResponseObject();

        try {

            Page<VW_ADDRESS> data = null;
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
                data = this.vwAddressRepo.findAll(this.vwAddressRepo.getSpecificationFromFilters(pagingData, filter),
                        PagingUtils.getPaging(pagingData));
            } else {
                data = this.vwAddressRepo.findAll(this.vwAddressRepo.getSpecificationDefault(filter),
                        PagingUtils.getPaging(pagingData));
            }

            PagedModel pagedData = assembler.toModel(data);

            Map<String, Object> d = new HashMap<>();
            d.put("result", pagedData.getContent());
            d.put("page", pagedData.getMetadata());
            d.put("links", pagedData.getLinks());

            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage(UtilsAccount.messageSuccess(ConstantAccount.PAGING, ConstantAccount.MASTER_ADDRESS));
            result.setData(d);

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<InputStreamResource> downloadFilter(MaterialTablePagingRequest pagingData) {
        try {
            Page<VW_ADDRESS> data;
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

                long maxData = StreamSupport.stream(vwAddressRepo.findAll().spliterator(), false).count();
                pagingData.setSize((int) maxData);
                pagingData.setPage(1);

            }
            if (!pagingData.getSearch().isEmpty()) {
                data = this.vwAddressRepo.findAll(this.vwAddressRepo.getSpecificationFromFilters(pagingData, filter),
                        PagingUtils.getPaging(pagingData));
            } else {
                data = this.vwAddressRepo.findAll(this.vwAddressRepo.getSpecificationDefault(filter),
                        PagingUtils.getPaging(pagingData));
            }
            List<VW_ADDRESS> listAction = data.getContent();
            List<LinkedHashMap<String, Object>> allData = new ArrayList<>();
            Integer no = 1;
            HttpHeaders headers = new HttpHeaders();
            if(data.hasContent()) {
                for (VW_ADDRESS a : listAction) {
                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                    response.put("NO", no);
                    response.put("ADDRESS", a.getFullAddress());
                    response.put("TYPE", a.getType());
                    response.put("BUILDING", a.getBuilding());
                    response.put("FLOOR", a.getFloor());
                    response.put("HOUSE NAME", a.getHouseName());
                    response.put("STREET NAME", a.getStreetName());
                    response.put("BLOCK", a.getBlock());
                    response.put("HOUSE NUMBER", a.getHouseNumber());
                    response.put("RT", a.getNeighborhood1());
                    response.put("RW", a.getNeighborhood2());
                    response.put("ADDITIONAL NOTE", a.getAdditionalInfo());
                    response.put("SUB DISTRICT", a.getSubDistrict());
                    response.put("DISTRICT", a.getSubDistrict());
                    response.put("CITY", a.getCity());
                    response.put("PROVINCE", a.getProvince());
                    response.put("COUNTRY", a.getCountry());
                    response.put("POSTAL CODE", a.getPostalCode());
                    response.put("DESCRIPTION", a.getDescription());
                    response.put("STATUS", capitalizeFully(a.getStatus()));
                    allData.add(response);
                    no = no + 1;
                }
                ByteArrayInputStream in = DownloadToExcel.downloadsFiles(allData, "Excel");
                headers.add(Constant.CONTENT_DISPOSITION, "attachment; filename=" +
                        "ADDRESS_" + UtilsDate.dateToString(new Date(), "yyyyMMddHHmmss") + ".xlsx");
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

    public ResponseEntity<ResponseObject> createMasterAddress(CreateUpdateDTO request) {
        ResponseObject result;
        try {
            ResponseEntity<ResponseObject> validate = this.validateCreateMasterAddress(Boolean.FALSE, request);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            M_ADDRESSES newAddress = objectMapper.convertValue(request, M_ADDRESSES.class);
            newAddress.setCreatedBy(UserDetailUtils.getUsername());
            newAddress.setCreatedDate(new Date());
            newAddress.setStatus(FlowStatus.ACTIVE.name());
            mAddressRepo.save(newAddress);

//            M_ADDRESSES newAddress = this.saveAddress(request);
            
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED, UtilsAccount.messageSuccess(ConstantAccount.CREATE, ConstantAccount.MASTER_ADDRESS), newAddress);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    //FOR CHANGE REQUEST
//    public M_ADDRESSES saveAddress(CreateUpdateDTO request) {
//
//        M_ADDRESSES newAddress = new M_ADDRESSES();
//        newAddress.setCountryId(request.getCountryId());
//        newAddress.setProvinceId(request.getProvinceId());
//        newAddress.setCityId(request.getCityId());
//        newAddress.setDistrictId(request.getDistrictId());
//        newAddress.setSubDistrictId(request.getSubDistrictId());
//        newAddress.setPostalCodeId(request.getPostalCodeId());
//        newAddress.setBuildingPrefix(request.getBuilding().getPrefix());
//        newAddress.setBuilding(request.getBuilding().getValue());
//        newAddress.setFloorPrefix(request.getFloor().getPrefix());
//        newAddress.setFloor(request.getFloor().getValue());
//        newAddress.setHouseName(request.getHouseName());
//        newAddress.setStreetNamePrefix(request.getStreetName().getPrefix());
//        newAddress.setStreetName(request.getStreetName().getValue());
//        newAddress.setBlock(request.getBlock());
//        newAddress.setHouseNumber(request.getHouseNumber());
//        newAddress.setNeighborhood1Prefix(request.getNeighborhood1().getPrefix());
//        newAddress.setNeighborhood1(request.getNeighborhood1().getValue());
//        newAddress.setNeighborhood2Prefix(request.getNeighborhood2().getPrefix());
//        newAddress.setNeighborhood2(request.getNeighborhood2().getValue());
//        newAddress.setType(request.getType());
//        newAddress.setAdditionalInfo(request.getAdditionalInfo());
//        newAddress.setDescription(request.getDescAddress());
//        newAddress.setSource(request.getSource());
//        newAddress.setLongitude(request.getLongitude());
//        newAddress.setLatitude(request.getLatitude());
//        newAddress.setFullAddress(request.getFullAddress());
//        newAddress.setCreatedBy(UserDetailUtils.getUsername());
//        newAddress.setCreatedDate(new Date());
//        newAddress.setStatus(FlowStatus.ACTIVE.name());
//        mAddressRepo.save(newAddress);
//
//        return newAddress;
//    }

    public ResponseEntity<ResponseObject> validateCreateMasterAddress(Boolean api, CreateUpdateDTO request) {
        try {
            Set<ConstraintViolation<CreateUpdateDTO>> violations = this.validator.validate(request);
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

//            // FOR CHANGE REQUEST
//            if (!StringUtils.hasValue(request.getStreetName().getPrefix()) || !StringUtils.hasValue(request.getStreetName().getValue())) {
//                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
//                        "Street name cannot be empty", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
//            }

            // UNIQUE FULLADDRESS
            Optional<M_ADDRESSES> cekFullAddressExist = mAddressRepo.findByFullAddress(request.getFullAddress());
            if(cekFullAddressExist.isPresent()){
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "The Address have been registered", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }

            if(Boolean.TRUE.equals(api)) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        UtilsAccount.messageSuccess(ConstantAccount.VALIDATE, ConstantAccount.MASTER_ADDRESS), ResponseUtils.DATA_EMPTY), HttpStatus.OK);
            } else {
                return null;
            }

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> listLocationInformation(String locationType, Integer parentId){
        ResponseObject result;
        try{
            List<M_LOCATION> listCountry = mLocationRepo.findAllByLocationTypeAndLocationParent(locationType, parentId);
            if (listCountry.isEmpty()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, null), HttpStatus.NOT_FOUND);
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_OK, listCountry);
            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> updateAddress(CreateUpdateDTO request){
        ResponseObject result;
        try {

            ResponseEntity<ResponseObject> validate = this.validateUpdateAddress(Boolean.FALSE, request);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            M_ADDRESSES updatedData = mAddressRepo.findByAddressId(request.getAddressId());
            updatedData.setDescription(request.getDescription());
            updatedData.setSource(request.getSource());
            updatedData.setUpdatedBy(UserDetailUtils.getUsername());
            updatedData.setUpdatedDate(new Date());
            mAddressRepo.save(updatedData);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED, UtilsAccount.messageSuccess(ConstantAccount.UPDATE, ConstantAccount.MASTER_ADDRESS), ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateUpdateAddress(Boolean api, CreateUpdateDTO request){
        ResponseObject result;
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
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, validateHeader.get(0), violationHeaderList);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            Optional<M_ADDRESSES> addressData = mAddressRepo.findById(request.getAddressId());
            if (addressData.isEmpty()){
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        UtilsAccount.messageDataNotFound(ConstantAccount.MASTER_ADDRESS, request.getAddressId()), ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
            }

            if(Boolean.TRUE.equals(api)) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        UtilsAccount.messageSuccess(ConstantAccount.VALIDATE, ConstantAccount.MASTER_ADDRESS), ResponseUtils.DATA_EMPTY), HttpStatus.OK);
            } else {
                return null;
            }
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("java:S3776")
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> activateInactiveDataAddress(MasterAddressInactiveDTO request) {
        ResponseObject result;
        String message;
        try {
            Optional<M_ADDRESSES> data = mAddressRepo.findById(request.getId());
            if(data.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Master Address with id " + request.getId() + " is not found.", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            M_ADDRESSES ma = data.get();
            LinkedHashMap<String, Object> oldMa = new LinkedHashMap<>();
            oldMa.put("addressId", ma.getAddressId());
            oldMa.put("description", ma.getDescription());
            AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
            ObjectMapper mapper = new ObjectMapper();
            String oldValue = mapper.writeValueAsString(oldMa);
            auditTrail.setOldValue(oldValue);
            if(request.getRemark().equalsIgnoreCase("")) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "remark not be empty", ResponseUtils.MESSAGE_BAD_REQUEST);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
                
            //set remarks
            auditTrail.setRemark(request.getRemark());
            auditTrail.setTableName("M_ADDRESSES");
            Optional<M_USER> user = userRepo.findByUsername(UserDetailUtils.getUsername());
            if (user.isPresent()) {
                R_GLOBAL_TYPE_VALUE rUserLevel = globalTypeValueService.getGlobalTypeByGlbValue(Constant.USER_LEVEL, user.get().getUserLevel());
                String userLevel = rUserLevel.getName() != null ? rUserLevel.getName() : "";
                auditTrail.setUserLevel(userLevel);
            } else {
                auditTrail.setUserLevel("");
            }
            auditTrail.setDataId(ma.getAddressId().toString());
            auditTrail.setCreatedBy(UserDetailUtils.getUsername());
            auditTrail.setCreatedDate(new Date());
            if (ma.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())) {
                Optional<M_ACCOUNT_ADDRESS> checkAccount = mAccountAddressRepo.findTopByAddressIdAndStatus(request.getId(), FlowStatus.ACTIVE.name());
                if(checkAccount.isPresent()) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                            "You cannot inactivate address because this address is being used in account", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                ma.setStatus(FlowStatus.INACTIVE.name());
                auditTrail.setOperation(FlowStatus.INACTIVE.name());
                message = ResponseUtils.MESSAGE_INACTIVE;
            } else if(ma.getStatus().equalsIgnoreCase(FlowStatus.INACTIVE.name())) {
                ma.setStatus(FlowStatus.ACTIVE.name());
                auditTrail.setOperation(FlowStatus.ACTIVE.name());
                message = ResponseUtils.MESSAGE_ACTIVE;
            }else{
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Master Address Status not found or null", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            ma.setUpdatedBy(UserDetailUtils.getUsername() != null ? UserDetailUtils.getUsername() : null);
            ma.setUpdatedDate(new Date());
            M_ADDRESSES saveWT = mAddressRepo.save(ma);

            LinkedHashMap<String, Object> newMa = new LinkedHashMap<>();
            newMa.put("addressId", saveWT.getAddressId());
            newMa.put("description", saveWT.getDescription());

            String newValue = mapper.writeValueAsString(newMa);
            auditTrail.setNewValue(newValue);
            auditTrailRepo.save(auditTrail);
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    message, saveWT);

            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings("java:S1141")
    public ResponseEntity<ResponseObject> detailAddress(Integer addressId){
        ResponseObject result;
        try{
            Optional<M_ADDRESSES> getMAddress = mAddressRepo.findById(addressId);
            if(getMAddress.isEmpty()){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        UtilsAccount.messageDataNotFound(ConstantAccount.MASTER_ADDRESS, addressId), ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            M_ADDRESSES mAddresses = getMAddress.get();
            DetailAddressResponseDto detailAddressResponseDto = new DetailAddressResponseDto();
            AddressInformationResponseDto information = new AddressInformationResponseDto();
            information.setAddressId(mAddresses.getAddressId());
            information.setCountryId(mAddresses.getCountryId());
            Optional<M_LOCATION> findCountry= mLocationRepo.findByLocationIdAndLocationType(mAddresses.getCountryId(), Constant.LOCATION_TYPE_COUNTRY);
            findCountry.ifPresent(mLocation -> information.setCountry(mLocation.getLocationName()));
            information.setDistrictId(mAddresses.getDistrictId());
            Optional<M_LOCATION> findDistrict= mLocationRepo.findByLocationIdAndLocationType(mAddresses.getDistrictId(), Constant.LOCATION_TYPE_DISTRICT);
            findDistrict.ifPresent(mLocation -> information.setDistrict(mLocation.getLocationName()));
            information.setBuilding(mAddresses.getBuilding());
            information.setStreetName(mAddresses.getStreetName());
            information.setBlock(mAddresses.getBlock());
            information.setRt(mAddresses.getNeighborhood1());
            information.setAdditionalInfo(mAddresses.getAdditionalInfo());
            information.setAddress(mAddresses.getFullAddress());

            information.setProvinceId(mAddresses.getProvinceId());
            Optional<M_LOCATION> findProvince= mLocationRepo.findByLocationIdAndLocationType(mAddresses.getProvinceId(), Constant.LOCATION_TYPE_PROVINCE);
            findProvince.ifPresent(mLocation -> information.setProvince(mLocation.getLocationName()));

            information.setSubDistrictId(mAddresses.getSubDistrictId());
            Optional<M_LOCATION> findSubDistrict= mLocationRepo.findByLocationIdAndLocationType(mAddresses.getSubDistrictId(), Constant.LOCATION_TYPE_SUB_DISTRICT);
            findSubDistrict.ifPresent(mLocation -> information.setSubDistrict(mLocation.getLocationName()));
            information.setFloor(mAddresses.getFloor());
            information.setStreetNumber(mAddresses.getStreetNumber());
            information.setRw(mAddresses.getNeighborhood2());

            information.setCityId(mAddresses.getCityId());
            Optional<M_LOCATION> findCity= mLocationRepo.findByLocationIdAndLocationType(mAddresses.getCityId(), Constant.LOCATION_TYPE_CITY);
            findCity.ifPresent(mLocation -> information.setCity(mLocation.getLocationName()));
            information.setPostalCodeId(mAddresses.getPostalCodeId());
            Optional<M_LOCATION> findPostalCode= mLocationRepo.findByLocationIdAndLocationType(mAddresses.getPostalCodeId(), Constant.LOCATION_TYPE_POSTAL_CODE);
            findPostalCode.ifPresent(mLocation -> information.setPostalCode(mLocation.getLocationName()));
            information.setHouseName(mAddresses.getHouseName());
            information.setHouseNumber(mAddresses.getHouseNumber());
            information.setTypeId(mAddresses.getType());
            R_GLOBAL_TYPE_VALUE globalTypeAddressType = globalTypeValueService.getGlobalTypeByGlbTypeValId(Constant.ADDRESS_TYPE_NAME, mAddresses.getType());
            information.setType(StringUtils.hasValue(globalTypeAddressType) ? globalTypeAddressType.getName() : null);
            information.setDescription(mAddresses.getDescription());
            information.setFullAddress(mAddresses.getFullAddress());
            detailAddressResponseDto.setInformation(information);

            AddressCoordinateInformationDto coordinateInformationDto = new AddressCoordinateInformationDto();
            coordinateInformationDto.setSource(mAddresses.getSource());
            coordinateInformationDto.setLongtitude(mAddresses.getLongitude());
            coordinateInformationDto.setLatitude(mAddresses.getLatitude());
            coordinateInformationDto.setAltitude(mAddresses.getAltitude());
            detailAddressResponseDto.setCoordinateInformation(coordinateInformationDto);

            HistoryLogInformationResponseDto historyLogInformationResponseDto = new HistoryLogInformationResponseDto();
            historyLogInformationResponseDto.setId(mAddresses.getAddressId());
            if(!ObjectUtils.isEmpty(mAddresses.getCreatedDate())){
                try{
                    historyLogInformationResponseDto.setCreatedDate(UtilsDate.dateToString(mAddresses.getCreatedDate(), "dd MMM yyyy hh:mm:ss"));
                }catch (Exception e){
                    historyLogInformationResponseDto.setCreatedDate(null);
                }
            }
            historyLogInformationResponseDto.setCreatedBy(mAddresses.getCreatedBy());
            if(!ObjectUtils.isEmpty(mAddresses.getUpdatedDate())){
                try{
                    historyLogInformationResponseDto.setUpdatedDate(UtilsDate.dateToString(mAddresses.getUpdatedDate(), "dd MMM yyyy hh:mm:ss"));
                }catch (Exception e){
                    historyLogInformationResponseDto.setUpdatedDate(null);
                }
            }
            historyLogInformationResponseDto.setUpdatedBy(mAddresses.getUpdatedBy());
            detailAddressResponseDto.setHistoryLogInformation(historyLogInformationResponseDto);

            //acr
            List<AUDIT_TRAIL> auditTrail = auditTrailRepo.findAll().stream()
                    .filter(e -> e.getTableName().equalsIgnoreCase("M_ADDRESSES"))
                    .filter(f -> f.getDataId().equalsIgnoreCase(mAddresses.getAddressId().toString()))
                    .collect(Collectors.toList());
            detailAddressResponseDto.setActiveInactiveLog(auditTrail);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_OK,
                    detailAddressResponseDto);

            return new ResponseEntity<>(result, result.getHttpCode());

        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getFromGlobalType(String name){
        ResponseObject result;
        try {
            List<LinkedHashMap<String, Object>> allData = globalTypeValueService.getGlobalTypeByGroupName(name);
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, UtilsAccount.messageSuccess(ConstantAccount.DDL, name), allData);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getChooseAddressCustom(String type, @Nullable Integer customerId, MaterialTablePagingRequest pagingData,
                                                                 PagedResourcesAssembler<VW_CHOOSE_ADDRESS> assembler) {
        logger.info("Get List Choose Address");
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

//            List<Integer> idAddress = new ArrayList<>();
//            Optional<VW_CUSTOMER_ADDDRESS> optCustomerId = vwCustomerAddressRepo.findTopByAccountId(accountId);
//            if(optCustomerId.isPresent()) {
//                List<VW_CUSTOMER_ADDDRESS> optAddress = vwCustomerAddressRepo.findAllByCustomerId(optCustomerId.get().getCustomerId());
//                if(!optAddress.isEmpty()){
//                    for(VW_CUSTOMER_ADDDRESS ctc : optAddress) {
//                        idAddress.add(ctc.getAddressId());
//                    }
//                    // delete duplicate id address
//                    Set<Integer> uniqueIds = new HashSet<>(idAddress);
//                    idAddress.clear();
//                    idAddress.addAll(uniqueIds);
//                }
//            }

            List<Integer> idAddress = new ArrayList<>();
            if(customerId!=null) {
                List<VW_CUSTOMER_ADDDRESS> optAddress = vwCustomerAddressRepo.findAllByCustomerId(customerId);
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

            List<LinkedHashMap<String, Object>> allData = this.getResponseChoose(data.getContent());

            PagedModel<EntityModel<VW_CHOOSE_ADDRESS>> pagedData = assembler.toModel(data);
            Map<String, Object> d = new HashMap<>();
            d.put("result", allData);
            d.put("page", pagedData.getMetadata());
            d.put("links", pagedData.getLinks());
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.PAGING, ConstantAccount.MASTER_ADDRESS), d), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<LinkedHashMap<String, Object>> getResponseChoose(List<VW_CHOOSE_ADDRESS> dataList) {
        return dataList.stream()
                .map(g -> {
                    LinkedHashMap<String, Object> address = new LinkedHashMap<>();
                    address.put("address", g.getAddressId());
                    address.put("fullAddress", g.getFullAddress());
                    address.put("description", g.getDescription());
                    address.put("additionalNote", g.getAdditionalInfo());
                    address.put("houseName", g.getHouseName());
                    address.put("streetName", g.getStreetName());
                    address.put("block", g.getBlock());
                    address.put("houseNumber", g.getHouseNumber());
                    address.put("rt", g.getNeighborhood1());
                    address.put("rw", g.getNeighborhood2());
                    address.put("building", g.getBuilding());
                    address.put("floor", g.getFloor());
                    address.put("postalCode", g.getPostalCode());
                    address.put("subDistrict", g.getSubDistrict());
                    address.put("city", g.getCity());
                    address.put("province", g.getProvince());
                    address.put("country", g.getCountry());
                    address.put("district", g.getDistrict());
                    address.put("source", g.getSource());
                    return address;
                })
                .collect(Collectors.toList());
    }
}
