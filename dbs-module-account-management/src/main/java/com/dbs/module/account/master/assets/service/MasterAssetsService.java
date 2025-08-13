package com.dbs.module.account.master.assets.service;

import com.dbs.common.library.services.CriteriaServices;
import com.dbs.module.account.master.assets.dto.AssetAttributeDto;
import com.dbs.module.account.master.assets.dto.HistoryAssignmentDto;
import com.dbs.module.account.master.assets.dto.MasterAssetCreateUpdateDto;
import com.dbs.module.account.master.assets.dto.MasterAssetsInactiveDTO;
import com.dbs.module.account.master.assets.dto.MasterAssetResponseDto;
import com.dbs.module.account.master.assets.dto.AssetStatusDto;
import com.dbs.module.account.master.assets.dto.DetailAssetResponseDto;
import com.dbs.module.account.master.assets.dto.HistoryLogInformationDto;
import com.dbs.module.account.master.assets.dto.AssetInformationDto;
import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.*;
import com.dbs.database.crm.entities.accountmanagement.*;
import com.dbs.database.crm.entities.product.M_PRODUCT;
import com.dbs.database.crm.entities.product.M_PRODUCT_VERSION;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_ACCOUNT_INFORMATION;
import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import com.dbs.database.crm.entities.usermanagement.M_USER;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.accountmanagement.Account.MAccountAddressRepo;
import com.dbs.database.crm.repositories.accountmanagement.Account.MaddressRepo;
import com.dbs.database.crm.repositories.accountmanagement.Account.MassetsRepo;
import com.dbs.database.crm.repositories.accountmanagement.Account.VwAssetAssignmentRepo;
import com.dbs.database.crm.repositories.accountmanagement.VWAccountInfoRepo;
import com.dbs.database.crm.repositories.accountmanagement.VwChooseAssetRepo;
import com.dbs.database.crm.repositories.product.MProductRepo;
import com.dbs.database.crm.repositories.product.MProductVersionRepo;
import com.dbs.database.crm.repositories.product.VWProductRepo;
import com.dbs.database.crm.repositories.usermanagement.AuditTrailRepo;
import com.dbs.database.crm.repositories.usermanagement.MUserRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import com.dbs.module.account.detail.address.service.AccountAddressService;
import com.dbs.module.account.main.dto.NameValueDdlResponseDto;
import com.dbs.module.account.utils.ConstantAccount;
import com.dbs.module.account.utils.UtilsAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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
public class MasterAssetsService {
    private static final Logger logger = LoggerFactory.getLogger(AccountAddressService.class);

    @Autowired
    private MassetsRepo mAssetsRepo;
    @Autowired
    private MUserRepo userRepo;
    @Autowired
    private AuditTrailRepo auditTrailRepo;
    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;
    @Autowired
    private Validator validator;
    @Autowired 
    private GlobalTypeValueService globalTypeService;
    @Autowired
    private VWProductRepo vwProductRepo;
    @Autowired
    private VwAssetAssignmentRepo vwAssetAssignmentRepo;
    @Autowired
    private MAccountAddressRepo mAccountAddressRepo;
    @Autowired
    private VWAccountInfoRepo vwAccountInfoRepo;
    @Autowired
    private MProductRepo mProductRepo;
    @Autowired
    private MProductVersionRepo mProductVersionRepo;
    @Autowired
    private MaddressRepo maddressRepo;
    @Autowired
    private VwChooseAssetRepo vwChooseAssetRepo;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CriteriaServices criteriaServices;

    public ResponseEntity<ResponseObject> getListAssetName() {
        logger.info("Asset Name");
        ResponseObject result;
        try {
            List<LinkedHashMap<String, Object>> allData = criteriaServices.getCriteriaByGlobalType("Asset Name");
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_OK, allData);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(CommonVariables.ERROR_IN, e.getMessage(), e);
            throw e;
        }
//        logger.info("Get List Asset Name");
//        ResponseObject result = new ResponseObject();
//
//        try {
//
//
//            List<R_GLOBAL_TYPE_VALUE> data = rGlobalTypeValueRepo.findByGlobalType(Constant.ASSET_NAME_GLB_TYPE_ID);
//            result.setSuccess(true);
//            result.setCode(HttpStatus.OK);
//            result.setMessage("Success Get List Asset Name");
//            result.setData(data);
//
//            return new ResponseEntity<>(result, HttpStatus.OK);
//
//        } catch (Exception e) {
//            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
//            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
//                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> detailAsset(Integer assetId){
        ResponseObject result;
        try {
            Optional<M_ASSETS> findMasset = mAssetsRepo.findById(assetId);
            if(findMasset.isEmpty()){
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Asset Not found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            M_ASSETS mAsset = findMasset.get();
            DetailAssetResponseDto detailAssetResponseDto = new DetailAssetResponseDto();
            detailAssetResponseDto.setAssetId(mAsset.getId());
            AssetInformationDto assetInformationDto = new AssetInformationDto();
            assetInformationDto.setSerialNumber(mAsset.getSerialNumber());
            Optional<R_GLOBAL_TYPE_VALUE> globalTypeValueAsset = rGlobalTypeValueRepo.findByGlbTypeValId(mAsset.getAssetName());
            if(globalTypeValueAsset.isPresent()){
                assetInformationDto.setAssetNameId(mAsset.getAssetName());
                assetInformationDto.setAssetName(globalTypeValueAsset.get().getName());
            }
            Optional<R_GLOBAL_TYPE_VALUE> globalTypeValueServiceType = rGlobalTypeValueRepo.findByGlbTypeValId(mAsset.getServiceType());
            if(globalTypeValueServiceType.isPresent()){
                assetInformationDto.setServiceTypeId(mAsset.getServiceType());
                assetInformationDto.setServiceType(globalTypeValueServiceType.get().getName());
            }
            Optional<R_GLOBAL_TYPE_VALUE> globalTypeValueBrand = rGlobalTypeValueRepo.findByGlbTypeValId(mAsset.getBrand());
            if(globalTypeValueBrand.isPresent()) {
                assetInformationDto.setBrandId(mAsset.getBrand());
                assetInformationDto.setBrand(globalTypeValueBrand.get().getName());
            }
            assetInformationDto.setYear(mAsset.getYear());
            assetInformationDto.setCustodyTransfer(mAsset.getCustodyTransfer());
            Optional<R_GLOBAL_TYPE_VALUE> rGlobalTypeValueAssetType = rGlobalTypeValueRepo.findByGlbTypeValId(mAsset.getType());
            if(!ObjectUtils.isEmpty(rGlobalTypeValueAssetType)){
                assetInformationDto.setAssetTypeId(mAsset.getType());
                assetInformationDto.setAssetType(rGlobalTypeValueAssetType.get().getName());
            }

            if(StringUtils.hasValue(mAsset.getProductVersion())) {
                Optional<M_PRODUCT_VERSION> getProductVersion = mProductVersionRepo.findTopByProductIdAndStatusIgnoreCase(mAsset.getProductVersion(), FlowStatus.ACTIVE.name());
                if(getProductVersion.isPresent()){
                    Optional<M_PRODUCT> getProduct = mProductRepo.findById(getProductVersion.get().getProductId());
                    if(getProduct.isPresent()){
                        NameValueDdlResponseDto nameValueDdlResponseDto = new NameValueDdlResponseDto();
                        nameValueDdlResponseDto.setName(getProduct.get().getProductName());
                        nameValueDdlResponseDto.setValue(getProductVersion.get().getProductId());
                        assetInformationDto.setProductInformation(nameValueDdlResponseDto);
                    }
                }
            }

            detailAssetResponseDto.setInformationDto(assetInformationDto);

            AssetAttributeDto assetAttributeDto = new AssetAttributeDto();
            assetAttributeDto.setDescription(mAsset.getDescription());
            assetAttributeDto.setMaxOutletPressure(mAsset.getMaximumOutletPressure());
            assetAttributeDto.setStreamAmount(mAsset.getStreamAmount());
            assetAttributeDto.setBoltHoleAmount(mAsset.getBoltHoleAmount());
            assetAttributeDto.setInletDiameter(mAsset.getInletDiameter());
            assetAttributeDto.setMinInletPressure(mAsset.getMinimumInletPressure());
            Optional<R_GLOBAL_TYPE_VALUE> globalTypeValueGSize = rGlobalTypeValueRepo.findByGlbTypeValId(mAsset.getGSize());
            if(globalTypeValueGSize.isPresent()){
                assetAttributeDto.setGSize(globalTypeValueGSize.get().getName());
                assetAttributeDto.setGSizeId(mAsset.getGSize());
            }
            assetAttributeDto.setMinCapacity(mAsset.getMinimumCapacity());
            assetAttributeDto.setOutletDiameter(mAsset.getOutletDiameter());
            assetAttributeDto.setMinimumOutletPressure(mAsset.getMinimumOutletPressure());
            assetAttributeDto.setSettingPressure(mAsset.getSettingPressure());
            assetAttributeDto.setMaxCapacity(mAsset.getMaximumCapacity());
            assetAttributeDto.setMaxInletPressure(mAsset.getMaximumInletPressure());
            assetAttributeDto.setMaxFlowCapacityPerStream(mAsset.getMaxFlowCapacityPerStream());
            assetAttributeDto.setLength(mAsset.getLength());
            assetAttributeDto.setAnsiId(mAsset.getAnsi());
            if(!ObjectUtils.isEmpty(mAsset.getAnsi())){
                Optional<R_GLOBAL_TYPE_VALUE> rGlobalAnsi = rGlobalTypeValueRepo.findByGlbTypeValId(mAsset.getAnsi());
                if (rGlobalAnsi.isPresent()) {
                    assetAttributeDto.setAnsi(rGlobalAnsi.get().getName());
                }
            }
            detailAssetResponseDto.setAttribute(assetAttributeDto);

            AssetStatusDto assetStatusDto = new AssetStatusDto();
            assetStatusDto.setStatus(mAsset.getStatus());

            Optional<VW_ASSET_ASSIGNMENT> getLocation = vwAssetAssignmentRepo.findTopByAssetIdAndStatus(assetId, FlowStatus.ACTIVE.name());
            if(getLocation.isPresent()) {
                assetStatusDto.setAssetLocation(getLocation.get().getLocation());
            } else {
                assetStatusDto.setAssetLocation(null);
            }

            detailAssetResponseDto.setAssetStatusDto(assetStatusDto);

            HistoryLogInformationDto historyLogInformationDto = new HistoryLogInformationDto();
            historyLogInformationDto.setId(mAsset.getId());
            if(!ObjectUtils.isEmpty(mAsset.getCreatedDate())){
                historyLogInformationDto.setCreatedDate(UtilsDate.dateToString(mAsset.getCreatedDate(), "dd MMM yyyy HH:mm:ss"));
            }
            historyLogInformationDto.setCreatedBy(mAsset.getCreatedBy());
            if(!ObjectUtils.isEmpty(mAsset.getUpdatedDate())){
                historyLogInformationDto.setUpdatedDate(UtilsDate.dateToString(mAsset.getUpdatedDate(),"dd MMM yyyy HH:mm:ss"));
            }
            historyLogInformationDto.setUpdatedBy(mAsset.getUpdatedBy());

            detailAssetResponseDto.setHistory(historyLogInformationDto);

            List<VW_ASSET_ASSIGNMENT> allHistory = vwAssetAssignmentRepo.findAllByAssetIdOrderByCreatedDateDesc(assetId);
            detailAssetResponseDto.setHistoryAssegment(allHistory);

            //acr
            List<AUDIT_TRAIL> auditTrail = auditTrailRepo.findAll().stream()
                    .filter(e -> e.getTableName().equalsIgnoreCase("M_ASSETS"))
                    .filter(f -> f.getDataId().equalsIgnoreCase(mAsset.getId().toString()))
                    .collect(Collectors.toList());
            detailAssetResponseDto.setActiveInactiveLog(auditTrail);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, "Detail Asset", detailAssetResponseDto);

            return new ResponseEntity<>(result, result.getHttpCode());

        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getListType() {
        logger.info("Asset Type");
        ResponseObject result;
        try {
            List<LinkedHashMap<String, Object>> allData = criteriaServices.getCriteriaByGlobalType("Asset Type");
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_OK, allData);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(CommonVariables.ERROR_IN, e.getMessage(), e);
            throw e;
        }
//        logger.info("Get List Type");
//        ResponseObject result = new ResponseObject();
//
//        try {
//
//            List<R_GLOBAL_TYPE_VALUE> data = rGlobalTypeValueRepo.findByGlobalType(34);
//            result.setSuccess(true);
//            result.setCode(HttpStatus.OK);
//            result.setMessage("Success Get List Type");
//            result.setData(data);
//
//            return new ResponseEntity<>(result, HttpStatus.OK);
//
//        } catch (Exception e) {
//            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
//            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
//                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }

    public ResponseEntity<ResponseObject> getListServiceType() {
        logger.info("Get List Service Type");
        ResponseObject result = new ResponseObject();

        try {

            List<R_GLOBAL_TYPE_VALUE> data = globalTypeService.getDetailGlobalType("Service Type");
//                    rGlobalTypeValueRepo.findByGlobalType(602);
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

    public ResponseEntity<ResponseObject> getListBrand() {
        logger.info("Assest Brand");
        ResponseObject result;
        try {
            List<LinkedHashMap<String, Object>> allData = criteriaServices.getCriteriaByGlobalType("Assest Brand");
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_OK, allData);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(CommonVariables.ERROR_IN, e.getMessage(), e);
            throw e;
        }
//        logger.info("Get List Brand");
//        ResponseObject result = new ResponseObject();
//
//        try {
//
//            List<R_GLOBAL_TYPE_VALUE> data = rGlobalTypeValueRepo.findByGlobalType(196042);
//            result.setSuccess(true);
//            result.setCode(HttpStatus.OK);
//            result.setMessage("Success Get List Brand");
//            result.setData(data);
//
//            return new ResponseEntity<>(result, HttpStatus.OK);
//
//        } catch (Exception e) {
//            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
//            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
//                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }

    public ResponseEntity<ResponseObject> getGsizes() {
        logger.info("Find G-Sizes");
        ResponseObject result;
        try {
            List<LinkedHashMap<String, Object>> allData = criteriaServices.getCriteriaByGlobalType(Constant.GSIZES);
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_OK, allData);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(CommonVariables.ERROR_IN, e.getMessage(), e);
            throw e;
        }
    }

    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> getListMasterAssets(MaterialTablePagingRequest pagingData, PagedResourcesAssembler<VW_CHOOSE_ASSET> assembler) {
        
        // Logger Info Service
        logger.info("Get List Master Asset");

        // Response Object Result
        ResponseObject result = new ResponseObject();

        try {

            Page<VW_CHOOSE_ASSET> data = null;
            Map<String, Object> filter = new HashMap<>();

            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key+"~"+value);
                }
            }

            Specification<VW_CHOOSE_ASSET> specification = pagingData.getSearch().isEmpty() ? this.vwChooseAssetRepo.getSpecificationDefault(filter) : this.vwChooseAssetRepo.getSpecificationFromFilters(pagingData, filter);
            data = this.vwChooseAssetRepo.findAll(specification, PagingUtils.getPaging(pagingData));
            List<MasterAssetResponseDto> responseDtos = new ArrayList<>();

            for (VW_CHOOSE_ASSET masterAsset : data.getContent()){
                MasterAssetResponseDto responseData = new MasterAssetResponseDto();
                responseData.setProductName(masterAsset.getProductName());
                responseData.setServiceType(masterAsset.getServiceType());
                responseData.setAssetName(masterAsset.getAssetName());
                responseData.setBrand(masterAsset.getBrand());
                responseData.setGSize(masterAsset.getGSize());
                responseData.setAnsi(masterAsset.getAnsi());
                responseData.setType(masterAsset.getType());
                responseData.setSerialNumber(masterAsset.getSerialNumber());
                responseData.setYear(masterAsset.getYear());
                responseData.setId(masterAsset.getId());
                responseData.setCustodyTransfer(masterAsset.getCustodyTransfer());
                responseData.setDescription(masterAsset.getDescription());
                responseData.setInletDiameter(masterAsset.getInletDiameter());
                responseData.setOutletDiameter(masterAsset.getOutletDiameter());
                responseData.setMaximumInletPressure(masterAsset.getMaximumInletPressure());
                responseData.setMaximumOutletPressure(masterAsset.getMaximumOutletPressure());
                responseData.setMinimumInletPressure(masterAsset.getMinimumInletPressure());
                responseData.setMinimumOutletPressure(masterAsset.getMinimumOutletPressure());
                responseData.setMaxFlowCapacityPerStream(masterAsset.getMaxFlowCapacityPerStream());
                responseData.setStreamAmount(masterAsset.getStreamAmount());
                responseData.setSettingPressure(masterAsset.getSettingPressure());
                responseData.setLength(masterAsset.getLength());
                responseData.setBoltHoleAmount(masterAsset.getBoltHoleAmount());
                responseData.setMinimumCapacity(masterAsset.getMinimumCapacity());
                responseData.setMaximumCapacity(masterAsset.getMaximumCapacity());
                responseData.setStatus(masterAsset.getStatus());
                responseData.setLocation(masterAsset.getLocation());
                responseData.setEntityId(masterAsset.getEntityId());
                responseDtos.add(responseData);
            }
            PagedModel pagedData = assembler.toModel(data);

            Map<String, Object> d = new HashMap<>();
            d.put("result", responseDtos);
            d.put("page", pagedData.getMetadata());
            d.put("links", pagedData.getLinks());

            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success Get List Master Assets");
            result.setData(d);

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<InputStreamResource> downloadFilter(MaterialTablePagingRequest pagingData) {
        try {
            Page<VW_CHOOSE_ASSET> data;
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
                long maxData = StreamSupport.stream(mAssetsRepo.findAll().spliterator(), false).count();
                pagingData.setSize((int) maxData);
                pagingData.setPage(1);
            }
            if (!pagingData.getSearch().isEmpty()) {
                data = this.vwChooseAssetRepo.findAll(this.vwChooseAssetRepo.getSpecificationFromFilters(pagingData, filter),
                        PagingUtils.getPaging(pagingData));
            } else {
                data = this.vwChooseAssetRepo.findAll(this.vwChooseAssetRepo.getSpecificationDefault(filter),
                        PagingUtils.getPaging(pagingData));
            }
            List<VW_CHOOSE_ASSET> listAction = data.getContent();
            List<LinkedHashMap<String, Object>> allData = new ArrayList<>();
            Integer no = 1;
            HttpHeaders headers = new HttpHeaders();
            if(data.hasContent()) {
                for (VW_CHOOSE_ASSET a : listAction) {
                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                    response.put("NO", no);
                    response.put("PRODUCT NAME", a.getProductName());
                    response.put("SERVICE TYPE", a.getServiceType());
                    response.put("ASSET NAME", a.getAssetName());
                    response.put("TYPE", a.getType());
                    response.put("SERIAL NUMBER", a.getSerialNumber());
                    response.put("BRAND", a.getBrand());
                    response.put("YEAR", a.getYear());
                    response.put("CUSTODY TRANSFER", a.getCustodyTransfer());
                    response.put("INLET DIAMETER", a.getInletDiameter());
                    response.put("OUTLET DIAMETER", a.getOutletDiameter());
                    response.put("MINIMUM INLET PRESSURE", a.getMinimumInletPressure());
                    response.put("MAXIMUM INLET PRESSURE", a.getMaximumInletPressure());
                    response.put("MINIMUM OUTLET PRESSURE", a.getMinimumOutletPressure());
                    response.put("MAXIMUM OUTLET PRESSURE", a.getMaximumOutletPressure());
                    response.put("MAX FLOW CAPACITY PER STREAM", a.getMaxFlowCapacityPerStream());
                    response.put("STREAM AMOUNT", a.getStreamAmount());
                    response.put("G SIZE", a.getGSize());
                    response.put("SETTING PRESSURE", a.getSettingPressure());
                    response.put("LENGTH", a.getLength());
                    response.put("BOLT HOLE AMOUNT", a.getBoltHoleAmount());
                    response.put("MINIMUM CAPACITY", a.getMinimumCapacity());
                    response.put("MAXIMUM CAPACITY", a.getMaximumCapacity());
                    response.put("CLASS/ANSI", a.getAnsi());
                    response.put("LOCATION", a.getLocation());
                    response.put("DESCRIPTION", a.getDescription());
                    response.put("STATUS", capitalizeFully(a.getStatus()));
                    allData.add(response);
                    no = no + 1;
                }
                ByteArrayInputStream in = DownloadToExcel.downloadsFiles(allData, "Excel");
                headers.add(Constant.CONTENT_DISPOSITION, "attachment; filename=" +
                        "ASSET_" + UtilsDate.dateToString(new Date(), "yyyyMMddHHmmss") + ".xlsx");
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

    public ResponseEntity<ResponseObject> createMasterAssets(MasterAssetCreateUpdateDto request) {
        ResponseObject result;
        try {

            ResponseEntity<ResponseObject> validate = this.validateCreateMasterAssets(Boolean.FALSE, request);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            M_ASSETS newMasset = objectMapper.convertValue(request, M_ASSETS.class);
            newMasset.setSerialNumber(removeSpace(request.getSerialNumber()));
            newMasset.setGSize(request.getGSize());
            newMasset.setCreatedBy(UserDetailUtils.getUsername());
            newMasset.setDescription(removeSpace(request.getDescription()));
            newMasset.setStatus(FlowStatus.ACTIVE.name());
            newMasset.setCreatedDate(new Date());
            mAssetsRepo.save(newMasset);
            
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED, UtilsAccount.messageSuccess(ConstantAccount.CREATE, ConstantAccount.MASTER_ASSET), newMasset);
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateCreateMasterAssets(Boolean api, MasterAssetCreateUpdateDto request) {
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
            // VALIDASI UNIQUE
            Optional<M_ASSETS> check = mAssetsRepo.findBySerialNumberIgnoreCaseAndBrand(removeSpace(request.getSerialNumber()), request.getBrand());
            if (check.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Serial number and brand already exist", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            // Validasi gSize null if assetName meter
            if (request.getAssetName().equals(155) && request.getGSize() == null) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "G-Size cannot be null with asset name is meter", null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.MASTER_ASSET);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("java:S3776")
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> inactiveDataAssets(MasterAssetsInactiveDTO request) {
        ResponseObject result;

        try {
            Optional<M_ASSETS> data = mAssetsRepo.findById(request.getId());
            String message = "";
            if(data.isPresent()) {
                M_ASSETS ma = data.get();
                LinkedHashMap<String, Object> oldMa = new LinkedHashMap<>();
                oldMa.put("MAssetId", ma.getId());
                oldMa.put("description", ma.getDescription());
                AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
                ObjectMapper mapper = new ObjectMapper();
                String oldValue = mapper.writeValueAsString(oldMa);
                auditTrail.setOldValue(oldValue);
                if(request.getRemark().equalsIgnoreCase("")) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "remark not be empty", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                
                //set remarks
                auditTrail.setRemark(request.getRemark());
                auditTrail.setTableName("M_ASSETS");
                auditTrail.setDataId(ma.getId().toString());
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

                if (ma.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())) {
                    ma.setStatus(FlowStatus.INACTIVE.name());
                    auditTrail.setOperation(FlowStatus.INACTIVE.name());
                    message = ResponseUtils.MESSAGE_INACTIVE;
                } else if (ma.getStatus().equalsIgnoreCase(FlowStatus.INACTIVE.name())) {
                    ma.setStatus(FlowStatus.ACTIVE.name());
                    auditTrail.setOperation(FlowStatus.ACTIVE.name());
                    message = ResponseUtils.MESSAGE_ACTIVE;
                } else {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Master Address Status not found or null", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }

                ma.setUpdatedBy(UserDetailUtils.getUsername() != null ? UserDetailUtils.getUsername() : null);
                ma.setUpdatedDate(new Date());
                M_ASSETS saveWT = mAssetsRepo.save(ma);

                LinkedHashMap<String, Object> newMa = new LinkedHashMap<>();
                newMa.put("mAssetId", saveWT.getId());
                newMa.put("description", saveWT.getDescription());

                String newValue = mapper.writeValueAsString(newMa);
                auditTrail.setNewValue(newValue);
                auditTrailRepo.save(auditTrail);
                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        message, saveWT);

                return new ResponseEntity<>(result, result.getHttpCode());

            } else {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Master Address with id " + request.getId() + " is not found.", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> updateAssets(MasterAssetCreateUpdateDto request) {
        try {

            ResponseEntity<ResponseObject> validate = this.validateUpdateAssets(Boolean.FALSE, request);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            Optional<M_ASSETS> mAssetData = mAssetsRepo.findById(request.getId());
            M_ASSETS newMasset = new M_ASSETS();
            if(mAssetData.isPresent()) {
                newMasset = mAssetData.get();
                // GANTI SESUAI KOLOM YG BISA DI UPDATE
                newMasset.setInletDiameter(request.getInletDiameter());
                newMasset.setOutletDiameter(request.getOutletDiameter());
                newMasset.setMinimumInletPressure(request.getMinimumInletPressure());
                newMasset.setMinimumOutletPressure(request.getMinimumOutletPressure());
                newMasset.setMaximumInletPressure(request.getMaximumInletPressure());
                newMasset.setMaximumOutletPressure(request.getMaximumOutletPressure());
                newMasset.setMaxFlowCapacityPerStream(request.getMaxFlowCapacityPerStream());
                newMasset.setStreamAmount(request.getStreamAmount());
                newMasset.setGSize(request.getGSize());
                newMasset.setSettingPressure(request.getSettingPressure());
                newMasset.setLength(request.getLength());
                newMasset.setBoltHoleAmount(request.getBoltHoleAmount());
                newMasset.setMinimumCapacity(request.getMinimumCapacity());
                newMasset.setMaximumCapacity(request.getMaximumCapacity());
                newMasset.setAnsi(request.getAnsi());
                newMasset.setDescription(removeSpace(request.getDescription()));
                newMasset.setUpdatedBy(UserDetailUtils.getUsername());
                newMasset.setUpdatedDate(new Date());
                mAssetsRepo.save(newMasset);
            }

            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.UPDATE, ConstantAccount.MASTER_ASSET), newMasset), HttpStatus.OK);
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateUpdateAssets(Boolean api, MasterAssetCreateUpdateDto masterAssetData){
        ResponseObject result;
        try {
            var violations = validator.validate(masterAssetData);
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

            Optional<M_ASSETS> mAssetData = mAssetsRepo.findById(masterAssetData.getId());
            if(mAssetData.isEmpty()){
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        UtilsAccount.messageDataNotFound(ConstantAccount.MASTER_ASSET, masterAssetData.getId()), ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
            }

            return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.MASTER_ASSET);

        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> assetHistoryAssignment(Integer assetId){
        ResponseObject result;
        try {
            List<VW_ASSET_ASSIGNMENT> findVwAssetAssignment = vwAssetAssignmentRepo.findAllByAssetId(assetId);


            List<HistoryAssignmentDto> historyAssignmentDtos = new ArrayList<>();
            for(VW_ASSET_ASSIGNMENT vwAssetAssignment : findVwAssetAssignment){
                Optional<M_ACCOUNT_ADDRESS> findAccountAddress = mAccountAddressRepo.findById(vwAssetAssignment.getId());

                Optional<VW_ACCOUNT_INFORMATION> findAccountInfo = vwAccountInfoRepo.findByAccountId(findAccountAddress.get().getAccountId());

                VW_ACCOUNT_INFORMATION accountInformation = findAccountInfo.get();

                HistoryAssignmentDto historyAssignmentDto = new HistoryAssignmentDto();
                historyAssignmentDto.setId(vwAssetAssignment.getId());
                historyAssignmentDto.setCustomerNumber(accountInformation.getCustomerNumber());
                historyAssignmentDto.setCustomerName(accountInformation.getCustomerName());
                historyAssignmentDto.setAccountNumber(accountInformation.getAccountNumber());
                historyAssignmentDto.setAccountName(accountInformation.getAccountName());
                Optional<R_GLOBAL_TYPE_VALUE> servicePoint = rGlobalTypeValueRepo.findByGlbTypeValId(vwAssetAssignment.getServicePointId());
                if (servicePoint.isPresent()) {
                    historyAssignmentDto.setServicePoint(servicePoint.get().getName());
                }
                historyAssignmentDto.setRemark(vwAssetAssignment.getRemark());
                if (!ObjectUtils.isEmpty(vwAssetAssignment.getInstallDate())) {
                    historyAssignmentDto.setInstallDate(UtilsDate.dateToString(vwAssetAssignment.getInstallDate(), "dd MMM yyyy"));
                }

                if (!ObjectUtils.isEmpty(vwAssetAssignment.getUnInstallDate())) {
                    historyAssignmentDto.setUninstallDate(UtilsDate.dateToString(vwAssetAssignment.getUnInstallDate(), "dd MMM yyyy"));
                }
                historyAssignmentDtos.add(historyAssignmentDto);
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, "History Assignment List", historyAssignmentDtos);
            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
