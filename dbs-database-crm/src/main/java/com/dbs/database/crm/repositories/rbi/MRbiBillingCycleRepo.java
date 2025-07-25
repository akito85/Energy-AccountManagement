package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_BILLING_CYCLE;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value= "crmTransactionManager")
public interface MRbiBillingCycleRepo extends PagingAndSortingRepository<M_RBI_BILLING_CYCLE, Integer>, JpaSpecificationExecutor<M_RBI_BILLING_CYCLE> {

    List<M_RBI_BILLING_CYCLE> findAll ();

    List<M_RBI_BILLING_CYCLE> findAllByStatus (String status);

    @SuppressWarnings("unchecked")
    default Specification<M_RBI_BILLING_CYCLE> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<M_RBI_BILLING_CYCLE> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_RBI_BILLING_CYCLE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_RBI_BILLING_CYCLE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<M_RBI_BILLING_CYCLE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_RBI_BILLING_CYCLE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<M_RBI_BILLING_CYCLE> addDefaultFilters(Specification<M_RBI_BILLING_CYCLE> specification,
                                                                        Map<String, Object> filter, Boolean isFirst) {
        specification = (Specification<M_RBI_BILLING_CYCLE>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        if (filter.get("positionId") != null) {
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()), GET_CC_PARENT);
            specification = (Specification<M_RBI_BILLING_CYCLE>) PagingUtils.createCostCenterFilter(specification, ccList, false);
        }
        return specification;
    }

    boolean existsByBeginCycleAndEndCycleAndTimeUnit(int beginCycle, int endCycle, String timeUnit);

    boolean existsByBillingCycleIdNotAndBeginCycleAndEndCycleAndTimeUnit(Integer billingCycleId, int beginCycle, int endCycle, String timeUnit);
}
