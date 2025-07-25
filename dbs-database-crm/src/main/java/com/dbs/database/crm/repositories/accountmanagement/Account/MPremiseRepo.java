package com.dbs.database.crm.repositories.accountmanagement.Account;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_PREMISE;

import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MPremiseRepo extends PagingAndSortingRepository<M_PREMISE, Integer>, JpaSpecificationExecutor<M_PREMISE> {
   
    default Specification<M_PREMISE> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_PREMISE> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_PREMISE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_PREMISE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }

        specification = addDefaultFilters(specification, filter,false);

        return specification;
    }

    default Specification<M_PREMISE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_PREMISE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_PREMISE> addDefaultFilters(Specification<M_PREMISE> specification, Map<String, Object> filter, Boolean isFirst){
        return specification;
    }  

    Optional<M_PREMISE> findAllByAccountId(Integer accountId);
}
