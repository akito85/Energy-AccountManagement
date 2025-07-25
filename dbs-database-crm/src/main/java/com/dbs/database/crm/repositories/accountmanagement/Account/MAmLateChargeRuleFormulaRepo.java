package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.database.crm.entities.accountmanagement.M_AM_LATECHARGE_RULE_FORMULA;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MAmLateChargeRuleFormulaRepo extends PagingAndSortingRepository<M_AM_LATECHARGE_RULE_FORMULA, Integer>, JpaSpecificationExecutor<M_AM_LATECHARGE_RULE_FORMULA> {

    @Query(value = "SELECT * FROM M_AM_LATECHARGE_RULE_FORMULA WHERE M_AM_LATECHARGE_RULE_ID =:latechargeRuleId AND STATUS = 'ACTIVE'", nativeQuery = true)
    List<M_AM_LATECHARGE_RULE_FORMULA> findAllByLatechargeRuleIdAndStatus(Integer latechargeRuleId);
    
    List<M_AM_LATECHARGE_RULE_FORMULA> findAllByLatechargeRuleIdAndStatusOrderByIdAsc(Integer latechargeRuleId, String status);

    List<M_AM_LATECHARGE_RULE_FORMULA> findAllByLatechargeRuleId(Integer latechargeRuleId);
}
