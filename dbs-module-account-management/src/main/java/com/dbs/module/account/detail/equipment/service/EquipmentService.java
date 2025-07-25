package com.dbs.module.account.detail.equipment.service;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.StringUtils;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.database.crm.entities.accountmanagement.M_AM_EQUIPMENT;
import com.dbs.database.crm.entities.accountmanagement.view.VW_AM_EQUIPMENT;
import com.dbs.database.crm.repositories.accountmanagement.Account.EquipmentRepo;
import com.dbs.database.crm.repositories.accountmanagement.Account.view.ViewEquipmentRepo;
import com.dbs.module.account.detail.equipment.dto.EquimentCreateUpdateDTO;
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

import javax.validation.Validator;
import java.util.*;

@Service
public class EquipmentService {

    private static final Logger logger = LoggerFactory.getLogger(EquipmentService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    @Autowired
    private EquipmentRepo equipmentRepo;

    @Autowired
    private GlobalTypeValueService globalTypeValueService;

    @Autowired
    private ViewEquipmentRepo viewEquipmentRepo;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> createUpdateEquipment(EquimentCreateUpdateDTO request) {
        ResponseObject result;
        try {

            ResponseEntity<ResponseObject> validate = this.validateCreateUpdateEquipment(Boolean.FALSE, request);
            if(StringUtils.hasValue(validate)) {
                return validate;
            }

            M_AM_EQUIPMENT equipment = new M_AM_EQUIPMENT();
            if(request.getId()!=null) {
                Optional<M_AM_EQUIPMENT> findEquipment = equipmentRepo.findById(request.getId());
                equipment = findEquipment.get();
                equipment.setUpdatedBy(UserDetailUtils.getUsername());
                equipment.setUpdatedDate(new Date());
            } else {
                equipment.setCreatedBy(UserDetailUtils.getUsername());
                equipment.setCreatedDate(new Date());
            }
            equipment.setAccountId(request.getAccountId());
            equipment.setName(request.getName());
            equipment.setTypeEquipment(request.getTypeEquipment());
            equipment.setBrand(request.getBrand());
            equipment.setQty(request.getQty());
            equipment.setQtyUom(request.getQtyUom());
            equipment.setCap(request.getCap());
            equipment.setCapUom(request.getCapUom());
            equipment.setCon(request.getCon());
            equipment.setConUom(request.getConUom());
            equipment.setNoh(request.getNoh());
            equipment.setNod(request.getNod());
            equipment.setGasConv(request.getGasConv());
            equipment.setGasConvUom(request.getGasConvUom());
            equipment.setIsDualFuel(request.getIsDualFuel());
            equipment.setFuelType1(request.getFuelType1());
            equipment.setFuelType2(request.getFuelType2());
            equipment.setDescription(request.getDescription());
            equipment.setIsDeleted(Boolean.FALSE);
            equipmentRepo.save(equipment);

            result  = new ResponseObject(
                    ResponseUtils.SUCCESS_TRUE,
                    HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.CREATE_UPDATE, ConstantAccount.EQUIPMENT),
                    equipment
            );
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(),e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());

        }
    }

    public ResponseEntity<ResponseObject> validateCreateUpdateEquipment(Boolean api, EquimentCreateUpdateDTO request) {
        ResponseObject result;
        try {
            //REQUEST EQUIPMENT
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

            // IF UPDATE
            if(request.getId()!=null) {
                Optional<M_AM_EQUIPMENT> findEquipment = equipmentRepo.findById(request.getId());
                if(findEquipment.isEmpty()) {
                    result = new ResponseObject(
                            ResponseUtils.SUCCESS_FALSE,
                            HttpStatus.NOT_FOUND,
                            UtilsAccount.messageDataNotFound(ConstantAccount.EQUIPMENT, request.getId()),
                            ResponseUtils.DATA_EMPTY
                    );
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
            }

            // IF DUAL TYPE TRUE
            if(request.getIsDualFuel()) {
                if(request.getFuelType2()==null) {
                    result = new ResponseObject(
                            ResponseUtils.SUCCESS_FALSE,
                            HttpStatus.BAD_REQUEST,
                            "If dual fuel is active, then fuel type 2 cannot be null",
                            ResponseUtils.DATA_EMPTY
                    );
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                if(request.getFuelType1().equals(request.getFuelType2())) {
                    result = new ResponseObject(
                            ResponseUtils.SUCCESS_FALSE,
                            HttpStatus.BAD_REQUEST,
                            "Fuel type 1 and fuel type 2 cannot be same",
                            ResponseUtils.DATA_EMPTY
                    );
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
            }

            return UtilsAccount.returnForValidateOrApi(api, ConstantAccount.EQUIPMENT);

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(),e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());

        }
    }

    public ResponseEntity<ResponseObject> viewPagingEquipment(Integer accountId, MaterialTablePagingRequest pagingData, PagedResourcesAssembler<VW_AM_EQUIPMENT> assembler) {
        ResponseObject result;
        try {
            Page<VW_AM_EQUIPMENT> data;
           Map<String, Object> filter = new HashMap<>();
           filter.put("isDeleted", Boolean.FALSE);
           filter.put("accountId", accountId);

           if(StringUtils.hasValue(pagingData.getSearchs())) {
               Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
               for(Map.Entry<String, Object> entry : searchMap.entrySet()) {
                   String key = entry.getKey();
                   Object value = entry.getValue();
                   pagingData.getSearch().add(key+"~"+value);
               }
           }

           if(!pagingData.getSearch().isEmpty()) {
               data = viewEquipmentRepo.findAll(
                       viewEquipmentRepo.getSpecificationFromFilters(pagingData, filter),
                       PagingUtils.getPaging(pagingData)
               );
           } else {
               data = viewEquipmentRepo.findAll(
                       viewEquipmentRepo.getSpecificationDefault(filter),
                       PagingUtils.getPaging(pagingData)
               );
           }

           PagedModel<EntityModel<VW_AM_EQUIPMENT>> pagedData = assembler.toModel(data);
           Map<String, Object> dataPaging = new HashMap<>();
           dataPaging.put(Constant.RESULT, pagedData.getContent());
           dataPaging.put(Constant.PAGE, pagedData.getMetadata());
           dataPaging.put(Constant.LINK, pagedData.getLinks());

           result = new ResponseObject(
                   ResponseUtils.SUCCESS_TRUE,
                   HttpStatus.OK,
                   UtilsAccount.messageSuccess(ConstantAccount.PAGING, ConstantAccount.EQUIPMENT),
                   dataPaging
           );
           return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(),e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }

    public ResponseEntity<ResponseObject> viewDetailEquipment(Integer id) {
        ResponseObject result;
        try {

            Optional<VW_AM_EQUIPMENT> findEquipment = viewEquipmentRepo.findById(id);
            if(findEquipment.isEmpty()) {
                result = new ResponseObject(
                        ResponseUtils.SUCCESS_FALSE,
                        HttpStatus.NOT_FOUND,
                        UtilsAccount.messageDataNotFound(ConstantAccount.EQUIPMENT, id),
                        ResponseUtils.DATA_EMPTY
                );
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            VW_AM_EQUIPMENT equipment = findEquipment.get();
            result = new ResponseObject(
                    ResponseUtils.SUCCESS_TRUE,
                    HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.DETAIL, ConstantAccount.EQUIPMENT),
                    equipment
            );
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(),e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }

    public ResponseEntity<ResponseObject> softDeleteEquipment(Integer id) {
        ResponseObject result;
        try {

            Optional<M_AM_EQUIPMENT> findEquipment = equipmentRepo.findById(id);
            if(findEquipment.isEmpty()) {
                result = new ResponseObject(
                        ResponseUtils.SUCCESS_FALSE,
                        HttpStatus.NOT_FOUND,
                        UtilsAccount.messageDataNotFound(ConstantAccount.EQUIPMENT, id),
                        ResponseUtils.DATA_EMPTY
                );
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            M_AM_EQUIPMENT deleteEquipment = findEquipment.get();
            deleteEquipment.setIsDeleted(Boolean.TRUE);
            deleteEquipment.setUpdatedBy(UserDetailUtils.getUsername());
            deleteEquipment.setUpdatedDate(new Date());
            equipmentRepo.save(deleteEquipment);

            result = new ResponseObject(
                    ResponseUtils.SUCCESS_TRUE,
                    HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.SOFT_DELETE, ConstantAccount.EQUIPMENT),
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

    public ResponseEntity<ResponseObject> getDropDownListEquipment(String groupName) {
        try {
            List<LinkedHashMap<String, Object>> allData = globalTypeValueService.getGlobalTypeOther(groupName, null);
            ResponseObject result = new ResponseObject(
                    ResponseUtils.SUCCESS_TRUE,
                    HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.DDL, groupName),
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
