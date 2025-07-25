/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.module.account.master.taximplication.validate;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.database.crm.entities.accountmanagement.M_AM_TAXIMPLICATION;
import com.dbs.database.crm.repositories.accountmanagement.Account.MTaxImplicationRepo;
import com.dbs.module.account.master.latecharge.dto.CriteriaListDTO;
import com.dbs.module.account.master.taximplication.dto.CreateMasterTaxImplicationRequestDto;
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
public class ValidateTaxImplication {
    
    private static final Logger logger = LoggerFactory.getLogger(ValidateTaxImplication.class);
    
    @Autowired
    private Validator validator;
    
    @Autowired
    private MTaxImplicationRepo taxImpliRepo;
    
    private static final String EMPTY = " is empty";
    
    public ResponseObject validateCreateOrUpdate(CreateMasterTaxImplicationRequestDto inputRequest, String type) {
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
        if (Constant.CREATE.equals(type)) {
            Optional<M_AM_TAXIMPLICATION> cekUnique = taxImpliRepo.findTopByTaxImplicationNameIgnoreCaseAndCategoryAndServiceType(inputRequest.getTaxImplicationName().strip(), inputRequest.getCategory(), inputRequest.getServiceType());
            if (cekUnique.isPresent()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "Tax implication name and Category and Service type already exist!",
                        ResponseUtils.DATA_EMPTY);
                return result;
            }
        } else {
            Optional<M_AM_TAXIMPLICATION> getTaxImplication = taxImpliRepo.findById(inputRequest.getTaxImplicationId());

            if (getTaxImplication.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        UtilsAccount.messageDataNotFound(ConstantAccount.MASTER_TAXIMPLI, inputRequest.getTaxImplicationId()), HttpStatus.NOT_FOUND);
                return result;
            }

            Optional<M_AM_TAXIMPLICATION> cekUnique = taxImpliRepo.findTopByTaxImplicationNameIgnoreCaseAndCategoryAndServiceType(inputRequest.getTaxImplicationName(), inputRequest.getCategory(), inputRequest.getServiceType());
            if (cekUnique.isPresent() && !cekUnique.get().getId().equals(inputRequest.getTaxImplicationId())) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "Tax implication name and Category and Service type already exist!",
                        ResponseUtils.DATA_EMPTY);
                return result;
            }
        }
        
        boolean isCheck = true;
        String message = "";
        for (CriteriaListDTO criteriaListDTO : inputRequest.getCriteria()) {
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.SA_TYPE_VAL)) {
                isCheck = inputRequest.getTaxImplicationCriterias().stream()
                        .filter(e -> e.getSaType() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }

            }

            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.WAPU_FLAG_VAL)) {
                isCheck = inputRequest.getTaxImplicationCriterias().stream()
                        .filter(e -> e.getWapuFlag() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }

            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.ACCOUNT_NUMBER_VAL)) {
                isCheck = inputRequest.getTaxImplicationCriterias().stream()
                        .filter(e -> e.getAccountNumber() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }

            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.COST_CENTER_VAL)) {
                isCheck = inputRequest.getTaxImplicationCriterias().stream()
                        .filter(e -> e.getCostCenter() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.PREMISE_COUNTRY_VAL)) {
                isCheck = inputRequest.getTaxImplicationCriterias().stream()
                        .filter(e -> e.getPremiseCountry() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.PREMISE_CITY_VAL)) {
                isCheck = inputRequest.getTaxImplicationCriterias().stream()
                        .filter(e -> e.getPremiseCity() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.PREMISE_PROVINCE_VAL)) {
                isCheck = inputRequest.getTaxImplicationCriterias().stream()
                        .filter(e -> e.getPremiseProvince() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.PREMISE_DISTRICT_VAL)) {
                isCheck = inputRequest.getTaxImplicationCriterias().stream()
                        .filter(e -> e.getPremiseDistrict() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.PREMISE_SUBDISTRICT_VAL)) {
                isCheck = inputRequest.getTaxImplicationCriterias().stream()
                        .filter(e -> e.getPremiseSubdistrict() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.ACCOUNT_SEGMENT_VAL)) {
                isCheck = inputRequest.getTaxImplicationCriterias().stream()
                        .filter(e -> e.getAccountSegment() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.CORPORATE_FLAG_VAL)) {
                isCheck = inputRequest.getTaxImplicationCriterias().stream()
                        .filter(e -> e.getCorporateFlag() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.ACCOUNT_TYPE_VAL)) {
                isCheck = inputRequest.getTaxImplicationCriterias().stream()
                        .filter(e -> e.getAccountType() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.ACCOUNT_GROUP_TYPE_VAL)) {
                isCheck = inputRequest.getTaxImplicationCriterias().stream()
                        .filter(e -> e.getAccountGroupType() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.IS_ALL_VAL)) {
                isCheck = inputRequest.getTaxImplicationCriterias().stream()
                        .filter(e -> e.getIsAll() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.SOR_VAL)) {
                isCheck = inputRequest.getTaxImplicationCriterias().stream()
                        .filter(e -> e.getSor() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.ACCOUNT_CATEGORY_VAL)) {
                isCheck = inputRequest.getTaxImplicationCriterias().stream()
                        .filter(e -> e.getAccountCategory() == null)
                        .collect(Collectors.toList()).isEmpty();
                if (!isCheck) {
                    message = criteriaListDTO.getName() + EMPTY;
                    break;
                }
            }
            if (criteriaListDTO.getLabel().equalsIgnoreCase(Constant.CLASSIFICATION_TYPE_VAL)) {
                isCheck = inputRequest.getTaxImplicationCriterias().stream()
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
