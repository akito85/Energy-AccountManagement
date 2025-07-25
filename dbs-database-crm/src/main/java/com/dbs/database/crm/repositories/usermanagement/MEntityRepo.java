package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.M_ENTITY;
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
public interface MEntityRepo extends PagingAndSortingRepository<M_ENTITY, Integer>, JpaSpecificationExecutor<M_ENTITY> {
    Optional<M_ENTITY> findByEntityId(Integer entityId);

    List<M_ENTITY> findAll();

    M_ENTITY findTopByEntityId(Integer entityId);

    M_ENTITY findByEntityCode(String entityCode);

    @Query("SELECT u FROM M_ENTITY u WHERE LOWER(u.entityName) = LOWER(:entityName)")
    List<M_ENTITY> findByEntityName(String entityName);

    List<M_ENTITY> findAllByEntityName(String entityName);

    default Specification<M_ENTITY> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_ENTITY> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_ENTITY>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_ENTITY>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        return specification;
    }

    default Specification<M_ENTITY> getSpecificationDefault(Map<String, Object> filter) {
        return null;
    }

    List<M_ENTITY> findAllByEntityIdAndStatus(Integer entityId, String status);
}
