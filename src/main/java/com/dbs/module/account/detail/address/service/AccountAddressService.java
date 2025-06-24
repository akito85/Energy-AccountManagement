package com.dbs.module.account.detail.address.service;

import com.dbs.common.library.services.CriteriaServices;
import com.dbs.common.library.utils.CommonVariables;
import com.dbs.module.account.detail.address.dto.AccountAddressDTO;
import com.dbs.module.account.detail.address.dto.AccountAddressCreateDTO;
import com.dbs.module.account.detail.address.dto.AddressesResponseDto;
import com.dbs.module.account.detail.address.dto.AccountAddressInactiveDTO;
import com.dbs.module.account.detail.address.dto.AccountAddressUpdateDTO;
import com.dbs.module.account.detail.address.dto.BooleanObjectDTO;
import antlr.Utils;
import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.FlowStatus;
import com.dbs.common.library.utils.StringUtils;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.database.crm.entities.accountmanagement.*;
import com.dbs.database.crm.entities.product.M_LOCATION;
import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import com.dbs.database.crm.entities.usermanagement.M_USER;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.accountmanagement.Account.*;
import com.dbs.database.crm.repositories.accountmanagement.VwChooseAddressRepo;
import com.dbs.database.crm.repositories.usermanagement.AuditTrailRepo;
import com.dbs.database.crm.repositories.usermanagement.MUserRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import com.dbs.module.account.main.dto.MLocationDetailDTO;
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
import org.springframework.util.ObjectUtils;

import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

import static com.dbs.common.base.utils.CommonHelper.removeSpace;
import com.dbs.database.crm.repositories.mastermanagement.MLocationsRepo;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class AccountAddressService {

    private static final Logger logger = LoggerFactory.getLogger(AccountAddressService.class);

    @Autowired
    private MAccountAddressRepo accountAddressRepo;
    @Autowired
    private MUserRepo userRepo;
    @Autowired
    private GlobalTypeValueService globalTypeValueService;
    @Autowired
    private AuditTrailRepo auditTrailRepo;
    @Autowired
    private MaddressRepo mAddressRepo;
    @Autowired
    private MLocationsRepo mLocationRepo;
    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;
    @Autowired
    private Validator validator;
    @Autowired
    private MAccountRepo mAccountRepo;
    @Autowired
    private VWCustomerAddressRepo vwAddressRepo;
    @Autowired
    private MAccountContactRepo contactRepo;
    @Autowired
    private MTaxIdentifierRepo taxRepo;
    @Autowired
    private VwAssetAssignmentRepo vwAssetAssignRepo;

    @Autowired
    private VwChooseAddressRepo vwChooseAddressRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UtilsAccount.GlobalTypeServiceAccount globalTypeServiceAccount;
    @Autowired
    private CriteriaServices criteriaServices;

    public ResponseEntity<ResponseObject> getBusinessPurposeList() {
        logger.info("Business Purpose");
        ResponseObject result;
        try {
            List<LinkedHashMap<String, Object>> allData = criteriaServices.getCriteriaByGlobalType("Business Purpose");
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
//            List<R_GLOBAL_TYPE_VALUE> getListTypeValue = rGlobalTypeValueRepo.findByGlobalType(38);
//            List<MLocationDetailDTO> listType = new ArrayList<>();
//            int index = 0;
//
//            for (R_GLOBAL_TYPE_VALUE type : getListTypeValue) {
//                MLocationDetailDTO locDTO = new MLocationDetailDTO();
//                locDTO.setId(type.getGlbTypeValId());
//                locDTO.setName(type.getName());
//                listType.add(index++, locDTO);
//            }
//
//            result.setSuccess(true);
//            result.setCode(HttpStatus.OK);
//            result.setMessage("Success Get List Business Purpose");
//            result.setData(listType);
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

    public ResponseEntity<ResponseObject> getTypeHomeList() {
        ResponseObject result = new ResponseObject();

        try {

            List<R_GLOBAL_TYPE_VALUE> getListTypeValue = rGlobalTypeValueRepo.findByGlobalType(33);
            List<MLocationDetailDTO> listType = new ArrayList<>();
            int index = 0;

            for (R_GLOBAL_TYPE_VALUE type : getListTypeValue) {
                MLocationDetailDTO locDTO = new MLocationDetailDTO();
                locDTO.setId(type.getGlbTypeValId());
                locDTO.setName(type.getName());
                listType.add(index++, locDTO);
            }

            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success Get List Type Home");
            result.setData(listType);

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {

            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<M_ACCOUNT_ADDRESS> getAllAccountAddress() {
        return (List<M_ACCOUNT_ADDRESS>) accountAddressRepo.findAll();
    }

    @SuppressWarnings("java:S3776")
    public ResponseEntity<ResponseObject>  getAccountAddressByAccountId(Integer accountId, MaterialTablePagingRequest pagingData,
			PagedResourcesAssembler<VW_CUSTOMER_ADDDRESS> assembler) {
        
        logger.info("Get List Account Address By Account Id");

        ResponseObject result = new ResponseObject();

        try {
            Page<VW_CUSTOMER_ADDDRESS> accountAddress;
            Map<String, Object> filter = new HashMap<>(); // For Default Filter
            filter.put("accountId", accountId);
            if (pagingData.getSort() != null && pagingData.getSort().isEmpty()) {
                List<String> sort = new ArrayList<>();
                sort.add("createdDate~desc");
                pagingData.setSort(sort);
            }

            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key+"~"+value);
                }
            }
            if (!pagingData.getSearch().isEmpty()) {
                accountAddress = this.vwAddressRepo.findAll(this.vwAddressRepo.getSpecificationFromFilters(pagingData, filter),
                        PagingUtils.getPaging(pagingData));
            } else {
                accountAddress = this.vwAddressRepo.findAll(this.vwAddressRepo.getSpecificationDefault(filter),
                        PagingUtils.getPaging(pagingData));
            }
            
            List<VW_CUSTOMER_ADDDRESS> newData = accountAddress.getContent();

            List<AccountAddressDTO> newDtoData = new ArrayList<>();

            for (int i=0; i < newData.size(); i++) {
                AccountAddressDTO dataAddressNew = new AccountAddressDTO();
                
                dataAddressNew.setAccountAddressId(newData.get(i).getAccountAddressId());

                dataAddressNew.setAddressId(newData.get(i).getAddressId());

                if(newData.get(i).getPrimaryFlag()) {
                    BooleanObjectDTO newBool = new BooleanObjectDTO();
                    newBool.setBool(true);
                    newBool.setValue("Primary");
                    dataAddressNew.setPrimary(newBool);
                }else{
                    BooleanObjectDTO newBool = new BooleanObjectDTO();
                    newBool.setBool(false);
                    newBool.setValue("-");
                    dataAddressNew.setPrimary(newBool);
                }

                // Get Address on M_ADDRESSES
                Optional<M_ADDRESSES> address = mAddressRepo.findById(newData.get(i).getAddressId());
                if (address.isPresent()) {
                    dataAddressNew.setMAddressId(address.get().getAddressId());
                    dataAddressNew.setFullAddress(address.get().getFullAddress());
                    dataAddressNew.setDescription_accountAddress(newData.get(i).getDescriptionAccountAddress());
                    dataAddressNew.setDescription_mAddress(newData.get(i).getDescriptionAddress());
                    dataAddressNew.setAdditionalNote(address.get().getAdditionalInfo());
                    Optional<R_GLOBAL_TYPE_VALUE> getTypeValue = rGlobalTypeValueRepo.findByGlbTypeValId(address.get().getType());
                    if (getTypeValue.isPresent()) {
                        MLocationDetailDTO locDTO = new MLocationDetailDTO();
                        locDTO.setId(getTypeValue.get().getGlbTypeValId() != null ? getTypeValue.get().getGlbTypeValId() : null);
                        locDTO.setName(getTypeValue.get().getGlbValue() != null ? getTypeValue.get().getName() : null );
                        dataAddressNew.setType(locDTO);
                    }
                    dataAddressNew.setHouseName(address.get().getHouseName());
                    dataAddressNew.setHouseNumber(address.get().getHouseNumber());
                    dataAddressNew.setStreetName(address.get().getStreetName());
                    dataAddressNew.setStreetNumber(address.get().getStreetNumber());
                    dataAddressNew.setRt(address.get().getNeighborhood1());
                    dataAddressNew.setRw(address.get().getNeighborhood2());
                    dataAddressNew.setBuilding(address.get().getBuilding());
                    dataAddressNew.setFloor(address.get().getFloor());
                    dataAddressNew.setBlock(address.get().getBlock());
                    M_LOCATION getPostalCode = mLocationRepo.findByLocationId(address.get().getPostalCodeId());
                    if (getPostalCode != null) {
                        MLocationDetailDTO locDTO = new MLocationDetailDTO();
                        locDTO.setId(getPostalCode.getLocationId());
                        locDTO.setName(getPostalCode.getLocationName());
                        dataAddressNew.setPostalCode(locDTO);
                    }
                    M_LOCATION getSubDistrict = mLocationRepo.findByLocationId(address.get().getSubDistrictId());
                    if (getSubDistrict != null) {
                        MLocationDetailDTO locDTO = new MLocationDetailDTO();
                        locDTO.setId(getSubDistrict.getLocationId());
                        locDTO.setName(getSubDistrict.getLocationName());
                        dataAddressNew.setSubDistrict(locDTO);
                    }
                    M_LOCATION getDistrictValue = mLocationRepo.findByLocationId(address.get().getDistrictId());
                    if (getDistrictValue != null) {
                        MLocationDetailDTO locDTO = new MLocationDetailDTO();
                        locDTO.setId(getDistrictValue.getLocationId());
                        locDTO.setName(getDistrictValue.getLocationName());
                        dataAddressNew.setDistrict(locDTO);
                    }
                    M_LOCATION getCityValue = mLocationRepo.findByLocationId(address.get().getCityId());
                    if (getCityValue != null) {
                        MLocationDetailDTO locDTO = new MLocationDetailDTO();
                        locDTO.setId(getCityValue.getLocationId());
                        locDTO.setName(getCityValue.getLocationName());
                        dataAddressNew.setCity(locDTO);
                    }
                    M_LOCATION getProvinceValue = mLocationRepo.findByLocationId(address.get().getProvinceId());
                    if (getProvinceValue != null) {
                        MLocationDetailDTO locDTO = new MLocationDetailDTO();
                        locDTO.setId(getProvinceValue.getLocationId());
                        locDTO.setName(getProvinceValue.getLocationName());
                        dataAddressNew.setProvince(locDTO);
                    }
                    M_LOCATION getCountryValue = mLocationRepo.findByLocationId(address.get().getCountryId());
                    if (getCountryValue != null) {
                        MLocationDetailDTO locDTO = new MLocationDetailDTO();
                        locDTO.setId(getCountryValue.getLocationId());
                        locDTO.setName(getCountryValue.getLocationName());
                        dataAddressNew.setCountry(locDTO);
                    }
                }

                if(newData.get(i).getPremiseFlag()) {
                    BooleanObjectDTO newBool = new BooleanObjectDTO();
                    newBool.setBool(true);
                    newBool.setValue("Yes");
                    dataAddressNew.setPremise(newBool);
                }else{
                    BooleanObjectDTO newBool = new BooleanObjectDTO();
                    newBool.setBool(false);
                    newBool.setValue("No");
                    dataAddressNew.setPremise(newBool);
                }

                dataAddressNew.setStatus(newData.get(i).getStatus());
                dataAddressNew.setCreatedBy(newData.get(i).getCreatedBy());
                dataAddressNew.setCreatedDate(newData.get(i).getCreatedDate());
                dataAddressNew.setUpdatedBy(newData.get(i).getUpdatedBy());
                dataAddressNew.setUpdatedDate(newData.get(i).getUpdatedDate());

                if(!ObjectUtils.isEmpty(newData.get(i).getBusinessPurpose())){
                    Optional<R_GLOBAL_TYPE_VALUE> getBp = rGlobalTypeValueRepo.findById(newData.get(i).getBusinessPurposeId());
                    Map<String, Object> businessPurposeInformation = new HashMap<>();
                    List<Map<String, Object>> businessPurposeParent = new ArrayList<>();
                    if (getBp.isPresent()) {
                        businessPurposeInformation.put("id", getBp.get().getGlbTypeValId());
                        businessPurposeInformation.put("name", getBp.get().getName());
                        businessPurposeParent.add(businessPurposeInformation);
                        dataAddressNew.setBusinessPurpose(businessPurposeParent);
                    }
                }
                newDtoData.add(dataAddressNew);
            }

            PagedModel pagedData = assembler.toModel(accountAddress);

            Map<String, Object> d = new HashMap<>();
            d.put("result", newDtoData);
            d.put("page", pagedData.getMetadata());
            d.put("links", pagedData.getLinks());

            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success View Account Address");
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

    @SuppressWarnings("java:S1192")
    public ResponseEntity<ResponseObject> getChooseAddress(Integer accountId, MaterialTablePagingRequest pagingData,
                                                           PagedResourcesAssembler<VW_CHOOSE_ADDRESS> assembler) {
        logger.info("Get List Address, " + accountId);
        try {
            Map<String, Object> filter = new HashMap<>();
            filter.put("status", FlowStatus.ACTIVE.name());

            Page<VW_CHOOSE_ADDRESS> data;

            if(!pagingData.getSearch().isEmpty()) {
                data = vwChooseAddressRepo.findAll(vwChooseAddressRepo.getSpecificationFromFilters(pagingData, filter), PagingUtils.getPaging(pagingData));
            } else {
                data = vwChooseAddressRepo.findAll(vwChooseAddressRepo.getSpecificationDefault(filter), PagingUtils.getPaging(pagingData));
            }

            List<Integer> idAddress = new ArrayList<>();
            Optional<VW_CUSTOMER_ADDDRESS> optCustomerId = vwAddressRepo.findTopByAccountId(accountId);
            if(optCustomerId.isPresent()) {
                List<VW_CUSTOMER_ADDDRESS> optAddress = vwAddressRepo.findAllByCustomerId(optCustomerId.get().getCustomerId());
                if(!optAddress.isEmpty()){
                    for(VW_CUSTOMER_ADDDRESS ctc : optAddress) {
                        idAddress.add(ctc.getAddressId());
                    }
                    // delete duplicate id contact
                    Set<Integer> uniqueIds = new HashSet<>(idAddress);
                    idAddress.clear();
                    idAddress.addAll(uniqueIds);
                }
            }

            List<LinkedHashMap<String, Object>> allData = data.stream()
                    .map(g -> {
                        LinkedHashMap<String, Object> acontact = new LinkedHashMap<>();
                        acontact.put("address", g.getAddressId());
                        acontact.put("fullAddress", g.getFullAddress());
                        acontact.put("description", g.getDescription());
                        acontact.put("additionalNote", g.getAdditionalInfo());
                        acontact.put("houseName", g.getHouseName());
                        acontact.put("streetName", g.getStreetName());
                        acontact.put("block", g.getBlock());
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
                        acontact.put("source", idAddress.contains(g.getAddressId())? "CUSTOMER":"MASTER");
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
    public ResponseEntity<ResponseObject> getDetailAccountAddressByAccountAddressId(Integer accountAddressId) {
        logger.info("Get Detail Account Address");

        ResponseObject result = new ResponseObject();

        try { 
            Optional<M_ACCOUNT_ADDRESS> accountAddress = this.accountAddressRepo.findById(accountAddressId);

            if(accountAddress.isPresent()) {

                M_ACCOUNT_ADDRESS newData = accountAddress.get();

                AccountAddressDTO dataAddressNew = new AccountAddressDTO();

                dataAddressNew.setAddressId(newData.getId());

                if(newData.getPrimaryFlag()) {
                    BooleanObjectDTO newBool = new BooleanObjectDTO();
                    newBool.setBool(true);
                    newBool.setValue("Primary");
                    dataAddressNew.setPrimary(newBool);
                }else{
                    BooleanObjectDTO newBool = new BooleanObjectDTO();
                    newBool.setBool(false);
                    newBool.setValue("-");
                    dataAddressNew.setPrimary(newBool);
                }

                // Get Address on M_ADDRESSES
                Optional<M_ADDRESSES> address = mAddressRepo.findById(accountAddress.get().getAddressId());
                
                if (address.isPresent()) {
                    dataAddressNew.setFullAddress(address.get().getFullAddress());
                    dataAddressNew.setDescription_accountAddress(accountAddress.get().getDescription());
                    dataAddressNew.setDescription_mAddress(address.get().getDescription());
                    dataAddressNew.setAdditionalNote(address.get().getAdditionalInfo());
                    Optional<R_GLOBAL_TYPE_VALUE> getTypeValue = rGlobalTypeValueRepo.findByGlbTypeValId(address.get().getType());
                    if (getTypeValue.isPresent()) {
                        MLocationDetailDTO locDTO = new MLocationDetailDTO();
                        locDTO.setId(getTypeValue.get().getGlbTypeValId());
                        locDTO.setName(getTypeValue.get().getGlbValue());
                        dataAddressNew.setType(locDTO);
                    }
                    dataAddressNew.setHouseName(address.get().getHouseName());
                    dataAddressNew.setHouseNumber(address.get().getHouseNumber());
                    dataAddressNew.setBlock(address.get().getBlock());
                    dataAddressNew.setStreetName(address.get().getStreetName());
                    dataAddressNew.setStreetNumber(address.get().getStreetNumber());
                    dataAddressNew.setRt(address.get().getNeighborhood1());
                    dataAddressNew.setRw(address.get().getNeighborhood2());
                    dataAddressNew.setBuilding(address.get().getBuilding());
                    dataAddressNew.setFloor(address.get().getFloor());
                    M_LOCATION getPostalCode = mLocationRepo.findByLocationId(address.get().getPostalCodeId());
                    if (getPostalCode != null) {
                        MLocationDetailDTO locDTO = new MLocationDetailDTO();
                        locDTO.setId(getPostalCode.getLocationId());
                        locDTO.setName(getPostalCode.getLocationName());
                        dataAddressNew.setPostalCode(locDTO);
                    }
                    M_LOCATION getSubDistrict = mLocationRepo.findByLocationId(address.get().getSubDistrictId());
                    if (getSubDistrict != null) {
                        MLocationDetailDTO locDTO = new MLocationDetailDTO();
                        locDTO.setId(getSubDistrict.getLocationId());
                        locDTO.setName(getSubDistrict.getLocationName());
                        dataAddressNew.setSubDistrict(locDTO);
                    }
                    M_LOCATION getDistrictValue = mLocationRepo.findByLocationId(address.get().getDistrictId());
                    if (getDistrictValue != null) {
                        MLocationDetailDTO locDTO = new MLocationDetailDTO();
                        locDTO.setId(getDistrictValue.getLocationId());
                        locDTO.setName(getDistrictValue.getLocationName());
                        dataAddressNew.setDistrict(locDTO);
                    }
                    M_LOCATION getCityValue = mLocationRepo.findByLocationId(address.get().getCityId());
                    if (getCityValue != null) {
                        MLocationDetailDTO locDTO = new MLocationDetailDTO();
                        locDTO.setId(getCityValue.getLocationId());
                        locDTO.setName(getCityValue.getLocationName());
                        dataAddressNew.setCity(locDTO);
                    }
                    M_LOCATION getProvinceValue = mLocationRepo.findByLocationId(address.get().getProvinceId());
                    if (getProvinceValue != null) {
                        MLocationDetailDTO locDTO = new MLocationDetailDTO();
                        locDTO.setId(getProvinceValue.getLocationId());
                        locDTO.setName(getProvinceValue.getLocationName());
                        dataAddressNew.setProvince(locDTO);
                    }
                    M_LOCATION getCountryValue = mLocationRepo.findByLocationId(address.get().getCountryId());
                    if (getCountryValue != null) {
                        MLocationDetailDTO locDTO = new MLocationDetailDTO();
                        locDTO.setId(getCountryValue.getLocationId());
                        locDTO.setName(getCountryValue.getLocationName());
                        dataAddressNew.setCountry(locDTO);
                    }
                    dataAddressNew.setSource(address.get().getSource());
                    dataAddressNew.setLongitude(address.get().getLongitude());
                    dataAddressNew.setLatitude(address.get().getLatitude());
                    dataAddressNew.setAltitude(address.get().getAltitude());
                    dataAddressNew.setFullAddress(address.get().getFullAddress());
                }
                Optional<R_GLOBAL_TYPE_VALUE> getBp = rGlobalTypeValueRepo.findById(newData.getBusinessPurpose());
                if(getBp.isPresent()){
                    Map<String, Object> bpInformation = new HashMap<>();
                    List<Map<String, Object>> businessPurposeParent = new ArrayList<>();
                    bpInformation.put("id",newData.getBusinessPurpose());
                    bpInformation.put("name",getBp.get().getName());
                    businessPurposeParent.add(bpInformation);
                    dataAddressNew.setBusinessPurpose(businessPurposeParent);
                }
                if(newData.getPremiseFlag()) {
                    BooleanObjectDTO newBool = new BooleanObjectDTO();
                    newBool.setBool(newData.getPremiseFlag());
                    newBool.setValue("Yes");
                    dataAddressNew.setPremise(newBool);
                }else{
                    BooleanObjectDTO newBool = new BooleanObjectDTO();
                    newBool.setBool(newData.getPremiseFlag());
                    newBool.setValue("No");
                    dataAddressNew.setPremise(newBool);
                }

                dataAddressNew.setStatus(newData.getStatus());     
                dataAddressNew.setCreatedBy(newData.getCreatedBy());   
                dataAddressNew.setCreatedDate(newData.getCreatedDate());
                dataAddressNew.setUpdatedBy(newData.getUpdatedBy());
                dataAddressNew.setUpdatedDate(newData.getUpdatedDate());

                result.setSuccess(true);
                result.setCode(HttpStatus.OK);
                result.setMessage("Success View Detail Account Address");
                result.setData(dataAddressNew);

                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        } catch (Exception e) {

            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        result.setSuccess(ResponseUtils.SUCCESS_FALSE);
        result.setCode(HttpStatus.NOT_FOUND);
        result.setMessage("account address is not available");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @SuppressWarnings("java:S3776")
    public ResponseEntity<ResponseObject> getDetailAddressByAddressId(Integer accountAddressId){
        ResponseObject result = new ResponseObject();

        try{
            Optional<M_ACCOUNT_ADDRESS> dataAccAdd = accountAddressRepo.findById(accountAddressId);
            if(!dataAccAdd.isPresent()) {
                result.setSuccess(false);
                result.setCode(HttpStatus.BAD_REQUEST);
                result.setMessage("Account Address is not available");
                result.setData(ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            Optional<M_ADDRESSES> getAddress = mAddressRepo.findById(dataAccAdd.get().getAddressId());
            if(!getAddress.isPresent()){
                result.setSuccess(false);
                result.setCode(HttpStatus.BAD_REQUEST);
                result.setMessage("Address is not available");
                result.setData(ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }

            M_ACCOUNT_ADDRESS accountAddressInfo = dataAccAdd.get();
            M_ADDRESSES addressesInfo = getAddress.get();
            AddressesResponseDto addressResponse = new AddressesResponseDto();

            addressResponse.setAccountAddressId(accountAddressInfo.getId());
            addressResponse.setAddressId(addressesInfo.getAddressId());
            MLocationDetailDTO countryInfo = new MLocationDetailDTO();
            Optional<M_LOCATION> getCountry = mLocationRepo.findByLocationIdAndLocationType(addressesInfo.getCountryId(),Constant.LOCATION_TYPE_COUNTRY);
            if(getCountry.isPresent()){
                countryInfo.setId(addressesInfo.getCountryId());
                countryInfo.setName(getCountry.get().getLocationName());
                addressResponse.setCountry(countryInfo);
            }
            Optional<M_LOCATION> getProvince = mLocationRepo.findByLocationIdAndLocationType(addressesInfo.getProvinceId(),Constant.LOCATION_TYPE_PROVINCE);
            MLocationDetailDTO provinceInfo = new MLocationDetailDTO();
            if(getProvince.isPresent()){
                provinceInfo.setId(addressesInfo.getProvinceId());
                provinceInfo.setName(getProvince.get().getLocationName());
                addressResponse.setProvince(provinceInfo);
            }
            Optional<M_LOCATION> getCity = mLocationRepo.findByLocationIdAndLocationType(addressesInfo.getCityId(),Constant.LOCATION_TYPE_CITY);
            MLocationDetailDTO cityInformation = new MLocationDetailDTO();
            if(getCity.isPresent()){
                cityInformation.setId(addressesInfo.getCityId());
                cityInformation.setName(getCity.get().getLocationName());
                addressResponse.setCity(cityInformation);
            }
            Optional<M_LOCATION> getDistrict = mLocationRepo.findByLocationIdAndLocationType(addressesInfo.getDistrictId(),Constant.LOCATION_TYPE_DISTRICT);
            MLocationDetailDTO districtInformation = new MLocationDetailDTO();
            if(getDistrict.isPresent()){
                districtInformation.setId(addressesInfo.getDistrictId());
                districtInformation.setName(getDistrict.get().getLocationName());
                addressResponse.setDistrict(districtInformation);
            }
            Optional<M_LOCATION> getSubDistrict = mLocationRepo.findByLocationIdAndLocationType(addressesInfo.getSubDistrictId(),Constant.LOCATION_TYPE_SUB_DISTRICT);
            MLocationDetailDTO subDistrictInformation = new MLocationDetailDTO();
            if(getSubDistrict.isPresent()){
                subDistrictInformation.setId(addressesInfo.getSubDistrictId());
                subDistrictInformation.setName(getSubDistrict.get().getLocationName());
                addressResponse.setSubDistrict(subDistrictInformation);
            }
            Optional<M_LOCATION> getPostalCode = mLocationRepo.findByLocationIdAndLocationType(addressesInfo.getPostalCodeId(),Constant.LOCATION_TYPE_POSTAL_CODE);
            MLocationDetailDTO postalCodeInformation = new MLocationDetailDTO();
            if(getPostalCode.isPresent()){
                postalCodeInformation.setId(addressesInfo.getSubDistrictId());
                postalCodeInformation.setName(getPostalCode.get().getLocationName());
                addressResponse.setPostalCode(postalCodeInformation);
            }
            addressResponse.setTypeId(addressesInfo.getType());
            Optional<R_GLOBAL_TYPE_VALUE> typeName = rGlobalTypeValueRepo.findById(addressesInfo.getType());
            addressResponse.setType(typeName.isPresent() ? typeName.get().getName() : null);
            addressResponse.setSource(addressesInfo.getSource());
            addressResponse.setBuilding(addressesInfo.getBuilding());
            addressResponse.setFloor(addressesInfo.getFloor());
            addressResponse.setHouseName(addressesInfo.getHouseName());
            addressResponse.setStreetName(addressesInfo.getStreetName());
            addressResponse.setBlock(addressesInfo.getBlock());
            addressResponse.setHouseNumber(addressesInfo.getHouseNumber());
            addressResponse.setRt(addressesInfo.getNeighborhood1());
            addressResponse.setRw(addressesInfo.getNeighborhood2());
            addressResponse.setAdditionalNote(addressesInfo.getAdditionalInfo());
            addressResponse.setLongitude(addressesInfo.getLongitude());
            addressResponse.setLatitude(addressesInfo.getLatitude());
            addressResponse.setAltitude(addressesInfo.getAltitude());
            addressResponse.setStreetNumber(addressesInfo.getStreetNumber());
            addressResponse.setFullAddress(addressesInfo.getFullAddress());
            addressResponse.setDescAddress(addressesInfo.getDescription());
            
            Optional<R_GLOBAL_TYPE_VALUE> bPurpose = rGlobalTypeValueRepo.findById(accountAddressInfo.getBusinessPurpose());
            addressResponse.setBusinessPurpose(bPurpose.isPresent() ? bPurpose.get().getName() : null);
            addressResponse.setPremiseFlag(accountAddressInfo.getPremiseFlag());
            addressResponse.setPrimaryFlag(accountAddressInfo.getPrimaryFlag());
            addressResponse.setDescAccountAddress(accountAddressInfo.getDescription());
            
            addressResponse.setCreatedBy(accountAddressInfo.getCreatedBy());
            addressResponse.setCreatedDate(accountAddressInfo.getCreatedDate());
            addressResponse.setUpdatedBy(accountAddressInfo.getUpdatedBy());
            addressResponse.setUpdatedDate(accountAddressInfo.getUpdatedDate());
            //acr
            List<AUDIT_TRAIL> auditTrail = auditTrailRepo.findAll().stream()
                    .filter(e -> e.getTableName().equalsIgnoreCase("M_ACCOUNT_ADDRESS"))
                    .filter(f -> f.getDataId().equalsIgnoreCase(accountAddressInfo.getId().toString()))
                    .collect(Collectors.toList());
            addressResponse.setActiveInactiveLog(auditTrail);
            
            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success get detail addresses");
            result.setData(addressResponse);
            return new ResponseEntity<>(result, HttpStatus.OK);

        }catch (Exception e){
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @SuppressWarnings("java:S3776")
    public ResponseEntity<ResponseObject> getDetailChoose(Integer addressId){
        ResponseObject result = new ResponseObject();

        try{
            Optional<M_ADDRESSES> getAddress = mAddressRepo.findById(addressId);
            if(!getAddress.isPresent()){
                result.setSuccess(false);
                result.setCode(HttpStatus.BAD_REQUEST);
                result.setMessage("Address is not available");
                result.setData(ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }

            M_ADDRESSES addressesInfo = getAddress.get();
            AddressesResponseDto addressResponse = new AddressesResponseDto();

            addressResponse.setAddressId(addressesInfo.getAddressId());
            MLocationDetailDTO countryInfo = new MLocationDetailDTO();
            Optional<M_LOCATION> getCountry = mLocationRepo.findByLocationIdAndLocationType(addressesInfo.getCountryId(),"COUNTRY");
            if(getCountry.isPresent()){
                countryInfo.setId(addressesInfo.getCountryId());
                countryInfo.setName(getCountry.get().getLocationName());
                addressResponse.setCountry(countryInfo);
            }
            Optional<M_LOCATION> getProvince = mLocationRepo.findByLocationIdAndLocationType(addressesInfo.getProvinceId(),"PROVINCE");
            MLocationDetailDTO provinceInfo = new MLocationDetailDTO();
            if(getProvince.isPresent()){
                provinceInfo.setId(addressesInfo.getProvinceId());
                provinceInfo.setName(getProvince.get().getLocationName());
                addressResponse.setProvince(provinceInfo);
            }
            Optional<M_LOCATION> getCity = mLocationRepo.findByLocationIdAndLocationType(addressesInfo.getCityId(),"CITY");
            MLocationDetailDTO cityInformation = new MLocationDetailDTO();
            if(getCity.isPresent()){
                cityInformation.setId(addressesInfo.getCityId());
                cityInformation.setName(getCity.get().getLocationName());
                addressResponse.setCity(cityInformation);
            }
            Optional<M_LOCATION> getDistrict = mLocationRepo.findByLocationIdAndLocationType(addressesInfo.getDistrictId(),"DISTRICT");
            MLocationDetailDTO districtInformation = new MLocationDetailDTO();
            if(getDistrict.isPresent()){
                districtInformation.setId(addressesInfo.getDistrictId());
                districtInformation.setName(getDistrict.get().getLocationName());
                addressResponse.setDistrict(districtInformation);
            }
            Optional<M_LOCATION> getSubDistrict = mLocationRepo.findByLocationIdAndLocationType(addressesInfo.getSubDistrictId(),"SUB_DISTRICT");
            MLocationDetailDTO subDistrictInformation = new MLocationDetailDTO();
            if(getSubDistrict.isPresent()){
                subDistrictInformation.setId(addressesInfo.getSubDistrictId());
                subDistrictInformation.setName(getSubDistrict.get().getLocationName());
                addressResponse.setSubDistrict(subDistrictInformation);
            }
            Optional<M_LOCATION> getPostalCode = mLocationRepo.findByLocationIdAndLocationType(addressesInfo.getPostalCodeId(),"POSTAL_CODE");
            MLocationDetailDTO postalCodeInformation = new MLocationDetailDTO();
            if(getPostalCode.isPresent()){
                postalCodeInformation.setId(addressesInfo.getSubDistrictId());
                postalCodeInformation.setName(getPostalCode.get().getLocationName());
                addressResponse.setPostalCode(postalCodeInformation);
            }
            addressResponse.setTypeId(addressesInfo.getType());
            Optional<R_GLOBAL_TYPE_VALUE> typeName = rGlobalTypeValueRepo.findById(addressesInfo.getType());
            addressResponse.setType(typeName.isPresent() ? typeName.get().getName() : null);
            addressResponse.setSource(addressesInfo.getSource());
            addressResponse.setBuilding(addressesInfo.getBuilding());
            addressResponse.setFloor(addressesInfo.getFloor());
            addressResponse.setHouseName(addressesInfo.getHouseName());
            addressResponse.setStreetName(addressesInfo.getStreetName());
            addressResponse.setBlock(addressesInfo.getBlock());
            addressResponse.setHouseNumber(addressesInfo.getHouseNumber());
            addressResponse.setRt(addressesInfo.getNeighborhood1());
            addressResponse.setRw(addressesInfo.getNeighborhood2());
            addressResponse.setAdditionalNote(addressesInfo.getAdditionalInfo());
            addressResponse.setLongitude(addressesInfo.getLongitude());
            addressResponse.setLatitude(addressesInfo.getLatitude());
            addressResponse.setAltitude(addressesInfo.getAltitude());
            addressResponse.setStreetNumber(addressesInfo.getStreetNumber());
            addressResponse.setFullAddress(addressesInfo.getFullAddress());
            addressResponse.setRecordId(addressesInfo.getAddressId());
            addressResponse.setDescAddress(addressesInfo.getDescription());
            addressResponse.setCreatedBy(addressesInfo.getCreatedBy());
            addressResponse.setCreatedDate(addressesInfo.getCreatedDate());
            addressResponse.setUpdatedBy(addressesInfo.getUpdatedBy());
            addressResponse.setUpdatedDate(addressesInfo.getUpdatedDate());
            
            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success get detail addresses");
            result.setData(addressResponse);
            return new ResponseEntity<>(result, HttpStatus.OK);

        }catch (Exception e){
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @SuppressWarnings("java:S3776")
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> createAccountAddress(AccountAddressCreateDTO request) {

        logger.info("paramRequest -> {}", request);
        ResponseObject result;
        try {

            ResponseEntity<ResponseObject> validate = this.validateCreateAccountAddress(Boolean.FALSE, request);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            List<M_ACCOUNT_ADDRESS> accountAddressesHavePrimaryAndActive = accountAddressRepo.findAllByAccountIdAndPrimaryFlagAndStatus(request.getAccountId(), Boolean.TRUE, FlowStatus.ACTIVE.name());
            if(request.getNeedValidation()!=null && !request.getNeedValidation() && !accountAddressesHavePrimaryAndActive.isEmpty() && request.getPrimaryFlag()){
                for(M_ACCOUNT_ADDRESS accAddressPrimary : accountAddressesHavePrimaryAndActive) {
                    accAddressPrimary.setPrimaryFlag(Boolean.FALSE);
                    accAddressPrimary.setUpdatedBy(UserDetailUtils.getUsername());
                    accAddressPrimary.setUpdatedDate(new Date());
                    accountAddressRepo.save(accAddressPrimary);
                }
            }

            List<Object> addressDataCreated = new ArrayList<>();
            Integer savedAddress = 0;
            M_ADDRESSES mAddress = new M_ADDRESSES();
            for (Integer bp : request.getBusinessPurpose()) {
                if(request.getAddressId()==null && savedAddress<1) {
                    mAddress = objectMapper.convertValue(request,M_ADDRESSES.class);
                    try {
                        mAddress.setCreatedBy(UserDetailUtils.getUsername());
                    } catch (Exception e) {
                        mAddress.setCreatedBy(null);
                    }
                    mAddress.setBuilding(request.getBuilding()!=null?removeSpace(request.getBuilding().toUpperCase()):null);
                    mAddress.setHouseName(request.getHouseName()!=null?removeSpace(request.getHouseName().toUpperCase()):null);
                    mAddress.setStreetName(request.getStreetName()!=null?removeSpace(request.getStreetName().toUpperCase()):null);
                    mAddress.setBlock(request.getBlock()!=null?removeSpace(request.getBlock().toUpperCase()):null);
                    mAddress.setHouseNumber(request.getHouseNumber()!=null?removeSpace(request.getHouseNumber().toUpperCase()):null);
                    mAddress.setAdditionalInfo(request.getAdditionalNote()!=null?removeSpace(request.getAdditionalNote().toUpperCase()):null);
                    mAddress.setFullAddress(request.getAddress());
                    mAddress.setDescription(removeSpace(request.getDescAddress()));
                    mAddress.setNeighborhood1(request.getRt());
                    mAddress.setNeighborhood2(request.getRw());
                    mAddress.setType(request.getTypeId());
                    mAddress.setStatus(FlowStatus.ACTIVE.name());
                    mAddress.setCreatedDate(new Date());
                    mAddressRepo.save(mAddress);
                    savedAddress++;
                }
                M_ACCOUNT_ADDRESS accAddress = new M_ACCOUNT_ADDRESS();
                accAddress.setPrimaryFlag(request.getPrimaryFlag());
                R_GLOBAL_TYPE_VALUE getBp = globalTypeServiceAccount.getIdGlobalTypeByGroupNameAndValue("Business Purpose", "SHIP_TO");
                if(bp.equals(getBp.getGlbTypeValId())) {
                    accAddress.setPremiseFlag(Boolean.TRUE);
                } else {
                    accAddress.setPremiseFlag(Boolean.FALSE);
                }
                Optional<M_ACCOUNT_ADDRESS> cekPrimaryFlag = accountAddressRepo.findTopByAccountIdAndStatusAndPrimaryFlagIsTrue(request.getAccountId(), FlowStatus.ACTIVE.name());
                if(cekPrimaryFlag.isPresent()){
                    accAddress.setPrimaryFlag(Boolean.FALSE);
                }
                accAddress.setAddressId(request.getAddressId()==null?mAddress.getAddressId():request.getAddressId());
                accAddress.setCreatedDate(new Date());
                accAddress.setAccountId(request.getAccountId());
                accAddress.setBusinessPurpose(bp);
                accAddress.setDescription(removeSpace(request.getDescAccountAddress()));
                accAddress.setCreatedBy(UserDetailUtils.getUsername());
                accAddress.setStatus(FlowStatus.ACTIVE.name());
                accountAddressRepo.save(accAddress);
                addressDataCreated.add(accAddress);
            }
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                    UtilsAccount.messageSuccess(ConstantAccount.CREATE, ConstantAccount.ACCOUNT_ADDRESS), addressDataCreated);

            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateCreateAccountAddress(Boolean api, AccountAddressCreateDTO request) {
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

        Optional<M_ACCOUNT> getAccountInfo = mAccountRepo.findByAccountId(request.getAccountId());
        if (!getAccountInfo.isPresent()){
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    "Account is not available", ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
        }

        R_GLOBAL_TYPE_VALUE getBp = globalTypeServiceAccount.getIdGlobalTypeByGroupNameAndValue("Business Purpose", "SHIP_TO");
        if(!StringUtils.hasValue(getBp.getGlbTypeValId())) {
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                    getBp.getGlbValue(), ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
        }

        if(request.getAddressId()==null) {
            Optional<M_ADDRESSES> cekFullAddressExist = mAddressRepo.findByFullAddress(request.getAddress());
            if(cekFullAddressExist.isPresent()){
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "The Address have been registered", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
        } else {
            for(Integer bp : request.getBusinessPurpose()) {
                Optional<M_ACCOUNT_ADDRESS> checkExistAccountAddress = accountAddressRepo.findTopByAccountIdAndAddressIdAndBusinessPurposeAndStatus(request.getAccountId(), request.getAddressId(), bp, FlowStatus.ACTIVE.name());
                if(checkExistAccountAddress.isPresent()){
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "The Address and business purposes has been registered", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
            }
        }

        if(request.getPremiseFlag() && request.getAddressId() != null) {
            Optional<M_ACCOUNT_ADDRESS> cekPremiseActive = accountAddressRepo.findTopByAddressIdAndPremiseFlagAndStatus(request.getAddressId(), Boolean.TRUE, FlowStatus.ACTIVE.name());
            if(cekPremiseActive.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "This address is already registered as a premise on another account", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
        }

        List<M_ACCOUNT_ADDRESS> accountAddressesHavePrimaryAndActive = accountAddressRepo.findAllByAccountIdAndPrimaryFlagAndStatus(request.getAccountId(), Boolean.TRUE, FlowStatus.ACTIVE.name());
        if(request.getPrimaryFlag() && request.getNeedValidation() && !accountAddressesHavePrimaryAndActive.isEmpty()){
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
                    "validation primary address", UtilsAccount.messageWarningInactivePrimary(ConstantAccount.ACCOUNT_ADDRESS)), HttpStatus.OK);
        }

        return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.ACCOUNT_ADDRESS);
    }

    public ResponseEntity<ResponseObject> validateUpdateAccountAddress(Boolean api, AccountAddressUpdateDTO request){
        Optional<M_ACCOUNT_ADDRESS> existingAccountAddress = accountAddressRepo.findById(request.getAddressId());
        if (!existingAccountAddress.isPresent()) {
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    "Account address is not available", ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
        }
        M_ACCOUNT_ADDRESS accountAddress = existingAccountAddress.get();
        List<M_ACCOUNT_ADDRESS> accountAddressesHavePrimaryAndActive = accountAddressRepo.findAllByAccountIdAndPrimaryFlagAndStatus(accountAddress.getAccountId(), Boolean.TRUE, FlowStatus.ACTIVE.name());
        if(!accountAddressesHavePrimaryAndActive.isEmpty()) {
            for(M_ACCOUNT_ADDRESS cek : accountAddressesHavePrimaryAndActive) {
                if (request.getNeedValidation()!=null && request.getNeedValidation() && request.getPrimaryFlag() != null && request.getPrimaryFlag() && !cek.getId().equals(request.getAddressId())) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
                            "validation primary address", UtilsAccount.messageWarningInactivePrimary(ConstantAccount.ACCOUNT_ADDRESS)), HttpStatus.OK);
                } else if(request.getNeedValidation()!=null && request.getNeedValidation() && accountAddress.getPrimaryFlag() && accountAddress.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name()) && !request.getPrimaryFlag()){
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "You cannot inactivate this address because there must be at least one primary flag address", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
            }
        }
        return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.ACCOUNT_ADDRESS);
    }

    @SuppressWarnings("java:S3776")
    private String generateFullAddress(AccountAddressCreateDTO request){
        String fullAddress = "";
        List<String> commonStreetTemplate = new ArrayList<>();
        commonStreetTemplate.add("JLN.");
        commonStreetTemplate.add("JLN");
        commonStreetTemplate.add("JALAN.");
        commonStreetTemplate.add("JALAN");
        commonStreetTemplate.add("GG.");
        commonStreetTemplate.add("GG");
        commonStreetTemplate.add("GANG.");
        commonStreetTemplate.add("GANG");
        commonStreetTemplate.add("JL.");
        commonStreetTemplate.add("JL");

        for(String ss : commonStreetTemplate){
            String streetNameUpperCase = request.getStreetName().toUpperCase();
            if(streetNameUpperCase.contains(ss)){
                logger.info("contains : " + streetNameUpperCase);
                String replaceStreet = streetNameUpperCase.replaceAll(ss, "");
                fullAddress = "JL." + replaceStreet;
                break;
            } else {
                fullAddress = "JL. " + streetNameUpperCase;
            }
        }

        if(!ObjectUtils.isEmpty(request.getHouseNumber())){
            fullAddress += ", "+ request.getHouseNumber();
        }

        if(!ObjectUtils.isEmpty(request.getRt())){
            fullAddress += ", RT. "+request.getRt();
        }
        if(!ObjectUtils.isEmpty(request.getRw())){
            fullAddress += ", RW "+request.getRw();
        }

        if(!ObjectUtils.isEmpty(request.getAdditionalInfo())){
            fullAddress += ", "+request.getAdditionalInfo();
        }
        if(!ObjectUtils.isEmpty(request.getPostalCodeId())){
            Optional<M_LOCATION> postCode = mLocationRepo.findByLocationIdAndLocationType(request.getPostalCodeId(),"POSTAL_CODE");
            if(postCode.isPresent()){
                fullAddress += ", " + postCode.get().getLocationName();
            }
        }
        if(!ObjectUtils.isEmpty(request.getSubDistrictId())){
            Optional<M_LOCATION> subDistrict = mLocationRepo.findByLocationIdAndLocationType(request.getSubDistrictId(),"SUB_DISTRICT");
            if(subDistrict.isPresent()){
                fullAddress += ", " + subDistrict.get().getLocationName();
            }
        }
        if(!ObjectUtils.isEmpty(request.getDistrictId())){
            Optional<M_LOCATION> district = mLocationRepo.findByLocationIdAndLocationType(request.getDistrictId(),"DISTRICT");
            if(district.isPresent()){
                fullAddress += ", " + district.get().getLocationName();
            }
        }
        if(!ObjectUtils.isEmpty(request.getCityId())){
            Optional<M_LOCATION> city = mLocationRepo.findByLocationIdAndLocationType(request.getCityId(),"CITY");
            if (city.isPresent()){
                fullAddress += ", " + city.get().getLocationName();
            }
        }
        if(!ObjectUtils.isEmpty(request.getProvinceId())){
            Optional<M_LOCATION> province = mLocationRepo.findByLocationIdAndLocationType(request.getProvinceId(),"PROVINCE");
            if(province.isPresent()){
                fullAddress += ", " + province.get().getLocationName();
            }
        }
        if(!ObjectUtils.isEmpty(request.getCountryId())){
            Optional<M_LOCATION> country = mLocationRepo.findByLocationIdAndLocationType(request.getCountryId(),"COUNTRY");
            if(country.isPresent()){
                fullAddress += ", " + country.get().getLocationName();
            }
        }

        return fullAddress.toUpperCase();
    }

    @SuppressWarnings("java:S3776")
    public ResponseEntity<ResponseObject> updateAccountAddress(AccountAddressUpdateDTO request) {
        logger.info("paramRequest -> {}", request);

        ResponseObject result;
        try {

            ResponseEntity<ResponseObject> validate = this.validateUpdateAccountAddress(Boolean.FALSE, request);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            Optional<M_ACCOUNT_ADDRESS> existingAccountAddress = accountAddressRepo.findById(request.getAddressId());
            M_ACCOUNT_ADDRESS accountAddress = existingAccountAddress.get();

            List<M_ACCOUNT_ADDRESS> accountAddressesHavePrimaryAndActive = accountAddressRepo.findAllByAccountIdAndPrimaryFlagAndStatus(accountAddress.getAccountId(), Boolean.TRUE, FlowStatus.ACTIVE.name());
            if (!accountAddressesHavePrimaryAndActive.isEmpty()) {
                for (M_ACCOUNT_ADDRESS cek : accountAddressesHavePrimaryAndActive) {
                    if (request.getPrimaryFlag() != null && request.getPrimaryFlag() && !cek.getId().equals(request.getAddressId()) ) {
                        cek.setPrimaryFlag(Boolean.FALSE);
                        cek.setUpdatedBy(UserDetailUtils.getUsername());
                        cek.setUpdatedDate(new Date());
                        accountAddressRepo.save(cek);
                    }
                }
            }

            accountAddress.setUpdatedBy(UserDetailUtils.getUserId());
            accountAddress.setUpdatedDate(new Date());
            accountAddress.setPrimaryFlag(request.getPrimaryFlag());
            accountAddress.setDescription(removeSpace(request.getDescription()));
            accountAddressRepo.save(accountAddress);

//            //acr
//            M_ADDRESSES dataAddress = mAddressRepo.findByAddressId(accountAddress.getAddressId());
//            dataAddress.setDescription(removeSpace(request.getDescAddress()));
//            mAddressRepo.save(dataAddress);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success update Account Address", request);

            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }   

   @SuppressWarnings({"java:S3776","java:S1192"})
   public ResponseEntity<ResponseObject> updateStatusActiveInactive(AccountAddressInactiveDTO requestDTO) {
        ResponseObject result;
        try {
            Optional<M_ACCOUNT_ADDRESS> data = accountAddressRepo.findById(requestDTO.getId());
            if(data.isPresent()) {
                M_ACCOUNT_ADDRESS accountAddress = data.get();
                Optional<M_TAX_IDENTIFIER> cekTax = taxRepo.findTopByAccountIdAndTaxIdentifierAddressAndStatus(accountAddress.getAccountId(), accountAddress.getAddressId(), FlowStatus.ACTIVE.name());
                if(cekTax.isPresent() && accountAddress.getBusinessPurpose().equals(162)){
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                                    "You cannot inactivate address because this address has active tax identifier", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                
                if(accountAddress.getPrimaryFlag() && accountAddress.getStatus().equalsIgnoreCase(FlowStatus.INACTIVE.name())){
                    List<M_ACCOUNT_ADDRESS> accountAddressesHavePrimaryAndActive = accountAddressRepo.findAllByAccountIdAndPrimaryFlagAndStatus(accountAddress.getAccountId(), Boolean.TRUE, FlowStatus.ACTIVE.name());
                    if(!accountAddressesHavePrimaryAndActive.isEmpty()){
                        return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                                "There is existing active primary account address, please inactive those primary account address", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                    }
                }else if(accountAddress.getPrimaryFlag() && accountAddress.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())){
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "You cannot inactivate this address because there must be at least one primary flag address", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
                
                if(accountAddress.getPremiseFlag() && accountAddress.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())){
                    Optional<VW_ASSET_ASSIGNMENT> cekAssetActive = vwAssetAssignRepo.findTopByAccountAddressIdAndStatus(accountAddress.getId(), FlowStatus.ACTIVE.name());
                    if(cekAssetActive.isPresent()){
                        return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                                "You cannot inactivate this address because there is a service point with assets that are still active", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                    }
                }
                
                LinkedHashMap<String, Object> oldWt = new LinkedHashMap<>();
                oldWt.put("addressId", accountAddress.getId());
                oldWt.put("accountId", accountAddress.getAccountId());
                oldWt.put("description", accountAddress.getDescription());
                AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
                ObjectMapper mapper = new ObjectMapper();
                String oldValue = mapper.writeValueAsString(oldWt);
                auditTrail.setOldValue(oldValue);
                if(removeSpace(requestDTO.getRemarks()).equalsIgnoreCase("")) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "remark not be empty", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                auditTrail.setRemark(removeSpace(requestDTO.getRemarks()));
                auditTrail.setTableName("M_ACCOUNT_ADDRESS");
                auditTrail.setDataId(accountAddress.getId().toString());
                Optional<M_USER> user = userRepo.findByUsername(UserDetailUtils.getUsername());
                if (user.isPresent()) {
                    R_GLOBAL_TYPE_VALUE rUserLevel = globalTypeValueService.getGlobalTypeByGlbValue(Constant.USER_LEVEL, user.get().getUserLevel());
                    String userLevel = rUserLevel.getName() != null ? rUserLevel.getName() : "";
                    auditTrail.setUserLevel(userLevel);
                } else {
                    auditTrail.setUserLevel("");
                }
                auditTrail.setCreatedBy(user.get().getUsername());
                auditTrail.setCreatedDate(new Date());
                if (accountAddress.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())) {
                    accountAddress.setStatus(FlowStatus.INACTIVE.name());
                    accountAddress.setUpdatedBy(StringUtils.hasValue(UserDetailUtils.getUsername()) ? UserDetailUtils.getUsername() : null);
                    accountAddress.setUpdatedDate(new Date());
                    M_ACCOUNT_ADDRESS saveWT = accountAddressRepo.save(accountAddress);
                    LinkedHashMap<String, Object> newWt = new LinkedHashMap<>();
                    newWt.put("addressId", saveWT.getId());
                    newWt.put("accoundId", saveWT.getAccountId());
                    newWt.put("description", saveWT.getDescription());
                    String newValue = mapper.writeValueAsString(newWt);
                    auditTrail.setOperation(FlowStatus.INACTIVE.name());
                    auditTrail.setNewValue(newValue);
                    auditTrailRepo.save(auditTrail);
                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            ResponseUtils.MESSAGE_INACTIVE, saveWT);
                } else if (accountAddress.getStatus().equalsIgnoreCase(FlowStatus.INACTIVE.name())) {
                    accountAddress.setStatus(FlowStatus.ACTIVE.name());
                    accountAddress.setUpdatedBy(UserDetailUtils.getUsername() != null ? UserDetailUtils.getUsername() : null);
                    accountAddress.setUpdatedDate(new Date());
                    M_ACCOUNT_ADDRESS saveWT = accountAddressRepo.save(accountAddress);
                    LinkedHashMap<String, Object> newWt = new LinkedHashMap<>();
                    newWt.put("addressId", saveWT.getId());
                    newWt.put("accoundId", saveWT.getAccountId());
                    newWt.put("description", saveWT.getDescription());
                    String newValue = mapper.writeValueAsString(newWt);
                    auditTrail.setNewValue(newValue);
                    auditTrail.setOperation(FlowStatus.ACTIVE.name());
                    auditTrailRepo.save(auditTrail);
                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            ResponseUtils.MESSAGE_ACTIVE, saveWT);
                } else {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Account Address Status not found or null", ResponseUtils.DATA_EMPTY);
                }

                return new ResponseEntity<>(result, result.getHttpCode());

            } else {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Account Address with id " + requestDTO.getId() + " is not found.", ResponseUtils.DATA_EMPTY);

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
