package com.dbs.module.account.master.gassource.services;

import com.dbs.module.account.master.gassource.dto.QualityDetailDTO;
import com.dbs.module.account.master.gassource.dto.UpdateGSDTO;
import com.dbs.module.account.master.gassource.dto.GasSourceDTO;
import com.dbs.module.account.master.gassource.dto.GSCriteriaDTO;
import com.dbs.module.account.master.gassource.dto.ActiveInactiveDTO;
import com.dbs.module.account.master.gassource.dto.AssignGSDTO;
import com.dbs.common.base.utils.*;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.*;
import com.dbs.database.crm.entities.accountmanagement.*;
import com.dbs.database.crm.entities.usermanagement.*;
import com.dbs.database.crm.repositories.accountmanagement.*;
import com.dbs.database.crm.repositories.accountmanagement.Account.MAccountGasSourceRepo;
import com.dbs.database.crm.repositories.accountmanagement.Account.MAccountRepo;
import com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement.VwSaRepo;
import com.dbs.database.crm.repositories.usermanagement.*;
import com.dbs.module.account.utils.ConstantAccount;
import com.dbs.module.account.utils.UtilsAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;

import static com.dbs.common.base.utils.CommonHelper.removeSpace;
import static com.dbs.common.library.utils.StringUtils.capitalizeFully;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class GasSourceServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(GasSourceServiceImpl.class);
    @Autowired
    private MGasSourceRepo mGasSourceRepo;
    @Autowired
    private Validator validator;
    @Autowired
    private RGasSourceDetailRepo rGasSourceDetailRepo;
    @Autowired
    private RGasSourceCriteriaRepo rGasSourceCriteriaRepo;
    @Autowired
    private MCostCenterRepo costCenterRepo;
    @Autowired
    private MGlobalTypeRepo mGlobalTypeRepo;
    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;
    @Autowired
    private MUserRepo mUserRepo;
    @Autowired
    private AuditTrailRepo auditTrailRepo;
    @Autowired
    private VWGasSourceRepo vwGasSourceRepo;
    @Autowired
    private MAccountGasSourceRepo mAccountGasSourceRepo;
    @Autowired
    private MAccountRepo mAccountRepo;
    @Autowired
    private VWAccountGSRepo vwAccountGSRepo;
    @Autowired
    private VwSaRepo vwSaRepo;
    @Autowired
    private GlobalTypeValueService globalTypeValueService;
    @Autowired
    private ObjectMapper objectMapper;


    @SuppressWarnings({"java:S3776","java:S1192"})
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> create(GasSourceDTO request) {
        try {
            ResponseObject result;

            ResponseEntity<ResponseObject> validate = this.validateCreate(Boolean.FALSE, request);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            M_GAS_SOURCE mGasSource = new M_GAS_SOURCE(request.getCalorieCode().strip(), request.getName().strip(), request.getDescription(),
                    request.getUom(), UserDetailUtils.getUserEntity());
            mGasSource.setCreatedBy(UserDetailUtils.getUsername());
            mGasSource.setStatus(FlowStatus.ACTIVE.name());
            mGasSource.setCreatedDate(new Date());
            M_GAS_SOURCE newGasSource = mGasSourceRepo.save(mGasSource);

            List<R_GAS_SOURCE_CRITERIA> allCriteria = new ArrayList<>();
            for(GSCriteriaDTO c : request.getCriteria()) {
                R_GAS_SOURCE_CRITERIA criteria = new R_GAS_SOURCE_CRITERIA();
                criteria.setGasSourceId(newGasSource.getGasSourceId());
                Optional<M_COSTCENTER> costCenter = costCenterRepo.findByccId(c.getCostCenterId());
                criteria.setCostCenterId(costCenter.map(M_COSTCENTER::getCcId).orElse(null));
                SimpleDateFormat formatDate = new SimpleDateFormat(Constant.FORMAT_START_END_DATE);
                try {
                    criteria.setStartDate(formatDate.parse(c.getStartDate()));
                    criteria.setEndDate(c.getEndDate()!=null?formatDate.parse(c.getEndDate()):null);
                } catch (Exception e) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "Invalid date", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                criteria.setDescription(c.getDescription());
                criteria.setCreatedBy(UserDetailUtils.getUsername());
                criteria.setCreatedDate(new Date());
                criteria.setStatus(FlowStatus.ACTIVE.name());
                allCriteria.add(criteria);
            }
            rGasSourceCriteriaRepo.saveAll(allCriteria);

            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                    UtilsAccount.messageSuccess(ConstantAccount.CREATE, ConstantAccount.MASTER_GASSOURCE), newGasSource), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateCreate(Boolean api, GasSourceDTO request) {
        try {
            ResponseObject result;

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

            for(GSCriteriaDTO validateReq : request.getCriteria()) {
                var violationsCriteria = validator.validate(validateReq);
                if (!violationsCriteria.isEmpty()) {
                    List<Map<String, Object>> violationHeaderList = new ArrayList<>();
                    List<String> validateHeader = new ArrayList<>();
                    for (var violation : violationsCriteria) {
                        logger.error(violation.getMessage());
                        Map<String, Object> data = new HashMap<>();
                        validateHeader.add(violation.getMessage());
                        data.put(violation.getPropertyPath().toString(), violation.getMessage());
                        violationHeaderList.add(data);
                    }
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, validateHeader.get(0), violationHeaderList);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
            }


            Optional<M_GAS_SOURCE> calorieCode = mGasSourceRepo.findTopByCalorieCodeIgnoreCase(request.getCalorieCode().strip());
            Optional<M_GAS_SOURCE> calorieName = mGasSourceRepo.findTopByNameIgnoreCase(request.getName().strip());
            if (calorieCode.isPresent() && calorieName.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Calorie Code and Name already Exist", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
            if (calorieCode.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Calorie Code already Exist", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
            if (calorieName.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Name already Exist", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
            if (request.getCriteria() == null) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Gas Source Criteria cannot be null", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }

            return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.MASTER_GASSOURCE);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings({"java:S1192"})
    public ResponseEntity<ResponseObject> getUOM() {
        try {
            Optional<M_GLOBAL_TYPE> mGlobalType = mGlobalTypeRepo.findByGroupName("Gas Source UOM");
            List<LinkedHashMap<String, Object>> allResponse = mGlobalType
                    .filter(g -> FlowStatus.ACTIVE.name().equalsIgnoreCase(g.getStatus()))
                    .map(g -> rGlobalTypeValueRepo.findAll().stream()
                            .filter(v -> Objects.equals(v.getGlobalType(), g.getGlbTypeId()) &&
                                    FlowStatus.ACTIVE.name().equalsIgnoreCase(v.getStatus()))
                            .map(v -> {
                                LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                                response.put("uomId", v.getGlbTypeValId());
                                response.put("uomName", v.getName());
                                return response;
                            })
                            .collect(Collectors.toList()))
                    .orElseGet(Collections::emptyList);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get UOM", allResponse), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getCostCenter() {
        try {
            List<LinkedHashMap<String, Object>> allResponse = costCenterRepo.findAll().stream()
                    .filter(c -> FlowStatus.ACTIVE.name().equalsIgnoreCase(c.getStatus()))
                    .map(c -> {
                        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                        response.put("costCenterId", c.getCcId());
                        response.put("costCenterName", c.getCode() + " - " + c.getName());
                        return response;
                    })
                    .collect(Collectors.toList());
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get all cost center", allResponse), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> getPagging(MaterialTablePagingRequest pagingData,
                                                     PagedResourcesAssembler<VW_GAS_SOURCE> assembler) {
        try {
            Map<String, Object> filter = new HashMap<>();
            filter.put("entityId", UserDetailUtils.getUserEntity());

            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key+"~"+value);
                }
            }

            Specification<VW_GAS_SOURCE> specification = pagingData.getSearch().isEmpty()
                    ? vwGasSourceRepo.getSpecificationDefault(filter)
                    : vwGasSourceRepo.getSpecificationFromFilters(pagingData, filter);

            Page<VW_GAS_SOURCE> data = vwGasSourceRepo.findAll(specification, PagingUtils.getPaging(pagingData));

            List<LinkedHashMap<String, Object>> allData = data.getContent().stream()
                    .map(g -> {
                        LinkedHashMap<String, Object> gasSource = new LinkedHashMap<>();
                        gasSource.put("gasSourceId", g.getGasSourceId());
                        gasSource.put("calorieCode", g.getCalorieCode());
                        gasSource.put("name", g.getName());
                        gasSource.put("description", g.getDescription());
                        gasSource.put("uom", g.getUom());

                        List<LinkedHashMap<String, Object>> criteriaList = rGasSourceCriteriaRepo.findByGasSourceId(g.getGasSourceId()).stream().map(c -> {
                            LinkedHashMap<String, Object> cc = new LinkedHashMap<>();
                            cc.put("gasSourceCriteriaId", c.getGasSourceCriteriaId());
                            cc.put("gasSourceId", c.getGasSourceId());

                            Optional<M_COSTCENTER> costCenter = costCenterRepo.findByccId(c.getCostCenterId());
                            cc.put("costCenter", costCenter.isPresent() ? costCenter.get().getCode() +" - "+costCenter.get().getName() : "");

                            String formatDate = Constant.FORMAT_DATE;
                            cc.put("startDate", c.getStartDate()!=null?CommonHelper.convertDateToString(formatDate, c.getStartDate()):null);
                            cc.put("endDate", c.getEndDate()!=null?CommonHelper.convertDateToString(formatDate, c.getEndDate()):null);
                            cc.put("description", c.getDescription());
                            cc.put("status", c.getStatus());
                            return cc;
                        }).collect(Collectors.toList());

                        gasSource.put("criteria", criteriaList);
                        gasSource.put("status", g.getStatus());
                        return gasSource;
                    })
                    .collect(Collectors.toList());

            PagedModel<EntityModel<VW_GAS_SOURCE>> pagedData = assembler.toModel(data);
            Map<String, Object> d = new HashMap<>();
            d.put(Constant.RESULT, allData);
            d.put(Constant.PAGE, pagedData.getMetadata());
            d.put(Constant.LINK, pagedData.getLinks());
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get view paging", d), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<InputStreamResource> downloadFilter(MaterialTablePagingRequest pagingData) {
        try {
            Page<VW_GAS_SOURCE> data;
            Map<String, Object> filter = new HashMap<>();
            filter.put("entityId", UserDetailUtils.getUserEntity());
            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key+"~"+value);
                }
            }
            if (pagingData.getPage() != null && pagingData.getSize() != null) {
                long maxData = StreamSupport.stream(vwGasSourceRepo.findAll().spliterator(), false).count();
                pagingData.setSize((int) maxData);
                pagingData.setPage(1);
            }
            if (!pagingData.getSearch().isEmpty()) {
                data = this.vwGasSourceRepo.findAll(this.vwGasSourceRepo.getSpecificationFromFilters(pagingData, filter),
                        PagingUtils.getPaging(pagingData));
            } else {
                data = this.vwGasSourceRepo.findAll(this.vwGasSourceRepo.getSpecificationDefault(filter),
                        PagingUtils.getPaging(pagingData));
            }
            List<VW_GAS_SOURCE> listAction = data.getContent();
            List<LinkedHashMap<String, Object>> allData = new ArrayList<>();
            Integer no = 1;
            HttpHeaders headers = new HttpHeaders();
            if(data.hasContent()) {
                for (VW_GAS_SOURCE a : listAction) {
                    List<R_GAS_SOURCE_DETAIL> dataQuality = rGasSourceDetailRepo.findAllByGasSourceId(a.getGasSourceId());
                    if(!dataQuality.isEmpty()) {
                        for(R_GAS_SOURCE_DETAIL quality : dataQuality) {
                            LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                            response.put("NO", no);
                            response.put("CALORIE CODE", a.getCalorieCode());
                            response.put("GAS SOURCE NAME", a.getName());
                            response.put("UOM", a.getUom());
                            response.put("DESCRIPTION", a.getDescription());
                            response.put("STATUS", capitalizeFully(a.getStatus()));
                            response.put("DOCUMENT NUMBER", quality.getDocumentNumber());
                            response.put("START DATE", Objects.nonNull(quality.getStartDate())?UtilsDate.dateToString(quality.getStartDate(), Constant.FORMAT_START_END_DATE):null);
                            response.put("END DATE", Objects.nonNull(quality.getEndDate())?UtilsDate.dateToString(quality.getEndDate(), Constant.FORMAT_START_END_DATE):null);
                            response.put("VALUE (M3/MMBTU)", quality.getM3());
                            response.put("VALUE (BTU/SCF (GHV))", quality.getBtu());
                            response.put("SG", quality.getSg());
                            response.put("N2", quality.getN2());
                            response.put("CO2", quality.getCo2());
                            response.put("DESCRIPTION GAS QUALITY", quality.getDescription());
                            response.put("STATUS GAS QUALITY", capitalizeFully(quality.getStatus()));
                            allData.add(response);
                            no++;
                        }
                    } else {
                        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                        response.put("NO", no);
                        response.put("CALORIE CODE", a.getCalorieCode());
                        response.put("GAS SOURCE NAME", a.getName());
                        response.put("UOM", a.getUom());
                        response.put("DESCRIPTION", a.getDescription());
                        response.put("STATUS", capitalizeFully(a.getStatus()));
                        response.put("DOCUMENT NUMBER", null);
                        response.put("START DATE", null);
                        response.put("END DATE", null);
                        response.put("VALUE (M3/MMBTU)", null);
                        response.put("VALUE (BTU/SCF (GHV))", null);
                        response.put("SG", null);
                        response.put("N2", null);
                        response.put("CO2", null);
                        response.put("DESCRIPTION GAS QUALITY", null);
                        response.put("STATUS GAS QUALITY", null);
                        allData.add(response);
                        no++;
                    }
                }
                ByteArrayInputStream in = DownloadToExcel.downloadsFiles(allData, "Excel");
                headers.add(Constant.CONTENT_DISPOSITION, "attachment; filename=" +
                        "GAS_SOURCE_" + UtilsDate.dateToString(new Date(), "yyyyMMddHHmmss") + ".xlsx");
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
    public ResponseEntity<ResponseObject> getDetail(Integer id) {
        try {
            Optional<M_GAS_SOURCE> mGasSource = mGasSourceRepo.findByGasSourceId(id);
            if (mGasSource.isPresent()) {
                M_GAS_SOURCE data = mGasSource.get();
                LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                response.put("gasSourceId", data.getGasSourceId());
                response.put("calorieCode", data.getCalorieCode());
                response.put("name", data.getName());
                response.put("description", data.getDescription());
                Optional<R_GLOBAL_TYPE_VALUE> rGlobalTypeValue = rGlobalTypeValueRepo.findByGlbTypeValId(data.getUom());
                response.put("uomId", data.getUom());
                response.put("uomName", rGlobalTypeValue.isPresent() ? rGlobalTypeValue.get().getName() : "");
                response.put("status", data.getStatus());

                List<LinkedHashMap<String, Object>> allCriteria = rGasSourceCriteriaRepo.findAll().stream()
                        .filter(c -> FlowStatus.ACTIVE.name().equalsIgnoreCase(c.getStatus()) &&
                                Objects.equals(data.getGasSourceId(), c.getGasSourceId()))
                        .map(c -> {
                            LinkedHashMap<String, Object> cc = new LinkedHashMap<>();
                            cc.put("gasSourceCriteriaId", c.getGasSourceCriteriaId());
                            cc.put("gasSourceId", c.getGasSourceId());

                            Optional<M_COSTCENTER> costCenter = costCenterRepo.findByccId(c.getCostCenterId());
                            cc.put("costCenterId", c.getCostCenterId());
                            cc.put("costCenterName", costCenter.isPresent() ? costCenter.get().getCode() +" - "+costCenter.get().getName() : "");

                            String formatDate = Constant.FORMAT_DATE;
                            cc.put("startDate", c.getStartDate()!=null?CommonHelper.convertDateToString(formatDate, c.getStartDate()):null);
                            cc.put("endDate", c.getEndDate()!=null?CommonHelper.convertDateToString(formatDate, c.getEndDate()):null);
                            cc.put("description", c.getDescription());
                            cc.put("status", c.getStatus());
                            cc.put("createdDate", c.getCreatedDate());
                            cc.put("createdBy", c.getCreatedBy());
                            cc.put("updatedDate", c.getUpdatedDate());
                            cc.put("updatedBy", c.getUpdatedBy());
                            return cc;
                        })
                        .collect(Collectors.toList());
                response.put("criteria", allCriteria);

                response.put("createdBy", data.getCreatedBy());
                response.put("updatedBy", data.getUpdatedBy());
                response.put("createdDate", data.getCreatedDate());
                response.put("updatedDate", data.getUpdatedDate());

                //acr
                List<AUDIT_TRAIL> auditTrail = auditTrailRepo.findAll().stream()
                        .filter(e -> e.getTableName().equalsIgnoreCase("M_GAS_SOURCE"))
                        .filter(f -> f.getDataId().equalsIgnoreCase(data.getGasSourceId().toString()))
                        .collect(Collectors.toList());
                response.put("activeInactiveLog", auditTrail);

                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        "Success get detail gas source", response), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Gas source not found", ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings({"java:S1192"})
    public ResponseEntity<ResponseObject> activeInactive(ActiveInactiveDTO request) {
        try {
            Optional<M_GAS_SOURCE> mGasSource = mGasSourceRepo.findByGasSourceId(request.getGasSourceId());
            if (mGasSource.isPresent()) {
                M_GAS_SOURCE data = mGasSource.get();
                LinkedHashMap<String, Object> oldGasSource = new LinkedHashMap<>();
                oldGasSource.put("gasSourceId", data.getGasSourceId());
                oldGasSource.put("calorieCode", data.getCalorieCode());
                oldGasSource.put("name", data.getName());
                oldGasSource.put("description", data.getDescription());
                oldGasSource.put("uom", data.getUom());
                oldGasSource.put("status", data.getStatus());
                AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
                ObjectMapper mapper = new ObjectMapper();
                String oldValue = mapper.writeValueAsString(oldGasSource);
                auditTrail.setOldValue(oldValue);
                auditTrail.setRemark(request.getRemark());
                auditTrail.setTableName("M_GAS_SOURCE");
                auditTrail.setDataId(data.getGasSourceId().toString());
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
                if (data.getStatus().equalsIgnoreCase(FlowStatus.INACTIVE.name())) {
                    data.setStatus(FlowStatus.ACTIVE.name());
                    data.setUpdatedBy(UserDetailUtils.getUsername());
                    data.setUpdatedDate(new Date());
                    M_GAS_SOURCE saveGS = mGasSourceRepo.save(data);
                    LinkedHashMap<String, Object> newGasSource = new LinkedHashMap<>();
                    newGasSource.put("gasSourceId", saveGS.getGasSourceId());
                    newGasSource.put("calorieCode", saveGS.getCalorieCode());
                    newGasSource.put("name", saveGS.getName());
                    newGasSource.put("description", saveGS.getDescription());
                    newGasSource.put("uom", saveGS.getUom());
                    newGasSource.put("status", saveGS.getStatus());
                    String newValue = mapper.writeValueAsString(newGasSource);
                    auditTrail.setNewValue(newValue);
                    auditTrail.setOperation(FlowStatus.ACTIVE.name());
                    auditTrailRepo.save(auditTrail);
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            ResponseUtils.MESSAGE_ACTIVE, ResponseUtils.DATA_EMPTY), HttpStatus.OK);
                }

                List<R_GAS_SOURCE_DETAIL> cekDetail = rGasSourceDetailRepo.findByGasSourceIdAndStatus(request.getGasSourceId(), FlowStatus.ACTIVE.name());
                if(!cekDetail.isEmpty()){
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "You cannot inactivate this gas source because there is a gas quality detail that are still active", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
                data.setStatus(FlowStatus.INACTIVE.name());
                data.setUpdatedBy(UserDetailUtils.getUsername());
                data.setUpdatedDate(new Date());
                M_GAS_SOURCE saveGS = mGasSourceRepo.save(data);
                LinkedHashMap<String, Object> newGasSource = new LinkedHashMap<>();
                newGasSource.put("gasSourceId", saveGS.getGasSourceId());
                newGasSource.put("calorieCode", saveGS.getCalorieCode());
                newGasSource.put("name", saveGS.getName());
                newGasSource.put("description", saveGS.getDescription());
                newGasSource.put("uom", saveGS.getUom());
                newGasSource.put("status", saveGS.getStatus());
                String newValue = mapper.writeValueAsString(newGasSource);
                auditTrail.setNewValue(newValue);
                auditTrail.setOperation(FlowStatus.INACTIVE.name());
                auditTrailRepo.save(auditTrail);
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        ResponseUtils.MESSAGE_INACTIVE, ResponseUtils.DATA_EMPTY), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Gas source not found", ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class, readOnly = false)
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> update(UpdateGSDTO request) {
        try {

            ResponseEntity<ResponseObject> validate = this.validateUpdate(Boolean.FALSE, request);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            Optional<M_GAS_SOURCE> mGasSource = mGasSourceRepo.findByGasSourceId(request.getGasSourceId());
            M_GAS_SOURCE gasSource = mGasSource.get();
            gasSource.setCalorieCode(request.getCalorieCode().strip());
            gasSource.setName(request.getName().strip());
            gasSource.setDescription(request.getDescription());
            gasSource.setUom(request.getUom());
            gasSource.setEntityId(UserDetailUtils.getUserEntity());
            gasSource.setUpdatedBy(UserDetailUtils.getUsername());
            gasSource.setUpdatedDate(new Date());
            List<R_GAS_SOURCE_CRITERIA> dataCriteria = new ArrayList<>();

            List<R_GAS_SOURCE_CRITERIA> dataGasCriteria = rGasSourceCriteriaRepo.findByGasSourceId(gasSource.getGasSourceId());
            List<Integer> receivedIdCri = new ArrayList<>();

            for (GSCriteriaDTO c : request.getCriteria()) {
                Optional<R_GAS_SOURCE_CRITERIA> data = c.getGasSourceCriteriaId()!=null?rGasSourceCriteriaRepo.findByGasSourceCriteriaId(c.getGasSourceCriteriaId()):null;
                R_GAS_SOURCE_CRITERIA gCriteria = new R_GAS_SOURCE_CRITERIA();
                if (data!=null) {
                    gCriteria = data.get();
                    gCriteria.setUpdatedBy(UserDetailUtils.getUsername());
                    gCriteria.setUpdatedDate(new Date());
                    receivedIdCri.add(gCriteria.getGasSourceCriteriaId());
                } else {
                    gCriteria.setCreatedBy(UserDetailUtils.getUsername());
                    gCriteria.setCreatedDate(new Date());
                }
                gCriteria.setGasSourceId(gasSource.getGasSourceId());
                gCriteria.setStatus(FlowStatus.ACTIVE.name());
                Optional<M_COSTCENTER> costCenter = costCenterRepo.findByccId(c.getCostCenterId());
                gCriteria.setCostCenterId(costCenter.map(M_COSTCENTER::getCcId).orElse(null));
                SimpleDateFormat formatDate = new SimpleDateFormat(Constant.FORMAT_START_END_DATE);
                try {
                    gCriteria.setStartDate(formatDate.parse(c.getStartDate()));
                    gCriteria.setEndDate(c.getEndDate()!=null?formatDate.parse(c.getEndDate()):null);
                } catch (Exception e) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                            "Invalid date", ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
                }
                gCriteria.setDescription(c.getDescription());
                dataCriteria.add(gCriteria);
            }
            M_GAS_SOURCE updateGS = mGasSourceRepo.save(gasSource);
            rGasSourceCriteriaRepo.saveAll(dataCriteria);

            for(R_GAS_SOURCE_CRITERIA del : dataGasCriteria) {
                if(receivedIdCri.isEmpty() || !receivedIdCri.contains(del.getGasSourceCriteriaId())) {
                    rGasSourceCriteriaRepo.deleteById(del.getGasSourceCriteriaId());
                }
            }

            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.UPDATE, ConstantAccount.MASTER_GASSOURCE), updateGS), HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateUpdate(Boolean api, UpdateGSDTO request) {
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

            for(GSCriteriaDTO validateReq : request.getCriteria()) {
                var violationsCriteria = validator.validate(validateReq);
                if (!violationsCriteria.isEmpty()) {
                    List<Map<String, Object>> violationHeaderList = new ArrayList<>();
                    List<String> validateHeader = new ArrayList<>();
                    for (var violation : violationsCriteria) {
                        logger.error(violation.getMessage());
                        Map<String, Object> data = new HashMap<>();
                        validateHeader.add(violation.getMessage());
                        data.put(violation.getPropertyPath().toString(), violation.getMessage());
                        violationHeaderList.add(data);
                    }
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, validateHeader.get(0), violationHeaderList);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
            }

            Optional<M_GAS_SOURCE> mGasSource = mGasSourceRepo.findByGasSourceId(request.getGasSourceId());
            if (mGasSource.isEmpty()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        UtilsAccount.messageDataNotFound(ConstantAccount.MASTER_GASSOURCE, request.getGasSourceId()), ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }

            Optional<M_GAS_SOURCE> calorieCode = mGasSourceRepo.findTopByCalorieCodeIgnoreCase(request.getCalorieCode().strip());
            Optional<M_GAS_SOURCE> calorieName = mGasSourceRepo.findTopByNameIgnoreCase(request.getName().strip());
            if (calorieCode.isPresent() && calorieName.isPresent() && !calorieCode.get().getGasSourceId().equals(request.getGasSourceId()) && !calorieName.get().getGasSourceId().equals(request.getGasSourceId())) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Calorie Code and Name already Exist", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
            if (calorieCode.isPresent() && !calorieCode.get().getGasSourceId().equals(request.getGasSourceId())) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Calorie Code already Exist", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
            if (calorieName.isPresent() && !calorieName.get().getGasSourceId().equals(request.getGasSourceId())) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Name already Exist", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }
            if (request.getCriteria() == null) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Gas Source Criteria cannot be null", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }

            return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.MASTER_GASSOURCE);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> createQuality(QualityDetailDTO request) {
        try {

            ResponseEntity<ResponseObject> validate = this.validateCreateQuality(Boolean.FALSE, request);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            SimpleDateFormat formatDate = new SimpleDateFormat(Constant.FORMAT_START_END_DATE);

            Date startDate = UtilsDate.stringToDate(request.getStartDate(), Constant.FORMAT_START_END_DATE);

            // set null endDate gas quality
            Optional<R_GAS_SOURCE_DETAIL> getDate = rGasSourceDetailRepo.findTopByGasSourceIdAndStatusOrderByStartDateDesc(request.getGasSourceId(), FlowStatus.ACTIVE.name());
            if(getDate.isPresent()) {
                R_GAS_SOURCE_DETAIL quality = getDate.get();
                String st = UtilsDate.dateToString(quality.getStartDate(), Constant.FORMAT_START_END_DATE);
                Date startDateExist = UtilsDate.stringToDate(st, Constant.FORMAT_START_END_DATE);
                if(quality.getEndDate()==null && startDate.after(startDateExist)) {
                    quality.setEndDate(UtilsDate.getDateMinusDay(formatDate.parse(request.getStartDate()), 1));
                    quality.setUpdatedBy(UserDetailUtils.getUsername());
                    quality.setUpdatedDate(new Date());
                    rGasSourceDetailRepo.save(quality);
                }
            }

            Optional<M_GAS_SOURCE> gasSource = mGasSourceRepo.findByGasSourceId(request.getGasSourceId());
            M_GAS_SOURCE dataGS = gasSource.get();
            R_GAS_SOURCE_DETAIL rGasSourceDetail = new R_GAS_SOURCE_DETAIL();
            rGasSourceDetail.setGasSourceId(dataGS.getGasSourceId());
            rGasSourceDetail.setDocumentNumber(request.getDocumentNumber());
            rGasSourceDetail.setStartDate(formatDate.parse(request.getStartDate()));
            rGasSourceDetail.setEndDate(request.getEndDate()!=null?formatDate.parse(request.getEndDate()):null);
            rGasSourceDetail.setM3(request.getM3());
            rGasSourceDetail.setBtu(request.getBtu());
            rGasSourceDetail.setSg(request.getSg());
            rGasSourceDetail.setN2(request.getN2());
            rGasSourceDetail.setCo2(request.getCo2());
            rGasSourceDetail.setDescription(request.getDescription());
            rGasSourceDetail.setStatus(FlowStatus.ACTIVE.name());
            rGasSourceDetail.setCreatedBy(UserDetailUtils.getUsername());
            rGasSourceDetail.setCreatedDate(new Date());
            R_GAS_SOURCE_DETAIL newDetail = rGasSourceDetailRepo.save(rGasSourceDetail);

            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                    UtilsAccount.messageSuccess(ConstantAccount.CREATE, ConstantAccount.MASTER_GASSOURCE_QUALITY), newDetail), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateCreateQuality(Boolean api, QualityDetailDTO request) {
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

            Date startDate = UtilsDate.stringToDate(request.getStartDate(), Constant.FORMAT_START_END_DATE);
            Date endDate = UtilsDate.stringToDate(request.getEndDate(), Constant.FORMAT_START_END_DATE);

            List<R_GAS_SOURCE_DETAIL> getAllDetail = rGasSourceDetailRepo.findAllByGasSourceIdAndStatus(request.getGasSourceId(), FlowStatus.ACTIVE.name());
            List<DateRange> existingDate = new ArrayList<>();
            for(R_GAS_SOURCE_DETAIL add : getAllDetail) {
                existingDate.add(new DateRange(add.getStartDate(), add.getEndDate()));
            }
            DateRange newDate = new DateRange(startDate, endDate);
            if(isOverlapping(existingDate, newDate)) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Start date/end date is overlapping with other data!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            } else if (isEndDateRequired(existingDate, newDate)) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "End date not be null, because the date is less than the existing date!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }


//            SimpleDateFormat formatDate = new SimpleDateFormat(Constant.FORMAT_START_END_DATE);
//            if (StringUtils.hasValue(request.getEndDate())) {
//                Date startDate = UtilsDate.stringToDate(request.getStartDate(), Constant.FORMAT_START_END_DATE);
//                Date endDate = UtilsDate.stringToDate(request.getEndDate(), Constant.FORMAT_START_END_DATE);
//                if (startDate.getTime() > endDate.getTime()) {
//                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
//                                "Start date must be less than end date!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
//                }
//            }

            Optional<M_GAS_SOURCE> gasSource = mGasSourceRepo.findByGasSourceId(request.getGasSourceId());
            if (gasSource.isPresent()) {
                R_GAS_SOURCE_DETAIL document = rGasSourceDetailRepo.findByGasSourceIdAndDocumentNumber(request.getGasSourceId(),request.getDocumentNumber());
                if (document != null) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Document number already exist", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }

                // validation m3 not be null if uom btu/scf
                Optional<R_GLOBAL_TYPE_VALUE> optUom = globalTypeValueService.getOptionalGlobalTypeByGlbTypeValId("Gas Source UOM", gasSource.get().getUom());
                if(optUom.isPresent()) {
                    if(optUom.get().getGlbValue().equalsIgnoreCase("M3/MMBTU") && !StringUtils.hasValue(request.getM3())) {
                        return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                                "M3 not be null!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                    }
                }

//                List<R_GAS_SOURCE_DETAIL> getAllDetail = rGasSourceDetailRepo.findAllByGasSourceIdAndStatus(request.getGasSourceId(), FlowStatus.ACTIVE.name());
//                for(R_GAS_SOURCE_DETAIL c : getAllDetail) {
//                    if(isOverlapping(c.getStartDate(), c.getEndDate(), formatDate.parse(request.getStartDate()), request.getEndDate()!=null?formatDate.parse(request.getEndDate()):null)) {
//                        return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "You can't create gas quality, there is active gas quality with intersecting start date & end date", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
//                    }
//                }

//                Optional<R_GAS_SOURCE_DETAIL> getDate = rGasSourceDetailRepo.findTopByGasSourceIdAndStatusOrderByCreatedDateDesc(request.getGasSourceId(), FlowStatus.ACTIVE.name());
//                if(getDate.isPresent()) {
//                    R_GAS_SOURCE_DETAIL dataDate = getDate.get();
//                    if(dataDate.getEndDate()==null && formatDate.parse(request.getStartDate()).getTime() <= dataDate.getStartDate().getTime()) {
//                        return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
//                                "Start date must be greater than last gas quality start date!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
//                    } else if(dataDate.getEndDate()!=null && formatDate.parse(request.getStartDate()).getTime() <= dataDate.getEndDate().getTime()) {
//                        return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
//                                "Start date must be greater than last gas quality end date!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
//                    }
//                }

                return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.MASTER_GASSOURCE_QUALITY);

            } else {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        UtilsAccount.messageDataNotFound(ConstantAccount.MASTER_GASSOURCE_QUALITY, request.getGasSourceId()), ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> updateQuality(QualityDetailDTO request) {
        try {
            SimpleDateFormat formatDate = new SimpleDateFormat(Constant.FORMAT_START_END_DATE);

            ResponseEntity<ResponseObject> validate = this.validateUpdateQuality(Boolean.FALSE, request);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            Optional<R_GAS_SOURCE_DETAIL> rGasSourceDetail = rGasSourceDetailRepo.findByGasSourceDetailId(request.getGasSourceDetailId());
            R_GAS_SOURCE_DETAIL updateQuality = rGasSourceDetail.get();
            updateQuality.setEndDate(request.getEndDate()!=null?formatDate.parse(request.getEndDate()):null);
            updateQuality.setDescription(request.getDescription());
            updateQuality.setUpdatedBy(UserDetailUtils.getUsername());
            updateQuality.setUpdatedDate(new Date());
            rGasSourceDetailRepo.save(updateQuality);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.UPDATE, ConstantAccount.MASTER_GASSOURCE_QUALITY), updateQuality), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateUpdateQuality(Boolean api, QualityDetailDTO request) {
        try {
            if (StringUtils.hasValue(request.getEndDate())) {
                Optional<R_GAS_SOURCE_DETAIL> rGasSourceDetail = rGasSourceDetailRepo.findById(request.getGasSourceDetailId());
//                Date startDate = UtilsDate.stringToDate(request.getStartDate(), Constant.FORMAT_START_END_DATE);
//                Date endDate = UtilsDate.stringToDate(request.getEndDate(), Constant.FORMAT_START_END_DATE);
//                if (rGasSourceDetail.isPresent()) {
//                    if (rGasSourceDetail.get().getStartDate().getTime() > endDate.getTime()) {
//                        return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
//                                    "Start date must be less than end date!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
//                    }
//                }
                if(rGasSourceDetail.isPresent()) {
                    String tempStartDate = UtilsDate.dateToString(rGasSourceDetail.get().getStartDate(), Constant.FORMAT_START_END_DATE);
                    Date startDate = UtilsDate.stringToDate(tempStartDate, Constant.FORMAT_START_END_DATE);
                    Date endDate = UtilsDate.stringToDate(request.getEndDate(), Constant.FORMAT_START_END_DATE);
                    List<R_GAS_SOURCE_DETAIL> getAllDetail = rGasSourceDetailRepo.findAllByGasSourceIdAndStatusAndGasSourceDetailIdNot(rGasSourceDetail.get().getGasSourceId(), FlowStatus.ACTIVE.name(), rGasSourceDetail.get().getGasSourceDetailId());
                    List<DateRange> existingDate = new ArrayList<>();
                    for(R_GAS_SOURCE_DETAIL add : getAllDetail) {
                        existingDate.add(new DateRange(add.getStartDate(), add.getEndDate()));
                    }
                    DateRange newDate = new DateRange(startDate, endDate);
                    if(isOverlapping(existingDate, newDate)) {
                        return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                                "Start date/end date is overlapping with other data!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                    }
                }
            }
            
//            SimpleDateFormat formatDate = new SimpleDateFormat(Constant.FORMAT_START_END_DATE);
            Optional<R_GAS_SOURCE_DETAIL> rGasSourceDetail = rGasSourceDetailRepo.findByGasSourceDetailId(request.getGasSourceDetailId());
            if (!rGasSourceDetail.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        UtilsAccount.messageDataNotFound(ConstantAccount.MASTER_GASSOURCE_QUALITY, request.getGasSourceDetailId()), ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
            }
            R_GAS_SOURCE_DETAIL updateQuality = rGasSourceDetail.get();

            if(updateQuality.getStatus().equalsIgnoreCase(FlowStatus.INACTIVE.name())) {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "You cannot update this inactive gas quality!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
            }

            if(request.getEndDate()==null) {
                Optional<R_GAS_SOURCE_DETAIL> getQuality = rGasSourceDetailRepo.findTopByGasSourceIdAndStatusOrderByStartDateDesc(updateQuality.getGasSourceId(), FlowStatus.ACTIVE.name());
                if(!updateQuality.getGasSourceDetailId().equals(getQuality.get().getGasSourceDetailId())) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "You cannot update end date to be empty!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
            }

//            if(updateQuality.getEndDate()!=null) {
//                List<R_GAS_SOURCE_DETAIL> listAllQuality = rGasSourceDetailRepo.findAllByGasSourceIdOrderByCreatedDateAsc(updateQuality.getGasSourceId());
//                Integer indexGas = listAllQuality.indexOf(updateQuality);
//
//                if(indexGas != 0 && StringUtils.hasValue(request.getEndDate()) && formatDate.parse(request.getEndDate()).getTime() <= listAllQuality.get(indexGas-1).getStartDate().getTime()) {
//                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
//                            "End date must be less than new gas quality start date!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
//                }
//            }
//
//            if(StringUtils.hasValue(request.getEndDate()) && formatDate.parse(request.getEndDate()).getTime() <= updateQuality.getStartDate().getTime()) {
//                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
//                        "Start date must be greater than gas quality start date!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
//            }

            return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.MASTER_GASSOURCE_QUALITY);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> activeInactiveQuality(ActiveInactiveDTO request) {
        try {
            ResponseObject result = null;
            Optional<R_GAS_SOURCE_DETAIL> rGasSourceDetail = rGasSourceDetailRepo.findByGasSourceDetailId(request.getGasSourceDetailId());
            if (rGasSourceDetail.isPresent()) {
                R_GAS_SOURCE_DETAIL data = rGasSourceDetail.get();
                LinkedHashMap<String, Object> oldQuality = new LinkedHashMap<>();
                oldQuality.put("gasSourceDetailId", data.getGasSourceDetailId());
                oldQuality.put("gasSourceId", data.getGasSourceId());
                oldQuality.put("documentNumber", data.getDocumentNumber());
                oldQuality.put("startDate", data.getStartDate());
                oldQuality.put("endDate", data.getEndDate());
                oldQuality.put("status", data.getStatus());
                AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
                ObjectMapper mapper = new ObjectMapper();
                String oldValue = mapper.writeValueAsString(oldQuality);
                auditTrail.setOldValue(oldValue);
                auditTrail.setRemark(request.getRemark());
                auditTrail.setOperation("MODIFY");
                auditTrail.setTableName("R_GAS_SOURCE_DETAIL");
                auditTrail.setDataId(data.getGasSourceId().toString());
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
                if (data.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())) {
                    data.setStatus(FlowStatus.INACTIVE.name());
                } else if (data.getStatus().equalsIgnoreCase(FlowStatus.INACTIVE.name())) {
                    data.setStatus(FlowStatus.ACTIVE.name());
                } else {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Status not found", ResponseUtils.DATA_EMPTY);
                }
                data.setUpdatedBy(UserDetailUtils.getUsername());
                data.setUpdatedDate(new Date());
                R_GAS_SOURCE_DETAIL saveQuality = rGasSourceDetailRepo.save(data);
                LinkedHashMap<String, Object> newQuality = new LinkedHashMap<>();
                newQuality.put("gasSourceDetailId", saveQuality.getGasSourceDetailId());
                newQuality.put("gasSourceId", saveQuality.getGasSourceId());
                newQuality.put("documentNumber", saveQuality.getDocumentNumber());
                newQuality.put("startDate", saveQuality.getStartDate());
                newQuality.put("endDate", saveQuality.getEndDate());
                newQuality.put("status", saveQuality.getStatus());
                String newValue = mapper.writeValueAsString(newQuality);
                auditTrail.setNewValue(newValue);
                auditTrailRepo.save(auditTrail);
                if (saveQuality.getStatus().equalsIgnoreCase(FlowStatus.INACTIVE.name())) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            ResponseUtils.MESSAGE_INACTIVE, ResponseUtils.DATA_EMPTY);
                }
                if (saveQuality.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            ResponseUtils.MESSAGE_ACTIVE, ResponseUtils.DATA_EMPTY);
                }
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Gas source not found", ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> detailQuality(Integer id) {
        try {
            Optional<R_GAS_SOURCE_DETAIL> rGasSourceDetail = rGasSourceDetailRepo.findByGasSourceDetailId(id);
            if (rGasSourceDetail.isPresent()) {
                R_GAS_SOURCE_DETAIL data = rGasSourceDetail.get();
                LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                response.put("gasSourceDetailId", data.getGasSourceDetailId());
                response.put("gasSourceId", data.getGasSourceId());
                response.put("documentNumber", data.getDocumentNumber());
                String formatDate = Constant.FORMAT_DATE;
                response.put("startDate", CommonHelper.convertDateToString(formatDate, data.getStartDate()));
                response.put("endDate", data.getEndDate()!=null?CommonHelper.convertDateToString(formatDate, data.getEndDate()):null);
                response.put("m3", data.getM3());
                response.put("btu", data.getBtu());
                response.put("sg", data.getSg());
                response.put("n2", data.getN2());
                response.put("co2", data.getCo2());
                response.put("description", data.getDescription());
                response.put("status", data.getStatus());
                response.put("createdBy", data.getCreatedBy());
                response.put("updatedBy", data.getUpdatedBy());
                response.put("createdDate", data.getCreatedDate());
                response.put("updatedDate", data.getUpdatedDate());
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        "Success get detail quality", response), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Quality detail not found", ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getPagingQuality(Integer id, MaterialTablePagingRequest pagingData,
                                                           PagedResourcesAssembler<R_GAS_SOURCE_DETAIL> assembler) {
        try {
            Optional<M_GAS_SOURCE> mGasSource = mGasSourceRepo.findByGasSourceId(id);
            if (mGasSource.isPresent()) {
                M_GAS_SOURCE gasSource = mGasSource.get();
                Map<String, Object> filter = new HashMap<>();
                filter.put("gasSourceId", gasSource.getGasSourceId());

                if (isNotBlank(pagingData.getSearchs())) {
                    Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                    for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        pagingData.getSearch().add(key+"~"+value);
                    }
                }

                Specification<R_GAS_SOURCE_DETAIL> specification = pagingData.getSearch().isEmpty()
                        ? rGasSourceDetailRepo.getSpecificationDefault(filter)
                        : rGasSourceDetailRepo.getSpecificationFromFilters(pagingData, filter);

                Page<R_GAS_SOURCE_DETAIL> data = rGasSourceDetailRepo.findAll(specification, PagingUtils.getPaging(pagingData));

                List<LinkedHashMap<String, Object>> allData = data.getContent().stream()
                        .map(d -> {
                            LinkedHashMap<String, Object> detail = new LinkedHashMap<>();
                            detail.put("gasSourceDetailId", d.getGasSourceDetailId());
                            detail.put("gasSourceId", d.getGasSourceId());
                            detail.put("documentNumber", d.getDocumentNumber());
                            String formatDate = Constant.FORMAT_DATE;
                            detail.put("startDate", CommonHelper.convertDateToString(formatDate, d.getStartDate()));
                            detail.put("endDate", d.getEndDate()!=null?CommonHelper.convertDateToString(formatDate, d.getEndDate()):null);
                            detail.put("m3", d.getM3());
                            detail.put("btu", d.getBtu());
                            detail.put("sg", d.getSg());
                            detail.put("n2", d.getN2());
                            detail.put("co2", d.getCo2());
                            detail.put("description", d.getDescription());
                            detail.put("status", d.getStatus());
                            return detail;
                        })
                        .collect(Collectors.toList());

                PagedModel<EntityModel<R_GAS_SOURCE_DETAIL>> pagedData = assembler.toModel(data);
                Map<String, Object> d = new HashMap<>();
                d.put("result", allData);
                d.put("page", pagedData.getMetadata());
                d.put("links", pagedData.getLinks());
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        "Success get view paging", d), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Gas source id not found", ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<ResponseObject> assignGasSource(AssignGSDTO request) {
        try {
            SimpleDateFormat formatDate = new SimpleDateFormat(Constant.FORMAT_START_END_DATE);

            ResponseEntity<ResponseObject> validate = this.validateAssignGasSource(request);
            if(!validate.getBody().getSuccess()) {
                return validate;
            }

            Optional<VW_ACCOUNT_GS> accountHaveGasSourceActive = vwAccountGSRepo.findTopByAccountIdAndStatusOrderByCreatedDateDesc(request.getAccountId(), FlowStatus.ACTIVE.name());
            if(accountHaveGasSourceActive.isPresent()) {
                if(request.getNeedValidation()!=null && !request.getNeedValidation() && accountHaveGasSourceActive.get().getEndDate()==null) {
                    Optional<M_ACCOUNT_GAS_SOURCE> getAccountGasSourceActive = mAccountGasSourceRepo.findById(accountHaveGasSourceActive.get().getId());
                    M_ACCOUNT_GAS_SOURCE accountGasSourceActive = getAccountGasSourceActive.get();
                    accountGasSourceActive.setEndDate(UtilsDate.getDateMinusDay(formatDate.parse(request.getStartDate()), 1));
                    accountGasSourceActive.setUpdatedBy(UserDetailUtils.getUsername());
                    accountGasSourceActive.setUpdatedDate(new Date());
                    mAccountGasSourceRepo.save(accountGasSourceActive);
                }
            }

            M_ACCOUNT_GAS_SOURCE mAccountGasSource = new M_ACCOUNT_GAS_SOURCE();
            mAccountGasSource.setAccountId(request.getAccountId());
            mAccountGasSource.setCalorieType(request.getCalorieType());
            mAccountGasSource.setGasSourceCodeId(request.getGasSourceCodeId());
            mAccountGasSource.setStartDate(formatDate.parse(request.getStartDate()));
            mAccountGasSource.setRemark(removeSpace(request.getRemark()));
            mAccountGasSource.setStatus(FlowStatus.ACTIVE.name());
            mAccountGasSource.setCreatedBy(UserDetailUtils.getUsername());
            mAccountGasSource.setCreatedDate(new Date());
            mAccountGasSourceRepo.save(mAccountGasSource);

            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                    ResponseUtils.MESSAGE_CREATED, mAccountGasSource), HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateAssignGasSource(AssignGSDTO request) {
        SimpleDateFormat formatDate = new SimpleDateFormat(Constant.FORMAT_START_END_DATE);
        var violationsAccount = validator.validate(request);
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

        Optional<M_GAS_SOURCE> mGasSource = mGasSourceRepo.findByGasSourceId(request.getGasSourceCodeId());
        if(!mGasSource.isPresent()){
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    "Data gas source based on gas source id is not found", ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
        }

        try {
            Optional<VW_ACCOUNT_GS> accountHaveGasSourceActive = vwAccountGSRepo.findTopByAccountIdAndStatusOrderByCreatedDateDesc(request.getAccountId(), FlowStatus.ACTIVE.name());

            // CHECK STARTDATE BEFORE EXISTING STARTDATE
            if(accountHaveGasSourceActive.isPresent()) {
                if(formatDate.parse(request.getStartDate()).before(accountHaveGasSourceActive.get().getStartDate()) || request.getStartDate().equals(formatDate.format(accountHaveGasSourceActive.get().getStartDate()))) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "Start date must be greater than last gas source start date!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
                if(request.getNeedValidation()!=null && request.getNeedValidation() && accountHaveGasSourceActive.get().getEndDate()==null){
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Warning! The previous gas source end date will be set to H-1 from new start date", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                "Success validate assign gas source", ResponseUtils.DATA_EMPTY), HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> getCalorieType() {
        try {
            Optional<M_GLOBAL_TYPE> mGlobalType = mGlobalTypeRepo.findByGroupName("Calorie Type");
            List<LinkedHashMap<String, Object>> allResponse = mGlobalType
                    .filter(g -> FlowStatus.ACTIVE.name().equalsIgnoreCase(g.getStatus()))
                    .map(g -> rGlobalTypeValueRepo.findAll().stream()
                            .filter(v -> Objects.equals(v.getGlobalType(), g.getGlbTypeId()) &&
                                    FlowStatus.ACTIVE.name().equalsIgnoreCase(v.getStatus()))
                            .map(v -> {
                                LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                                response.put("calorieId", v.getGlbTypeValId());
                                response.put("calorieName", v.getName());
                                response.put("calorieValue", v.getGlbValue());
                                return response;
                            })
                            .collect(Collectors.toList()))
                    .orElseGet(Collections::emptyList);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get calorie type", allResponse), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings({"java:S1192"})
    public ResponseEntity<ResponseObject> getGasSource() {
        try {
            List<LinkedHashMap<String, Object>> allGasSource = mGasSourceRepo.findAll().stream()
                    .filter(i -> FlowStatus.ACTIVE.name().equalsIgnoreCase(i.getStatus()))
                    .sorted(Comparator.comparing(M_GAS_SOURCE::getGasSourceId))
                    .map(g -> {
                        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
                        data.put("gasSourceId", g.getGasSourceId());
                        data.put("calorieCode", g.getCalorieCode());
                        data.put("name", g.getName());
                        Optional<R_GLOBAL_TYPE_VALUE> globalTypeValue = rGlobalTypeValueRepo.findByGlbTypeValId(g.getUom());
                        data.put("uom", globalTypeValue.isPresent() ? globalTypeValue.get().getName() : "");
                        data.put("description", g.getDescription());
                        List<LinkedHashMap<String, Object>> details = rGasSourceDetailRepo.findAll()
                                .stream()
                                .filter(j -> j.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name()) &&
                                        Objects.equals(j.getGasSourceId(), g.getGasSourceId()))
                                .map(k -> {
                                    LinkedHashMap<String, Object> d = new LinkedHashMap<>();
                                    d.put("gasSourceDetailId", k.getGasSourceDetailId());
                                    d.put("gasSourceId", k.getGasSourceId());
                                    d.put("documentNumber", k.getDocumentNumber());
                                    d.put("startDate", UtilsDate.dateToString(k.getStartDate(),Constant.FORMAT_DATETIME_VIEW));
                                    d.put("endDate", UtilsDate.dateToString(k.getStartDate(),Constant.FORMAT_DATETIME_VIEW));
                                    d.put("m3", k.getM3());
                                    d.put("btu", k.getBtu());
                                    d.put("sg", k.getSg());
                                    d.put("n2", k.getN2());
                                    d.put("co2", k.getCo2());
                                    d.put("description", k.getDescription());
                                    d.put("status", k.getStatus());
                                    return d;
                                })
                                .collect(Collectors.toList());
                        data.put("detail", details);
                        return data;
                    }).collect(Collectors.toList());
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get gas source", allGasSource), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public ResponseEntity<ResponseObject> getGasSourceByCostCenter(Integer costCenterID) {
        try {
            
            List<R_GAS_SOURCE_CRITERIA> rgcList = rGasSourceCriteriaRepo.findByCostCenterIdAndStatus(costCenterID, FlowStatus.ACTIVE.name());
            List<LinkedHashMap<String, Object>> allGasSource = rgcList.stream()
                    .map(g -> {
                        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
                        Optional<M_GAS_SOURCE> mgOptional = mGasSourceRepo.findById(g.getGasSourceId());
                        data.put("gasSourceId", mgOptional.isPresent() ? mgOptional.get().getGasSourceId() : "");
                        data.put("calorieCode", mgOptional.isPresent() ? mgOptional.get().getCalorieCode() : "");
                        data.put("name", mgOptional.isPresent() ? mgOptional.get().getName() : "");
                        Optional<R_GLOBAL_TYPE_VALUE> globalTypeValue = rGlobalTypeValueRepo.findByGlbTypeValId(mgOptional.isPresent() ? mgOptional.get().getUom() : null);
                        data.put("uom", globalTypeValue.isPresent() ? globalTypeValue.get().getName() : "");
                        data.put("description", g.getDescription());
                        List<LinkedHashMap<String, Object>> details = rGasSourceDetailRepo.findAllByGasSourceIdAndStatusOrderByCreatedDateDesc(g.getGasSourceId(), FlowStatus.ACTIVE.name())
                                .stream()
                                .map(k -> {
                                    LinkedHashMap<String, Object> d = new LinkedHashMap<>();
                                    d.put("gasSourceDetailId", k.getGasSourceDetailId());
                                    d.put("gasSourceId", k.getGasSourceId());
                                    d.put("documentNumber", k.getDocumentNumber());
                                    d.put("startDate", UtilsDate.dateToString(k.getStartDate(),"dd MMM YYYY"));
                                    d.put("endDate", UtilsDate.dateToString(k.getEndDate(),"dd MMM YYYY"));
                                    d.put("m3", k.getM3());
                                    d.put("btu", k.getBtu());
                                    d.put("sg", k.getSg());
                                    d.put("n2", k.getN2());
                                    d.put("co2", k.getCo2());
                                    d.put("description", k.getDescription());
                                    d.put("status", k.getStatus());
                                    return d;
                                })
                                .collect(Collectors.toList());
                        data.put("detail", details);
                        return data;
                    }).collect(Collectors.toList());
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get gas source", allGasSource), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> inactiveAccountGasSource(ActiveInactiveDTO request) {
        try {
            
            ResponseObject result;
            
            Optional<M_ACCOUNT_GAS_SOURCE> mAccountGasSource = mAccountGasSourceRepo.findById(request.getId());
            if (mAccountGasSource.isPresent()) {
                M_ACCOUNT_GAS_SOURCE data = mAccountGasSource.get();

//                if(data.getEndDate()!=null && request.getEndDate()!=null) {
//                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND, "This gas source will be end on " + UtilsDate.dateToString(data.getEndDate(), Constant.FORMAT_START_END_DATE), ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
//                }

//                Optional<List<VW_SA>> cekIsThereExist = vwSaRepo.findAllByAccountIdAndServiceTypeAndStatusAndIsMain(data.getAccountId(), "Gas", FlowStatus.ACTIVE.name(), "Y");
//                if(cekIsThereExist.isPresent()) {
//                    for(VW_SA cekk : cekIsThereExist.get()) {
//                        if(cekk.getStartDate().getTime() <= new Date().getTime() && cekk.getEndDate().getTime() >= new Date().getTime()) {
//                            result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
//                                    "You cannot inactivate gas source because this account has active main gas service agreement, account should have at least one active gas source", ResponseUtils.DATA_EMPTY);
//                            return new ResponseEntity<>(result, result.getHttpCode());
//                        }
//                    }
//                }

                if(StringUtils.hasValue(request.getEndDate()) && !StringUtils.hasValue(data.getEndDate())) {
                    String formatDate = Constant.FORMAT_START_END_DATE;
                    data.setEndDate(CommonHelper.convertStringToDate(formatDate, request.getEndDate()));
                } else if(!StringUtils.hasValue(data.getEndDate()) && !StringUtils.hasValue(request.getEndDate())) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "End date not be null!", ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
                }
                
                LinkedHashMap<String, Object> oldAccountGS = new LinkedHashMap<>();
                oldAccountGS.put("id", data.getId());
                oldAccountGS.put("accountId", data.getAccountId());
                oldAccountGS.put("calorieType", data.getCalorieType());
                oldAccountGS.put("gasSourceCodeId", data.getGasSourceCodeId());
                oldAccountGS.put("startDate", data.getStartDate());
                oldAccountGS.put("endDate", data.getEndDate());
                oldAccountGS.put("status", data.getStatus());
                AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
                ObjectMapper mapper = new ObjectMapper();
                String oldValue = mapper.writeValueAsString(oldAccountGS);
                auditTrail.setOldValue(oldValue);
                auditTrail.setRemark(removeSpace(request.getRemark()));
                auditTrail.setOperation("MODIFY");
                auditTrail.setTableName("M_ACCOUNT_GAS_SOURCE");
                auditTrail.setDataId(data.getId().toString());
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
                data.setStatus(FlowStatus.INACTIVE.name());
                data.setUpdatedBy(UserDetailUtils.getUsername());
                data.setUpdatedDate(new Date());
                M_ACCOUNT_GAS_SOURCE saveAccount = mAccountGasSourceRepo.save(data);
                LinkedHashMap<String, Object> newAccountGS = new LinkedHashMap<>();
                newAccountGS.put("id", saveAccount.getId());
                newAccountGS.put("accountId", saveAccount.getAccountId());
                newAccountGS.put("calorieType", saveAccount.getCalorieType());
                newAccountGS.put("gasSourceCodeId", saveAccount.getGasSourceCodeId());
                newAccountGS.put("startDate", saveAccount.getStartDate());
                newAccountGS.put("endDate", saveAccount.getEndDate());
                newAccountGS.put("status", saveAccount.getStatus());
                String newValue = mapper.writeValueAsString(newAccountGS);
                auditTrail.setNewValue(newValue);
                auditTrailRepo.save(auditTrail);
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        ResponseUtils.MESSAGE_INACTIVE, ResponseUtils.DATA_EMPTY), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Account gas source not found", ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getPagingAssign(MaterialTablePagingRequest pagingData,
                                                          PagedResourcesAssembler<VW_ACCOUNT_GS> assembler,
                                                          Integer accountId) {
        try {
            Page<VW_ACCOUNT_GS> data;
            Map<String, Object> filter = new HashMap<>();
            filter.put("accountId",accountId);
            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key+"~"+value);
                }
            }
            if (!pagingData.getSearch().isEmpty()) {
                data = this.vwAccountGSRepo.findAll(this.vwAccountGSRepo.getSpecificationFromFilters(pagingData, filter),
                        PagingUtils.getPaging(pagingData));
            } else {
                data = this.vwAccountGSRepo.findAll(this.vwAccountGSRepo.getSpecificationDefault(filter),
                        PagingUtils.getPaging(pagingData));
            }
            PagedModel<EntityModel<VW_ACCOUNT_GS>> pagedData = assembler.toModel(data);
            Map<String, Object> d = new HashMap<>();
            d.put("result", data.getContent());
            d.put("page", pagedData.getMetadata());
            d.put("links", pagedData.getLinks());
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get view paging", d), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> detailAssign(Integer id) {
        try {
            Optional<VW_ACCOUNT_GS> mAccountGasSource = vwAccountGSRepo.findById(id);
            if (mAccountGasSource.isPresent()) {
                VW_ACCOUNT_GS data = mAccountGasSource.get();
                LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                response.put("id", data.getId());
                response.put("accountId", data.getAccountId());
                response.put("calorieTypeId", data.getCalorieType());
                Optional<R_GLOBAL_TYPE_VALUE> calorie = rGlobalTypeValueRepo.findByGlbTypeValId(data.getCalorieTypeId());
                response.put("calorieType", calorie.isPresent() ? calorie.get().getName() : "");
                response.put("gasSourceCodeId", data.getGasSourceCodeId());
                Optional<M_GAS_SOURCE> mGasSource = mGasSourceRepo.findByGasSourceId(data.getGasSourceCodeId());
                if (mGasSource.isPresent()) {
                    M_GAS_SOURCE gs = mGasSource.get();
                    response.put("gasSourceId", gs.getGasSourceId());
                    response.put("calorieCode", gs.getCalorieCode());
                    response.put("name", gs.getName());
                    response.put("description", gs.getDescription());
                    Optional<R_GLOBAL_TYPE_VALUE> uom = rGlobalTypeValueRepo.findByGlbTypeValId(mGasSource.get().getUom());
                    response.put("uomId", gs.getUom());
                    response.put("uom", uom.isPresent() ? uom.get().getName() : "");
                    response.put("entityId", gs.getEntityId());
                    List<R_GAS_SOURCE_DETAIL> detail = rGasSourceDetailRepo.findAll().stream()
                            .filter(e -> FlowStatus.ACTIVE.name().equalsIgnoreCase(e.getStatus())
                                    && Objects.equals(e.getGasSourceId(), gs.getGasSourceId())).collect(Collectors.toList());
                    List<LinkedHashMap<String, Object>> allDetail = new ArrayList<>();
                    detail.forEach(d -> {
                        LinkedHashMap<String, Object> dd = new LinkedHashMap<>();
                        dd.put("gasSourceDetailId", d.getGasSourceDetailId());
                        dd.put("gasSourceId", d.getGasSourceId());
                        dd.put("documentNumber", d.getDocumentNumber());
                        String formatDate = Constant.FORMAT_DATE;
                        dd.put("startDate", CommonHelper.convertDateToString(formatDate, d.getStartDate()));
                        dd.put("endDate",  d.getEndDate()!=null?CommonHelper.convertDateToString(formatDate, d.getEndDate()):null);
                        dd.put("m3", d.getM3());
                        dd.put("btu", d.getBtu());
                        dd.put("sg", d.getSg());
                        dd.put("n2", d.getN2());
                        dd.put("co2", d.getCo2());
                        dd.put("description", d.getDescription());
                        dd.put("status", d.getStatus());
                        allDetail.add(dd);
                    });
                    response.put("detail", allDetail);
                } else {
                    response.put("gasSourceId", null);
                    response.put("calorieCode", null);
                    response.put("name", null);
                    response.put("description", null);
                    response.put("uomId", null);
                    response.put("uom", null);
                    response.put("entityId", null);
                    response.put("detail", null);
                }
                String formatDate = Constant.FORMAT_DATE;
                response.put("startDate", CommonHelper.convertDateToString(formatDate, data.getStartDate()));
                if (data.getEndDate() != null) {
                    response.put("endDate", CommonHelper.convertDateToString(formatDate, data.getEndDate()));
                } else {
                    response.put("endDate", "");
                }
                response.put("remark", data.getRemark());
                response.put("recordId", data.getId());
                response.put("createdBy", data.getCreatedBy());
                response.put("updatedBy", data.getUpdatedBy());
                response.put("status", data.getStatus());
                response.put("createdDate", data.getCreatedDate());
                response.put("updatedDate", data.getUpdatedDate());
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        "Success get detail assign gas source", response), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Assign gas source not found", ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    public static boolean isOverlapping(Date start1, Date end1, Date start2, Date end2) {
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat(Constant.FORMAT_START_END_DATE);
//            String startt1 = sdf.format(start1);
//            String endd1 = end1!=null?sdf.format(end1):null;
//            String startt2 = sdf.format(start2);
//            String endd2 = end2!=null?sdf.format(end2):null;
//            Boolean value;
//            if(end1==null) {
//                value = (start1.before(end2) || startt1.equals(endd2));
//            } else if(end2==null) {
//                value = (end1.after(start2) || startt2.equals(endd1));
//            } else {
//                value = (start1.before(end2) || startt1.equals(endd2)) && (end1.after(start2) || startt2.equals(endd1));
//            }
//            return value;
//
//        } catch (Exception e) {
//            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
//            return false;
//        }
//    }

    static class DateRange {
        Date startDate;
        Date endDate;

        public DateRange(Date startDate, Date endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public boolean overlaps(DateRange other) {
            Date currentStart = this.startDate != null ? this.startDate : this.endDate;
            Date currentEnd = this.endDate != null ? this.endDate : this.startDate;

            Date otherStart = other.startDate != null ? other.startDate : other.endDate;
            Date otherEnd = other.endDate != null ? other.endDate : other.startDate;

            return !currentEnd.before(otherStart) && !currentStart.after(otherEnd);
        }

        public boolean isStartDateBeforeAny(Date newStartDate) {
            return this.startDate != null && newStartDate.before(this.startDate);
        }
    }

    public static boolean isOverlapping(List<DateRange> existingRanges, DateRange newRange) {
        for (DateRange range : existingRanges) {
            if (range.overlaps(newRange)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEndDateRequired(List<DateRange> existingRanges, DateRange newRange) {
        for (DateRange range : existingRanges) {
            if (newRange.startDate != null && range.isStartDateBeforeAny(newRange.startDate)) {
                if (newRange.endDate == null) {
                    return true;
                }
            }
        }
        return false;
    }



}
