package com.dbs.module.account.detail.servicerequest.controller;

import com.dbs.module.account.detail.servicerequest.dto.BillingItemCreateDTO;
import com.dbs.module.account.detail.servicerequest.dto.BillingItemUpdateDTO;
import com.dbs.module.account.detail.servicerequest.service.BillingItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST Controller for Billing Item operations
 */
@RestController
@RequestMapping("/v1/dbs/api/account/servicerequest/billingitem")
@Api(tags = "Account_ServiceRequest_BillingItem")
public class BillingItemController {

    @Autowired
    private BillingItemService billingItemService;

    /**
     * Create new billing item
     * POST /v1/dbs/api/account/servicerequest/billingitem/create
     */
    @PostMapping("/create")
    @ApiOperation(value = "Create new billing item")
    public ResponseEntity<?> create(@Valid @RequestBody BillingItemCreateDTO request) {
        return billingItemService.create(request);
    }

    /**
     * Update billing item
     * PUT /v1/dbs/api/account/servicerequest/billingitem/update
     */
    @PutMapping("/update")
    @ApiOperation(value = "Update billing item")
    public ResponseEntity<?> update(@Valid @RequestBody BillingItemUpdateDTO request) {
        return billingItemService.update(request);
    }

    /**
     * Get billing item detail
     * GET /v1/dbs/api/account/servicerequest/billingitem/detail/{id}
     */
    @GetMapping("/detail/{billingItemId}")
    @ApiOperation(value = "Get billing item detail")
    public ResponseEntity<?> getDetail(@PathVariable Integer billingItemId) {
        return billingItemService.getDetail(billingItemId);
    }

    /**
     * Get billing items by installment
     * GET /v1/dbs/api/account/servicerequest/billingitem/installment/{installmentId}
     */
    @GetMapping("/installment/{installmentId}")
    @ApiOperation(value = "Get billing items by installment")
    public ResponseEntity<?> getByInstallment(@PathVariable Integer installmentId) {
        return billingItemService.getByInstallment(installmentId);
    }

    /**
     * Get billing items by item type
     * GET /v1/dbs/api/account/servicerequest/billingitem/type/{itemType}
     */
    @GetMapping("/type/{itemType}")
    @ApiOperation(value = "Get billing items by item type")
    public ResponseEntity<?> getByItemType(@PathVariable Integer itemType) {
        return billingItemService.getByItemType(itemType);
    }

    /**
     * Toggle active/inactive status
     * PUT /v1/dbs/api/account/servicerequest/billingitem/toggle/{id}
     */
    @PutMapping("/toggle/{billingItemId}")
    @ApiOperation(value = "Toggle billing item status")
    public ResponseEntity<?> toggleStatus(@PathVariable Integer billingItemId) {
        return billingItemService.toggleStatus(billingItemId);
    }
}
