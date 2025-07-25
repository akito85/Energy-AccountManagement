package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.database.crm.entities.accountmanagement.M_AM_TAXIMPLICATION_CRITERIA_DATA;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MTaxImplicationCriteriaDataRepo extends PagingAndSortingRepository<M_AM_TAXIMPLICATION_CRITERIA_DATA, Integer>, JpaSpecificationExecutor<M_AM_TAXIMPLICATION_CRITERIA_DATA>{
    
    List<M_AM_TAXIMPLICATION_CRITERIA_DATA> findAllByStatus(String status);
    
    Optional<M_AM_TAXIMPLICATION_CRITERIA_DATA> findById(Integer id);
    
    @Query("SELECT a FROM M_AM_TAXIMPLICATION_CRITERIA_DATA a WHERE a.taximplicationId IN :taximplicationId")
    List<M_AM_TAXIMPLICATION_CRITERIA_DATA> findIn(List<Integer> taximplicationId);

    Optional<M_AM_TAXIMPLICATION_CRITERIA_DATA> findByIdAndTaximplicationId(Integer id, Integer taxImplicationId);

    List<M_AM_TAXIMPLICATION_CRITERIA_DATA> findAllByTaximplicationId(Integer taxImplicationId);
}
