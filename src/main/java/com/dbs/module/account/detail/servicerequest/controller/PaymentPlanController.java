package com.dbs.module.account.detail.servicerequest.controller;

import com.dbs.module.account.detail.servicerequest.dto.PaymentPlanCreateDTO;
import com.dbs.module.account.detail.servicerequest.dto.PaymentPlanUpdateDTO;
import com.dbs.module.account.detail.servicerequest.service.PaymentPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST Controller for Payment Plan operations
 */
@RestController
@RequestMapping("/v1/dbs/api/account/servicerequest/paymentplan")
@Api(tags = "Account_ServiceRequest_PaymentPlan")
public class PaymentPlanController {

    @Autowired
    private PaymentPlanService paymentPlanService;

    /**
     * Create new payment plan
     * POST /v1/dbs/api/account/servicerequest/paymentplan/create
     */
    @PostMapping("/create")
    @ApiOperation(value = "Create new payment plan")
    public ResponseEntity<?> create(@Valid @RequestBody PaymentPlanCreateDTO request) {
        return paymentPlanService.create(request);
    }

    /**
     * Update payment plan
     * PUT /v1/dbs/api/account/servicerequest/paymentplan/update
     */
    @PutMapping("/update")
    @ApiOperation(value = "Update payment plan")
    public ResponseEntity<?> update(@Valid @RequestBody PaymentPlanUpdateDTO request) {
        return paymentPlanService.update(request);
    }

    /**
     * Get payment plan detail
     * GET /v1/dbs/api/account/servicerequest/paymentplan/detail/{id}
     */
    @GetMapping("/detail/{paymentPlanId}")
    @ApiOperation(value = "Get payment plan detail")
    public ResponseEntity<?> getDetail(@PathVariable Integer paymentPlanId) {
        return paymentPlanService.getDetail(paymentPlanId);
    }

    /**
     * Get payment plans by installment
     * GET /v1/dbs/api/account/servicerequest/paymentplan/installment/{installmentId}
     */
    @GetMapping("/installment/{installmentId}")
    @ApiOperation(value = "Get payment plans by installment")
    public ResponseEntity<?> getByInstallment(@PathVariable Integer installmentId) {
        return paymentPlanService.getByInstallment(installmentId);
    }

    /**
     * Get payment plans by plan type
     * GET /v1/dbs/api/account/servicerequest/paymentplan/type/{planType}
     */
    @GetMapping("/type/{planType}")
    @ApiOperation(value = "Get payment plans by plan type")
    public ResponseEntity<?> getByPlanType(@PathVariable Integer planType) {
        return paymentPlanService.getByPlanType(planType);
    }

    /**
     * Toggle active/inactive status
     * PUT /v1/dbs/api/account/servicerequest/paymentplan/toggle/{id}
     */
    @PutMapping("/toggle/{paymentPlanId}")
    @ApiOperation(value = "Toggle payment plan status")
    public ResponseEntity<?> toggleStatus(@PathVariable Integer paymentPlanId) {
        return paymentPlanService.toggleStatus(paymentPlanId);
    }
}
