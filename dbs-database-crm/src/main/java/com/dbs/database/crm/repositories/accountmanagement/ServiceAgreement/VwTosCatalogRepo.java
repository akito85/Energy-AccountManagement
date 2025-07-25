package com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.VW_TOS_CATALOG;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwTosCatalogRepo extends PagingAndSortingRepository<VW_TOS_CATALOG, Integer>, JpaSpecificationExecutor<VW_TOS_CATALOG> {
    default Specification<VW_TOS_CATALOG> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_TOS_CATALOG> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_TOS_CATALOG>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_TOS_CATALOG>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_TOS_CATALOG> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_TOS_CATALOG> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_TOS_CATALOG> addDefaultFilters(Specification<VW_TOS_CATALOG> specification, Map<String, Object> filter, Boolean isFirst){
        if(filter.get("saId") != null) {
            specification = (Specification<VW_TOS_CATALOG>) PagingUtils.createSpecification("saId~" + (filter.get("saId")),EQUALS_SELECTOR);
        }

        return specification;
    }
    List<VW_TOS_CATALOG> findAll();
}
