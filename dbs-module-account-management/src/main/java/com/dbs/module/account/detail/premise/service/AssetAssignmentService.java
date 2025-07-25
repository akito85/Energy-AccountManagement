package com.dbs.module.account.detail.premise.service;

import com.dbs.common.library.services.CriteriaServices;
import com.dbs.common.library.utils.*;
import com.dbs.module.account.detail.premise.dto.GlobalValueAssetDTO;
import com.dbs.module.account.detail.premise.dto.InactiveAssetAssignmentDTO;
import com.dbs.module.account.detail.premise.dto.MessageAssignDTO;
import com.dbs.module.account.detail.premise.dto.DetailServicePointDTO;
import com.dbs.module.account.detail.premise.dto.AssignAssetDTO;
import com.dbs.module.account.detail.premise.dto.ChooseAssetDTO;
import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.database.crm.entities.accountmanagement.*;
import com.dbs.database.crm.entities.product.VW_PRODUCT;
import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import com.dbs.database.crm.entities.usermanagement.M_USER;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.accountmanagement.Account.*;
import com.dbs.database.crm.repositories.accountmanagement.VwChooseAssetRepo;
import com.dbs.database.crm.repositories.product.VWProductRepo;
import com.dbs.database.crm.repositories.usermanagement.AuditTrailRepo;
import com.dbs.database.crm.repositories.usermanagement.MUserRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import com.dbs.module.account.detail.address.service.AccountAddressService;
import com.dbs.module.account.detail.serviceagreement.service.SaDdlService;
import com.dbs.module.account.utils.ConstantAccount;
import com.dbs.module.account.utils.UtilsAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.dbs.common.base.utils.CommonHelper.removeSpace;

@Service
public class AssetAssignmentService {

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
    private MServicePointRepo mServicePointRepo;
    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;
    @Autowired
    private MAssetAssignmentRepo mAssetAssignmentRepo;
    @Autowired
    private MassetsRepo massetsRepo;
    @Autowired
    private VWProductRepo vwProductRepo;
    @Autowired
    private GlobalTypeValueService globalTypeService;
    @Autowired
    private VwAssetAssignmentRepo vwAssetAssignRepo;

    @Autowired
    private VwChooseAssetRepo vwChooseAssetRepo;

    @Autowired
    private SaDdlService saDdlService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CriteriaServices criteriaServices;


    @SuppressWarnings("java:S3776")
    public ResponseEntity<ResponseObject> chooseAsset(MaterialTablePagingRequest pagingRequest,
                                                      PagedResourcesAssembler<VW_CHOOSE_ASSET> assembler) {
        logger.info("Get List Assets from Master asset");

        ResponseObject result = new ResponseObject();

        try {
            Map<String, Object> filter = new HashMap<>();
            filter.put("ready", "Yes");
            logger.info(filter.toString());

            if (StringUtils.hasValue(pagingRequest.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingRequest.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingRequest.getSearch().add(key+"~"+value);
                }
            }

            Specification<VW_CHOOSE_ASSET> specification = pagingRequest.getSearch().isEmpty()
                    ? vwChooseAssetRepo.getSpecificationDefault(filter)
                    : vwChooseAssetRepo.getSpecificationFromFilters(pagingRequest, filter);

            Page<VW_CHOOSE_ASSET> data = vwChooseAssetRepo.findAll(specification, PagingUtils.getPaging(pagingRequest));

            List<ChooseAssetDTO> newDTO = new ArrayList<>();

            for (VW_CHOOSE_ASSET asset : data.getContent()) {
                ChooseAssetDTO assetDTO = new ChooseAssetDTO();
                // Asset Id
                assetDTO.setId(asset.getId());
                // Service Type
                GlobalValueAssetDTO dto = new GlobalValueAssetDTO();
                dto.setId(asset.getServiceTypeId());
                dto.setName(asset.getServiceType());
                dto.setValue(asset.getServiceTypeValue());
                assetDTO.setServiceType(dto);

                // Asset Name
                GlobalValueAssetDTO dtoName = new GlobalValueAssetDTO();
                dtoName.setId(asset.getAssetNameId());
                dtoName.setName(asset.getAssetName());
                dtoName.setValue(asset.getAssetNameValue());
                assetDTO.setAssetName(dtoName);

                // Serial Number
                if (asset.getSerialNumber() != null) {
                    assetDTO.setSerialNumber(asset.getSerialNumber());
                }

                // Type
                GlobalValueAssetDTO dtoType = new GlobalValueAssetDTO();
                dtoType.setId(asset.getTypeId());
                dtoType.setName(asset.getType());
                dtoType.setValue(asset.getTypeValue());
                assetDTO.setType(dtoType);

                // Brand
                GlobalValueAssetDTO dtoBrand = new GlobalValueAssetDTO();
                dtoBrand.setId(asset.getBrandId());
                dtoBrand.setName(asset.getBrand());
                dtoBrand.setValue(asset.getBrandValue());
                assetDTO.setBrand(dtoBrand);

                // Year
                if (asset.getYear() != null) {
                    assetDTO.setYear(asset.getYear());
                }
                // Custody Transfer
                if (asset.getCustodyTransfer() != null) {
                    assetDTO.setCustodyTransfer(asset.getCustodyTransfer());
                }
                // Description
                if (asset.getDescription() != null)
                    assetDTO.setDescription(asset.getDescription());
                // Inlet Diameter
                if (asset.getInletDiameter() != null)
                    assetDTO.setInletDiameter(asset.getInletDiameter());
                // Outlet Diameter
                if (asset.getOutletDiameter() != null)
                    assetDTO.setOutletDiameter(asset.getOutletDiameter());
                // Max Inlet Pressure
                if (asset.getMaximumInletPressure() != null)
                    assetDTO.setMaximumInletPressure(asset.getMaximumInletPressure());
                // Max Outlet Pressure
                if (asset.getMaximumOutletPressure() != null)
                    assetDTO.setMaximumOutletPressure(asset.getMaximumOutletPressure());
                // Min Inlet Pressure
                if (asset.getMinimumInletPressure() != null)
                    assetDTO.setMinimumInletPressure(asset.getMinimumInletPressure());
                // Min Outlet Pressure
                if (asset.getMinimumOutletPressure() != null)
                    assetDTO.setMinimumOutletPressure(asset.getMinimumOutletPressure());
                // Max Flow Capacity Per Stream
                if (asset.getMaxFlowCapacityPerStream() != null)
                    assetDTO.setMaxFlowCapacityPerStream(asset.getMaxFlowCapacityPerStream());
                // Stream amount
                if (asset.getStreamAmount() != null)
                    assetDTO.setStreamAmount(asset.getStreamAmount());
                // GSize\
                GlobalValueAssetDTO dtoGSize = new GlobalValueAssetDTO();
                dtoGSize.setId(asset.getGSizeId());
                dtoGSize.setName(asset.getGSize());
                dtoGSize.setValue(asset.getGSizeValue());
                assetDTO.setGSize(dtoGSize);
                // Setting Pressure
                if (asset.getSettingPressure() != null)
                    assetDTO.setSettingPressure(asset.getSettingPressure());
                // Length
                if (asset.getLength() != null)
                    assetDTO.setLength(asset.getLength());
                // BoltHole Amount
                if (asset.getBoltHoleAmount() != null)
                    assetDTO.setBoltHoleAmount(asset.getBoltHoleAmount());
                // Min Capacity
                if (asset.getMinimumCapacity() != null)
                    assetDTO.setMinimumCapacity(asset.getMinimumCapacity());
                // Max Capacity
                if (asset.getMaximumCapacity() != null)
                    assetDTO.setMaximumCapacity(asset.getMaximumCapacity());
                // ANSI
                GlobalValueAssetDTO dtoAnsi = new GlobalValueAssetDTO();
                dtoAnsi.setId(asset.getAnsiId());
                dtoAnsi.setName(asset.getAnsi());
                dtoAnsi.setValue(asset.getAnsiValue());
                assetDTO.setAnsi(dtoAnsi);
                // Entity Id
                if (asset.getEntityId() != null) {
                    assetDTO.setEntityId(asset.getEntityId());
                }

                GlobalValueAssetDTO dtoProduct = new GlobalValueAssetDTO();
                dtoProduct.setId(asset.getProductNameId());
                dtoProduct.setName(asset.getProductName());
                dtoProduct.setValue(asset.getProductNameValue());
                assetDTO.setProductName(dtoProduct);

                assetDTO.setStatus(asset.getStatus());

                newDTO.add(assetDTO);
            }

            PagedModel<EntityModel<VW_CHOOSE_ASSET>> pagedData = assembler.toModel(data);
            Map<String, Object> d = new HashMap<>();
            d.put("result", newDTO);
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

    public ResponseEntity<ResponseObject> getDataServicePoint(Integer servicePointId) {
        logger.info("Get List Asset Assignment By Service Point Id");

        ResponseObject result = new ResponseObject();

        try {
            Optional<M_SERVICE_POINT> sp = this.mServicePointRepo.findById(servicePointId);
            if (sp.isPresent()) {
                DetailServicePointDTO spDetail = new DetailServicePointDTO();

                spDetail.setServicePointId(servicePointId);
                Optional<M_ACCOUNT_ADDRESS> getAccountAddress = accountAddressRepo.findById(sp.get().getAccountAddressId());
                spDetail.setPremiseAddressId(sp.get().getAccountAddressId());
                if (getAccountAddress.isPresent()) {
                    Optional<M_ADDRESSES> getAddress = maddressRepo.findById(getAccountAddress.get().getAddressId());
                    getAddress.ifPresent(mAddresses -> spDetail.setPremiseAddress(mAddresses.getFullAddress()));
                }
                spDetail.setServicePointNameId(sp.get().getServicePointName());
                Optional<R_GLOBAL_TYPE_VALUE> getServicePointName = rGlobalTypeValueRepo.findById(sp.get().getServicePointName());
                getServicePointName.ifPresent(rGlobalTypeValue -> spDetail.setServicePointName(rGlobalTypeValue.getName()));
                spDetail.setDescription(sp.get().getDescription());
                spDetail.setRecordId(sp.get().getId());
                spDetail.setCreatedBy(sp.get().getCreatedBy());
                spDetail.setCreatedDate(sp.get().getCreatedDate());
                spDetail.setUpdatedBy(sp.get().getUpdatedBy());
                spDetail.setUpdateDate(sp.get().getUpdatedDate());

                result.setSuccess(true);
                result.setCode(HttpStatus.OK);
                result.setMessage("Success View Detail Service Point");
                result.setData(spDetail);

                return new ResponseEntity<>(result, HttpStatus.OK);
            }

        } catch (Exception e) {

            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> viewAssetAssignment(MaterialTablePagingRequest pagingData,
                                                              PagedResourcesAssembler<VW_ASSET_ASSIGNMENT> assembler, Integer servicePointId) {
        logger.info("Get List View Asset Assignment");
        try {
            Map<String, Object> filter = new HashMap<>();
            filter.put("servicePointId", servicePointId);

            Page<VW_ASSET_ASSIGNMENT> data;
            if (StringUtils.hasValue(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key+"~"+value);
                }
            }
            if (!pagingData.getSearch().isEmpty()) {
                pagingData.setPage(0);
                data = vwAssetAssignRepo.findAll(vwAssetAssignRepo.getSpecificationFromFilters(pagingData, filter), PagingUtils.getPaging(pagingData));
            } else {
                data = vwAssetAssignRepo.findAll(vwAssetAssignRepo.getSpecificationDefault(filter), PagingUtils.getPaging(pagingData));
            }
            List<LinkedHashMap<String, Object>> allData = data.getContent().stream()
                    .map(g -> {
                        LinkedHashMap<String, Object> tr = new LinkedHashMap<>();
                        tr.put("id", g.getId());
                        tr.put("installDate", g.getInstallDate());
                        tr.put("unInstallDate", g.getUnInstallDate());
                        tr.put("remark", g.getRemark());
                        tr.put("status", g.getStatus());
                        tr.put("assetId", g.getAssetId());
                        tr.put("assetName", g.getAssetName());
                        tr.put("serialNumber", g.getSerialNumber());
                        tr.put("type", g.getType());
                        tr.put("productName", g.getProductName());
                        tr.put("brand", g.getBrand());
                        tr.put("year", g.getYear());
                        tr.put("custodyTransfer", g.getCustodyTransfer());
                        tr.put("description", g.getDescription());
                        tr.put("inletDiameter", g.getInletDiameter());
                        tr.put("outletDiameter", g.getOutletDiameter());
                        tr.put("maximumInletPressure", g.getMaximumInletPressure());
                        tr.put("maximumOutletPressure", g.getMaximumOutletPressure());
                        tr.put("minimumInletPressure", g.getMinimumInletPressure());
                        tr.put("minimumOutletPressure", g.getMinimumOutletPressure());
                        tr.put("maxFlowCapacityPerStream", g.getMaxFlowCapacityPerStream());
                        tr.put("streamAmount", g.getStreamAmount());
                        tr.put("settingPressure", g.getSettingPressure());
                        tr.put("length", g.getLength());
                        tr.put("boltHoleAmount", g.getBoltHoleAmount());
                        tr.put("minimumCapacity", g.getMinimumCapacity());
                        tr.put("maximumCapacity", g.getMaximumCapacity());
                        tr.put("ansi", g.getAnsi());
                        tr.put("source", g.getSource());
                        tr.put("gsize", g.getGsize());

                        tr.put("createdBy", g.getCreatedBy());
                        tr.put("createdDate", g.getCreatedDate());
                        tr.put("updatedBy", g.getUpdatedBy());
                        tr.put("updatedDate", g.getUpdatedDate());


                        return tr;

                    })
                    .collect(Collectors.toList());

            PagedModel<EntityModel<VW_ASSET_ASSIGNMENT>> pagedData = assembler.toModel(data);
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

    //    @Transactional(rollbackFor = Exception.class, readOnly = false)
    @SuppressWarnings("java:S3776")
    public ResponseEntity<ResponseObject> assignAsset(AssignAssetDTO request) {

        ResponseObject result;
        Date newDate = new Date();

        try {

            ResponseEntity<ResponseObject> validate = this.validateAssignAsset(request);
            if(!validate.getBody().getSuccess()) {
                return validate;
            }

            if (request.getAssetId() == null) {
                M_ASSETS asset = getmAssets(request, newDate);
                asset.setStatus(FlowStatus.ASSIGNED.name());
                asset.setEntityId(UserDetailUtils.getUserEntity());
                M_ASSETS newAsset = massetsRepo.save(asset);

                Optional<M_ASSETS> assetName = massetsRepo.findById(newAsset.getId());
                List<M_ASSETS_ASSIGNMENT_HISTORY> inactive = mAssetAssignmentRepo.findAllByServicePointIdAndUninstallDateIsNull(request.getServicePointId());
                for (M_ASSETS_ASSIGNMENT_HISTORY cek : inactive) {
                    Optional<M_ASSETS> cekAssetName = massetsRepo.findById(cek.getAssetId());
                    if (assetName.get().getAssetName().equals(cekAssetName.get().getAssetName())) {
                        M_ASSETS_ASSIGNMENT_HISTORY inacAsset = cek;
                        inacAsset.setStatus(FlowStatus.INACTIVE.name());
                        inacAsset.setUninstallDate(UtilsDate.getDateMinusDay(request.getInstallDate(), 1));
                        mAssetAssignmentRepo.save(inacAsset);
                        M_ASSETS standbyAsset = cekAssetName.get();
                        standbyAsset.setStatus(FlowStatus.STANDBY.name());
                        standbyAsset.setUpdatedDate(newDate);
                        standbyAsset.setUpdatedBy(UserDetailUtils.getUsername());
                        massetsRepo.save(standbyAsset);
                    }
                }
                M_ASSETS_ASSIGNMENT_HISTORY assignAsset = getmAssetsAssignmentHistory(newDate, newAsset.getId(), request);
                M_ASSETS_ASSIGNMENT_HISTORY assignAssetData = mAssetAssignmentRepo.save(assignAsset);
                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                        "Success Assign Asset ", assignAssetData);
            } else {
                Optional<M_ASSETS> setAssigned = massetsRepo.findById(request.getAssetId());
                if (setAssigned.isPresent()) {
                    M_ASSETS updateStatus = setAssigned.get();
                    updateStatus.setStatus(FlowStatus.ASSIGNED.name());
                    massetsRepo.save(updateStatus);
                }
                Optional<M_ASSETS> assetName = massetsRepo.findById(request.getAssetId());
                List<M_ASSETS_ASSIGNMENT_HISTORY> inactive = mAssetAssignmentRepo.findAllByServicePointIdAndUninstallDateIsNull(request.getServicePointId());
                for (M_ASSETS_ASSIGNMENT_HISTORY cek : inactive) {
                    Optional<M_ASSETS> cekAssetName = massetsRepo.findById(cek.getAssetId());
                    if (assetName.get().getAssetName().equals(cekAssetName.get().getAssetName())) {
                        M_ASSETS_ASSIGNMENT_HISTORY inacAsset = cek;
                        inacAsset.setStatus(FlowStatus.INACTIVE.name());
                        inacAsset.setUninstallDate(UtilsDate.getDateMinusDay(request.getInstallDate(), 1));
                        mAssetAssignmentRepo.save(inacAsset);
                        M_ASSETS standbyAsset = cekAssetName.get();
                        standbyAsset.setStatus(FlowStatus.STANDBY.name());
                        standbyAsset.setUpdatedDate(newDate);
                        standbyAsset.setUpdatedBy(UserDetailUtils.getUsername());
                        massetsRepo.save(standbyAsset);
                    }
                }
                M_ASSETS_ASSIGNMENT_HISTORY assignAsset = getmAssetsAssignmentHistory(newDate, request.getAssetId(), request);
                M_ASSETS_ASSIGNMENT_HISTORY assignAssetData = mAssetAssignmentRepo.save(assignAsset);
                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                        "Success Assign Asset ", assignAssetData);
            }
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateAssignAsset(AssignAssetDTO request) {
        logger.info("gSize -> {}", request.getGsize());
        ResponseObject result;
        if (request.getAssetId() == null) {
            // VALIDASI UNIQUE
            Optional<M_ASSETS> check = massetsRepo.findBySerialNumberIgnoreCaseAndBrand(removeSpace(request.getSerialNumber()), request.getBrand());
            if (check.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Serial number and brand already exist", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
            if (request.getAssetName().equals(155) && request.getGsize() == null) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "G-Size cannot be null with asset name is meter", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
        }

        // VALIDATE ASSET NAME
        if (request.getIsDuplicate()) {
            if (request.getAssetId() != null) {
                Optional<M_ASSETS> assetName = massetsRepo.findById(request.getAssetId());
                if (assetName.isPresent()) {
                    List<M_ASSETS> allAss = massetsRepo.findAllByAssetName(assetName.get().getAssetName());
                    for (M_ASSETS aa : allAss) {
                        Optional<M_ASSETS_ASSIGNMENT_HISTORY> cek = mAssetAssignmentRepo.findByServicePointIdAndAssetIdAndUninstallDateIsNull(request.getServicePointId(), aa.getId());
                        if (cek.isPresent()) {
                            M_ASSETS_ASSIGNMENT_HISTORY cekOverlapping = cek.get();
                            if(request.getInstallDate().equals(cekOverlapping.getInstallDate()) || request.getInstallDate().before(cekOverlapping.getInstallDate())) {
                                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "Start date must be greater than last asset assignment with same asset name start date!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                            }
                            MessageAssignDTO dto = new MessageAssignDTO();
                            dto.setMessage("Data message true");
                            result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
                                    "Confirmation", dto);

                            return new ResponseEntity<>(result, result.getHttpCode());
                        }
                    }
                }
            } else {
                List<M_ASSETS> allAss = massetsRepo.findAllByAssetName(request.getAssetName());
                for (M_ASSETS aa : allAss) {
                    Optional<M_ASSETS_ASSIGNMENT_HISTORY> cek = mAssetAssignmentRepo.findByServicePointIdAndAssetIdAndUninstallDateIsNull(request.getServicePointId(), aa.getId());
                    if (cek.isPresent()) {
                        M_ASSETS_ASSIGNMENT_HISTORY cekOverlapping = cek.get();
                        if(request.getInstallDate().equals(cekOverlapping.getInstallDate()) || request.getInstallDate().before(cekOverlapping.getInstallDate())) {
                            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "Start date must be greater than last asset assignment with same asset name start date!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                        }
                        MessageAssignDTO dto = new MessageAssignDTO();
                        dto.setMessage("Data message true");
                        result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
                                "Confirmation", dto);

                        return new ResponseEntity<>(result, result.getHttpCode());
                    }
                }
            }
        }

        return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                UtilsAccount.messageSuccess(ConstantAccount.VALIDATE, "Asset Assigment"), ResponseUtils.DATA_EMPTY), HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> serialNumberBrand(AssignAssetDTO request) {
        logger.info("paramRequest -> {}", request);
        ResponseObject result;
        try {
            Optional<M_ASSETS> check = massetsRepo.findBySerialNumberIgnoreCaseAndBrand(request.getSerialNumber(), request.getBrand());
            if (check.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Serial number and brand already exist", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success check validate serial number and brand", ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static M_ASSETS_ASSIGNMENT_HISTORY getmAssetsAssignmentHistory(Date newDate, Integer request, AssignAssetDTO request1) {
        M_ASSETS_ASSIGNMENT_HISTORY assignAsset = new M_ASSETS_ASSIGNMENT_HISTORY();

        assignAsset.setCreatedBy(UserDetailUtils.getUsername());
        assignAsset.setStatus(FlowStatus.ACTIVE.name());
        assignAsset.setCreatedDate(newDate);
        assignAsset.setAssetId(request);
        assignAsset.setServicePointId(request1.getServicePointId());
        assignAsset.setInstallDate(request1.getInstallDate());
        assignAsset.setRemark(removeSpace(request1.getRemark()));
        return assignAsset;
    }

    private static M_ASSETS getmAssets(AssignAssetDTO request, Date newDate) {
        M_ASSETS asset = new M_ASSETS();

        asset.setCreatedBy(UserDetailUtils.getUsername());
        asset.setStatus(FlowStatus.ACTIVE.name());
        asset.setCreatedDate(newDate);

        asset.setServiceType(request.getServiceType());
        asset.setAssetName(request.getAssetName());
        asset.setType(request.getType());
        asset.setSerialNumber(request.getSerialNumber());
        asset.setYear(request.getYear());
        asset.setBrand(request.getBrand());
        asset.setCustodyTransfer(request.getCustodyTransfer());
        asset.setSource(request.getSource());
        asset.setDescription(removeSpace(request.getDescription()));
        asset.setInletDiameter(request.getInletDiameter());
        asset.setOutletDiameter(request.getOutletDiameter());
        asset.setMaximumInletPressure(request.getMaximumInletPressure());
        asset.setMaximumOutletPressure(request.getMaximumOutletPressure());
        asset.setMinimumInletPressure(request.getMinimumInletPressure());
        asset.setMinimumOutletPressure(request.getMinimumOutletPressure());
        asset.setMaxFlowCapacityPerStream(request.getMaxFlowCapacityPerStream());
        asset.setStreamAmount(request.getStreamAmount());
        asset.setGSize(request.getGsize());
        asset.setSettingPressure(request.getSettingPressure());
        asset.setLength(request.getLength());
        asset.setBoltHoleAmount(request.getBoltHoleAmount());
        asset.setMinimumCapacity(request.getMinimumCapacity());
        asset.setMaximumCapacity(request.getMaximumCapacity());
        asset.setAnsi(request.getAnsi());
        asset.setEntityId(request.getEntityId());
        asset.setProductVersion(request.getProductVersion());
        return asset;
    }

    @SuppressWarnings("java:S3776")
    public ResponseEntity<ResponseObject> inactiveAssetAssignment(InactiveAssetAssignmentDTO request) {
        logger.info("paramRequest -> {}", request);

        ResponseObject result;

        try {
            Optional<M_ASSETS_ASSIGNMENT_HISTORY> data = mAssetAssignmentRepo.findById(request.getAssetAssignmentId());
            if (data.isPresent()) {
                M_ASSETS_ASSIGNMENT_HISTORY wt = data.get();
                if(wt.getUninstallDate()!=null) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND, "Asset assignment will be inactive on " + UtilsDate.dateToString(wt.getUninstallDate(), Constant.FORMAT_START_END_DATE), ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
                }
                LinkedHashMap<String, Object> oldWt = new LinkedHashMap<>();
                oldWt.put("assetAssignmentId", wt.getId());
                oldWt.put("servicePointId", wt.getServicePointId());
                AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
                ObjectMapper mapper = new ObjectMapper();
                String oldValue = mapper.writeValueAsString(oldWt);
                auditTrail.setOldValue(oldValue);
                auditTrail.setRemark(removeSpace(request.getRemarks()));
                auditTrail.setTableName("M_ASSETS_ASSIGNMENT_HISTORY");
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
                    if (request.getUninstallDate().getTime() < wt.getInstallDate().getTime()) {
                        result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                                "Uninstall Date must be greater than Install Date!",
                                ResponseUtils.DATA_EMPTY);
                        return new ResponseEntity<>(result, result.getHttpCode());
                    }
                    wt.setUninstallDate(request.getUninstallDate());
                    wt.setUpdatedDate(new Date());
                    M_ASSETS_ASSIGNMENT_HISTORY saveWT = mAssetAssignmentRepo.save(wt);
                    Optional<M_ASSETS> standbyAsset = massetsRepo.findById(saveWT.getAssetId());
                    if (standbyAsset.isPresent()) {
                        M_ASSETS aa = standbyAsset.get();
                        aa.setStatus("STANDBY");
                        aa.setUpdatedBy(UserDetailUtils.getUsername());
                        aa.setUpdatedDate(new Date());
                        massetsRepo.save(aa);
                    }
                    LinkedHashMap<String, Object> newWt = new LinkedHashMap<>();
                    newWt.put("assetAssignmentId", saveWT.getId());
                    newWt.put("servicePointId", saveWT.getServicePointId());
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

            } else {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Service Point with id " + request.getAssetAssignmentId() + " is not found.", ResponseUtils.DATA_EMPTY);

            }
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> viewListProductAsset() {
        logger.info("Get List Product");
        ResponseObject result = new ResponseObject();
        try {
            List<VW_PRODUCT> data = this.vwProductRepo.findAllByServiceTypeAndStatus(2219, FlowStatus.ACTIVE.name());
            // filtered date now
            List<VW_PRODUCT> dataFiltered = new ArrayList<>();
            for(VW_PRODUCT cc : data) {
                if(cc.getStartDate() != null && UtilsAccount.isDateInRange(cc.getStartDate(), cc.getEndDate())) {
                    dataFiltered.add(cc);
                }
            }
            logger.info("data : {}", dataFiltered);
            result.setData(dataFiltered);
            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Get List Product Success");

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getListAnsi() {
        logger.info("Assets ANSI");
        ResponseObject result;
        try {
            List<LinkedHashMap<String, Object>> allData = criteriaServices.getCriteriaByGlobalType("Assets ANSI");
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_OK, allData);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(CommonVariables.ERROR_IN, e.getMessage(), e);
            throw e;
        }
//        logger.info("Get List Product");
//        ResponseObject result = new ResponseObject();
//        try {
//            List<R_GLOBAL_TYPE_VALUE> datas = this.rGlobalTypeValueRepo.findByGlobalType(196041);
//            logger.info("data : {}", datas);
//            List<GlobalValueAssetDTO> newData = new ArrayList<>();
//            for (R_GLOBAL_TYPE_VALUE data : datas) {
//                int index = 0;
//                GlobalValueAssetDTO dto = new GlobalValueAssetDTO();
//                dto.setId(data.getGlbTypeValId());
//                dto.setName(data.getName());
//                dto.setValue(data.getGlbValue());
//                newData.add(index, dto);
//            }
//            result.setData(newData);
//            result.setSuccess(true);
//            result.setCode(HttpStatus.OK);
//            result.setMessage("Get List Ansi/Class");
//
//            return new ResponseEntity<>(result, HttpStatus.OK);
//
//        } catch (Exception e) {
//            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
//            return new ResponseEntity<>(
//                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
//                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
//                    HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }
}