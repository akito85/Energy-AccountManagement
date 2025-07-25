/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.module.account.master.latecharge.validate;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.database.crm.entities.accountmanagement.M_AM_LATECHARGE;
import com.dbs.database.crm.repositories.accountmanagement.Account.MAmLateChargeRepo;
import com.dbs.module.account.master.latecharge.dto.CriteriaListDTO;
import com.dbs.module.account.master.latecharge.dto.MAmLateChargeDto;
import com.dbs.module.account.utils.ConstantAccount;
import com.dbs.module.account.utils.UtilsAccount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 *
 * @author RachmatY
 */
@Service
public class ValidateLateCharge {

    private static final Logger logger = LoggerFactory.getLogger(ValidateLateCharge.class);

    @Autowired
    private Validator validator;

    @Autowired
    private MAmLateChargeRepo mAmLateChargeRepo;

    private static final String EMPTY = " is empty";

    public ResponseObject validatecreateMasterLateCharge(MAmLateChargeDto inputRequest, String type) {
        ResponseObject result;
        var violations = validator.validate(inputRequest);
        if (!violations.isEmpty()) {
            List<Map<String, Object>> violationHeaderList = new ArrayList<>();
            List<String> validateHeader = new ArrayList<>();
            for (var violation : violations) {
                logger.error(violation.getMessage());
                Map<String, Object> data = new HashMap<>();
                validateHeader.add(violation.getMessage());
                data.put(violation.getPropertyPath().toString(), violation.getMessage());
                violationHeaderList.add(data);
            }
            result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, validateHeader.get(0), violationHeaderList);
            return result;
        }
        if (Constant.CREATE.equalsIgnoreCase(type)) {
            Optional<M_AM_LATECHARGE> cekNameCurrencyUnique = mAmLateChargeRepo.findTopByLateChargeNameIgnoreCaseAndCurrency(inputRequest.getLateChargeName().strip(), inputRequest.getCurrency());
            if (cekNameCurrencyUnique.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "Latecharge name and Currency already exist!",
                        ResponseUtils.DATA_EMPTY);
                return result;
            }
        } else {
            Optional<M_AM_LATECHARGE> getLateCharge = mAmLateChargeRepo.findById(inputRequest.getLateChargeId());

            if (getLateCharge.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        UtilsAccount.messageDataNotFound(ConstantAccount.MASTER_LATECHARGE, inputRequest.getLateChargeId()), HttpStatus.NOT_FOUND);
                return result;
            }

            Optional<M_AM_LATECHARGE> cekNameCurrencyUnique = mAmLateChargeRepo.findTopByLateChargeNameIgnoreCaseAndCurrency(inputRequest.getLateChargeName().strip(), inputRequest.getCurrency());
            if (cekNameCurrencyUnique.isPresent() && !cekNameCurrencyUnique.get().getId().equals(inputRequest.getLateChargeId())) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "Latecharge name and Currency already exist!",
                        ResponseUtils.DATA_EMPTY);
                return result;
            }
        }

        boolean isCheck = true;
        String message = "";
        for (CriteriaListDTO criteriaListDTO : inputRequest.getCriteria()) {
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.SA_TYPE_VAL)) {
                isCheck = inputRequest.getLateChargeCriteriaDatas().stream()
                        .filter(e -> e.getSaType() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }

            }

            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.WAPU_FLAG_VAL)) {
                isCheck = inputRequest.getLateChargeCriteriaDatas().stream()
                        .filter(e -> e.getWapuFlag() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }

            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.ACCOUNT_NUMBER_VAL)) {
                isCheck = inputRequest.getLateChargeCriteriaDatas().stream()
                        .filter(e -> e.getAccountNumber() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }

            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.COST_CENTER_VAL)) {
                isCheck = inputRequest.getLateChargeCriteriaDatas().stream()
                        .filter(e -> e.getCostCenter() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.PREMISE_COUNTRY_VAL)) {
                isCheck = inputRequest.getLateChargeCriteriaDatas().stream()
                        .filter(e -> e.getPremiseCountry() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.PREMISE_CITY_VAL)) {
                isCheck = inputRequest.getLateChargeCriteriaDatas().stream()
                        .filter(e -> e.getPremiseCity() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.PREMISE_PROVINCE_VAL)) {
                isCheck = inputRequest.getLateChargeCriteriaDatas().stream()
                        .filter(e -> e.getPremiseProvince() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.PREMISE_DISTRICT_VAL)) {
                isCheck = inputRequest.getLateChargeCriteriaDatas().stream()
                        .filter(e -> e.getPremiseDistrict() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.PREMISE_SUBDISTRICT_VAL)) {
                isCheck = inputRequest.getLateChargeCriteriaDatas().stream()
                        .filter(e -> e.getPremiseSubdistrict() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.ACCOUNT_SEGMENT_VAL)) {
                isCheck = inputRequest.getLateChargeCriteriaDatas().stream()
                        .filter(e -> e.getAccountSegment()== null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.CORPORATE_FLAG_VAL)) {
                isCheck = inputRequest.getLateChargeCriteriaDatas().stream()
                        .filter(e -> e.getCorporateFlag() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.ACCOUNT_TYPE_VAL)) {
                isCheck = inputRequest.getLateChargeCriteriaDatas().stream()
                        .filter(e -> e.getAccountType() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.ACCOUNT_GROUP_TYPE_VAL)) {
                isCheck = inputRequest.getLateChargeCriteriaDatas().stream()
                        .filter(e -> e.getAccountGroupType() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.IS_ALL_VAL)) {
                isCheck = inputRequest.getLateChargeCriteriaDatas().stream()
                        .filter(e -> e.getAllCriteria() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.SOR_VAL)) {
                isCheck = inputRequest.getLateChargeCriteriaDatas().stream()
                        .filter(e -> e.getSor() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.ACCOUNT_CATEGORY_VAL)) {
                isCheck = inputRequest.getLateChargeCriteriaDatas().stream()
                        .filter(e -> e.getAccountCategory() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.CLASSIFICATION_TYPE_VAL)) {
                isCheck = inputRequest.getLateChargeCriteriaDatas().stream()
                        .filter(e -> e.getClassificationType() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }

        }

        if (!isCheck) {
            result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, message,
                    ResponseUtils.DATA_EMPTY);
            return result;
        }

        return null;
    }

}
