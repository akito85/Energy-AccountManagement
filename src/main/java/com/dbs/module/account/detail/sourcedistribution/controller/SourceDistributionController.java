package com.dbs.module.account.detail.sourcedistribution.controller;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.database.crm.entities.accountmanagement.view.VW_AM_SRC_DIST;
import com.dbs.module.account.detail.sourcedistribution.dto.SourceDistributionCreateUpdateDTO;
import com.dbs.module.account.detail.sourcedistribution.service.SourceDistributionService;
import com.dbs.module.account.utils.ConstantAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/dbs/api/account-detail/source-distribution")
public class SourceDistributionController {

    // API FOR RAW_MATERIAL AND PRODUCT DISTRIBUTION
    // FILTERING BY (TYPE_DIST = "RAW_MATERIAL" OR "PRODUCT_DISTRIBUTION")

    private SourceDistributionService service;

    public SourceDistributionController(@Autowired SourceDistributionService service) {
        this.service = service;
    }

    @RequestMapping("/create-update")
    public ResponseEntity<?> createUpdateSourceDistribution(@RequestBody SourceDistributionCreateUpdateDTO request) {
        return service.createUpdateSourceDistribution(request);
    }

    @PostMapping("/validate-create-update")
    public ResponseEntity<?> validateSourceDistribution(@RequestBody SourceDistributionCreateUpdateDTO request) {
        return service.validateCreateUpdateSourceDistribution(Boolean.TRUE, request);
    }

    @GetMapping("/view-paging/{typeDist}/{accountId}")
    public ResponseEntity<?> viewPagingSourceDistribution(@PathVariable String typeDist, @PathVariable Integer accountId, @Valid MaterialTablePagingRequest pagingData, PagedResourcesAssembler<VW_AM_SRC_DIST> assembler) {
        switch (typeDist) {
            case ConstantAccount.PRODUCT_DISTRIBUTION_PATH:
                return service.viewPagingSourceDistribution(ConstantAccount.PRODUCT_DISTRIBUTION, accountId, pagingData, assembler);
            case ConstantAccount.RAW_MATERIAL_PATH:
                return service.viewPagingSourceDistribution(ConstantAccount.RAW_MATERIAL, accountId, pagingData, assembler);
            default:
                return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/view-detail/{typeDist}/{id}")
    public ResponseEntity<?> viewDetailSourceDistribution(@PathVariable String typeDist, @PathVariable Integer id) {
        switch (typeDist) {
            case ConstantAccount.PRODUCT_DISTRIBUTION_PATH:
                return service.viewDetailSourceDistribution(ConstantAccount.PRODUCT_DISTRIBUTION, id, null);
            case ConstantAccount.RAW_MATERIAL_PATH:
                return service.viewDetailSourceDistribution(ConstantAccount.RAW_MATERIAL, id, null);
            default:
                return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/view-detail-current/{typeDist}/{accountId}")
    public ResponseEntity<?> viewDetailCurrentSourceDistribution(@PathVariable String typeDist, @PathVariable Integer accountId) {
        switch (typeDist) {
            case ConstantAccount.PRODUCT_DISTRIBUTION_PATH:
                return service.viewDetailSourceDistribution(ConstantAccount.PRODUCT_DISTRIBUTION, null, accountId);
            case ConstantAccount.RAW_MATERIAL_PATH:
                return service.viewDetailSourceDistribution(ConstantAccount.RAW_MATERIAL, null, accountId);
            default:
                return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/soft-delete/{typeDist}/{id}")
    public ResponseEntity<?> softDeleteSorceDistribution(@PathVariable String typeDist, @PathVariable Integer id) {
        switch (typeDist) {
            case ConstantAccount.PRODUCT_DISTRIBUTION_PATH:
                return service.softDeletedSourceDistribution(ConstantAccount.PRODUCT_DISTRIBUTION, id);
            case ConstantAccount.RAW_MATERIAL_PATH:
                return service.softDeletedSourceDistribution(ConstantAccount.RAW_MATERIAL, id);
            default:
                return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/drop-down-list/country-name")
    public ResponseEntity<ResponseObject> getDdlUtilizationName() {
        return service.getDropDownListCountryDistribution();
    }
}
