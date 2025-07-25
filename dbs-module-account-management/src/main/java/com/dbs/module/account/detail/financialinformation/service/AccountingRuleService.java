package com.dbs.module.account.detail.financialinformation.service;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNT;
import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNTING_RULE;
import com.dbs.database.crm.repositories.accountmanagement.Account.MAccountRepo;
import com.dbs.database.crm.repositories.accountmanagement.MAccountingRulesRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountingRuleService {
    private static final Logger logger = LoggerFactory.getLogger(AccountingRuleService.class);
    
    private final MAccountingRulesRepo accountingRulesRepo;
    private final MAccountRepo accountRepo;

    public AccountingRuleService(MAccountingRulesRepo accountingRulesRepo, MAccountRepo accountRepo) {
        this.accountingRulesRepo = accountingRulesRepo;
        this.accountRepo = accountRepo;
    }
    
    public ResponseEntity<ResponseObject> detail(Integer accountId) {
        try {
            Optional<M_ACCOUNT> idClassType = accountRepo.findByAccountId(accountId);
            Optional<M_ACCOUNTING_RULE> mAccountingRule = accountingRulesRepo.findByMasterAccountingRuleId(idClassType.get().getAccountRuleId());
            if (mAccountingRule.isEmpty()){
                return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Id not found", ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
            }
            M_ACCOUNTING_RULE data = new M_ACCOUNTING_RULE();
            data.setReceivableAccount(mAccountingRule.get().getCode() + " - " + mAccountingRule.get().getClassificationTypeName() + " - " + mAccountingRule.get().getReceivableAccount());
            data.setRevenueAccount(mAccountingRule.get().getCode() + " - " + mAccountingRule.get().getClassificationTypeName() + " - " + mAccountingRule.get().getRevenueAccount());
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    ResponseUtils.MESSAGE_OK, data), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
