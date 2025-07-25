package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.database.crm.entities.accountmanagement.VW_ACCOUNT_CRITERIA;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWAccountCriteriaRepo extends JpaRepository<VW_ACCOUNT_CRITERIA, Integer> {
    
    Optional<VW_ACCOUNT_CRITERIA> findByAccountNumber(String accountNumber);

    Optional<VW_ACCOUNT_CRITERIA> findByAccountId(Integer accountId);
    
}
