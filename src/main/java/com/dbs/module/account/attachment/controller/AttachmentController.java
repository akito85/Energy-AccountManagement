/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.module.account.attachment.controller;

import com.dbs.module.account.attachment.services.AttachmentServiceImpl;
import io.swagger.annotations.Api;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/v1/dbs/api/attachment")
@Api(tags = "attachment")
public class AttachmentController {
    
    @Autowired
    private AttachmentServiceImpl services;
    
    @PostMapping(path = "/upload", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> testUpload(@RequestParam(value = "attachmentId", required = false) Integer attachmentId, 
            @RequestParam(value = "referensiId", required = true) Integer referensiId, 
            @RequestParam(value = "category", required = true) String category, 
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "fileCategoryId", required = true) Integer fileCategoryId,
            HttpServletRequest httpServletRequest) {
        return services.uploadAttachment(attachmentId, referensiId, category, file, fileCategoryId);
    }
}
