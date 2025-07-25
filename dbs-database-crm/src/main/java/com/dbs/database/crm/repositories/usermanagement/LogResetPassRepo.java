package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.LOG_RESET_PASSWORD;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface LogResetPassRepo extends PagingAndSortingRepository<LOG_RESET_PASSWORD, String>, JpaSpecificationExecutor<LOG_RESET_PASSWORD> {
    Optional<LOG_RESET_PASSWORD> findByLogId(String logId);

    Optional<LOG_RESET_PASSWORD> findFirstByLogIdAndStatus(String logId, String status);

    List<LOG_RESET_PASSWORD> findAllByUserId(Integer userId);
    
    List<LOG_RESET_PASSWORD> findAllByUserIdAndTypeReset(Integer userId, String typeReset);
}
