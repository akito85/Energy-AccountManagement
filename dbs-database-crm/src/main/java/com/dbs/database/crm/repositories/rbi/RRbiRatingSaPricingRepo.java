package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.R_RBI_RATING_SA;
import com.dbs.database.crm.entities.ratingbillinginvoice.R_RBI_RATING_SA_PRICING;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
public interface RRbiRatingSaPricingRepo extends PagingAndSortingRepository<R_RBI_RATING_SA_PRICING, Integer>, JpaSpecificationExecutor<R_RBI_RATING_SA_PRICING> {
    default Specification<R_RBI_RATING_SA_PRICING> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter){
        Specification<R_RBI_RATING_SA_PRICING> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingData.getSearch()){
            specification =
                    i == 0 ? (Specification<R_RBI_RATING_SA_PRICING>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<R_RBI_RATING_SA_PRICING>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<R_RBI_RATING_SA_PRICING> getSpecificationDefault(Map<String, Object> filter){
        Specification<R_RBI_RATING_SA_PRICING> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<R_RBI_RATING_SA_PRICING> addDefaultFilters(Specification<R_RBI_RATING_SA_PRICING> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        return specification;
    }
    List<R_RBI_RATING_SA_PRICING> findAllByRatingSaId(Integer ratingSaId);
    List<R_RBI_RATING_SA_PRICING> findAll();
    Optional<R_RBI_RATING_SA_PRICING> findFirstByRatingSaId(Integer ratingSaId);
}
