package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.R_COSTCENTER_SIBLING;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface RCostCenterSiblingRepo extends PagingAndSortingRepository<R_COSTCENTER_SIBLING, Integer>, JpaSpecificationExecutor<R_COSTCENTER_SIBLING> {

    Optional<R_COSTCENTER_SIBLING> findByrDahIdAndSiblingIdAndStatus(Integer rDahId, Integer siblingId, String status);

    List<R_COSTCENTER_SIBLING> findByrDahIdAndStatus(Integer rDahId, String status);

    List<R_COSTCENTER_SIBLING> findAll();

    List<R_COSTCENTER_SIBLING> findAllBySiblingId(Integer siblingId);

    List<R_COSTCENTER_SIBLING> findAllByrDahIdAndSiblingIdAndStatus(Integer rDahId, Integer siblingId, String status);

    List<R_COSTCENTER_SIBLING> findAllByrDahIdAndStatus(Integer rDahId, String status);

    @Query("SELECT s FROM R_COSTCENTER_SIBLING s WHERE s.rDahId = :rDahId AND s.rsiblingId NOT IN :rsiblingId")
    List<R_COSTCENTER_SIBLING> findNotIn(Integer rDahId, List<Integer> rsiblingId);
}

