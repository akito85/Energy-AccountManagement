package com.dbs.module.account.detail.gasutilization.service;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.StringUtils;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.common.library.utils.UtilsDate;
import com.dbs.database.crm.entities.accountmanagement.M_AM_GAS_UTILS;
import com.dbs.database.crm.entities.accountmanagement.R_AM_GAS_UTILS_DTL;
import com.dbs.database.crm.entities.accountmanagement.view.VW_AM_GAS_UTILS;
import com.dbs.database.crm.repositories.accountmanagement.Account.GasUtilsDetailRepo;
import com.dbs.database.crm.repositories.accountmanagement.Account.GasUtilsRepo;
import com.dbs.database.crm.repositories.accountmanagement.Account.view.ViewGasUtilsRepo;
import com.dbs.module.account.detail.gasutilization.dto.GasUtilizationCreateUpdateDTO;
import com.dbs.module.account.detail.gasutilization.dto.GasUtilizationDetailCreateUpdateDTO;
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

import javax.annotation.Nullable;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GasUtilizationService {

    private static final Logger logger = LoggerFactory.getLogger(GasUtilizationService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    @Autowired
    private GasUtilsRepo gasUtilsRepo;

    @Autowired
    private GasUtilsDetailRepo gasUtilsDetailRepo;

    @Autowired
    private GlobalTypeValueService globalTypeValueService;

    @Autowired
    private ViewGasUtilsRepo viewGasUtilsRepo;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> createUpdateGasUtilization(GasUtilizationCreateUpdateDTO request) {
        ResponseObject result;
        try{

            ResponseEntity<ResponseObject> validate = this.validateCreateUpdateGasUtilization(Boolean.FALSE, request);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            // GAS UTILIZATION
            M_AM_GAS_UTILS gasUtils = new M_AM_GAS_UTILS();
            if(request.getId()!=null) {
                Optional<M_AM_GAS_UTILS> findGasUtils = gasUtilsRepo.findById(request.getId());
                gasUtils = findGasUtils.get();
                gasUtils.setUpdatedBy(UserDetailUtils.getUsername());
                gasUtils.setUpdatedDate(new Date());
            } else {
                gasUtils.setCreatedBy(UserDetailUtils.getUsername());
                gasUtils.setCreatedDate(new Date());
            }
            gasUtils.setAccountId(request.getAccountId());
            gasUtils.setEffectiveDate(UtilsDate.stringToDate(request.getEffectiveDate(), Constant.FORMAT_START_END_DATE));
            gasUtils.setDescription(request.getDescription());
            gasUtils.setIsDeleted(Boolean.FALSE);
            gasUtilsRepo.save(gasUtils);

            List<R_AM_GAS_UTILS_DTL> getAllDetail = gasUtilsDetailRepo.findAllByGasUtilsId(gasUtils.getId());
            List<Integer> receivedIdDetail = new ArrayList<>();
            // GAS UTILIZATION DETAIL
            for(GasUtilizationDetailCreateUpdateDTO getDetail : request.getGasUtilsDtl()) {
                R_AM_GAS_UTILS_DTL gasUtilsDetail = new R_AM_GAS_UTILS_DTL();
                if(getDetail.getId()!=null) {
                    Optional<R_AM_GAS_UTILS_DTL> findGasUtilsDetail = gasUtilsDetailRepo.findById(getDetail.getId());
                    gasUtilsDetail = findGasUtilsDetail.get();
                    gasUtilsDetail.setUpdatedBy(UserDetailUtils.getUsername());
                    gasUtilsDetail.setUpdatedDate(new Date());
                    receivedIdDetail.add(gasUtilsDetail.getId());
                } else {
                    gasUtilsDetail.setCreatedBy(UserDetailUtils.getUsername());
                    gasUtilsDetail.setCreatedDate(new Date());
                }
                gasUtilsDetail.setGasUtilsId(gasUtils.getId());
                gasUtilsDetail.setName(getDetail.getName());
                gasUtilsDetail.setPercentage(getDetail.getPercentage());
                gasUtilsDetailRepo.save(gasUtilsDetail);
            }
            for(R_AM_GAS_UTILS_DTL delete : getAllDetail) {
                if(receivedIdDetail.isEmpty() || !receivedIdDetail.contains(delete.getId())) {
                    gasUtilsDetailRepo.deleteById(delete.getId());
                }
            }

            result = new ResponseObject(
                    ResponseUtils.SUCCESS_TRUE,
                    HttpStatus.CREATED,
                    UtilsAccount.messageSuccess(ConstantAccount.CREATE_UPDATE, ConstantAccount.GAS_UTILIZATION),
                    gasUtils);
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(),e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());

        }
    }

    public ResponseEntity<ResponseObject> validateCreateUpdateGasUtilization(Boolean api, GasUtilizationCreateUpdateDTO request) {
        ResponseObject result;
        try{
            //REQUEST GAS UTILIZATION
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
                result = new ResponseObject(
                        ResponseUtils.SUCCESS_FALSE,
                        HttpStatus.BAD_REQUEST,
                        validateHeader.get(0),
                        violationHeaderList
                );
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            //REQUEST GAS UTILIZATION DETAIL
            for(GasUtilizationDetailCreateUpdateDTO dtoDetail : request.getGasUtilsDtl()) {
                var violationsDetail = validator.validate(dtoDetail);
                if (!violationsDetail.isEmpty()) {
                    List<Map<String, Object>> violationHeaderList = new ArrayList<>();
                    List<String> validateHeader = new ArrayList<>();
                    for (var violation : violationsDetail) {
                        logger.error(violation.getMessage());
                        Map<String, Object> data = new HashMap<>();
                        validateHeader.add(violation.getMessage());
                        data.put(violation.getPropertyPath().toString(), violation.getMessage());
                        violationHeaderList.add(data);
                    }
                    result = new ResponseObject(
                            ResponseUtils.SUCCESS_FALSE,
                            HttpStatus.BAD_REQUEST,
                            validateHeader.get(0),
                            violationHeaderList
                    );
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
            }

            // UPDATE
            if (request.getId() != null) {
                // CHECK UPDATE DATA NOT FOUND
                Optional<M_AM_GAS_UTILS> findGasUtils = gasUtilsRepo.findById(request.getId());
                if (findGasUtils.isEmpty()) {
                    result = new ResponseObject(
                            ResponseUtils.SUCCESS_FALSE,
                            HttpStatus.BAD_REQUEST,
                            UtilsAccount.messageDataNotFound(ConstantAccount.GAS_UTILIZATION, request.getId()),
                            ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                for (GasUtilizationDetailCreateUpdateDTO getDetail : request.getGasUtilsDtl()) {
                    if (getDetail.getId() != null) {
                        Optional<R_AM_GAS_UTILS_DTL> findGasUtilsDetail = gasUtilsDetailRepo.findById(getDetail.getId());
                        if (findGasUtilsDetail.isEmpty()) {
                            result = new ResponseObject(
                                    ResponseUtils.SUCCESS_FALSE,
                                    HttpStatus.BAD_REQUEST,
                                    UtilsAccount.messageDataNotFound(ConstantAccount.GAS_UTILIZATION_DETAIL, request.getId()),
                                    ResponseUtils.DATA_EMPTY);
                            return new ResponseEntity<>(result, result.getHttpCode());
                        }
                    }
                }

                // UNIQUE EFFECTIVE DATE UPDATE
                if(gasUtilsRepo.existsByIdNotAndEffectiveDateAndIsDeletedIsFalse(request.getId(), UtilsDate.stringToDate(request.getEffectiveDate(), Constant.FORMAT_START_END_DATE))) {
                    result = new ResponseObject(
                            ResponseUtils.SUCCESS_FALSE,
                            HttpStatus.BAD_REQUEST,
                            UtilsAccount.messageAlreadyExist(ConstantAccount.EFFECTIVE_DATE),
                            ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
            } else {
                // UNIQUE EFFECTIVE DATE CREATE
                if(gasUtilsRepo.existsByEffectiveDateAndIsDeletedIsFalse(UtilsDate.stringToDate(request.getEffectiveDate(), Constant.FORMAT_START_END_DATE))) {
                    result = new ResponseObject(
                            ResponseUtils.SUCCESS_FALSE,
                            HttpStatus.BAD_REQUEST,
                            UtilsAccount.messageAlreadyExist(ConstantAccount.EFFECTIVE_DATE),
                            ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
            }

            // UNIQUE UTILIZATION NAME
            Map<Integer, Long> nameCount = request.getGasUtilsDtl().stream()
                    .collect(Collectors.groupingBy(GasUtilizationDetailCreateUpdateDTO::getName, Collectors.counting()));
            if(nameCount.values().stream().anyMatch(count -> count > 1)) {
                result = new ResponseObject(
                        ResponseUtils.SUCCESS_FALSE,
                        HttpStatus.BAD_REQUEST,
                        UtilsAccount.messageAlreadyExist("Utilization name"),
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            // 100% PERCENTAGE TOTAL
            if(request.getGasUtilsDtl().stream()
                    .mapToDouble(GasUtilizationDetailCreateUpdateDTO::getPercentage)
                    .sum() != 100) {
                result = new ResponseObject(
                        ResponseUtils.SUCCESS_FALSE,
                        HttpStatus.BAD_REQUEST,
                        "Total percentage must be 100%",
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.GAS_UTILIZATION);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(),e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());

        }
    }

    public ResponseEntity<ResponseObject> viewPagingGasUtilization(Integer accountId, MaterialTablePagingRequest pagingData, PagedResourcesAssembler<VW_AM_GAS_UTILS> assembler) {
        ResponseObject result;
        try {
            Page<VW_AM_GAS_UTILS> data;
            Map<String, Object> filter = new HashMap<>();
            filter.put("isDeleted", Boolean.FALSE);
            filter.put("accountId", accountId);

            if (StringUtils.hasValue(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key+"~"+value);
                }
            }

            if (!pagingData.getSearch().isEmpty()) {
                data = this.viewGasUtilsRepo.findAll(
                        this.viewGasUtilsRepo.getSpecificationFromFilters(pagingData, filter),
                        PagingUtils.getPaging(pagingData)
                );
            } else {
                data = this.viewGasUtilsRepo.findAll(
                        this.viewGasUtilsRepo.getSpecificationDefault(filter),
                        PagingUtils.getPaging(pagingData)
                );
            }
            PagedModel<EntityModel<VW_AM_GAS_UTILS>> pagedData = assembler.toModel(data);
            Map<String, Object> dataPaging = new HashMap<>();
            dataPaging.put(Constant.RESULT, pagedData.getContent());
            dataPaging.put(Constant.PAGE, pagedData.getMetadata());
            dataPaging.put(Constant.LINK, pagedData.getLinks());
            result = new ResponseObject(
                    ResponseUtils.SUCCESS_TRUE,
                    HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.PAGING, ConstantAccount.GAS_UTILIZATION),
                    dataPaging);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(),e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }

    public ResponseEntity<ResponseObject> viewDetailGasUtilization(@Nullable Integer accountId, @Nullable Integer id) {
        ResponseObject result;
        try {
            VW_AM_GAS_UTILS gasUtils;
            if(id!=null) {
                Optional<VW_AM_GAS_UTILS> findGasUtils = viewGasUtilsRepo.findById(id);
                if (findGasUtils.isEmpty()) {
                    result = new ResponseObject(
                            ResponseUtils.SUCCESS_FALSE,
                            HttpStatus.NOT_FOUND,
                            UtilsAccount.messageDataNotFound(ConstantAccount.GAS_UTILIZATION, id),
                            ResponseUtils.DATA_EMPTY
                    );
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                gasUtils = findGasUtils.get();
            } else {
                Optional<VW_AM_GAS_UTILS> findCurrentGasUtils = viewGasUtilsRepo.findTopByAccountIdAndIsDeletedIsFalseOrderByEffectiveDateDesc(accountId);
                if(findCurrentGasUtils.isEmpty()) {
                    result = new ResponseObject(
                            ResponseUtils.SUCCESS_TRUE,
                            HttpStatus.OK,
                            "No data gas utils active in this account!",
                            ResponseUtils.DATA_EMPTY
                    );
                    return new ResponseEntity<>(result, result.getHttpCode());
                }

                gasUtils = findCurrentGasUtils.get();
            }

            result = new ResponseObject(
                    ResponseUtils.SUCCESS_TRUE,
                    HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.DETAIL, ConstantAccount.GAS_UTILIZATION),
                    gasUtils
            );

            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(),e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }

    public ResponseEntity<ResponseObject> softDeleteGasUtilization(Integer id) {
        ResponseObject result;
        try {
            Optional<M_AM_GAS_UTILS> findGasUtils = gasUtilsRepo.findById(id);
            if (findGasUtils.isEmpty()) {
                result = new ResponseObject(
                        ResponseUtils.SUCCESS_FALSE,
                        HttpStatus.NOT_FOUND,
                        UtilsAccount.messageDataNotFound(ConstantAccount.GAS_UTILIZATION, id),
                        ResponseUtils.DATA_EMPTY
                );
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            M_AM_GAS_UTILS deleteGasUtils = findGasUtils.get();
            deleteGasUtils.setIsDeleted(Boolean.TRUE);
            deleteGasUtils.setUpdatedDate(new Date());
            deleteGasUtils.setUpdatedBy(UserDetailUtils.getUsername());
            gasUtilsRepo.save(deleteGasUtils);

            result = new ResponseObject(
                    ResponseUtils.SUCCESS_TRUE,
                    HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.SOFT_DELETE, ConstantAccount.GAS_UTILIZATION),
                    ResponseUtils.DATA_EMPTY
            );
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(),e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }

    public ResponseEntity<ResponseObject> getDropDownListUtilizationName() {
        try {
            List<LinkedHashMap<String, Object>> allData = globalTypeValueService.getGlobalTypeOther(ConstantAccount.DDL_UTILIZATION_NAME, null);
            ResponseObject result = new ResponseObject(
                    ResponseUtils.SUCCESS_TRUE,
                    HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.DDL, ConstantAccount.DDL_UTILIZATION_NAME),
                    allData
            );
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(),e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }

}
