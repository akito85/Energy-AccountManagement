package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_RATING_CALC_USAGE;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

public interface VwRatingCalcUsageRepo extends PagingAndSortingRepository<VW_RATING_CALC_USAGE, Integer>, JpaSpecificationExecutor<VW_RATING_CALC_USAGE> {
    default Specification<VW_RATING_CALC_USAGE> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter){
        Specification<VW_RATING_CALC_USAGE> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingData.getSearch()){
            specification =
                    i == 0 ? (Specification<VW_RATING_CALC_USAGE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_RATING_CALC_USAGE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters(specification, filter, false);
        /*code here*/

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<VW_RATING_CALC_USAGE> getSpecificationDefault(Map<String, Object> filter){
        Specification<VW_RATING_CALC_USAGE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_RATING_CALC_USAGE> addDefaultFilters(Specification<VW_RATING_CALC_USAGE> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        if(specification != null ){
            if(filter.get("ratingCode") != null) {
                specification =specification.and((Specification<VW_RATING_CALC_USAGE>) PagingUtils.createSpecification("ratingCode~" + filter.get("ratingCode"), EQUALS_SELECTOR));
            }
        }else{
            specification = ((Specification<VW_RATING_CALC_USAGE>) PagingUtils.createSpecification("ratingCode~" + filter.get("ratingCode"), EQUALS_SELECTOR));
        }
        return specification;
    }
    List<VW_RATING_CALC_USAGE> findAll();
}
