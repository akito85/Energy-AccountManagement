package com.dbs.module.account.detail.servicerequest.service;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.FlowStatus;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.database.crm.entities.accountmanagement.servicerequest.NX_M_WORK_ORDER;
import com.dbs.database.crm.repositories.accountmanagement.servicerequest.NxMWorkOrderRepo;
import com.dbs.database.crm.repositories.accountmanagement.servicerequest.NxRSrDetailsRepo;
import com.dbs.module.account.detail.servicerequest.dto.WorkOrderDTO;
import com.dbs.module.account.detail.servicerequest.dto.WorkOrderViewDTO;
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
 * Service for managing Work Orders
 */
@Service
public class WorkOrderService {

    private static final Logger logger = LoggerFactory.getLogger(WorkOrderService.class);

    @Autowired
    private NxMWorkOrderRepo workOrderRepo;

    @Autowired
    private NxRSrDetailsRepo srDetailsRepo;

    @Autowired
    private GlobalTypeValueService globalTypeService;

    /**
     * Create a new work order
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> create(WorkOrderDTO request, Integer serviceRequestId) {
        ResponseObject result;
        try {
            // Check duplicate work order number
            if (request.getWorkOrderNumber() != null &&
                workOrderRepo.existsByWorkOrderNumber(request.getWorkOrderNumber())) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                    "Work order number already exists", null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            NX_M_WORK_ORDER workOrder = new NX_M_WORK_ORDER();
            workOrder.setWorkOrderNumber(request.getWorkOrderNumber());
            workOrder.setWorkOrderType(request.getWorkOrderType());
            workOrder.setWorkOrderStatus(request.getWorkOrderStatus());
            workOrder.setScheduledDate(request.getScheduledDate());
            workOrder.setStartDate(request.getStartDate());
            workOrder.setCompletionDate(request.getCompletionDate());
            workOrder.setAssignedTeam(request.getAssignedTeam());
            workOrder.setAssignedTechnician(request.getAssignedTechnician());
            workOrder.setEstimatedHours(request.getEstimatedHours());
            workOrder.setActualHours(request.getActualHours());
            workOrder.setNotes(request.getNotes());

            workOrder.setCreatedBy(UserDetailUtils.getUsername());
            workOrder.setStatus(FlowStatus.ACTIVE.name());
            workOrder.setCreatedDate(new Date());

            NX_M_WORK_ORDER savedWorkOrder = workOrderRepo.save(workOrder);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                ResponseUtils.MESSAGE_CREATED, savedWorkOrder);
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
     * Update work order
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> update(WorkOrderDTO request) {
        ResponseObject result;
        try {
            Optional<NX_M_WORK_ORDER> data =
                workOrderRepo.findByWorkOrderId(request.getWorkOrderId());

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    "Work order not found", null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            NX_M_WORK_ORDER workOrder = data.get();

            if (request.getWorkOrderStatus() != null) {
                workOrder.setWorkOrderStatus(request.getWorkOrderStatus());
            }
            if (request.getScheduledDate() != null) {
                workOrder.setScheduledDate(request.getScheduledDate());
            }
            if (request.getStartDate() != null) {
                workOrder.setStartDate(request.getStartDate());
            }
            if (request.getCompletionDate() != null) {
                workOrder.setCompletionDate(request.getCompletionDate());
            }
            if (request.getAssignedTechnician() != null) {
                workOrder.setAssignedTechnician(request.getAssignedTechnician());
            }
            if (request.getActualHours() != null) {
                workOrder.setActualHours(request.getActualHours());
            }
            if (request.getNotes() != null) {
                workOrder.setNotes(request.getNotes());
            }

            workOrder.setUpdatedBy(UserDetailUtils.getUsername());
            workOrder.setUpdatedDate(new Date());

            NX_M_WORK_ORDER savedWorkOrder = workOrderRepo.save(workOrder);

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                ResponseUtils.MESSAGE_UPDATED, savedWorkOrder);
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
     * Get work order detail
     */
    public ResponseEntity<ResponseObject> getDetail(Integer workOrderId) {
        ResponseObject result;
        try {
            Optional<NX_M_WORK_ORDER> data = workOrderRepo.findByWorkOrderId(workOrderId);

            if (!data.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                    ResponseUtils.MESSAGE_NOT_FOUND, null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            WorkOrderViewDTO viewDTO = buildViewDTO(data.get());

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
     * Get work orders for a service request
     */
    public ResponseEntity<ResponseObject> getListByServiceRequest(Integer serviceRequestId) {
        ResponseObject result;
        try {
            var srDetails = srDetailsRepo.findAllByServiceRequestIdAndWorkOrderIdIsNotNull(
                serviceRequestId);

            List<WorkOrderViewDTO> workOrders = new ArrayList<>();
            for (var detail : srDetails) {
                if (detail.getWorkOrderId() != null) {
                    Optional<NX_M_WORK_ORDER> workOrder =
                        workOrderRepo.findByWorkOrderId(detail.getWorkOrderId());
                    if (workOrder.isPresent()) {
                        workOrders.add(buildViewDTO(workOrder.get()));
                    }
                }
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                "Success", workOrders);
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
    private WorkOrderViewDTO buildViewDTO(NX_M_WORK_ORDER workOrder) {
        WorkOrderViewDTO dto = new WorkOrderViewDTO();
        dto.setWorkOrderId(workOrder.getWorkOrderId());
        dto.setWorkOrderNumber(workOrder.getWorkOrderNumber());
        dto.setWorkOrderType(workOrder.getWorkOrderType());
        dto.setWorkOrderStatus(workOrder.getWorkOrderStatus());
        dto.setScheduledDate(workOrder.getScheduledDate());
        dto.setStartDate(workOrder.getStartDate());
        dto.setCompletionDate(workOrder.getCompletionDate());
        dto.setAssignedTeam(workOrder.getAssignedTeam());
        dto.setAssignedTechnician(workOrder.getAssignedTechnician());
        dto.setEstimatedHours(workOrder.getEstimatedHours());
        dto.setActualHours(workOrder.getActualHours());
        dto.setNotes(workOrder.getNotes());
        dto.setStatus(workOrder.getStatus());
        dto.setCreatedBy(workOrder.getCreatedBy());
        dto.setCreatedDate(workOrder.getCreatedDate());

        // Get GLOBAL_TYPE names
        if (workOrder.getWorkOrderType() != null) {
            var type = globalTypeService.getGlobalTypeByGlbTypeValId(
                "Work Order Type", workOrder.getWorkOrderType());
            if (type != null) {
                dto.setWorkOrderTypeName(type.getName());
            }
        }

        if (workOrder.getWorkOrderStatus() != null) {
            var status = globalTypeService.getGlobalTypeByGlbTypeValId(
                "Work Order Status", workOrder.getWorkOrderStatus());
            if (status != null) {
                dto.setWorkOrderStatusName(status.getName());
            }
        }

        return dto;
    }
}
