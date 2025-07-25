package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.database.crm.entities.usermanagement.M_DELEGATION;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MDelegationRepo extends PagingAndSortingRepository<M_DELEGATION, Integer>, JpaSpecificationExecutor<M_DELEGATION> {
    default Specification<M_DELEGATION> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_DELEGATION> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_DELEGATION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_DELEGATION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<M_DELEGATION> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_DELEGATION> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_DELEGATION> addDefaultFilters(Specification<M_DELEGATION> specification, Map<String, Object> filter, Boolean isFirst) {
        if (filter.get("entityId") != null) {
            specification = (Specification<M_DELEGATION>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
            specification = (Specification<M_DELEGATION>) PagingUtils.createIsDeletedFilter(specification, false, false);
        }
        return specification;
    }
}
