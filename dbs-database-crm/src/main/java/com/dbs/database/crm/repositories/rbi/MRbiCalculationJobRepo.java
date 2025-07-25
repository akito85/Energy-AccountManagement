package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_CALCULATION_JOB;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MRbiCalculationJobRepo extends PagingAndSortingRepository<M_RBI_CALCULATION_JOB,Integer>, 
        JpaSpecificationExecutor<M_RBI_CALCULATION_JOB>, JpaRepository<M_RBI_CALCULATION_JOB, Integer> {
    default Specification<M_RBI_CALCULATION_JOB> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_RBI_CALCULATION_JOB> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_RBI_CALCULATION_JOB>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_RBI_CALCULATION_JOB>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_RBI_CALCULATION_JOB> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_RBI_CALCULATION_JOB> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_RBI_CALCULATION_JOB> addDefaultFilters(Specification<M_RBI_CALCULATION_JOB> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        specification = (Specification<M_RBI_CALCULATION_JOB>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);

        /*INFO: Add Cost Center Filter*/
//        List<String> tes = null;//Get Cost Center From Username To Be Done
        return specification;
    }

    Optional<M_RBI_CALCULATION_JOB> findByCalCode (String calCode);

    Optional<M_RBI_CALCULATION_JOB> findByBillingCycle (Integer billingCycle);

    Optional<M_RBI_CALCULATION_JOB> findByCalculationType (Integer calType);

    Optional<M_RBI_CALCULATION_JOB> findByScheduleType (Integer scheduleType);

    Optional<List<M_RBI_CALCULATION_JOB>> findByPrefixCode (String prefixCode);

    List<M_RBI_CALCULATION_JOB> findAll ();
    
    @Query(value = "SELECT COALESCE(TO_NUMBER(MAX(SUBSTR(CALCULATION_CODE, 8, 12))),0) FROM M_RBI_CALCULATION_JOB WHERE PREFIX_CODE = :prefix", nativeQuery = true)
    Integer findMaxCodeByPrefix(String prefix);
}
