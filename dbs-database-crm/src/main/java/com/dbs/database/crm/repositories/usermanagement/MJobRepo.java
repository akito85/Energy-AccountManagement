package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.M_JOB;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.database.crm.entities.usermanagement.M_USER;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MJobRepo extends PagingAndSortingRepository<M_JOB, Integer>, JpaSpecificationExecutor<M_JOB> {
    default Specification<M_JOB> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_JOB> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_JOB>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_JOB>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<M_JOB> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_JOB> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_JOB> addDefaultFilters(Specification<M_JOB> specification, Map<String, Object> filter, Boolean isFirst) {
        if (filter.get("entityId") != null) {
            specification = (Specification<M_JOB>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
            specification = (Specification<M_JOB>) PagingUtils.createIsDeletedFilter(specification, false, false);
        }
        return specification;
    }
    @Query("SELECT u FROM M_JOB u WHERE LOWER(u.jobName) = LOWER(:jobName)")
    M_JOB findFirstByJobName(@Param("jobName") String jobName);
    
    List<M_JOB> findByJobName(String jobName);

    M_JOB findTopByEntityIdAndIsDeletedAndJobId(Integer entity, Boolean isDeleted, Integer jobId);

    List<M_JOB> findAllByEntityIdAndIsDeleted(Integer entity, Boolean isDeleted);

    Optional<M_JOB> findByJobId(Integer jobId);

    List<M_JOB> findAllByStatusAndIsDeleted(String status, Boolean isDeleted);
    
    List<M_JOB> findAllByStatusAndIsDeletedAndEntityId(String status, Boolean isDeleted, Integer entityId);

    List<M_JOB> findAllByIsDeleted(Boolean isDeleted);

    List<M_JOB> findAllByOrderByJobId();

    List<M_JOB> findAll();
}
