package com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement;

import com.dbs.database.crm.entities.accountmanagement.T_AM_SA_PRC_RULE_DTL;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface TAmSAPrcRuleRepo extends PagingAndSortingRepository<T_AM_SA_PRC_RULE_DTL, Integer>, JpaSpecificationExecutor<T_AM_SA_PRC_RULE_DTL> {
    List<T_AM_SA_PRC_RULE_DTL> findAllBySaId(Integer saId);
}
