package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.M_USER_DELEGATION;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional(value = "crmTransactionManager")
public interface MUserDelegationRepo extends PagingAndSortingRepository<M_USER_DELEGATION, Integer>, JpaSpecificationExecutor<M_USER_DELEGATION> {
    @Query(value = "SELECT MUD.* FROM M_USER_DELEGATION MUD WHERE MUD.DELEGATE_FROM = :delegateFrom AND MUD.DELEGATE_TO = :delegateTo AND (MUD.STATUS='ACTIVE' OR MUD.STATUS='WAITING_APPROVAL') AND MUD.START_DATE<=CAST(TRUNC(SYSDATE) AS TIMESTAMP) AND MUD.END_DATE>=CAST(TRUNC(SYSDATE) AS TIMESTAMP)", nativeQuery = true)
    List<M_USER_DELEGATION> delegatorValidation(Integer delegateFrom, Integer delegateTo);

    Optional<M_USER_DELEGATION> findById(Integer integer);

    @Query(value = "SELECT 1 FROM M_USER_DELEGATION MUD WHERE MUD.DELEGATE_FROM=:delegator AND MUD.DELEGATE_TO=:delegateTo AND (STATUS='ACTIVE' OR STATUS='WAITING_APPROVAL') AND MUD.START_DATE<=CAST(TRUNC(SYSDATE) AS TIMESTAMP) AND MUD.END_DATE>=CAST(TRUNC(SYSDATE) AS TIMESTAMP)", nativeQuery = true)
    List<Object[]> existsByDelegateFromAndDelegateToAndStatus(Integer delegator, Integer delegateTo);
}
