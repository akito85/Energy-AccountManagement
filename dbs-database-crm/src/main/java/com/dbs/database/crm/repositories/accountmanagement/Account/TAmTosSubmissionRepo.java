package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.T_AM_TOS_SUBMISSION;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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
public interface TAmTosSubmissionRepo extends PagingAndSortingRepository<T_AM_TOS_SUBMISSION,Integer>, JpaSpecificationExecutor<T_AM_TOS_SUBMISSION> {

    @SuppressWarnings("unchecked")
    default Specification<T_AM_TOS_SUBMISSION> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<T_AM_TOS_SUBMISSION> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<T_AM_TOS_SUBMISSION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<T_AM_TOS_SUBMISSION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<T_AM_TOS_SUBMISSION> getSpecificationDefault(Map<String, Object> filter) {
        Specification<T_AM_TOS_SUBMISSION> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<T_AM_TOS_SUBMISSION> addDefaultFilters(Specification<T_AM_TOS_SUBMISSION> specification,
                                                       Map<String, Object> filter, Boolean isFirst) {
        return specification;
    }

    @Query(nativeQuery = true, value = "SELECT * FROM T_AM_TOS_SUBMISSION tats WHERE tats.T_AM_SA_ID = :saId AND tats.T_AM_SA_TOS_ID = :saTosId AND (tats.STATUS = :status OR tats.STATUS_APPROVAL = :statusApproval)")
    List<T_AM_TOS_SUBMISSION> findStatusIn(Integer saId, Integer saTosId, String status, String statusApproval);

    List<T_AM_TOS_SUBMISSION> findAllBySaTosIdAndSaIdAndStatusApprovalIn(Integer saTosId, Integer saId, List<String> statusApproval);
}
