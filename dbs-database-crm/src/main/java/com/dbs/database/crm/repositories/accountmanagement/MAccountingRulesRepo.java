package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNTING_RULE;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MAccountingRulesRepo extends PagingAndSortingRepository<M_ACCOUNTING_RULE, Integer>, JpaSpecificationExecutor<M_ACCOUNTING_RULE> {
    Optional<M_ACCOUNTING_RULE> findByMasterAccountingRuleId(Integer masterAccountingRuleId);

    default Specification<M_ACCOUNTING_RULE> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_ACCOUNTING_RULE> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_ACCOUNTING_RULE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_ACCOUNTING_RULE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        return specification;
    }

    default Specification<M_ACCOUNTING_RULE> getSpecificationDefault(Map<String, Object> filter) {
        return null;
    }
    List<M_ACCOUNTING_RULE> findAllByStatus(String status);

    Optional<M_ACCOUNTING_RULE> findTopByClassificationTypeName(String classificationTypeName);

    Boolean existsByClassificationTypeName(String classificationTypeName);

    Optional<M_ACCOUNTING_RULE> findTopByClassificationTypeNameIgnoreCase(String classificationTypeName);

    Optional<M_ACCOUNTING_RULE> findTopByCodeIgnoreCase(String code);
}
