package com.dbs.module.account.detail.sourcedistribution.service;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.FlowStatus;
import com.dbs.common.library.utils.StringUtils;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.common.library.utils.UtilsDate;
import com.dbs.database.crm.entities.accountmanagement.M_AM_SRC_DIST;
import com.dbs.database.crm.entities.accountmanagement.R_AM_SRC_DIST_DTL;
import com.dbs.database.crm.entities.accountmanagement.view.VW_AM_SRC_DIST;
import com.dbs.database.crm.entities.product.M_LOCATION;
import com.dbs.database.crm.repositories.accountmanagement.Account.SourceDistributionDetailRepo;
import com.dbs.database.crm.repositories.accountmanagement.Account.SourceDistributionRepo;
import com.dbs.database.crm.repositories.accountmanagement.Account.view.ViewSourceDistributionRepo;
import com.dbs.database.crm.repositories.mastermanagement.MLocationsRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import com.dbs.module.account.detail.sourcedistribution.dto.SourceDistributionCreateUpdateDTO;
import com.dbs.module.account.detail.sourcedistribution.dto.SourceDistributionDetailCreateUpdateDTO;
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
public class SourceDistributionService {
    private static final Logger logger = LoggerFactory.getLogger(SourceDistributionService.class);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    @Autowired
    private SourceDistributionRepo sourceDistributionRepo;

    @Autowired
    private SourceDistributionDetailRepo sourceDistributionDetailRepo;

    @Autowired
    private GlobalTypeValueService globalTypeValueService;

    @Autowired
    private MLocationsRepo mlocationRepo;

    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;

    @Autowired
    private ViewSourceDistributionRepo viewSourceDistributionRepo;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> createUpdateSourceDistribution (SourceDistributionCreateUpdateDTO request) {
        ResponseObject result;
        try {

            ResponseEntity<ResponseObject> validate = this.validateCreateUpdateSourceDistribution(Boolean.FALSE, request);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            // SOURCE DISTRIBUTION
            M_AM_SRC_DIST sourceDistribution = new M_AM_SRC_DIST();
            if(request.getId()!=null) {
                Optional<M_AM_SRC_DIST> findSourceDistribution = sourceDistributionRepo.findById(request.getId());
                sourceDistribution = findSourceDistribution.get();
                sourceDistribution.setUpdatedBy(UserDetailUtils.getUsername());
                sourceDistribution.setUpdatedDate(new Date());
            } else {
                sourceDistribution.setCreatedBy(UserDetailUtils.getUsername());
                sourceDistribution.setCreatedDate(new Date());
            }
            sourceDistribution.setTypeDist(request.getTypeDist());
            sourceDistribution.setAccountId(request.getAccountId());
            sourceDistribution.setEffectiveDate(UtilsDate.stringToDate(request.getEffectiveDate(), Constant.FORMAT_START_END_DATE));
            sourceDistribution.setValue1(request.getValue1());
            sourceDistribution.setValue2(request.getValue2());
            sourceDistribution.setDescription(request.getDescription());
            sourceDistribution.setIsDeleted(Boolean.FALSE);
            sourceDistributionRepo.save(sourceDistribution);

            List<R_AM_SRC_DIST_DTL> getAllDetail = sourceDistributionDetailRepo.findAllBySrcDistId(sourceDistribution.getId());
            List<Integer> receivedIdDetail = new ArrayList<>();

            // SOURCE DISTRIBUTION DETAIL
            for(SourceDistributionDetailCreateUpdateDTO getDetail : request.getSrcDistDtl()) {
                R_AM_SRC_DIST_DTL sourceDistributionDetail = new R_AM_SRC_DIST_DTL();
                if(getDetail.getId()!=null) {
                    Optional <R_AM_SRC_DIST_DTL> findSourceDistributionDetail = sourceDistributionDetailRepo.findById(getDetail.getId());
                    sourceDistributionDetail = findSourceDistributionDetail.get();
                    sourceDistributionDetail.setUpdatedBy(UserDetailUtils.getUsername());
                    sourceDistributionDetail.setUpdatedDate(new Date());
                    receivedIdDetail.add(sourceDistributionDetail.getId());
                } else {
                    sourceDistributionDetail.setCreatedBy(UserDetailUtils.getUsername());
                    sourceDistributionDetail.setCreatedDate(new Date());
                }
                sourceDistributionDetail.setSrcDistId(sourceDistribution.getId());
                sourceDistributionDetail.setCountry(getDetail.getCountry());
                sourceDistributionDetail.setPercentage(getDetail.getPercentage());
                sourceDistributionDetailRepo.save(sourceDistributionDetail);
            }
            for(R_AM_SRC_DIST_DTL delete : getAllDetail) {
                if(receivedIdDetail.isEmpty() || !receivedIdDetail.contains(delete.getId())) {
                    sourceDistributionDetailRepo.deleteById(delete.getId());
                }
            }

            result = new ResponseObject(
                    ResponseUtils.SUCCESS_TRUE,
                    HttpStatus.CREATED,
                    UtilsAccount.messageSuccess(ConstantAccount.CREATE_UPDATE, ConstantAccount.SOURCE_DISTRIBUTION),
                    sourceDistribution);
            return new ResponseEntity<>(result, result.getHttpCode());


        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(),e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());

        }
    }

    public ResponseEntity<ResponseObject> validateCreateUpdateSourceDistribution(Boolean api, SourceDistributionCreateUpdateDTO request) {
        ResponseObject result;
        try {
            // REQUEST SOURCE DISTRIBUTION
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

            // REQUEST SOURCE DISTRIBUTION DETAIL
            for(SourceDistributionDetailCreateUpdateDTO dtoDetail : request.getSrcDistDtl()) {
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

            // TYPE DISTRIBUTION MUST BE RAW_MATERIAL OR PRODUCT_DISTRIBUTION
            if(!request.getTypeDist().equals(ConstantAccount.RAW_MATERIAL) && !request.getTypeDist().equals(ConstantAccount.PRODUCT_DISTRIBUTION)) {
                result = new ResponseObject(
                        ResponseUtils.SUCCESS_FALSE,
                        HttpStatus.BAD_REQUEST,
                        "Type distribution must be RAW_MATERIAL or PRODUCT_DISTRIBUTION",
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            // UPDATE
            if(request.getId()!=null) {
                Optional<M_AM_SRC_DIST> findSourceDistribution = sourceDistributionRepo.findByIdAndTypeDist(request.getId(), request.getTypeDist());
                if(findSourceDistribution.isEmpty()) {
                    result = new ResponseObject(
                            ResponseUtils.SUCCESS_FALSE,
                            HttpStatus.NOT_FOUND,
                            UtilsAccount.messageDataNotFound(ConstantAccount.SOURCE_DISTRIBUTION, request.getId()),
                            ResponseUtils.DATA_EMPTY
                    );
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                for(SourceDistributionDetailCreateUpdateDTO getDetail : request.getSrcDistDtl()) {
                    if(getDetail.getId()!=null) {
                        Optional <R_AM_SRC_DIST_DTL> findSourceDistributionDetail = sourceDistributionDetailRepo.findById(getDetail.getId());
                        if(findSourceDistributionDetail.isEmpty()) {
                            result = new ResponseObject(
                                    ResponseUtils.SUCCESS_FALSE,
                                    HttpStatus.NOT_FOUND,
                                    UtilsAccount.messageDataNotFound(ConstantAccount.SOURCE_DISTRIBUTION_DETAIL, getDetail.getId()),
                                    ResponseUtils.DATA_EMPTY
                            );
                            return new ResponseEntity<>(result, result.getHttpCode());
                        }
                    }
                }

                // UNIQUE EFFECTIVE DATA UPDATE
                if(sourceDistributionRepo.existsByAccountIdAndTypeDistAndIdNotAndEffectiveDateAndIsDeletedIsFalse(request.getAccountId(), request.getTypeDist(), request.getId(), UtilsDate.stringToDate(request.getEffectiveDate(), Constant.FORMAT_START_END_DATE))) {
                    result = new ResponseObject(
                            ResponseUtils.SUCCESS_FALSE,
                            HttpStatus.BAD_REQUEST,
                            UtilsAccount.messageAlreadyExist(ConstantAccount.EFFECTIVE_DATE),
                            ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
            } else {
                // UNIQUE EFFECTIVE DATE CREATE
                if(sourceDistributionRepo.existsByAccountIdAndTypeDistAndEffectiveDateAndIsDeletedIsFalse(request.getAccountId(), request.getTypeDist(),UtilsDate.stringToDate(request.getEffectiveDate(), Constant.FORMAT_START_END_DATE))) {
                    result = new ResponseObject(
                            ResponseUtils.SUCCESS_FALSE,
                            HttpStatus.BAD_REQUEST,
                            UtilsAccount.messageAlreadyExist(ConstantAccount.EFFECTIVE_DATE),
                            ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
            }

            // UNIQUE COUNTRY
           Map<Integer, Long> countryCount = request.getSrcDistDtl().stream()
                   .collect(Collectors.groupingBy(SourceDistributionDetailCreateUpdateDTO::getCountry, Collectors.counting()));
            if(countryCount.values().stream().anyMatch(count -> count > 1)) {
                result = new ResponseObject(
                        ResponseUtils.SUCCESS_FALSE,
                        HttpStatus.BAD_REQUEST,
                        UtilsAccount.messageAlreadyExist("Country"),
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            // 100% PERCENTAGE TOTAL
            if(request.getSrcDistDtl().stream()
                    .mapToDouble(SourceDistributionDetailCreateUpdateDTO::getPercentage)
                    .sum() != 100) {
                result = new ResponseObject(
                        ResponseUtils.SUCCESS_FALSE,
                        HttpStatus.BAD_REQUEST,
                        "Total percentage must be 100%",
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.SOURCE_DISTRIBUTION);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(),e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }

    public ResponseEntity<ResponseObject> viewPagingSourceDistribution(String typeDist, Integer accountId, MaterialTablePagingRequest pagingData, PagedResourcesAssembler<VW_AM_SRC_DIST> assembler) {
        ResponseObject result;
        try {

            Page<VW_AM_SRC_DIST> data;
            Map<String, Object> filter = new HashMap<>();
            filter.put("isDeleted", Boolean.FALSE);
            filter.put("typeDist", typeDist);
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
                data = viewSourceDistributionRepo.findAll(
                        viewSourceDistributionRepo.getSpecificationFromFilters(pagingData, filter),
                        PagingUtils.getPaging(pagingData)
                );
            } else {
                data = viewSourceDistributionRepo.findAll(
                        viewSourceDistributionRepo.getSpecificationDefault(filter),
                        PagingUtils.getPaging(pagingData)
                );
            }
            PagedModel<EntityModel<VW_AM_SRC_DIST>> pagedData = assembler.toModel(data);
            Map<String, Object> dataPaging = new HashMap<>();
            dataPaging.put(Constant.RESULT, pagedData.getContent());
            dataPaging.put(Constant.PAGE, pagedData.getMetadata());
            dataPaging.put(Constant.LINK, pagedData.getLinks());
            result = new ResponseObject(
                    ResponseUtils.SUCCESS_TRUE,
                    HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.PAGING, ConstantAccount.SOURCE_DISTRIBUTION),
                    dataPaging);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(),e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());

        }
    }

    public ResponseEntity<ResponseObject> viewDetailSourceDistribution(String typeDist, @Nullable Integer id, @Nullable Integer accountId) {
        ResponseObject result;
        try {
            VW_AM_SRC_DIST sourceDistribution;
           if(id!=null) {
               Optional<VW_AM_SRC_DIST> findSourceDistribution = viewSourceDistributionRepo.findByIdAndTypeDist(id, typeDist);
               if (findSourceDistribution.isEmpty()) {
                   result = new ResponseObject(
                           ResponseUtils.SUCCESS_FALSE,
                           HttpStatus.NOT_FOUND,
                           UtilsAccount.messageDataNotFound(ConstantAccount.SOURCE_DISTRIBUTION, id),
                           ResponseUtils.DATA_EMPTY
                   );
                   return new ResponseEntity<>(result, result.getHttpCode());
               }
               sourceDistribution = findSourceDistribution.get();
           } else {
               Optional<VW_AM_SRC_DIST> findSourceDistribution = viewSourceDistributionRepo.findTopByAccountIdAndTypeDistAndIsDeletedIsFalseOrderByEffectiveDateDesc(accountId, typeDist);
               if(findSourceDistribution.isEmpty()) {
                   result = new ResponseObject(
                           ResponseUtils.SUCCESS_TRUE,
                           HttpStatus.OK,
                           "No data source distribution active in this account!",
                           ResponseUtils.DATA_EMPTY
                   );
                   return new ResponseEntity<>(result, result.getHttpCode());
               }
               sourceDistribution = findSourceDistribution.get();
           }

            result = new ResponseObject(
                    ResponseUtils.SUCCESS_TRUE,
                    HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.DETAIL, ConstantAccount.SOURCE_DISTRIBUTION),
                    sourceDistribution
            );
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(),e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }

    public ResponseEntity<ResponseObject> softDeletedSourceDistribution(String typeDist, Integer id) {
        ResponseObject result;
        try {
            Optional<M_AM_SRC_DIST> findSourceDistribution = sourceDistributionRepo.findByIdAndTypeDist(id, typeDist);
            if (findSourceDistribution.isEmpty()) {
                result = new ResponseObject(
                        ResponseUtils.SUCCESS_FALSE,
                        HttpStatus.NOT_FOUND,
                        UtilsAccount.messageDataNotFound(ConstantAccount.SOURCE_DISTRIBUTION, id),
                        ResponseUtils.DATA_EMPTY
                );
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            M_AM_SRC_DIST deleteSourceDistribution = findSourceDistribution.get();
            deleteSourceDistribution.setIsDeleted(Boolean.TRUE);
            deleteSourceDistribution.setUpdatedDate(new Date());
            deleteSourceDistribution.setUpdatedBy(UserDetailUtils.getUsername());
            sourceDistributionRepo.save(deleteSourceDistribution);
            result = new ResponseObject(
                    ResponseUtils.SUCCESS_TRUE,
                    HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.SOFT_DELETE, ConstantAccount.SOURCE_DISTRIBUTION),
                    ResponseUtils.DATA_EMPTY
            );
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }

    public ResponseEntity<ResponseObject> getDropDownListCountryDistribution() {
        try {
            List<M_LOCATION> dataCountry = mlocationRepo.findAllByLocationTypeIdAndStatus(103, FlowStatus.ACTIVE.name());

            List<LinkedHashMap<String, Object>> allData = dataCountry
                    .stream()
                    .map(v -> {
                        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                        response.put("value", v.getLocationId());
                        response.put("label", v.getLocationName());
                        return response;
                    })
                    .collect(Collectors.toList());

            ResponseObject result = new ResponseObject(
                    ResponseUtils.SUCCESS_TRUE,
                    HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.DDL, "Country Name"),
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
