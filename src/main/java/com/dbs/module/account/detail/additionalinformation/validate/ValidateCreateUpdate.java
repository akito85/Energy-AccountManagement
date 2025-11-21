package com.dbs.module.account.detail.additionalinformation.validate;

import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.database.crm.entities.accountmanagement.M_AM_ADDITIONAL_INFORMATION;
import com.dbs.database.crm.repositories.accountmanagement.Account.AdditionalInformationRepo;
import com.dbs.module.account.detail.additionalinformation.dto.CreateUpdateAddInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.Objects;
import java.util.Optional;

@Service
public class ValidateCreateUpdate {
    @Autowired
    private Validator validator;
    @Autowired
    private AdditionalInformationRepo additionalInformationRepo;

    public ResponseObject validateCreateUpdateAddInfo (CreateUpdateAddInfoDto createUpdateAddInfoDto){
        ResponseObject result;
        if (createUpdateAddInfoDto.getInformationType()==null){
            result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                    "Category Cannot Be Null", null);
            return result;
        }

        Optional<M_AM_ADDITIONAL_INFORMATION> cekInfo =
                additionalInformationRepo.findByInformationTypeAndAccountIdAndIsDeleted(createUpdateAddInfoDto.getInformationType(),createUpdateAddInfoDto.getAccountId(),Boolean.FALSE);
        if (cekInfo.isPresent()){
            M_AM_ADDITIONAL_INFORMATION getAddInfo = cekInfo.get();
            if (!(Objects.equals(createUpdateAddInfoDto.getInformationType(), createUpdateAddInfoDto.getInformationType()))){
                if (Objects.equals(getAddInfo.getInformationType(), createUpdateAddInfoDto.getInformationType())) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "Category Already Exist", null);
                    return result;
                }
            }
        }
        return null;
    }
}
