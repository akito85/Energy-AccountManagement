package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.LOG_CHANGE_AUTH_TYPE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface LChangeAuthTypeRepo extends JpaRepository<LOG_CHANGE_AUTH_TYPE, Integer> {
}
