/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.module.account.config.controller;

import com.dbs.module.account.config.service.MasterConfigService;
import io.swagger.annotations.Api;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author RachmatY
 */
@RestController
@RequestMapping("/v1/dbs/api/master")
@Api(tags = "Master")
public class MasterConfigController {
    
    @Autowired
    private MasterConfigService services;
    
    @GetMapping("/config-file")
    public ResponseEntity<?> configFile() {
        return services.configFile();
    }
    
    @PostMapping("/validate-upload-url")
    public ResponseEntity<?> uploadImageUrl(@RequestParam(value = "image", required = true) MultipartFile image,
                                         HttpServletRequest httpServletRequest) {
        return services.validateUploadFileUrl(image);
    }
}
