package com.dbs.module.account.detail.servicerequest.service;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.library.utils.FlowStatus;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.database.crm.entities.accountmanagement.servicerequest.NX_M_INSTALLMENT_SCHEDULE;
import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import com.dbs.database.crm.repositories.accountmanagement.servicerequest.NxMInstallmentScheduleRepo;
import com.dbs.database.crm.repositories.usermanagement.AuditTrailRepo;
import com.dbs.module.account.detail.servicerequest.dto.InstallmentScheduleDTO;
import com.dbs.module.account.detail.servicerequest.dto.ScheduleCreateDTO;
import com.dbs.module.account.detail.servicerequest.dto.ScheduleUpdateDTO;
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
 * Service for managing Installment Schedules
 */
@Service
public class ScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    @Autowired
    private NxMInstallmentScheduleRepo scheduleRepo;

    @Autowired
    private AuditTrailRepo auditTrailRepo;

    @Autowired
    private GlobalTypeValueService globalTypeService;

    @Autowired
    private Validator validator;

    /**
     * Create a new installment schedule
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> create(ScheduleCreateDTO request) {
        ResponseObject result;
        try {
            // Validate
            ResponseEntity<ResponseObject> validate = validateCreate(request);
            if (validate != null) {
                return validate;
            }

            // Check if schedule already exists for this installment number
            Optional<NX_M_INSTALLMENT_SCHEDULE> existing =
                scheduleRepo.findByInstallmentIdAndInstallmentNumber(
                    request.getInstallmentId(), request.getInstallmentNumber());

            if (existing.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                    "Schedule already exists for this installment number", null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            NX_M_INSTALLMENT_SCHEDULE schedule = new NX_M_INSTALLMENT_SCHEDULE();
            schedule.setInstallmentId(request.getInstallmentId());
            schedule.setInstallmentNumber(request.getInstallmentNumber());
            schedule.setDueDate(request.getDueDate());
            schedule.setAmount(request.getAmount());
            schedule.setPrincipalAmount(request.getPrincipalAmount());
            schedule.setInterestAmount(request.getInterestAmount());
            schedule.setPaidAmount(request.getPaidAmount());
            schedule.setPaymentDate(request.getPaymentDate());
            schedule.setPaymentStatus(request.getPaymentStatus());
            schedule.setLateFee(request.getLateFee());
            schedule.setNotes(request.getNotes());

            schedule.setCreatedBy(UserDetailUtils.getUsername());
            schedule.setStatus(FlowStatus.ACTIVE.name());
            schedule.setCreatedDate(new Date());

            NX_M_INSTALLMENT_SCHEDULE saved = scheduleRepo.save(schedule);

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
     * Update installment schedule
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> update(ScheduleUpdateDTO request) {
        ResponseObject result;
        try {
            Optional<NX_M_INSTALLMENT_SCHEDULE> data =
                scheduleRepo.findByScheduleId(request.getScheduleId());

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    "Schedule not found", null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            NX_M_INSTALLMENT_SCHEDULE schedule = data.get();

            // Create audit trail
            AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
            ObjectMapper mapper = new ObjectMapper();
            String oldValue = mapper.writeValueAsString(schedule);
            auditTrail.setOldValue(oldValue);

            // Update fields
            if (request.getDueDate() != null) {
                schedule.setDueDate(request.getDueDate());
            }
            if (request.getAmount() != null) {
                schedule.setAmount(request.getAmount());
            }
            if (request.getPrincipalAmount() != null) {
                schedule.setPrincipalAmount(request.getPrincipalAmount());
            }
            if (request.getInterestAmount() != null) {
                schedule.setInterestAmount(request.getInterestAmount());
            }
            if (request.getPaidAmount() != null) {
                schedule.setPaidAmount(request.getPaidAmount());
            }
            if (request.getPaymentDate() != null) {
                schedule.setPaymentDate(request.getPaymentDate());
            }
            if (request.getPaymentStatus() != null) {
                schedule.setPaymentStatus(request.getPaymentStatus());
            }
            if (request.getLateFee() != null) {
                schedule.setLateFee(request.getLateFee());
            }
            if (request.getNotes() != null) {
                schedule.setNotes(request.getNotes());
            }

            schedule.setUpdatedBy(UserDetailUtils.getUsername());
            schedule.setUpdatedDate(new Date());

            NX_M_INSTALLMENT_SCHEDULE saved = scheduleRepo.save(schedule);

            // Save audit trail
            String newValue = mapper.writeValueAsString(saved);
            auditTrail.setNewValue(newValue);
            auditTrail.setOperation("UPDATE_SCHEDULE");
            auditTrail.setTableName("NX_M_INSTALLMENT_SCHEDULE");
            auditTrail.setDataId(saved.getScheduleId().toString());
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
     * Get schedule detail
     */
    public ResponseEntity<ResponseObject> getDetail(Integer scheduleId) {
        ResponseObject result;
        try {
            Optional<NX_M_INSTALLMENT_SCHEDULE> data =
                scheduleRepo.findByScheduleId(scheduleId);

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            InstallmentScheduleDTO dto = buildDTO(data.get());

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
     * Get schedules by installment
     */
    public ResponseEntity<ResponseObject> getByInstallment(Integer installmentId) {
        ResponseObject result;
        try {
            List<NX_M_INSTALLMENT_SCHEDULE> schedules =
                scheduleRepo.findAllByInstallmentIdAndStatusOrderByInstallmentNumberAsc(
                    installmentId, FlowStatus.ACTIVE.name());

            List<InstallmentScheduleDTO> dtos = new ArrayList<>();
            for (var schedule : schedules) {
                dtos.add(buildDTO(schedule));
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
     * Get schedules by payment status
     */
    public ResponseEntity<ResponseObject> getByPaymentStatus(Integer paymentStatus) {
        ResponseObject result;
        try {
            List<NX_M_INSTALLMENT_SCHEDULE> schedules =
                scheduleRepo.findAllByPaymentStatusAndStatus(
                    paymentStatus, FlowStatus.ACTIVE.name());

            List<InstallmentScheduleDTO> dtos = new ArrayList<>();
            for (var schedule : schedules) {
                dtos.add(buildDTO(schedule));
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
     * Record payment for a schedule
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> recordPayment(Integer scheduleId,
                                                         ScheduleUpdateDTO paymentInfo) {
        ResponseObject result;
        try {
            Optional<NX_M_INSTALLMENT_SCHEDULE> data =
                scheduleRepo.findByScheduleId(scheduleId);

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    "Schedule not found", null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            NX_M_INSTALLMENT_SCHEDULE schedule = data.get();

            schedule.setPaidAmount(paymentInfo.getPaidAmount());
            schedule.setPaymentDate(paymentInfo.getPaymentDate() != null ?
                paymentInfo.getPaymentDate() : new Date());
            schedule.setPaymentStatus(paymentInfo.getPaymentStatus());
            schedule.setNotes(paymentInfo.getNotes());

            schedule.setUpdatedBy(UserDetailUtils.getUsername());
            schedule.setUpdatedDate(new Date());

            NX_M_INSTALLMENT_SCHEDULE saved = scheduleRepo.save(schedule);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                "Payment recorded successfully", saved);
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
    public ResponseEntity<ResponseObject> toggleStatus(Integer scheduleId) {
        ResponseObject result;
        try {
            Optional<NX_M_INSTALLMENT_SCHEDULE> data =
                scheduleRepo.findByScheduleId(scheduleId);

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            NX_M_INSTALLMENT_SCHEDULE schedule = data.get();

            // Toggle status
            if (schedule.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())) {
                schedule.setStatus(FlowStatus.INACTIVE.name());
            } else {
                schedule.setStatus(FlowStatus.ACTIVE.name());
            }

            schedule.setUpdatedDate(new Date());
            schedule.setUpdatedBy(UserDetailUtils.getUsername());

            NX_M_INSTALLMENT_SCHEDULE saved = scheduleRepo.save(schedule);

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
    private ResponseEntity<ResponseObject> validateCreate(ScheduleCreateDTO request) {
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
    private InstallmentScheduleDTO buildDTO(NX_M_INSTALLMENT_SCHEDULE schedule) {
        InstallmentScheduleDTO dto = new InstallmentScheduleDTO();
        dto.setScheduleId(schedule.getScheduleId());
        dto.setInstallmentId(schedule.getInstallmentId());
        dto.setInstallmentNumber(schedule.getInstallmentNumber());
        dto.setDueDate(schedule.getDueDate());
        dto.setAmount(schedule.getAmount());
        dto.setPrincipalAmount(schedule.getPrincipalAmount());
        dto.setInterestAmount(schedule.getInterestAmount());
        dto.setPaidAmount(schedule.getPaidAmount());
        dto.setPaymentDate(schedule.getPaymentDate());
        dto.setPaymentStatus(schedule.getPaymentStatus());
        dto.setLateFee(schedule.getLateFee());
        dto.setNotes(schedule.getNotes());

        // Get GLOBAL_TYPE name for payment status
        if (schedule.getPaymentStatus() != null) {
            var status = globalTypeService.getGlobalTypeByGlbTypeValId(
                "Payment Status", schedule.getPaymentStatus());
            if (status != null) {
                dto.setPaymentStatusName(status.getName());
            }
        }

        return dto;
    }
}
