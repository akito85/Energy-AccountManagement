package com.dbs.common.base.utils;

import java.util.Collections;
import java.util.List;

public class ResponseUtils {

    public static final Boolean SUCCESS_TRUE = true;

    public static final Boolean SUCCESS_FALSE = false;

    public static final String MESSAGE_OK = "OK";
    
    public static final String MESSAGE_SUCCESS = "Success";

    public static final String MESSAGE_CREATED = "Your data has been created";

    public static final String MESSAGE_UPDATED = "Your data has been updated";
    public static final String MESSAGE_UNAUTHORIZED = "User Unauthorized";
    public static final String SERVICE_UNAVAILABLE = "Service Unavailable";

    public static final String MESSAGE_NOT_FOUND = "Not Found";

    public static final String MESSAGE_INTERNAL_SERVER_ERROR = "Internal Server Error";
    public static final String MESSAGE_FAIL_CHOOSE = "Your session time has expired, please log in again";
    public static final String MESSAGE_INVALID_REQ = "Your session will expired";

    public static final String MESSAGE_ACTIVE = "Your data has been activated";
    public static final String MESSAGE_FAILED = "Failed";

    public static final String MESSAGE_INACTIVE = "Your data has been inactivated";

    public static final String MESSAGE_BAD_REQUEST = "Input Violation";
    public static final String MESSAGE_USER_UNAUTHORIZED = "Please contact administrator, User Unauthorized";
    public static final String MESSAGE_GA_UNAUTHORIZED = "Please contact administrator, Group Access Unauthorized";
    public static final String MESSAGE_FAILED_LOGIN = "Oops, login failed Username or Password is incorrect";
    public static final String MESSAGE_FAILED_LOGIN_AE_EU = "Oops, login failed Username or Password or Entity is incorrect";
    public static final String MESSAGE_FORGOT_PASSWORD = "Oops, login failed Username or Entity is incorrect";
    public static final String MESSAGE_START_DATE = "The start date must be greater than or equal to the current date";
    public static final String MESSAGE_START_DATE_CRITERIA = "The start date criteria must be greater than or equal to the current date";
    public static final String MESSAGE_START_DATE_ASSIGMENT = "The start assignment date must be greater than or equal to the current date";
    public static final String MESSAGE_END_DATE = "The end date must be the same or greater than the current date";
    public static final String MESSAGE_END_DATE_CRITERIA = "The end date criteria must be the same or greater than the current date";
    public static final String MESSAGE_START_DATE_AND_END_DATE = "The end date cannot be smaller than the start date";
    public static final String MESSAGE_START_DATE_AND_END_DATE_CRITERIA = "The end date criteria cannot be smaller than the start date";
    public static final String MESSAGE_FAILED_LOGIN_USER_INACTIVE = "This user is no longer active.";
    public static final String MESSAGE_REMARK = "Please input your remark!";
    public static final String MESSAGE_ERROR = "Please contact administrator, Internal Server Error";
    public static final String MESSAGE_NOT_APPROVER = "You are not approver!";
    public static final String MESSAGE_KICK_SESSION = "Your current username is being used by other user. You will be logged out";
    public static final String MESSAGE_AMOUNT_ALLOCATION_OVER_RECEIPT_AMOUNT = "Amount allocation can't greater than receipt amount";

    public static final String MESSAGE_SUBMITTED = "Your data has been submitted";
    public static final String MESSAGE_BLANK_START_DATE_CRITERIA = "The start date criteria cannot be empty";

    public static final List<Object> DATA_EMPTY = Collections.emptyList();
    
    public static final String STS_SUCCESS_FALSE = "Failed";
    
    public static final String STS_SUCCESS_TRUE = "Success";
    private ResponseUtils() {
    }
}
