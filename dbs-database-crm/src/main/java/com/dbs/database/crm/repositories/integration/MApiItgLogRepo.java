package com.dbs.database.crm.repositories.integration;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.integration.M_API_ITG_LOG;
import java.util.Map;
import org.springframework.data.jpa.domain.Specification;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(value = "crmTransactionManager")
public interface MApiItgLogRepo extends PagingAndSortingRepository <M_API_ITG_LOG, String>, JpaSpecificationExecutor<M_API_ITG_LOG> {
    default Specification<M_API_ITG_LOG> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_API_ITG_LOG> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_API_ITG_LOG>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_API_ITG_LOG>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        return specification;
    }

    default Specification<M_API_ITG_LOG> getSpecificationDefault(Map<String, Object> filter) {
        return null;
    }
}
