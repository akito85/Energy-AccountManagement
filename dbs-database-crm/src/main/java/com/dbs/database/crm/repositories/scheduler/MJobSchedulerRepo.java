package com.dbs.database.crm.repositories.scheduler;
import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.dbs.database.crm.entities.scheduler.M_JOB_SCHEDULER;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.domain.Specification;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MJobSchedulerRepo extends PagingAndSortingRepository<M_JOB_SCHEDULER, Long>, JpaSpecificationExecutor<M_JOB_SCHEDULER> {

    default Specification<M_JOB_SCHEDULER> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_JOB_SCHEDULER> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_JOB_SCHEDULER>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_JOB_SCHEDULER>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<M_JOB_SCHEDULER> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_JOB_SCHEDULER> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_JOB_SCHEDULER> addDefaultFilters(Specification<M_JOB_SCHEDULER> specification, Map<String, Object> filter, Boolean isFirst) {
        return specification;
    }
    
    public List<M_JOB_SCHEDULER> findAll();
    
}
