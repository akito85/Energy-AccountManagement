package com.dbs.common.library.utils;

import java.io.IOException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;

import com.dbs.database.crm.entities.usermanagement.M_EMPLOYEE;
import com.dbs.database.crm.entities.usermanagement.M_USER;

import com.dbs.database.crm.repositories.usermanagement.MEmployeeRepo;
import com.dbs.database.crm.repositories.usermanagement.MUserRepo;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PricingRuleUtils {

    @Autowired
    private static MUserRepo mUserRepo;
    @Autowired
    private static MEmployeeRepo mEmployeeRepo;

    private PricingRuleUtils() {
    }

    public static Map<String, Object> jsonStringToMap(String jsonString) {

        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> map = null;

        try {

            // convert JSON string to Map
            map = (HashMap<String, Object>) mapper.readValue(jsonString, Map.class);

            // it works
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static ResponseObject responseFail(String msg) {
        return new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, msg, ResponseUtils.DATA_EMPTY);
    }

    public static ResponseObject responseSuccess(String msg, Object data) {
        return new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, msg, data);
    }

    public static String findFullNameByUserName() {

        Optional<M_USER> mUser = mUserRepo.findByUsername(UserDetailUtils.getUsername());
        Integer employeeId = 0;
        if (mUser.isPresent()) {
            employeeId = mUser.get().getEmployeeId();

        }

        Optional<M_EMPLOYEE> mEmployee = mEmployeeRepo.findByEmployeeId(employeeId);

        if (mEmployee.isPresent()) {
            return mEmployee.get().getFullName();
        }

        return null;
    }

    public static ResponseObject validateStartEndDate(Date startDate, Date endDate, String dateFormat, String status, String src) {
        ResponseObject result = null;

//        if (!CommonVariables.ACTIVE.equalsIgnoreCase(status) &&
//            startDate != null && !UtilsDate.validateDateWithCurrMinute2(UtilsDate.dateToString(startDate, dateFormat))) {
//            result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
//                    String.format("Start Date in %s must be later than current date", src), ResponseUtils.DATA_EMPTY);
//            return result;
//        }

        if (startDate != null && endDate != null) {

            if (!UtilsDate
                    .validateDateWithCurrMinute2(UtilsDate.dateToString(endDate, dateFormat))) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        String.format("End Date in %s must be later than current date", src), ResponseUtils.DATA_EMPTY);
                return result;
            }

            try {

                if (endDate.getTime() <= startDate.getTime()) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            String.format("End Date in %s must be later than start date", src), ResponseUtils.DATA_EMPTY);
                    return result;
                }
            } catch (Exception e) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Invalid date format", ResponseUtils.DATA_EMPTY);
                return result;
            }
        }

        return result;
    }

    public static ResponseObject validateStartEndDateStr(String startDateStr, String endDateStr, String dateFormat, String status, String src) {
        ResponseObject result = null;
        Date startDate = startDateStr != null ? UtilsDate.stringToDate(startDateStr, dateFormat) : null;
        Date endDate = endDateStr != null ? UtilsDate.stringToDate(endDateStr, dateFormat) : null;

        if (!CommonVariables.ACTIVE.equalsIgnoreCase(status) && 
                startDate != null && !UtilsDate.validateDateWithCurrMinute2(UtilsDate.dateToString(startDate, dateFormat))) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        String.format("Start Date in %s must be later than current date", src), ResponseUtils.DATA_EMPTY);
                return result;
        }

        if (startDate != null && endDate != null) {

            if (!UtilsDate
                    .validateDateWithCurrMinute2(UtilsDate.dateToString(endDate, dateFormat))) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        String.format("End Date in %s must be later than current date", src), ResponseUtils.DATA_EMPTY);
                return result;
            }

            try {

                if (endDate.getTime() <= startDate.getTime()) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            String.format("End Date in %s must be later than start date", src), ResponseUtils.DATA_EMPTY);
                    return result;
                }
            } catch (Exception e) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Invalid date format", ResponseUtils.DATA_EMPTY);
                return result;
            }
        }

        return result;
    }
}
