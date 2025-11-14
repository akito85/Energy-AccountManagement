package com.dbs.module.account.detail.servicerequest.service;

import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.entities.TApprovalObject;
import com.dbs.common.library.services.ApprovalServices;
import com.dbs.common.library.utils.ApprovalCategory;
import com.dbs.common.library.utils.ApprovalType;
import com.dbs.database.crm.entities.accountmanagement.servicerequest.NX_M_SERVICE_REQUEST;
import com.dbs.database.crm.entities.usermanagement.T_APPROVAL;
import com.dbs.database.crm.repositories.accountmanagement.servicerequest.NxMServiceRequestRepo;
import com.dbs.module.account.detail.servicerequest.dto.ServiceRequestApprovalSubmissionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service for handling Service Request approval workflow
 * Integrates with the existing ApprovalServices for approval flow management
 */
@Service
public class ServiceRequestApprovalService {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRequestApprovalService.class);

    @Autowired
    private NxMServiceRequestRepo serviceRequestRepo;

    @Autowired
    private ApprovalServices approvalServices;

    /**
     * Submit service request for approval
     * Creates approval record in T_APPROVAL and starts the approval workflow
     *
     * @param request Approval submission data
     * @param httpServletRequest HTTP request for user context
     * @return Response with approval workflow ID and status
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> submitForApproval(
            ServiceRequestApprovalSubmissionDTO request,
            HttpServletRequest httpServletRequest) {

        logger.info("Submitting service request {} for approval", request.getServiceRequestId());

        try {
            // Validate service request exists
            Optional<NX_M_SERVICE_REQUEST> srOpt =
                serviceRequestRepo.findByServiceRequestId(request.getServiceRequestId());

            if (!srOpt.isPresent()) {
                logger.warn("Service request not found: {}", request.getServiceRequestId());
                ResponseObject result = new ResponseObject(
                    ResponseUtils.SUCCESS_FALSE,
                    HttpStatus.NOT_FOUND,
                    "Service request not found: " + request.getServiceRequestId(),
                    null
                );
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            NX_M_SERVICE_REQUEST serviceRequest = srOpt.get();

            // Check if approval already exists for this service request
            TApprovalObject approvalObj = new TApprovalObject();
            approvalObj.setIdTrans(serviceRequest.getRequestNumber());
            approvalObj.setApprovalType(ApprovalType.CREATE);
            approvalObj.setCategory(ApprovalCategory.SERVICE_AGREEMENT.name());
            approvalObj.setAppHierId(request.getApprovalHierarchyId());

            // Check for existing pending approval
            Boolean canSubmitApproval = approvalServices.checkExistingApproval(approvalObj);

            if (!canSubmitApproval) {
                logger.warn("Service request {} already has pending approval", serviceRequest.getRequestNumber());
                ResponseObject result = new ResponseObject(
                    ResponseUtils.SUCCESS_FALSE,
                    HttpStatus.BAD_REQUEST,
                    "Service request already has a pending approval",
                    null
                );
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            // Prepare approval object
            String description = request.getApprovalNotes() != null ?
                request.getApprovalNotes() :
                "Service Request: " + serviceRequest.getRequestNumber();

            approvalObj.setDescription(description);

            // Prepare trigger JSON (service request data for approval context)
            String triggerJson = request.getTriggerJson();
            if (triggerJson == null || triggerJson.trim().isEmpty()) {
                triggerJson = buildServiceRequestJson(serviceRequest);
            }
            approvalObj.setJsonString(triggerJson);

            // Register approval and start workflow
            T_APPROVAL approval = approvalServices.registerTransactionApproval(httpServletRequest, approvalObj);

            logger.info("Approval workflow created successfully: Approval ID = {}, SR = {}",
                approval.getTAppId(), serviceRequest.getRequestNumber());

            // Build response
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("approvalId", approval.getTAppId());
            responseData.put("approvalStatus", approval.getStatus());
            responseData.put("serviceRequestId", serviceRequest.getServiceRequestId());
            responseData.put("serviceRequestNumber", serviceRequest.getRequestNumber());
            responseData.put("approvalHierarchyId", approval.getAppHierId());
            responseData.put("approvalName", approval.getApprovalName());

            ResponseObject result = new ResponseObject(
                ResponseUtils.SUCCESS_TRUE,
                HttpStatus.CREATED,
                "Service request submitted for approval successfully",
                responseData
            );

            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (IllegalArgumentException e) {
            logger.error("Validation error during approval submission: {}", e.getMessage(), e);

            ResponseObject result = new ResponseObject(
                ResponseUtils.SUCCESS_FALSE,
                HttpStatus.BAD_REQUEST,
                "Validation failed: " + e.getMessage(),
                null
            );
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error("Failed to submit service request for approval", e);

            ResponseObject result = new ResponseObject(
                ResponseUtils.SUCCESS_FALSE,
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Failed to submit service request for approval: " + e.getMessage(),
                null
            );
            return new ResponseEntity<>(result, result.getHttpCode());
        }
    }

    /**
     * Build JSON representation of service request for approval trigger
     *
     * @param sr Service request entity
     * @return JSON string
     */
    private String buildServiceRequestJson(NX_M_SERVICE_REQUEST sr) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"serviceRequestId\":").append(sr.getServiceRequestId()).append(",");
        json.append("\"requestNumber\":\"").append(sr.getRequestNumber()).append("\",");
        json.append("\"accountId\":").append(sr.getAccountId()).append(",");
        json.append("\"subject\":\"").append(escapedJson(sr.getSubject())).append("\",");
        json.append("\"description\":\"").append(escapedJson(sr.getDescription())).append("\"");
        json.append("}");
        return json.toString();
    }

    /**
     * Escape special characters in JSON strings
     */
    private String escapedJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}
