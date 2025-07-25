package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.LOG_SYS_SERVICES;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(value = "crmTransactionManager")
public interface LogSysServicesRepo extends PagingAndSortingRepository <LOG_SYS_SERVICES, Integer>, JpaSpecificationExecutor<LOG_SYS_SERVICES> {

}
