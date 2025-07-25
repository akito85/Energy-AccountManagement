package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.M_GROUPACCESS;
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
public interface MGroupAccessRepo extends PagingAndSortingRepository<M_GROUPACCESS, Integer>, JpaSpecificationExecutor<M_GROUPACCESS> {
    Optional<M_GROUPACCESS> findTopByGaId(Integer gaId);

    @Query("SELECT u FROM M_GROUPACCESS u WHERE LOWER(u.name) = LOWER(:name)")
    Optional<M_GROUPACCESS> findByName(String name);

    List<M_GROUPACCESS> findAll();

    M_GROUPACCESS findByGaId(Integer gaId);

    List<M_GROUPACCESS> findTopByName(String name);

    Optional<M_GROUPACCESS> findByGaIdAndStatus(Integer gaId, String status);

    default Specification<M_GROUPACCESS> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_GROUPACCESS> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_GROUPACCESS>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_GROUPACCESS>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<M_GROUPACCESS> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_GROUPACCESS> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_GROUPACCESS> addDefaultFilters(Specification<M_GROUPACCESS> specification, Map<String, Object> filter, Boolean isFirst) {
        specification = (Specification<M_GROUPACCESS>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        return specification;
    }
    
    List<M_GROUPACCESS> findByUserLevelAndStatus(String userLevel, String status);

    List<M_GROUPACCESS> findAllByOrderByGaIdAsc();
    
    
    boolean existsByNameIgnoreCase(String name);
}
