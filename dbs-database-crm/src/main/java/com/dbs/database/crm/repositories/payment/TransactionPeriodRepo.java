package com.dbs.database.crm.repositories.payment;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.payment.R_PAY_TRANSACTION_PERIOD;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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
public interface TransactionPeriodRepo extends PagingAndSortingRepository<R_PAY_TRANSACTION_PERIOD, Long>, JpaSpecificationExecutor<R_PAY_TRANSACTION_PERIOD> {

    @SuppressWarnings("unchecked")
    default Specification<R_PAY_TRANSACTION_PERIOD> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<R_PAY_TRANSACTION_PERIOD> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<R_PAY_TRANSACTION_PERIOD>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<R_PAY_TRANSACTION_PERIOD>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<R_PAY_TRANSACTION_PERIOD> getSpecificationDefault(Map<String, Object> filter) {
        Specification<R_PAY_TRANSACTION_PERIOD> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<R_PAY_TRANSACTION_PERIOD> addDefaultFilters(Specification<R_PAY_TRANSACTION_PERIOD> specification,
                                                                        Map<String, Object> filter, Boolean isFirst) {
        specification = (Specification<R_PAY_TRANSACTION_PERIOD>) PagingUtils
                .createCommonColumnNumberEqualsFilter(specification, "transactionCalendarId", Long.parseLong(filter.get("transactionCalendarId").toString()), isFirst);
        return specification;
    }

    @Query(value = "SELECT 1 FROM R_PAY_TRANSACTION_PERIOD WHERE SUBSTR(PERIOD, -4)=:year AND TRANSACTION_CALENDAR_ID=:transactionCalendarId", nativeQuery = true)
    List<Integer> findAllByTransactionCalendarIdAndYear(Long transactionCalendarId, String year);

    Optional<R_PAY_TRANSACTION_PERIOD> findFirstByTransactionCalendarIdOrderByEndDateDesc(Long transactionCalendarId);

    @Query(value = "SELECT CAST(SUBSTR(PERIOD, -4) AS INT) FROM R_PAY_TRANSACTION_PERIOD rptp WHERE TRANSACTION_CALENDAR_ID=:transactionCalendarId ORDER BY TRANSACTION_PERIOD_ID DESC FETCH FIRST 1 ROW ONLY", nativeQuery = true)
    Optional<Integer> findFirstYearByTransactionCalendarIdCustom(Long transactionCalendarId);
}
