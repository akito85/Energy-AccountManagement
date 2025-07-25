package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.T_DELEGATION;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional(value = "crmTransactionManager")
public interface TDelegationRepo extends JpaRepository<T_DELEGATION, Integer> {
    List<T_DELEGATION> findAllByRequestor(String requestor);
    List<T_DELEGATION> findAllByDelegateTo(String delegateTo);
}
