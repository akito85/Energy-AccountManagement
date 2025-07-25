package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.database.crm.entities.accountmanagement.VW_ACC_GAS_SOURCE;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwAccGasSourceRepo extends PagingAndSortingRepository<VW_ACC_GAS_SOURCE,Integer>, JpaSpecificationExecutor<VW_ACC_GAS_SOURCE> {
    List<VW_ACC_GAS_SOURCE> findByAccountNumberAndStatus(String accountNumber, String status);
}
