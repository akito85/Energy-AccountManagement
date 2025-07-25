package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.R_GAS_SOURCE_CRITERIA;
import com.dbs.database.crm.entities.usermanagement.T_EMP_ASSIGNMENT;
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
public interface RGasSourceCriteriaRepo extends PagingAndSortingRepository<R_GAS_SOURCE_CRITERIA, Integer>, JpaSpecificationExecutor<R_GAS_SOURCE_CRITERIA> {
    default Specification<R_GAS_SOURCE_CRITERIA> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<R_GAS_SOURCE_CRITERIA> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<R_GAS_SOURCE_CRITERIA>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<R_GAS_SOURCE_CRITERIA>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        return specification;
    }

    List<R_GAS_SOURCE_CRITERIA> findAll();

    Optional<R_GAS_SOURCE_CRITERIA> findByGasSourceCriteriaId(Integer gasSourceCriteriaId);

    @Query("SELECT a FROM R_GAS_SOURCE_CRITERIA a WHERE a.gasSourceId = :gasSourceId AND a.gasSourceCriteriaId NOT IN :gasSourceCriteriaId")
    List<R_GAS_SOURCE_CRITERIA> findNotIn(Integer gasSourceId, List<Integer> gasSourceCriteriaId);
    
    
    List<R_GAS_SOURCE_CRITERIA> findByCostCenterIdAndStatus(Integer costCenterId, String status);
    List<R_GAS_SOURCE_CRITERIA> findByGasSourceId(Integer gasSourceId);
}
