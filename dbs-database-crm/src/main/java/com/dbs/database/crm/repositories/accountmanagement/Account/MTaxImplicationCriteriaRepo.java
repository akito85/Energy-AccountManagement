package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.database.crm.entities.accountmanagement.M_AM_TAXIMPLICATION_CRITERIA;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MTaxImplicationCriteriaRepo extends PagingAndSortingRepository<M_AM_TAXIMPLICATION_CRITERIA, Integer>, JpaSpecificationExecutor<M_AM_TAXIMPLICATION_CRITERIA>{
    
    List<M_AM_TAXIMPLICATION_CRITERIA> findAllByStatus(String status);
    
    Optional<M_AM_TAXIMPLICATION_CRITERIA> findById(Integer id);

    Optional<M_AM_TAXIMPLICATION_CRITERIA> findByIdAndTaximplicationId(Integer id, Integer taxImplicationId);

    List<M_AM_TAXIMPLICATION_CRITERIA> findAllByTaximplicationId(Integer taxImplicationId);
}
