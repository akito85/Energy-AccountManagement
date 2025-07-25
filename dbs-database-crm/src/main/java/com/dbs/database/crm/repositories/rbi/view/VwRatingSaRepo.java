package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_RATING_SA;
import com.dbs.database.crm.repositories.rbi.RRbiRatingSaRepo;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository

public interface VwRatingSaRepo extends PagingAndSortingRepository<VW_RATING_SA, Integer>, JpaSpecificationExecutor<VW_RATING_SA> {
    default Specification<VW_RATING_SA> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter){
        Specification<VW_RATING_SA> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingData.getSearch()){
            specification =
                    i == 0 ? (Specification<VW_RATING_SA>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_RATING_SA>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_RATING_SA> getSpecificationDefault(Map<String, Object> filter){
        Specification<VW_RATING_SA> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_RATING_SA> addDefaultFilters(Specification<VW_RATING_SA> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        if(specification != null ){
            if(filter.get("ratingCode") != null) {
                specification =specification.and((Specification<VW_RATING_SA>) PagingUtils.createSpecification("ratingCode~" + filter.get("ratingCode"), EQUALS_SELECTOR));
            }
        }else{
            specification = ((Specification<VW_RATING_SA>) PagingUtils.createSpecification("ratingCode~" + filter.get("ratingCode"), EQUALS_SELECTOR));
        }
        return specification;
    }
    List<VW_RATING_SA> findAllByRatingCode(String ratingCode);
    List<VW_RATING_SA> findAll();
}
