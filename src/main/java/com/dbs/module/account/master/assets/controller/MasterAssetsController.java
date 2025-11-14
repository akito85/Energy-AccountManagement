package com.dbs.module.account.master.assets.controller;

import com.dbs.module.account.master.assets.service.MasterAssetsService;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.database.crm.entities.accountmanagement.M_ASSETS;
import com.dbs.database.crm.entities.accountmanagement.VW_CHOOSE_ASSET;
import com.dbs.module.account.master.assets.dto.MasterAssetCreateUpdateDto;
import com.dbs.module.account.master.assets.dto.MasterAssetsInactiveDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/dbs/api/master/assets")
@Api(tags = "Master_Assets")
public class MasterAssetsController {

    @Autowired
    private MasterAssetsService masterAssetsService;

    @GetMapping
    public ResponseEntity<?> getListMasterAssets(@Valid MaterialTablePagingRequest pagingdata, PagedResourcesAssembler<VW_CHOOSE_ASSET> assembler) {
        return masterAssetsService.getListMasterAssets(pagingdata, assembler);
    }
    @GetMapping("/download-filter")
    public ResponseEntity<InputStreamResource>downloadFilter(@Valid MaterialTablePagingRequest pagingdata) {
        return masterAssetsService.downloadFilter(pagingdata);
    }
    @GetMapping("/detail-asset/{assetId}")
    public ResponseEntity<?> detailAsset(@PathVariable Integer assetId) {
        return masterAssetsService.detailAsset(assetId);
    }

//    @GetMapping("/asset-history-assignment/{assetId}")
//    public ResponseEntity<ResponseObject> assetHistoryAssignment(@PathVariable Integer assetId){
//        return masterAssetsService.assetHistoryAssignment(assetId);
//    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody MasterAssetCreateUpdateDto request) {
        return masterAssetsService.createMasterAssets(request);
    }

    @PostMapping("/validate-create")
    public ResponseEntity<?> validateCreate(@RequestBody MasterAssetCreateUpdateDto request) {
        return masterAssetsService.validateCreateMasterAssets(Boolean.TRUE, request);
    }

    @PutMapping("/inactive")
    public ResponseEntity<?> inactiveDataAssets(@RequestBody MasterAssetsInactiveDTO request) {
        return masterAssetsService.inactiveDataAssets(request);
    }

    @GetMapping("/assetName")
    public ResponseEntity<ResponseObject> getListAssetName() {
        return masterAssetsService.getListAssetName();
    }

    @GetMapping("/assetType")
    public ResponseEntity<ResponseObject> getListType() {
        return masterAssetsService.getListType();
    }

    @GetMapping("/assetBrand")
    public ResponseEntity<ResponseObject> getListBrand() {
        return masterAssetsService.getListBrand();
    }

    @GetMapping("/getGsizes")
    public ResponseEntity<ResponseObject> getGsizes() {
        return masterAssetsService.getGsizes();
    }

    @GetMapping("/assetServiceType")
    public ResponseEntity<ResponseObject> getListServiceType() {
        return masterAssetsService.getListServiceType();
    }

    @PutMapping("/update-asset")
    public ResponseEntity<ResponseObject> updateAssets(@RequestBody MasterAssetCreateUpdateDto masterAssetData) {
        return masterAssetsService.updateAssets(masterAssetData);
    }

    @PostMapping("/validate-update-asset")
    public ResponseEntity<ResponseObject> validateUpdateAssets(@RequestBody MasterAssetCreateUpdateDto masterAssetData) {
        return masterAssetsService.validateUpdateAssets(Boolean.TRUE, masterAssetData);
    }
}
