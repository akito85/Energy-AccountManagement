package com.dbs.module.account.gtaccount.services;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.FlowStatus;
import com.dbs.database.crm.entities.usermanagement.M_GLOBAL_TYPE;
import com.dbs.database.crm.repositories.usermanagement.MGlobalTypeRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GTAccountServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(GTAccountServiceImpl.class);
    @Autowired
    private MGlobalTypeRepo mGlobalTypeRepo;
    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;
    @Autowired
    private GlobalTypeValueService globalTypeValueService;


//    public ResponseEntity<ResponseObject> getRT() {
//        try {
//            List<LinkedHashMap<String, Object>> allResponse = getGlobalType("Account Group Type RT");
//            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
//                    "Success get RT", allResponse), HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
//            return new ResponseEntity<>(
//                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
//                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
//                    HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    public ResponseEntity<ResponseObject> getPK() {
//        try {
//            List<LinkedHashMap<String, Object>> allResponse = getGlobalType("Account Group Type PK");
//            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
//                    "Success get PK", allResponse), HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
//            return new ResponseEntity<>(
//                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
//                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
//                    HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    public ResponseEntity<ResponseObject> getKI() {
//        try {
//            List<LinkedHashMap<String, Object>> allResponse = getGlobalType("Account Group");
//            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
//                    "Success get Account Group", allResponse), HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
//            return new ResponseEntity<>(
//                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
//                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
//                    HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    public ResponseEntity<ResponseObject> getLT() {
        try {
            List<LinkedHashMap<String, Object>> allResponse = getGlobalType("Location Type");
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get location type", allResponse), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getLR(Integer parentId) {
        try {
            List<LinkedHashMap<String, Object>> allResponse = globalTypeValueService.getGlobalTypeByParentValue(Constant.LOCATION_REFERENCE_NAME, parentId).stream()
                    .filter(v -> FlowStatus.ACTIVE.name().equalsIgnoreCase(v.getStatus()))
                    .map(v -> {
                        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                        response.put("id", v.getGlbTypeValId());
                        response.put("name", v.getName());
                        response.put("value", v.getGlbValue());
                        return response;
                    })
                    .collect(Collectors.toList());

            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get location reference", allResponse), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public List<LinkedHashMap<String, Object>> getGlobalType(String groupName) {
        Optional<M_GLOBAL_TYPE> mGlobalType = mGlobalTypeRepo.findByGroupName(groupName);
        if (mGlobalType.isEmpty() || !FlowStatus.ACTIVE.name().equalsIgnoreCase(mGlobalType.get().getStatus())) {
            return Collections.emptyList();
        }
        return rGlobalTypeValueRepo.findAll().stream()
                .filter(v -> Objects.equals(v.getGlobalType(), mGlobalType.get().getGlbTypeId()) &&
                        FlowStatus.ACTIVE.name().equalsIgnoreCase(v.getStatus()))
                .map(v -> {
                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                    response.put("id", v.getGlbTypeValId());
                    response.put("name", v.getName());
                    response.put("value", v.getGlbValue());
                    return response;
                })
                .collect(Collectors.toList());
    }
}
