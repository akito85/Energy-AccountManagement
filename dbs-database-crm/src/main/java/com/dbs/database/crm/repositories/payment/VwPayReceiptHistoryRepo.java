package com.dbs.database.crm.repositories.payment;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.payment.view.VW_PAY_RECEIPT_HISTORY;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.dbs.common.base.utils.Constant.BETWEEN_SELECTOR;
import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwPayReceiptHistoryRepo extends PagingAndSortingRepository<VW_PAY_RECEIPT_HISTORY, Long>, JpaSpecificationExecutor<VW_PAY_RECEIPT_HISTORY> {

    @SuppressWarnings("unchecked")
    default Specification<VW_PAY_RECEIPT_HISTORY> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<VW_PAY_RECEIPT_HISTORY> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            if (sr.contains("receiptDate")) {
                specification =
                        i == 0 ?
                                (Specification<VW_PAY_RECEIPT_HISTORY>) where(PagingUtils.createSpecification(sr, BETWEEN_SELECTOR))
                                : specification.and((Specification<VW_PAY_RECEIPT_HISTORY>) PagingUtils.createSpecification(sr, BETWEEN_SELECTOR));

            } else {
                specification =
                        i == 0 ?
                                (Specification<VW_PAY_RECEIPT_HISTORY>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                                : specification.and((Specification<VW_PAY_RECEIPT_HISTORY>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));

            }
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<VW_PAY_RECEIPT_HISTORY> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_PAY_RECEIPT_HISTORY> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_PAY_RECEIPT_HISTORY> addDefaultFilters(Specification<VW_PAY_RECEIPT_HISTORY> specification,
                                                            Map<String, Object> filter, Boolean isFirst) {
        return specification;
    }
}
