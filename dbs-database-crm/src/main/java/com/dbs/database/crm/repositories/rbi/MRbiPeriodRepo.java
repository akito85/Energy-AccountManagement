package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_PERIOD;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value= "crmTransactionManager")
public interface MRbiPeriodRepo extends PagingAndSortingRepository<M_RBI_PERIOD, Integer>, JpaSpecificationExecutor<M_RBI_PERIOD> {

    List<M_RBI_PERIOD> findAllByBillingCycleIdAndStatus (Integer id, String status);
    
    List<M_RBI_PERIOD> findAll();

    @Query(nativeQuery = true, value = "SELECT * FROM M_RBI_PERIOD RP\n" +
            "LEFT JOIN M_RBI_CALCULATION_JOB CJ ON CJ.BILLING_PERIOD = RP.ID\n" +
            "WHERE CJ.CALCULATION_CODE = :calCode")
    M_RBI_PERIOD findPeriod (String calCode);

    @Query(value = "SELECT mrp.* FROM M_RBI_PERIOD mrp WHERE TO_CHAR(mrp.PERIOD, 'MON') = :month AND EXTRACT(YEAR FROM mrp.PERIOD) = :year AND BILLING_CYCLE_ID = :billingCycleId AND STATUS = :status", nativeQuery = true)
    List<M_RBI_PERIOD> findPeriodByBillCycleMonthYear(Integer billingCycleId, String month, int year, String status);

    List<M_RBI_PERIOD> findAllByStatus(String status);

    @SuppressWarnings("unchecked")
    default Specification<M_RBI_PERIOD> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<M_RBI_PERIOD> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_RBI_PERIOD>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_RBI_PERIOD>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<M_RBI_PERIOD> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_RBI_PERIOD> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<M_RBI_PERIOD> addDefaultFilters(Specification<M_RBI_PERIOD> specification,
                                                                  Map<String, Object> filter, Boolean isFirst) {
        specification = (Specification<M_RBI_PERIOD>) PagingUtils
                .createCommonColumnNumberEqualsFilter(specification, "billingCycleId", Long.parseLong(filter.get("billingCycleId").toString()), isFirst);
        return specification;
    }

    boolean existsByBillingCycleIdAndPeriod(Integer billingCycleId, Date period);

    boolean existsByIdNotAndBillingCycleIdAndPeriod(Integer id, Integer billingCycleId, Date period);
}