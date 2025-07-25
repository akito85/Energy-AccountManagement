package com.dbs.common.library.utils;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.utils.dto.UtilsContactCreateRequestDTO;
import com.dbs.database.crm.entities.accountmanagement.M_CONTACT;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.accountmanagement.Account.MContactRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.*;

@Service
public class ContactUtils {

    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;

    @Autowired
    private Validator validator;

    @Autowired
    private MContactRepo contactRepo;

    public List<String> getContactDetailValue(Integer reqPrefix1, Integer reqPrefix2, String reqValue, String reqSufix) {
        List<String> value = new ArrayList<>();
        if(reqPrefix1 != null && reqPrefix2 != null) {
            Optional<R_GLOBAL_TYPE_VALUE> prefix1 = rGlobalTypeValueRepo.findById(reqPrefix1);
            Optional<R_GLOBAL_TYPE_VALUE> prefix2 = rGlobalTypeValueRepo.findById(reqPrefix2);
            value.add(
                    reqSufix != null ?
                            prefix1.get().getName() + " " + prefix2.get().getName() + " " + reqValue + " " + " Ext. " + reqSufix
                            :
                            prefix1.get().getName() + " " + prefix2.get().getName() + " " + reqValue
            );
            value.add(
                    reqSufix != null ?
                    prefix1.get().getGlbValue() + prefix2.get().getGlbValue() + reqValue + " Ext. " + reqSufix
                    :
                    prefix1.get().getGlbValue() + prefix2.get().getGlbValue() + reqValue
            );
        } else if(reqPrefix2 != null && reqSufix == null) {
            Optional<R_GLOBAL_TYPE_VALUE> prefix1 = rGlobalTypeValueRepo.findById(reqPrefix1);
            Optional<R_GLOBAL_TYPE_VALUE> prefix2 = rGlobalTypeValueRepo.findById(reqPrefix2);
            value.add(prefix1.get().getName() + " " + prefix2.get().getName() + " " + reqValue);
            value.add(prefix1.get().getGlbValue() + reqValue);
        } else if(reqPrefix1 != null && reqPrefix2 == null) {
            Optional<R_GLOBAL_TYPE_VALUE> prefix1 = rGlobalTypeValueRepo.findById(reqPrefix1);
            value.add(prefix1.get().getName() + " " + reqValue);
            value.add(prefix1.get().getGlbValue() + reqValue);
        } else {
            value.add(reqValue);
            value.add(reqValue);
        }
        return value;
    }

    public ResponseEntity<ResponseObject> validateCreateUpdate(UtilsContactCreateRequestDTO request) {
        ResponseObject result;
        try {
            var violations = validator.validate(request);
            if (!violations.isEmpty()) {
                List<Map<String, Object>> violationHeaderList = new ArrayList<>();
                List<String> validateHeader = new ArrayList<>();
                for (var violation : violations) {
                    Map<String, Object> data = new HashMap<>();
                    validateHeader.add(violation.getMessage());
                    data.put(violation.getPropertyPath().toString(), violation.getMessage());
                    violationHeaderList.add(data);
                }
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, validateHeader.get(0), violationHeaderList);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            Optional<M_CONTACT> cekExist = contactRepo.findTopByContactNameAndJobIdAndPositionId(request.getContactName().strip(), request.getJobId(), request.getPositionId());
            if(cekExist.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "The contact data name, job and position have been registered", null);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            return null;

        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
