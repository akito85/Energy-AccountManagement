package com.dbs.module.account.master.location.controller;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.database.crm.entities.accountmanagement.MV_LOCATION;
import com.dbs.database.crm.entities.product.M_LOCATION;
import com.dbs.database.crm.entities.usermanagement.view.VW_LOCATION;
import com.dbs.module.account.master.location.dto.ActiveInactiveDto;
import com.dbs.module.account.master.location.dto.CreateMasterLocationDTO;
import com.dbs.module.account.master.location.dto.UpdateMasterLocationDTO;
import com.dbs.module.account.master.location.service.MasterLocationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/v1/dbs/api/master/location")
@Api(tags = "Master_Location")
public class MasterLocationController {


    @Autowired
    private MasterLocationService masterLocationService;

    @GetMapping("/paging-location/{locationType}")
    public ResponseEntity<?> getPagingLocation(@Valid MaterialTablePagingRequest pagingRequest, PagedResourcesAssembler<VW_LOCATION> assembler, PagedResourcesAssembler<MV_LOCATION> assemblerMv, @PathVariable Integer locationType) {

        if(locationType.equals(2346)) {
            return masterLocationService.getAllPagingLocation(pagingRequest, assemblerMv);
        } else {
            return masterLocationService.getPagingLocation(locationType, pagingRequest,assembler);
        }
    }

    @GetMapping("/download-filter/{locationType}")
    public ResponseEntity<InputStreamResource>downloadFilter(@PathVariable Integer locationType, @Valid MaterialTablePagingRequest pagingdata) {

        if(locationType.equals(2346)) {
            return masterLocationService.downloadFilterAll(pagingdata);
        } else {
            return masterLocationService.downloadFilter(locationType, pagingdata);
        }
    }

    @GetMapping("/detail-location/{locationId}")
    public ResponseEntity<?> getDetailLocation(@PathVariable Integer locationId) {
        return masterLocationService.detailLocation(locationId);
    }

    @GetMapping("/detail-location-all/{postalCodeId}")
    public ResponseEntity<?> getDetailLocationAll(@PathVariable Integer postalCodeId) {
        return masterLocationService.detailLocationAll(postalCodeId);
    }

    @GetMapping("/country")
    public ResponseEntity<?> getAllCountry() {
        return masterLocationService.getAllCountry();
    }

    @GetMapping("/province/{id}")
    public ResponseEntity<?> getAllProvince(@PathVariable Integer id) {
        return masterLocationService.getAllProvince(id);
    }

    @GetMapping("/city/{id}")
    public ResponseEntity<?> getAllCity(@PathVariable Integer id) {
        return masterLocationService.getAllCity(id);
    }

    @GetMapping("/district/{id}")
    public ResponseEntity<?> getAllDistrict(@PathVariable Integer id) {
        return masterLocationService.getAllDistrict(id);
    }

    @GetMapping("/sub-district/{id}")
    public ResponseEntity<?> getAllSubDistrict(@PathVariable Integer id) {
        return masterLocationService.getAllSubDistrict(id);
    }

    @GetMapping("/postal-code/{id}")
    public ResponseEntity<?> getAllPostalCode(@PathVariable Integer id) {
        return masterLocationService.getAllPostalCode(id);
    }
//    @GetMapping("/list-location-type")
//    public ResponseEntity<?> getLocationType() {
//        return masterLocationService.getFromMasterGlobal(Constant.GLB_TYPE_LOCATION_TYPE_NAME);
//    }
//    @GetMapping("/list-location-parent/{locationTypeId}")
//    public ResponseEntity<?> getLocationParent(@PathVariable Integer locationTypeId) {
//        return masterLocationService.getFromMasterLocation(Constant.MASTER_LOCATION_TYPE_BY_LOCATION_TYPE_ID,locationTypeId);
//    }
//    @GetMapping("/paging-location-parent/{locationTypeId}")
//    public ResponseEntity<?> getPagingLocationParent(@Valid MaterialTablePagingRequest pagingRequest,
//                                                     PagedResourcesAssembler<M_LOCATION> assembler, @PathVariable Integer locationTypeId) {
//        return masterLocationService.getPagingFromMasterLocation(pagingRequest, assembler, Constant.MASTER_LOCATION_TYPE_BY_LOCATION_TYPE_ID,locationTypeId);
//    }
    @GetMapping("/list-location-parent-type/{locationTypeId}")
    public ResponseEntity<?> getLocationParentType(@PathVariable Integer locationTypeId) {
        return masterLocationService.getLocationParentType(locationTypeId);
    }
    @GetMapping("/list-location-child/{locationTypeId}")
    public ResponseEntity<?> getLocationChild(@PathVariable Integer locationTypeId) {
        return masterLocationService.getFromMasterLocation(Constant.MASTER_LOCATION_TYPE_LOCATION_PARENT_ID,locationTypeId);
    }
    @GetMapping("/list-location-reference")
    public ResponseEntity<?> getLocationCity() {
        return masterLocationService.getFromMasterGlobal(Constant.LOCATION_REFERENCE_NAME);
    }
    @PutMapping("/active-inactive")
    public ResponseEntity<?> activeInactive(@RequestBody ActiveInactiveDto request) {
        return masterLocationService.activeInactive(request);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createDataLocation(@RequestBody CreateMasterLocationDTO request) {
        return masterLocationService.createDataLocation(request);
    }

    @PostMapping("/validate-create")
    public ResponseEntity<?> validateCreateDataLocation(@RequestBody CreateMasterLocationDTO request) {
        return masterLocationService.validateCreateDataLocation(Boolean.TRUE, request);
    }

    @PutMapping("/update/{locationId}")
    public ResponseEntity<?> updateDataLocation(@PathVariable Integer locationId, @RequestBody UpdateMasterLocationDTO request) {
        return masterLocationService.updateDataLocation(locationId, request);
    }

    @PostMapping("/validate-update/{locationId}")
    public ResponseEntity<?> validateUpdateDataLocation(@PathVariable Integer locationId, @RequestBody UpdateMasterLocationDTO request) {
        return masterLocationService.validateUpdateDataLocation(Boolean.TRUE, locationId, request);
    }
}
