package com.dbs.module.account.detail.additionalinformation.controller;

import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.module.account.detail.additionalinformation.dto.CreateUpdateAddInfoDto;
import com.dbs.module.account.detail.additionalinformation.service.AdditionalInformationService;
import com.dbs.module.account.utils.ConstantAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/dbs/api/account-detail/additional-information")
public class AdditionalInformationController {

    @Autowired
    private AdditionalInformationService services;

    @GetMapping("/ddl/category")
    public ResponseEntity<ResponseObject> getDdlAdditionalInformationCategory() {
        return services.getDropDownListAdditionalInformation(ConstantAccount.DDL_INFORMATION_CATEGORY, null);
    }

    @GetMapping("/ddl/value/{parentValue}")
    public ResponseEntity<ResponseObject> getDdlAdditionalInformationValue(@PathVariable Integer parentValue) {
        return services.getDropDownListAdditionalInformation(ConstantAccount.DDL_INFORMATION_VALUE, parentValue);
    }
    @PostMapping("/create-update")
    public ResponseEntity<ResponseObject> createUpdateAdditionalInformation (@RequestBody CreateUpdateAddInfoDto createUpdateAddInfoDto, HttpServletRequest httpServletRequest) {
        return services.createUpdateAddInfo(createUpdateAddInfoDto, httpServletRequest);
    }

    @PostMapping("/validate-create-update")
    public ResponseEntity<ResponseObject> validateCreateUpdateAdditionalInformation (@RequestBody CreateUpdateAddInfoDto createUpdateAddInfoDto) {
        return services.validateCreateUpdate(createUpdateAddInfoDto);
    }

    @GetMapping("/view/{accountId}")
    public ResponseEntity<ResponseObject> viewAdditionalInformation (@PathVariable Integer accountId) {
        return services.viewAddInfo(accountId);
    }

    @PostMapping("/soft-delete/{id}")
    public ResponseEntity<ResponseObject> softDeleteAdditionalInformation(@PathVariable Integer id) {
        return services.softDelete(id);
    }
}
