package com.dbs.module.account.detail.servicerequest.service;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.FlowStatus;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.database.crm.entities.accountmanagement.servicerequest.NX_M_PRE_REQUISITE;
import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import com.dbs.database.crm.repositories.accountmanagement.servicerequest.NxMPreRequisiteRepo;
import com.dbs.database.crm.repositories.accountmanagement.servicerequest.NxRSrDetailsRepo;
import com.dbs.database.crm.repositories.usermanagement.AuditTrailRepo;
import com.dbs.module.account.detail.servicerequest.dto.PreRequisiteCreateDTO;
import com.dbs.module.account.detail.servicerequest.dto.PreRequisiteUpdateDTO;
import com.dbs.module.account.detail.servicerequest.dto.PreRequisiteViewDTO;
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
 * Service for managing Prerequisites
 */
@Service
public class PreRequisiteService {

    private static final Logger logger = LoggerFactory.getLogger(PreRequisiteService.class);

    @Autowired
    private NxMPreRequisiteRepo preRequisiteRepo;

    @Autowired
    private NxRSrDetailsRepo srDetailsRepo;

    @Autowired
    private AuditTrailRepo auditTrailRepo;

    @Autowired
    private GlobalTypeValueService globalTypeService;

    @Autowired
    private Validator validator;

    /**
     * Create a new prerequisite
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> create(PreRequisiteCreateDTO request) {
        ResponseObject result;
        try {
            // Validate
            ResponseEntity<ResponseObject> validate = validateCreate(request);
            if (validate != null) {
                return validate;
            }

            NX_M_PRE_REQUISITE preRequisite = new NX_M_PRE_REQUISITE();
            preRequisite.setPrerequisiteType(request.getPrerequisiteType());
            preRequisite.setPrerequisiteName(request.getPrerequisiteName());
            preRequisite.setDescription(request.getDescription());
            preRequisite.setIsMandatory(request.getIsMandatory());
            preRequisite.setSequenceOrder(request.getSequenceOrder());

            preRequisite.setCreatedBy(UserDetailUtils.getUsername());
            preRequisite.setStatus(FlowStatus.ACTIVE.name());
            preRequisite.setCreatedDate(new Date());

            NX_M_PRE_REQUISITE saved = preRequisiteRepo.save(preRequisite);

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
     * Update prerequisite
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> update(PreRequisiteUpdateDTO request) {
        ResponseObject result;
        try {
            Optional<NX_M_PRE_REQUISITE> data =
                preRequisiteRepo.findByPrerequisiteId(request.getPrerequisiteId());

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    "Prerequisite not found", null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            NX_M_PRE_REQUISITE preRequisite = data.get();

            // Create audit trail
            AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
            ObjectMapper mapper = new ObjectMapper();
            String oldValue = mapper.writeValueAsString(preRequisite);
            auditTrail.setOldValue(oldValue);

            // Update fields
            if (request.getPrerequisiteName() != null) {
                preRequisite.setPrerequisiteName(request.getPrerequisiteName());
            }
            if (request.getDescription() != null) {
                preRequisite.setDescription(request.getDescription());
            }
            if (request.getIsMandatory() != null) {
                preRequisite.setIsMandatory(request.getIsMandatory());
            }
            if (request.getSequenceOrder() != null) {
                preRequisite.setSequenceOrder(request.getSequenceOrder());
            }

            preRequisite.setUpdatedBy(UserDetailUtils.getUsername());
            preRequisite.setUpdatedDate(new Date());

            NX_M_PRE_REQUISITE saved = preRequisiteRepo.save(preRequisite);

            // Save audit trail
            String newValue = mapper.writeValueAsString(saved);
            auditTrail.setNewValue(newValue);
            auditTrail.setOperation("UPDATE_PREREQUISITE");
            auditTrail.setTableName("NX_M_PRE_REQUISITE");
            auditTrail.setDataId(saved.getPrerequisiteId().toString());
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
     * Get prerequisite detail
     */
    public ResponseEntity<ResponseObject> getDetail(Integer prerequisiteId) {
        ResponseObject result;
        try {
            Optional<NX_M_PRE_REQUISITE> data =
                preRequisiteRepo.findByPrerequisiteId(prerequisiteId);

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            PreRequisiteViewDTO viewDTO = buildViewDTO(data.get());

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
     * Get all active prerequisites
     */
    public ResponseEntity<ResponseObject> getAllActive() {
        ResponseObject result;
        try {
            List<NX_M_PRE_REQUISITE> prerequisites =
                preRequisiteRepo.findAllByStatusOrderBySequenceOrderAsc(FlowStatus.ACTIVE.name());

            List<PreRequisiteViewDTO> viewDTOs = new ArrayList<>();
            for (var prerequisite : prerequisites) {
                viewDTOs.add(buildViewDTO(prerequisite));
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
     * Get mandatory prerequisites
     */
    public ResponseEntity<ResponseObject> getMandatory() {
        ResponseObject result;
        try {
            List<NX_M_PRE_REQUISITE> prerequisites =
                preRequisiteRepo.findAllByIsMandatoryAndStatus(true, FlowStatus.ACTIVE.name());

            List<PreRequisiteViewDTO> viewDTOs = new ArrayList<>();
            for (var prerequisite : prerequisites) {
                viewDTOs.add(buildViewDTO(prerequisite));
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
     * Get prerequisites by type
     */
    public ResponseEntity<ResponseObject> getByType(Integer prerequisiteType) {
        ResponseObject result;
        try {
            List<NX_M_PRE_REQUISITE> prerequisites =
                preRequisiteRepo.findAllByPrerequisiteTypeAndStatus(
                    prerequisiteType, FlowStatus.ACTIVE.name());

            List<PreRequisiteViewDTO> viewDTOs = new ArrayList<>();
            for (var prerequisite : prerequisites) {
                viewDTOs.add(buildViewDTO(prerequisite));
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
    public ResponseEntity<ResponseObject> toggleStatus(Integer prerequisiteId) {
        ResponseObject result;
        try {
            Optional<NX_M_PRE_REQUISITE> data =
                preRequisiteRepo.findByPrerequisiteId(prerequisiteId);

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            NX_M_PRE_REQUISITE preRequisite = data.get();

            // Toggle status
            if (preRequisite.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())) {
                preRequisite.setStatus(FlowStatus.INACTIVE.name());
            } else {
                preRequisite.setStatus(FlowStatus.ACTIVE.name());
            }

            preRequisite.setUpdatedDate(new Date());
            preRequisite.setUpdatedBy(UserDetailUtils.getUsername());

            NX_M_PRE_REQUISITE saved = preRequisiteRepo.save(preRequisite);

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
    private ResponseEntity<ResponseObject> validateCreate(PreRequisiteCreateDTO request) {
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
    private PreRequisiteViewDTO buildViewDTO(NX_M_PRE_REQUISITE preRequisite) {
        PreRequisiteViewDTO dto = new PreRequisiteViewDTO();
        dto.setPrerequisiteId(preRequisite.getPrerequisiteId());
        dto.setPrerequisiteType(preRequisite.getPrerequisiteType());
        dto.setPrerequisiteName(preRequisite.getPrerequisiteName());
        dto.setDescription(preRequisite.getDescription());
        dto.setIsMandatory(preRequisite.getIsMandatory());
        dto.setSequenceOrder(preRequisite.getSequenceOrder());

        // Get GLOBAL_TYPE name for prerequisite type
        if (preRequisite.getPrerequisiteType() != null) {
            var type = globalTypeService.getGlobalTypeByGlbTypeValId(
                "Prerequisite Type", preRequisite.getPrerequisiteType());
            if (type != null) {
                dto.setPrerequisiteTypeName(type.getName());
            }
        }

        return dto;
    }
}
