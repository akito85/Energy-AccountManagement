package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.VW_GAS_SOURCE;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface VWGasSourceRepo extends PagingAndSortingRepository<VW_GAS_SOURCE, Integer>, JpaSpecificationExecutor<VW_GAS_SOURCE> {
    default Specification<VW_GAS_SOURCE> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_GAS_SOURCE> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_GAS_SOURCE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_GAS_SOURCE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<VW_GAS_SOURCE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_GAS_SOURCE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_GAS_SOURCE> addDefaultFilters(Specification<VW_GAS_SOURCE> specification, Map<String, Object> filter, Boolean isFirst) {
        specification = (Specification<VW_GAS_SOURCE>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        return specification;
    }

    Page<VW_GAS_SOURCE> findAll(Specification<VW_GAS_SOURCE> specification, Pageable paging);
}
