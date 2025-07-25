package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.R_RBI_CALCULATION_ACCOUNT_GROUP_TYPE;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

public interface RRbiCalcAccGroupType extends PagingAndSortingRepository<R_RBI_CALCULATION_ACCOUNT_GROUP_TYPE,Integer>, JpaSpecificationExecutor<R_RBI_CALCULATION_ACCOUNT_GROUP_TYPE> {
    default Specification<R_RBI_CALCULATION_ACCOUNT_GROUP_TYPE> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<R_RBI_CALCULATION_ACCOUNT_GROUP_TYPE> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<R_RBI_CALCULATION_ACCOUNT_GROUP_TYPE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<R_RBI_CALCULATION_ACCOUNT_GROUP_TYPE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<R_RBI_CALCULATION_ACCOUNT_GROUP_TYPE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<R_RBI_CALCULATION_ACCOUNT_GROUP_TYPE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<R_RBI_CALCULATION_ACCOUNT_GROUP_TYPE> addDefaultFilters(Specification<R_RBI_CALCULATION_ACCOUNT_GROUP_TYPE> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
//        specification = (Specification<R_RBI_CALCULATION_ACCOUNT_GROUP_TYPE>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);

        /*INFO: Add Cost Center Filter*/
//        List<String> tes = null;//Get Cost Center From Username To Be Done
        return specification;
    }

    Optional<R_RBI_CALCULATION_ACCOUNT_GROUP_TYPE> findAllByCalCode (String calCode);
}
