package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_CALC_HISTORY;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_CALC_JOB;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.GET_CC_PARENT;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwCalcHistoryRepo extends PagingAndSortingRepository<VW_CALC_HISTORY,Integer>, JpaSpecificationExecutor<VW_CALC_HISTORY> {
    default Specification<VW_CALC_HISTORY> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_CALC_HISTORY> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_CALC_HISTORY>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_CALC_HISTORY>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_CALC_HISTORY> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_CALC_HISTORY> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_CALC_HISTORY> addDefaultFilters(Specification<VW_CALC_HISTORY> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        specification = (Specification<VW_CALC_HISTORY>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        /*INFO: Add Cost Center Filter*/
        if(filter.get("positionId") != null) {
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()),GET_CC_PARENT);
            specification = (Specification<VW_CALC_HISTORY>) PagingUtils.createCostCenterFilter(specification, ccList, false);
        }
        return specification;
    }

    List<VW_CALC_HISTORY> findAll();
}

