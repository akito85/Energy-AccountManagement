package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.R_GROUPACCESS_ACTION;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional(value = "crmTransactionManager")
public interface RGroupAccessActionRepo extends JpaRepository<R_GROUPACCESS_ACTION, Integer> {
    List<R_GROUPACCESS_ACTION> findByGaMenuIdAndStatus(Integer gaMenuId, String status);

    List<R_GROUPACCESS_ACTION> findAllByActionId(Integer actionId);

    List<R_GROUPACCESS_ACTION> findByGaMenuId(Integer gaMenuId);

    Optional<R_GROUPACCESS_ACTION> findTopByActionIdAndGaMenuId(Integer actionId, Integer gaMenuId);
    
    Optional<R_GROUPACCESS_ACTION> findTopByActionIdAndGaMenuIdAndStatus(Integer actionId, Integer gaMenuId, String status);
    
    @Query("SELECT a FROM R_GROUPACCESS_ACTION a WHERE a.gaMenuId = :gaMenuId AND a.status = :status AND a.actionId NOT IN :actionId")
    List<R_GROUPACCESS_ACTION> findNotIn(Integer gaMenuId, String status, List<Integer> actionId);
    
    @Query("SELECT a FROM R_GROUPACCESS_ACTION a WHERE a.gaMenuId = :gaMenuId AND a.status = :status AND a.actionId IN :actionId")
    List<R_GROUPACCESS_ACTION> findIn(Integer gaMenuId, String status, List<Integer> actionId);
    
}
