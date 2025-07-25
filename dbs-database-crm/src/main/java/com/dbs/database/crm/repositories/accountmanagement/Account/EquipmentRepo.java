package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_AM_EQUIPMENT;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface EquipmentRepo extends PagingAndSortingRepository<M_AM_EQUIPMENT, Integer>, JpaSpecificationExecutor<M_AM_EQUIPMENT> {

    @SuppressWarnings("unchecked")
    default Specification<M_AM_EQUIPMENT> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_AM_EQUIPMENT> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_AM_EQUIPMENT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_AM_EQUIPMENT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }

        specification = addDefaultFilters(specification, filter,false);

        return specification;
    }

    default Specification<M_AM_EQUIPMENT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_AM_EQUIPMENT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<M_AM_EQUIPMENT> addDefaultFilters(Specification<M_AM_EQUIPMENT> specification, Map<String, Object> filter, Boolean isFirst){
        specification = specification == null ? (Specification<M_AM_EQUIPMENT>)
                PagingUtils.createIsDeletedFilter(specification, false, isFirst) : specification.and((Specification<M_AM_EQUIPMENT>)
                PagingUtils.createIsDeletedFilter(specification, false, isFirst));

        specification =specification.and((Specification<M_AM_EQUIPMENT>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR));

        return specification;
    }
}
