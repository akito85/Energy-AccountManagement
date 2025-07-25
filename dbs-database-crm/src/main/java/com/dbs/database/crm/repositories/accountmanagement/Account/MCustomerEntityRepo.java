package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.database.crm.entities.accountmanagement.M_CUSTOMER_ENTITY;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MCustomerEntityRepo extends PagingAndSortingRepository<M_CUSTOMER_ENTITY, Integer>, JpaSpecificationExecutor<M_CUSTOMER_ENTITY> {
    
    Optional<M_CUSTOMER_ENTITY> findByCustomerId(Integer customerId);
}
