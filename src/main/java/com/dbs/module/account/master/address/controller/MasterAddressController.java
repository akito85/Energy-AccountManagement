package com.dbs.module.account.master.address.controller;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.database.crm.entities.accountmanagement.M_ADDRESSES;
import com.dbs.database.crm.entities.accountmanagement.VW_CHOOSE_ADDRESS;
import com.dbs.database.crm.entities.accountmanagement.view.VW_ADDRESS;
import com.dbs.module.account.master.address.dto.MasterAddressInactiveDTO;
import com.dbs.module.account.master.address.dto.CreateUpdateDTO;
import com.dbs.module.account.master.address.service.MasterAddressService;
import com.dbs.module.account.utils.ConstantAccount;
import io.swagger.annotations.Api;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/dbs/api/master/address")
@Api(tags = "Master_Address")
public class MasterAddressController {
    private final MasterAddressService masterAddressService;

    public MasterAddressController(MasterAddressService masterAddressService) {
        this.masterAddressService = masterAddressService;
    }
    
    @GetMapping
    public ResponseEntity<?> getListMasterAddress(@Valid MaterialTablePagingRequest pagingdata, PagedResourcesAssembler<VW_ADDRESS> assembler) {
        return masterAddressService.getListMasterAddress(pagingdata, assembler);
    }
    @GetMapping("/download-filter")
    public ResponseEntity<InputStreamResource>downloadFilter(@Valid MaterialTablePagingRequest pagingdata) {
        return masterAddressService.downloadFilter(pagingdata);
    }
    @GetMapping("/detail/{addressId}")
    public ResponseEntity<?> getDetail(@PathVariable Integer addressId) {
        return masterAddressService.detailAddress(addressId);
    }
    @GetMapping("/list-country")
    public ResponseEntity<?> getListCountry() {
        return masterAddressService.listLocationInformation(Constant.LOCATION_TYPE_COUNTRY, null);
    }
    @GetMapping("/list-province/{parentId}")
    public ResponseEntity<?> getListProvince(@PathVariable Integer parentId) {
        return masterAddressService.listLocationInformation(Constant.LOCATION_TYPE_PROVINCE, parentId);
    }
    @GetMapping("/list-city/{parentId}")
    public ResponseEntity<?> getListCity(@PathVariable Integer parentId) {
        return masterAddressService.listLocationInformation(Constant.LOCATION_TYPE_CITY, parentId);
    }
    @GetMapping("/list-district/{parentId}")
    public ResponseEntity<?> getListDistrict(@PathVariable Integer parentId) {
        return masterAddressService.listLocationInformation(Constant.LOCATION_TYPE_DISTRICT, parentId);
    }
    @GetMapping("/list-sub-district/{parentId}")
    public ResponseEntity<?> getListSubDistrict(@PathVariable Integer parentId) {
        return masterAddressService.listLocationInformation(Constant.LOCATION_TYPE_SUB_DISTRICT, parentId);
    }
    @GetMapping("/list-postal-code/{parentId}")
    public ResponseEntity<?> getPostalCode(@PathVariable Integer parentId) {
        return masterAddressService.listLocationInformation(Constant.LOCATION_TYPE_POSTAL_CODE, parentId);
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateUpdateDTO request) {
        return masterAddressService.createMasterAddress(request);
    }

    @PostMapping("/validate-create")
    public ResponseEntity<?> validateCreate(@RequestBody CreateUpdateDTO request) {
        return masterAddressService.validateCreateMasterAddress(Boolean.TRUE, request);
    }

    @PutMapping("/activate-inactive")
    public ResponseEntity<?> inactiveDataAssets(@RequestBody MasterAddressInactiveDTO request) {
        return masterAddressService.activateInactiveDataAddress(request);
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateAddress(@RequestBody CreateUpdateDTO request) {
        return masterAddressService.updateAddress(request);
    }

    @PostMapping("/validate-update")
    public ResponseEntity<?> validateUpdateDataAddress(@RequestBody CreateUpdateDTO request) {
        return masterAddressService.validateUpdateAddress(Boolean.TRUE, request);
    }


    // CHANGE REQUEST
    @GetMapping("/building-prefix")
    public ResponseEntity<?> getBuildingPrefix() {
        return masterAddressService.getFromGlobalType(ConstantAccount.ADDRESS_BUILDING_PREFIX);
    }

    @GetMapping("/floor-prefix")
    public ResponseEntity<?> getFloorPrefix() {
        return masterAddressService.getFromGlobalType(ConstantAccount.ADDRESS_FLOOR_PREFIX);
    }

    @GetMapping("/street-name-prefix")
    public ResponseEntity<?> getStreetNamePrefix() {
        return masterAddressService.getFromGlobalType(ConstantAccount.ADDRESS_STREET_NAME_PREFIX);
    }

    @GetMapping("/neighborhood1-prefix")
    public ResponseEntity<?> getBrotherhood1Prefix() {
        return masterAddressService.getFromGlobalType(ConstantAccount.ADDRESS_NEIGHBORHOOD1_PREFIX);
    }

    @GetMapping("/neighborhood2-prefix")
    public ResponseEntity<?> getBrotherhood2Prefix() {
        return masterAddressService.getFromGlobalType(ConstantAccount.ADDRESS_NEIGHBORHOOD2_PREFIX);
    }
}
