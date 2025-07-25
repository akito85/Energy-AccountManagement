package com.dbs.database.crm.repositories.payment;

import com.dbs.database.crm.entities.payment.R_PAY_ACCOUNT_BANK_CRITERIA;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface AccountBankCriteriaRepo extends PagingAndSortingRepository<R_PAY_ACCOUNT_BANK_CRITERIA, Long>, JpaSpecificationExecutor<R_PAY_ACCOUNT_BANK_CRITERIA> {

    List<R_PAY_ACCOUNT_BANK_CRITERIA> findAllByAccountInformationId(Long accountInformationId);

    void deleteAllByIdNotInAndAccountInformationId(List<Long> idList, Long accountInformationId);
}
