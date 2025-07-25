package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.R_AM_SRC_DIST_DTL;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface SourceDistributionDetailRepo extends PagingAndSortingRepository<R_AM_SRC_DIST_DTL, Integer>, JpaSpecificationExecutor<R_AM_SRC_DIST_DTL> {

    default Specification<R_AM_SRC_DIST_DTL> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<R_AM_SRC_DIST_DTL> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<R_AM_SRC_DIST_DTL>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<R_AM_SRC_DIST_DTL>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        return specification;
    }

    default Specification<R_AM_SRC_DIST_DTL> getSpecificationDefault(Map<String, Object> filter) {
        return null;
    }

    default Specification<R_AM_SRC_DIST_DTL> addDefaultFilters(Specification<R_AM_SRC_DIST_DTL> specification, Boolean isFirst){
        return specification;
    }

    List<R_AM_SRC_DIST_DTL> findAllBySrcDistId(Integer srcDistId);
}
