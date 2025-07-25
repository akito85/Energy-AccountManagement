package com.dbs.module.account.detail.gasutilization.controller;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.database.crm.entities.accountmanagement.M_AM_GAS_UTILS;
import com.dbs.database.crm.entities.accountmanagement.view.VW_AM_GAS_UTILS;
import com.dbs.module.account.detail.gasutilization.dto.GasUtilizationCreateUpdateDTO;
import com.dbs.module.account.detail.gasutilization.service.GasUtilizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/v1/dbs/api/account-detail/gas-utilization")
public class GasUtilizationController {

    @Autowired
    private GasUtilizationService services;

    @PostMapping("/create-update")
    public ResponseEntity<?> createGasUtilization(@RequestBody GasUtilizationCreateUpdateDTO request) {
        return services.createUpdateGasUtilization(request);
    }

    @PostMapping("/validate-create-update")
    public ResponseEntity<?> validateCreateUpdate(@RequestBody GasUtilizationCreateUpdateDTO request) {
        return services.validateCreateUpdateGasUtilization(Boolean.TRUE, request);
    }

    @GetMapping("/view-paging/{accountId}")
    public ResponseEntity<?> viewPagingGasUtilization(@PathVariable Integer accountId, @Valid MaterialTablePagingRequest pagingData, PagedResourcesAssembler<VW_AM_GAS_UTILS> assembler) {
        return services.viewPagingGasUtilization(accountId, pagingData, assembler);
    }

    @GetMapping("/view-detail/{id}")
    public ResponseEntity<?> viewDetailGasUtilization(@PathVariable Integer id) {
        return services.viewDetailGasUtilization(null, id);
    }

    @GetMapping("/view-detail-current/{accountId}")
    public ResponseEntity<?> viewDetailCurrentGasUtilization(@PathVariable Integer accountId) {
        return services.viewDetailGasUtilization(accountId,null);
    }

    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<?> softDeleteGasUtilization(@PathVariable Integer id) {
        return services.softDeleteGasUtilization(id);
    }

    @GetMapping("/drop-down-list/utilization-name")
    public ResponseEntity<ResponseObject> getDdlUtilizationName() {
        return services.getDropDownListUtilizationName();
    }

}
