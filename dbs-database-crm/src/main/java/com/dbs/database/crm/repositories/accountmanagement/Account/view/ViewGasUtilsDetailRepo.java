package com.dbs.database.crm.repositories.accountmanagement.Account.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.view.VW_AM_GAS_UTILS_DTL;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface ViewGasUtilsDetailRepo extends PagingAndSortingRepository<VW_AM_GAS_UTILS_DTL, Integer>, JpaSpecificationExecutor<VW_AM_GAS_UTILS_DTL> {

    default Specification<VW_AM_GAS_UTILS_DTL> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_AM_GAS_UTILS_DTL> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_AM_GAS_UTILS_DTL>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_AM_GAS_UTILS_DTL>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        return specification;
    }

    default Specification<VW_AM_GAS_UTILS_DTL> getSpecificationDefault(Map<String, Object> filter) {
        return null;
    }

    default Specification<VW_AM_GAS_UTILS_DTL> addDefaultFilters(Specification<VW_AM_GAS_UTILS_DTL> specification, Boolean isFirst){
        return specification;
    }

    Optional<VW_AM_GAS_UTILS_DTL> findById(Integer id);

}
