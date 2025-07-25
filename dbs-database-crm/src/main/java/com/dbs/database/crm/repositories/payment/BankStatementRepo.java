package com.dbs.database.crm.repositories.payment;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.payment.M_BANK_STATEMENT;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.BETWEEN_SELECTOR;
import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface BankStatementRepo extends PagingAndSortingRepository<M_BANK_STATEMENT, Long>, JpaSpecificationExecutor<M_BANK_STATEMENT>, JpaRepository<M_BANK_STATEMENT, Long> {

    @SuppressWarnings("unchecked")
    default Specification<M_BANK_STATEMENT> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<M_BANK_STATEMENT> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            if (sr.contains("bankStatementDate")) {
                specification =
                        i == 0 ?
                                (Specification<M_BANK_STATEMENT>) where(PagingUtils.createSpecification(sr, BETWEEN_SELECTOR))
                                : specification.and((Specification<M_BANK_STATEMENT>) PagingUtils.createSpecification(sr, BETWEEN_SELECTOR));

            } else {
                specification =
                        i == 0 ?
                                (Specification<M_BANK_STATEMENT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                                : specification.and((Specification<M_BANK_STATEMENT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));

            }
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<M_BANK_STATEMENT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_BANK_STATEMENT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_BANK_STATEMENT> addDefaultFilters(Specification<M_BANK_STATEMENT> specification,
                                                    Map<String, Object> filter, Boolean isFirst) {
        return specification;
    }

    Optional<M_BANK_STATEMENT> findByUuidIgnoreCase(String uuid);

    boolean existsByFilenameIgnoreCase(String filename);
}
