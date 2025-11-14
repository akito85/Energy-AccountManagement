package com.dbs.module.account.detail.servicerequest.controller;

import com.dbs.module.account.detail.servicerequest.dto.ServiceRequestApprovalSubmissionDTO;
import com.dbs.module.account.detail.servicerequest.service.ServiceRequestApprovalService;
import com.dbs.common.library.ctrl.ResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * REST Controller for Service Request Approval Operations
 *
 * This controller provides endpoints for submitting service requests to approval workflow.
 * It integrates with the existing approval system (T_APPROVAL, T_APPROVAL_DTL tables)
 * to manage approval hierarchies and workflows.
 */
@Slf4j
@RestController
@RequestMapping("/v1/dbs/api/account/servicerequest/approval")
@Api(tags = "Service Request - Approval", description = "Endpoints for service request approval workflow")
public class ServiceRequestApprovalController {

    @Autowired
    private ServiceRequestApprovalService approvalService;

    /**
     * Submit service request for approval
     *
     * Creates an approval record in T_APPROVAL table and starts the approval workflow
     * based on the specified approval hierarchy. The approval follows the configured
     * hierarchy levels and positions.
     *
     * This endpoint is designed to be called independently after service request creation,
     * allowing flexibility in the submission flow (immediate approval vs delayed approval).
     *
     * @param request Approval submission data including SR ID and approval hierarchy
     * @param httpServletRequest HTTP servlet request for user context extraction
     * @return ResponseObject containing approval ID and workflow status
     */
    @PostMapping("/submit")
    @ApiOperation(
        value = "Submit service request for approval",
        notes = "Creates approval record and starts approval workflow. " +
                "Validates that service request exists and no pending approval exists. " +
                "Returns approval workflow ID and initial status. " +
                "The approval follows the hierarchy levels defined in M_APPROVAL_HIERARCHY.",
        response = ResponseObject.class
    )
    public ResponseEntity<ResponseObject> submitForApproval(
            @Valid @RequestBody ServiceRequestApprovalSubmissionDTO request,
            HttpServletRequest httpServletRequest) {

        log.info("Received approval submission request for service request ID: {}",
                 request.getServiceRequestId());

        return approvalService.submitForApproval(request, httpServletRequest);
    }
}
