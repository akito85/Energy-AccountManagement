package com.dbs.module.account.detail.distributionmedia.controller;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.database.crm.entities.accountmanagement.VW_ACC_DIST_MEDIA;
import com.dbs.module.account.detail.distributionmedia.dto.DismeCreateRequestDTO;
import com.dbs.module.account.detail.distributionmedia.dto.DismeInactiveRequestDTO;
import com.dbs.module.account.detail.distributionmedia.service.DistributionMediaService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@RequestMapping("/v1/dbs/api/distribution-media")
@Api(tags = "Distribution_Media")
public class DistributionMediaController {
    
    @Autowired
    private DistributionMediaService service;

    @Autowired
    private DistributionMediaService dismeService;
    
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DismeCreateRequestDTO request) {
        return service.createDistributionMedia(request);
    }

    @PostMapping("/validate-create")
    public ResponseEntity<?> validateCreate(@RequestBody DismeCreateRequestDTO request) {
        return service.validateCreateDistributionMedia(request);
    }
    
    @GetMapping("/view/{accountId}")
    public ResponseEntity<?> view(@PathVariable Integer accountId, @Valid MaterialTablePagingRequest pagingData, PagedResourcesAssembler<VW_ACC_DIST_MEDIA> assembler) {
        return service.getListDistributionMedia(accountId, pagingData, assembler);
    }
    
    @PutMapping("/inactive")
    public ResponseEntity<?> activeInactive(@RequestBody DismeInactiveRequestDTO request) {
        return service.inactiveDistributionMedia(request);
    }
    
    @GetMapping("/getDistributionMedia")
	public ResponseEntity<?> viewDistributionMedia( HttpServletRequest httpServletRequest) {
		return service.viewDistributionMedia(httpServletRequest);
	}
}
