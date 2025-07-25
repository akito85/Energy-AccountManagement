package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.T_AM_TOS_SUBMISSION_DTL;
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
public interface TAmTosSubmissionDetailRepo extends PagingAndSortingRepository<T_AM_TOS_SUBMISSION_DTL,Integer>, JpaSpecificationExecutor<T_AM_TOS_SUBMISSION_DTL> {

    @SuppressWarnings("unchecked")
    default Specification<T_AM_TOS_SUBMISSION_DTL> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<T_AM_TOS_SUBMISSION_DTL> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<T_AM_TOS_SUBMISSION_DTL>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<T_AM_TOS_SUBMISSION_DTL>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<T_AM_TOS_SUBMISSION_DTL> getSpecificationDefault(Map<String, Object> filter) {
        Specification<T_AM_TOS_SUBMISSION_DTL> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<T_AM_TOS_SUBMISSION_DTL> addDefaultFilters(Specification<T_AM_TOS_SUBMISSION_DTL> specification,
                                                                 Map<String, Object> filter, Boolean isFirst) {
        specification = (Specification<T_AM_TOS_SUBMISSION_DTL>) PagingUtils
                .createCommonColumnNumberEqualsFilter(specification, "tosSubmissionId", Long.parseLong(filter.get("tosSubmissionId").toString()), isFirst);
        return specification;
    }

    List<T_AM_TOS_SUBMISSION_DTL> findAllByTosSubmissionId(Integer tosSubmissionId);
}
