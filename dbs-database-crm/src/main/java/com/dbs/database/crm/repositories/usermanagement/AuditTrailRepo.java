package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface AuditTrailRepo extends JpaRepository<AUDIT_TRAIL, Integer> {
    Optional<AUDIT_TRAIL> findTopByTableNameOrderByCreatedDateDesc(String tableName);
    
    List<AUDIT_TRAIL> findAllByDataIdAndOperationOrderByCreatedDateDesc(String dataId, String operation);
}
