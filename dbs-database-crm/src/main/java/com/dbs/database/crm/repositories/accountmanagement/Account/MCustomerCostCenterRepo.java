package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.database.crm.entities.accountmanagement.M_CUSTOMER_COST_CENTER;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MCustomerCostCenterRepo extends PagingAndSortingRepository<M_CUSTOMER_COST_CENTER, Integer>, JpaSpecificationExecutor<M_CUSTOMER_COST_CENTER> {
    List<M_CUSTOMER_COST_CENTER> findAllByCustomerId(Integer customerId);
    
    Optional<M_CUSTOMER_COST_CENTER> findBymCustomerEntityId(Integer mCustomerEntityId);
}
