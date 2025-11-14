package com.dbs.module.account.detail.servicerequest.controller;

import com.dbs.module.account.detail.servicerequest.dto.PreRequisiteCreateDTO;
import com.dbs.module.account.detail.servicerequest.dto.PreRequisiteUpdateDTO;
import com.dbs.module.account.detail.servicerequest.service.PreRequisiteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST Controller for Prerequisite operations
 */
@RestController
@RequestMapping("/v1/dbs/api/account/servicerequest/prerequisite")
@Api(tags = "Account_ServiceRequest_PreRequisite")
public class PreRequisiteController {

    @Autowired
    private PreRequisiteService preRequisiteService;

    /**
     * Create new prerequisite
     * POST /v1/dbs/api/account/servicerequest/prerequisite/create
     */
    @PostMapping("/create")
    @ApiOperation(value = "Create new prerequisite")
    public ResponseEntity<?> create(@Valid @RequestBody PreRequisiteCreateDTO request) {
        return preRequisiteService.create(request);
    }

    /**
     * Update prerequisite
     * PUT /v1/dbs/api/account/servicerequest/prerequisite/update
     */
    @PutMapping("/update")
    @ApiOperation(value = "Update prerequisite")
    public ResponseEntity<?> update(@Valid @RequestBody PreRequisiteUpdateDTO request) {
        return preRequisiteService.update(request);
    }

    /**
     * Get prerequisite detail
     * GET /v1/dbs/api/account/servicerequest/prerequisite/detail/{id}
     */
    @GetMapping("/detail/{prerequisiteId}")
    @ApiOperation(value = "Get prerequisite detail")
    public ResponseEntity<?> getDetail(@PathVariable Integer prerequisiteId) {
        return preRequisiteService.getDetail(prerequisiteId);
    }

    /**
     * Get all active prerequisites
     * GET /v1/dbs/api/account/servicerequest/prerequisite/list
     */
    @GetMapping("/list")
    @ApiOperation(value = "Get all active prerequisites")
    public ResponseEntity<?> getAllActive() {
        return preRequisiteService.getAllActive();
    }

    /**
     * Get mandatory prerequisites
     * GET /v1/dbs/api/account/servicerequest/prerequisite/mandatory
     */
    @GetMapping("/mandatory")
    @ApiOperation(value = "Get mandatory prerequisites")
    public ResponseEntity<?> getMandatory() {
        return preRequisiteService.getMandatory();
    }

    /**
     * Get prerequisites by type
     * GET /v1/dbs/api/account/servicerequest/prerequisite/type/{prerequisiteType}
     */
    @GetMapping("/type/{prerequisiteType}")
    @ApiOperation(value = "Get prerequisites by type")
    public ResponseEntity<?> getByType(@PathVariable Integer prerequisiteType) {
        return preRequisiteService.getByType(prerequisiteType);
    }

    /**
     * Toggle active/inactive status
     * PUT /v1/dbs/api/account/servicerequest/prerequisite/toggle/{id}
     */
    @PutMapping("/toggle/{prerequisiteId}")
    @ApiOperation(value = "Toggle prerequisite status")
    public ResponseEntity<?> toggleStatus(@PathVariable Integer prerequisiteId) {
        return preRequisiteService.toggleStatus(prerequisiteId);
    }
}
