package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.database.crm.entities.accountmanagement.M_ASSETS_ASSIGNMENT_HISTORY;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MAssetAssignmentRepo extends PagingAndSortingRepository<M_ASSETS_ASSIGNMENT_HISTORY, Integer>, JpaSpecificationExecutor<M_ASSETS_ASSIGNMENT_HISTORY> {

    Page<M_ASSETS_ASSIGNMENT_HISTORY> findAllByServicePointId(Pageable pageable, Integer servicePointId);
    
    @Query("SELECT a FROM M_ASSETS_ASSIGNMENT_HISTORY a WHERE a.servicePointId = :servicePointId")
    List<M_ASSETS_ASSIGNMENT_HISTORY> findByServicePointId(Integer servicePointId);
    
    Optional<M_ASSETS_ASSIGNMENT_HISTORY> findByServicePointIdAndAssetIdAndUninstallDateIsNull(Integer servicePointId, Integer assetId);
    
    Optional<M_ASSETS_ASSIGNMENT_HISTORY> findByAssetIdAndStatus(Integer assetId, String status);
    
    List<M_ASSETS_ASSIGNMENT_HISTORY> findAllByServicePointIdAndUninstallDateIsNull(Integer servicePointId);

    Optional<M_ASSETS_ASSIGNMENT_HISTORY> findTopByServicePointIdOrderByCreatedDateDesc(Integer servicePointId);
    
}
