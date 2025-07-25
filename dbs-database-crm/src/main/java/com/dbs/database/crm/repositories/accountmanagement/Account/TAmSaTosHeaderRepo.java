package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.T_AM_SA_TOS_HEADER;
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
public interface TAmSaTosHeaderRepo extends PagingAndSortingRepository<T_AM_SA_TOS_HEADER,Integer>, JpaSpecificationExecutor<T_AM_SA_TOS_HEADER> {

    @SuppressWarnings("unchecked")
    default Specification<T_AM_SA_TOS_HEADER> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<T_AM_SA_TOS_HEADER> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<T_AM_SA_TOS_HEADER>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<T_AM_SA_TOS_HEADER>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<T_AM_SA_TOS_HEADER> getSpecificationDefault(Map<String, Object> filter) {
        Specification<T_AM_SA_TOS_HEADER> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<T_AM_SA_TOS_HEADER> addDefaultFilters(Specification<T_AM_SA_TOS_HEADER> specification,
                                                                 Map<String, Object> filter, Boolean isFirst) {
        return specification;
    }
}
