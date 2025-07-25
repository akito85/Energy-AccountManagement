package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.T_RBI_POS_BUCKET;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface TRbiPosBucketRepo extends PagingAndSortingRepository<T_RBI_POS_BUCKET,Integer>, JpaSpecificationExecutor<T_RBI_POS_BUCKET> {
    default Specification<T_RBI_POS_BUCKET> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<T_RBI_POS_BUCKET> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<T_RBI_POS_BUCKET>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<T_RBI_POS_BUCKET>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<T_RBI_POS_BUCKET> getSpecificationDefault(Map<String, Object> filter) {
        Specification<T_RBI_POS_BUCKET> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<T_RBI_POS_BUCKET> addDefaultFilters(Specification<T_RBI_POS_BUCKET> specification, Map<String, Object> filter, Boolean isFirst) {
        /*INFO: Add Entity Filter*/
        return specification;
    }
}
