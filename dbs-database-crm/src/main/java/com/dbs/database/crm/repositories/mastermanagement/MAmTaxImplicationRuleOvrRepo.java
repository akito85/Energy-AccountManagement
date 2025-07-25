package com.dbs.database.crm.repositories.mastermanagement;

import com.dbs.database.crm.entities.accountmanagement.M_AM_TAXIMPLICATION_RULE_OVR;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MAmTaxImplicationRuleOvrRepo extends PagingAndSortingRepository<M_AM_TAXIMPLICATION_RULE_OVR, Integer>, JpaSpecificationExecutor<M_AM_TAXIMPLICATION_RULE_OVR> {
    List<M_AM_TAXIMPLICATION_RULE_OVR> findAllByTaximplicationRuleId(Integer ruleId);
    List<M_AM_TAXIMPLICATION_RULE_OVR> findAllByTaximplicationRuleIdOrderByIdAsc(Integer ruleId);
}
