package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.M_ACTION;
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
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MActionRepo extends PagingAndSortingRepository<M_ACTION, Integer>, JpaSpecificationExecutor<M_ACTION> {
    Optional<M_ACTION> findTopByActionIdAndIsDeleted(Integer s, Boolean deleted);

    List<M_ACTION> findAllByIsDeleted(Boolean deleted);

    @Query("SELECT u FROM M_ACTION u WHERE LOWER(u.name) = LOWER(:name)")
    Optional<M_ACTION> findByName(String name);

    List<M_ACTION> findAll();

    List<M_ACTION> findAllByName(String name);

    List<M_ACTION> findAllByStatus(String status);

    default Specification<M_ACTION> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_ACTION> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_ACTION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_ACTION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<M_ACTION> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_ACTION> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_ACTION> addDefaultFilters(Specification<M_ACTION> specification, Map<String, Object> filter, Boolean isFirst) {
        if (filter.get("entityId") != null) {
            specification = (Specification<M_ACTION>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        }
        return specification;
    }
}
