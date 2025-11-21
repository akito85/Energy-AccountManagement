/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.module.account.config.service;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalPropertiesService;
import com.dbs.common.library.utils.dto.ValidateRespDTO;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_PROPERTIES_DTL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author RachmatY
 */
@Service
public class MasterConfigService {
    
    private static final Logger logger = LoggerFactory.getLogger(MasterConfigService.class);
    
    @Autowired
    private GlobalPropertiesService globalPropertiesService;
    
    public ResponseEntity<ResponseObject> configFile() {
        ResponseObject result;
        try {
            List<R_GLOBAL_PROPERTIES_DTL> rgpOptional = globalPropertiesService.getGlobalPropertieses("SECURITY");
            Optional<R_GLOBAL_PROPERTIES_DTL> rgpExtOptional = rgpOptional.stream()
                            .filter(e -> e.getGpdKey().equalsIgnoreCase("FILE_EXT_ATT"))
                            .findFirst();
            Optional<R_GLOBAL_PROPERTIES_DTL> rgpSizeOptional = rgpOptional.stream()
                            .filter(e -> e.getGpdKey().equalsIgnoreCase("FILE_SIZE"))
                            .findFirst();
            LinkedHashMap<String, Object> fileConfig = new LinkedHashMap<>();
            
            fileConfig.put("fileExt", rgpExtOptional.isPresent() ? rgpExtOptional.get().getGpdVal() : null);
            fileConfig.put("size", rgpSizeOptional.isPresent() ? rgpSizeOptional.get().getGpdVal() : null);
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_OK, fileConfig);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
           logger.error(Constant.LOG_ERROR + e.getMessage());
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public ResponseEntity<ResponseObject> validateUploadFileUrl(MultipartFile file) {
        try {
            if (file != null) {
                ValidateRespDTO validateFile = validateFileAttachment(file);
                if (validateFile.getStatus()) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            validateFile.getMessage(), ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
                } else {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                      ResponseUtils.MESSAGE_SUCCESS, ResponseUtils.MESSAGE_SUCCESS), HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            null, ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage());
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }
    
    private ValidateRespDTO validateFileAttachment(MultipartFile document){
        ValidateRespDTO validate = new ValidateRespDTO();
        List<R_GLOBAL_PROPERTIES_DTL> rgpOptional = globalPropertiesService.getGlobalPropertieses("SECURITY");
        Optional<R_GLOBAL_PROPERTIES_DTL> rgpExtOptional = rgpOptional.stream()
                            .filter(e -> e.getGpdKey().equalsIgnoreCase("FILE_EXT_ATT"))
                            .findFirst();
        
        String[] exts = rgpExtOptional.isPresent() ? rgpExtOptional.get().getGpdVal().split(",") : null;
        String extension = FilenameUtils.getExtension(document.getOriginalFilename()).toUpperCase();
        logger.info("extension", extension);
        List<String> strs = new ArrayList<>();
        if (exts != null && exts.length > 0) {
            strs.addAll(Arrays.asList(exts));
            Optional<String> optExt = strs.stream().filter(e->e.toUpperCase().equals(extension)).findFirst();
            if (optExt.isEmpty()) {
                validate.setStatus(Boolean.TRUE);
                validate.setMessage("Format file not valid");
                return validate;
            }
        }
        
        
        Optional<R_GLOBAL_PROPERTIES_DTL> rgpFileSizeOptional = rgpOptional.stream()
                            .filter(e -> e.getGpdKey().equalsIgnoreCase("FILE_SIZE"))
                            .findFirst();
        long size = rgpFileSizeOptional.isPresent() ? Long.valueOf(rgpFileSizeOptional.get().getGpdVal()) : 0;
        long sizeInMb = document.getSize() / (1024 * 1024);
        if (sizeInMb > size) {
            validate.setStatus(Boolean.TRUE);
            validate.setMessage("File size not valid, file too large!");
            return validate;
        }
        validate.setStatus(Boolean.FALSE);
        return validate;
    }
    
}
