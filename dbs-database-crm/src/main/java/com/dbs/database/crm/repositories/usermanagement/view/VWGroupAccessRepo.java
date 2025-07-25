package com.dbs.database.crm.repositories.usermanagement.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.view.VW_GROUPACCESS;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import java.util.List;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWGroupAccessRepo extends PagingAndSortingRepository<VW_GROUPACCESS, Integer>, JpaSpecificationExecutor<VW_GROUPACCESS> {

    default Specification<VW_GROUPACCESS> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_GROUPACCESS> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_GROUPACCESS>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_GROUPACCESS>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<VW_GROUPACCESS> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_GROUPACCESS> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_GROUPACCESS> addDefaultFilters(Specification<VW_GROUPACCESS> specification, Map<String, Object> filter, Boolean isFirst) {
        if (filter.get("entityId") != null) {
            specification = (Specification<VW_GROUPACCESS>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        }
        return specification;
    }

    @Override
    public List<VW_GROUPACCESS> findAll();
    
    
}
