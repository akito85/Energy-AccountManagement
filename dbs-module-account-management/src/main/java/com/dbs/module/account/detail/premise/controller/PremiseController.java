package com.dbs.module.account.detail.premise.controller;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.database.crm.entities.accountmanagement.VW_ASSET_ASSIGNMENT;
import com.dbs.database.crm.entities.accountmanagement.VW_CHOOSE_ASSET;
import com.dbs.database.crm.entities.accountmanagement.VW_CUSTOMER_ADDDRESS;
import com.dbs.module.account.detail.premise.dto.InactiveAssetAssignmentDTO;
import com.dbs.module.account.detail.premise.dto.ServicePointCreateDTO;
import com.dbs.module.account.detail.premise.dto.UpdateServicePointDTO;
import com.dbs.module.account.detail.premise.service.AssetAssignmentService;
import com.dbs.module.account.detail.premise.service.PremiseService;
import com.dbs.module.account.detail.premise.service.ServicePointService;
import com.dbs.module.account.detail.premise.dto.AssignAssetDTO;
import io.swagger.annotations.Api;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/dbs/api/premise")
@Api(tags = "Premise")
public class PremiseController {

    private final PremiseService premiseService;
    private final ServicePointService servicePointService;
    private final AssetAssignmentService assetAssignmentService;

    public PremiseController(AssetAssignmentService assetAssignmentService, ServicePointService servicePointService, PremiseService premiseService) {
        this.premiseService = premiseService;
        this.servicePointService = servicePointService;
        this.assetAssignmentService = assetAssignmentService;
    }

    @GetMapping("/productName")
    public ResponseEntity<ResponseObject> getProductNameList() {
        return assetAssignmentService.viewListProductAsset();
    }

    @GetMapping("/ansi")
    public ResponseEntity<ResponseObject> getAnsiList() {
        return assetAssignmentService.getListAnsi();
    }
    
    @GetMapping("/view/{accountId}")
    public ResponseEntity<ResponseObject> getListPremiseByAccountId(@Valid MaterialTablePagingRequest pagingdata, PagedResourcesAssembler<VW_CUSTOMER_ADDDRESS> assembler, @PathVariable Integer accountId) {
        return premiseService.getPremiseByAccountId(pagingdata,assembler,accountId);
    }

    @GetMapping("/servicePoint/getListName")
    public ResponseEntity<ResponseObject> getListServicePointName() {
        return servicePointService.getServicePointNameList();
    }

    @GetMapping("/servicePoint/getListAddress/{accountId}")
    public ResponseEntity<ResponseObject> getListServicePointAddress(@PathVariable Integer accountId) {
        return servicePointService.getServicePointAddress(accountId);
    }

    @PostMapping("/servicePoint/create")
    public ResponseEntity<ResponseObject> createServicePoint(@RequestBody ServicePointCreateDTO request) {
        return servicePointService.createServicePoint(request);
    }

    @PostMapping("/servicePoint/validate-create")
    public ResponseEntity<ResponseObject> validateCreateServicePoint(@RequestBody ServicePointCreateDTO request) {
        return servicePointService.validateCreateServicePoint(request);
    }

    @GetMapping("/servicePoint/assets/view/{servicePointId}")
    public ResponseEntity<ResponseObject> getDataServicePoint(@PathVariable Integer servicePointId) {
        return assetAssignmentService.getDataServicePoint(servicePointId);
    }

    @GetMapping("/servicePoint/assets/view/list/{servicePointId}")
    public ResponseEntity<ResponseObject> getListAssetAssignments(@Valid MaterialTablePagingRequest pagingdata, PagedResourcesAssembler<VW_ASSET_ASSIGNMENT> assembler, @PathVariable Integer servicePointId) {
        return assetAssignmentService.viewAssetAssignment(pagingdata, assembler, servicePointId);
    }

    @PostMapping("/servicePoint/assets/assign")
    public ResponseEntity<ResponseObject> assignAsset(@RequestBody AssignAssetDTO request) {
        return assetAssignmentService.assignAsset(request);
    }

    @PostMapping("/servicePoint/assets/validate-assign")
    public ResponseEntity<ResponseObject> validateAssignAsset(@RequestBody AssignAssetDTO request) {
        return assetAssignmentService.validateAssignAsset(request);
    }

    @PostMapping("/servicePoint/assets/serialnumber-brand")
    public ResponseEntity<ResponseObject> serialNumberBrand(@RequestBody AssignAssetDTO request) {
        return assetAssignmentService.serialNumberBrand(request);
    }

    @GetMapping("/servicePoint/assets/choose")
    public ResponseEntity<ResponseObject> chooseAsset(@Valid MaterialTablePagingRequest pagingRequest,
                                                      PagedResourcesAssembler<VW_CHOOSE_ASSET> assembler) {
        return assetAssignmentService.chooseAsset(pagingRequest, assembler);
    }

    @PutMapping("/servicePoint/update")
    public ResponseEntity<ResponseObject> updateServicePoint(@RequestBody UpdateServicePointDTO request) {
        return servicePointService.updateServicePoint(request);
    }

    @PutMapping("/servicePoint/inactive")
    public ResponseEntity<ResponseObject> inactiveServicePoint(@RequestBody UpdateServicePointDTO request) {
        return servicePointService.inactiveServicePoint(request);
    }

    @PutMapping("/servicePoint/assets/inactive")
    public ResponseEntity<ResponseObject> inactiveAssetAssignment(@RequestBody InactiveAssetAssignmentDTO request) {
        return assetAssignmentService.inactiveAssetAssignment(request);
    }
}
