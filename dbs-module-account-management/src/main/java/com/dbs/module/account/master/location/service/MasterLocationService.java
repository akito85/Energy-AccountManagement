package com.dbs.module.account.master.location.service;

import com.dbs.module.account.master.location.dto.UpdateMasterLocationDTO;
import com.dbs.module.account.master.location.dto.PagingLocationDTO;
import com.dbs.module.account.master.location.dto.CreateMasterLocationDTO;
import com.dbs.module.account.master.location.dto.MasterLocationResponseDto;
import com.dbs.module.account.master.location.dto.DetailLocationDtoResponse;
import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.*;
import com.dbs.database.crm.entities.accountmanagement.MV_LOCATION;
import com.dbs.database.crm.entities.product.M_LOCATION;
import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import com.dbs.database.crm.entities.usermanagement.M_USER;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.entities.usermanagement.view.VW_LOCATION;
import com.dbs.database.crm.repositories.mastermanagement.MvLocationRepo;
import com.dbs.database.crm.repositories.usermanagement.AuditTrailRepo;
import com.dbs.database.crm.repositories.usermanagement.MUserRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import com.dbs.database.crm.repositories.usermanagement.view.VWLocationRepo;
import com.dbs.module.account.master.location.dto.ActiveInactiveDto;
import com.dbs.module.account.main.dto.NameValueDdlResponseDto;
import com.dbs.module.account.utils.ConstantAccount;
import com.dbs.module.account.utils.UtilsAccount;
//import com.dbs.module.master.tos.services.TosServiceImpl;
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
import org.springframework.util.ObjectUtils;

import javax.validation.Validator;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.dbs.common.library.utils.StringUtils.capitalizeFully;
import com.dbs.common.library.utils.dto.DropdownDTO;
import com.dbs.database.crm.repositories.mastermanagement.MLocationsRepo;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class MasterLocationService {

    private static final Logger logger = LoggerFactory.getLogger(MasterLocationService.class);

    @Autowired
    private MLocationsRepo mlocationRepo;
    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;
    @Autowired
    private MUserRepo userRepo;
    @Autowired
    private AuditTrailRepo auditTrailRepo;
    @Autowired
    private MvLocationRepo mvLocationRepo;
    @Autowired
    private VWLocationRepo vwLocationRepo;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GlobalTypeValueService globalTypeService;

    @Autowired
    private Validator validator;

    public ResponseEntity<ResponseObject> getAllPagingLocation(MaterialTablePagingRequest pagingRequest, PagedResourcesAssembler<MV_LOCATION> assembler){
        ResponseObject result;
        try{
            Map<String, Object> filter = new HashMap<>();
            Page<MV_LOCATION> datas;

            if (isNotBlank(pagingRequest.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingRequest.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingRequest.getSearch().add(key+"~"+value);
                }
            }

            Specification<MV_LOCATION> specification = pagingRequest.getSearch().isEmpty() ?
                    mvLocationRepo.getSpecificationDefault(filter)
                    : mvLocationRepo.getSpecificationFromFilters(pagingRequest, filter);

            datas = mvLocationRepo.findAll(specification, PagingUtils.getPaging(pagingRequest));

            PagedModel<EntityModel<MV_LOCATION>> pagedData = assembler.toModel(datas);

            Map<String, Object> allData = new HashMap<>();
            allData.put(Constant.RESULT, datas.getContent());
            allData.put(Constant.PAGE, pagedData.getMetadata());
            allData.put(Constant.LINK, pagedData.getLinks());
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.PAGING, ConstantAccount.MASTER_LOCATION), allData);
            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<InputStreamResource> downloadFilterAll(MaterialTablePagingRequest pagingData) {
        try {
            Page<MV_LOCATION> data;
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

                long maxData = StreamSupport.stream(mvLocationRepo.findAll().spliterator(), false).count();
                pagingData.setSize((int) maxData);
                pagingData.setPage(1);

            }
            if (!pagingData.getSearch().isEmpty()) {
                data = this.mvLocationRepo.findAll(this.mvLocationRepo.getSpecificationFromFilters(pagingData, filter),
                        PagingUtils.getPaging(pagingData));
            } else {
                data = this.mvLocationRepo.findAll(this.mvLocationRepo.getSpecificationDefault(filter),
                        PagingUtils.getPaging(pagingData));
            }
            List<MV_LOCATION> listAction = data.getContent();
            List<LinkedHashMap<String, Object>> allData = new ArrayList<>();
            Integer no = 1;
            HttpHeaders headers = new HttpHeaders();
            if(data.hasContent()) {
                for (MV_LOCATION a : listAction) {
                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                    response.put("NO", no);
                    response.put("COUNTRY", a.getCountry());
                    response.put("PROVINCE", a.getProvince());
                    response.put("CITY", a.getCity());
                    response.put("DISTRICT", a.getDistrict());
                    response.put("SUB DISTRICT", a.getSubDistrict());
                    response.put("POSTAL CODE", a.getPostalCode());
                    allData.add(response);
                    no++;
                }
                ByteArrayInputStream in = DownloadToExcel.downloadsFiles(allData, "Excel");
                headers.add(Constant.CONTENT_DISPOSITION, "attachment; filename=" +
                        "LOCATION_" + UtilsDate.dateToString(new Date(), "yyyyMMddHHmmss") + ".xlsx");
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
    @SuppressWarnings({"java:S1192"})
    public ResponseEntity<ResponseObject> getPagingLocation(Integer locationType, MaterialTablePagingRequest pagingRequest, PagedResourcesAssembler<VW_LOCATION> assembler){
        ResponseObject result = new ResponseObject();
        try {
            Map<String, Object> filter = new HashMap<>();
            filter.put("locationTypeId", locationType);

            Page<VW_LOCATION> data = null;

            if (isNotBlank(pagingRequest.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingRequest.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingRequest.getSearch().add(key+"~"+value);
                }
            }

            Specification<VW_LOCATION> specification = pagingRequest.getSearch().isEmpty() ? vwLocationRepo.getSpecificationDefault(filter) : vwLocationRepo.getSpecificationFromFilters(pagingRequest, filter);
            data = vwLocationRepo.findAll(specification, PagingUtils.getPaging(pagingRequest));

            logger.info(data.getContent().toString());

            List<MasterLocationResponseDto> responses = new ArrayList<>();

            for(VW_LOCATION locationValue : data.getContent()){
                MasterLocationResponseDto response = new MasterLocationResponseDto();
                response.setLocationId(locationValue.getLocationId());
                response.setLocationCode(locationValue.getLocationCode());
                response.setLocationName(locationValue.getLocationName());
                response.setLocationType(locationValue.getLocationType());
                response.setLocationReference(locationValue.getLocationReference());
                response.setLocationParentType(locationValue.getLocationParentType());
                response.setLocationParent(locationValue.getLocationParent());
                response.setStatus(locationValue.getStatus());
                responses.add(response);
            }

            PagedModel pagedData = assembler.toModel(data);

            Map<String, Object> d = new HashMap<>();
            d.put(Constant.RESULT, responses);
            d.put(Constant.PAGE, pagedData.getMetadata());
            d.put(Constant.LINK, pagedData.getLinks());

            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage(UtilsAccount.messageSuccess(ConstantAccount.PAGING, ConstantAccount.MASTER_LOCATION));
            result.setData(d);

            return new ResponseEntity<>(result, HttpStatus.OK);

        }catch (Exception e){
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<InputStreamResource> downloadFilter(Integer locationType, MaterialTablePagingRequest pagingData) {
        try {
            Page<VW_LOCATION> data;
            Map<String, Object> filter = new HashMap<>();
            filter.put("locationTypeId", locationType);

            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key+"~"+value);
                }
            }

            if (pagingData.getPage() != null && pagingData.getSize() != null) {

                long maxData = StreamSupport.stream(mlocationRepo.findAll().spliterator(), false).count();
                pagingData.setSize((int) maxData);
                pagingData.setPage(1);

            }
            if (!pagingData.getSearch().isEmpty()) {
                data = this.vwLocationRepo.findAll(this.vwLocationRepo.getSpecificationFromFilters(pagingData, filter),
                        PagingUtils.getPaging(pagingData));
            } else {
                data = this.vwLocationRepo.findAll(this.vwLocationRepo.getSpecificationDefault(filter),
                        PagingUtils.getPaging(pagingData));
            }
            List<VW_LOCATION> listAction = data.getContent();
            List<LinkedHashMap<String, Object>> allData = new ArrayList<>();
            Integer no = 1;
            HttpHeaders headers = new HttpHeaders();
            if(data.hasContent()) {
                for (VW_LOCATION a : listAction) {
                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                    response.put("NO", no);
                    response.put("CODE", a.getLocationCode());
                    response.put("LOCATION TYPE", a.getLocationType());
                    response.put("LOCATION NAME", a.getLocationName());
                    response.put("LOCATION PARENT", a.getLocationParent());
                    response.put("LOCATION PARENT TYPE", a.getLocationParentType());
                    response.put("LOCATION REFERENCE", a.getLocationReference());
                    response.put("STATUS", capitalizeFully(a.getStatus()));
                    allData.add(response);
                    no = no + 1;
                }
                ByteArrayInputStream in = DownloadToExcel.downloadsFiles(allData, "Excel");
                headers.add(Constant.CONTENT_DISPOSITION, "attachment; filename=" +
                        "LOCATION_" + UtilsDate.dateToString(new Date(), "yyyyMMddHHmmss") + ".xlsx");
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
    @SuppressWarnings({"java:S1192"})
    public ResponseEntity<ResponseObject> getAllCountry() {
        LinkedHashMap<String, Object> responseData = new LinkedHashMap<>();
        List<M_LOCATION> countryList = mlocationRepo.findAllByLocationType(Constant.LOCATION_TYPE_COUNTRY);
        List<DropdownDTO> dropdownDTOs = new ArrayList<>();
        for (M_LOCATION domain : countryList) {
            DropdownDTO dto = new DropdownDTO();
            dto.setId(domain.getLocationId());
            dto.setName(domain.getLocationName());
            dropdownDTOs.add(dto);
        }
        responseData.put("data", dropdownDTOs);
        ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, UtilsAccount.messageSuccess(ConstantAccount.DDL, Constant.LOCATION_TYPE_COUNTRY), responseData);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @SuppressWarnings({"java:S1192"})
    public ResponseEntity<ResponseObject> getAllPostalCode(Integer id) {
        LinkedHashMap<String, Object> responseData = new LinkedHashMap<>();
        List<M_LOCATION> provinceList = mlocationRepo.findAllByLocationTypeAndLocationParent("POSTAL_CODE", id);
        List<DropdownDTO> dropdownDTOs = new ArrayList<>();
        for (M_LOCATION domain : provinceList) {
            DropdownDTO dto = new DropdownDTO();
            dto.setId(domain.getLocationId());
            dto.setName(domain.getLocationName());
            dropdownDTOs.add(dto);
        }
        responseData.put("data", dropdownDTOs);
        ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, UtilsAccount.messageSuccess(ConstantAccount.DDL, "POSTAL_CODE"), responseData);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> getAllProvince(Integer id) {
        LinkedHashMap<String, Object> responseData = new LinkedHashMap<>();
        List<M_LOCATION> provinceList = mlocationRepo.findAllByLocationTypeAndLocationParent("PROVINCE", id);
        List<DropdownDTO> dropdownDTOs = new ArrayList<>();
        for (M_LOCATION domain : provinceList) {
            DropdownDTO dto = new DropdownDTO();
            dto.setId(domain.getLocationId());
            dto.setName(domain.getLocationName());
            dropdownDTOs.add(dto);
        }
        responseData.put("data", dropdownDTOs);
        ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, UtilsAccount.messageSuccess(ConstantAccount.DDL, "PROVINCE"), responseData);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> createDataLocation(CreateMasterLocationDTO request) {

        ResponseObject result;
        try {
            ResponseEntity<ResponseObject> validate = this.validateCreateDataLocation(Boolean.FALSE, request);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            M_LOCATION createLocation = new M_LOCATION();
            createLocation.setCreatedBy(UserDetailUtils.getUsername());
            createLocation.setStatus(FlowStatus.ACTIVE.name());
            createLocation.setCreatedDate(new Date());
            createLocation.setLocationCode(request.getLocationCode().strip());
            createLocation.setLocationName(request.getLocationName().strip());
            createLocation.setLocationTypeId(request.getLocationType());
            Optional<R_GLOBAL_TYPE_VALUE> getLocTypName = rGlobalTypeValueRepo.findById(request.getLocationType());
            createLocation.setLocationType(getLocTypName.get().getGlbValue());
            createLocation.setLocationParent(request.getLocationParent());
            createLocation.setLocationReference(request.getLocationReference());
            mlocationRepo.save(createLocation);
            mvLocationRepo.refreshMaterializedView("MV_LOCATION");

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                        UtilsAccount.messageSuccess(ConstantAccount.CREATE, ConstantAccount.MASTER_LOCATION), createLocation);

            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateCreateDataLocation(Boolean api, CreateMasterLocationDTO request) {
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

            List<M_LOCATION> cekExist = mlocationRepo.findAllByLocationTypeId(request.getLocationType());
            for(M_LOCATION cek : cekExist) {
                if(cek.getLocationCode().equalsIgnoreCase(request.getLocationCode().strip())) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Location code already exist!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
            }

            List<M_LOCATION> cekExistUnique = mlocationRepo.findAllByLocationNameAndLocationTypeIdAndLocationParent(request.getLocationName().strip(), request.getLocationType(), request.getLocationParent());
            if(!cekExistUnique.isEmpty()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Location name with location type and location parent already exist!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }

            if(Boolean.TRUE.equals(api)) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        UtilsAccount.messageSuccess(ConstantAccount.VALIDATE, ConstantAccount.MASTER_LOCATION), ResponseUtils.DATA_EMPTY), HttpStatus.OK);
            } else {
                return null;
            }

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> updateDataLocation(Integer locationId, UpdateMasterLocationDTO request) {
        ResponseObject result;
        try {

            ResponseEntity<ResponseObject> validate = this.validateUpdateDataLocation(Boolean.FALSE, locationId, request);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            Optional<M_LOCATION> getLocationData = mlocationRepo.findById(locationId);
            M_LOCATION updateLocation = getLocationData.get();
            Optional<R_GLOBAL_TYPE_VALUE> getLocTypName = rGlobalTypeValueRepo.findById(request.getLocationType());
            updateLocation.setLocationType(getLocTypName.isPresent()?getLocTypName.get().getGlbValue():null);
            updateLocation.setUpdatedBy(UserDetailUtils.getUsername());
            updateLocation.setUpdatedDate(new Date());
            updateLocation.setLocationCode(request.getLocationCode().strip());
            updateLocation.setLocationName(request.getLocationName().strip());
            updateLocation.setLocationTypeId(request.getLocationType());
            updateLocation.setLocationParent(request.getLocationParent());
            updateLocation.setLocationReference(request.getLocationReference());
            mlocationRepo.save(updateLocation);
            mvLocationRepo.refreshMaterializedView("MV_LOCATION");

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.UPDATE, ConstantAccount.MASTER_LOCATION), updateLocation);

            return new ResponseEntity<>(result, result.getHttpCode());


        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateUpdateDataLocation(Boolean api, Integer locationId, UpdateMasterLocationDTO request) {
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

            Optional<M_LOCATION> getLocationData = mlocationRepo.findById(locationId);
            if(!getLocationData.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        UtilsAccount.messageDataNotFound(ConstantAccount.MASTER_LOCATION, locationId), null), HttpStatus.NOT_FOUND);
            }

            List<M_LOCATION> cekExist = mlocationRepo.findAllByLocationTypeId(request.getLocationType());
            for(M_LOCATION cek : cekExist) {
                if(cek.getLocationCode().equalsIgnoreCase(request.getLocationCode().strip()) && !cek.getLocationId().equals(locationId)) {
                    System.out.println(cek.getLocationId() + " " + locationId);
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Location code already exist!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
            }

            List<M_LOCATION> cekExistUnique = mlocationRepo.findAllByLocationNameAndLocationTypeIdAndLocationParent(request.getLocationName().strip(), request.getLocationType(), request.getLocationParent());
            for(M_LOCATION cek : cekExistUnique) {
                if(!cekExistUnique.isEmpty() && !cek.getLocationId().equals(locationId)) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Location name with location type and location parent already exist!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
            }

            return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.MASTER_LOCATION);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getFromMasterGlobal(String groupName){
        try {
            ResponseObject result;
            List<R_GLOBAL_TYPE_VALUE> listLocationType = globalTypeService.getDetailGlobalType(groupName);
            if(listLocationType.isEmpty()){
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, null), HttpStatus.NOT_FOUND);
            }

            List<DropdownDTO> dropdownDTOS= new ArrayList<>();
            for (R_GLOBAL_TYPE_VALUE globalType : listLocationType) {
                DropdownDTO dto = new DropdownDTO();
                dto.setId(globalType.getGlbTypeValId());
                dto.setName(globalType.getName());
                dropdownDTOS.add(dto);
            }
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    ResponseUtils.MESSAGE_SUCCESS, dropdownDTOS);

            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getFromMasterLocation(String requestType, Integer id){
        try {
            ResponseObject result;
            List<M_LOCATION> listValue = null;
            if(requestType.equalsIgnoreCase(Constant.MASTER_LOCATION_TYPE_BY_LOCATION_TYPE_ID)) {
                listValue = mlocationRepo.findAllByLocationTypeId(id);
            } else if (requestType.equalsIgnoreCase(Constant.MASTER_LOCATION_TYPE_LOCATION_PARENT_ID)) {
                listValue = mlocationRepo.findAllByLocationParent(id);
            }else{
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Type Not Recognized", ResponseUtils.MESSAGE_BAD_REQUEST);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            if(listValue.isEmpty() || ObjectUtils.isEmpty(listValue)){
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, null), HttpStatus.NOT_FOUND);
            }

            List<DropdownDTO> dropdownDTOS= new ArrayList<>();
            for (M_LOCATION location : listValue) {
                DropdownDTO dto = new DropdownDTO();
                dto.setId(location.getLocationId());
                dto.setName(location.getLocationName());
                dropdownDTOS.add(dto);
            }
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    ResponseUtils.MESSAGE_SUCCESS, dropdownDTOS);

            return new ResponseEntity<>(result, result.getHttpCode());

        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getPagingFromMasterLocation(MaterialTablePagingRequest pagingRequest,
                                                                      PagedResourcesAssembler<M_LOCATION> assembler, String requestType, Integer id){
        try {
            ResponseObject result;
            Map<String, Object> filter = new HashMap<>();

            if(requestType.equalsIgnoreCase(Constant.MASTER_LOCATION_TYPE_BY_LOCATION_TYPE_ID)) {
                filter.put("locationTypeId", id);
            } else if (requestType.equalsIgnoreCase(Constant.MASTER_LOCATION_TYPE_LOCATION_PARENT_ID)) {
                filter.put("locationParent", id);
            }else{
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Type Not Recognized", ResponseUtils.MESSAGE_BAD_REQUEST);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            filter.put("status", FlowStatus.ACTIVE.name());

            Page<M_LOCATION> datas;

            if(!pagingRequest.getSearch().isEmpty()) {
                datas = mlocationRepo.findAll(mlocationRepo.getSpecificationFromFilters(pagingRequest, filter), PagingUtils.getPaging(pagingRequest));
            } else {
                datas = mlocationRepo.findAll(mlocationRepo.getSpecificationDefault(filter), PagingUtils.getPaging(pagingRequest));
            }

            if(datas.isEmpty() || ObjectUtils.isEmpty(datas)){
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, null), HttpStatus.NOT_FOUND);
            }

            List<PagingLocationDTO> dropdownDTOS= new ArrayList<>();
            for (M_LOCATION location : datas.getContent()) {
                PagingLocationDTO dto = new PagingLocationDTO();
                dto.setId(location.getLocationId());
                dto.setLocationName(location.getLocationName());
                dropdownDTOS.add(dto);
            }
            PagedModel<EntityModel<M_LOCATION>> pagedData = assembler.toModel(datas);

            Map<String, Object> d = new HashMap<>();
            d.put("result", dropdownDTOS);
            d.put("page", pagedData.getMetadata());
            d.put("links", pagedData.getLinks());
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get List Master Late Charge", d), HttpStatus.OK);

        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> activeInactive(ActiveInactiveDto request){
        String message = "";
        ResponseObject result;
        try {
            Optional<M_LOCATION> locationData = mlocationRepo.findById(request.getLocationId());
            if(locationData.isEmpty()){
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, null), HttpStatus.NOT_FOUND);
            }

            M_LOCATION data = locationData.get();
            LinkedHashMap<String, Object> oldData = new LinkedHashMap<>();
            oldData.put("locationId", data.getLocationId());
            AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
            if(data.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())){
                data.setStatus(FlowStatus.INACTIVE.name());
                auditTrail.setOperation(FlowStatus.INACTIVE.name());
                message = ResponseUtils.MESSAGE_INACTIVE;
            }else if(data.getStatus().equalsIgnoreCase(FlowStatus.INACTIVE.name())){
                data.setStatus(FlowStatus.ACTIVE.name());
                auditTrail.setOperation(FlowStatus.ACTIVE.name());
                message = ResponseUtils.MESSAGE_ACTIVE;
            }
            data.setUpdatedBy(UserDetailUtils.getUsername());
            data.setUpdatedDate(new Date());
            M_LOCATION saveData = mlocationRepo.save(data);

            ObjectMapper mapper = new ObjectMapper();
            String oldValue = mapper.writeValueAsString(oldData);
            auditTrail.setOldValue(oldValue);
            auditTrail.setRemark(request.getRemark());
            auditTrail.setTableName("M_LOCATION");
            auditTrail.setDataId(data.getLocationId().toString());
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

            LinkedHashMap<String, Object> newMa = new LinkedHashMap<>();
            newMa.put("addressId", saveData.getLocationId());
            newMa.put("description", request.getRemark());
            String newValue = mapper.writeValueAsString(newMa);
            auditTrail.setNewValue(newValue);
            auditTrailRepo.save(auditTrail);

            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, message, ResponseUtils.MESSAGE_OK), HttpStatus.CREATED);
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getLocationParentType(Integer locationTypeId){
        ResponseObject result;
        try {
            List<R_GLOBAL_TYPE_VALUE> listLocationType = globalTypeService.getDetailGlobalType("Location Type");
//                    rGlobalTypeValueRepo.findAllByGlobalType(23);
            if (listLocationType.isEmpty()){
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, null), HttpStatus.NOT_FOUND);
            }

            List<NameValueDdlResponseDto> nameValueDdlResponseDtos = new ArrayList<>();
            listLocationType.stream().filter(e ->e.getGlbTypeValId()<locationTypeId).forEach(globalTypeValue -> {
                NameValueDdlResponseDto nameValueDdlResponseDto = new NameValueDdlResponseDto();
                nameValueDdlResponseDto.setValue(globalTypeValue.getGlbTypeValId());
                nameValueDdlResponseDto.setName(globalTypeValue.getName());
                nameValueDdlResponseDtos.add(nameValueDdlResponseDto);
            });

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("result", nameValueDdlResponseDtos);
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    ResponseUtils.MESSAGE_SUCCESS, responseData);

            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> detailLocation(Integer locationId){
        ResponseObject result;
        try {
            Optional<VW_LOCATION> findLocation = vwLocationRepo.findByLocationId(locationId);
            if(findLocation.isEmpty()){
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, null), HttpStatus.NOT_FOUND);
            }
            VW_LOCATION vwLocation = findLocation.get();
            DetailLocationDtoResponse detailLocationDtoResponse = new DetailLocationDtoResponse();

            detailLocationDtoResponse.setId(vwLocation.getLocationId());
            NameValueDdlResponseDto locationType = new NameValueDdlResponseDto();
            locationType.setValue(vwLocation.getLocationTypeId());
            locationType.setName(vwLocation.getLocationType());
            detailLocationDtoResponse.setLocationType(locationType);


                NameValueDdlResponseDto locationParentType = new NameValueDdlResponseDto();

                    locationParentType.setValue(vwLocation.getLocationParentTypeId());
                    locationParentType.setName(vwLocation.getLocationParentType());
                detailLocationDtoResponse.setLocationParentType(locationParentType);


            NameValueDdlResponseDto locationParent = new NameValueDdlResponseDto();
            locationParent.setValue(vwLocation.getLocationParentId());
            locationParent.setName(vwLocation.getLocationParent());
            detailLocationDtoResponse.setLocationParent(locationParent);

            NameValueDdlResponseDto locationReference = new NameValueDdlResponseDto();
            locationReference.setValue(vwLocation.getLocationReferenceId());
            locationReference.setName(vwLocation.getLocationReference());
            detailLocationDtoResponse.setLocationReference(locationReference);


            detailLocationDtoResponse.setLocationCode(vwLocation.getLocationCode());
            detailLocationDtoResponse.setLocationName(vwLocation.getLocationName());

            detailLocationDtoResponse.setStatus(vwLocation.getStatus());
            detailLocationDtoResponse.setCreatedBy(vwLocation.getCreatedBy());
            detailLocationDtoResponse.setCreatedDate(vwLocation.getCreatedDate());
            detailLocationDtoResponse.setUpdatedBy(vwLocation.getUpdatedBy());
            detailLocationDtoResponse.setUpdatedDate(vwLocation.getUpdatedDate());

            //acr
            List<AUDIT_TRAIL> auditTrail = auditTrailRepo.findAll().stream()
                    .filter(e -> e.getTableName().equalsIgnoreCase("M_LOCATION"))
                    .filter(f -> f.getDataId().equalsIgnoreCase(vwLocation.getLocationId().toString()))
                    .collect(Collectors.toList());
            detailLocationDtoResponse.setActiveInactiveLog(auditTrail);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    ResponseUtils.MESSAGE_SUCCESS, detailLocationDtoResponse);

            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> detailLocationAll(Integer postalCodeId){
        ResponseObject result;
        try {
            Optional<MV_LOCATION> findMVLocation = mvLocationRepo.findById(postalCodeId);
            if(findMVLocation.isEmpty()){
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        ResponseUtils.MESSAGE_NOT_FOUND, null), HttpStatus.NOT_FOUND);
            }
            MV_LOCATION mLocation = findMVLocation.get();

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    ResponseUtils.MESSAGE_SUCCESS, mLocation);

            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public ResponseEntity<ResponseObject> getAllCity(Integer id) {
        LinkedHashMap<String, Object> responseData = new LinkedHashMap<>();
        List<M_LOCATION> provinceList = mlocationRepo.findAllByLocationTypeAndLocationParent("CITY", id);
        List<DropdownDTO> dropdownDTOs = new ArrayList<>();
        for (M_LOCATION domain : provinceList) {
            DropdownDTO dto = new DropdownDTO();
            dto.setId(domain.getLocationId());
            dto.setName(domain.getLocationName());
            dropdownDTOs.add(dto);
        }
        responseData.put("data", dropdownDTOs);
        ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, CommonVariables.SUCCESS,
                responseData);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    public ResponseEntity<ResponseObject> getAllDistrict(Integer id) {
        LinkedHashMap<String, Object> responseData = new LinkedHashMap<>();
        List<M_LOCATION> provinceList = mlocationRepo.findAllByLocationTypeAndLocationParent("DISTRICT", id);
        List<DropdownDTO> dropdownDTOs = new ArrayList<>();
        for (M_LOCATION domain : provinceList) {
            DropdownDTO dto = new DropdownDTO();
            dto.setId(domain.getLocationId());
            dto.setName(domain.getLocationName());
            dropdownDTOs.add(dto);
        }
        responseData.put("data", dropdownDTOs);
        ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, CommonVariables.SUCCESS,
                responseData);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    public ResponseEntity<ResponseObject> getAllSubDistrict(Integer id) {
        LinkedHashMap<String, Object> responseData = new LinkedHashMap<>();
        List<M_LOCATION> provinceList = mlocationRepo.findAllByLocationTypeAndLocationParent("SUB_DISTRICT", id);
        List<DropdownDTO> dropdownDTOs = new ArrayList<>();
        for (M_LOCATION domain : provinceList) {
            DropdownDTO dto = new DropdownDTO();
            dto.setId(domain.getLocationId());
            dto.setName(domain.getLocationName());
            dropdownDTOs.add(dto);
        }
        responseData.put("data", dropdownDTOs);
        ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, CommonVariables.SUCCESS,
                responseData);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}