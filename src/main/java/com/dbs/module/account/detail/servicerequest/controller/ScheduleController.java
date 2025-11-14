package com.dbs.module.account.detail.servicerequest.controller;

import com.dbs.module.account.detail.servicerequest.dto.ScheduleCreateDTO;
import com.dbs.module.account.detail.servicerequest.dto.ScheduleUpdateDTO;
import com.dbs.module.account.detail.servicerequest.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST Controller for Installment Schedule operations
 */
@RestController
@RequestMapping("/v1/dbs/api/account/servicerequest/schedule")
@Api(tags = "Account_ServiceRequest_Schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * Create new installment schedule
     * POST /v1/dbs/api/account/servicerequest/schedule/create
     */
    @PostMapping("/create")
    @ApiOperation(value = "Create new installment schedule")
    public ResponseEntity<?> create(@Valid @RequestBody ScheduleCreateDTO request) {
        return scheduleService.create(request);
    }

    /**
     * Update installment schedule
     * PUT /v1/dbs/api/account/servicerequest/schedule/update
     */
    @PutMapping("/update")
    @ApiOperation(value = "Update installment schedule")
    public ResponseEntity<?> update(@Valid @RequestBody ScheduleUpdateDTO request) {
        return scheduleService.update(request);
    }

    /**
     * Get schedule detail
     * GET /v1/dbs/api/account/servicerequest/schedule/detail/{id}
     */
    @GetMapping("/detail/{scheduleId}")
    @ApiOperation(value = "Get schedule detail")
    public ResponseEntity<?> getDetail(@PathVariable Integer scheduleId) {
        return scheduleService.getDetail(scheduleId);
    }

    /**
     * Get schedules by installment
     * GET /v1/dbs/api/account/servicerequest/schedule/installment/{installmentId}
     */
    @GetMapping("/installment/{installmentId}")
    @ApiOperation(value = "Get schedules by installment")
    public ResponseEntity<?> getByInstallment(@PathVariable Integer installmentId) {
        return scheduleService.getByInstallment(installmentId);
    }

    /**
     * Get schedules by payment status
     * GET /v1/dbs/api/account/servicerequest/schedule/status/{paymentStatus}
     */
    @GetMapping("/status/{paymentStatus}")
    @ApiOperation(value = "Get schedules by payment status")
    public ResponseEntity<?> getByPaymentStatus(@PathVariable Integer paymentStatus) {
        return scheduleService.getByPaymentStatus(paymentStatus);
    }

    /**
     * Record payment for a schedule
     * POST /v1/dbs/api/account/servicerequest/schedule/payment/{id}
     */
    @PostMapping("/payment/{scheduleId}")
    @ApiOperation(value = "Record payment for a schedule")
    public ResponseEntity<?> recordPayment(
        @PathVariable Integer scheduleId,
        @Valid @RequestBody ScheduleUpdateDTO paymentInfo) {
        return scheduleService.recordPayment(scheduleId, paymentInfo);
    }

    /**
     * Toggle active/inactive status
     * PUT /v1/dbs/api/account/servicerequest/schedule/toggle/{id}
     */
    @PutMapping("/toggle/{scheduleId}")
    @ApiOperation(value = "Toggle schedule status")
    public ResponseEntity<?> toggleStatus(@PathVariable Integer scheduleId) {
        return scheduleService.toggleStatus(scheduleId);
    }
}
