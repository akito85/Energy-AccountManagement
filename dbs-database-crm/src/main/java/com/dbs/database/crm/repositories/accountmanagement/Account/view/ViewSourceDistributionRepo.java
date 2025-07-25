package com.dbs.database.crm.repositories.accountmanagement.Account.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.view.VW_AM_SRC_DIST;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface ViewSourceDistributionRepo extends PagingAndSortingRepository<VW_AM_SRC_DIST, Integer>, JpaSpecificationExecutor<VW_AM_SRC_DIST> {

    @SuppressWarnings("unchecked")
    default Specification<VW_AM_SRC_DIST> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_AM_SRC_DIST> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_AM_SRC_DIST>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_AM_SRC_DIST>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }

        specification = addDefaultFilters(specification, filter,false);

        return specification;
    }

    default Specification<VW_AM_SRC_DIST> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_AM_SRC_DIST> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<VW_AM_SRC_DIST> addDefaultFilters(Specification<VW_AM_SRC_DIST> specification, Map<String, Object> filter, Boolean isFirst){
        specification = (Specification<VW_AM_SRC_DIST>)
                PagingUtils.createIsDeletedFilter(specification, false, isFirst);

        specification =specification.and((Specification<VW_AM_SRC_DIST>) PagingUtils.createSpecification("typeDist~" + filter.get("typeDist"), EQUALS_SELECTOR));

        specification =specification.and((Specification<VW_AM_SRC_DIST>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR));

        return specification;
    }
    Optional<VW_AM_SRC_DIST> findTopByAccountIdAndTypeDistAndIsDeletedIsFalseOrderByEffectiveDateDesc(Integer accountId, String typeDist);

    Optional<VW_AM_SRC_DIST> findByIdAndTypeDist(Integer id, String typeDist);

}
