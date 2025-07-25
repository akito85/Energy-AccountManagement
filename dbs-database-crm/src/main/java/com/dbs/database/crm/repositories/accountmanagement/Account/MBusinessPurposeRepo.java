package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.database.crm.entities.accountmanagement.M_BUSINESS_PURPOSE;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MBusinessPurposeRepo extends PagingAndSortingRepository<M_BUSINESS_PURPOSE, Integer>, JpaSpecificationExecutor<M_BUSINESS_PURPOSE> {
    
    @Query("SELECT a FROM M_BUSINESS_PURPOSE a WHERE a.accountAddressId = :accountAddressId")
    List<M_BUSINESS_PURPOSE> findAllByAccountAddressId(Integer accountAddressId);
}
