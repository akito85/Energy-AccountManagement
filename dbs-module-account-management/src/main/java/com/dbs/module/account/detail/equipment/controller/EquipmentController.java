package com.dbs.module.account.detail.equipment.controller;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.database.crm.entities.accountmanagement.M_AM_EQUIPMENT;
import com.dbs.database.crm.entities.accountmanagement.view.VW_AM_EQUIPMENT;
import com.dbs.module.account.detail.equipment.dto.EquimentCreateUpdateDTO;
import com.dbs.module.account.detail.equipment.service.EquipmentService;
import com.dbs.module.account.utils.ConstantAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/dbs/api/account-detail/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService service;

    @PostMapping("/create-update")
    public ResponseEntity<?> createUpdateEquipment(@RequestBody EquimentCreateUpdateDTO request) {
        return service.createUpdateEquipment(request);
    }

    @PostMapping("/validate-create-update")
    public ResponseEntity<?> validateCreateUpdateEquipment(@RequestBody EquimentCreateUpdateDTO request) {
        return service.validateCreateUpdateEquipment(Boolean.TRUE, request);
    }

    @GetMapping("/view-paging/{accountId}")
    public ResponseEntity<?> viewPagingEquipment(@PathVariable Integer accountId, @Valid MaterialTablePagingRequest pagingData, PagedResourcesAssembler<VW_AM_EQUIPMENT> assembler) {
        return service.viewPagingEquipment(accountId,pagingData, assembler);
    }

    @GetMapping("/view-detail/{id}")
    public ResponseEntity<?> viewDetailEquipment(@PathVariable Integer id) {
        return service.viewDetailEquipment(id);
    }

    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<?> softDeleteEquipment(@PathVariable Integer id) {
        return service.softDeleteEquipment(id);
    }

    // FOR EVERY DDL IN EQUIPMENT
    @GetMapping("/drop-down-list/{groupName}")
    public ResponseEntity<ResponseObject> getDdlUtilizationName(@PathVariable String groupName) {
        switch (groupName) {
            case "name":
                return service.getDropDownListEquipment("Equipment Name");
            case "type":
                return service.getDropDownListEquipment("Equipment Type");
            case "brand":
                return service.getDropDownListEquipment("Equipment Brand");
            case "quantity-uom":
                return service.getDropDownListEquipment("Equipment Quantity UOM");
            case "capacity-uom":
                return service.getDropDownListEquipment("Equipment Capacity UOM");
            case "energy-consumption-uom":
                return service.getDropDownListEquipment("Equipment Energy Consumption UOM");
            case "gas-conversion-uom":
                return service.getDropDownListEquipment("Equipment Gas Conversion UOM");
            case "fuel-type":
                return service.getDropDownListEquipment("Equipment Fuel Type");
            default:
                return ResponseEntity.notFound().build();
        }
    }

}
