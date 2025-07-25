package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.database.crm.entities.accountmanagement.M_AM_LATECHARGE_RULE_CONDITION;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MAmLateChargeRuleConditionRepo extends PagingAndSortingRepository<M_AM_LATECHARGE_RULE_CONDITION, Integer>, JpaSpecificationExecutor<M_AM_LATECHARGE_RULE_CONDITION> {
    List<M_AM_LATECHARGE_RULE_CONDITION> findAllByLatechargeRuleId(Integer lateChargeRuleId);
    List<M_AM_LATECHARGE_RULE_CONDITION> findAllByLatechargeRuleIdOrderByIdAsc(Integer lateChargeRuleId);
}
