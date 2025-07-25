package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.database.crm.entities.usermanagement.M_FORWARD_TASK;
import java.util.List;
import java.util.Optional;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MForwardTaskRepo extends PagingAndSortingRepository<M_FORWARD_TASK, Integer>, JpaSpecificationExecutor<M_FORWARD_TASK> {
    default Specification<M_FORWARD_TASK> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_FORWARD_TASK> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_FORWARD_TASK>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_FORWARD_TASK>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<M_FORWARD_TASK> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_FORWARD_TASK> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_FORWARD_TASK> addDefaultFilters(Specification<M_FORWARD_TASK> specification, Map<String, Object> filter, Boolean isFirst) {
        if (filter.get("entityId") != null) {
            specification = (Specification<M_FORWARD_TASK>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
            specification = (Specification<M_FORWARD_TASK>) PagingUtils.createIsDeletedFilter(specification, false, false);
        }
        return specification;
    }

    public Optional<M_FORWARD_TASK> findBytAppId(Integer tAppId);
    
    public List<M_FORWARD_TASK> findByforwardTaskHdrId(Integer forwardTaskHdrId);
    
    
}
