package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_CALC_RESULT;
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
public interface VwCalcResultRepo extends PagingAndSortingRepository<VW_CALC_RESULT,Integer>, JpaSpecificationExecutor<VW_CALC_RESULT> {
    default Specification<VW_CALC_RESULT> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_CALC_RESULT> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_CALC_RESULT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_CALC_RESULT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_CALC_RESULT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_CALC_RESULT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_CALC_RESULT> addDefaultFilters(Specification<VW_CALC_RESULT> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        specification = (Specification<VW_CALC_RESULT>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        /*INFO: Add Cost Center Filter*/
//        List<String> tes = null;//Get Cost Center From Username To Be Done
        return specification;
    }

    List<VW_CALC_RESULT> findAllByCalCodeAndCalTypeAndIsTry (String calCode, Integer calType, Boolean isTry);
}

