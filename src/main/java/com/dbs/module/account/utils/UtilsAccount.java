package com.dbs.module.account.utils;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.security.CryptoSecurity;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.StringUtils;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.common.library.utils.UtilsDate;
import com.dbs.database.crm.entities.usermanagement.M_USER;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.usermanagement.MUserRepo;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class UtilsAccount {

    private UtilsAccount() {}

    private static final String SUCCESS = "Success ";

    public static String messageSuccess(String type, String name){
        return SUCCESS + type + name;
    }

    public static String messageAlreadyExist(String name){
        return name + " already exist!";
    }

    public static String messageDataNotFound(String name, Integer id) {
        return name + " with id " + id + " not found!";
    }

    public static ResponseEntity<ResponseObject> returnForValidateOrApi(Boolean api, String name) {
        if(Boolean.TRUE.equals(api)) {
            return new ResponseEntity<>(
                    new ResponseObject(
                            ResponseUtils.SUCCESS_TRUE,
                            HttpStatus.OK,
                            UtilsAccount.messageSuccess(ConstantAccount.VALIDATE, name),
                            ResponseUtils.DATA_EMPTY)
                    , HttpStatus.OK
            );
        } else {
            return null;
        }
    }

    public static LinkedHashMap<String, Object> messageWarningInactivePrimary(String type) {
        LinkedHashMap<String, Object> responseWarningInfo = new LinkedHashMap<>();
        responseWarningInfo.put("success", Boolean.FALSE);
        responseWarningInfo.put("message", warningInactive(type));
        return responseWarningInfo;
    }

    public static String warningInactive(String type) {
        return "Warning! the previous primary " + type + " will be inactived";
    }

    public static dtoLabelValue getLabelValue(R_GLOBAL_TYPE_VALUE globalType){
        dtoLabelValue type = new dtoLabelValue();
        type.setKey(globalType !=null? globalType.getGlbValue() : null);
        type.setLabel(globalType !=null? globalType.getName() : null);
        type.setValue(globalType !=null? globalType.getGlbTypeValId() : null);
        return type;
    }

    public static List<String> encryptTaxForFiltering(List<String> searchList) {

        return searchList.stream()
                .map(s -> {
                    String[] split = s.split("~");
                    if (split[0].equalsIgnoreCase("taxIdentifierNumber")) {
                        split[1] = CryptoSecurity.encrypt(split[1]);
                    }
                    return split[0] + "~" + split[1];
                })
                .collect(Collectors.toList());
    }

    public static String messageValidateExist(String type, String status) {

        switch (status) {
            case ConstantAccount.WAITING_APPROVAL:
                if(type.equalsIgnoreCase(ConstantAccount.SA_MAIN)) {
                    return "You can't create an SA Main because another SA Main is waiting for approval.";
                } else if(type.equalsIgnoreCase(ConstantAccount.SA_AMANDEMEN)) {
                    return "You can't create an SA Amendment because there is another waiting approval SA Amendment with overlapping date.";
                } else if(type.equalsIgnoreCase(ConstantAccount.SA_ADDON)) {
                    return "You can't create an SA Addon because there is another waiting approval SA Addon with same product and has overlapping date.";
                } else if(type.equalsIgnoreCase(ConstantAccount.TOS_SA)) {
                    return "You can't create an TOS Submission because there is another waiting approval TOS Submission with same term of service and has overlapping date.";
                } else if(type.equalsIgnoreCase(ConstantAccount.MASTER_LATECHARGE)) {
                    return "You can't create a Late Charge Rule because another Late Charge Rule is waiting for approval.";
                } else if(type.equalsIgnoreCase(ConstantAccount.MASTER_TAXIMPLI)) {
                    return "You can't create a Tax Implication Rule because another Tax Implication Rule is waiting for approval.";
                } else if(type.equalsIgnoreCase(ConstantAccount.SA_AMANDEMEN_INACTIVE)) {
                    return "You can't inactive this SA Main because there is waiting approval SA Amendment.";
                } else if(type.equalsIgnoreCase(ConstantAccount.SA_ADDON_INACTIVE)) {
                    return "You can't inactive this SA Main because there is waiting approval SA Addon.";
                } else {
                    return "";
                }

            case ConstantAccount.APPROVED:
                if(type.equalsIgnoreCase(ConstantAccount.SA_MAIN)) {
                    return "You can't create an SA Main because another active SA Main already exists.";
                } else if(type.equalsIgnoreCase(ConstantAccount.SA_AMANDEMEN)) {
                    return "You can't create an SA Amendment because there is another active SA Amendment with overlapping date.";
                } else if(type.equalsIgnoreCase(ConstantAccount.SA_ADDON)) {
                    return "You can't create an SA Addon because there is another active SA Addon with same product and has overlapping date.";
                } else if(type.equalsIgnoreCase(ConstantAccount.TOS_SA)) {
                    return "You can't create a TOS Submission because there is another active TOS Submission with same term of service and has overlapping date.";
                } else if(type.equalsIgnoreCase(ConstantAccount.MASTER_LATECHARGE)) {
                    return "You can't create a Late Charge Rule because another active Late Charge Rule already exists.";
                } else if(type.equalsIgnoreCase(ConstantAccount.MASTER_TAXIMPLI)) {
                    return "You can't create a Tax Implication Rule because another active Tax Implication Rule already exists.";
                } else if(type.equalsIgnoreCase(ConstantAccount.SA_AMANDEMEN_INACTIVE)) {
                    return "You can't inactive this SA Main because there is active SA Amendment.";
                } else if(type.equalsIgnoreCase(ConstantAccount.SA_ADDON_INACTIVE)) {
                    return "You can't inactive this SA Main because there is active SA Addon.";
                } else {
                    return "";
                }
        }
        return "";
    }

    public static boolean isDateInRange(Date startDate, Date endDate) {
        Date currentDate = new Date();
        if(startDate==null) {
            return true;
        } else if(endDate==null) {
            return currentDate.after(startDate);
        } else {
            return currentDate.after(startDate) && currentDate.before(endDate);
        }
    }


    // SERVICE
    @Service
    public static class GlobalTypeServiceAccount {

        @Autowired
        private GlobalTypeValueService globalTypeValueService;

        @Autowired
        private MUserRepo mUserRepo;

        public R_GLOBAL_TYPE_VALUE getIdGlobalTypeByGroupNameAndValue(String groupName, String value) {
            Optional<R_GLOBAL_TYPE_VALUE> rServiceType = globalTypeValueService.getOptionalGlobalTypeByGlbValue(groupName, value);
            R_GLOBAL_TYPE_VALUE rValue = new R_GLOBAL_TYPE_VALUE();
            if(rServiceType.isPresent()) {
                rValue = rServiceType.get();
            } else {
                rValue.setGlbValue("Global type with group name " + groupName + " and value " + value + " not found!");
            }
            return rValue;
        }

        public Boolean isItSuperUser(){
            Optional<M_USER> userInformation = mUserRepo.findByUserId(Integer.parseInt(Objects.requireNonNull(UserDetailUtils.getUserId())));
            return userInformation.filter(mUser -> mUser.getUserLevel().equalsIgnoreCase("SU")).map(mUser -> Boolean.TRUE).orElse(Boolean.FALSE);

        }
    }

}
