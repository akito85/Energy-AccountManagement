package com.dbs.database.crm.repositories.scheduler;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.dbs.database.crm.entities.scheduler.R_JOB_SCHEDULER_LOG_DTL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface RJobSchedulerLogDtlRepo extends PagingAndSortingRepository<R_JOB_SCHEDULER_LOG_DTL, String>, JpaSpecificationExecutor<R_JOB_SCHEDULER_LOG_DTL> {
    
}
