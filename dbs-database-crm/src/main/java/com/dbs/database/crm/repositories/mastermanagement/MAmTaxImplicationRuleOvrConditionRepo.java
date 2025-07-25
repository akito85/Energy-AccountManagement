package com.dbs.database.crm.repositories.mastermanagement;
import com.dbs.database.crm.entities.accountmanagement.M_AM_TAXIMPLICATION_RULE_OVR_CONDITION;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MAmTaxImplicationRuleOvrConditionRepo extends PagingAndSortingRepository<M_AM_TAXIMPLICATION_RULE_OVR_CONDITION, Integer>, JpaSpecificationExecutor<M_AM_TAXIMPLICATION_RULE_OVR_CONDITION> {
    List<M_AM_TAXIMPLICATION_RULE_OVR_CONDITION> findAllByTaximplicationRuleOvrId(Integer ruleOvrId);
    List<M_AM_TAXIMPLICATION_RULE_OVR_CONDITION> findAllByTaximplicationRuleOvrIdOrderByIdAsc(Integer ruleOvrId);
}
