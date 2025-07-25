package com.dbs.database.crm.repositories.payment;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.payment.view.VW_T_LATE_CHARGE;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwTLateChargeRepo extends PagingAndSortingRepository<VW_T_LATE_CHARGE, Long>, JpaSpecificationExecutor<VW_T_LATE_CHARGE> {

    @SuppressWarnings("unchecked")
    default Specification<VW_T_LATE_CHARGE> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<VW_T_LATE_CHARGE> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_T_LATE_CHARGE>) where(PagingUtils.createSpecificationPayment(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_T_LATE_CHARGE>) PagingUtils.createSpecificationPayment(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<VW_T_LATE_CHARGE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_T_LATE_CHARGE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_T_LATE_CHARGE> addDefaultFilters(Specification<VW_T_LATE_CHARGE> specification,
                                                                 Map<String, Object> filter, Boolean isFirst) {
        return specification;
    }
}
