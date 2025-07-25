package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNT_BILLING_BUCKET;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_RATING;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_RBI_RATING;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwRbiRatingRepo extends PagingAndSortingRepository<VW_RBI_RATING,String>, JpaSpecificationExecutor<VW_RBI_RATING> {
    default Specification<VW_RBI_RATING> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_RBI_RATING> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_RBI_RATING>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_RBI_RATING>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_RBI_RATING> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_RBI_RATING> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_RBI_RATING> addDefaultFilters(Specification<VW_RBI_RATING> specification, Map<String, Object> filter, Boolean isFirst){

        if(filter.get("positionId") != null) {
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()),GET_CC_CHILD);
            specification = (Specification<VW_RBI_RATING>) PagingUtils.createCostCenterFilter(specification, ccList, isFirst);
        }


        return specification;
    }
    List<VW_RBI_RATING> findAll();
    List<VW_RBI_RATING> findAllByRatingCode(String ratingCode);

    @Query(nativeQuery = true, value = "SELECT * FROM VW_RBI_RATING RR\n" +
            "WHERE RR.ACCOUNT_NUMBER = :accNumb AND RR.BILLING_PERIOD_ID = :billPeriod AND RR.BILLING_CYCLE_ID = :billCycle")
    List<VW_RBI_RATING> findAllByAccountNumber(String accNumb, Integer billPeriod, Integer billCycle);
}
