package com.dbs.database.crm.repositories.payment;

import com.dbs.database.crm.entities.payment.M_PAY_COLLECTING_AGENT;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface CollectingAgentRepo extends PagingAndSortingRepository<M_PAY_COLLECTING_AGENT, Long>, JpaSpecificationExecutor<M_PAY_COLLECTING_AGENT> {

    List<M_PAY_COLLECTING_AGENT> findAllByStatusAndStatusApproval(String status, String statusApproval);

    Optional<M_PAY_COLLECTING_AGENT> findByNameLikeIgnoreCaseAndStatusIgnoreCase(String name, String status);
}
