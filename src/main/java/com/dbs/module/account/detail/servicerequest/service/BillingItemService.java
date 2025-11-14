package com.dbs.module.account.detail.servicerequest.service;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.FlowStatus;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.database.crm.entities.accountmanagement.servicerequest.NX_M_INSTALLMENT_BILLING_ITEM;
import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import com.dbs.database.crm.repositories.accountmanagement.servicerequest.NxMInstallmentBillingItemRepo;
import com.dbs.database.crm.repositories.usermanagement.AuditTrailRepo;
import com.dbs.module.account.detail.servicerequest.dto.BillingItemCreateDTO;
import com.dbs.module.account.detail.servicerequest.dto.BillingItemUpdateDTO;
import com.dbs.module.account.detail.servicerequest.dto.InstallmentBillingItemDTO;
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
 * Service for managing Billing Items
 */
@Service
public class BillingItemService {

    private static final Logger logger = LoggerFactory.getLogger(BillingItemService.class);

    @Autowired
    private NxMInstallmentBillingItemRepo billingItemRepo;

    @Autowired
    private AuditTrailRepo auditTrailRepo;

    @Autowired
    private GlobalTypeValueService globalTypeService;

    @Autowired
    private Validator validator;

    /**
     * Create a new billing item
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> create(BillingItemCreateDTO request) {
        ResponseObject result;
        try {
            // Validate
            ResponseEntity<ResponseObject> validate = validateCreate(request);
            if (validate != null) {
                return validate;
            }

            NX_M_INSTALLMENT_BILLING_ITEM billingItem = new NX_M_INSTALLMENT_BILLING_ITEM();
            billingItem.setInstallmentId(request.getInstallmentId());
            billingItem.setItemName(request.getItemName());
            billingItem.setItemDescription(request.getItemDescription());
            billingItem.setItemType(request.getItemType());
            billingItem.setAmount(request.getAmount());
            billingItem.setQuantity(request.getQuantity());
            billingItem.setUnitPrice(request.getUnitPrice());
            billingItem.setTaxAmount(request.getTaxAmount());
            billingItem.setDiscountAmount(request.getDiscountAmount());
            billingItem.setNetAmount(request.getNetAmount());

            billingItem.setCreatedBy(UserDetailUtils.getUsername());
            billingItem.setStatus(FlowStatus.ACTIVE.name());
            billingItem.setCreatedDate(new Date());

            NX_M_INSTALLMENT_BILLING_ITEM saved = billingItemRepo.save(billingItem);

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
     * Update billing item
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> update(BillingItemUpdateDTO request) {
        ResponseObject result;
        try {
            Optional<NX_M_INSTALLMENT_BILLING_ITEM> data =
                billingItemRepo.findByBillingItemId(request.getBillingItemId());

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    "Billing item not found", null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            NX_M_INSTALLMENT_BILLING_ITEM billingItem = data.get();

            // Create audit trail
            AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
            ObjectMapper mapper = new ObjectMapper();
            String oldValue = mapper.writeValueAsString(billingItem);
            auditTrail.setOldValue(oldValue);

            // Update fields
            if (request.getItemName() != null) {
                billingItem.setItemName(request.getItemName());
            }
            if (request.getItemDescription() != null) {
                billingItem.setItemDescription(request.getItemDescription());
            }
            if (request.getItemType() != null) {
                billingItem.setItemType(request.getItemType());
            }
            if (request.getAmount() != null) {
                billingItem.setAmount(request.getAmount());
            }
            if (request.getQuantity() != null) {
                billingItem.setQuantity(request.getQuantity());
            }
            if (request.getUnitPrice() != null) {
                billingItem.setUnitPrice(request.getUnitPrice());
            }
            if (request.getTaxAmount() != null) {
                billingItem.setTaxAmount(request.getTaxAmount());
            }
            if (request.getDiscountAmount() != null) {
                billingItem.setDiscountAmount(request.getDiscountAmount());
            }
            if (request.getNetAmount() != null) {
                billingItem.setNetAmount(request.getNetAmount());
            }

            billingItem.setUpdatedBy(UserDetailUtils.getUsername());
            billingItem.setUpdatedDate(new Date());

            NX_M_INSTALLMENT_BILLING_ITEM saved = billingItemRepo.save(billingItem);

            // Save audit trail
            String newValue = mapper.writeValueAsString(saved);
            auditTrail.setNewValue(newValue);
            auditTrail.setOperation("UPDATE_BILLING_ITEM");
            auditTrail.setTableName("NX_M_INSTALLMENT_BILLING_ITEM");
            auditTrail.setDataId(saved.getBillingItemId().toString());
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
     * Get billing item detail
     */
    public ResponseEntity<ResponseObject> getDetail(Integer billingItemId) {
        ResponseObject result;
        try {
            Optional<NX_M_INSTALLMENT_BILLING_ITEM> data =
                billingItemRepo.findByBillingItemId(billingItemId);

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            InstallmentBillingItemDTO dto = buildDTO(data.get());

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
     * Get billing items by installment
     */
    public ResponseEntity<ResponseObject> getByInstallment(Integer installmentId) {
        ResponseObject result;
        try {
            List<NX_M_INSTALLMENT_BILLING_ITEM> items =
                billingItemRepo.findAllByInstallmentIdAndStatus(
                    installmentId, FlowStatus.ACTIVE.name());

            List<InstallmentBillingItemDTO> dtos = new ArrayList<>();
            for (var item : items) {
                dtos.add(buildDTO(item));
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
     * Get billing items by item type
     */
    public ResponseEntity<ResponseObject> getByItemType(Integer itemType) {
        ResponseObject result;
        try {
            List<NX_M_INSTALLMENT_BILLING_ITEM> items =
                billingItemRepo.findAllByItemTypeAndStatus(itemType, FlowStatus.ACTIVE.name());

            List<InstallmentBillingItemDTO> dtos = new ArrayList<>();
            for (var item : items) {
                dtos.add(buildDTO(item));
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
    public ResponseEntity<ResponseObject> toggleStatus(Integer billingItemId) {
        ResponseObject result;
        try {
            Optional<NX_M_INSTALLMENT_BILLING_ITEM> data =
                billingItemRepo.findByBillingItemId(billingItemId);

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            NX_M_INSTALLMENT_BILLING_ITEM billingItem = data.get();

            // Toggle status
            if (billingItem.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())) {
                billingItem.setStatus(FlowStatus.INACTIVE.name());
            } else {
                billingItem.setStatus(FlowStatus.ACTIVE.name());
            }

            billingItem.setUpdatedDate(new Date());
            billingItem.setUpdatedBy(UserDetailUtils.getUsername());

            NX_M_INSTALLMENT_BILLING_ITEM saved = billingItemRepo.save(billingItem);

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
    private ResponseEntity<ResponseObject> validateCreate(BillingItemCreateDTO request) {
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
    private InstallmentBillingItemDTO buildDTO(NX_M_INSTALLMENT_BILLING_ITEM billingItem) {
        InstallmentBillingItemDTO dto = new InstallmentBillingItemDTO();
        dto.setBillingItemId(billingItem.getBillingItemId());
        dto.setInstallmentId(billingItem.getInstallmentId());
        dto.setItemName(billingItem.getItemName());
        dto.setItemDescription(billingItem.getItemDescription());
        dto.setItemType(billingItem.getItemType());
        dto.setAmount(billingItem.getAmount());
        dto.setQuantity(billingItem.getQuantity());
        dto.setUnitPrice(billingItem.getUnitPrice());
        dto.setTaxAmount(billingItem.getTaxAmount());
        dto.setDiscountAmount(billingItem.getDiscountAmount());
        dto.setNetAmount(billingItem.getNetAmount());

        // Get GLOBAL_TYPE name for item type
        if (billingItem.getItemType() != null) {
            var type = globalTypeService.getGlobalTypeByGlbTypeValId(
                "Billing Item Type", billingItem.getItemType());
            if (type != null) {
                dto.setItemTypeName(type.getName());
            }
        }

        return dto;
    }
}
