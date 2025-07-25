package com.dbs.database.crm.repositories.usermanagement.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.view.VW_POSITION;
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
public interface VWPositionRepo extends PagingAndSortingRepository<VW_POSITION, Integer>, JpaSpecificationExecutor<VW_POSITION> {
    default Specification<VW_POSITION> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_POSITION> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_POSITION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_POSITION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<VW_POSITION> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_POSITION> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_POSITION> addDefaultFilters(Specification<VW_POSITION> specification, Map<String, Object> filter, Boolean isFirst) {
        if (filter.get("entityId") != null) {
            specification = (Specification<VW_POSITION>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
            specification = (Specification<VW_POSITION>) PagingUtils.createIsDeletedFilter(specification, false, false);
        }
        return specification;
    }

    @Override
    public List<VW_POSITION> findAll();
    
    List<VW_POSITION> findAllByStatusAndIsDeleted(String status, Boolean isDeleted);
    
    
}
