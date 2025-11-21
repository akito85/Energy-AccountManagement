package com.dbs.module.account.detail.additionalinformation.service;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.StringUtils;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.database.crm.entities.accountmanagement.M_AM_ADDITIONAL_INFORMATION;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.accountmanagement.Account.AdditionalInformationRepo;
import com.dbs.database.crm.repositories.usermanagement.MGlobalTypeRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import com.dbs.module.account.detail.additionalinformation.dto.CreateUpdateAddInfoDto;
import com.dbs.module.account.detail.additionalinformation.dto.ValueIntDdl;
import com.dbs.module.account.detail.additionalinformation.validate.ValidateCreateUpdate;
import com.dbs.module.account.utils.ConstantAccount;
import com.dbs.module.account.utils.UtilsAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class AdditionalInformationService {

    private static final Logger logger = LoggerFactory.getLogger(AdditionalInformationService.class);

    @Autowired
    private MGlobalTypeRepo mGlobalTypeRepo;

    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;

    @Autowired
    private GlobalTypeValueService globalTypeValueService;

    @Autowired
    private AdditionalInformationRepo additionalInformationRepo;

    @Autowired
    private ValidateCreateUpdate validateCreateUpdate;

    public ResponseEntity<ResponseObject> getDropDownListAdditionalInformation(String groupName, Integer parentValue) {
        try {
            List<LinkedHashMap<String, Object>> allData = globalTypeValueService.getGlobalTypeAnyChild(groupName, parentValue);
            ResponseObject result = new ResponseObject(
                    ResponseUtils.SUCCESS_TRUE,
                    HttpStatus.OK,
                    UtilsAccount.messageSuccess(ConstantAccount.DDL, groupName),
                    allData
            );
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(),e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }

    public ResponseEntity<ResponseObject> createUpdateAddInfo (CreateUpdateAddInfoDto createUpdateAddInfoDto, HttpServletRequest httpServletRequest){
        if (createUpdateAddInfoDto.getId() == null){
            logger.info("Create Additional Information");
        } else {
            logger.info("Update Additional Information");
        }
        ResponseObject result = new ResponseObject();
        try {
            ResponseObject resp = validateCreateUpdate.validateCreateUpdateAddInfo(createUpdateAddInfoDto);
            if (StringUtils.hasValue(resp)) {
                return new ResponseEntity<>(resp, resp.getHttpCode());
            }
            M_AM_ADDITIONAL_INFORMATION addInfo = new M_AM_ADDITIONAL_INFORMATION();
            validateCreateUpdate(createUpdateAddInfoDto);
//            Boolean anyChild = rGlobalTypeValueRepo.existsByParentValue(createUpdateAddInfoDto.getInformationType());
            if (createUpdateAddInfoDto.getId() == null){
                addInfo.setAccountId(createUpdateAddInfoDto.getAccountId());
                addInfo.setInformationType(createUpdateAddInfoDto.getInformationType());
                addInfo.setValue1(createUpdateAddInfoDto.getValueStr());
                addInfo.setValue2(createUpdateAddInfoDto.getValueIntDdl().getId());
//                if (!anyChild){
//                    addInfo.setValue1(createUpdateAddInfoDto.getValue1());
//                } else {
//                    addInfo.setValue2(createUpdateAddInfoDto.getValue2());
//                }
                addInfo.setIsDeleted(Boolean.FALSE);
                addInfo.setCreatedBy(UserDetailUtils.getUsernameFromToken(httpServletRequest));
                addInfo.setCreatedDate(new Date());

                additionalInformationRepo.save(addInfo);
            } else {
                Optional<M_AM_ADDITIONAL_INFORMATION> cekAddInfo = additionalInformationRepo.findById(createUpdateAddInfoDto.getId());
                if (cekAddInfo.isPresent()) {
                    M_AM_ADDITIONAL_INFORMATION getAddInfo = cekAddInfo.get();
                    getAddInfo.setAccountId(createUpdateAddInfoDto.getAccountId());
                    getAddInfo.setInformationType(createUpdateAddInfoDto.getInformationType());
                    getAddInfo.setValue1(createUpdateAddInfoDto.getValueStr());
                    getAddInfo.setValue2(createUpdateAddInfoDto.getValueIntDdl().getId());
//                    if (!anyChild) {
//                        getAddInfo.setValue1(createUpdateAddInfoDto.getValue1());
//                    } else {
//                        getAddInfo.setValue2(createUpdateAddInfoDto.getValue2());
//                    }
                    getAddInfo.setUpdatedBy(UserDetailUtils.getUsernameFromToken(httpServletRequest));
                    getAddInfo.setUpdatedDate(new Date());

                    additionalInformationRepo.save(getAddInfo);
                }
            }
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED,
                    "Success To Proceed Additional Information", null);
            logger.info("Response Success ->" + result);
            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(),e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }

    public ResponseEntity<ResponseObject> validateCreateUpdate (CreateUpdateAddInfoDto createUpdateAddInfoDto){
        try {
            ResponseObject resp = validateCreateUpdate.validateCreateUpdateAddInfo(createUpdateAddInfoDto);
            if (StringUtils.hasValue(resp)) {
                return new ResponseEntity<>(resp, resp.getHttpCode());
            }
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
        ResponseObject response = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                ResponseUtils.MESSAGE_SUCCESS, ResponseUtils.DATA_EMPTY);
        return new ResponseEntity<>(response, response.getHttpCode());
    }

    public ResponseEntity<ResponseObject> viewAddInfo (Integer accountId) {
        ResponseObject result = new ResponseObject();
        try {
            List<M_AM_ADDITIONAL_INFORMATION> getAll = additionalInformationRepo.findAllByAccountIdAndIsDeleted(accountId,Boolean.FALSE);
            List<CreateUpdateAddInfoDto> arrayList = new ArrayList<>();
            for (M_AM_ADDITIONAL_INFORMATION all : getAll){
                CreateUpdateAddInfoDto addInfoDto = new CreateUpdateAddInfoDto();
                addInfoDto.setId(all.getId());
                addInfoDto.setAccountId(all.getAccountId());
                addInfoDto.setInformationType(all.getInformationType());
                Optional<R_GLOBAL_TYPE_VALUE> getValue = rGlobalTypeValueRepo.findByGlbTypeValId(all.getInformationType());
                addInfoDto.setInformationTypeVal(getValue.map(R_GLOBAL_TYPE_VALUE::getName).orElse(null));
                addInfoDto.setValueStr(all.getValue1());

                ValueIntDdl getStr = new ValueIntDdl();
                Optional<R_GLOBAL_TYPE_VALUE> getValues = rGlobalTypeValueRepo.findByGlbTypeValId(all.getValue2());
                getStr.setId(all.getValue2());
                getStr.setValue(getValues.map(R_GLOBAL_TYPE_VALUE::getName).orElse(null));

                addInfoDto.setValueIntDdl(getStr);
                addInfoDto.setIsDeleted(all.getIsDeleted());

                addInfoDto.setCreatedDate(all.getCreatedDate());
                addInfoDto.setCreatedBy(all.getCreatedBy());
                addInfoDto.setUpdatedDate(all.getUpdatedDate());
                addInfoDto.setUpdatedBy(all.getUpdatedBy());
                arrayList.add(addInfoDto);
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success To Proceed View Additional Information", arrayList);
            logger.info("Response Success ->" + result);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(), e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }

    public ResponseEntity<ResponseObject> softDelete (Integer id){
        ResponseObject result;
        try {
            Optional<M_AM_ADDITIONAL_INFORMATION> cekInfo = additionalInformationRepo.findById(id);
            if (cekInfo.isPresent()){
                M_AM_ADDITIONAL_INFORMATION getAddInfo = cekInfo.get();
                getAddInfo.setIsDeleted(Boolean.TRUE);
                getAddInfo.setUpdatedDate(new Date());
                getAddInfo.setUpdatedBy(UserDetailUtils.getUsername());

                additionalInformationRepo.save(getAddInfo);
            }
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success To Proceed Soft Delete Additional Information", null);
            logger.info("Response Success ->" + result);
            return new ResponseEntity<>(result, result.getHttpCode());
        }catch (Exception e) {
            logger.error(Constant.LOG_ERROR + e.getMessage(),e.getMessage(), e);
            ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                    ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(error, error.getHttpCode());
        }
    }

}
