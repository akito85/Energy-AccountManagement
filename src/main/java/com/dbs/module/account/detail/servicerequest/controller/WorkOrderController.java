package com.dbs.module.account.detail.servicerequest.controller;

import com.dbs.module.account.detail.servicerequest.dto.WorkOrderDTO;
import com.dbs.module.account.detail.servicerequest.service.SRDetailsService;
import com.dbs.module.account.detail.servicerequest.service.WorkOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST Controller for Work Order operations
 */
@RestController
@RequestMapping("/v1/dbs/api/account/servicerequest/workorder")
@Api(tags = "Account_ServiceRequest_WorkOrder")
public class WorkOrderController {

    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private SRDetailsService srDetailsService;

    /**
     * Create new work order and link to service request
     * POST /v1/dbs/api/account/servicerequest/workorder/create
     */
    @PostMapping("/create")
    @ApiOperation(value = "Create new work order")
    public ResponseEntity<?> create(
        @Valid @RequestBody WorkOrderDTO request,
        @RequestParam Integer serviceRequestId,
        @RequestParam Integer accountId) {

        // Create work order
        ResponseEntity<?> createResponse = workOrderService.create(request, serviceRequestId);

        // If successful, link to service request
        if (createResponse.getStatusCode().is2xxSuccessful()) {
            // Extract work order ID from response and link
            // This would need to be implemented based on response structure
        }

        return createResponse;
    }

    /**
     * Update work order
     * PUT /v1/dbs/api/account/servicerequest/workorder/update
     */
    @PutMapping("/update")
    @ApiOperation(value = "Update work order")
    public ResponseEntity<?> update(@Valid @RequestBody WorkOrderDTO request) {
        return workOrderService.update(request);
    }

    /**
     * Get work order detail
     * GET /v1/dbs/api/account/servicerequest/workorder/detail/{id}
     */
    @GetMapping("/detail/{workOrderId}")
    @ApiOperation(value = "Get work order detail")
    public ResponseEntity<?> getDetail(@PathVariable Integer workOrderId) {
        return workOrderService.getDetail(workOrderId);
    }

    /**
     * Get work orders for a service request
     * GET /v1/dbs/api/account/servicerequest/workorder/list/{serviceRequestId}
     */
    @GetMapping("/list/{serviceRequestId}")
    @ApiOperation(value = "Get work orders by service request")
    public ResponseEntity<?> getListByServiceRequest(@PathVariable Integer serviceRequestId) {
        return workOrderService.getListByServiceRequest(serviceRequestId);
    }

    /**
     * Link existing work order to service request
     * POST /v1/dbs/api/account/servicerequest/workorder/link
     */
    @PostMapping("/link")
    @ApiOperation(value = "Link work order to service request")
    public ResponseEntity<?> linkWorkOrder(
        @RequestParam Integer serviceRequestId,
        @RequestParam Integer accountId,
        @RequestParam Integer workOrderId) {
        return srDetailsService.linkWorkOrder(serviceRequestId, accountId, workOrderId);
    }
}
