package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_APPROVAL_USAGE;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
public interface VwApprovalUsageRepo extends PagingAndSortingRepository<VW_APPROVAL_USAGE, Integer>, JpaSpecificationExecutor<VW_APPROVAL_USAGE> {
    default Specification<VW_APPROVAL_USAGE> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter){
        Specification<VW_APPROVAL_USAGE> specification = null;

        int i=0;
        for(String sr : pagingData.getSearch()){
            specification =
                    i == 0 ?
                            (Specification<VW_APPROVAL_USAGE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_APPROVAL_USAGE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification,filter,false);
        return specification;
    }

    default Specification<VW_APPROVAL_USAGE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_APPROVAL_USAGE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }
    default Specification<VW_APPROVAL_USAGE> addDefaultFilters(Specification<VW_APPROVAL_USAGE> specification, Map<String, Object> filter, Boolean isFirst){
        if(isFirst){
            specification = Specification.where((Specification<VW_APPROVAL_USAGE>) PagingUtils.createSpecification("positionId~"+filter.get("positionId").toString(),"EQUALS"));
        }else{
            specification = specification.and((Specification<VW_APPROVAL_USAGE>) PagingUtils.createSpecification("positionId~"+filter.get("positionId").toString(),"EQUALS"));
        }
        return specification;
    }
}
