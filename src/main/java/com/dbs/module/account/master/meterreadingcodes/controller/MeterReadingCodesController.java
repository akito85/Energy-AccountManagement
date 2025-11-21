package com.dbs.module.account.master.meterreadingcodes.controller;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.database.crm.entities.accountmanagement.M_METER_READING_CODES;
import com.dbs.module.account.master.meterreadingcodes.dto.ActiveInactiveDTO;
import com.dbs.module.account.master.meterreadingcodes.dto.CreateRequestDTO;
import com.dbs.module.account.master.meterreadingcodes.dto.UpdateRequestDTO;
import com.dbs.module.account.master.meterreadingcodes.service.MeterReadingCodesService;
import io.swagger.annotations.Api;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/dbs/api/meter-reading-codes")
@Api(tags = "Meter_Reading_Codes")
public class MeterReadingCodesController {
    
    @Autowired
    private MeterReadingCodesService services;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateRequestDTO request, HttpServletRequest httpServletRequest) {
        return services.create(request, httpServletRequest);
    }

    @PostMapping("/validate-create")
    public ResponseEntity<?> validateCreate(@RequestBody CreateRequestDTO request, HttpServletRequest httpServletRequest) {
        return services.validateCreate(Boolean.TRUE, request, httpServletRequest);
    }

    @GetMapping("/list")
    public ResponseEntity<?> paging(@Valid MaterialTablePagingRequest pagingData,
                                                   PagedResourcesAssembler<M_METER_READING_CODES> assembler, HttpServletRequest httpServletRequest) {
        return services.getList(pagingData, assembler, httpServletRequest);
    }

    @GetMapping("/download-filter")
    public ResponseEntity<InputStreamResource>downloadFilter(@Valid MaterialTablePagingRequest pagingdata, HttpServletRequest httpServletRequest) {
        return services.downloadFilter(pagingdata, httpServletRequest);
    }
    
    @GetMapping("/detail/{meterReadingCodesId}")
    public ResponseEntity<?> detail(@PathVariable Integer meterReadingCodesId) {
        return services.getDetail(meterReadingCodesId);
    }
    @GetMapping("/list-cost-center")
    public ResponseEntity<?> ddlCostCenter(HttpServletRequest request) {
        return services.listCostCenter(request);
    }

    @PutMapping("/active-inactive")
    public ResponseEntity<?> activeInactive(@RequestBody ActiveInactiveDTO request) {
        return services.activeInactive(request);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody UpdateRequestDTO request, HttpServletRequest httpServletRequest) {
        return services.update(request, httpServletRequest);
    }

    @PostMapping("/validate-update")
    public ResponseEntity<?> validateUpdate(@RequestBody UpdateRequestDTO request, HttpServletRequest httpServletRequest) {
        return services.validateUpdate(Boolean.TRUE, request, httpServletRequest);
    }
}
