package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.R_PRICING_CRITERIA_DATA;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_RATING_RESULT;
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
public interface VwRatingResultRepo extends PagingAndSortingRepository<VW_RATING_RESULT,Integer>, JpaSpecificationExecutor<VW_RATING_RESULT> {
    default Specification<VW_RATING_RESULT> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_RATING_RESULT> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_RATING_RESULT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_RATING_RESULT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_RATING_RESULT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_RATING_RESULT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_RATING_RESULT> addDefaultFilters(Specification<VW_RATING_RESULT> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        if (specification == null){
            specification = (Specification<VW_RATING_RESULT>)PagingUtils.createSpecification("ratingCode~"+filter.get("ratingCode"),EQUALS_SELECTOR);
        } else {
            specification = specification.and((Specification<VW_RATING_RESULT>)PagingUtils.createSpecification("ratingCode~"+filter.get("ratingCode"),EQUALS_SELECTOR));
        }
//        if(filter.get("ratingCode") != null){
//            specification = (Specification<VW_RATING_RESULT>)PagingUtils.createSpecification("ratingCode~"+filter.get("ratingCode"),EQUALS_SELECTOR);
//        }
        /*INFO: Add Cost Center Filter*/
//        List<String> tes = null;//Get Cost Center From Username To Be Done
        return specification;
    }

    @Override
    public List<VW_RATING_RESULT> findAll();
}
