package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_BATCH_USAGE;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.GET_CC_CHILD;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
public interface MRbiBatchUsageRepo extends PagingAndSortingRepository<M_RBI_BATCH_USAGE, Integer>, JpaSpecificationExecutor<M_RBI_BATCH_USAGE> {
    default Specification<M_RBI_BATCH_USAGE> getSpecficationFromFilters(MaterialTablePagingRequest pagingData){
        Specification<M_RBI_BATCH_USAGE> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingData.getSearch()){
            specification =
                    i == 0 ? (Specification<M_RBI_BATCH_USAGE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_RBI_BATCH_USAGE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<M_RBI_BATCH_USAGE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_RBI_BATCH_USAGE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_RBI_BATCH_USAGE> addDefaultFilters(Specification<M_RBI_BATCH_USAGE> specification, Map<String, Object> filter, Boolean isFirst){
        return specification;
    }

    List<M_RBI_BATCH_USAGE> findAll();

    Optional<M_RBI_BATCH_USAGE> findByBatchId(Integer batchId);
    @Query(value = "SELECT mrbu.BATCH_ID FROM M_RBI_BATCH_USAGE mrbu ORDER BY DBMS_RANDOM.VALUE FETCH FIRST 1 ROW ONLY", nativeQuery = true)
    Object[] findRandomId();
}
