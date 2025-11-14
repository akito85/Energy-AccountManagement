/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.module.account.attachment.services;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.database.crm.entities.usermanagement.M_ATTACHMENT;
import com.dbs.database.crm.entities.usermanagement.M_ENTITY;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.usermanagement.MAttachmentRepo;
import com.dbs.database.crm.repositories.usermanagement.MEntityRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import com.jlefebure.spring.boot.minio.MinioConfigurationProperties;
import io.minio.MinioClient;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
public class AttachmentServiceImpl {
    
    private static Logger logger = LoggerFactory.getLogger(AttachmentServiceImpl.class);
    
    @Autowired
    private MAttachmentRepo mAttachmentRepo;
    
    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;
    
    @Autowired
    private MEntityRepo mEntityRepo;
    
    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioConfigurationProperties configurationProperties;
    
    private static final String PATH ="PATH_08";
    
     @SuppressWarnings("deprecation")
	public ResponseEntity<ResponseObject> uploadAttachment(Integer attachmentId, 
             Integer referensiId, 
             String category, 
             MultipartFile file, 
             Integer fileCategoryId){
         ResponseObject result = new ResponseObject();
         try {
            String generatedFileName = UserDetailUtils.generateFileName(file.getOriginalFilename());
            M_ATTACHMENT mAttachment = null;
            if (attachmentId != null) {
                 Optional<M_ATTACHMENT> mAttOpt = mAttachmentRepo.findById(attachmentId);
                 if (mAttOpt.isPresent()) {
                    mAttachment = mAttOpt.get();
                    mAttachment.setCategory(category);
                    mAttachment.setReferenceId(referensiId);
                    mAttachment.setType(file.getContentType());
                    mAttachment.setCreatedBy(UserDetailUtils.getUsername());
                    mAttachment.setCreatedDate(new Date());
                    mAttachment.setPathFile(PATH);
                    mAttachment.setFileSize(file.getSize());
                    mAttachment.setFileName(generatedFileName);
                    mAttachment.setFileCategoryId(fileCategoryId);
                }
            } else {
                mAttachment = new M_ATTACHMENT();
                mAttachment.setCategory(category);
                mAttachment.setReferenceId(referensiId);
                mAttachment.setType(file.getContentType());
                mAttachment.setCreatedBy(UserDetailUtils.getUsername());
                mAttachment.setCreatedDate(new Date());
                mAttachment.setPathFile(PATH);
                mAttachment.setFileSize(file.getSize());
                mAttachment.setFileName(generatedFileName);
                mAttachment.setFileCategoryId(fileCategoryId);
            }
           if(mAttachment !=null) {
            mAttachmentRepo.save(mAttachment);
           }
            R_GLOBAL_TYPE_VALUE rGlobalTypeValue = rGlobalTypeValueRepo.findTopByGlbValueIgnoreCaseAndIsDeleted(PATH, false);
            String fullPath = rGlobalTypeValue.getName() + generatedFileName;

            M_ENTITY mEntity = mEntityRepo.findTopByEntityId(UserDetailUtils.getUserEntity());
            String fullobject = mEntity.getEntityName() + fullPath;
            this.minioClient.putObject(this.configurationProperties.getBucket(), fullobject, file.getInputStream(), file.getContentType());
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                    "Success upload attachment", null);
            
            return new ResponseEntity<>(result, result.getHttpCode());
         } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
         }
     }
    
     
     @SuppressWarnings("deprecation")
	public ResponseEntity<ResponseObject> uploadAttachmentWithPath(Integer attachmentId, 
             Integer referensiId, 
             String category, 
             MultipartFile file, 
             Integer fileCategoryId,
             String path,
             boolean isDraft){
         ResponseObject result = new ResponseObject();
         try {
            String generatedFileName = UserDetailUtils.generateFileName(file.getOriginalFilename());
            M_ATTACHMENT mAttachment = null;
            if (attachmentId != null) {
                 Optional<M_ATTACHMENT> mAttOpt = mAttachmentRepo.findById(attachmentId);
                 if (mAttOpt.isPresent()) {
                    mAttachment = mAttOpt.get();
                    mAttachment.setCategory(category);
                    mAttachment.setReferenceId(referensiId);
                    mAttachment.setType(file.getContentType());
                    mAttachment.setCreatedBy(UserDetailUtils.getUsername());
                    mAttachment.setCreatedDate(new Date());
                    mAttachment.setPathFile(path.toUpperCase());
                    mAttachment.setFileSize(file.getSize());
                    mAttachment.setFileName(generatedFileName);
                    mAttachment.setFileCategoryId(fileCategoryId);
                    mAttachment.setIsDraft(isDraft);
                }
            } else {
                mAttachment = new M_ATTACHMENT();
                mAttachment.setCategory(category);
                mAttachment.setReferenceId(referensiId);
                mAttachment.setType(file.getContentType());
                mAttachment.setCreatedBy(UserDetailUtils.getUsername());
                mAttachment.setCreatedDate(new Date());
                mAttachment.setPathFile(path.toUpperCase());
                mAttachment.setFileSize(file.getSize());
                mAttachment.setFileName(generatedFileName);
                mAttachment.setFileCategoryId(fileCategoryId);
                mAttachment.setIsDraft(isDraft);
            }
            if(mAttachment != null) {
                mAttachmentRepo.save(mAttachment);
            }
            R_GLOBAL_TYPE_VALUE rGlobalTypeValue = rGlobalTypeValueRepo.findTopByGlbValueIgnoreCaseAndIsDeleted(path, false);
            String fullPath = rGlobalTypeValue.getName() + generatedFileName;


            String fullobject = "FILE" + fullPath;
            this.minioClient.putObject(this.configurationProperties.getBucket(), fullobject, file.getInputStream(), file.getContentType());
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                    "Success upload attachment", null);
            
            return new ResponseEntity<>(result, result.getHttpCode());
         } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
         }
     }
    
     
     public ResponseEntity<ResponseObject> softDeleteAttachment(List<Integer> attachmentId, 
             Integer referensiId
             ){
         ResponseObject result = new ResponseObject();
         try {
            
              List<M_ATTACHMENT> mAttOpt = mAttachmentRepo.findByReferenceIdAndIdIn(referensiId, attachmentId);
                 if (!mAttOpt.isEmpty()) {
                	 for(M_ATTACHMENT mAttachment:mAttOpt) {
                		 
                		 mAttachment.setIsDeleted(Boolean.TRUE);
                		 mAttachment.setUpdatedBy(UserDetailUtils.getUsername());
                		 mAttachment.setUpdatedDate(new Date());
                		 mAttachmentRepo.save(mAttachment);
                	 }
                 }
           
               
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                    "Success soft delete attachment", null);
            
            return new ResponseEntity<>(result, result.getHttpCode());
         } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
         }
     }

}
