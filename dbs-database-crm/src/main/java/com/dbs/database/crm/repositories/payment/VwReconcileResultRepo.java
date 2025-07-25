package com.dbs.database.crm.repositories.payment;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.payment.view.VW_RECONCILE_RESULT;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwReconcileResultRepo extends PagingAndSortingRepository<VW_RECONCILE_RESULT, Long>, JpaSpecificationExecutor<VW_RECONCILE_RESULT> {

    @SuppressWarnings("unchecked")
    default Specification<VW_RECONCILE_RESULT> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<VW_RECONCILE_RESULT> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_RECONCILE_RESULT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_RECONCILE_RESULT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<VW_RECONCILE_RESULT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_RECONCILE_RESULT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<VW_RECONCILE_RESULT> addDefaultFilters(Specification<VW_RECONCILE_RESULT> specification,
                                                                 Map<String, Object> filter, Boolean isFirst) {
        specification = (Specification<VW_RECONCILE_RESULT>)
                PagingUtils.createStatusFilter(specification, "ACTIVE", isFirst);

        if (!ObjectUtils.isEmpty(filter.get("bankStatementId")))
            specification = specification.and((Specification<VW_RECONCILE_RESULT>) PagingUtils.createBankStatementIdFilter(specification,
                    Long.parseLong(filter.get("bankStatementId").toString()), isFirst));

        if (!ObjectUtils.isEmpty(filter.get("type")))
            specification = specification.and((Specification<VW_RECONCILE_RESULT>) PagingUtils
                    .createCommonColumnVarcharEqualsFilter(specification, "type",
                    filter.get("type").toString(), isFirst));
        else
            specification = specification.and((Specification<VW_RECONCILE_RESULT>) PagingUtils
                    .createCommonColumnVarcharNotEqualsFilter(specification, "type",
                            "REVERSE", isFirst));

        if (!ObjectUtils.isEmpty(filter.get("statusApproval"))) {
            if (filter.get("statusApproval").equals("-"))
                specification = specification.and((Specification<VW_RECONCILE_RESULT>) PagingUtils
                        .createCommonColumnIsNullFilter(
                        specification, "statusApproval", isFirst));
            else
                specification = specification.and((Specification<VW_RECONCILE_RESULT>) PagingUtils
                    .createStatusApprovalFilter(specification,
                    filter.get("statusApproval").toString(), isFirst));
        }


        return specification;
    }
}
