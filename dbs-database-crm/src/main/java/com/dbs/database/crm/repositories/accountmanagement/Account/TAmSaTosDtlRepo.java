package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.T_AM_SA_TOS_DTL;
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
public interface TAmSaTosDtlRepo extends PagingAndSortingRepository<T_AM_SA_TOS_DTL,Integer>, JpaSpecificationExecutor<T_AM_SA_TOS_DTL> {

    @SuppressWarnings("unchecked")
    default Specification<T_AM_SA_TOS_DTL> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<T_AM_SA_TOS_DTL> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<T_AM_SA_TOS_DTL>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<T_AM_SA_TOS_DTL>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<T_AM_SA_TOS_DTL> getSpecificationDefault(Map<String, Object> filter) {
        Specification<T_AM_SA_TOS_DTL> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<T_AM_SA_TOS_DTL> addDefaultFilters(Specification<T_AM_SA_TOS_DTL> specification,
                                                                Map<String, Object> filter, Boolean isFirst) {
        specification = (Specification<T_AM_SA_TOS_DTL>) PagingUtils
                .createCommonColumnNumberEqualsFilter(specification, "saTosId", Long.parseLong(filter.get("saTosId").toString()), isFirst);
        return specification;
    }

    List<T_AM_SA_TOS_DTL> findAllBySaTosId(Integer saTosId);
}
