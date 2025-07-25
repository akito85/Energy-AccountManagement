package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.GA_HISTORY;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface GAHistoryRepo extends JpaRepository<GA_HISTORY, Integer> {
    List<GA_HISTORY> findAllByUserId(Integer userId);
    
    Optional<GA_HISTORY> findTopByUserGaIdAndUserIdAndGaIdAndStatus(Integer userGaId, Integer userId, Integer gaId, String status);
}
