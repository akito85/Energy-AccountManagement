package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.database.crm.entities.accountmanagement.M_SERVICE_POINT;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MServicePointRepo extends PagingAndSortingRepository<M_SERVICE_POINT, Integer>, JpaSpecificationExecutor<M_SERVICE_POINT> {
    
    List<M_SERVICE_POINT> findAllByAccountAddressId(Integer accountAddressId);

    boolean existsByServicePointName(Integer servicePointName);
    
    Optional<M_SERVICE_POINT> findByServicePointNameAndAccountAddressIdAndStatus(Integer servicePointName, Integer accountAddressId, String status);
    
    Optional<M_SERVICE_POINT> findByIdAndAccountAddressId(Integer id, Integer accountAddressId);
}
