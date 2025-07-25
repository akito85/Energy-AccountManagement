package com.dbs.database.crm.repositories.payment;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.payment.view.VW_RECEIPT_ALLOCATION_V1;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.BETWEEN_SELECTOR;
import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwReceiptAllocationRepoV1 extends PagingAndSortingRepository<VW_RECEIPT_ALLOCATION_V1, Long>, JpaSpecificationExecutor<VW_RECEIPT_ALLOCATION_V1> {

    @SuppressWarnings("unchecked")
    default Specification<VW_RECEIPT_ALLOCATION_V1> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<VW_RECEIPT_ALLOCATION_V1> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            if (sr.contains("allocationDate")) {
                specification =
                        i == 0 ?
                                (Specification<VW_RECEIPT_ALLOCATION_V1>) where(PagingUtils.createSpecification(sr, BETWEEN_SELECTOR))
                                : specification.and((Specification<VW_RECEIPT_ALLOCATION_V1>) PagingUtils.createSpecification(sr, BETWEEN_SELECTOR));

            } else {
                specification =
                        i == 0 ?
                                (Specification<VW_RECEIPT_ALLOCATION_V1>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                                : specification.and((Specification<VW_RECEIPT_ALLOCATION_V1>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            }
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<VW_RECEIPT_ALLOCATION_V1> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_RECEIPT_ALLOCATION_V1> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<VW_RECEIPT_ALLOCATION_V1> addDefaultFilters(Specification<VW_RECEIPT_ALLOCATION_V1> specification,
                                                                      Map<String, Object> filter, Boolean isFirst) {
        specification = (Specification<VW_RECEIPT_ALLOCATION_V1>) PagingUtils.createReceiptIdFilter(specification,
                Long.parseLong(filter.get("receiptId").toString()), isFirst);
        return specification;
    }

    List<VW_RECEIPT_ALLOCATION_V1> findAllByReceiptId(Long receiptId);
}
