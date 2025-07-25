package com.dbs.database.crm.repositories.usermanagement.view;

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
import com.dbs.database.crm.entities.usermanagement.view.VW_PENDING_APPROVAL;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWPendingApprRepo extends PagingAndSortingRepository<VW_PENDING_APPROVAL, Integer>, JpaSpecificationExecutor<VW_PENDING_APPROVAL> {
    default Specification<VW_PENDING_APPROVAL> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_PENDING_APPROVAL> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_PENDING_APPROVAL>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_PENDING_APPROVAL>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<VW_PENDING_APPROVAL> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_PENDING_APPROVAL> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_PENDING_APPROVAL> addDefaultFilters(Specification<VW_PENDING_APPROVAL> specification, Map<String, Object> filter, Boolean isFirst) {
        if (filter.get("entityId") != null) {
            specification = (Specification<VW_PENDING_APPROVAL>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        }
        specification = (Specification<VW_PENDING_APPROVAL>) PagingUtils.createAppHierIdFilter(specification, Integer.parseInt(filter.get("apphierId").toString()), isFirst);
        return specification;
    }

    @Override
    public List<VW_PENDING_APPROVAL> findAll();
    
    List<VW_PENDING_APPROVAL> findByApphierIdAndEntityId(Integer apphierId, Integer entityId);
    
    List<VW_PENDING_APPROVAL> findByPositionId(Integer positionId);
    
    List<VW_PENDING_APPROVAL> findByApphierId(Integer apphierId);
    
    
}
