package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


import static com.dbs.common.base.utils.Constant.*;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_PERIOD;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_RBI_PERIOD;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwRbiPeriodeRepo extends PagingAndSortingRepository<VW_RBI_PERIOD,Integer>, JpaSpecificationExecutor<VW_RBI_PERIOD> {
    default Specification<VW_RBI_PERIOD> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_RBI_PERIOD> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_RBI_PERIOD>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_RBI_PERIOD>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter,false);
        return specification;
    }

    default Specification<VW_RBI_PERIOD> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_RBI_PERIOD> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_RBI_PERIOD> addDefaultFilters(Specification<VW_RBI_PERIOD> specification, Map<String, Object> filter, Boolean isFirst){
       specification = (Specification<VW_RBI_PERIOD>) PagingUtils
                .createCommonColumnNumberEqualsFilter(specification, "billingCycleId", Long.parseLong(filter.get("billingCycleId").toString()), isFirst);
        return specification;
    }

    List<VW_RBI_PERIOD> findAllByBillingCycleId(Integer billingCycleId);
    
}

