package com.dbs.module.account.master.gassource.controller;

import com.dbs.module.account.master.gassource.dto.QualityDetailDTO;
import com.dbs.module.account.master.gassource.dto.UpdateGSDTO;
import com.dbs.module.account.master.gassource.dto.GasSourceDTO;
import com.dbs.module.account.master.gassource.dto.ActiveInactiveDTO;
import com.dbs.module.account.master.gassource.dto.AssignGSDTO;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.database.crm.entities.accountmanagement.R_GAS_SOURCE_DETAIL;
import com.dbs.database.crm.entities.accountmanagement.VW_ACCOUNT_GS;
import com.dbs.database.crm.entities.accountmanagement.VW_GAS_SOURCE;
import com.dbs.module.account.master.gassource.services.GasSourceServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/dbs/api/gas-source")
@Api(tags = "gasSource")
public class GasSourceController {
    @Autowired
    private GasSourceServiceImpl gasSourceService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody GasSourceDTO request) {
        return gasSourceService.create(request);
    }

    @PostMapping("/validate-create")
    public ResponseEntity<?> validateCreate(@RequestBody GasSourceDTO request) {
        return gasSourceService.validateCreate(Boolean.TRUE, request);
    }

    @GetMapping("/get-UOM")
    public ResponseEntity<?> getUOM() {
        return gasSourceService.getUOM();
    }

    @GetMapping("/cost-center")
    public ResponseEntity<?> getCostCenter() {
        return gasSourceService.getCostCenter();
    }

    @GetMapping("/paging")
    public ResponseEntity<?> paging(@Valid MaterialTablePagingRequest pagingData,
                                                   PagedResourcesAssembler<VW_GAS_SOURCE> assembler) {
        return gasSourceService.getPagging(pagingData, assembler);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable Integer id) {
        return gasSourceService.getDetail(id);
    }

    @GetMapping("/download-filter")
    public ResponseEntity<InputStreamResource>downloadFilter(@Valid MaterialTablePagingRequest pagingdata) {
        return gasSourceService.downloadFilter(pagingdata);
    }

    @PutMapping("/active-inactive")
    public ResponseEntity<?> activeInactive(@RequestBody ActiveInactiveDTO request) {
        return gasSourceService.activeInactive(request);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody UpdateGSDTO request) {
        return gasSourceService.update(request);
    }

    @PostMapping("/validate-update")
    public ResponseEntity<?> validateUpdate(@RequestBody UpdateGSDTO request) {
        return gasSourceService.validateUpdate(Boolean.TRUE, request);
    }

    @PostMapping("/create-quality")
    public ResponseEntity<?> createQuality(@RequestBody QualityDetailDTO request) {
        return gasSourceService.createQuality(request);
    }

    @PostMapping("/validate-create-quality")
    public ResponseEntity<?> validateCreateQuality(@RequestBody QualityDetailDTO request) {
        return gasSourceService.validateCreateQuality(Boolean.TRUE, request);
    }

    @PutMapping("/active-inactive-quality")
    public ResponseEntity<?> inactiveQuality(@RequestBody ActiveInactiveDTO request) {
        return gasSourceService.activeInactiveQuality(request);
    }

    @GetMapping("/detail-quality/{id}")
    public ResponseEntity<?> detailQuality(@PathVariable Integer id) {
        return gasSourceService.detailQuality(id);
    }

    @PutMapping("/update-quality")
    public ResponseEntity<?> updateQuality(@RequestBody QualityDetailDTO request) {
        return gasSourceService.updateQuality(request);
    }

    @PutMapping("/validate-update-quality")
    public ResponseEntity<?> validateUpdateQuality(@RequestBody QualityDetailDTO request) {
        return gasSourceService.validateUpdateQuality(Boolean.TRUE, request);
    }

    @GetMapping("/paging-quality/{id}")
    public ResponseEntity<?> pagingQuality(@PathVariable Integer id, @Valid MaterialTablePagingRequest pagingData,
                                    PagedResourcesAssembler<R_GAS_SOURCE_DETAIL> assembler) {
        return gasSourceService.getPagingQuality(id, pagingData, assembler);
    }


    // FOR ACCOUNT DETAIL (NOT MASTER GAS SOURCE)
    @PostMapping("/assign")
    public ResponseEntity<?> assignGasSource(@RequestBody AssignGSDTO request) {
        return gasSourceService.assignGasSource(request);
    }

    @PostMapping("/validate-assign")
    public ResponseEntity<?> validateAssignGasSource(@RequestBody AssignGSDTO request) {
        return gasSourceService.validateAssignGasSource(request);
    }

    @GetMapping("/get-calorie-type")
    public ResponseEntity<?> getCalorieType() {
        return gasSourceService.getCalorieType();
    }

//    @GetMapping("/get-gas-source")
//    public ResponseEntity<?> getGasSource() {
//        return gasSourceService.getGasSource();
//    }
    
    @GetMapping("/get-gas-source/{costCenterId}")
    public ResponseEntity<?> getGasSource(@PathVariable Integer costCenterId) {
        return gasSourceService.getGasSourceByCostCenter(costCenterId);
    }

    @PutMapping("/inactive-account")
    public ResponseEntity<?> inactiveAccountGasSource(@RequestBody ActiveInactiveDTO request) {
        return gasSourceService.inactiveAccountGasSource(request);
    }

    @GetMapping("/paging-assign/{accountId}")
    public ResponseEntity<?> pagingAssign(@Valid MaterialTablePagingRequest pagingData,
                                    PagedResourcesAssembler<VW_ACCOUNT_GS> assembler,
                                          @PathVariable Integer accountId) {
        return gasSourceService.getPagingAssign(pagingData, assembler, accountId);
    }

    @GetMapping("/detail-assign/{id}")
    public ResponseEntity<?> detailAssign(@PathVariable Integer id) {
        return gasSourceService.detailAssign(id);
    }
}
