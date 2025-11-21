package com.dbs.module.account.detail.servicerequest.service;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.library.utils.FlowStatus;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.database.crm.entities.accountmanagement.servicerequest.NX_R_SR_DETAILS;
import com.dbs.database.crm.repositories.accountmanagement.servicerequest.NxRSrDetailsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Service for managing Service Request Details relationships
 */
@Service
public class SRDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(SRDetailsService.class);

    @Autowired
    private NxRSrDetailsRepo srDetailsRepo;

    /**
     * Link work order to service request
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> linkWorkOrder(Integer serviceRequestId,
                                                         Integer accountId,
                                                         Integer workOrderId) {
        ResponseObject result;
        try {
            NX_R_SR_DETAILS srDetails = new NX_R_SR_DETAILS();
            srDetails.setServiceRequestId(serviceRequestId);
            srDetails.setAccountId(accountId);
            srDetails.setWorkOrderId(workOrderId);
            srDetails.setIsPrimary(false);
            srDetails.setCreatedBy(UserDetailUtils.getUsername());
            srDetails.setStatus(FlowStatus.ACTIVE.name());
            srDetails.setCreatedDate(new Date());

            NX_R_SR_DETAILS saved = srDetailsRepo.save(srDetails);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                "Work order linked successfully", saved);
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                new ResponseObject(ResponseUtils.SUCCESS_FALSE,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Link installment to service request
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> linkInstallment(Integer serviceRequestId,
                                                           Integer accountId,
                                                           Integer installmentId) {
        ResponseObject result;
        try {
            NX_R_SR_DETAILS srDetails = new NX_R_SR_DETAILS();
            srDetails.setServiceRequestId(serviceRequestId);
            srDetails.setAccountId(accountId);
            srDetails.setInstallmentId(installmentId);
            srDetails.setIsPrimary(false);
            srDetails.setCreatedBy(UserDetailUtils.getUsername());
            srDetails.setStatus(FlowStatus.ACTIVE.name());
            srDetails.setCreatedDate(new Date());

            NX_R_SR_DETAILS saved = srDetailsRepo.save(srDetails);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                "Installment linked successfully", saved);
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                new ResponseObject(ResponseUtils.SUCCESS_FALSE,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Link attachment to service request
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> linkAttachment(Integer serviceRequestId,
                                                          Integer accountId,
                                                          Integer attachmentId) {
        ResponseObject result;
        try {
            NX_R_SR_DETAILS srDetails = new NX_R_SR_DETAILS();
            srDetails.setServiceRequestId(serviceRequestId);
            srDetails.setAccountId(accountId);
            srDetails.setAttachmentId(attachmentId);
            srDetails.setIsPrimary(false);
            srDetails.setCreatedBy(UserDetailUtils.getUsername());
            srDetails.setStatus(FlowStatus.ACTIVE.name());
            srDetails.setCreatedDate(new Date());

            NX_R_SR_DETAILS saved = srDetailsRepo.save(srDetails);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                "Attachment linked successfully", saved);
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                new ResponseObject(ResponseUtils.SUCCESS_FALSE,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Link prerequisite to service request
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> linkPrerequisite(Integer serviceRequestId,
                                                            Integer accountId,
                                                            Integer prerequisiteId) {
        ResponseObject result;
        try {
            NX_R_SR_DETAILS srDetails = new NX_R_SR_DETAILS();
            srDetails.setServiceRequestId(serviceRequestId);
            srDetails.setAccountId(accountId);
            srDetails.setPrerequisiteId(prerequisiteId);
            srDetails.setIsPrimary(false);
            srDetails.setCreatedBy(UserDetailUtils.getUsername());
            srDetails.setStatus(FlowStatus.ACTIVE.name());
            srDetails.setCreatedDate(new Date());

            NX_R_SR_DETAILS saved = srDetailsRepo.save(srDetails);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                "Prerequisite linked successfully", saved);
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                new ResponseObject(ResponseUtils.SUCCESS_FALSE,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Link data requirement to service request
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> linkDataRequirement(Integer serviceRequestId,
                                                               Integer accountId,
                                                               Integer dataRequirementId) {
        ResponseObject result;
        try {
            NX_R_SR_DETAILS srDetails = new NX_R_SR_DETAILS();
            srDetails.setServiceRequestId(serviceRequestId);
            srDetails.setAccountId(accountId);
            srDetails.setDataRequirementId(dataRequirementId);
            srDetails.setIsPrimary(false);
            srDetails.setCreatedBy(UserDetailUtils.getUsername());
            srDetails.setStatus(FlowStatus.ACTIVE.name());
            srDetails.setCreatedDate(new Date());

            NX_R_SR_DETAILS saved = srDetailsRepo.save(srDetails);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                "Data requirement linked successfully", saved);
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                new ResponseObject(ResponseUtils.SUCCESS_FALSE,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all relationships for a service request
     */
    public ResponseEntity<ResponseObject> getRelationships(Integer serviceRequestId) {
        ResponseObject result;
        try {
            var details = srDetailsRepo.findAllByServiceRequestId(serviceRequestId);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                "Success", details);
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                new ResponseObject(ResponseUtils.SUCCESS_FALSE,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
