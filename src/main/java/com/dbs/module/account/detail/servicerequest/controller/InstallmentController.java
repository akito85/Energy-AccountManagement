package com.dbs.module.account.detail.servicerequest.controller;

import com.dbs.module.account.detail.servicerequest.dto.InstallmentDTO;
import com.dbs.module.account.detail.servicerequest.service.InstallmentService;
import com.dbs.module.account.detail.servicerequest.service.SRDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST Controller for Installment operations
 */
@RestController
@RequestMapping("/v1/dbs/api/account/servicerequest/installment")
@Api(tags = "Account_ServiceRequest_Installment")
public class InstallmentController {

    @Autowired
    private InstallmentService installmentService;

    @Autowired
    private SRDetailsService srDetailsService;

    /**
     * Create new installment
     * POST /v1/dbs/api/account/servicerequest/installment/create
     */
    @PostMapping("/create")
    @ApiOperation(value = "Create new installment")
    public ResponseEntity<?> create(@Valid @RequestBody InstallmentDTO request) {
        return installmentService.create(request);
    }

    /**
     * Update installment
     * PUT /v1/dbs/api/account/servicerequest/installment/update
     */
    @PutMapping("/update")
    @ApiOperation(value = "Update installment")
    public ResponseEntity<?> update(@Valid @RequestBody InstallmentDTO request) {
        return installmentService.update(request);
    }

    /**
     * Get installment detail
     * GET /v1/dbs/api/account/servicerequest/installment/detail/{id}
     */
    @GetMapping("/detail/{installmentId}")
    @ApiOperation(value = "Get installment detail")
    public ResponseEntity<?> getDetail(@PathVariable Integer installmentId) {
        return installmentService.getDetail(installmentId);
    }

    /**
     * Get installment payment schedule
     * GET /v1/dbs/api/account/servicerequest/installment/schedule/{id}
     */
    @GetMapping("/schedule/{installmentId}")
    @ApiOperation(value = "Get installment payment schedule")
    public ResponseEntity<?> getSchedule(@PathVariable Integer installmentId) {
        return installmentService.getSchedules(installmentId);
    }

    /**
     * Link existing installment to service request
     * POST /v1/dbs/api/account/servicerequest/installment/link
     */
    @PostMapping("/link")
    @ApiOperation(value = "Link installment to service request")
    public ResponseEntity<?> linkInstallment(
        @RequestParam Integer serviceRequestId,
        @RequestParam Integer accountId,
        @RequestParam Integer installmentId) {
        return srDetailsService.linkInstallment(serviceRequestId, accountId, installmentId);
    }
}
