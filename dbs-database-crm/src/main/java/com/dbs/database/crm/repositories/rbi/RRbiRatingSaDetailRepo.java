package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.R_RBI_RATING_SA_DETAIL;
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
public interface RRbiRatingSaDetailRepo extends PagingAndSortingRepository<R_RBI_RATING_SA_DETAIL, Integer>, JpaSpecificationExecutor<R_RBI_RATING_SA_DETAIL> {
    default Specification<R_RBI_RATING_SA_DETAIL> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter){
        Specification<R_RBI_RATING_SA_DETAIL> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingData.getSearch()){
            specification =
                    i == 0 ? (Specification<R_RBI_RATING_SA_DETAIL>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<R_RBI_RATING_SA_DETAIL>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<R_RBI_RATING_SA_DETAIL> getSpecificationDefault(Map<String, Object> filter){
        Specification<R_RBI_RATING_SA_DETAIL> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<R_RBI_RATING_SA_DETAIL> addDefaultFilters(Specification<R_RBI_RATING_SA_DETAIL> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        if(specification != null ){
            if(filter.get("ratingSaId") != null) {
                specification =specification.and((Specification<R_RBI_RATING_SA_DETAIL>) PagingUtils.createSpecification("ratingSaId~" + filter.get("ratingSaId"), EQUALS_SELECTOR));
            }
        }else{
            specification = ((Specification<R_RBI_RATING_SA_DETAIL>) PagingUtils.createSpecification("ratingSaId~" + filter.get("ratingSaId"), EQUALS_SELECTOR));
        }
        return specification;
    }
    List<R_RBI_RATING_SA_DETAIL> findAllByRatingSaId(Integer ratingSaId);
    List<R_RBI_RATING_SA_DETAIL> findAll();
}
