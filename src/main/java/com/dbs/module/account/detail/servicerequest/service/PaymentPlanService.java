package com.dbs.module.account.detail.servicerequest.service;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.library.utils.FlowStatus;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.database.crm.entities.accountmanagement.servicerequest.NX_M_INSTALLMENT_PAYMENT_PLAN;
import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import com.dbs.database.crm.repositories.accountmanagement.servicerequest.NxMInstallmentPaymentPlanRepo;
import com.dbs.database.crm.repositories.usermanagement.AuditTrailRepo;
import com.dbs.module.account.detail.servicerequest.dto.InstallmentPaymentPlanDTO;
import com.dbs.module.account.detail.servicerequest.dto.PaymentPlanCreateDTO;
import com.dbs.module.account.detail.servicerequest.dto.PaymentPlanUpdateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;
import java.util.*;

/**
 * Service for managing Payment Plans
 */
@Service
public class PaymentPlanService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentPlanService.class);

    @Autowired
    private NxMInstallmentPaymentPlanRepo paymentPlanRepo;

    @Autowired
    private AuditTrailRepo auditTrailRepo;

    @Autowired
    private GlobalTypeValueService globalTypeService;

    @Autowired
    private Validator validator;

    /**
     * Create a new payment plan
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> create(PaymentPlanCreateDTO request) {
        ResponseObject result;
        try {
            // Validate
            ResponseEntity<ResponseObject> validate = validateCreate(request);
            if (validate != null) {
                return validate;
            }

            NX_M_INSTALLMENT_PAYMENT_PLAN paymentPlan = new NX_M_INSTALLMENT_PAYMENT_PLAN();
            paymentPlan.setInstallmentId(request.getInstallmentId());
            paymentPlan.setPlanName(request.getPlanName());
            paymentPlan.setPlanType(request.getPlanType());
            paymentPlan.setDescription(request.getDescription());
            paymentPlan.setTermsAndConditions(request.getTermsAndConditions());
            paymentPlan.setAutoDebit(request.getAutoDebit());
            paymentPlan.setPaymentMethod(request.getPaymentMethod());

            paymentPlan.setCreatedBy(UserDetailUtils.getUsername());
            paymentPlan.setStatus(FlowStatus.ACTIVE.name());
            paymentPlan.setCreatedDate(new Date());

            NX_M_INSTALLMENT_PAYMENT_PLAN saved = paymentPlanRepo.save(paymentPlan);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                ResponseUtils.MESSAGE_CREATED, saved);
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
     * Update payment plan
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> update(PaymentPlanUpdateDTO request) {
        ResponseObject result;
        try {
            Optional<NX_M_INSTALLMENT_PAYMENT_PLAN> data =
                paymentPlanRepo.findByPaymentPlanId(request.getPaymentPlanId());

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    "Payment plan not found", null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            NX_M_INSTALLMENT_PAYMENT_PLAN paymentPlan = data.get();

            // Create audit trail
            AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
            ObjectMapper mapper = new ObjectMapper();
            String oldValue = mapper.writeValueAsString(paymentPlan);
            auditTrail.setOldValue(oldValue);

            // Update fields
            if (request.getPlanName() != null) {
                paymentPlan.setPlanName(request.getPlanName());
            }
            if (request.getDescription() != null) {
                paymentPlan.setDescription(request.getDescription());
            }
            if (request.getTermsAndConditions() != null) {
                paymentPlan.setTermsAndConditions(request.getTermsAndConditions());
            }
            if (request.getAutoDebit() != null) {
                paymentPlan.setAutoDebit(request.getAutoDebit());
            }
            if (request.getPaymentMethod() != null) {
                paymentPlan.setPaymentMethod(request.getPaymentMethod());
            }

            paymentPlan.setUpdatedBy(UserDetailUtils.getUsername());
            paymentPlan.setUpdatedDate(new Date());

            NX_M_INSTALLMENT_PAYMENT_PLAN saved = paymentPlanRepo.save(paymentPlan);

            // Save audit trail
            String newValue = mapper.writeValueAsString(saved);
            auditTrail.setNewValue(newValue);
            auditTrail.setOperation("UPDATE_PAYMENT_PLAN");
            auditTrail.setTableName("NX_M_INSTALLMENT_PAYMENT_PLAN");
            auditTrail.setDataId(saved.getPaymentPlanId().toString());
            auditTrail.setCreatedBy(UserDetailUtils.getUsername());
            auditTrail.setCreatedDate(new Date());
            auditTrailRepo.save(auditTrail);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                ResponseUtils.MESSAGE_UPDATED, saved);
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
     * Get payment plan detail
     */
    public ResponseEntity<ResponseObject> getDetail(Integer paymentPlanId) {
        ResponseObject result;
        try {
            Optional<NX_M_INSTALLMENT_PAYMENT_PLAN> data =
                paymentPlanRepo.findByPaymentPlanId(paymentPlanId);

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            InstallmentPaymentPlanDTO dto = buildDTO(data.get());

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                "Success", dto);
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
     * Get payment plans by installment
     */
    public ResponseEntity<ResponseObject> getByInstallment(Integer installmentId) {
        ResponseObject result;
        try {
            List<NX_M_INSTALLMENT_PAYMENT_PLAN> plans =
                paymentPlanRepo.findAllByInstallmentIdAndStatus(
                    installmentId, FlowStatus.ACTIVE.name());

            List<InstallmentPaymentPlanDTO> dtos = new ArrayList<>();
            for (var plan : plans) {
                dtos.add(buildDTO(plan));
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                "Success", dtos);
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
     * Get payment plans by plan type
     */
    public ResponseEntity<ResponseObject> getByPlanType(Integer planType) {
        ResponseObject result;
        try {
            List<NX_M_INSTALLMENT_PAYMENT_PLAN> plans =
                paymentPlanRepo.findAllByPlanTypeAndStatus(planType, FlowStatus.ACTIVE.name());

            List<InstallmentPaymentPlanDTO> dtos = new ArrayList<>();
            for (var plan : plans) {
                dtos.add(buildDTO(plan));
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                "Success", dtos);
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
     * Toggle active/inactive status
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> toggleStatus(Integer paymentPlanId) {
        ResponseObject result;
        try {
            Optional<NX_M_INSTALLMENT_PAYMENT_PLAN> data =
                paymentPlanRepo.findByPaymentPlanId(paymentPlanId);

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            NX_M_INSTALLMENT_PAYMENT_PLAN paymentPlan = data.get();

            // Toggle status
            if (paymentPlan.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())) {
                paymentPlan.setStatus(FlowStatus.INACTIVE.name());
            } else {
                paymentPlan.setStatus(FlowStatus.ACTIVE.name());
            }

            paymentPlan.setUpdatedDate(new Date());
            paymentPlan.setUpdatedBy(UserDetailUtils.getUsername());

            NX_M_INSTALLMENT_PAYMENT_PLAN saved = paymentPlanRepo.save(paymentPlan);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                ResponseUtils.MESSAGE_UPDATED, saved);
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

    // ==================== Private Helper Methods ====================

    /**
     * Validate create request
     */
    private ResponseEntity<ResponseObject> validateCreate(PaymentPlanCreateDTO request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            List<Map<String, Object>> violationList = new ArrayList<>();
            for (var violation : violations) {
                Map<String, Object> data = new HashMap<>();
                data.put(violation.getPropertyPath().toString(), violation.getMessage());
                violationList.add(data);
            }
            ResponseObject result = new ResponseObject(
                ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                "Validation failed", violationList);
            return new ResponseEntity<>(result, result.getHttpCode());
        }
        return null;
    }

    /**
     * Build DTO from entity
     */
    private InstallmentPaymentPlanDTO buildDTO(NX_M_INSTALLMENT_PAYMENT_PLAN paymentPlan) {
        InstallmentPaymentPlanDTO dto = new InstallmentPaymentPlanDTO();
        dto.setPaymentPlanId(paymentPlan.getPaymentPlanId());
        dto.setInstallmentId(paymentPlan.getInstallmentId());
        dto.setPlanName(paymentPlan.getPlanName());
        dto.setPlanType(paymentPlan.getPlanType());
        dto.setDescription(paymentPlan.getDescription());
        dto.setTermsAndConditions(paymentPlan.getTermsAndConditions());
        dto.setAutoDebit(paymentPlan.getAutoDebit());
        dto.setPaymentMethod(paymentPlan.getPaymentMethod());

        // Get GLOBAL_TYPE names
        if (paymentPlan.getPlanType() != null) {
            var type = globalTypeService.getGlobalTypeByGlbTypeValId(
                "Payment Plan Type", paymentPlan.getPlanType());
            if (type != null) {
                dto.setPlanTypeName(type.getName());
            }
        }

        if (paymentPlan.getPaymentMethod() != null) {
            var method = globalTypeService.getGlobalTypeByGlbTypeValId(
                "Payment Method", paymentPlan.getPaymentMethod());
            if (method != null) {
                dto.setPaymentMethodName(method.getName());
            }
        }

        return dto;
    }
}
