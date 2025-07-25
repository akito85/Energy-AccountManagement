package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.database.crm.entities.usermanagement.M_MAINTENANCE_MODE;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MMaintenanceModeRepo extends PagingAndSortingRepository<M_MAINTENANCE_MODE, Integer>, JpaSpecificationExecutor<M_MAINTENANCE_MODE> {

    default Specification<M_MAINTENANCE_MODE> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_MAINTENANCE_MODE> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_MAINTENANCE_MODE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_MAINTENANCE_MODE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        return specification;
    }

    default Specification<M_MAINTENANCE_MODE> getSpecificationDefault(Map<String, Object> filter) {
        return null;
    }

    default Specification<M_MAINTENANCE_MODE> addDefaultFilters(Specification<M_MAINTENANCE_MODE> specification, Boolean isFirst){
        return specification;
    }
    

    List<M_MAINTENANCE_MODE> findAll();
    
    List<M_MAINTENANCE_MODE> findByStatus(String status);

}
