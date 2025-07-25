package com.dbs.database.crm.repositories.usermanagement.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.view.VW_R_POS_HIER;
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
public interface VWRPosHierarchyRepo extends PagingAndSortingRepository<VW_R_POS_HIER, Integer>, JpaSpecificationExecutor<VW_R_POS_HIER> {

    List<VW_R_POS_HIER> findAllByHierIdAndParentIdAndStatus(Integer hierId, Integer parentId, String status);

    default Specification<VW_R_POS_HIER> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_R_POS_HIER> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_R_POS_HIER>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_R_POS_HIER>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters(specification, filter,false);
        /*code here*/

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<VW_R_POS_HIER> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_R_POS_HIER> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_R_POS_HIER> addDefaultFilters(Specification<VW_R_POS_HIER> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        specification = (Specification<VW_R_POS_HIER>) PagingUtils.createHeaderFilter(specification, "hierId",Integer.parseInt(filter.get("hierId").toString()), isFirst);

        /*INFO: Is Deleted Filter*/
        if(!filter.get("status").toString().isEmpty()){
            specification = (Specification<VW_R_POS_HIER>) PagingUtils.createStatusFilter(specification, filter.get("status").toString(), false);
        }

        return specification;
    }


}
