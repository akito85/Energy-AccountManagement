package com.dbs.module.account.detail.servicerequest.controller;

import com.dbs.module.account.detail.servicerequest.dto.DataRequirementCreateDTO;
import com.dbs.module.account.detail.servicerequest.dto.DataRequirementUpdateDTO;
import com.dbs.module.account.detail.servicerequest.service.DataRequirementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST Controller for Data Requirement operations
 */
@RestController
@RequestMapping("/v1/dbs/api/account/servicerequest/datarequirement")
@Api(tags = "Account_ServiceRequest_DataRequirement")
public class DataRequirementController {

    @Autowired
    private DataRequirementService dataRequirementService;

    /**
     * Create new data requirement
     * POST /v1/dbs/api/account/servicerequest/datarequirement/create
     */
    @PostMapping("/create")
    @ApiOperation(value = "Create new data requirement")
    public ResponseEntity<?> create(@Valid @RequestBody DataRequirementCreateDTO request) {
        return dataRequirementService.create(request);
    }

    /**
     * Update data requirement
     * PUT /v1/dbs/api/account/servicerequest/datarequirement/update
     */
    @PutMapping("/update")
    @ApiOperation(value = "Update data requirement")
    public ResponseEntity<?> update(@Valid @RequestBody DataRequirementUpdateDTO request) {
        return dataRequirementService.update(request);
    }

    /**
     * Get data requirement detail
     * GET /v1/dbs/api/account/servicerequest/datarequirement/detail/{id}
     */
    @GetMapping("/detail/{dataRequirementId}")
    @ApiOperation(value = "Get data requirement detail")
    public ResponseEntity<?> getDetail(@PathVariable Integer dataRequirementId) {
        return dataRequirementService.getDetail(dataRequirementId);
    }

    /**
     * Get all active data requirements
     * GET /v1/dbs/api/account/servicerequest/datarequirement/list
     */
    @GetMapping("/list")
    @ApiOperation(value = "Get all active data requirements")
    public ResponseEntity<?> getAllActive() {
        return dataRequirementService.getAllActive();
    }

    /**
     * Get mandatory data requirements
     * GET /v1/dbs/api/account/servicerequest/datarequirement/mandatory
     */
    @GetMapping("/mandatory")
    @ApiOperation(value = "Get mandatory data requirements")
    public ResponseEntity<?> getMandatory() {
        return dataRequirementService.getMandatory();
    }

    /**
     * Get data requirements by type
     * GET /v1/dbs/api/account/servicerequest/datarequirement/type/{requirementType}
     */
    @GetMapping("/type/{requirementType}")
    @ApiOperation(value = "Get data requirements by type")
    public ResponseEntity<?> getByType(@PathVariable Integer requirementType) {
        return dataRequirementService.getByType(requirementType);
    }

    /**
     * Toggle active/inactive status
     * PUT /v1/dbs/api/account/servicerequest/datarequirement/toggle/{id}
     */
    @PutMapping("/toggle/{dataRequirementId}")
    @ApiOperation(value = "Toggle data requirement status")
    public ResponseEntity<?> toggleStatus(@PathVariable Integer dataRequirementId) {
        return dataRequirementService.toggleStatus(dataRequirementId);
    }
}
