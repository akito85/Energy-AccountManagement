package com.dbs.module.account.detail.servicerequest.service;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.library.utils.FlowStatus;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.database.crm.entities.accountmanagement.servicerequest.NX_M_DATA_REQUIREMENT;
import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import com.dbs.database.crm.repositories.accountmanagement.servicerequest.NxMDataRequirementRepo;
import com.dbs.database.crm.repositories.usermanagement.AuditTrailRepo;
import com.dbs.module.account.detail.servicerequest.dto.DataRequirementCreateDTO;
import com.dbs.module.account.detail.servicerequest.dto.DataRequirementUpdateDTO;
import com.dbs.module.account.detail.servicerequest.dto.DataRequirementViewDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;
import java.util.*;

/**
 * Service for managing Data Requirements
 */
@Service
public class DataRequirementService {

    private static final Logger logger = LoggerFactory.getLogger(DataRequirementService.class);

    @Autowired
    private NxMDataRequirementRepo dataRequirementRepo;

    @Autowired
    private AuditTrailRepo auditTrailRepo;

    @Autowired
    private GlobalTypeValueService globalTypeService;

    @Autowired
    private Validator validator;

    /**
     * Create a new data requirement
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> create(DataRequirementCreateDTO request) {
        ResponseObject result;
        try {
            // Validate
            ResponseEntity<ResponseObject> validate = validateCreate(request);
            if (validate != null) {
                return validate;
            }

            NX_M_DATA_REQUIREMENT dataRequirement = new NX_M_DATA_REQUIREMENT();
            dataRequirement.setRequirementName(request.getRequirementName());
            dataRequirement.setRequirementType(request.getRequirementType());
            dataRequirement.setDataType(request.getDataType());
            dataRequirement.setIsMandatory(request.getIsMandatory());
            dataRequirement.setValidationRule(request.getValidationRule());
            dataRequirement.setDefaultValue(request.getDefaultValue());
            dataRequirement.setHelpText(request.getHelpText());

            dataRequirement.setCreatedBy(UserDetailUtils.getUsername());
            dataRequirement.setStatus(FlowStatus.ACTIVE.name());
            dataRequirement.setCreatedDate(new Date());

            NX_M_DATA_REQUIREMENT saved = dataRequirementRepo.save(dataRequirement);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                ResponseUtils.MESSAGE_CREATED, saved);
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                new ResponseObject(ResponseUtils.SUCCESS_FALSE,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update data requirement
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> update(DataRequirementUpdateDTO request) {
        ResponseObject result;
        try {
            Optional<NX_M_DATA_REQUIREMENT> data =
                dataRequirementRepo.findByDataRequirementId(request.getDataRequirementId());

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    "Data requirement not found", null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            NX_M_DATA_REQUIREMENT dataRequirement = data.get();

            // Create audit trail
            AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
            ObjectMapper mapper = new ObjectMapper();
            String oldValue = mapper.writeValueAsString(dataRequirement);
            auditTrail.setOldValue(oldValue);

            // Update fields
            if (request.getRequirementName() != null) {
                dataRequirement.setRequirementName(request.getRequirementName());
            }
            if (request.getDataType() != null) {
                dataRequirement.setDataType(request.getDataType());
            }
            if (request.getIsMandatory() != null) {
                dataRequirement.setIsMandatory(request.getIsMandatory());
            }
            if (request.getValidationRule() != null) {
                dataRequirement.setValidationRule(request.getValidationRule());
            }
            if (request.getDefaultValue() != null) {
                dataRequirement.setDefaultValue(request.getDefaultValue());
            }
            if (request.getHelpText() != null) {
                dataRequirement.setHelpText(request.getHelpText());
            }

            dataRequirement.setUpdatedBy(UserDetailUtils.getUsername());
            dataRequirement.setUpdatedDate(new Date());

            NX_M_DATA_REQUIREMENT saved = dataRequirementRepo.save(dataRequirement);

            // Save audit trail
            String newValue = mapper.writeValueAsString(saved);
            auditTrail.setNewValue(newValue);
            auditTrail.setOperation("UPDATE_DATA_REQUIREMENT");
            auditTrail.setTableName("NX_M_DATA_REQUIREMENT");
            auditTrail.setDataId(saved.getDataRequirementId().toString());
            auditTrail.setCreatedBy(UserDetailUtils.getUsername());
            auditTrail.setCreatedDate(new Date());
            auditTrailRepo.save(auditTrail);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                ResponseUtils.MESSAGE_UPDATED, saved);
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                new ResponseObject(ResponseUtils.SUCCESS_FALSE,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get data requirement detail
     */
    public ResponseEntity<ResponseObject> getDetail(Integer dataRequirementId) {
        ResponseObject result;
        try {
            Optional<NX_M_DATA_REQUIREMENT> data =
                dataRequirementRepo.findByDataRequirementId(dataRequirementId);

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            DataRequirementViewDTO viewDTO = buildViewDTO(data.get());

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                "Success", viewDTO);
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                new ResponseObject(ResponseUtils.SUCCESS_FALSE,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all active data requirements
     */
    public ResponseEntity<ResponseObject> getAllActive() {
        ResponseObject result;
        try {
            List<NX_M_DATA_REQUIREMENT> requirements =
                dataRequirementRepo.findAllByStatus(FlowStatus.ACTIVE.name());

            List<DataRequirementViewDTO> viewDTOs = new ArrayList<>();
            for (var requirement : requirements) {
                viewDTOs.add(buildViewDTO(requirement));
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                "Success", viewDTOs);
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                new ResponseObject(ResponseUtils.SUCCESS_FALSE,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get mandatory data requirements
     */
    public ResponseEntity<ResponseObject> getMandatory() {
        ResponseObject result;
        try {
            List<NX_M_DATA_REQUIREMENT> requirements =
                dataRequirementRepo.findAllByIsMandatoryAndStatus(true, FlowStatus.ACTIVE.name());

            List<DataRequirementViewDTO> viewDTOs = new ArrayList<>();
            for (var requirement : requirements) {
                viewDTOs.add(buildViewDTO(requirement));
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                "Success", viewDTOs);
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                new ResponseObject(ResponseUtils.SUCCESS_FALSE,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get data requirements by type
     */
    public ResponseEntity<ResponseObject> getByType(Integer requirementType) {
        ResponseObject result;
        try {
            List<NX_M_DATA_REQUIREMENT> requirements =
                dataRequirementRepo.findAllByRequirementTypeAndStatus(
                    requirementType, FlowStatus.ACTIVE.name());

            List<DataRequirementViewDTO> viewDTOs = new ArrayList<>();
            for (var requirement : requirements) {
                viewDTOs.add(buildViewDTO(requirement));
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                "Success", viewDTOs);
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                new ResponseObject(ResponseUtils.SUCCESS_FALSE,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Toggle active/inactive status
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> toggleStatus(Integer dataRequirementId) {
        ResponseObject result;
        try {
            Optional<NX_M_DATA_REQUIREMENT> data =
                dataRequirementRepo.findByDataRequirementId(dataRequirementId);

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            NX_M_DATA_REQUIREMENT dataRequirement = data.get();

            // Toggle status
            if (dataRequirement.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())) {
                dataRequirement.setStatus(FlowStatus.INACTIVE.name());
            } else {
                dataRequirement.setStatus(FlowStatus.ACTIVE.name());
            }

            dataRequirement.setUpdatedDate(new Date());
            dataRequirement.setUpdatedBy(UserDetailUtils.getUsername());

            NX_M_DATA_REQUIREMENT saved = dataRequirementRepo.save(dataRequirement);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                ResponseUtils.MESSAGE_UPDATED, saved);
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                new ResponseObject(ResponseUtils.SUCCESS_FALSE,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ==================== Private Helper Methods ====================

    /**
     * Validate create request
     */
    private ResponseEntity<ResponseObject> validateCreate(DataRequirementCreateDTO request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            List<Map<String, Object>> violationList = new ArrayList<>();
            for (var violation : violations) {
                Map<String, Object> data = new HashMap<>();
                data.put(violation.getPropertyPath().toString(), violation.getMessage());
                violationList.add(data);
            }
            ResponseObject result = new ResponseObject(
                ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                "Validation failed", violationList);
            return new ResponseEntity<>(result, result.getHttpCode());
        }
        return null;
    }

    /**
     * Build view DTO from entity
     */
    private DataRequirementViewDTO buildViewDTO(NX_M_DATA_REQUIREMENT dataRequirement) {
        DataRequirementViewDTO dto = new DataRequirementViewDTO();
        dto.setDataRequirementId(dataRequirement.getDataRequirementId());
        dto.setRequirementName(dataRequirement.getRequirementName());
        dto.setRequirementType(dataRequirement.getRequirementType());
        dto.setDataType(dataRequirement.getDataType());
        dto.setIsMandatory(dataRequirement.getIsMandatory());
        dto.setValidationRule(dataRequirement.getValidationRule());
        dto.setDefaultValue(dataRequirement.getDefaultValue());
        dto.setHelpText(dataRequirement.getHelpText());

        // Get GLOBAL_TYPE name for requirement type
        if (dataRequirement.getRequirementType() != null) {
            var type = globalTypeService.getGlobalTypeByGlbTypeValId(
                "Data Requirement Type", dataRequirement.getRequirementType());
            if (type != null) {
                dto.setRequirementTypeName(type.getName());
            }
        }

        return dto;
    }
}
