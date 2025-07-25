package com.dbs.database.crm.repositories.payment;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.payment.M_PAY_TRANSACTION_CALENDAR;
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
public interface TransactionCalendarRepo extends PagingAndSortingRepository<M_PAY_TRANSACTION_CALENDAR, Long>, JpaSpecificationExecutor<M_PAY_TRANSACTION_CALENDAR> {

    @SuppressWarnings("unchecked")
    default Specification<M_PAY_TRANSACTION_CALENDAR> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<M_PAY_TRANSACTION_CALENDAR> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_PAY_TRANSACTION_CALENDAR>) where(PagingUtils.createSpecificationPayment(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_PAY_TRANSACTION_CALENDAR>) PagingUtils.createSpecificationPayment(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<M_PAY_TRANSACTION_CALENDAR> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_PAY_TRANSACTION_CALENDAR> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_PAY_TRANSACTION_CALENDAR> addDefaultFilters(Specification<M_PAY_TRANSACTION_CALENDAR> specification,
                                                                        Map<String, Object> filter, Boolean isFirst) {
        return specification;
    }

    List<M_PAY_TRANSACTION_CALENDAR> findAll();

    Optional<M_PAY_TRANSACTION_CALENDAR> findByIdAndStatusApproval(Long id, String statusApproval);

    boolean existsByBeginCycleAndEndCycleAndTimeUnitId(Integer beginCycle, Integer endCycle, Integer timeUnitId);
    boolean existsByIdNotAndBeginCycleAndEndCycleAndTimeUnitId(Long id, Integer beginCycle, Integer endCycle, Integer timeUnitId);

    boolean existsByBeginCycle(Integer beginCycle);
    boolean existsByIdNotAndBeginCycle(Long id, Integer beginCycle);
    boolean existsByEndCycle(Integer endCycle);
    boolean existsByIdNotAndEndCycle(Long id, Integer endCycle);
    boolean existsByTimeUnitId(Integer timeUnitId);
    boolean existsByIdNotAndTimeUnitId(Long id, Integer timeUnitId);
}
