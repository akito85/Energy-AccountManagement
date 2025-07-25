package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.T_APPROVAL_DTL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface TApprovalDtlRepo extends JpaRepository<T_APPROVAL_DTL, Integer> {
    Optional<T_APPROVAL_DTL> findFirstByUserId(String userId);
}
