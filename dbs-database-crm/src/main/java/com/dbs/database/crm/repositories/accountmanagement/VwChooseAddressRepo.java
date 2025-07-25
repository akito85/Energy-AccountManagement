package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.VW_CHOOSE_ADDRESS;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

public interface VwChooseAddressRepo extends PagingAndSortingRepository<VW_CHOOSE_ADDRESS, Integer>, JpaSpecificationExecutor<VW_CHOOSE_ADDRESS> {

    default Specification<VW_CHOOSE_ADDRESS> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_CHOOSE_ADDRESS> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_CHOOSE_ADDRESS>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_CHOOSE_ADDRESS>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_CHOOSE_ADDRESS> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_CHOOSE_ADDRESS> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_CHOOSE_ADDRESS> addDefaultFilters(Specification<VW_CHOOSE_ADDRESS> specification, Map<String, Object> filter, Boolean isFirst){
//        /*INFO: Add Entity Filter*/

        if(filter.get("status") != null) {
            if(specification == null) {
                specification =(Specification<VW_CHOOSE_ADDRESS>) PagingUtils.createSpecification("status~" + filter.get("status"), EQUALS_SELECTOR);
            } else {
                specification =specification.and((Specification<VW_CHOOSE_ADDRESS>) PagingUtils.createSpecification("status~" + filter.get("status"), EQUALS_SELECTOR));
            }
        }
        return specification;
    }

}
