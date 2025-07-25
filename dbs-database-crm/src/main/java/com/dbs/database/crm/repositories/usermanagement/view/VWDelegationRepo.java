package com.dbs.database.crm.repositories.usermanagement.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.view.VW_DELEGATION;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWDelegationRepo extends PagingAndSortingRepository<VW_DELEGATION, Integer>, JpaSpecificationExecutor<VW_DELEGATION> {
    default Specification<VW_DELEGATION> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_DELEGATION> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_DELEGATION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_DELEGATION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_DELEGATION> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_DELEGATION> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_DELEGATION> addDefaultFilters(Specification<VW_DELEGATION> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Entity Filter*/
        specification = (Specification<VW_DELEGATION>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);

        if(filter.get("type").toString().equals("REQUEST")){
            specification = (Specification<VW_DELEGATION>) PagingUtils.createHeaderFilterString(specification, "requestor",filter.get("userId").toString(), false);
        }else if(filter.get("type").toString().equals("APPROVAL")){
            specification = (Specification<VW_DELEGATION>) PagingUtils.createHeaderFilterString(specification, "delegateTo",filter.get("userId").toString(), false);
        }

        return specification;
    }


}
