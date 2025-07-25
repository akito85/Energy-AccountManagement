package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_CALCULATION_RESULT;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

public interface MRbiCalculationResultRepo extends PagingAndSortingRepository<M_RBI_CALCULATION_RESULT, Integer>,
        JpaSpecificationExecutor<M_RBI_CALCULATION_RESULT>,
        JpaRepository<M_RBI_CALCULATION_RESULT, Integer>{
    default Specification<M_RBI_CALCULATION_RESULT> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_RBI_CALCULATION_RESULT> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_RBI_CALCULATION_RESULT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_RBI_CALCULATION_RESULT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_RBI_CALCULATION_RESULT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_RBI_CALCULATION_RESULT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_RBI_CALCULATION_RESULT> addDefaultFilters(Specification<M_RBI_CALCULATION_RESULT> specification, Map<String, Object> filter, Boolean isFirst) {
        /*INFO: Add Entity Filter*/
        specification = (Specification<M_RBI_CALCULATION_RESULT>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        return specification;
    }


    @Query(nativeQuery = true, value = "SELECT * FROM M_RBI_CALCULATION_RESULT \n" +
            "WHERE CALCULATION_CODE = :calCode \n" +
            "AND CALCULATION_TYPE = :calType \n" +
            "AND ACCOUNT_NUMBER in :accNumb \n" +
            "AND ACTION = :action")
    List<M_RBI_CALCULATION_RESULT> findWithAccNumb(
            String calCode,
            Integer calType,
            List<String> accNumb,
            String action
    );

    List<M_RBI_CALCULATION_RESULT> findAll();

    List<M_RBI_CALCULATION_RESULT> findAllByCalCodeAndBillingPeriodAndIsTryAndStatus (
            String calCode,
            Integer billPeriod,
            String isTry,
            String Status);
    List<M_RBI_CALCULATION_RESULT> findAllByResultIdInAndIsTry (List<Integer>resultId, String isTry);

    List<M_RBI_CALCULATION_RESULT> findAllByCalCode(String calCode);

    @Query(nativeQuery = true, value = "SELECT * FROM M_RBI_CALCULATION_RESULT mrcr \n" +
            "WHERE MRCR.ACCOUNT_NUMBER = :accNumb \n" +
            "AND MRCR.BILLING_PERIOD = :periodId \n" +
            "AND MRCR.BILLING_CYCLE = :cycleId \n" +
            "AND MRCR.CALCULATION_TYPE NOT IN (624)\n" +
            "AND MRCR.IS_TRY = 'N'\n" +
            "AND MRCR.STATUS = 'FAILED'")
    Optional<M_RBI_CALCULATION_RESULT> findByAccNumbAndBillingCycleAndBillingPeriod (String accNumb, Integer cycleId, Integer periodId);

    List<M_RBI_CALCULATION_RESULT> findAllByCalCodeAndAccNumbIn(String calCode, List<String>accNumb);

    List<M_RBI_CALCULATION_RESULT> findAllByCalCodeAndCalTypeAndIsTryAndAccNumbIn(String calCode,Integer calType,String isTry, List<String>accNumb);

    List<M_RBI_CALCULATION_RESULT> findAllByCalCodeAndCalTypeAndIsTryAndStatus(String calCode, Integer calType, String isTry, String status);

    List<M_RBI_CALCULATION_RESULT> findAllByCalCodeAndCalTypeAndStatusAndIsTry(String calCode, Integer calType, String status, String isTry);
}
