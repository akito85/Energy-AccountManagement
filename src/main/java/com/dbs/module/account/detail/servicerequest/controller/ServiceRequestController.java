package com.dbs.module.account.detail.servicerequest.controller;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.module.account.detail.servicerequest.dto.ServiceRequestCreateDTO;
import com.dbs.module.account.detail.servicerequest.dto.ServiceRequestUpdateDTO;
import com.dbs.module.account.detail.servicerequest.service.ServiceRequestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST Controller for Service Request operations
 */
@RestController
@RequestMapping("/v1/dbs/api/account/servicerequest")
@Api(tags = "Account_ServiceRequest")
public class ServiceRequestController {

    @Autowired
    private ServiceRequestService serviceRequestService;

    /**
     * Create new service request
     * POST /v1/dbs/api/account/servicerequest/create
     */
    @PostMapping("/create")
    @ApiOperation(value = "Create new service request")
    public ResponseEntity<?> create(@Valid @RequestBody ServiceRequestCreateDTO request) {
        return serviceRequestService.create(request);
    }

    /**
     * Update service request
     * PUT /v1/dbs/api/account/servicerequest/update
     */
    @PutMapping("/update")
    @ApiOperation(value = "Update service request")
    public ResponseEntity<?> update(@Valid @RequestBody ServiceRequestUpdateDTO request) {
        return serviceRequestService.update(request);
    }

    /**
     * Get service request detail
     * GET /v1/dbs/api/account/servicerequest/detail/{id}
     */
    @GetMapping("/detail/{serviceRequestId}")
    @ApiOperation(value = "Get service request detail")
    public ResponseEntity<?> getDetail(@PathVariable Integer serviceRequestId) {
        return serviceRequestService.getDetail(serviceRequestId);
    }

    /**
     * Get paginated list of service requests for account
     * GET /v1/dbs/api/account/servicerequest/list/{accountId}
     */
    @GetMapping("/list/{accountId}")
    @ApiOperation(value = "Get service requests by account")
    public ResponseEntity<?> getList(
        @PathVariable Integer accountId,
        @Valid MaterialTablePagingRequest pagingdata,
        PagedResourcesAssembler assembler) {
        return serviceRequestService.getListByAccount(accountId, pagingdata, assembler);
    }

    /**
     * Toggle active/inactive status
     * PUT /v1/dbs/api/account/servicerequest/toggle/{id}
     */
    @PutMapping("/toggle/{serviceRequestId}")
    @ApiOperation(value = "Toggle service request status")
    public ResponseEntity<?> toggleStatus(@PathVariable Integer serviceRequestId) {
        return serviceRequestService.toggleStatus(serviceRequestId);
    }

    /**
     * Get service request types from GLOBAL_TYPE
     * GET /v1/dbs/api/account/servicerequest/types
     */
    @GetMapping("/types")
    @ApiOperation(value = "Get service request types")
    public ResponseEntity<?> getRequestTypes() {
        return serviceRequestService.getRequestTypes();
    }

    /**
     * Get service request categories for dropdown
     * GET /v1/dbs/api/account/servicerequest/category
     */
    @GetMapping("/category")
    @ApiOperation(value = "Get service request categories for dropdown")
    public ResponseEntity<?> getRequestCategories() {
        return serviceRequestService.getRequestCategories();
    }

    /**
     * Get service request subcategories for dropdown
     * GET /v1/dbs/api/account/servicerequest/subcategory
     */
    @GetMapping("/subcategory")
    @ApiOperation(value = "Get service request subcategories for dropdown")
    public ResponseEntity<?> getRequestSubcategories() {
        return serviceRequestService.getRequestSubcategories();
    }
}
