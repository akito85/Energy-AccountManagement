package com.dbs.module.account.detail.servicerequest.service;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.FlowStatus;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.database.crm.entities.accountmanagement.servicerequest.NX_M_SERVICE_REQUEST;
import com.dbs.database.crm.entities.accountmanagement.servicerequest.NX_R_SR_DETAILS;
import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import com.dbs.database.crm.repositories.accountmanagement.servicerequest.NxMServiceRequestRepo;
import com.dbs.database.crm.repositories.accountmanagement.servicerequest.NxRSrDetailsRepo;
import com.dbs.database.crm.repositories.usermanagement.AuditTrailRepo;
import com.dbs.module.account.detail.servicerequest.dto.ServiceRequestCreateDTO;
import com.dbs.module.account.detail.servicerequest.dto.ServiceRequestUpdateDTO;
import com.dbs.module.account.detail.servicerequest.dto.ServiceRequestViewDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;
import java.util.*;

/**
 * Service for managing Service Requests
 */
@Service
public class ServiceRequestService {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRequestService.class);

    @Autowired
    private NxMServiceRequestRepo serviceRequestRepo;

    @Autowired
    private NxRSrDetailsRepo srDetailsRepo;

    @Autowired
    private AuditTrailRepo auditTrailRepo;

    @Autowired
    private GlobalTypeValueService globalTypeService;

    @Autowired
    private Validator validator;

    /**
     * Create a new service request
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> create(ServiceRequestCreateDTO request) {
        ResponseObject result;
        try {
            // Validate
            ResponseEntity<ResponseObject> validate = validateCreate(request);
            if (validate != null) {
                return validate;
            }

            // Check duplicate request number
            if (serviceRequestRepo.existsByRequestNumber(request.getRequestNumber())) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                    "Request number already exists", null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            // Create service request
            NX_M_SERVICE_REQUEST sr = new NX_M_SERVICE_REQUEST();
            sr.setAccountId(request.getAccountId());
            sr.setRequestNumber(request.getRequestNumber());
            sr.setRequestType(request.getRequestType());
            sr.setRequestCategory(request.getRequestCategory());
            sr.setPriority(request.getPriority());
            sr.setSubject(request.getSubject());
            sr.setDescription(request.getDescription());
            sr.setRequestedDate(request.getRequestedDate() != null ?
                request.getRequestedDate() : new Date());
            sr.setDueDate(request.getDueDate());
            sr.setAssignedTo(request.getAssignedTo());

            // Set initial status from GLOBAL_TYPE
            sr.setRequestStatus(getInitialStatus());

            // Audit fields
            sr.setCreatedBy(UserDetailUtils.getUsername());
            sr.setStatus(FlowStatus.ACTIVE.name());
            sr.setCreatedDate(new Date());

            NX_M_SERVICE_REQUEST savedSR = serviceRequestRepo.save(sr);

            // Create relationship entry in NX_R_SR_DETAILS
            createSRDetails(savedSR.getServiceRequestId(), request.getAccountId(),
                null, null, null, null, null, null, null, null, true);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                ResponseUtils.MESSAGE_CREATED, savedSR);
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
     * Update service request
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> update(ServiceRequestUpdateDTO request) {
        ResponseObject result;
        try {
            Optional<NX_M_SERVICE_REQUEST> data =
                serviceRequestRepo.findByServiceRequestId(request.getServiceRequestId());

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    "Service request not found", null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            NX_M_SERVICE_REQUEST sr = data.get();

            // Create audit trail
            AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
            ObjectMapper mapper = new ObjectMapper();
            String oldValue = mapper.writeValueAsString(sr);
            auditTrail.setOldValue(oldValue);

            // Update fields
            if (request.getSubject() != null) {
                sr.setSubject(request.getSubject());
            }
            if (request.getDescription() != null) {
                sr.setDescription(request.getDescription());
            }
            if (request.getDueDate() != null) {
                sr.setDueDate(request.getDueDate());
            }
            if (request.getCompletionDate() != null) {
                sr.setCompletionDate(request.getCompletionDate());
            }
            if (request.getAssignedTo() != null) {
                sr.setAssignedTo(request.getAssignedTo());
            }
            if (request.getRequestStatus() != null) {
                sr.setRequestStatus(request.getRequestStatus());
            }
            if (request.getPriority() != null) {
                sr.setPriority(request.getPriority());
            }

            sr.setUpdatedBy(UserDetailUtils.getUsername());
            sr.setUpdatedDate(new Date());

            NX_M_SERVICE_REQUEST savedSR = serviceRequestRepo.save(sr);

            // Save audit trail
            String newValue = mapper.writeValueAsString(savedSR);
            auditTrail.setNewValue(newValue);
            auditTrail.setOperation("UPDATE_SERVICE_REQUEST");
            auditTrail.setTableName("NX_M_SERVICE_REQUEST");
            auditTrail.setDataId(savedSR.getServiceRequestId().toString());
            auditTrail.setCreatedBy(UserDetailUtils.getUsername());
            auditTrail.setCreatedDate(new Date());
            auditTrailRepo.save(auditTrail);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                ResponseUtils.MESSAGE_UPDATED, savedSR);
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
     * Get service request detail
     */
    public ResponseEntity<ResponseObject> getDetail(Integer serviceRequestId) {
        ResponseObject result;
        try {
            Optional<NX_M_SERVICE_REQUEST> data =
                serviceRequestRepo.findByServiceRequestId(serviceRequestId);

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            // Build view DTO with related data
            ServiceRequestViewDTO viewDTO = buildViewDTO(data.get());

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
     * Get paginated list of service requests for an account
     */
    public ResponseEntity<?> getListByAccount(Integer accountId,
                                               MaterialTablePagingRequest pagingdata,
                                               PagedResourcesAssembler assembler) {
        try {
            Map<String, Object> filter = new HashMap<>();
            filter.put("accountId", accountId);

            Pageable paging = PagingUtils.getPaging(pagingdata);
            Page<NX_M_SERVICE_REQUEST> page = serviceRequestRepo.findAll(
                serviceRequestRepo.getSpecificationFromFilters(pagingdata, filter), paging);

            PagedModel<?> pr = assembler.toModel(page);
            return new ResponseEntity<>(pr, HttpStatus.OK);

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
     * Activate/Deactivate service request
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> toggleStatus(Integer serviceRequestId) {
        ResponseObject result;
        try {
            Optional<NX_M_SERVICE_REQUEST> data =
                serviceRequestRepo.findByServiceRequestId(serviceRequestId);

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            NX_M_SERVICE_REQUEST sr = data.get();

            // Toggle status
            if (sr.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())) {
                sr.setStatus(FlowStatus.INACTIVE.name());
            } else {
                sr.setStatus(FlowStatus.ACTIVE.name());
            }

            sr.setUpdatedDate(new Date());
            sr.setUpdatedBy(UserDetailUtils.getUsername());

            NX_M_SERVICE_REQUEST savedSR = serviceRequestRepo.save(sr);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                ResponseUtils.MESSAGE_UPDATED, savedSR);
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
     * Get service request types from GLOBAL_TYPE
     */
    public ResponseEntity<ResponseObject> getRequestTypes() {
        ResponseObject result;
        try {
            var types = globalTypeService.getListGlobalTypeValue("Service Request Type");
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                "Success", types);
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
    private ResponseEntity<ResponseObject> validateCreate(ServiceRequestCreateDTO request) {
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
     * Create SR Details relationship entry
     */
    private void createSRDetails(Integer serviceRequestId, Integer accountId,
                                  Integer prerequisiteId, Integer dataRequirementId,
                                  Integer workOrderId, Integer installmentId,
                                  Integer paymentPlanId, Integer billingItemId,
                                  Integer scheduleId, Integer attachmentId,
                                  Boolean isPrimary) {
        NX_R_SR_DETAILS srDetails = new NX_R_SR_DETAILS();
        srDetails.setServiceRequestId(serviceRequestId);
        srDetails.setAccountId(accountId);
        srDetails.setPrerequisiteId(prerequisiteId);
        srDetails.setDataRequirementId(dataRequirementId);
        srDetails.setWorkOrderId(workOrderId);
        srDetails.setInstallmentId(installmentId);
        srDetails.setPaymentPlanId(paymentPlanId);
        srDetails.setBillingItemId(billingItemId);
        srDetails.setScheduleId(scheduleId);
        srDetails.setAttachmentId(attachmentId);
        srDetails.setIsPrimary(isPrimary != null ? isPrimary : false);
        srDetails.setCreatedBy(UserDetailUtils.getUsername());
        srDetails.setStatus(FlowStatus.ACTIVE.name());
        srDetails.setCreatedDate(new Date());

        srDetailsRepo.save(srDetails);
    }

    /**
     * Build view DTO from entity
     */
    private ServiceRequestViewDTO buildViewDTO(NX_M_SERVICE_REQUEST sr) {
        ServiceRequestViewDTO dto = new ServiceRequestViewDTO();
        dto.setServiceRequestId(sr.getServiceRequestId());
        dto.setAccountId(sr.getAccountId());
        dto.setRequestNumber(sr.getRequestNumber());
        dto.setRequestType(sr.getRequestType());
        dto.setRequestCategory(sr.getRequestCategory());
        dto.setRequestStatus(sr.getRequestStatus());
        dto.setPriority(sr.getPriority());
        dto.setSubject(sr.getSubject());
        dto.setDescription(sr.getDescription());
        dto.setRequestedDate(sr.getRequestedDate());
        dto.setDueDate(sr.getDueDate());
        dto.setCompletionDate(sr.getCompletionDate());
        dto.setAssignedTo(sr.getAssignedTo());
        dto.setStatus(sr.getStatus());
        dto.setCreatedBy(sr.getCreatedBy());
        dto.setCreatedDate(sr.getCreatedDate());
        dto.setUpdatedBy(sr.getUpdatedBy());
        dto.setUpdatedDate(sr.getUpdatedDate());

        // Get GLOBAL_TYPE names
        if (sr.getRequestType() != null) {
            var requestType = globalTypeService.getGlobalTypeByGlbTypeValId(
                "Service Request Type", sr.getRequestType());
            if (requestType != null) {
                dto.setRequestTypeName(requestType.getName());
            }
        }

        if (sr.getRequestCategory() != null) {
            var requestCategory = globalTypeService.getGlobalTypeByGlbTypeValId(
                "Service Request Category", sr.getRequestCategory());
            if (requestCategory != null) {
                dto.setRequestCategoryName(requestCategory.getName());
            }
        }

        if (sr.getRequestStatus() != null) {
            var requestStatus = globalTypeService.getGlobalTypeByGlbTypeValId(
                "Service Request Status", sr.getRequestStatus());
            if (requestStatus != null) {
                dto.setRequestStatusName(requestStatus.getName());
            }
        }

        if (sr.getPriority() != null) {
            var priority = globalTypeService.getGlobalTypeByGlbTypeValId(
                "Service Request Priority", sr.getPriority());
            if (priority != null) {
                dto.setPriorityName(priority.getName());
            }
        }

        return dto;
    }

    /**
     * Get initial status ID from GLOBAL_TYPE
     */
    private Integer getInitialStatus() {
        var status = globalTypeService.getGlobalTypeByGlbValue(
            "Service Request Status", "NEW");
        return status != null ? status.getGlbTypeValId() : null;
    }
}
