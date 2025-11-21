package com.dbs.module.account.detail.servicerequest.service;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.FlowStatus;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.database.crm.entities.accountmanagement.servicerequest.NX_M_INSTALLMENT;
import com.dbs.database.crm.entities.accountmanagement.servicerequest.NX_M_INSTALLMENT_SCHEDULE;
import com.dbs.database.crm.repositories.accountmanagement.servicerequest.NxMInstallmentRepo;
import com.dbs.database.crm.repositories.accountmanagement.servicerequest.NxMInstallmentScheduleRepo;
import com.dbs.module.account.detail.servicerequest.dto.InstallmentDTO;
import com.dbs.module.account.detail.servicerequest.dto.InstallmentScheduleDTO;
import com.dbs.module.account.detail.servicerequest.dto.InstallmentViewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing Installments
 */
@Service
public class InstallmentService {

    private static final Logger logger = LoggerFactory.getLogger(InstallmentService.class);

    @Autowired
    private NxMInstallmentRepo installmentRepo;

    @Autowired
    private NxMInstallmentScheduleRepo scheduleRepo;

    @Autowired
    private GlobalTypeValueService globalTypeService;

    /**
     * Create a new installment
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> create(InstallmentDTO request) {
        ResponseObject result;
        try {
            // Check duplicate installment number
            if (request.getInstallmentNumber() != null &&
                installmentRepo.existsByInstallmentNumber(request.getInstallmentNumber())) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                    "Installment number already exists", null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            NX_M_INSTALLMENT installment = new NX_M_INSTALLMENT();
            installment.setInstallmentNumber(request.getInstallmentNumber());
            installment.setTotalAmount(request.getTotalAmount());
            installment.setNumberOfInstallments(request.getNumberOfInstallments());
            installment.setStartDate(request.getStartDate());
            installment.setEndDate(request.getEndDate());
            installment.setInterestRate(request.getInterestRate());
            installment.setPaymentFrequency(request.getPaymentFrequency());
            installment.setInstallmentStatus(request.getInstallmentStatus());
            installment.setDownPayment(request.getDownPayment());

            installment.setCreatedBy(UserDetailUtils.getUsername());
            installment.setStatus(FlowStatus.ACTIVE.name());
            installment.setCreatedDate(new Date());

            NX_M_INSTALLMENT savedInstallment = installmentRepo.save(installment);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                ResponseUtils.MESSAGE_CREATED, savedInstallment);
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
     * Update installment
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> update(InstallmentDTO request) {
        ResponseObject result;
        try {
            Optional<NX_M_INSTALLMENT> data =
                installmentRepo.findByInstallmentId(request.getInstallmentId());

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    "Installment not found", null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            NX_M_INSTALLMENT installment = data.get();

            if (request.getInstallmentStatus() != null) {
                installment.setInstallmentStatus(request.getInstallmentStatus());
            }
            if (request.getEndDate() != null) {
                installment.setEndDate(request.getEndDate());
            }

            installment.setUpdatedBy(UserDetailUtils.getUsername());
            installment.setUpdatedDate(new Date());

            NX_M_INSTALLMENT savedInstallment = installmentRepo.save(installment);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                ResponseUtils.MESSAGE_UPDATED, savedInstallment);
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
     * Get installment detail
     */
    public ResponseEntity<ResponseObject> getDetail(Integer installmentId) {
        ResponseObject result;
        try {
            Optional<NX_M_INSTALLMENT> data = installmentRepo.findByInstallmentId(installmentId);

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            InstallmentViewDTO viewDTO = buildViewDTO(data.get());

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                "Success", viewDTO);
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
     * Get installment schedules
     */
    public ResponseEntity<ResponseObject> getSchedules(Integer installmentId) {
        ResponseObject result;
        try {
            List<NX_M_INSTALLMENT_SCHEDULE> schedules =
                scheduleRepo.findAllByInstallmentIdAndStatusOrderByInstallmentNumberAsc(
                    installmentId, FlowStatus.ACTIVE.name());

            List<InstallmentScheduleDTO> scheduleDTOs = new ArrayList<>();
            for (var schedule : schedules) {
                scheduleDTOs.add(buildScheduleDTO(schedule));
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                "Success", scheduleDTOs);
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
     * Build view DTO from entity
     */
    private InstallmentViewDTO buildViewDTO(NX_M_INSTALLMENT installment) {
        InstallmentViewDTO dto = new InstallmentViewDTO();
        dto.setInstallmentId(installment.getInstallmentId());
        dto.setInstallmentNumber(installment.getInstallmentNumber());
        dto.setTotalAmount(installment.getTotalAmount());
        dto.setNumberOfInstallments(installment.getNumberOfInstallments());
        dto.setStartDate(installment.getStartDate());
        dto.setEndDate(installment.getEndDate());
        dto.setInterestRate(installment.getInterestRate());
        dto.setPaymentFrequency(installment.getPaymentFrequency());
        dto.setInstallmentStatus(installment.getInstallmentStatus());
        dto.setDownPayment(installment.getDownPayment());
        dto.setStatus(installment.getStatus());
        dto.setCreatedBy(installment.getCreatedBy());
        dto.setCreatedDate(installment.getCreatedDate());

        // Get GLOBAL_TYPE names
        if (installment.getPaymentFrequency() != null) {
            var frequency = globalTypeService.getGlobalTypeByGlbTypeValId(
                "Payment Frequency", installment.getPaymentFrequency());
            if (frequency != null) {
                dto.setPaymentFrequencyName(frequency.getName());
            }
        }

        if (installment.getInstallmentStatus() != null) {
            var status = globalTypeService.getGlobalTypeByGlbTypeValId(
                "Installment Status", installment.getInstallmentStatus());
            if (status != null) {
                dto.setInstallmentStatusName(status.getName());
            }
        }

        return dto;
    }

    /**
     * Build schedule DTO from entity
     */
    private InstallmentScheduleDTO buildScheduleDTO(NX_M_INSTALLMENT_SCHEDULE schedule) {
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
