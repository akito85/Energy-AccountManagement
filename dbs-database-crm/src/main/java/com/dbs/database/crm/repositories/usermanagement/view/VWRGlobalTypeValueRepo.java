package com.dbs.database.crm.repositories.usermanagement.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.view.VW_R_GLOBALTYPE_VALUE;
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
public interface VWRGlobalTypeValueRepo extends PagingAndSortingRepository<VW_R_GLOBALTYPE_VALUE, Integer>, JpaSpecificationExecutor<VW_R_GLOBALTYPE_VALUE> {
    default Specification<VW_R_GLOBALTYPE_VALUE> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_R_GLOBALTYPE_VALUE> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_R_GLOBALTYPE_VALUE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_R_GLOBALTYPE_VALUE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_R_GLOBALTYPE_VALUE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_R_GLOBALTYPE_VALUE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_R_GLOBALTYPE_VALUE> addDefaultFilters(Specification<VW_R_GLOBALTYPE_VALUE> specification, Map<String, Object> filter, Boolean isFirst){

        specification =  isFirst ? (Specification<VW_R_GLOBALTYPE_VALUE>) where(PagingUtils.createSpecification("glbTypeId~"+filter.get("glbTypeId"), DEFAULT_SELECTOR))
                :
                specification.and((Specification<VW_R_GLOBALTYPE_VALUE>)PagingUtils.createSpecification("glbTypeId~"+filter.get("glbTypeId"),DEFAULT_SELECTOR));

        return specification;
    }

    List<VW_R_GLOBALTYPE_VALUE> findAllByGlbTypeId(Integer glbTypeId);

}
