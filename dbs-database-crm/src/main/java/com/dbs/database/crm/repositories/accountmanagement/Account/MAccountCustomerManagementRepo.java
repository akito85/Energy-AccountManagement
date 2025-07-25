package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNT_CUSTOMER_MANAGEMENT;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MAccountCustomerManagementRepo extends PagingAndSortingRepository<M_ACCOUNT_CUSTOMER_MANAGEMENT, Integer>, JpaSpecificationExecutor<M_ACCOUNT_CUSTOMER_MANAGEMENT> {
    Optional<M_ACCOUNT_CUSTOMER_MANAGEMENT> findByAccountIdAndStatus(Integer accountId, String status);
}
