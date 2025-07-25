package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNT_GAS_SOURCE;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface MAccountGasSourceRepo extends PagingAndSortingRepository<M_ACCOUNT_GAS_SOURCE, Integer>, JpaSpecificationExecutor<M_ACCOUNT_GAS_SOURCE> {
    Optional<M_ACCOUNT_GAS_SOURCE> findByAccountId(Integer accountId);

    Optional<M_ACCOUNT_GAS_SOURCE> findById(Integer id);

    List<M_ACCOUNT_GAS_SOURCE> findByAccountIdAndStatus(Integer accountId, String status);
}
